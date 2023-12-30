/*
 *  Copyright 2023 Pablo Baxter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Created by Pablo Baxter (Github: pablobaxter)
 * https://github.com/pablobaxter/task-avoidance
 */

import * as github from '@actions/github'
import * as tc from '@actions/tool-cache'
import * as exec from '@actions/exec'
import * as path from 'path'

class ProjectProviderAsset {
  projectProviderDownloadUrl: string
  projectProviderVersion: string
  constructor(url: string, version: string) {
    this.projectProviderDownloadUrl = url
    this.projectProviderVersion = version
  }
}

const IS_WINDOWS = process.platform === 'win32'

export async function runProjectProvider(params: {
  projectProviderVersion: string
  autoInjectDisabled: boolean
  gradleLoggingDisabled: boolean
  gradleParallelDisabled: boolean
  useIncludedBuilds: boolean
  changedFiles?: string[]
  gradleInstallationPath?: string
  buildDir?: string
  comparisonCommit?: string
  affectedProjectsOutput?: string
  serializedProjectsOutput?: string
  githubToken?: string | undefined
}): Promise<number> {
  const projectProviderAsset = await getProjectProvierAsset(params.projectProviderVersion, params.githubToken)

  const projectProviderPath = await getProjectProvider(projectProviderAsset)
  const binExecutable = IS_WINDOWS ? 'bin/project-provider.bat' : 'bin/project-provider'
  const toExecute = path.resolve(projectProviderPath, binExecutable)
  const status = await exec.exec(toExecute, generateArgs(params), {
    cwd: params.buildDir,
    ignoreReturnCode: true
  })

  return status
}

async function getProjectProvierAsset(
  releaseVersion: string,
  githubToken: string | undefined
): Promise<ProjectProviderAsset> {
  const token = process.env.GITHUB_TOKEN ?? githubToken
  if (!token) {
    throw new Error('No github token found')
  }
  const octokit = github.getOctokit(token)
  if (releaseVersion) {
    const githubRelease = await octokit.rest.repos.getLatestRelease({
      owner: 'pablobaxter',
      repo: 'task-avoidance'
    })
    const releaseAsset = githubRelease.data.assets.find(asset => asset.name.startsWith('project-provider-'))
    return new ProjectProviderAsset(releaseAsset?.browser_download_url ?? '', githubRelease.data.tag_name)
  } else {
    const githubRelease = await octokit.rest.repos.getReleaseByTag({
      owner: 'pablobaxter',
      repo: 'task-avoidance',
      tag: releaseVersion
    })
    const releaseAsset = githubRelease.data.assets.find(asset => asset.name.startsWith('project-provider-'))
    return new ProjectProviderAsset(releaseAsset?.browser_download_url ?? '', githubRelease.data.tag_name)
  }
}

async function getProjectProvider(projectProviderAsset: ProjectProviderAsset): Promise<string> {
  const cachedProvider = tc.find('project-provider', projectProviderAsset.projectProviderVersion)
  if (cachedProvider) {
    return extractProjectProvider(cachedProvider)
  } else {
    const tool = await download(projectProviderAsset)
    await tc.cacheDir(tool, 'project-provider', projectProviderAsset.projectProviderVersion)
    return extractProjectProvider(tool)
  }
}

async function download(projectProviderAsset: ProjectProviderAsset): Promise<string> {
  return await tc.downloadTool(projectProviderAsset.projectProviderDownloadUrl)
}

async function extractProjectProvider(projectProvider: string): Promise<string> {
  return tc.extractTar(projectProvider)
}

function generateArgs(params: {
  autoInjectDisabled: boolean
  gradleLoggingDisabled: boolean
  gradleParallelDisabled: boolean
  useIncludedBuilds: boolean
  changedFiles?: string[]
  gradleInstallationPath?: string
  comparisonCommit?: string
  affectedProjectsOutput?: string
  serializedProjectsOutput?: string
}): string[] {
  const result: string[] = []
  result.push(
    `--log-gradle=${params.gradleLoggingDisabled}`,
    `--inject-plugin=${params.autoInjectDisabled}`,
    `--allow-gradle-parallel=${params.gradleParallelDisabled}`,
    `--use-included-builds=${params.useIncludedBuilds}`
  )
  if (params.changedFiles) {
    for (const file of params.changedFiles) {
      result.push(`-f=${file}`)
    }
  }

  if (params.gradleInstallationPath) {
    result.push(`--gradle-installation-path=${params.gradleInstallationPath}`)
  }

  if (params.comparisonCommit) {
    result.push(`--comparison-commit=${params.comparisonCommit}`)
  }

  if (params.affectedProjectsOutput) {
    result.push(`--affected-projects-output=${params.affectedProjectsOutput}`)
  }

  if (params.serializedProjectsOutput) {
    result.push(`--serialized-projects-output=${params.serializedProjectsOutput}`)
  }

  return result
}

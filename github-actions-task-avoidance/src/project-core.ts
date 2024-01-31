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

import * as core from '@actions/core'
import fs from 'fs'

export function getGradleTasks(): string[] {
  return core.getMultilineInput('gradle-tasks')
}

export function isAutoInjectDisabled(): boolean {
  return core.getBooleanInput('disable-plugin-inject')
}

export function isGradleLoggingDisabled(): boolean {
  return core.getBooleanInput('disable-gradle-logging')
}

export function getBuildDir(): string {
  return core.getInput('build-dir')
}

export function getComparisonCommit(): string {
  return core.getInput('comparison-commit')
}

export function isGradleParallelDisabled(): boolean {
  return core.getBooleanInput('disable-gradle-parallel')
}

export function getChangedFiles(): string[] {
  return core.getMultilineInput('changed-files')
}

export function getGradleInstallationPath(): string {
  return core.getInput('gradle-installation-path')
}

export function useIncludedBuilds(): boolean {
  return core.getBooleanInput('use-included-builds')
}

export function getToken(): string {
  return core.getInput('token')
}

export function getProjectProviderVersion(): string {
  return core.getInput('project-provider-version')
}

export function getAffectedProjectsOutputFile(): string {
  return core.getInput('affected-projects-output-file')
}

export function getSerializedProjectsOutputFile(): string {
  return core.getInput('serialized-projects-output-file')
}

export function setAffectedProjectsOutput(): void {
  const affectedProjectsOutputFile = getAffectedProjectsOutputFile() ?? 'affected-projects.out'
  if (fs.existsSync(affectedProjectsOutputFile) && fs.lstatSync(affectedProjectsOutputFile).isFile()) {
    const affectedProjectsList = fs.readFileSync(affectedProjectsOutputFile, 'utf-8').split('\r\n').join(',')
    core.setOutput('affected-projects', affectedProjectsList)
  } else {
    throw new Error(`Path ${affectedProjectsOutputFile} does not exist or is not a file`)
  }
}

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Gradle Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Source: https://github.com/gradle/gradle-build-action
 */

import * as path from 'path'
import fs from 'fs'

const IS_WINDOWS = process.platform === 'win32'

export function wrapperScriptFilename(): string {
  return IS_WINDOWS ? './gradlew.bat' : './gradlew'
}

export function installScriptFilename(): string {
  return IS_WINDOWS ? 'gradle.bat' : 'gradle'
}

export function gradleWrapperScript(buildRootDirectory: string): string {
  validateGradleWrapper(buildRootDirectory)
  return wrapperScriptFilename()
}

function validateGradleWrapper(buildRootDirectory: string): void {
  const wrapperScript = path.resolve(buildRootDirectory, wrapperScriptFilename())
  verifyExists(wrapperScript, 'Gradle Wrapper script')
  verifyIsExecutableScript(wrapperScript)

  const wrapperProperties = path.resolve(buildRootDirectory, 'gradle/wrapper/gradle-wrapper.properties')
  verifyExists(wrapperProperties, 'Gradle wrapper properties file')
}

function verifyExists(file: string, description: string): void {
  if (!fs.existsSync(file)) {
    throw new Error(
      `Cannot locate ${description} at '${file}'. Specify 'gradle-version' or 'gradle-executable' for projects without Gradle wrapper configured.`
    )
  }
}

function verifyIsExecutableScript(toExecute: string): void {
  try {
    fs.accessSync(toExecute, fs.constants.X_OK)
  } catch (err) {
    throw new Error(`Gradle script '${toExecute}' is not executable.`)
  }
}

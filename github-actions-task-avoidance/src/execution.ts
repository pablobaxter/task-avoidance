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

import * as core from '@actions/core'
import * as exec from '@actions/exec'
import * as gradlew from './gradlew'

export async function executeGradleBuild(executable: string | undefined, root: string, args: string[]): Promise<void> {
  // Use the provided executable, or look for a Gradle wrapper script to run
  const toExecute = executable ?? gradlew.gradleWrapperScript(root)

  const status: number = await exec.exec(toExecute, args, {
    cwd: root,
    ignoreReturnCode: true
  })

  if (status !== 0) {
    core.setFailed(`Gradle build failed: see console output for details`)
  }
}

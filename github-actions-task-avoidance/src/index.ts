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

export async function main(): Promise<void> {
  // Check for cached results (affected-path if 'github.run_id' is found, serialized projects for same sha of gradle files and buildSrc directory)
  // If same run-id, just run gradle tasks (affected-paths are already found)
  // If same sha, run project provider with given serialized file
  // else just run project provider normally

  // cache affected-path and serialized projects
  // run gradle tasks for affected paths

  //todo use serialized projects to run lint/ktlint or any other tasks that normally use Gradle but can be run faster without
}

main()

import * as gradlew from '../../src/gradlew'
import * as path from 'path'

const mockIS_WINDOWS = jest.fn()
jest.mock('../../src/constants', () => ({
    get IS_WINDOWS() {
        return mockIS_WINDOWS()
    }
}))

test('gradle wrapper is resolvable', async () => {
    mockIS_WINDOWS.mockReturnValue(false)
    var result = gradlew.gradleWrapperScript('../gradle-project-provider')
    expect(result).toBe(path.resolve('../gradle-project-provider/gradlew'))
})

test('gradle wrapper does not exist', async () => {
    expect(() => {
        gradlew.gradleWrapperScript('.')
    }).toThrow()
})

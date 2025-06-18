package com.epam.learnosity.converter.qti

import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

class AppSpec extends Specification {

    @TempDir
    Path tempDir

    def "should correctly convert all QTI XML files to Learnosity JSON format"() {
        given:
        def inputDir = Path.of("src/test/resources/qti")
        def expectedOutputDir = Path.of("src/test/resources/learnosity")
        def actualOutputDir = tempDir.resolve("output")

        and:
        Files.createDirectories(actualOutputDir)

        when:
        App.main([inputDir.toString(), actualOutputDir.toString()] as String[])

        then:
        def expectedFiles = Files.list(expectedOutputDir)
                .filter { Files.isRegularFile(it) && it.toString().endsWith('.json') }
                .toList()

        def actualFiles = Files.list(actualOutputDir)
                .filter { Files.isRegularFile(it) && it.toString().endsWith('.json') }
                .toList()

        expectedFiles.size() == actualFiles.size()

        expectedFiles.each { expectedFile ->
            def actualFile = actualOutputDir.resolve(expectedFile.fileName.toString())
            assert Files.exists(actualFile)

            def expectedContent = Files.readString(expectedFile)
            def actualContent = Files.readString(actualFile).normalize().replace("\\r\\n", "\\n")

            assert actualContent == expectedContent
        }
    }
}

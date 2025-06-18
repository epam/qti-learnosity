package com.epam.learnosity.converter.qti.core.loader.file

import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

class FileSourceLoaderSpec extends Specification {

    @TempDir
    Path tempDir

    def "save should create file with content in target directory"() {
        given:
        def loader = new FileSourceLoader(tempDir.toString())
        def fileName = "testFile.txt"
        def content = "line1\nline2\nline3"

        when:
        loader.save(fileName, content)

        then:
        Path filePath = tempDir.resolve(fileName)
        Files.exists(filePath)
        Files.readAllLines(filePath) == ["line1", "line2", "line3"]
    }

    def "save should create parent directories if they don't exist"() {
        given:
        def loader = new FileSourceLoader(tempDir.toString())
        def fileName = "subdir1/subdir2/testFile.txt"
        def content = "test content"

        when:
        loader.save(fileName, content)

        then:
        Path filePath = tempDir.resolve(fileName)
        Files.exists(filePath)
    }

    def "save should throw IOException when file already exists"() {
        given:
        def loader = new FileSourceLoader(tempDir.toString())
        def fileName = "existingFile.txt"
        def content = "test content"
        Files.createFile(tempDir.resolve(fileName))

        when:
        loader.save(fileName, content)

        then:
        IOException e = thrown()
        e.message.contains("File already exists")
    }

    def "getPathWithTrailingSlash should add slash to path if missing"() {
        expect:
        FileSourceLoader.getPathWithTrailingSlash(inputPath) == expectedPath

        where:
        inputPath       | expectedPath
        "path"          | "path/"
        "path/"        | "path/"
        "/long/path"    | "/long/path/"
        "/long/path/"   | "/long/path/"
        ""             | "/"
    }
}

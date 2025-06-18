package com.epam.learnosity.converter.qti.core.extractor.file

import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path

class FileSourceSupplierSpec extends Specification {

    @TempDir
    Path tempDir

    def "should throw IOException when directory doesn't exist"() {
        given:
        def nonExistentPath = tempDir.resolve("nonexistent").toString()
        def supplier = new FileSourceSupplier(nonExistentPath)

        when:
        supplier.getContentStream()

        then:
        thrown(IOException)
    }

    def "should return empty stream when directory is empty"() {
        given:
        def supplier = new FileSourceSupplier(tempDir.toString())

        when:
        def result = supplier.getContentStream()

        then:
        result.count() == 0
    }

    def "should return only XML files"() {
        given:
        Files.createFile(tempDir.resolve("test1.xml"))
        Files.createFile(tempDir.resolve("test2.xml"))
        Files.createFile(tempDir.resolve("test3.json"))
        def supplier = new FileSourceSupplier(tempDir.toString())

        when:
        def result = supplier.getContentStream().toList()

        then:
        result.size() == 2
        result.every { it.name().endsWith('.xml') }
    }

    @Unroll
    def "should correctly read file content for #fileName"() {
        given:
        def fileContent = "test content"
        def file = tempDir.resolve(fileName)
        Files.writeString(file, fileContent)
        def supplier = new FileSourceSupplier(tempDir.toString())

        when:
        def result = supplier.getContentStream().findFirst().get()

        then:
        result.name() == fileName
        result.content() == fileContent

        where:
        fileName << ["test.xml", "another.xml"]
    }

    // Ignored on Windows because setting the file as unreadable does not seem to work there
    @IgnoreIf({ System.getProperty("os.name").toLowerCase().contains("windows") })
    def "should wrap IOException in UncheckedIOException when reading file fails"() {
        given:
        def fileName = "test.xml"
        def file = tempDir.resolve(fileName)
        Files.createFile(file)
        file.toFile().setReadable(false)
        def supplier = new FileSourceSupplier(tempDir.toString())

        when:
        supplier.getContentStream().toList()

        then:
        thrown(UncheckedIOException)
    }
}

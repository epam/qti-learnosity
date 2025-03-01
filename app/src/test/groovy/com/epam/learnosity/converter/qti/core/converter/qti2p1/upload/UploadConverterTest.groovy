/*
MIT License

Copyright (c) 2025 EPAM Systems

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.epam.learnosity.converter.qti.core.converter.qti2p1.upload

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import spock.lang.Specification

class UploadConverterTest extends Specification {
    def convertUploadTest() {
        given:
        def qtiXml = getClass().getResource("/qti/upload.xml").text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new UploadConverter()

        when:
        def fileUpload = converter.convert(assessmentItem)

        then:
        fileUpload.getType() == "fileupload"
        fileUpload.getMetadata() == null
        fileUpload.getStimulus() == "<p>A chocolate factory produces several types of chocolate, some of which have nut centres.\n" +
                "            The chocolates are mixed together and are randomly packed into cartons of ten.</p><p>Build a spreadsheet to simulate 50 cartons of chocolates when each carton\n" +
                "                contains 10 chocolates, and when one-seventh of the chocolates have nut centres.\n" +
                "                Your spreadsheet should include 50 rows representing the 50 cartons, each row\n" +
                "                containing 10 columns to represent the chocolates.</p>"
        fileUpload.allowPdf
        fileUpload.allowJpg
        fileUpload.allowPng
        !fileUpload.allowAssembly
    }
}

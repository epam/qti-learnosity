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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.textentry

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation
import spock.lang.Specification

class TextEntryConverterTest extends Specification {
    def convertSingleTextEntryTest() {
        given:
        def qtiXml = getClass().getResource('/qti/text_entry.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new TextEntryConverter()

        when:
        def clozeText = converter.convert(assessmentItem)

        then:
        clozeText.getType() == "clozetext"
        !clozeText.isMath()
        clozeText.getMetadata() == null
        clozeText.getStimulus() == null
        clozeText.getStimulusReview() == null
        clozeText.getInstructorStimulus() == null
        clozeText.getFeedbackAttempts() == 0
        !clozeText.isInstantFeedback()
        !clozeText.isShuffleOptions()
        !clozeText.isCaseSensitive()
        clozeText.getTemplate().replace("\r\n", "\n") == "<p>Identify the missing word in this famous quote from" +
                " Shakespeare's Richard III.</p><blockquote><p>Now is the winter of our discontent<br/> Made " +
                "glorious summer by this sun of\n                    {{response}};<br/>\n                And all the" +
                " clouds that lour'd upon our house<br/> In the deep bosom of the ocean\n                buried." +
                "</p>\n        </blockquote>"

        def validation = clozeText.getValidation()
        validation.getScoringType() == Validation.ScoringType.EXACT_MATCH
        validation.getValidResponse().score == "1.0"

        def values = validation.getValidResponse().getValue()
        values.size() == 1
        values.getFirst() == "York"

        def altResponses = validation.getAltResponses()
        altResponses.size() == 1
        altResponses[0].score == "0.5"
        altResponses[0].value.getFirst() == "york"
    }

    def convertMultipleTextEntryTest() {
        given:
        def qtiXml = getClass().getResource('/qti/text_entry_multiple_gaps.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new TextEntryConverter()

        when:
        def clozeText = converter.convert(assessmentItem)

        then:
        clozeText.getType() == "clozetext"
        !clozeText.isMath()
        clozeText.getMetadata() == null
        clozeText.getStimulus() == null
        clozeText.getStimulusReview() == null
        clozeText.getInstructorStimulus() == null
        clozeText.getFeedbackAttempts() == 0
        !clozeText.isInstantFeedback()
        !clozeText.isShuffleOptions()
        clozeText.isCaseSensitive()
        clozeText.getTemplate().replace("\r\n", "\n") == "<div xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">Level 1 CH1 Géoculture\n" +
                "            <br/><strong>A</strong>Match each letter in the map of the Paris region with the name " +
                "of the place it represents.\n" +
                "            (10 points)\n" +
                "            <br/><img src=\"L1_CH01101.gif\" alt=\"\"/><br/>\n" +
                "            le jardin de Giverny\n" +
                "            <br/>{{response}}\n" +
                "        </div><p>{{response}}\n" +
                "        </p>"

        def validation = clozeText.getValidation()
        validation.getScoringType() == Validation.ScoringType.EXACT_MATCH
        validation.getValidResponse().score == "6.0"

        def values = validation.getValidResponse().getValue()
        values.size() == 2
        values[0] == "b"
        values[1] == "d"

        def altResponses = validation.getAltResponses()
        altResponses.size() == 3

        altResponses[0].score == "4.0"
        altResponses[0].value[0] == "a"
        altResponses[0].value[1] == "c"

        altResponses[1].score == "5.0"
        altResponses[1].value[0] == "a"
        altResponses[1].value[1] == "d"

        altResponses[2].score == "5.0"
        altResponses[2].value[0] == "b"
        altResponses[2].value[1] == "c"
    }
}

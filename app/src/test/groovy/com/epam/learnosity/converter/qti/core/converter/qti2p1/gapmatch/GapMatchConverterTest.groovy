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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation
import spock.lang.Specification

class GapMatchConverterTest extends Specification {
    def convertSimpleGapMatchInteraction() {
        given:
        def qtiXml = getClass().getResource("/qti/gap_match.xml").text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new GapMatchConverter()

        when:
        def clozeAssociation = converter.convert(assessmentItem)

        then:
        clozeAssociation.getType() == "clozeassociation"
        clozeAssociation.getMetadata() == null
        clozeAssociation.getStimulus() == "Identify the missing words in this famous quote from Shakespeare's " +
                "Richard III."
        clozeAssociation.getInstructorStimulus() == null
        clozeAssociation.getStimulusReview() == null
        clozeAssociation.getInstructorStimulus() == null
        clozeAssociation.getFeedbackAttempts() == 0
        !clozeAssociation.isInstantFeedback()
        !clozeAssociation.isShuffleOptions()
        !clozeAssociation.isDuplicateResponses()
        clozeAssociation.getTemplate() == "<blockquote><p>Now is the {{response}} of our discontent<br/> Made" +
                " glorious {{response}} by this sun of York;<br/> And all the clouds that lour'd\n" +
                "\t\t\t\t\tupon our house<br/> In the deep bosom of the ocean buried.</p>\n" +
                "\t\t\t</blockquote>"

        def possibleResponses = clozeAssociation.getPossibleResponses()
        possibleResponses.size() == 4
        possibleResponses.containsAll("winter", "spring", "summer", "autumn")

        def validation = clozeAssociation.getValidation()
        validation.getScoringType() == Validation.ScoringType.PARTIAL_MATCH_V2
        validation.getValidResponse().score == "3.0"

        def validResponses = validation.getValidResponse().getValue()
        validResponses.size() == 2
        validResponses[0] == "winter"
        validResponses[1] == "summer"

        validation.getAltResponses().isEmpty()
    }

    def convertGapMatchInteractionWithMultipleResponses() {
        given:
        def qtiXml = getClass().getResource("/qti/gap_match_multiple_responses.xml").text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new GapMatchConverter()

        when:
        def clozeAssociation = converter.convert(assessmentItem)

        then:
        clozeAssociation.getType() == "clozeassociation"
        clozeAssociation.getMetadata() == null
        clozeAssociation.getStimulus() == "Identify the missing words in this famous quote from Shakespeare's " +
                "Richard III."
        clozeAssociation.getInstructorStimulus() == null
        clozeAssociation.getStimulusReview() == null
        clozeAssociation.getInstructorStimulus() == null
        clozeAssociation.getFeedbackAttempts() == 0
        !clozeAssociation.isInstantFeedback()
        !clozeAssociation.isShuffleOptions()
        clozeAssociation.isDuplicateResponses()
        clozeAssociation.getTemplate() == "<blockquote><p>Now is the {{response}} of our discontent<br/> Made" +
                " glorious {{response}} by this sun of York;<br/> And all the clouds that lour'd\n" +
                "\t\t\t\t\tupon our house<br/> In the deep bosom of the ocean buried.</p>\n" +
                "\t\t\t</blockquote>"

        def possibleResponses = clozeAssociation.getPossibleResponses()
        possibleResponses.size() == 4
        possibleResponses.containsAll("winter", "spring", "summer", "autumn")

        def validation = clozeAssociation.getValidation()
        validation.getScoringType() == Validation.ScoringType.PARTIAL_MATCH_V2
        validation.getValidResponse().score == "9.0"

        def validResponses = validation.getValidResponse().getValue()
        validResponses.size() == 2
        validResponses[0] == "spring"
        validResponses[1] == "winter"

        def altResponses = validation.getAltResponses()
        altResponses.size() == 5
        altResponses[0].score == "3.0"
        altResponses[0].value[0] == "winter"
        altResponses[0].value[1] == "summer"
        altResponses[1].score == "5.0"
        altResponses[1].value[0] == "winter"
        altResponses[1].value[1] == "winter"
        altResponses[2].score == "5.0"
        altResponses[2].value[0] == "summer"
        altResponses[2].value[1] == "summer"
        altResponses[3].score == "7.0"
        altResponses[3].value[0] == "summer"
        altResponses[3].value[1] == "winter"
        altResponses[4].score == "7.0"
        altResponses[4].value[0] == "spring"
        altResponses[4].value[1] == "summer"
    }
}

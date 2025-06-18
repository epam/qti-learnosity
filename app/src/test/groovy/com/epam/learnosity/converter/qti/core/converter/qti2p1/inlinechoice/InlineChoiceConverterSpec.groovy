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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.inlinechoice

import com.epam.learnosity.converter.qti.core.converter.ConversionException
import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation
import spock.lang.Specification

class InlineChoiceConverterSpec extends Specification {
    def "should convert a single inline choice interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/inline_choice.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new InlineChoiceConverter()

        when:
        def clozeDropdown = converter.convert(assessmentItem)

        then:
        clozeDropdown.getType() == "clozedropdown"
        clozeDropdown.getMetadata() == null
        clozeDropdown.getStimulus() == null
        clozeDropdown.getStimulusReview() == null
        clozeDropdown.getInstructorStimulus() == null
        clozeDropdown.getFeedbackAttempts() == 0
        !clozeDropdown.instantFeedback
        !clozeDropdown.isShuffleOptions()
        clozeDropdown.getTemplate().normalize() == "<p>Identify the missing word in this famous quote from" +
                " Shakespeare's Richard III.</p><blockquote><p>Now is the winter of our discontent<br/> Made glorious" +
                " summer by this sun of\n                    {{response}};<br/> And all the clouds that lour'd upon" +
                " our house<br/>\n                In the deep bosom of the ocean buried.</p>\n        </blockquote>"

        def possibleResponses = clozeDropdown.getPossibleResponses()
        possibleResponses.size() == 1

        def firstInteractionResponses = possibleResponses.getFirst()
        firstInteractionResponses.size() == 3
        firstInteractionResponses[0] == "Gloucester"
        firstInteractionResponses[1] == "Lancaster"
        firstInteractionResponses[2] == "York"

        def validation = clozeDropdown.getValidation()
        validation.getScoringType() == Validation.ScoringType.PARTIAL_MATCH_V2
        validation.getAltResponses() == null

        def validResponse = validation.getValidResponse()
        validResponse.score == "1"

        def validResponseValues = validResponse.value
        validResponseValues.size() == 1
        validResponseValues[0] == "York"
    }

    def "should convert a multiple inline choice interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/inline_choice_multiple_choices.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new InlineChoiceConverter()

        when:
        def clozeDropdown = converter.convert(assessmentItem)

        then:
        clozeDropdown.getType() == "clozedropdown"
        clozeDropdown.getMetadata() == null
        clozeDropdown.getStimulus() == null
        clozeDropdown.getStimulusReview() == null
        clozeDropdown.getInstructorStimulus() == null
        clozeDropdown.getFeedbackAttempts() == 0
        !clozeDropdown.instantFeedback
        clozeDropdown.isShuffleOptions()
        clozeDropdown.getTemplate().normalize() == "<p>Identify the missing word in this famous quote from" +
                " Shakespeare's Richard III.</p><blockquote><p>{{response}} is the winter of our discontent<br/> " +
                "Made glorious summer by this sun of\n                    {{response}};<br/> And all the clouds that" +
                " lour'd upon our house<br/>\n                In the deep bosom of the ocean buried.</p>\n        " +
                "</blockquote>"

        def possibleResponses = clozeDropdown.getPossibleResponses()
        possibleResponses.size() == 2

        def firstInteractionResponses = possibleResponses.getFirst()
        firstInteractionResponses.size() == 2
        firstInteractionResponses[0] == "Now"
        firstInteractionResponses[1] == "Then"

        def secondInteractionResponses = possibleResponses.getLast()
        secondInteractionResponses.size() == 3
        secondInteractionResponses.size() == 3
        secondInteractionResponses[0] == "Gloucester"
        secondInteractionResponses[1] == "Lancaster"
        secondInteractionResponses[2] == "York"

        def validation = clozeDropdown.getValidation()
        validation.getScoringType() == Validation.ScoringType.PARTIAL_MATCH_V2
        validation.getAltResponses() == null

        def validResponse = validation.getValidResponse()
        validResponse.score == "2"

        def validResponseValues = validResponse.value
        validResponseValues.size() == 2
        validResponseValues[0] == "Now"
        validResponseValues[1] == "York"
    }

    def "should throw an exception when trying to convert an unsupported response in inline choice"() {
        given:
        def qtiXml = getClass().getResource('/qti/inline_choice_custom_processing.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new InlineChoiceConverter()

        when:
        converter.convert(assessmentItem)

        then:
        thrown ConversionException
    }
}

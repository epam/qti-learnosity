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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.choice

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.McqUiStyle
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.Option
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation
import spock.lang.Specification

class ChoiceConverterTest extends Specification {

    def convertTest() {
        given:
        def qtiXml = getClass().getResource('/qti/choice.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new ChoiceConverter()

        when:
        def multipleChoice = converter.convert(assessmentItem)

        then:
        multipleChoice.getType() == "mcq"
        !multipleChoice.isMath()
        multipleChoice.getMetadata() == null
        multipleChoice.getStimulus() == "<p>Look at the text in the picture.</p><p><img src=\"images/sign.png\" " +
                "alt=\"NEVER LEAVE LUGGAGE UNATTENDED\"/></p><p>What does it say?</p>"
        multipleChoice.getStimulusReview() == null
        multipleChoice.getInstructorStimulus() == null
        multipleChoice.getFeedbackAttempts() == 0
        !multipleChoice.isInstantFeedback()
        !multipleChoice.isShuffleOptions()
        !multipleChoice.isMultipleResponses()

        def validation = multipleChoice.getValidation()
        !validation.isAllowNegativeScores()
        validation.getPenalty() == 0
        validation.getMinScoreIfAttempted() == 0
        validation.getScoringType() == Validation.ScoringType.EXACT_MATCH
        !validation.isUnscored()
        validation.isAutomarkable()
        validation.getAltResponses() == null

        def validResponse = validation.getValidResponse()
        validResponse.getScore() == "1"

        def values = validResponse.getValue()
        values.size() == 1
        values.getFirst() == "ChoiceA"

        def options = multipleChoice.getOptions()
        options.size() == 3
        options.containsAll(
                new Option("You must stay with your luggage at all times.", "ChoiceA"),
                new Option("Do not let someone else look after your luggage.", "ChoiceB"),
                new Option("Remember your luggage when you leave.", "ChoiceC"),
        )

        def uiStyle = multipleChoice.getUiStyle()
        uiStyle.getUiType() == McqUiStyle.UiType.HORIZONTAL
        uiStyle.getChoiceLabel() == null
        uiStyle.getColumns() == 1
        uiStyle.getOrientation() == McqUiStyle.Orientation.VERTICAL
        uiStyle.getFontSize() == null
    }
}


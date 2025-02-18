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

    def convertSimpleChoiceTest() {
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
        multipleChoice.getValidation() == null
        multipleChoice.getMaxSelection() == 1

        def uiStyle = multipleChoice.getUiStyle()
        uiStyle.getUiType() == McqUiStyle.UiType.HORIZONTAL
        uiStyle.getChoiceLabel() == null
        uiStyle.getColumns() == 1
        uiStyle.getOrientation() == McqUiStyle.Orientation.VERTICAL
        uiStyle.getFontSize() == null
    }

    def convertMultipleResponseChoiceTest() {
        given:
        def qtiXml = getClass().getResource('/qti/choice_multiple.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new ChoiceConverter()

        when:
        def multipleChoice = converter.convert(assessmentItem)

        then:
        multipleChoice.getType() == "mcq"
        !multipleChoice.isMath()
        multipleChoice.getMetadata() == null
        multipleChoice.getStimulus() == "Which of the following elements are used to form water?"
        multipleChoice.getStimulusReview() == null
        multipleChoice.getInstructorStimulus() == null
        multipleChoice.getFeedbackAttempts() == 0
        !multipleChoice.isInstantFeedback()
        multipleChoice.isShuffleOptions()
        multipleChoice.isMultipleResponses()
        multipleChoice.getMaxSelection() == 0

        def validation = multipleChoice.getValidation()
        !validation.isAllowNegativeScores()
        validation.getPenalty() == 0
        validation.getMinScoreIfAttempted() == 0
        validation.getScoringType() == Validation.ScoringType.PARTIAL_MATCH_V2
        !validation.isUnscored()
        validation.isAutomarkable()
        validation.getAltResponses() == null

        def validResponse = validation.getValidResponse()
        validResponse.getScore() == "2.0"

        def values = validResponse.getValue()
        values.size() == 2
        values[0] == "H"
        values[1] == "O"

        def options = multipleChoice.getOptions()
        options.size() == 6
        options.containsAll(
                new Option("Hydrogen", "H"),
                new Option("Helium", "He"),
                new Option("Carbon", "C"),
                new Option("Oxygen", "O"),
                new Option("Nitrogen", "N"),
                new Option("Chlorine", "Cl"),
        )

        def uiStyle = multipleChoice.getUiStyle()
        uiStyle.getUiType() == McqUiStyle.UiType.HORIZONTAL
        uiStyle.getChoiceLabel() == null
        uiStyle.getColumns() == 1
        uiStyle.getOrientation() == McqUiStyle.Orientation.VERTICAL
        uiStyle.getFontSize() == null
    }
}


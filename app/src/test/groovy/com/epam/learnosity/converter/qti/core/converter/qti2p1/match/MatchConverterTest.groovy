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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.match

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation
import spock.lang.Specification

class MatchConverterTest extends Specification {
    def convertToLearnosityTest() {
        given:
        def qtiXml = getClass().getResource('/qti/match.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new MatchConverter()

        when:
        def choiceMatrix = converter.convert(assessmentItem)

        then:
        choiceMatrix.getType() == "choicematrix"
        !choiceMatrix.isMath()
        choiceMatrix.getMetadata() == null
        choiceMatrix.getStimulus() == "Match the following characters to the Shakespeare play they appeared in:"
        choiceMatrix.getStimulusReview() == null
        choiceMatrix.getInstructorStimulus() == null
        choiceMatrix.getFeedbackAttempts() == 0
        !choiceMatrix.isInstantFeedback()
        choiceMatrix.isShuffleOptions()
        choiceMatrix.isMultipleResponses()

        def validation = choiceMatrix.getValidation()
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
        values.size() == 4
        values.containsAll(1, 0, 0, 2)

        def options = choiceMatrix.getOptions()
        options.size() == 3
        options.containsAll("A Midsummer-Night's Dream", "Romeo and Juliet", "The Tempest")

        def uiStyle = choiceMatrix.getUiStyle()
        uiStyle.getUiType() == "table"
        uiStyle.getStemTitle() == "Characters and Plays"
        uiStyle.getFontSize() == null
    }
}


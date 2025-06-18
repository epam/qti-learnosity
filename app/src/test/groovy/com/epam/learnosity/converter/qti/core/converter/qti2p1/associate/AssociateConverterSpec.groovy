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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.associate

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation
import spock.lang.Specification

class AssociateConverterSpec extends Specification {
    def "should convert a simple associate interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/associate.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new AssociateConverter()

        when:
        def matchList = converter.convert(assessmentItem)

        then:
        matchList.getType() == "association"
        !matchList.isMath()
        matchList.getStimulus() == "Hidden in this list of characters from famous Shakespeare plays are three pairs\n" +
                "                of rivals. Can you match each character to his adversary?"
        matchList.isShuffleOptions()
        !matchList.isDuplicateResponses()

        def stimulusList = matchList.getStimulusList()
        stimulusList.size() == 3
        stimulusList[0] == "Antonio"
        stimulusList[1] == "Capulet"
        stimulusList[2] == "Demetrius"

        def possibleResponses = matchList.getPossibleResponses()
        possibleResponses.size() == 3
        possibleResponses.containsAll("Lysander", "Montague", "Prospero")

        def validation = matchList.getValidation()
        validation.getScoringType() == Validation.ScoringType.PARTIAL_MATCH_V2
        !validation.isAllowNegativeScores()
        validation.getPenalty() == 0
        validation.getMinScoreIfAttempted() == 0
        !validation.isUnscored()
        validation.isAutomarkable()
        validation.getAltResponses() == null

        def validResponse = validation.getValidResponse()
        validResponse.getScore() == "4"

        def values = validResponse.getValue()
        values.size() == 3
        values[0] == "Prospero"
        values[1] == "Montague"
        values[2] == "Lysander"
    }

    def "should convert an extended associate interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/associate_extended.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new AssociateConverter()

        when:
        def matchList = converter.convert(assessmentItem)

        then:
        matchList.getType() == "association"
        !matchList.isMath()
        matchList.getStimulus() == "Match the key concepts of the CAP theorem with their definitions. Note that one" +
                " definition is unmatched, and one concept can be used multiple times."
        matchList.isShuffleOptions()
        matchList.isDuplicateResponses()

        def stimulusList = matchList.getStimulusList()
        stimulusList.size() == 3
        stimulusList[0] == "Consistency"
        stimulusList[1] == "Availability"
        stimulusList[2] == "Partition Tolerance"

        def possibleResponses = matchList.getPossibleResponses()
        possibleResponses.size() == 4
        possibleResponses.containsAll("Ensures all nodes see the same data at the same time",
                "Ensures that every request receives a response, even in case of partial failure",
                "Ensures the system continues to operate despite network partitions",
                "Ability to handle increased load by adding resources")

        def validation = matchList.getValidation()
        validation.getScoringType() == Validation.ScoringType.PARTIAL_MATCH_V2
        !validation.isAllowNegativeScores()
        validation.getPenalty() == 0
        validation.getMinScoreIfAttempted() == 0
        !validation.isUnscored()
        validation.isAutomarkable()
        validation.getAltResponses() == null

        def validResponse = validation.getValidResponse()
        validResponse.getScore() == "3"

        def values = validResponse.getValue()
        values.size() == 3
        values[0] == "Ensures all nodes see the same data at the same time"
        values[1] == "Ensures that every request receives a response, even in case of partial failure"
        values[2] == "Ensures the system continues to operate despite network partitions"
    }
}

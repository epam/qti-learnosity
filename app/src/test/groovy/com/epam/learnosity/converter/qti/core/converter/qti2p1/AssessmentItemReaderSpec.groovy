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
package com.epam.learnosity.converter.qti.core.converter.qti2p1

import com.epam.learnosity.converter.qti.core.converter.qti2p1.associate.qti.AssociateInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.qti.ChoiceInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.MapEntry
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.SimpleAssociableChoice
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.SimpleChoice
import com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch.qti.GapMatchInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch.qti.GapText
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.qti.MatchInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.order.qti.OrderInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.upload.qti.UploadInteraction
import spock.lang.Specification

class AssessmentItemReaderSpec extends Specification {

    def "should read a match interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/match.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "match"
        result.getTitle() == "Characters and Plays"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/map_response"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "multiple"
        responseDeclaration.getBaseType() == "directedPair"

        def mapping = responseDeclaration.getMapping()
        mapping.getDefaultValue() == 0

        def mapEntry = mapping.getMapEntry()
        mapEntry.size() == 4
        mapEntry.containsAll(
                new MapEntry("C R", "1", false),
                new MapEntry("D M", "0.5", false),
                new MapEntry("L M", "0.5", false),
                new MapEntry("P T", "1", false)
        )

        def values = responseDeclaration.getCorrectResponse().getValue()
        values.size() == 4
        values.containsAll("C R", "D M", "L M", "P T")

        def itemBody = result.getItemBody()
        itemBody.getContent() == null

        MatchInteraction interaction = itemBody.getInteraction() as MatchInteraction
        interaction.getType() == QtiType.MATCH
        interaction.getMaxAssociations() == 4
        interaction.getResponseIdentifier() == "RESPONSE"
        interaction.getPrompt() == "Match the following characters to the Shakespeare play they appeared in:"
        interaction.isShuffle()

        def simpleMatchSet = interaction.getSimpleMatchSet()
        simpleMatchSet.size() == 2
    }

    def "should read a choice interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/choice.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "choice"
        result.getTitle() == "Unattended Luggage"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/match_correct"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "single"
        responseDeclaration.getBaseType() == "identifier"
        responseDeclaration.getMapping() == null

        def values = responseDeclaration.getCorrectResponse().getValue()
        values.size() == 1
        values.contains("ChoiceA")

        def itemBody = result.getItemBody()
        itemBody.getContentAsSingleString() == "<p>Look at the text in the picture.</p>" +
                "<p><img src=\"images/sign.png\" alt=\"NEVER LEAVE LUGGAGE UNATTENDED\"/></p>"

        ChoiceInteraction interaction = itemBody.getInteraction() as ChoiceInteraction
        interaction.getType() == QtiType.CHOICE
        interaction.getMaxChoices() == 1
        interaction.getResponseIdentifier() == "RESPONSE"
        interaction.getPrompt() == "What does it say?"
        !interaction.isShuffle()

        def choices = interaction.getSimpleChoice()
        choices.size() == 3
        choices.containsAll(
                new SimpleChoice("ChoiceA", "You must stay with your luggage at all times."),
                new SimpleChoice("ChoiceB", "Do not let someone else look after your luggage."),
                new SimpleChoice("ChoiceC", "Remember your luggage when you leave.")
        )
    }

    def "should read an order interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/order.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "order"
        result.getTitle() == "Grand Prix of Bahrain"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/match_correct"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "ordered"
        responseDeclaration.getBaseType() == "identifier"
        responseDeclaration.getMapping() == null

        def values = responseDeclaration.getCorrectResponse().getValue()
        values.size() == 3
        values.containsAll("DriverC", "DriverA", "DriverB")

        def itemBody = result.getItemBody()
        itemBody.getContent() == null

        OrderInteraction interaction = itemBody.getInteraction() as OrderInteraction
        interaction.getType() == QtiType.ORDER
        interaction.getResponseIdentifier() == "RESPONSE"
        interaction.getPrompt() == "The following F1 drivers finished on the podium in the first ever Grand Prix of\n" +
                "                Bahrain. Can you rearrange them into the correct finishing order?"
        interaction.isShuffle()

        def choices = interaction.getSimpleChoice()
        choices.size() == 3
        choices.containsAll(
                new SimpleChoice("DriverA", "Rubens Barrichello"),
                new SimpleChoice("DriverB", "Jenson Button"),
                new SimpleChoice("DriverC", "Michael Schumacher")
        )
    }

    def "should read a single text entry interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/text_entry.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "textEntry"
        result.getTitle() == "Richard III (Take 3)"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/" +
                "map_response"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "single"
        responseDeclaration.getBaseType() == "string"
        responseDeclaration.getMapping().getDefaultValue() == 0

        def validResponses = responseDeclaration.getMapping().getMapEntry()
        validResponses.size() == 2
        validResponses[0].getMapKey() == "York"
        validResponses[0].getMappedValue() == "1"
        validResponses[1].getMapKey() == "york"
        validResponses[1].getMappedValue() == "0.5"

        def itemBody = result.getItemBody()
        itemBody.getContentAsSingleString().normalize() == "<p>Identify the missing word in this famous " +
                "quote from Shakespeare's Richard III.</p><blockquote><p>Now is the winter of our discontent<br/> " +
                "Made glorious summer by this sun of\n                    <textEntryInteraction " +
                "responseIdentifier=\"RESPONSE\" expectedLength=\"15\"/>;<br/>\n                And all the clouds" +
                " that lour'd upon our house<br/> In the deep bosom of the ocean\n                buried.</p>\n   " +
                "     </blockquote>"
    }

    def "should read a multiple text entry interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/text_entry_multiple_gaps.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "res_AA-FIB_B13_CH1_geoc_f1f1"
        result.getTitle() == "AA-FIB_B13_CH1_geoc_f1f1"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/" +
                "map_response"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "MAXSCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclarations = result.getResponseDeclaration()
        responseDeclarations.size() == 2

        responseDeclarations[0].getIdentifier() == "RESPONSE_1"
        responseDeclarations[0].getCardinality() == "single"
        responseDeclarations[0].getBaseType() == "string"
        responseDeclarations[0].getMapping().getDefaultValue() == 0

        responseDeclarations[0].getMapping().getMapEntry().size() == 2
        responseDeclarations[0].getMapping().getMapEntry()[0].getMapKey() == "a"
        responseDeclarations[0].getMapping().getMapEntry()[0].getMappedValue() == "1"
        responseDeclarations[0].getMapping().getMapEntry()[1].getMapKey() == "b"
        responseDeclarations[0].getMapping().getMapEntry()[1].getMappedValue() == "2"

        responseDeclarations[1].getIdentifier() == "RESPONSE_2"
        responseDeclarations[1].getCardinality() == "single"
        responseDeclarations[1].getBaseType() == "string"
        responseDeclarations[1].getMapping().getDefaultValue() == 0

        responseDeclarations[1].getMapping().getMapEntry().size() == 2
        responseDeclarations[1].getMapping().getMapEntry()[0].getMapKey() == "c"
        responseDeclarations[1].getMapping().getMapEntry()[0].getMappedValue() == "3"
        responseDeclarations[1].getMapping().getMapEntry()[1].getMapKey() == "d"
        responseDeclarations[1].getMapping().getMapEntry()[1].getMappedValue() == "4"

        def itemBody = result.getItemBody()
        itemBody.getContentAsSingleString().normalize() == "<div xmlns=\"http://www.imsglobal.org/xsd/" +
                "imsqti_v2p1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">Level 1 CH1 Géoculture\n" +
                "            <br/><strong>A</strong>Match each letter in the map of the Paris region with the name " +
                "of the place it represents.\n            (10 points)\n            <br/><img src=\"L1_CH01101.gif\"" +
                " alt=\"\"/><br/>\n            le jardin de Giverny\n            <br/><textEntryInteraction " +
                "responseIdentifier=\"RESPONSE_1\" expectedLength=\"6\"/>\n        </div><p><textEntryInteraction" +
                " responseIdentifier=\"RESPONSE_2\" expectedLength=\"6\"/>\n        </p>"
    }

    def "should read a simple association interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/associate.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "associate"
        result.getTitle() == "Shakespearian Rivals"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/map_response"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "multiple"
        responseDeclaration.getBaseType() == "pair"

        def mapping = responseDeclaration.getMapping()
        mapping.getDefaultValue() == 0

        def mapEntry = mapping.getMapEntry()
        mapEntry.size() == 3
        mapEntry.containsAll(
                new MapEntry("A P", "2", false),
                new MapEntry("C M", "1", false),
                new MapEntry("D L", "1", false)
        )

        def itemBody = result.getItemBody()
        itemBody.getContent() == null

        AssociateInteraction interaction = itemBody.getInteraction() as AssociateInteraction
        interaction.getType() == QtiType.ASSOCIATE
        interaction.getResponseIdentifier() == "RESPONSE"
        interaction.getPrompt() == "Hidden in this list of characters from famous Shakespeare plays are three pairs\n" +
                "                of rivals. Can you match each character to his adversary?"
        interaction.isShuffle()

        def choices = interaction.getSimpleAssociableChoice()
        choices.size() == 6
        choices.containsAll(
                new SimpleAssociableChoice("A", 1, "Antonio"),
                new SimpleAssociableChoice("C", 1, "Capulet"),
                new SimpleAssociableChoice("D", 1, "Demetrius"),
                new SimpleAssociableChoice("L", 1, "Lysander"),
                new SimpleAssociableChoice("M", 1, "Montague"),
                new SimpleAssociableChoice("P", 1, "Prospero")
        )
    }

    def "should read an extended association interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/associate_extended.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "cap_theorem_association"
        result.getTitle() == "CAP Theorem Matching"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/map_response"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "multiple"
        responseDeclaration.getBaseType() == "pair"

        def mapping = responseDeclaration.getMapping()
        mapping.getDefaultValue() == 0

        def mapEntry = mapping.getMapEntry()
        mapEntry.size() == 3
        mapEntry.containsAll(
                new MapEntry("A Consistency", "1", false),
                new MapEntry("B Availability", "1", false),
                new MapEntry("C PartitionTolerance", "1", false)
        )

        def itemBody = result.getItemBody()
        itemBody.getContent() == null

        AssociateInteraction interaction = itemBody.getInteraction() as AssociateInteraction
        interaction.getType() == QtiType.ASSOCIATE
        interaction.getResponseIdentifier() == "RESPONSE"
        interaction.getPrompt() == "Match the key concepts of the CAP theorem with their definitions. Note that one " +
                "definition is unmatched, and one concept can be used multiple times."
        interaction.isShuffle()

        def choices = interaction.getSimpleAssociableChoice()
        choices.size() == 7
        choices.containsAll(
                new SimpleAssociableChoice("A", 1, "Consistency"),
                new SimpleAssociableChoice("B", 1, "Availability"),
                new SimpleAssociableChoice("C", 1, "Partition Tolerance"),
                new SimpleAssociableChoice("Consistency", 2, "Ensures all nodes see the " +
                        "same data at the same time"),
                new SimpleAssociableChoice("Availability", 2, "Ensures that every request" +
                        " receives a response, even in case of partial failure"),
                new SimpleAssociableChoice("PartitionTolerance", 1, "Ensures the system" +
                        " continues to operate despite network partitions"),
                new SimpleAssociableChoice("Scalability", 1, "Ability to handle increased" +
                        " load by adding resources")
        )
    }

    def "should read a gap match interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/gap_match.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "gapMatch"
        result.getTitle() == "Richard III (Take 1)"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/map_response"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "multiple"
        responseDeclaration.getBaseType() == "directedPair"

        def mapping = responseDeclaration.getMapping()
        mapping.getDefaultValue() == -1

        def mapEntry = mapping.getMapEntry()
        mapEntry.size() == 2
        mapEntry.containsAll(
                new MapEntry("W G1", "1", false),
                new MapEntry("Su G2", "2", false)
        )

        def itemBody = result.getItemBody()
        itemBody.getContent() == null

        GapMatchInteraction interaction = itemBody.getInteraction() as GapMatchInteraction
        interaction.getType() == QtiType.GAP_MATCH
        interaction.getResponseIdentifier() == "RESPONSE"
        interaction.getPrompt() == "Identify the missing words in this famous quote from Shakespeare's Richard III."
        interaction.getTextBlock().normalize() == "<blockquote><p>Now is the <gap identifier=\"G1\"/> of " +
                "our discontent<br/> Made glorious <gap identifier=\"G2\"/> by this sun of York;<br/> And all the" +
                " clouds that lour'd\n                    upon our house<br/> In the deep bosom of the ocean " +
                "buried.</p>\n            </blockquote>"
        !interaction.isShuffle()

        def choices = interaction.getGapText()
        choices.size() == 4
        choices.containsAll(
                new GapText("W", 1, "winter"),
                new GapText("Sp", 1, "spring"),
                new GapText("Su", 1, "summer"),
                new GapText("A", 1, "autumn")
        )
    }

    def "should read an inline choice interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/inline_choice.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "inlineChoice"
        result.getTitle() == "Richard III (Take 2)"
        !result.isAdaptive()
        !result.isTimeDependent()
        result.getResponseProcessing().getTemplate() == "http://www.imsglobal.org/question/qti_v2p1/rptemplates/" +
                "match_correct"

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "single"
        responseDeclaration.getBaseType() == "identifier"
        responseDeclaration.getMapping() == null

        def validResponses = responseDeclaration.getCorrectResponse().getValue()
        validResponses.size() == 1
        validResponses[0] == "Y"

        def itemBody = result.getItemBody()
        itemBody.getContentAsSingleString().normalize() == "<p>Identify the missing word in this famous" +
                " quote from Shakespeare's Richard III.</p><blockquote><p>Now is the winter of our discontent<br/>" +
                " Made glorious summer by this sun of\n                    <inlineChoiceInteraction " +
                "responseIdentifier=\"RESPONSE\" shuffle=\"false\"><inlineChoice identifier=\"G\">Gloucester" +
                "</inlineChoice><inlineChoice identifier=\"L\">Lancaster</inlineChoice><inlineChoice " +
                "identifier=\"Y\">York</inlineChoice>\n                </inlineChoiceInteraction>;<br/> And all the" +
                " clouds that lour'd upon our house<br/>\n                In the deep bosom of the ocean buried.</p>" +
                "\n        </blockquote>"
    }

    def "should read an upload interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/upload.xml').text
        def reader = new AssessmentItemReader()

        when:
        def result = reader.read(qtiXml)

        then:
        result.getIdentifier() == "upload"
        result.getTitle() == "Chocolate Factory"
        !result.isAdaptive()
        !result.isTimeDependent()

        def outcomeDeclaration = result.getOutcomeDeclaration()
        outcomeDeclaration.getIdentifier() == "SCORE"
        outcomeDeclaration.getCardinality() == "single"
        outcomeDeclaration.getBaseType() == "float"

        def responseDeclaration = result.getResponseDeclaration().getFirst()
        responseDeclaration.getIdentifier() == "RESPONSE"
        responseDeclaration.getCardinality() == "single"
        responseDeclaration.getBaseType() == "file"

        def itemBody = result.getItemBody()
        itemBody.getContent().size() == 1
        itemBody.getContentAsSingleString().normalize() == "<p>A chocolate factory produces several types" +
                " of chocolate, some of which have nut centres.\n            The chocolates are mixed together and" +
                " are randomly packed into cartons of ten.</p>"

        UploadInteraction interaction = itemBody.getInteraction() as UploadInteraction
        interaction.getType() == QtiType.UPLOAD
        interaction.getFileType() == "application/pdf image/png image/jpeg"
        interaction.getResponseIdentifier() == "RESPONSE"
        interaction.getPrompt() == "Build a spreadsheet to simulate 50 cartons of chocolates when each carton\n" +
                "                contains 10 chocolates, and when one-seventh of the chocolates have nut centres.\n" +
                "                Your spreadsheet should include 50 rows representing the 50 cartons, each row\n" +
                "                containing 10 columns to represent the chocolates."
    }
}

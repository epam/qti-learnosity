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

import com.epam.learnosity.converter.qti.core.converter.qti2p1.associate.AssociateConverter
import com.epam.learnosity.converter.qti.core.converter.qti2p1.associate.qti.AssociateInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.ChoiceConverter
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.qti.ChoiceInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ItemBody
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.MatchConverter
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.qti.MatchInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.order.OrderConverter
import com.epam.learnosity.converter.qti.core.converter.qti2p1.order.qti.OrderInteraction
import com.epam.learnosity.converter.qti.core.converter.qti2p1.textentry.TextEntryConverter
import spock.lang.Specification

class QtiToLearnosityConverterFactoryImplSpec extends Specification {

    def "should get a choice converter"() {
        given:
        def assessmentItem = new AssessmentItem()
        def itemBody = new ItemBody()
        def interaction = new ChoiceInteraction()

        assessmentItem.setItemBody(itemBody)
        itemBody.setInteraction(interaction)

        def factory = new QtiToLearnosityConverterFactoryImpl()

        when:
        def converter = factory.getConverter(assessmentItem).get()

        then:
        converter instanceof ChoiceConverter
    }

    def "should get a gap match converter"() {
        given:
        def assessmentItem = new AssessmentItem()
        def itemBody = new ItemBody()
        def interaction = new MatchInteraction()

        assessmentItem.setItemBody(itemBody)
        itemBody.setInteraction(interaction)

        def factory = new QtiToLearnosityConverterFactoryImpl()

        when:
        def converter = factory.getConverter(assessmentItem).get()

        then:
        converter instanceof MatchConverter
    }

    def "should get an order converter"() {
        given:
        def assessmentItem = new AssessmentItem()
        def itemBody = new ItemBody()
        def interaction = new OrderInteraction()

        assessmentItem.setItemBody(itemBody)
        itemBody.setInteraction(interaction)

        def factory = new QtiToLearnosityConverterFactoryImpl()

        when:
        def converter = factory.getConverter(assessmentItem).get()

        then:
        converter instanceof OrderConverter
    }

    def "should get a text entry converter"() {
        given:
        def assessmentItem = new AssessmentItem()
        def itemBody = new ItemBody()
        itemBody.setContent(List.of("<blockquote xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><p>Now is the winter of our discontent<br/>" +
                " Made glorious summer by this sun of\n" +
                "\t\t\t\t\t<textEntryInteraction responseIdentifier=\"RESPONSE\" expectedLength=\"15\"/>;<br/>\n" +
                "\t\t\t\tAnd all the clouds that lour'd upon our house<br/> In the deep bosom of the ocean\n" +
                "\t\t\t\tburied.</p>\n" +
                "\t\t</blockquote>"))
        assessmentItem.setItemBody(itemBody)

        def factory = new QtiToLearnosityConverterFactoryImpl()

        when:
        def converter = factory.getConverter(assessmentItem).get()

        then:
        converter instanceof TextEntryConverter
    }

    def "should get an associate converter"() {
        given:
        def assessmentItem = new AssessmentItem()
        def itemBody = new ItemBody()
        def interaction = new AssociateInteraction()

        assessmentItem.setItemBody(itemBody)
        itemBody.setInteraction(interaction)

        def factory = new QtiToLearnosityConverterFactoryImpl()

        when:
        def converter = factory.getConverter(assessmentItem).get()

        then:
        converter instanceof AssociateConverter
    }
}

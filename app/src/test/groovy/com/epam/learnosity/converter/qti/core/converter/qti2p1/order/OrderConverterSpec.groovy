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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.order

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation
import spock.lang.Specification

class OrderConverterSpec extends Specification {
    def "should convert a simple order interaction"() {
        given:
        def qtiXml = getClass().getResource('/qti/order.xml').text
        def reader = new AssessmentItemReader()
        def assessmentItem = reader.read(qtiXml)
        def converter = new OrderConverter()

        when:
        def orderList = converter.convert(assessmentItem)

        then:
        orderList.getType() == "orderlist"
        !orderList.isMath()
        orderList.getMetadata() == null
        orderList.getStimulus() == "The following F1 drivers finished on the podium in the first ever Grand Prix of\n" +
                "                Bahrain. Can you rearrange them into the correct finishing order?"
        orderList.getStimulusReview() == null
        orderList.getInstructorStimulus() == null
        orderList.getFeedbackAttempts() == 0
        !orderList.isInstantFeedback()
        orderList.isShuffleOptions()

        def list = orderList.getList()
        list.size() == 3
        list.containsAll("Rubens Barrichello", "Jenson Button", "Michael Schumacher")

        def validation = orderList.getValidation()
        validation.getPenalty() == 0
        validation.getScoringType() == Validation.ScoringType.EXACT_MATCH

        def values = validation.getValidResponse().getValue()
        values.size() == 3
        values[0] == 2
        values[1] == 0
        values[2] == 1
    }
}


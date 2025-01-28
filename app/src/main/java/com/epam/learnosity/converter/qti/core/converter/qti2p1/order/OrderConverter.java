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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.order;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.IntValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.SimpleChoice;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.order.learnosity.OrderList;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.order.qti.OrderInteraction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderConverter extends QtiToLearnosityAbstractConverter<OrderList> {
    @Override
    public OrderList convert(AssessmentItem assessmentItem) {
        var orderList = new OrderList();

        orderList.setMath(isMath(assessmentItem));
        orderList.setStimulus(parseStimulus(assessmentItem));
        orderList.setInstructorStimulus(parseInstructorStimulus(assessmentItem).orElse(null));
        orderList.setShuffleOptions(parseShuffleOptions(assessmentItem));
        orderList.setList(extractList(assessmentItem));
        orderList.setValidation(parseValidation(assessmentItem));
        // TODO Consider basic mapping of UI style
        return orderList;
    }

    private List<String> extractList(AssessmentItem assessmentItem) {
        OrderInteraction orderInteraction = (OrderInteraction) assessmentItem.getItemBody().getInteraction();
        return orderInteraction.getSimpleChoice().stream()
                .map(SimpleChoice::getContent)
                .toList();
    }

    private Validation parseValidation(AssessmentItem assessmentItem) {
        var validation = new Validation();
        var intValidResponse = new IntValidResponse();
        List<String> correctResponses = assessmentItem.getResponseDeclaration()
                .getFirst()
                .getCorrectResponse()
                .getValue()
                .stream()
                .toList();
        OrderInteraction orderInteraction = (OrderInteraction) assessmentItem.getItemBody().getInteraction();
        List<String> choices = orderInteraction.getSimpleChoice().stream().map(SimpleChoice::getIdentifier).toList();

        List<Integer> expectedIndexes = new ArrayList<>();
        for (int i = 0; i < correctResponses.size(); i++) {
            for (int j = 0; j < choices.size(); j++) {
                if (correctResponses.get(i).equals(choices.get(j))) {
                    expectedIndexes.add(i, j);
                    break;
                }
            }
        }
        intValidResponse.setValue(expectedIndexes);
        validation.setValidResponse(intValidResponse);
        validation.setScoringType(Validation.ScoringType.EXACT_MATCH);
        validation.setPenalty(0);
        return validation;
    }
}

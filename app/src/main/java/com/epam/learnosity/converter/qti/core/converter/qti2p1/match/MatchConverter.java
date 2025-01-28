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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.match;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.IntValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.CorrectResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ItemBody;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ResponseDeclaration;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.SimpleAssociableChoice;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.learnosity.ChoiceMatrix;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.learnosity.ChoiceMatrixUiStyle;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.qti.MatchInteraction;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.qti.SimpleMatchSet;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SequencedCollection;
import java.util.SortedSet;
import java.util.TreeSet;

@Slf4j
public class MatchConverter extends QtiToLearnosityAbstractConverter<ChoiceMatrix> {

    @Override
    public ChoiceMatrix convert(AssessmentItem assessmentItem) {
        ChoiceMatrix choiceMatrix = new ChoiceMatrix();

        choiceMatrix.setMath(isMath(assessmentItem));
        choiceMatrix.setStimulus(parseStimulus(assessmentItem));
        choiceMatrix.setInstructorStimulus(parseInstructorStimulus(assessmentItem).orElse(null));
        choiceMatrix.setShuffleOptions(parseShuffleOptions(assessmentItem));
        choiceMatrix.setMultipleResponses(parseMultipleResponses(assessmentItem));
        choiceMatrix.setUiStyle(parseUiStyle(assessmentItem));

        fillStemsOptionsValidation(choiceMatrix, assessmentItem);

        // TODO Implement mapping of these fields or remove it completely to use defaults
        choiceMatrix.setStimulusReview(null);
        choiceMatrix.setMetadata(null);
        choiceMatrix.setFeedbackAttempts(0);
        choiceMatrix.setInstantFeedback(false);

        return choiceMatrix;
    }

    private void fillStemsOptionsValidation(ChoiceMatrix choiceMatrix, AssessmentItem assessmentItem) {
        List<LinkedHashMap<String, String>> linkedHashMaps = extractSimpleAssociableChoice(assessmentItem);
        LinkedHashMap<String, String> stems = linkedHashMaps.get(0);
        LinkedHashMap<String, String> options = linkedHashMaps.get(1);

        choiceMatrix.setOptions(options.sequencedValues());
        choiceMatrix.setStems(stems.sequencedValues());

        List<String> correctResponseValues = extractCorrectResponse(assessmentItem);
        List<String> stemsKeys = new ArrayList<>(stems.sequencedKeySet());
        List<String> optionsKeys = new ArrayList<>(options.sequencedKeySet());
        SortedSet<AbstractMap.SimpleEntry<Integer, Integer>> pairs = new TreeSet<>(Comparator.comparingInt(AbstractMap.SimpleEntry::getKey));
        for (String correct : correctResponseValues) {
            int optionIndex = -1;
            for (int stemIndex = 0; stemIndex < stemsKeys.size(); stemIndex++) {
                String key = stemsKeys.get(stemIndex);
                if (correct.startsWith(key)) {
                    String option = correct.substring(key.length()).trim();
                    optionIndex = optionsKeys.indexOf(option);
                    if (optionIndex > -1) {
                        AbstractMap.SimpleEntry<Integer, Integer> pair = new AbstractMap.SimpleEntry<>(stemIndex, optionIndex);
                        pairs.add(pair);
                        break;
                    }
                }
            }
            if (optionIndex < 0) {
                throw new IllegalArgumentException("Cannot parse correctResponse " + correct);
            }
        }
        List<Integer> value = pairs.stream().map(AbstractMap.SimpleEntry::getValue).toList();


        Validation validation = new Validation();
        IntValidResponse validResponse = new IntValidResponse();

        validResponse.setValue(value);
        validation.setValidResponse(validResponse);
        choiceMatrix.setValidation(validation);
    }

    private List<String> extractCorrectResponse(AssessmentItem assessmentItem) {
        ResponseDeclaration responseDeclaration = assessmentItem.getResponseDeclaration().getFirst();
        CorrectResponse correctResponse = responseDeclaration.getCorrectResponse();
        return correctResponse.getValue().stream()
                .map(String::trim)
                .toList();
    }

    private List<LinkedHashMap<String, String>> extractSimpleAssociableChoice(AssessmentItem assessmentItem) {
        List<LinkedHashMap<String, String>> result = new ArrayList<>(2);
        ItemBody itemBody = assessmentItem.getItemBody();
        MatchInteraction matchInteraction = (MatchInteraction) itemBody.getInteraction();
        List<SimpleMatchSet> simpleMatchSet = matchInteraction.getSimpleMatchSet();
        for (int i = 0; i < 2; i++) {
            SimpleMatchSet sms = simpleMatchSet.get(i);
            LinkedHashMap<String, String> linkedMap = collectChoices(sms);
            result.add(linkedMap);
        }
        return result;
    }

    private static LinkedHashMap<String, String> collectChoices(SimpleMatchSet sms) {
        LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
        SequencedCollection<SimpleAssociableChoice> simpleAssociableChoice = sms.getSimpleAssociableChoice();
        if (simpleAssociableChoice != null) {
            for (SimpleAssociableChoice sac : simpleAssociableChoice) {
                String identifier = sac.getIdentifier();
                String optionValue = sac.getContent();
                if (optionValue != null) {
                    optionValue = optionValue.trim();
                    linkedMap.put(identifier, optionValue);
                }
            }
        }
        return linkedMap;
    }

    private ChoiceMatrixUiStyle parseUiStyle(AssessmentItem assessmentItem) {
        ChoiceMatrixUiStyle uiStyle = new ChoiceMatrixUiStyle();
        uiStyle.setUiType(extractType(assessmentItem));
        uiStyle.setStemTitle(extractStemTitle(assessmentItem));
        // TODO Complete the implementation by setting the rest of UI fields
        return uiStyle;
    }

    private String extractStemTitle(AssessmentItem assessmentItem) {
        return assessmentItem.getTitle();
    }

    private String extractType(AssessmentItem assessmentItem) {
        // TODO Complete the implementation, for now the default value is used
        return "table";
    }

    private boolean parseMultipleResponses(AssessmentItem assessmentItem) {
        MatchInteraction interaction = (MatchInteraction) assessmentItem.getItemBody().getInteraction();
        return interaction.getMaxAssociations() > 1;
    }
}

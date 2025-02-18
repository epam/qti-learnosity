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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.choice;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.McqUiStyle;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.MultipleChoice;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.Option;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.StringValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.qti.ChoiceInteraction;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.MapEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

@Slf4j
public class ChoiceConverter extends QtiToLearnosityAbstractConverter<MultipleChoice> {

    @Override
    public MultipleChoice convert(AssessmentItem assessmentItem) {
        MultipleChoice multipleChoice = new MultipleChoice();

        multipleChoice.setMath(isMath(assessmentItem));
        multipleChoice.setStimulus(parseStimulus(assessmentItem));
        multipleChoice.setInstructorStimulus(parseInstructorStimulus(assessmentItem).orElse(null));
        multipleChoice.setShuffleOptions(parseShuffleOptions(assessmentItem));
        ChoiceInteraction interaction = (ChoiceInteraction) assessmentItem.getItemBody().getInteraction();
        multipleChoice.setMultipleResponses(parseMultipleResponses(interaction));
        multipleChoice.setUiStyle(parseUiStyle(assessmentItem));
        multipleChoice.setOptions(parseOptions(assessmentItem));
        multipleChoice.setValidation(parseValidation(assessmentItem));
        multipleChoice.setMaxSelection(interaction.getMaxChoices());

        // TODO Implement mapping of these fields or remove it completely to use defaults
        multipleChoice.setStimulusReview(null);
        multipleChoice.setMetadata(null);
        multipleChoice.setFeedbackAttempts(0);
        multipleChoice.setInstantFeedback(false);

        return multipleChoice;
    }

    private SequencedCollection<Option> parseOptions(AssessmentItem assessmentItem) {
        ChoiceInteraction interaction = (ChoiceInteraction) assessmentItem.getItemBody().getInteraction();
        return interaction.getSimpleChoice().stream()
                .map(simpleChoice -> new Option(simpleChoice.getContent(), simpleChoice.getIdentifier()))
                .toList();
    }

    private Validation parseValidation(AssessmentItem assessmentItem) {
        if (assessmentItem.getResponseDeclaration().getFirst().getMapping() == null) {
            log.warn("No response mapping available. Skipping validation...");
            return null;
        }
        Validation validation = new Validation();
        validation.setScoringType(Validation.ScoringType.PARTIAL_MATCH_V2);
        StringValidResponse validResponse = new StringValidResponse();
        SequencedCollection<MapEntry> mapEntries = assessmentItem.getResponseDeclaration().getFirst().getMapping()
                .getMapEntry();
        double totalScore = 0;
        List<String> correctResponseOptions = new ArrayList<>();
        for (MapEntry entry: mapEntries) {
            if (Double.parseDouble(entry.getMappedValue()) > 0) {
                correctResponseOptions.add(entry.getMapKey());
                totalScore += Double.parseDouble(entry.getMappedValue());
            } else {
                log.warn("Unsupported negative score found. Skipping '{}'", entry.getMapKey());
            }
        }
        validResponse.setScore(String.valueOf(totalScore));
        validResponse.setValue(correctResponseOptions);
        validation.setValidResponse(validResponse);
        return validation;
    }

    private McqUiStyle parseUiStyle(AssessmentItem assessmentItem) {
        McqUiStyle uiStyle = new McqUiStyle();
        uiStyle.setUiType(extractType(assessmentItem));
        return uiStyle;
    }

    private McqUiStyle.UiType extractType(AssessmentItem assessmentItem) {
        // TODO Complete the implementation, the default value is used for now
        return McqUiStyle.UiType.HORIZONTAL;
    }

    private boolean parseMultipleResponses(ChoiceInteraction interaction) {
        // 0 indicates unlimited number of choices
        return interaction.getMaxChoices() > 1 || interaction.getMaxChoices() == 0;
    }
}

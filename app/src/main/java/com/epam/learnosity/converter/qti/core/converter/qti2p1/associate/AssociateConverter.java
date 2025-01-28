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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.associate;

import com.epam.learnosity.converter.qti.core.converter.ConversionException;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.associate.learnosity.MatchList;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.associate.qti.AssociateInteraction;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.StringValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.MapEntry;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ResponseDeclaration;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.SimpleAssociableChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;
import java.util.stream.Collectors;

public class AssociateConverter extends QtiToLearnosityAbstractConverter<MatchList> {
    @Override
    public MatchList convert(AssessmentItem assessmentItem) {
        MatchList matchList = new MatchList();
        matchList.setMath(isMath(assessmentItem));
        matchList.setStimulus(parseStimulus(assessmentItem));
        matchList.setShuffleOptions(parseShuffleOptions(assessmentItem));
        matchList.setDuplicateResponses(areDuplicatesAllowed(assessmentItem));
        matchList.setStimulusList(mapStimulusList(assessmentItem));
        matchList.setPossibleResponses(mapPossibleResponses(assessmentItem, matchList.getStimulusList()));
        matchList.setValidation(mapValidation(assessmentItem));
        return matchList;
    }

    private Validation mapValidation(AssessmentItem assessmentItem) {
        ResponseDeclaration responseDeclaration = assessmentItem.getResponseDeclaration().getFirst();
        SequencedCollection<MapEntry> mapEntries = responseDeclaration.getMapping().getMapEntry();
        List<String> correctResponseIds = mapEntries.stream()
                .map(MapEntry::getMapKey)
                .map(key -> key.split(" ")[1])
                .toList();
        AssociateInteraction interaction = (AssociateInteraction) assessmentItem.getItemBody().getInteraction();
        List<SimpleAssociableChoice> associableChoices = interaction.getSimpleAssociableChoice();
        List<String> correctResponses = new ArrayList<>();
        for (String correctResponseId: correctResponseIds) {
            String responseValue = associableChoices.stream()
                    .filter(choice -> choice.getIdentifier().equals(correctResponseId))
                    .map(SimpleAssociableChoice::getContent)
                    .findFirst()
                    .orElseThrow(() -> new ConversionException("No value corresponds to expected choice ID"));
            correctResponses.add(responseValue);
        }

        int totalNumberOfPoints = mapEntries.stream()
                .mapToInt(mapEntry -> Integer.parseInt(mapEntry.getMappedValue()))
                .sum();

        Validation validation = new Validation();
        validation.setScoringType(Validation.ScoringType.PARTIAL_MATCH_V2);

        StringValidResponse validResponse = new StringValidResponse();
        validResponse.setScore(String.valueOf(totalNumberOfPoints));
        validResponse.setValue(correctResponses);
        validation.setValidResponse(validResponse);

        return validation;
    }

    private List<String> mapPossibleResponses(AssessmentItem assessmentItem, List<String> stimulusList) {
        AssociateInteraction interaction = (AssociateInteraction) assessmentItem.getItemBody().getInteraction();
        return interaction.getSimpleAssociableChoice().stream()
                .map(SimpleAssociableChoice::getContent)
                .filter(choiceContent -> !stimulusList.contains(choiceContent))
                .collect(Collectors.toList());
    }

    private List<String> mapStimulusList(AssessmentItem assessmentItem) {
        ResponseDeclaration responseDeclaration = assessmentItem.getResponseDeclaration().getFirst();
        SequencedCollection<MapEntry> mapEntries = responseDeclaration.getMapping().getMapEntry();
        List<String> stimulusListIds = mapEntries.stream()
                .map(MapEntry::getMapKey)
                .map(key -> key.split(" ")[0])
                .toList();
        AssociateInteraction interaction = (AssociateInteraction) assessmentItem.getItemBody().getInteraction();
        return interaction.getSimpleAssociableChoice().stream()
                .filter(choice -> stimulusListIds.contains(choice.getIdentifier()))
                .map(SimpleAssociableChoice::getContent)
                .toList();
    }

    private boolean areDuplicatesAllowed(AssessmentItem assessmentItem) {
        AssociateInteraction interaction = (AssociateInteraction) assessmentItem.getItemBody().getInteraction();
        return interaction.getSimpleAssociableChoice().stream()
                .anyMatch(choice -> choice.getMatchMax() > 1);
    }
}

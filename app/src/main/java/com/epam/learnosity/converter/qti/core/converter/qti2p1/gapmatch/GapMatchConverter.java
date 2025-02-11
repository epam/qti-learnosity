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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.StringValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AltResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.MapEntry;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ResponseDeclaration;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch.learnosity.ClozeAssociation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch.qti.GapMatchInteraction;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch.qti.GapText;
import com.epam.learnosity.converter.qti.core.converter.util.ResponseUtils;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SequencedCollection;
import java.util.stream.Collectors;

public class GapMatchConverter extends QtiToLearnosityAbstractConverter<ClozeAssociation> {

    private static final String GAP_REGEX_PATTERN = "<gap\\s+identifier=\"[^\"]+\"\\s*/>";
    private static final String RESPONSE_ID_REGEX_PATTERN = "identifier=\"(.*?)\"";

    @Override
    public ClozeAssociation convert(AssessmentItem assessmentItem) {
        ClozeAssociation clozeAssociation = new ClozeAssociation();

        GapMatchInteraction gapMatchInteraction = (GapMatchInteraction) assessmentItem.getItemBody().getInteraction();

        clozeAssociation.setStimulus(gapMatchInteraction.getPrompt());
        clozeAssociation.setTemplate(extractTemplate(gapMatchInteraction));
        clozeAssociation.setPossibleResponses(extractPossibleResponses(gapMatchInteraction));
        clozeAssociation.setValidation(extractValidation(assessmentItem));
        clozeAssociation.setDuplicateResponses(areDuplicatesAllowed(gapMatchInteraction));
        return clozeAssociation;
    }

    private boolean areDuplicatesAllowed(GapMatchInteraction gapMatchInteraction) {
        return gapMatchInteraction.getGapText().stream().anyMatch(gapText -> gapText.getMatchMax() > 1);
    }

    private Validation extractValidation(AssessmentItem assessmentItem) {
        Validation validation = new Validation();
        validation.setScoringType(Validation.ScoringType.PARTIAL_MATCH_V2);

        ResponseDeclaration responseDeclaration = assessmentItem.getResponseDeclaration().getFirst();
        SequencedCollection<MapEntry> mapEntries = responseDeclaration.getMapping().getMapEntry();

        GapMatchInteraction gapMatchInteraction = (GapMatchInteraction) assessmentItem.getItemBody().getInteraction();
        List<String> gapIds = ResponseUtils.extractElementIds(gapMatchInteraction.getTextBlock(), GAP_REGEX_PATTERN,
                RESPONSE_ID_REGEX_PATTERN);

        List<List<MapEntry>> gapMatchResponses = new ArrayList<>();
        for (String gapId: gapIds) {
            List<MapEntry> validResponsesForGap = mapEntries.stream()
                    .filter(entry -> entry.getMapKey().split(" ")[1].equals(gapId))
                    .map(entry -> new MapEntry(gapMatchInteraction.getGapText().stream()
                                    .filter(gapText -> gapText.getIdentifier().equals(entry.getMapKey().split(" ")[0]))
                                    .findFirst()
                                    .map(GapText::getContent)
                                    .orElse(null),
                            entry.getMappedValue(),
                            false))
                    .toList();
            gapMatchResponses.add(validResponsesForGap);
        }

        List<List<MapEntry>> cartesianProduct = Lists.cartesianProduct(gapMatchResponses);
        List<StringValidResponse> validResponses = ResponseUtils.mapToStringResponses(cartesianProduct);

        List<StringValidResponse> sortedResponses = validResponses.stream()
                .sorted(Comparator.comparing(stringValidResponse -> Double.parseDouble(stringValidResponse.getScore())))
                .collect(Collectors.toList());
        validation.setValidResponse(sortedResponses.removeLast());
        validation.setAltResponses(sortedResponses.stream()
                .map(response -> new AltResponse(response.getScore(), response.getValue().stream().toList()))
                .toList());

        return validation;
    }

    private List<String> extractPossibleResponses(GapMatchInteraction gapMatchInteraction) {
        return gapMatchInteraction.getGapText().stream().map(GapText::getContent).toList();
    }

    private String extractTemplate(GapMatchInteraction gapMatchInteraction) {
        return gapMatchInteraction.getTextBlock().replaceAll(GAP_REGEX_PATTERN, "{{response}}");
    }
}

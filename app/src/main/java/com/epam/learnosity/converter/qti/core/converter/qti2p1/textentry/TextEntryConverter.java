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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.textentry;

import com.epam.learnosity.converter.qti.core.converter.ConversionException;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.StringValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AltResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.MapEntry;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ResponseDeclaration;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.textentry.learnosity.ClozeText;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class TextEntryConverter extends QtiToLearnosityAbstractConverter<ClozeText> {

    private static final String TEXT_ENTRY_REGEX_PATTERN = "<textEntryInteraction[^>]*/>";
    private static final String RESPONSE_ID_REGEX_PATTERN = "responseIdentifier=\"(.*?)\"";

    private boolean isCaseSensitive;

    @Override
    public ClozeText convert(AssessmentItem assessmentItem) {
        ClozeText clozeText = new ClozeText();
        clozeText.setMath(isMath(assessmentItem));
        clozeText.setTemplate(extractTemplate(assessmentItem));
        clozeText.setValidation(extractValidation(assessmentItem));
        clozeText.setCaseSensitive(isCaseSensitive);
        return clozeText;
    }

    private Validation extractValidation(AssessmentItem assessmentItem) {
        var validation = new Validation();
        validation.setScoringType(Validation.ScoringType.EXACT_MATCH);
        String content = assessmentItem.getItemBody().getContentAsSingleString();

        List<String> responseIds = extractResponseIds(content);
        List<ResponseDeclaration> responseDeclarations = assessmentItem.getResponseDeclaration();
        List<List<MapEntry>> interactionResponses = new ArrayList<>();
        for (String responseId: responseIds) {
            Optional<ResponseDeclaration> responseDeclaration = responseDeclarations.stream()
                    .filter(declaration -> declaration.getIdentifier().equals(responseId))
                    .findFirst();
            if (responseDeclaration.isPresent()) {
                List<MapEntry> textEntryResponses = new ArrayList<>(responseDeclaration.get()
                        .getMapping().getMapEntry());
                setCaseSensitivity(textEntryResponses);
                interactionResponses.add(textEntryResponses);
            } else {
                throw new ConversionException("Invalid XML encountered. No response declaration available for the " +
                        "given responseId");
            }
        }
        List<List<MapEntry>> cartesianProduct = Lists.cartesianProduct(interactionResponses);
        List<StringValidResponse> validResponses = mapToLearnosityResponses(cartesianProduct);

        List<StringValidResponse> sortedResponses = validResponses.stream()
                .sorted(Comparator.comparing(stringValidResponse -> Double.parseDouble(stringValidResponse.getScore())))
                .collect(Collectors.toList());
        validation.setValidResponse(sortedResponses.removeLast());
        validation.setAltResponses(sortedResponses.stream()
                .map(response -> new AltResponse(response.getScore(), response.getValue().stream().toList()))
                .toList());
        return validation;
    }

    private void setCaseSensitivity(List<MapEntry> textEntryResponses) {
        if (textEntryResponses.stream().anyMatch(MapEntry::isCaseSensitive)) {
            isCaseSensitive = true;
        }
    }

    private static List<StringValidResponse> mapToLearnosityResponses(List<List<MapEntry>> cartesianProduct) {
        List<StringValidResponse> validResponses = new ArrayList<>();
        for (List<MapEntry> textEntryResponses: cartesianProduct) {
            StringValidResponse validResponse = new StringValidResponse();
            double totalScore = 0;
            List<String> responses = new ArrayList<>();
            for (MapEntry mapEntry: textEntryResponses) {
                totalScore = totalScore + Double.parseDouble(mapEntry.getMappedValue());
                responses.add(mapEntry.getMapKey());
            }
            validResponse.setScore(String.valueOf(totalScore));
            validResponse.setValue(responses);
            validResponses.add(validResponse);
        }
        return validResponses;
    }

    private List<String> extractResponseIds(String content) {
        List<String> responseIds = new ArrayList<>();
        Pattern textEntryPattern = Pattern.compile(TEXT_ENTRY_REGEX_PATTERN);
        Matcher textEntryMatcher = textEntryPattern.matcher(content);
        while (textEntryMatcher.find()) {
            String textEntryElement = textEntryMatcher.group(0);
            Pattern responseIdPattern = Pattern.compile(RESPONSE_ID_REGEX_PATTERN);
            Matcher responseIdMatcher = responseIdPattern.matcher(textEntryElement);
            String id = responseIdMatcher.find() ? responseIdMatcher.group(1) : null;
            responseIds.add(id);
        }
        return responseIds;
    }

    private String extractTemplate(AssessmentItem assessmentItem) {
        return assessmentItem.getItemBody().getContentAsSingleString()
                .replaceAll(TEXT_ENTRY_REGEX_PATTERN, "{{response}}");
    }
}

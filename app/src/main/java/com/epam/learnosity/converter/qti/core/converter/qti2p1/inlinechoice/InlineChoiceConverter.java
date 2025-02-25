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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.inlinechoice;

import com.epam.learnosity.converter.qti.core.converter.ConversionException;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.StringValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.Validation;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.inlinechoice.learnosity.ClozeDropdown;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InlineChoiceConverter extends QtiToLearnosityAbstractConverter<ClozeDropdown> {

    private static final String MATCH_CORRECT_TEMPLATE = "http://www.imsglobal.org/question/qti_v2p1/rptemplates" +
            "/match_correct";
    private static final String INLINE_CHOICE_INTERACTION_PATTERN = "<inlineChoiceInteraction\\s+[^>]*>[\\s\\" +
            "S]*?</inlineChoiceInteraction>";
    private static final String INLINE_CHOICE_PATTERN = "<inlineChoice\\s+[^>]*>([^<]*)</inlineChoice>";
    private static final String RESPONSE_IDENTIFIER_PATTERN = "responseIdentifier=\"([^\"]+)\"";
    private static final String INLINE_CHOICE_VALUE_PATTERN_TEMPLATE = "<inlineChoice\\s+identifier=\"{0}\"[^>]*>" +
            "([^<]+)</inlineChoice>";

    @Override
    public ClozeDropdown convert(AssessmentItem assessmentItem) {
        if (!MATCH_CORRECT_TEMPLATE.equals(assessmentItem.getResponseProcessing().getTemplate())) {
            throw new ConversionException("Unsupported response processing template. Please use match_correct");
        }
        ClozeDropdown clozeDropdown = new ClozeDropdown();
        clozeDropdown.setTemplate(extractTemplate(assessmentItem));
        clozeDropdown.setPossibleResponses(extractPossibleResponses(assessmentItem));
        clozeDropdown.setShuffleOptions(isShuffleSet(assessmentItem));
        clozeDropdown.setValidation(extractValidation(assessmentItem));
        return clozeDropdown;
    }

    private boolean isShuffleSet(AssessmentItem assessmentItem) {
        return assessmentItem.getItemBody().getContentAsSingleString().contains("shuffle=\"true\"");
    }

    private String extractTemplate(AssessmentItem assessmentItem) {
        return assessmentItem.getItemBody().getContentAsSingleString()
                .replaceAll(INLINE_CHOICE_INTERACTION_PATTERN, "{{response}}");
    }

    private List<List<String>> extractPossibleResponses(AssessmentItem assessmentItem) {
        String content = assessmentItem.getItemBody().getContentAsSingleString();
        Matcher inlineChoiceInteractionMatcher = Pattern.compile(INLINE_CHOICE_INTERACTION_PATTERN).matcher(content);
        List<List<String>> possibleResponses = new ArrayList<>();
        while (inlineChoiceInteractionMatcher.find()) {
            String inlineChoiceInteraction = inlineChoiceInteractionMatcher.group(0);
            Matcher inlineChoiceMatcher = Pattern.compile(INLINE_CHOICE_PATTERN).matcher(inlineChoiceInteraction);
            List<String> inlineChoiceInteractionResponses = new ArrayList<>();
            while (inlineChoiceMatcher.find()) {
                inlineChoiceInteractionResponses.add(inlineChoiceMatcher.group(1));
            }
            possibleResponses.add(inlineChoiceInteractionResponses);
        }
        return possibleResponses;
    }

    private Validation extractValidation(AssessmentItem assessmentItem) {
        var validation = new Validation();
        validation.setScoringType(Validation.ScoringType.PARTIAL_MATCH_V2);
        var validResponse = new StringValidResponse();
        List<String> validResponses = extractValidResponses(assessmentItem);
        validResponse.setScore(String.valueOf(validResponses.size()));
        validResponse.setValue(validResponses);
        validation.setValidResponse(validResponse);
        return validation;
    }

    private static List<String> extractValidResponses(AssessmentItem assessmentItem) {
        String content = assessmentItem.getItemBody().getContentAsSingleString();
        Matcher responseIdMatcher = Pattern.compile(RESPONSE_IDENTIFIER_PATTERN).matcher(content);
        List<String> validResponses = new ArrayList<>();
        while (responseIdMatcher.find()) {
            String responseId = responseIdMatcher.group(1);
            SequencedCollection<String> correctResponseValues = assessmentItem.getResponseDeclaration().stream()
                    .filter(responseDeclaration -> responseDeclaration.getIdentifier().equals(responseId))
                    .map(responseDeclaration -> responseDeclaration.getCorrectResponse().getValue())
                    .findFirst()
                    .orElseThrow(() -> new ConversionException("Invalid XML encountered. No response declaration " +
                            "available for the given responseId"));
            // assuming there is only one correct response per inlineChoiceInteraction
            String correctInlineChoiceValuePattern = MessageFormat.format(INLINE_CHOICE_VALUE_PATTERN_TEMPLATE,
                    correctResponseValues.getFirst());
            Matcher inlineChoiceValueMatcher = Pattern.compile(correctInlineChoiceValuePattern).matcher(content);
            String correctResponseValue = inlineChoiceValueMatcher.find() ? inlineChoiceValueMatcher.group(1) : null;
            validResponses.add(correctResponseValue);
        }
        return validResponses;
    }
}

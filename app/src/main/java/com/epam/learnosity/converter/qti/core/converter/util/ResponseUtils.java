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
package com.epam.learnosity.converter.qti.core.converter.util;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity.StringValidResponse;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.MapEntry;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ResponseUtils {
    /**
     * Extracts specified identifiers from a block of text by targeting specified elements
     *
     * @param content The block of text to extract identifiers from
     * @param elementPattern The regex pattern for elements to target
     * @param idPattern The pattern for identifier fields
     * @return The list of extracted identifiers
     */
    public List<String> extractElementIds(String content, String elementPattern, String idPattern) {
        List<String> responseIds = new ArrayList<>();
        Pattern elementRegexPattern = Pattern.compile(elementPattern);
        Matcher elementMatcher = elementRegexPattern.matcher(content);
        while (elementMatcher.find()) {
            String gapMatchElement = elementMatcher.group(0);
            Pattern responseIdPattern = Pattern.compile(idPattern);
            Matcher responseIdMatcher = responseIdPattern.matcher(gapMatchElement);
            String id = responseIdMatcher.find() ? responseIdMatcher.group(1) : null;
            responseIds.add(id);
        }
        return responseIds;
    }

    public List<StringValidResponse> mapToStringResponses(List<List<MapEntry>> cartesianProduct) {
        List<StringValidResponse> validResponses = new ArrayList<>();
        for (List<MapEntry> interactionResponses: cartesianProduct) {
            StringValidResponse validResponse = new StringValidResponse();
            double totalScore = 0;
            List<String> responses = new ArrayList<>();
            for (MapEntry mapEntry: interactionResponses) {
                totalScore = totalScore + Double.parseDouble(mapEntry.getMappedValue());
                responses.add(mapEntry.getMapKey());
            }
            validResponse.setScore(String.valueOf(totalScore));
            validResponse.setValue(responses);
            validResponses.add(validResponse);
        }
        return validResponses;
    }


}

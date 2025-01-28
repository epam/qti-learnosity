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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.common;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.QtiToLearnosityConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AbstractQuestionType;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ItemBody;

import java.util.Optional;

public abstract class QtiToLearnosityAbstractConverter<T extends AbstractQuestionType<?>>
        implements QtiToLearnosityConverter<T> {

    protected boolean parseShuffleOptions(AssessmentItem assessmentItem) {
        return assessmentItem.getItemBody().getInteraction().isShuffle();
    }

    protected Optional<String> parseInstructorStimulus(AssessmentItem assessmentItem) {
        // TODO Check if this mapping is relevant
        return Optional.empty();
    }

    protected String parseStimulus(AssessmentItem assessmentItem) {
        ItemBody itemBody = assessmentItem.getItemBody();
        String itemBodyContent = itemBody.getContentAsSingleString();
        String interactionPrompt = itemBody.getInteraction().getPrompt();
        if (itemBodyContent == null) {
            return interactionPrompt;
        }
        if (interactionPrompt == null) {
            return itemBodyContent;
        }
        if (itemBodyContent.startsWith("<") && !interactionPrompt.startsWith("<")) {
            interactionPrompt = "<p>" + interactionPrompt + "</p>";
        }
        return itemBodyContent + interactionPrompt;
    }

    protected boolean isMath(AssessmentItem assessmentItem) {
        // TODO To be implemented later
        return false;
    }
}

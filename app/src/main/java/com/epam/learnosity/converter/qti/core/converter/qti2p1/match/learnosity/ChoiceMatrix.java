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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.match.learnosity;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AbstractQuestionType;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AbstractUiStyle;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.SequencedCollection;

/**
 * Learnosity's Choice Matrix element which requires an association to be made between two sets of choices.
 *
 * @see <a href="https://help.learnosity.com/hc/en-us/articles/16685251694749-Choice-matrix-choicematrix">Choice Matrix</a>
 */
@Getter
@Setter
public class ChoiceMatrix extends AbstractQuestionType<String> {

    public ChoiceMatrix() {
        super("choicematrix");
    }

    @Override
    public ChoiceMatrixUiStyle getUiStyle() {
        return (ChoiceMatrixUiStyle) super.getUiStyle();
    }

    @Override
    public void setUiStyle(AbstractUiStyle uiStyle) {
        if (uiStyle instanceof ChoiceMatrixUiStyle) {
            super.setUiStyle(uiStyle);
        } else {
            throw new IllegalArgumentException("ChoiceMatrixUiStyle is expected");
        }
    }

    @SerializedName("multiple_responses")
    private boolean multipleResponses;

    private SequencedCollection<String> stems;
}

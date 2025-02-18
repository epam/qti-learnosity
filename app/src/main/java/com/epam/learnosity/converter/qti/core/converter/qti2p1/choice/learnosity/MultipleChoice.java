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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.learnosity;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AbstractQuestionType;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AbstractUiStyle;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Learnosity's Multiple Choice element which requires an item to be selected.
 *
 * @see <a href="https://reference.learnosity.com/questions-api/questiontypes#mcq">Multiple choice</a>
 */
@Setter
@Getter
public class MultipleChoice extends AbstractQuestionType<Option> {

    @SerializedName("multiple_responses")
    private boolean multipleResponses;

    @SerializedName("max_selection")
    private int maxSelection;

    public MultipleChoice() {
        super("mcq");
    }

    @Override
    public McqUiStyle getUiStyle() {
        return (McqUiStyle) super.getUiStyle();
    }

    @Override
    public void setUiStyle(AbstractUiStyle uiStyle) {
        if (uiStyle instanceof McqUiStyle) {
            super.setUiStyle(uiStyle);
        } else {
            throw new IllegalArgumentException("McqUiStyle is expected");
        }
    }
}

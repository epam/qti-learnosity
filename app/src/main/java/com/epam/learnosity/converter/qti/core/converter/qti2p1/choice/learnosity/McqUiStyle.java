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

import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AbstractUiStyle;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McqUiStyle extends AbstractUiStyle {
    @SerializedName("type")
    private UiType uiType;

    @SerializedName("choice_label")
    private String choiceLabel;

    @SerializedName("columns")
    private int columns = 1;

    @SerializedName("orientation")
    private Orientation orientation = Orientation.VERTICAL;

    public enum Orientation {
        @SerializedName("vertical")
        VERTICAL,

        @SerializedName("horizontal")
        HORIZONTAL
    }

    public enum UiType {
        @SerializedName("horizontal")
        HORIZONTAL,

        @SerializedName("block")
        BLOCK,

        @SerializedName("horizontal-input-bottom")
        HORIZONTAL_INPUT_BOTTOM
    }
}

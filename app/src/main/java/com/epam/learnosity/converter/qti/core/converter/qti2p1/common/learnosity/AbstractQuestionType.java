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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.SequencedCollection;

/**
 * Represents a Learnosity question type
 *
 * @see <a href="https://reference.learnosity.com/questions-api/questiontypes">Question Types</a>
 */
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractQuestionType<T> {

    private final String type;

    @SerializedName("is_math")
    private boolean isMath;

    private Metadata metadata;

    private String stimulus;

    @SerializedName("stimulus_review")
    private String stimulusReview;

    @SerializedName("instructor_stimulus")
    private String instructorStimulus;

    @SerializedName("feedback_attempts")
    private int feedbackAttempts = 0;

    @SerializedName("instant_feedback")
    private boolean instantFeedback;

    private Validation validation;

    @SerializedName("shuffle_options")
    private boolean shuffleOptions;

    // TODO this is not present everywhere and has to be moved to lower classes
    private SequencedCollection<T> options;

    @SerializedName("ui_style")
    private AbstractUiStyle uiStyle;

    @SerializedName("case_sensitive")
    private boolean caseSensitive;

}

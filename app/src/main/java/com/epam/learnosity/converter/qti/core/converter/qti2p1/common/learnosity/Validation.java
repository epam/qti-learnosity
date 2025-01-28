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
import lombok.Data;

import java.util.List;

@Data
public class Validation {
    @SerializedName("allow_negative_scores")
    private boolean allowNegativeScores;

    @SerializedName("penalty")
    private int penalty;

    @SerializedName("min_score_if_attempted")
    private int minScoreIfAttempted;

    @SerializedName("scoring_type")
    private ScoringType scoringType = ScoringType.EXACT_MATCH;

    @SerializedName("unscored")
    private boolean unscored;

    @SerializedName("valid_response")
    private AbstractValidResponse<?> validResponse;

    @SerializedName("automarkable")
    private boolean automarkable = true;

    @SerializedName("alt_responses")
    private List<AltResponse> altResponses;

    public enum ScoringType {
        /**
         * choiceMatrix - All parts of the question must be answered correctly to receive a mark
         * <p>
         * mcq - Entire response must match exactly
         */
        @SerializedName("exactMatch")
        EXACT_MATCH,
        /**
         * choiceMatrix - Each correct response element will be scored individually, and the overall question score will be divided between responses
         * <p>
         * mcq - A relative part of the score will be given for each correct response area
         */
        @SerializedName("partialMatchV2")
        PARTIAL_MATCH_V2,
        /**
         * choiceMatrix - Each correct response element will be awarded an individual score
         * <p>
         * mcq - Cumulative Score value will be given for each correct response area
         */
        @SerializedName("partialMatch")
        PARTIAL_MATCH
    }
}

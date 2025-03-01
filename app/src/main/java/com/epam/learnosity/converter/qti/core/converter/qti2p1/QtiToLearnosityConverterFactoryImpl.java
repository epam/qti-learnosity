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
package com.epam.learnosity.converter.qti.core.converter.qti2p1;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.associate.AssociateConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.choice.ChoiceConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.Interaction;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch.GapMatchConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.inlinechoice.InlineChoiceConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.match.MatchConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.order.OrderConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.textentry.TextEntryConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.upload.UploadConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class QtiToLearnosityConverterFactoryImpl implements QtiToLearnosityConverterFactory {

    private final Map<QtiType, QtiToLearnosityConverter<?>> converters;

    public QtiToLearnosityConverterFactoryImpl() {
        converters = Map.of(
                QtiType.MATCH, new MatchConverter(),
                QtiType.CHOICE, new ChoiceConverter(),
                QtiType.GAP_MATCH, new GapMatchConverter(),
                QtiType.INLINE_CHOICE, new InlineChoiceConverter(),
                QtiType.ORDER, new OrderConverter(),
                QtiType.TEXT_ENTRY, new TextEntryConverter(),
                QtiType.ASSOCIATE, new AssociateConverter(),
                QtiType.UPLOAD, new UploadConverter()
        );
    }

    @Override
    public Optional<QtiToLearnosityConverter<?>> getConverter(AssessmentItem assessmentItem) {
        Interaction interaction = assessmentItem.getItemBody().getInteraction();
        if (interaction == null) {
            switch (assessmentItem.getItemBody().getContentAsSingleString()) {
                case String s when s.contains("textEntry") -> {
                    return Optional.ofNullable(converters.get(QtiType.TEXT_ENTRY));
                }
                case String s when s.contains("inlineChoice") -> {
                    return Optional.ofNullable(converters.get(QtiType.INLINE_CHOICE));
                }
                default -> {
                    return Optional.empty();
                }
            }
        } else {
            return Optional.ofNullable(converters.get(interaction.getType()));
        }
    }
}

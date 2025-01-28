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
package com.epam.learnosity.converter.qti.core.converter;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.AssessmentItemReader;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.QtiToLearnosityConverterFactory;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.QtiToLearnosityConverterFactoryImpl;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.google.gson.GsonBuilder;
import jakarta.xml.bind.JAXBException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConverterImpl implements Converter {

    private final QtiToLearnosityConverterFactory converterFactory;
    private final AssessmentItemReader assessmentItemReader;

    @SneakyThrows
    public ConverterImpl() {
        converterFactory = new QtiToLearnosityConverterFactoryImpl();
        assessmentItemReader = new AssessmentItemReader();
    }

    @Override
    public String convertToLearnosity(String qtiContent) throws ConversionException {
        AssessmentItem assessmentItem;
        try {
            assessmentItem = assessmentItemReader.read(qtiContent);
        } catch (JAXBException e) {
            throw new ConversionException("Cannot convert " + qtiContent, e);
        }
        return converterFactory.getConverter(assessmentItem)
                .map(converter -> converter.convert(assessmentItem))
                .map(new GsonBuilder().setPrettyPrinting().create()::toJson)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported assessment type"));
    }

}

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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.gapmatch.qti;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.QtiType;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.Interaction;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.ItemBodyContentDomHandler;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * QTI GapMatchInteraction element which requires selecting choices from a set and using them to fill the gaps.
 *
 * @see <a href="https://www.imsglobal.org/question/qtiv2p1/imsqti_infov2p1.html#element10307">GapMatchInteraction</a>
 */
@Getter
@Setter
@XmlRootElement(namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1")
@XmlAccessorType(XmlAccessType.FIELD)
public class GapMatchInteraction extends Interaction {

    @XmlElement(namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1")
    private List<GapText> gapText;

    @Override
    public QtiType getType() {
        return QtiType.GAP_MATCH;
    }

    @XmlAnyElement(value = ItemBodyContentDomHandler.class)
    private String textBlock;
}

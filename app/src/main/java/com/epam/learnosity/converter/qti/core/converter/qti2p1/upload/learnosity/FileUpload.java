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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.upload.learnosity;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.learnosity.AbstractQuestionType;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Learnosity's FileUpload element which allows to upload a file to Learnosity, for review by the teacher.
 *
 * @see <a href="https://help.learnosity.com/hc/en-us/articles/16685209311261-File-upload-fileupload">FileUpload</a>
 */
@Getter
@Setter
public class FileUpload extends AbstractQuestionType<String> {

    @SerializedName("allow_pdf")
    private boolean allowPdf;

    @SerializedName("allow_jpg")
    private boolean allowJpg;

    @SerializedName("allow_gif")
    private boolean allowGif;

    @SerializedName("allow_png")
    private boolean allowPng;

    @SerializedName("allow_csv")
    private boolean allowCsv;

    @SerializedName("allow_rtf")
    private boolean allowRtf;

    @SerializedName("allow_txt")
    private boolean allowTxt;

    @SerializedName("allow_xps")
    private boolean allowXps;

    @SerializedName("allow_zip")
    private boolean allowZip;

    @SerializedName("allow_ms_word")
    private boolean allowMsWord;

    @SerializedName("allow_ms_excel")
    private boolean allowMsExcel;

    @SerializedName("allow_ms_powerpoint")
    private boolean allowMsPowerpoint;

    @SerializedName("allow_ms_publisher")
    private boolean allowMsPublisher;

    @SerializedName("allow_open_office")
    private boolean allowOpenOffice;

    @SerializedName("allow_video")
    private boolean allowVideo;

    @SerializedName("allow_matlab")
    private boolean allowMatlab;

    @SerializedName("allow_altera_quartus")
    private boolean allowAlteraQuartus;

    @SerializedName("allow_verilog")
    private boolean allowVerilog;

    @SerializedName("allow_c")
    private boolean allowC;

    @SerializedName("allow_h")
    private boolean allowH;

    @SerializedName("allow_s")
    private boolean allowS;

    @SerializedName("allow_v")
    private boolean allowV;

    @SerializedName("allow_cpp")
    private boolean allowCpp;

    @SerializedName("allow_assembly")
    private boolean allowAssembly;

    @SerializedName("allow_labview")
    private boolean allowLabview;

    @SerializedName("allow_psd")
    private boolean allowPsd;

    @SerializedName("allow_ai")
    private boolean allowAi;

    public FileUpload() {
        super("fileupload");
    }
}

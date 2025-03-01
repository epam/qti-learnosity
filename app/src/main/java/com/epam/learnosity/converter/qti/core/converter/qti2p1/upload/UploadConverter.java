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
package com.epam.learnosity.converter.qti.core.converter.qti2p1.upload;

import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.QtiToLearnosityAbstractConverter;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.common.qti.AssessmentItem;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.upload.learnosity.FileUpload;
import com.epam.learnosity.converter.qti.core.converter.qti2p1.upload.qti.UploadInteraction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadConverter extends QtiToLearnosityAbstractConverter<FileUpload> {

    @Override
    public FileUpload convert(AssessmentItem assessmentItem) {
        var fileUpload = new FileUpload();
        fileUpload.setStimulus(parseStimulus(assessmentItem));
        setAllowedFileTypes(assessmentItem, fileUpload);
        return fileUpload;
    }

    private static void setAllowedFileTypes(AssessmentItem assessmentItem, FileUpload fileUpload) {
        UploadInteraction uploadInteraction = (UploadInteraction) assessmentItem.getItemBody().getInteraction();
        String acceptedFileTypes = uploadInteraction.getFileType();
        for (String mimeType: acceptedFileTypes.split(" ")) {
            switch (mimeType) {
                case "application/pdf" -> fileUpload.setAllowPdf(true);
                case "image/jpeg" -> fileUpload.setAllowJpg(true);
                case "image/gif" -> fileUpload.setAllowGif(true);
                case "image/png" -> fileUpload.setAllowPng(true);
                case "text/csv" -> fileUpload.setAllowCsv(true);
                case "application/rtf" -> fileUpload.setAllowRtf(true);
                case "text/plain" -> {
                    fileUpload.setAllowTxt(true);
                    fileUpload.setAllowVerilog(true);
                    fileUpload.setAllowC(true);
                    fileUpload.setAllowH(true);
                    fileUpload.setAllowS(true);
                    fileUpload.setAllowV(true);
                    fileUpload.setAllowCpp(true);
                    fileUpload.setAllowAssembly(true);
                }
                case "application/vnd.ms-xpsdocument" -> fileUpload.setAllowXps(true);
                case "application/zip" -> fileUpload.setAllowZip(true);
                case "application/msword",
                     "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        -> fileUpload.setAllowMsWord(true);
                case "application/vnd.ms-excel",
                     "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        -> fileUpload.setAllowMsExcel(true);
                case "application/vnd.ms-powerpoint",
                     "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                        -> fileUpload.setAllowMsPowerpoint(true);
                case "application/x-mspublisher" -> fileUpload.setAllowMsPublisher(true);
                case "application/vnd.oasis.opendocument.base",
                     "application/vnd.oasis.opendocument.chart",
                     "application/vnd.oasis.opendocument.chart-template",
                     "application/vnd.oasis.opendocument.formula",
                     "application/vnd.oasis.opendocument.formula-template",
                     "application/vnd.oasis.opendocument.graphics",
                     "application/vnd.oasis.opendocument.graphics-template",
                     "application/vnd.oasis.opendocument.image",
                     "application/vnd.oasis.opendocument.image-template",
                     "application/vnd.oasis.opendocument.presentation",
                     "application/vnd.oasis.opendocument.presentation-template",
                     "application/vnd.oasis.opendocument.spreadsheet",
                     "application/vnd.oasis.opendocument.spreadsheet-template",
                     "application/vnd.oasis.opendocument.text",
                     "application/vnd.oasis.opendocument.text-master",
                     "application/vnd.oasis.opendocument.text-master-template",
                     "application/vnd.oasis.opendocument.text-template",
                     "application/vnd.oasis.opendocument.text-web"
                     -> fileUpload.setAllowOpenOffice(true);
                case "video/1d-interleaved-parityfec",
                     "video/3gpp",
                     "video/3gpp2",
                     "video/3gpp-tt",
                     "video/AV1",
                     "video/BMPEG",
                     "video/BT656",
                     "video/CelB",
                     "video/DV",
                     "video/encaprtp",
                     "video/evc",
                     "video/example",
                     "video/FFV1",
                     "video/flexfec",
                     "video/H261",
                     "video/H263",
                     "video/H263-1998",
                     "video/H263-2000",
                     "video/H264",
                     "video/H264-RCDO",
                     "video/H264-SVC",
                     "video/H265",
                     "video/H266",
                     "video/iso.segment",
                     "video/JPEG",
                     "video/jpeg2000",
                     "video/jxsv",
                     "video/lottie+json",
                     "video/matroska",
                     "video/matroska-3d",
                     "video/mj2",
                     "video/MP1S",
                     "video/MP2P",
                     "video/MP2T",
                     "video/mp4",
                     "video/MP4V-ES",
                     "video/MPV",
                     "video/mpeg",
                     "video/mpeg4-generic",
                     "video/nv",
                     "video/ogg",
                     "video/parityfec",
                     "video/pointer",
                     "video/quicktime",
                     "video/raptorfec",
                     "video/raw",
                     "video/rtp-enc-aescm128",
                     "video/rtploopback",
                     "video/rtx",
                     "video/scip",
                     "video/smpte291",
                     "video/SMPTE292M",
                     "video/ulpfec",
                     "video/vc1",
                     "video/vc2",
                     "video/vnd.CCTV",
                     "video/vnd.dece.hd",
                     "video/vnd.dece.mobile",
                     "video/vnd.dece.mp4",
                     "video/vnd.dece.pd",
                     "video/vnd.dece.sd",
                     "video/vnd.dece.video",
                     "video/vnd.directv.mpeg",
                     "video/vnd.directv.mpeg-tts",
                     "video/vnd.dlna.mpeg-tts",
                     "video/vnd.dvb.file",
                     "video/vnd.fvt",
                     "video/vnd.hns.video",
                     "video/vnd.iptvforum.1dparityfec-1010",
                     "video/vnd.iptvforum.1dparityfec-2005",
                     "video/vnd.iptvforum.2dparityfec-1010",
                     "video/vnd.iptvforum.2dparityfec-2005",
                     "video/vnd.iptvforum.ttsavc",
                     "video/vnd.iptvforum.ttsmpeg2",
                     "video/vnd.motorola.video",
                     "video/vnd.motorola.videop",
                     "video/vnd.mpegurl",
                     "video/vnd.ms-playready.media.pyv",
                     "video/vnd.nokia.interleaved-multimedia",
                     "video/vnd.nokia.mp4vr",
                     "video/vnd.nokia.videovoip",
                     "video/vnd.objectvideo",
                     "video/vnd.planar",
                     "video/vnd.radgamettools.bink",
                     "video/vnd.radgamettools.smacker",
                     "video/vnd.sealed.mpeg1",
                     "video/vnd.sealed.mpeg4",
                     "video/vnd.sealed.swf",
                     "video/vnd.sealedmedia.softseal.mov",
                     "video/vnd.uvvu.mp4",
                     "video/vnd.youtube.yt",
                     "video/vnd.vivo",
                     "video/VP8",
                     "video/VP9"
                    -> fileUpload.setAllowVideo(true);
                case "application/octet-stream" -> {
                    fileUpload.setAllowMatlab(true);
                    fileUpload.setAllowAlteraQuartus(true);
                    fileUpload.setAllowAlteraQuartus(true);
                    fileUpload.setAllowLabview(true);
                }
                case "image/vnd.adobe.photoshop" -> fileUpload.setAllowPsd(true);
                case "application/postscript" -> fileUpload.setAllowAi(true);
                default -> {
                    fileUpload.setAllowPdf(true);
                    fileUpload.setAllowJpg(true);
                    fileUpload.setAllowGif(true);
                    fileUpload.setAllowPng(true);
                    fileUpload.setAllowCsv(true);
                    fileUpload.setAllowRtf(true);
                    fileUpload.setAllowTxt(true);
                    fileUpload.setAllowXps(true);
                    fileUpload.setAllowZip(true);
                    fileUpload.setAllowMsWord(true);
                    fileUpload.setAllowMsExcel(true);
                    fileUpload.setAllowMsPowerpoint(true);
                    fileUpload.setAllowMsPublisher(true);
                    fileUpload.setAllowOpenOffice(true);
                    fileUpload.setAllowVideo(true);
                    fileUpload.setAllowMatlab(true);
                    fileUpload.setAllowAlteraQuartus(true);
                    fileUpload.setAllowVerilog(true);
                    fileUpload.setAllowC(true);
                    fileUpload.setAllowH(true);
                    fileUpload.setAllowS(true);
                    fileUpload.setAllowV(true);
                    fileUpload.setAllowCpp(true);
                    fileUpload.setAllowAssembly(true);
                    fileUpload.setAllowLabview(true);
                    fileUpload.setAllowPsd(true);
                    fileUpload.setAllowAi(true);
                }
            }
        }
    }
}

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
package com.epam.learnosity.converter.qti;

import com.epam.learnosity.converter.qti.core.Resource;
import com.epam.learnosity.converter.qti.core.converter.Converter;
import com.epam.learnosity.converter.qti.core.converter.ConverterImpl;
import com.epam.learnosity.converter.qti.core.extractor.file.FileSourceSupplier;
import com.epam.learnosity.converter.qti.core.loader.SourceLoader;
import com.epam.learnosity.converter.qti.core.loader.file.FileSourceLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class App {

    public static void main(String[] args) {
        log.info("Starting QTI to Learnosity Converter (v{})", App.class.getPackage().getImplementationVersion());

        if (args.length < 2) {
            log.error("Insufficient arguments provided. The converter expects two arguments: the source directory " +
                    "path and the destination directory path.");
            System.exit(1);
        }

        String sourcePath = args[0];
        String targetPath = args[1];

        log.info("Searching for QTI files to convert in '{}'", sourcePath);
        FileSourceSupplier fileSourceSupplier = new FileSourceSupplier(sourcePath);
        SourceLoader loader = new FileSourceLoader(targetPath);
        Converter converter = new ConverterImpl();
        int processedFilesCounter = 0;
        int totalFilesCounter = 0;
        try {
            List<Resource> resources = fileSourceSupplier.getContentStream().toList();
            totalFilesCounter = resources.size();
            log.info("{} XML files found", totalFilesCounter);
            for (int i = 0; i < resources.size(); i++) {
                try {
                    log.info("Converting file {}/{} '{}'", i + 1, totalFilesCounter, resources.get(i).name());
                    String convertedContent = converter.convertToLearnosity(resources.get(i).content());
                    String name = resources.get(i).name().replace(".xml", ".json");
                    loader.save(name, convertedContent);
                    log.info("Successfully converted '{}' and stored in '{}'", resources.get(i).name(), targetPath);
                    processedFilesCounter++;
                } catch (Exception e) {
                    log.warn("Cannot process '{}'", resources.get(i).name(), e);
                }
            }
        } catch (Exception e) {
            log.error("Cannot process '{}'", sourcePath, e);
        }
        log.info("Processing completed. {}/{} files successfully converted", processedFilesCounter, totalFilesCounter);
    }
}

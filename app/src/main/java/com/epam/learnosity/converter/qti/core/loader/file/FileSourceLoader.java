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
package com.epam.learnosity.converter.qti.core.loader.file;

import com.epam.learnosity.converter.qti.core.loader.SourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileSourceLoader implements SourceLoader {

    private final String targetDirectory;

    public FileSourceLoader(String targetDirectory) {
        this.targetDirectory = getPathWithTrailingSlash(targetDirectory);
    }

    @Override
    public void save(String fileName, String content) throws IOException {
        Path filePath = Paths.get(targetDirectory + fileName);
        if (Files.exists(filePath)) {
            throw new IOException("File already exists: " + filePath);
        }
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content.lines().toList(), StandardOpenOption.CREATE_NEW);
    }

    public static String getPathWithTrailingSlash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }

}

/*
 * The MIT License
 *
 * Copyright 2015 enlo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.naiv.lab.java.jmt.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.Files.deleteIfExists;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class RecursiveFileDeleter extends SimpleFileVisitor<Path> {

    private final boolean deleteDirectory;

    /**
     *
     * @param deleteDirectory
     */
    public RecursiveFileDeleter(boolean deleteDirectory) {
        this.deleteDirectory = deleteDirectory;
    }

    @Override
    @Nonnull
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (deleteDirectory) {
            deleteIfExists(dir);
        }
        return super.postVisitDirectory(dir, exc);
    }

    @Override
    @Nonnull
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!attrs.isDirectory() || deleteDirectory) {
            deleteIfExists(file);
        }
        return super.visitFile(file, attrs);
    }
}

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
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * Non Thread Safety.
 *
 * @author enlo
 */
public class RecursiveFileCopier extends SimpleFileVisitor<Path> {

    final Deque<Path> depth;
    final Path sourcePath;
    final Path destPath;
    final CopyOption[] options;

    /**
     * @param sourcePath
     * @param destPath
     * @param options
     */
    public RecursiveFileCopier(@NonNull Path sourcePath, @NonNull Path destPath, CopyOption... options) {
        this.sourcePath = sourcePath;
        this.destPath = destPath;
        this.depth = new ArrayDeque<>();
        this.depth.push(destPath);
        this.options = options;
    }

    @Override
    @Nonnull
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (!dir.equals(sourcePath)) {
            this.depth.pop();
        }
        return super.postVisitDirectory(dir, exc);
    }

    @Override
    @Nonnull
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (!dir.equals(sourcePath)) {
            Path current = this.depth.peekFirst();
            Path next = current.resolve(dir.getFileName());
            createDirectories(next);
            this.depth.push(next);
        }
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path current = this.depth.peekFirst();
        Path path = current.resolve(file.getFileName());
        copy(file, path, options);
        return super.visitFile(file, attrs);
    }

}

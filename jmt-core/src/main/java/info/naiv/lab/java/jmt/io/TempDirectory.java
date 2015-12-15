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

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Misc.isBlank;
import static info.naiv.lab.java.jmt.io.NIOUtils.copyAll;
import static info.naiv.lab.java.jmt.io.NIOUtils.deleteRecursive;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.*;
import static java.lang.Runtime.getRuntime;
import java.nio.file.*;
import static java.nio.file.Files.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
@EqualsAndHashCode
@ToString
public abstract class TempDirectory implements Closeable {


    public static final String JMT_PREFIX = "jmt";

    @ReturnNonNull
    protected static String checkPrefix(String prefix) {
        if (isBlank(prefix)) {
            return JMT_PREFIX;
        }
        else {
            return prefix;
        }
    }

    protected final Logger logger = getLogger(getClass());
    final Thread shutdownHandler;
    final Path tempRoot;

    /**
     * コンストラクター
     *
     * @param tempRoot
     * @throws IOException
     */
    protected TempDirectory(Path tempRoot) throws IOException {
        nonNull(tempRoot, "tempRoot");
        this.tempRoot = tempRoot;
        this.shutdownHandler = new Thread(new CloseOnShutdown());
        getRuntime().addShutdownHook(shutdownHandler);
    }

    @ReturnNonNull
    public Path add(String prefix, Resource resource) throws IOException {
        nonNull(resource, "resource");
        Path dir = resolvePrefix(prefix);
        Path dst = dir.resolve(resource.getFilename());
        try (final InputStream is = resource.getInputStream()) {
            copy(is, dst);
        }
        return dst;
    }

    @ReturnNonNull
    public Path add(String prefix, File file) throws IOException {
        return add(prefix, file.toPath());
    }

    @ReturnNonNull
    public Path add(String prefix, Path file) throws IOException {
        Path dir = resolvePrefix(prefix);
        Path dst = dir.resolve(file.getFileName());
        if (isDirectory(file)) {
            copyAll(file, dst);
        }
        else {
            copy(file, dst);
        }
        return dst;
    }

    @ReturnNonNull
    public Path add(String prefix, InputStream is) throws IOException {
        Path dir = resolvePrefix(prefix);
        Path dst = createTempFile(dir, null, null);
        copy(is, dst);
        return dst;
    }

    @Override
    public void close() throws IOException {
        if (!exists(tempRoot)) {
            return;
        }
        deleteRecursive(tempRoot, true);
        getRuntime().removeShutdownHook(shutdownHandler);
    }

    /**
     * パスの取得.
     *
     * @return
     */
    @ReturnNonNull
    public Path getPath() {
        return tempRoot;
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        try {
            close();
        }
        finally {
            super.finalize();
        }
    }

    @ReturnNonNull
    private Path resolvePrefix(String prefix) throws IOException {
        if (prefix != null) {
            Path dir = tempRoot.resolve(prefix);
            if (!exists(dir)) {
                createDirectory(dir);
            }
            return dir;
        }
        else {
            return tempRoot;
        }
    }

    private class CloseOnShutdown implements Runnable {

        @Override
        public void run() {
            try {
                close();
            }
            catch (IOException ex) {
                logger.warn(ex.getMessage());
            }
        }
    }
}

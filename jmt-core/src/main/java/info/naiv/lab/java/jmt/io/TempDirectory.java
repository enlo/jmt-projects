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
import info.naiv.lab.java.jmt.FinalizerGuardian;
import static info.naiv.lab.java.jmt.Misc.isBlank;
import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import static info.naiv.lab.java.jmt.io.NIOUtils.copyAll;
import static info.naiv.lab.java.jmt.io.NIOUtils.deleteRecursive;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.*;
import static java.lang.Runtime.getRuntime;
import java.nio.file.*;
import static java.nio.file.Files.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
@ToString(of = "tempRoot")
@Slf4j
public abstract class TempDirectory implements Closeable {

    public static final String JMT_PREFIX = "jmt";

    private static final ConcurrentMap<Path, Path> paths = new ConcurrentHashMap<>();

    @ReturnNonNull
    protected static String checkPrefix(String prefix) {
        if (isBlank(prefix)) {
            return JMT_PREFIX;
        }
        else {
            return prefix;
        }
    }
    final AtomicBoolean closed;
    final FinalizerGuardian guardian;

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
        Path prev = paths.putIfAbsent(tempRoot, tempRoot);
        if (prev != null) {
            throw new IOException(tempRoot + " is Used.");
        }

        this.tempRoot = tempRoot;
        this.shutdownHandler = new Thread(new CloseOnShutdown());
        getRuntime().addShutdownHook(shutdownHandler);
        guardian = new FinalizerGuardian() {
            @Override
            protected void onFinalize() throws Throwable {
                close();
            }
        };
        closed = new AtomicBoolean(false);
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
        copy(is, dst, StandardCopyOption.REPLACE_EXISTING);
        return dst;
    }

    @Override
    public void close() throws IOException {
        close(false);
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

    @ReturnNonNull
    private Path resolvePrefix(String prefix) throws IOException {
        if (isNotBlank(prefix)) {
            Path dir = getPath().resolve(prefix);
            if (!exists(dir)) {
                createDirectory(dir);
            }
            return dir;
        }
        else {
            return getPath();
        }
    }

    protected void close(boolean shutdown) throws IOException {
        if (closed.compareAndSet(false, true)) {
            Path p = getPath();
            paths.remove(p);
            if (!exists(p)) {
                return;
            }
            deleteRecursive(p, true);
            guardian.setClosed(true);
            if (!shutdown) {
                getRuntime().removeShutdownHook(shutdownHandler);
            }
        }
    }
    
    private class CloseOnShutdown implements Runnable {

        @Override
        public void run() {
            try {
                close(true);
            }
            catch (IOException ex) {
                logger.warn(ex.getMessage());
            }
        }
    }
}

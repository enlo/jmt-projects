/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 *
 * @author enlo
 */
public class FileDeleter implements Callable<Map<Path, Exception>> {

    List<Path> paths;

    public FileDeleter(Path... paths) {
        this.paths = Arrays.asList(paths);
    }

    public FileDeleter(List<Path> paths) {
        this.paths = paths;
    }

    @Override
    @SuppressWarnings("ThrowableResultIgnored")
    public Map<Path, Exception> call() {
        Map<Path, Exception> errors = new HashMap<>();
        for (Path path : paths) {
            try {
                deleteCore(errors, path);
            }
            catch (IOException ex) {
                errors.put(path, ex);
            }
        }
        return errors;
    }

    protected void deleteCore(Map<Path, Exception> errors, Path deleteTarget) throws IOException {
        IOException last = null;
        for (int i = 0; i < 3; i++) {
            try {
                Files.deleteIfExists(deleteTarget);
                return;
            }
            catch (IOException ex) {
                last = ex;                
            }
        }
        if (last != null) {
            throw last;
        }
    }
}

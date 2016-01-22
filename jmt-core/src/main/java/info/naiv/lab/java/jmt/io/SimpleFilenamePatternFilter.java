/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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

import info.naiv.lab.java.jmt.fx.StringPredicate;
import java.nio.file.Path;
import java.util.Map;
import org.springframework.core.io.Resource;

public class SimpleFilenamePatternFilter implements FilenamePatternFilter, StringPredicate {

    @Override
    public <T> boolean filter(String filename, T arg, Map<String, T> founds) {
        if (test(filename)) {
            founds.put(filename, arg);
            return true;
        }
        return false;
    }

    @Override
    public boolean filter(Path path, Map<String, ? super Path> founds) {
        return filter(path.getFileName().toString(), path, founds);
    }

    @Override
    public boolean filter(Resource resource, Map<String, ? super Resource> founds) {
        return filter(resource.getFilename(), resource, founds);
    }

    @Override
    public boolean test(String filename) {
        return true;
    }

}

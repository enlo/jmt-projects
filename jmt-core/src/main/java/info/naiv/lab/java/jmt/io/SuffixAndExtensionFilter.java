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

import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
public class SuffixAndExtensionFilter extends SimpleFilenamePatternFilter implements DirectoryStream.Filter<Path>, PathMatcher {

    private static void buildPatterns(String name,
                                      StringBuilder partsWithSuffix,
                                      StringBuilder partsNoSuffix,
                                      String suffix,
                                      String extension) {
        if (isNotBlank(name)) {
            partsWithSuffix.append("^(").append(name).append(")");
            partsNoSuffix.append("^(").append(name).append(")");
        }
        if (isNotBlank(suffix)) {
            partsWithSuffix.append("\\.").append(suffix);
        }
        if (isNotBlank(extension)) {
            partsWithSuffix.append("\\.").append(extension);
            partsNoSuffix.append("\\.").append(extension);
        }
        partsWithSuffix.append("$");
        partsNoSuffix.append("$");
    }

    private static Pattern makePattern(StringBuilder parts, boolean ignoreCase) {
        String regex = parts.toString();
        if (ignoreCase) {
            return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }
        else {
            return Pattern.compile(regex);
        }
    }
    final Pattern filenameNoSuffix;
    final Pattern filenameWithSuffix;

    /**
     *
     * @param name
     * @param suffix
     * @param extension
     * @param ignoreCase
     */
    public SuffixAndExtensionFilter(String name, String suffix, String extension, boolean ignoreCase) {
        StringBuilder partsWithSuffix = new StringBuilder();
        StringBuilder partsNoSuffix = new StringBuilder();
        buildPatterns(name, partsWithSuffix, partsNoSuffix, suffix, extension);
        filenameWithSuffix = makePattern(partsWithSuffix, ignoreCase);
        filenameNoSuffix = makePattern(partsNoSuffix, ignoreCase);
    }

    @Override
    public boolean accept(Path entry) throws IOException {
        return test(entry.getFileName().toString());
    }

    @Override
    public boolean filter(Resource resource, Map<String, ? super Resource> founds) {
        return filter(resource.getFilename(), resource, founds);
    }

    @Override
    public <T> boolean filter(String filename, T arg, Map<String, T> founds) {
        Matcher m = filenameWithSuffix.matcher(filename);
        if (m.find()) {
            founds.put(m.group(1), arg);
            return true;
        }
        m = filenameNoSuffix.matcher(filename);
        if (m.find()) {
            String key = m.group(1);
            if (!founds.containsKey(key)) {
                founds.put(key, arg);
            }
            return false;
        }
        return false;

    }

    @Override
    public boolean matches(Path path) {
        return test(path.getFileName().toString());
    }

    /**
     *
     * @param filename
     * @return
     */
    @Override
    public boolean test(String filename) {
        Matcher m = filenameWithSuffix.matcher(filename);
        if (m.find()) {
            return true;
        }
        m = filenameNoSuffix.matcher(filename);
        return m.find();
    }

}

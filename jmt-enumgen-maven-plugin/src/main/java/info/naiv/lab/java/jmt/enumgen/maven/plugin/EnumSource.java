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
package info.naiv.lab.java.jmt.enumgen.maven.plugin;

import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author enlo
 */
public class EnumSource {

    public static Multimap<String, EnumEntry> createEnumEntries(Path path, Charset charset) throws IOException {
        Splitter st = Splitter.on(Pattern.compile("\\s+")).trimResults();
        List<String> lines = Files.readAllLines(path, charset);
        Multimap<String, EnumEntry> result = HashMultimap.create();
        for (String line : lines) {
            if (isNotBlank(line)) {
                List<String> source = st.splitToList(line);
                String key = source.get(0);
                EnumEntry e = EnumEntry.forEnumSource(source.get(1),
                                                      source.get(2),
                                                      source.get(3),
                                                      source.get(4));
                result.put(key, e);
            }
        }
        return result;
    }

    private EnumSource() {
    }
}

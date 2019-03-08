/*
 * The MIT License
 *
 * Copyright 2018 enlo.
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
package info.naiv.lab.java.jmt.winini;

import static info.naiv.lab.java.jmt.Strings.isEmpty;
import static info.naiv.lab.java.jmt.Strings.trimLeft;
import static info.naiv.lab.java.jmt.Strings.trimToEmpty;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class IniParser {

    private final Pattern commentPattern;
    private final IniParserConfiguration configuration;

    public IniParser() {
        this(IniParserConfiguration.builder().build());
    }

    public IniParser(@NonNull IniParserConfiguration configuration) {
        this.configuration = configuration;
        this.commentPattern = configuration.createCommentPrefixPattern();
    }

    public Ini parse(String data) {
        String[] lines = data.split("\r\n|\r|\n");
        Ini ini = new WinIni();
        for (int ln = 0; ln < lines.length; ln++) {
            String line = trimLeft(lines[ln]);
            if (isEmpty(line)) {
                continue;
            }
            if (parseComment(ini, line)) {
                continue;
            }

            if (isEmpty(line)) {
                continue;
            }

        }
        return ini;
    }

    public boolean parseComment(Ini ini, String line) {
        Matcher cm = commentPattern.matcher(line);
        if (cm.find()) {
            String comment = trimToEmpty(cm.group(1));
            ini.add(new IniCommentImpl(comment));
            return true;
        }
        return false;
    }
}
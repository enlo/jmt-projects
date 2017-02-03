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
package info.naiv.lab.java.jmt.i18n;

import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import lombok.NonNull;

public class Locales {

    @Nonnull
    public static Locale toLocale(@NonNull String localeString) {
        Locale.Builder builder = new Locale.Builder();
        try {
            builder.setLanguageTag(localeString);
            return builder.build();
        }
        catch (IllformedLocaleException ex) {
            if (localeString.toLowerCase().equals("default")) {
                return Locale.getDefault();
            }
            // language.
            String[] parts = localeString.split("_");
            builder.setLanguage(parts[0]);
            if (1 < parts.length) {
                builder.setRegion(parts[1]);
            }
            if (2 < parts.length) {
                if (parts[2].startsWith("#")) {
                    // script and extensions.
                    parseScriptAndExtension(builder, parts[2]);
                }
                else {
                    // variant.
                    try {
                        builder.setVariant(parts[2]);
                    }
                    catch (IllformedLocaleException ex2) {
                        return new Locale(parts[0], parts[1], parts[2]);
                    }
                }
            }
            if (3 < parts.length) {
                // script and extensions.
                parseScriptAndExtension(builder, parts[3]);
            }
            return builder.build();
        }
    }

    private static void parseScriptAndExtension(@Nonnull Locale.Builder builder, @Nonnull String script) {
        if (script.startsWith("#")) {
            script = script.substring(1);
        }

        String current;
        String[] source = script.split("_", 2);
        if (source.length == 2) {
            builder.setScript(source[0]);
            current = source[1];
        }
        else {
            source = script.split("-", 2);
            if (1 < source[0].length()) {
                builder.setScript(source[0]);
                if (source.length == 1) {
                    return;
                }
                current = source[1];
            }
            else {
                current = script;
            }
        }

        Pattern p = Pattern.compile("\\b[ux]\\-");
        Matcher m = p.matcher(current);
        char key;
        int next;
        if (m.find()) {
            next = m.end();
            key = m.group().charAt(0);
            while (m.find()) {
                String val = current.substring(next, m.start() - 1);
                builder.setExtension(key, val);
                next = m.end();
                key = m.group().charAt(0);
            }
            String val = current.substring(next);
            builder.setExtension(key, val);
        }
    }

    private Locales() {
    }
}

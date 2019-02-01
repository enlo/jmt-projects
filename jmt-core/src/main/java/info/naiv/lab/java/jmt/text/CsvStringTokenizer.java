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
package info.naiv.lab.java.jmt.text;

import java.nio.CharBuffer;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class CsvStringTokenizer {

    final CharBuffer cb;
    int column;
    int index;
    int row;
    final char[] source;

    public CsvStringTokenizer(@NonNull CharSequence source) {
        this(source.toString().toCharArray());
    }

    public CsvStringTokenizer(@NonNull String source) {
        this(source.toCharArray());
    }

    public CsvStringTokenizer(@NonNull char[] source) {
        this.source = source;
        cb = CharBuffer.allocate(source.length);
        index = 0;
        row = 0;
        column = 0;
    }

    @Nonnull
    public String getColumn() {

        cb.clear();
        if (isEol()) {
            return "";
        }
        char ch = source[index];
        if (ch == '"') {
            boolean inQuote = true;
            index++;
            while (inQuote) {
                ch = source[index];
                if (ch == '"') {
                    if (peek() == '"') {
                        index += 2;
                        cb.append(ch);
                        continue;
                    }
                    else {
                        inQuote = false;
                    }
                }
                else {
                    cb.append(ch);
                }
                index++;
            }
            if (isEoc()) {
                skipEoc();
                column++;
                cb.flip();
                return cb.toString();
            }
            throw new IllegalStateException("[,] is not found.");
        }
        else {
            while (!isEoc()) {
                cb.append(source[index]);
                index++;
            }
            skipEoc();
            column++;
            cb.flip();
            return cb.toString();
        }
    }

    @Nonnull
    public String getRemaining() {
        if (check()) {
            return new String(source, index, source.length - index);
        }
        else {
            return "";
        }
    }

    public boolean nextLine() {
        if (check()) {
            while (!isEol()) {
                skipColumn();
            }
            if (check()) {
                skipEol();
                return true;
            }
        }
        return false;
    }

    public boolean skipColumn() {

        if (isEol()) {
            return false;
        }
        char ch = source[index];
        if (ch == '"') {
            boolean inQuote = true;
            index++;
            while (inQuote) {
                if (source[index] == '"') {
                    if (peek() == '"') {
                        index += 2;
                        continue;
                    }
                    else {
                        inQuote = false;
                    }
                }
                index++;
            }
            if (isEoc()) {
                skipEoc();
                column++;
                return true;
            }
            throw new IllegalStateException("[,] is not found.");
        }
        else {
            if (isEol()) {
                return false;
            }
            while (!isEoc()) {
                index++;
            }
            skipEoc();
            column++;
            return true;
        }
    }

    private boolean check() {
        return index < source.length;
    }

    private boolean isEoc() {
        if (isEol()) {
            return true;
        }
        return source[index] == ',';
    }

    private boolean isEol() {
        if (check()) {
            char ch = source[index];
            return (ch == 0x0A || ch == 0x0D);
        }
        else {
            return true;
        }
    }

    private char peek() {
        if ((index + 1) < source.length) {
            return source[index + 1];
        }
        return 0;
    }

    private void skipEoc() {
        if (check() && source[index] == ',') {
            index++;
        }
    }

    private void skipEol() {
        if (check()) {
            char ch = source[index];
            if (ch == 0x0D) {
                if (peek() == 0x0A) {
                    index += 2;
                }
                else {
                    index++;
                }
            }
            else if (ch == 0x0A || ch == 0x2028 || ch == 0x2029) {
                index++;
            }
        }
    }

}

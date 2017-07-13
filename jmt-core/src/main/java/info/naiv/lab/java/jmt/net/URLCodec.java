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
package info.naiv.lab.java.jmt.net;

import java.io.ByteArrayOutputStream;
import static java.lang.Character.forDigit;
import static java.lang.Character.toUpperCase;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static java.util.Arrays.sort;

/**
 * URL Encode / Decode
 *
 * @author enlo
 */
public class URLCodec {

    public static final byte ESCAPE_CHAR = '%';
    public static final char[] WWW_FORM_URL;
    static final int RADIX = 16;

    static {
        char[] special = "-_.* 1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                .toCharArray();
        sort(special);
        WWW_FORM_URL = special;
    }

    /**
     * URLデコード.
     *
     * @param data
     * @param charset
     * @return
     */
    public static String decode(String data, Charset charset) {
        return decode(data.getBytes(StandardCharsets.US_ASCII), charset);
    }

    /**
     * URLデコード.
     *
     * @param data
     * @param charset
     * @return
     */
    public static String decode(byte[] data, Charset charset) {
        return new String(decode(data), charset);
    }

    /**
     * URLデコード.
     *
     * @param data
     * @return
     */
    public static byte[] decode(byte[] data) {
        if (data == null) {
            return null;
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < data.length; i++) {
            final int b = data[i];
            switch (b) {
                case '+':
                    baos.write(' ');
                    break;
                case ESCAPE_CHAR:
                    final int u = hexDigitToInt(data[++i]);
                    final int l = hexDigitToInt(data[++i]);
                    baos.write((char) ((u << 4) + l));
                    break;
                default:
                    baos.write(b);
                    break;
            }
        }
        return baos.toByteArray();
    }

    /**
     * URLエンコード.
     *
     * @param data
     * @param charset
     * @return
     */
    public static String encode(String data, Charset charset) {
        if (data == null) {
            return null;
        }
        byte[] bytes = data.getBytes(charset);
        return new String(encode(bytes), StandardCharsets.US_ASCII);
    }

    /**
     * URLエンコード.
     *
     * @param data
     * @return
     */
    public static byte[] encode(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream((data.length * 3) / 2);
        for (byte c : data) {
            char b = (char) (c & 0xFF);
            if (nonEscape(b)) {
                if (b == ' ') {
                    b = '+';
                }
                baos.write(b);
            }
            else {
                baos.write(ESCAPE_CHAR);
                char hex1 = toUpperCase(forDigit((b >> 4) & 0xF, RADIX));
                char hex2 = toUpperCase(forDigit(b & 0xF, RADIX));
                baos.write(hex1);
                baos.write(hex2);
            }
        }
        return baos.toByteArray();
    }

    /**
     * URLエンコード必須かどうかチェック.
     *
     * @param data
     * @return
     */
    public static boolean isMustEncode(String data) {
        for (char ch : data.toCharArray()) {
            if (!nonEscape(ch)) {
                return true;
            }
        }
        return false;
    }

    private static int hexDigitToInt(byte b) {
        int cp = Character.digit((char) (b & 0xFF), URLCodec.RADIX);
        if (cp == -1) {
            throw new IllegalArgumentException("Invalid URL decoding: " + b);
        }
        return cp;
    }

    static boolean nonEscape(char ch) {
        return (('a' <= ch && ch <= 'z')
                || ('A' <= ch && ch <= 'Z')
                || ('0' <= ch && ch <= '9')
                || (ch == '-' || ch == '_' || ch == '.' || ch == '*' || ch == ' '));
    }

    private URLCodec() {
    }
}

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
package info.naiv.lab.java.jmt.net.http;

import static info.naiv.lab.java.jmt.Misc.isEmpty;
import info.naiv.lab.java.jmt.collection.KeyedValueCollection;
import info.naiv.lab.java.jmt.net.http.MultipartContent.Part;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author enlo
 */
@Setter
@Accessors(fluent = true)
public class MultipartContentBuilder {

    private static final int MAX_BOUNDARY_LEN = 36;

    private static final int MIN_BOUNDARY_LEN = 31;
    private static final char[] MULTIPART_CHARS
            = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();

    public static String generateBoundary() {
        StringBuilder sb = new StringBuilder("JMT_");
        int count = ThreadLocalRandom.current().nextInt(MIN_BOUNDARY_LEN, MAX_BOUNDARY_LEN + 1);
        for (int i = 0; i < count; i++) {
            int ch = ThreadLocalRandom.current().nextInt(MULTIPART_CHARS.length);
            sb.append(MULTIPART_CHARS[ch]);
        }
        return sb.toString();
    }

    @Getter
    @Setter
    private Charset charset;

    private final KeyedValueCollection<String, Part> values;

    public MultipartContentBuilder() {
        charset = StandardCharsets.UTF_8;
        values = new KeyedValueCollection<>();
    }

    public MultipartContentBuilder addBinaryPart(String name, String filename, String contentType, byte[] value) {
        values.add(new BinaryPart(name, filename, contentType, value));
        return this;
    }

    public MultipartContentBuilder addTextPart(String name, String value) {
        values.add(new TextPart(name, value));
        return this;
    }

    public MultipartContent build() {
        String boundary = generateBoundary();
        return new MultipartContent(boundary, values);
    }

    protected static class BinaryPart extends Part {

        private static final long serialVersionUID = -7556716821793559269L;

        private final byte[] data;

        protected BinaryPart(String name, String filename, String contentType, byte[] data) {
            super(name);
            this.data = data;
            if (isEmpty(contentType)) {
                contentType = "application/octet-stream";
            }
            headers.setContentType(contentType);
            setContentDisposition().parameter("filename", filename).update();
            headers.setContentTransferEncoding(ContentTransferEncodingType.BINARY);
            headers.setContentLength(data.length);
        }

        @Override
        public void writeTo(OutputStream out, Charset charset) throws IOException {
            this.headers.writeTo(out, charset);
            out.write(CRLF);
            out.write(data);
            out.write(CRLF);
        }

    }

    protected static class TextPart extends Part {

        private static final long serialVersionUID = -7718320238009752961L;
        final String value;

        protected TextPart(String name, String value) {
            super(name);
            this.value = value;
            setContentDisposition();
        }

        @Override
        public void writeTo(OutputStream out, Charset charset) throws IOException {
            this.headers.writeTo(out, charset);
            out.write(CRLF);
            out.write(value.getBytes(charset));
            out.write(CRLF);
        }
    }
}

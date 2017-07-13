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

import info.naiv.lab.java.jmt.collection.KeyedValue;
import info.naiv.lab.java.jmt.collection.KeyedValueCollection;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 *
 * @author enlo
 */
public class MultipartContent extends HttpContent {

    private static final long serialVersionUID = 445335545190871184L;

    protected final String boundary;

    protected final KeyedValueCollection<String, Part> parts;

    public MultipartContent(String boundary, Collection<Part> parts) {
        this.boundary = boundary;
        this.parts = new KeyedValueCollection(parts);
    }

    @Override
    public void writeTo(OutputStream out, Charset charset) throws IOException {

        headers.setContentType("multipart/form-data")
                .parameter("boundary", boundary)
                .parameter("charset", charset.name()).update();

        byte[] bb = ("--" + boundary).getBytes(charset);
        headers.writeTo(out, charset);
        out.write(CRLF);
        for (HttpContent part : parts) {
            out.write(bb);
            out.write(CRLF);
            part.writeTo(out, charset);
        }
        out.write(bb);
        out.write(TDASH);
        out.flush();
    }

    public abstract static class Part extends HttpContent implements KeyedValue<String> {

        private static final long serialVersionUID = 1236952542583974762L;

        protected final String name;

        protected Part(String name) {
            this.name = name;
        }

        @Override
        public String getKey() {
            return name;
        }

        protected final HttpHeaderBuilder setContentDisposition() {
            return this.headers.setContentDisposition(ContentDispositionType.FORMDATA, name);
        }

    }
}

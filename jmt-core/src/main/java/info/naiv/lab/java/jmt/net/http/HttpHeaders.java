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

import info.naiv.lab.java.jmt.collection.KeyedValueCollection;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 *
 * @author enlo
 */
public class HttpHeaders extends KeyedValueCollection<String, HttpHeader> implements StreamWritable {

    private static final long serialVersionUID = -4959376077250298929L;

    public HttpHeaderBuilder setBuilder(HttpHeaderBuilder hb) {
        hb.headers(this);
        hb.build();
        return hb;
    }

    public HttpHeaderBuilder setContentDisposition(ContentDispositionType contentType, String name) {
        return setContentDisposition(contentType, name, null);
    }

    public HttpHeaderBuilder setContentDisposition(ContentDispositionType contentType, String name, String filename) {
        return setBuilder(HttpHeaderBuilder.contentDisposition(contentType)
                .parameter("name", name).parameter("filename", filename));
    }

    public HttpHeaderBuilder setContentLength(long length) {
        return setHeader("Content-Length", Long.toString(length));
    }

    public HttpHeaderBuilder setContentTransferEncoding(ContentTransferEncodingType type) {
        return setHeader("Content-Transfer-Encoding", type.getKey());
    }

    public HttpHeaderBuilder setContentType(String contentType) {
        return setBuilder(HttpHeaderBuilder.contentType(contentType));
    }

    public HttpHeaderBuilder setContentType(String contentType, Charset charset) {
        return setBuilder(HttpHeaderBuilder.contentType(contentType, charset));
    }

    public HttpHeaderBuilder setHeader(String headerName, String value) {
        return setBuilder(new HttpHeaderBuilder(headerName).headerValue(headerName));
    }

    @Override
    public void writeTo(OutputStream out, Charset charset) throws IOException {
        for (HttpHeader h : this) {
            h.writeTo(out, charset);
        }
        out.flush();
    }
}

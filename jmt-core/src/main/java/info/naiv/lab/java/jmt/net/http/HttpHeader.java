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
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import lombok.Value;

/**
 *
 * @author enlo
 */
@Value
public class HttpHeader implements KeyedValue<String>, StreamWritable, Serializable {

    private static final long serialVersionUID = 2184955581448048474L;

    String name;
    String value;

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(name).append(": ").append(value).toString();
    }

    @Override
    public void writeTo(OutputStream out, Charset charset) throws IOException {
        out.write(name.getBytes(charset));
        out.write(HDELIM);
        out.write(value.getBytes(charset));
        out.write(CRLF);
    }

}

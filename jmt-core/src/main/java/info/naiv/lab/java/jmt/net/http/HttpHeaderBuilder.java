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

import info.naiv.lab.java.jmt.net.URLCodec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author enlo
 */
@Setter
@Accessors(fluent = true)
public class HttpHeaderBuilder {

    public static HttpHeaderBuilder contentDisposition() {
        return new HttpHeaderBuilder("Content-Disposition");
    }

    public static HttpHeaderBuilder contentDisposition(@Nonnull ContentDispositionType type) {
        return contentDisposition().headerValue(type.getKey());
    }

    public static HttpHeaderBuilder contentType() {
        return new HttpHeaderBuilder("Content-Disposition");
    }

    public static HttpHeaderBuilder contentType(@Nonnull String contentType) {
        return contentType().headerValue(contentType);
    }

    public static HttpHeaderBuilder contentType(@Nonnull String contentType, @Nonnull Charset charset) {
        return contentType(contentType).parameter("charset", charset.name());
    }

    private boolean alwaysNoEncode = true;
    private Charset charset = StandardCharsets.UTF_8;
    private Charset forOldInternetExplorerCharset = Charset.defaultCharset();
    private Locale locale = null;
    private boolean oldInterExplorer = false;
    private boolean quote = true;

    protected final String headerName;
    protected String headerValue;

    protected HttpHeaders headers;
    protected final Map<String, String> parameters = new HashMap<>();

    public HttpHeaderBuilder(String headerName) {
        this.headerName = headerName;
    }

    public HttpHeader build() {
        HttpHeader header = new HttpHeader(headerName, buildValue());
        if (headers != null) {
            headers.put(header);
        }
        return header;
    }

    public String buildValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(headerValue);
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            sb.append(";");
            buildEntry(param, sb);
        }
        return sb.toString();
    }

    public HttpHeaderBuilder parameter(@Nonnull String name, String value) {
        if (value != null) {
            String current = parameters.get(name);
            if (current != null) {
                current = value;
            }
            else {
                current += value;
            }
            parameters.put(name, current);
        }
        return this;
    }

    public HttpHeaderBuilder parameters(@Nonnull Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            parameter(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public HttpHeader update() {
        return headers.put(new HttpHeader(headerName, buildValue()));
    }

    protected void buildEntry(Map.Entry<String, String> entry, StringBuilder sb) {

        String key = entry.getKey();
        String value = entry.getValue();
        boolean mustEncode = !alwaysNoEncode && URLCodec.isMustEncode(value);

        sb.append(entry.getKey()).append("=");
        if (quote) {
            sb.append('"');
        }
        if (mustEncode && oldInterExplorer) {
            String encoded = URLCodec.encode(value, forOldInternetExplorerCharset);
            sb.append(encoded);
            mustEncode = false;
        }
        else {
            sb.append(entry.getValue());
        }
        if (quote) {
            sb.append('"');
        }
        if (mustEncode) {
            sb.append(";").append(key).append("*=").append(charset.name());
            sb.append("'");
            if (locale != null) {
                sb.append(locale.getLanguage());
            }
            sb.append("'").append(URLCodec.encode(value, charset));
        }
    }

}

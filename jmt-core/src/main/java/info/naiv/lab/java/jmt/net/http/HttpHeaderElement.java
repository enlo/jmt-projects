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

import info.naiv.lab.java.jmt.KeyValuePair;
import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.net.URLCodec;
import info.naiv.lab.java.jmt.tuple.Tuple4;
import info.naiv.lab.java.jmt.tuple.Tuples;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author enlo
 */
@Data
@NoArgsConstructor
public class HttpHeaderElement implements Serializable {

    private static final long serialVersionUID = -8000680752938459256L;

    public static HttpHeaderElement parse(String value) {
        KeyValuePair<String, String> hnv = Misc.splitKeyValue(value, ":", true);
        if (hnv == null) {
            return new HttpHeaderElement(value.trim(), "");
        }
        else {
            HttpHeaderElement result = new HttpHeaderElement(hnv.getKey(), hnv.getValue());
            List<Tuple4<String, Boolean, Integer, String>> kvpl = splitParameters(hnv, result);
            Map<String, String> params = new HashMap<>(kvpl.size());
            Map<String, String> paramsEnc = new HashMap<>(kvpl.size());
            segmentParamters(kvpl, paramsEnc, params);
            decodeValues(paramsEnc, params);
            result.parameters.putAll(params);
            return result;
        }
    }

    private static void decodeValues(Map<String, String> paramsEnc, Map<String, String> params) {
        for (Map.Entry<String, String> enc : paramsEnc.entrySet()) {
            String ev = enc.getValue();
            int fq = ev.indexOf('\'');
            int sq = ev.indexOf('\'', fq + 1);
            String cs = ev.substring(0, fq);
            // Localeは無視.
            //String lc = encValue.substring(fq + 1, sq);
            String src = ev.substring(sq + 1);
            params.put(enc.getKey(), URLCodec.decode(src, Charset.forName(cs)));
        }
    }

    private static void segmentParamters(List<Tuple4<String, Boolean, Integer, String>> kvpl, Map<String, String> paramsEnc, Map<String, String> params) {
        for (Tuple4<String, Boolean, Integer, String> kvp : kvpl) {
            Map<String, String> r = kvp.getValue2() ? paramsEnc : params;
            int idx = kvp.getValue3();
            if (idx < 0) {
                r.put(kvp.getValue1(), kvp.getValue4());
            }
            else {
                if (idx == 0) {
                    r.put(kvp.getValue1(), kvp.getValue4());
                }
                else {
                    String k = kvp.getValue1();
                    String c = Objects.toString(r.get(k), "");
                    r.put(k, c + kvp.getValue4());
                }
            }
        }
    }

    private static List<Tuple4<String, Boolean, Integer, String>> splitParameters(KeyValuePair<String, String> hnv, HttpHeaderElement result) {
        String[] values = hnv.getValue().split(";");
        List<Tuple4<String, Boolean, Integer, String>> kvpl = new ArrayList<>();
        for (String pair : values) {
            KeyValuePair<String, String> kvp = Misc.splitKeyValue(pair, "=", true);
            if (kvp == null) {
                result.headerValue = pair.trim();
            }
            else {
                String k = kvp.getKey();
                String v = kvp.getValue().replaceAll("\\\"", "");
                boolean encoded = k.endsWith("*");
                if (!encoded) {
                    v = URLCodec.decode(v, Charset.defaultCharset());
                }
                String[] keyPart = k.split("\\*");
                Tuple4<String, Boolean, Integer, String> t;
                if (keyPart.length == 1) {
                    t = Tuples.tie(keyPart[0], encoded, -1, v);
                }
                else {
                    int i = Integer.parseInt(keyPart[1]);
                    t = Tuples.tie(keyPart[0], encoded, i, v);
                }
                kvpl.add(t);
            }
        }
        Collections.sort(kvpl, Tuples.<String, Boolean, Integer>getTuple3Comparator());
        return kvpl;
    }

    protected String headerName;
    protected String headerValue;
    protected final Map<String, String> parameters = new HashMap<>();

    public HttpHeaderElement(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    public HttpHeader toHttpHeader() {
        return toHttpHeaderBuilder().build();
    }

    public HttpHeaderBuilder toHttpHeaderBuilder() {
        return new HttpHeaderBuilder(headerName)
                .headerValue(headerValue)
                .parameters(parameters);
    }

}

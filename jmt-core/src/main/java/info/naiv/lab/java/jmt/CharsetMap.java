/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.nio.charset.Charset;
import static java.nio.charset.Charset.defaultCharset;
import static java.nio.charset.Charset.forName;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 文字セットマップ. <br> {@link Charset#forName} が十分に高速なので使用する意味はない.
 *
 * @see <a href="https://ideone.com/K4lmu3">サンプルコード</a>
 *
 * @author enlo
 */
public final class CharsetMap extends SingletonMapSupport<String, Charset> {

    private static final CharsetMap instance;

    static {
        instance = new CharsetMap();
    }

    /**
     * {@link Charset}を戻す. <br>
     * name が null の場合、{@link Charset#defaultCharset()} を戻す.
     *
     * @param name 文字コード名.
     * @return Charset オブジェクト.
     */
    @ReturnNonNull
    public static Charset get(String name) {
        if (name == null) {
            return defaultCharset();
        }
        return instance.internalGet(name);
    }

    private CharsetMap() {
        super(new ConcurrentSkipListMap<String, Charset>(String.CASE_INSENSITIVE_ORDER));
    }

    private boolean check(Charset cs, String name) {
        return cs.name().equals(name) || cs.aliases().contains(name);
    }

    @Override
    @ReturnNonNull
    protected Charset newValue(String key) {
        if (check(UTF_8, key)) {
            return UTF_8;
        } else if (check(ISO_8859_1, key)) {
            return ISO_8859_1;
        } else if (check(US_ASCII, key)) {
            return US_ASCII;
        }
        return forName(key);
    }
}

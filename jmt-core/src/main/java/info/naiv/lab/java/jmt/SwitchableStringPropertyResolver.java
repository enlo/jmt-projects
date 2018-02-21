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
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.closeable.AutoCloseableSupport;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.CheckReturnValue;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class SwitchableStringPropertyResolver
        implements StringPropertyResolver {

    private static final AtomicReference<StringPropertyResolver> DEFAULT = new AtomicReference<>();
    private static final ConcurrentMap<String, StringPropertyResolver> RESOLVERS = new ConcurrentHashMap<>();

    private static final ThreadLocalStack<AtomicReference<StringPropertyResolver>> TLS = new ThreadLocalStack<>();

    static {
        DEFAULT.set(StringPropertyResolvers.EMPTY);
        TLS.push(DEFAULT);
    }

    public static StringPropertyResolver deregister(String name) {
        return RESOLVERS.remove(name);
    }

    public static StringPropertyResolver getResolver() {
        return TLS.getTop().get();
    }

    /**
     * StringPropertyResover を登録する. <br>
     * すでに同名の項目が存在する場合は登録せず、既存の Resolver をもどす.
     *
     * @param name
     * @param resolver
     *
     * @see ConcurrentMap#putIfAbsent(java.lang.Object, java.lang.Object)
     *
     * @return
     */
    public static StringPropertyResolver register(String name, StringPropertyResolver resolver) {
        return RESOLVERS.putIfAbsent(name, resolver);
    }

    public static void setDefault(@NonNull StringPropertyResolver defaultResolver) {
        DEFAULT.set(defaultResolver);
    }

    @CheckReturnValue
    public static Context switchResolver(String name) {
        return new Context(RESOLVERS.get(name));
    }

    @Override
    public boolean containsProperty(String key) {
        return getResolver().containsProperty(key);
    }

    @Override
    public String getProperty(String key) {
        return getResolver().getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return getResolver().getProperty(key, defaultValue);
    }

    public static class Context extends AutoCloseableSupport {

        public Context(StringPropertyResolver resolver) {
            TLS.push(new AtomicReference<>(resolver));
        }

        @Override
        public void close() {
            TLS.pop();
            super.suppressFinalize();
        }

    }
}

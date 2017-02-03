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

import info.naiv.lab.java.jmt.runtime.Classes;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

/**
 *
 * @author enlo
 */
public class SimpleBeanCopierFactory {

    private static final ConcurrentMap<Key, SimpleBeanCopier> CACHE = new ConcurrentHashMap<>();

    @Getter
    @Setter
    @NonNull
    private static GenericConversionService defaultConversionService = new DefaultConversionService();

    public static SimpleBeanCopier createInstance(@NonNull Class<?> srcType, @NonNull Class<?> dstType, GenericConversionService conversionService, String... ignoreProperties) {
        if (Classes.isProxyClass(srcType) || Classes.isProxyClass(dstType)) {
            // Proxy の場合、一時的なものである可能性が高いので、いちいちキャッシュしない.
            SimpleBeanCopier newInst = new SimpleBeanCopier(srcType, dstType, conversionService, ignoreProperties);
            return newInst;
        }
        Key key = new Key(srcType, dstType, conversionService, ignoreProperties);
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }
        else {
            SimpleBeanCopier newInst = new SimpleBeanCopier(srcType, dstType, conversionService, ignoreProperties);
            CACHE.put(key, newInst);
            return newInst;
        }
    }

    public static SimpleBeanCopier createInstance(@NonNull Class<?> srcType, @NonNull Class<?> dstType, String... ignoreProperties) {
        return createInstance(srcType, dstType, defaultConversionService, ignoreProperties);
    }

    public static <S, T> void registerConverter(Converter<S, T> converter) {
        defaultConversionService.addConverter(converter);
    }

    private SimpleBeanCopierFactory() {
    }

    @Value
    static final class Key {

        private final Class<?> clsDst;
        private final Class<?> clsSrc;
        private final GenericConversionService conversionService;
        private final String[] ignoreProperties;

        Key(Class<?> srcType, Class<?> dstType, GenericConversionService conversionService, String[] ignoreProperties) {
            this.clsSrc = srcType;
            this.clsDst = dstType;
            this.conversionService = conversionService;
            this.ignoreProperties = ignoreProperties;
        }
    }
}

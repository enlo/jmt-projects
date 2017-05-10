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
package info.naiv.lab.java.jmt.runtime;

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayContains;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;

/**
 *
 * @author enlo
 * @param <TDest>
 * @param <TSource>
 * @param <TKey>
 */
public abstract class AbstractBeanWriter<TDest, TSource, TKey> {

    protected final GenericConversionService conversionService;
    protected final Map<String, PropertyEntry> properties;
    protected final Class<?> dstType;
    protected final PropertyDescriptor[] dstPds;

    public AbstractBeanWriter(@NonNull Class<TDest> dstType, GenericConversionService conversionService) {
        this.dstType = dstType;
        this.dstPds = BeanUtils.getPropertyDescriptors(dstType);
        this.properties = new HashMap<>(dstPds.length);
        this.conversionService = conversionService;
    }

    protected abstract Object getSourceValue(TSource source, TKey key);

    protected abstract TypeDescriptor getSourceType(TKey key, PropertyDescriptor dstProp);

    protected abstract String getSourceName(TKey key);

    protected abstract TKey createSourceKey(PropertyDescriptor dstPd);

    protected void init(String... ignoreProperties) {
        properties.clear();
        for (PropertyDescriptor dstPd : dstPds) {
            Method dstMethod = dstPd.getWriteMethod();
            String key = dstPd.getName();
            if (dstMethod != null && !arrayContains(ignoreProperties, key)) {
                TKey srcKey = createSourceKey(dstPd);
                if (srcKey != null) {
                    PropertyEntry pe = new PropertyEntry(srcKey, dstPd, dstMethod);
                    properties.put(key, pe);
                }
            }
        }
    }

    public TDest write(@NonNull TDest dest, @NonNull TSource source) {
        if (this.conversionService != null) {
            for (PropertyEntry pe : properties.values()) {
                pe.copy(source, dest, conversionService);
            }
        }
        else {
            for (PropertyEntry pe : properties.values()) {
                pe.copy(source, dest);
            }
        }
        return dest;
    }

    public TDest createFrom(TSource source) {
        try {
            TDest dest = (TDest) dstType.newInstance();
            return write(dest, source);
        }
        catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Data
    protected class PropertyEntry {

        TypeDescriptor dstType;
        Method dstWrite;
        boolean noConversion;
        TypeDescriptor srcType;
        TKey srcKey;

        PropertyEntry(TKey srcKey, PropertyDescriptor dstProp, Method dstWrite) {
            this.srcKey = srcKey;
            this.dstWrite = dstWrite;
            if (!Modifier.isPublic(this.dstWrite.getDeclaringClass().getModifiers())) {
                this.dstWrite.setAccessible(true);
            }
            this.srcType = getSourceType(srcKey, dstProp);
            this.dstType = TypeDescriptor.valueOf(dstProp.getPropertyType());
            this.noConversion = srcType == null || dstType.equals(srcType);
        }

        public void copy(TSource src, Object dst) {
            try {
                Object val = getSourceValue(src, srcKey);
                dstWrite.invoke(dst, val);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                rethrow(ex);
            }
        }

        public void copy(TSource src, Object dst, GenericConversionService conversionService) {
            try {
                Object val = getSourceValue(src, srcKey);
                if (val != null && !noConversion) {
                    if (srcType == null) {
                        val = conversionService.convert(val, dstType);
                    }
                    else {
                        val = conversionService.convert(val, srcType, dstType);
                    }
                }
                dstWrite.invoke(dst, val);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                rethrow(ex);
            }
        }

        public void rethrow(final Exception ex) throws FatalBeanException {
            String src = getSourceName(srcKey);
            String dst = dstWrite.getName();
            String message = String.format("Could not copy properties from source to target. [dst.%s(src.%s())]", dst, src);
            throw new FatalBeanException(message, ex);
        }
    }

}

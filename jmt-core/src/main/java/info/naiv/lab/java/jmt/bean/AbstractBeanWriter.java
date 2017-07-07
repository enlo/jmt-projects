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
package info.naiv.lab.java.jmt.bean;

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayContains;
import info.naiv.lab.java.jmt.exception.UncheckedException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 *
 * @author enlo
 * @param <TSource>
 * @param <TDest>
 * @param <TKey>
 */
public abstract class AbstractBeanWriter<TSource, TDest, TKey> implements BeanWriter<TSource, TDest> {

    private final List<BeanWritingErrorHandler> errorHandlers = new ArrayList<>();

    protected final GenericConversionService conversionService;
    protected final Map<String, Entry<TKey>> destEntries = new HashMap<>();
    protected final Class<?> destType;

    public AbstractBeanWriter(@NonNull Class<TDest> destType,
                              GenericConversionService conversionService) {
        this.destType = destType;
        this.conversionService = conversionService;
    }

    @Override
    public void addErrorHandler(@NonNull BeanWritingErrorHandler errorHandler) {
        errorHandlers.add(errorHandler);
    }

    @Override
    public TDest createFrom(TSource source) {
        try {
            TDest dest = (TDest) destType.newInstance();
            return write(source, dest);
        }
        catch (InstantiationException | IllegalAccessException ex) {
            throw UncheckedException.toUnchecked(ex);
        }
    }

    @Override
    public void removeErrorHandler(BeanWritingErrorHandler errorHandler) {
        if (errorHandler != null) {
            errorHandlers.remove(errorHandler);
        }
    }

    @Override
    public TDest write(@NonNull TSource source, @NonNull TDest dest) {
        SourceValueResolver<TKey> resolver = getResolver(source);
        if (isIgnoreSource(source)) {
            return handleIgnoreSource(source, dest);
        }
        for (Entry<TKey> e : destEntries.values()) {
            TypeDescriptor td = e.destTypeDescriptor;
            Object dv;
            Object sv = null;
            try {
                if (!resolver.containsKey(e.key)) {
                    handleEntryNotFound(source, e.key);
                    dv = resolver.getDefaultValue(e.key, td);
                }
                else {
                    TypeDescriptor sd = resolver.getValueTypeDescriptor(e.key);
                    sv = resolver.getValue(e.key);
                    if (sv != null) {
                        if (sd == null) {
                            dv = conversionService.convert(sv, td);
                        }
                        else {
                            dv = conversionService.convert(sv, sd, td);
                        }
                    }
                    else {
                        dv = resolver.getDefaultValue(e.key, td);
                    }
                }
                validate(e.key, td, dv);
                e.setValue(dest, dv);
            }
            catch (ReflectiveOperationException | RuntimeException ex) {
                if (!handleException(e.getMember(), e.destTypeDescriptor, sv, ex)) {
                    throw UncheckedException.toUnchecked(ex);
                }
            }
        }
        return dest;
    }

    /**
     *
     * プロパティからキーを生成
     *
     * @param pd
     * @param type
     * @return
     */
    @CheckForNull
    protected abstract TKey createKey(PropertyDescriptor pd, TypeDescriptor type);

    /**
     * フィールドからキーを生成
     *
     * @param field
     * @param type
     * @return
     */
    @CheckForNull
    protected abstract TKey createKey(Field field, TypeDescriptor type);

    /**
     * ソースから値を取得するためのリゾルバを取得.
     *
     * @param source
     * @return
     */
    protected abstract SourceValueResolver<TKey> getResolver(TSource source);

    /**
     * ソースにキーが存在しない場合.
     *
     * @param source
     * @param key
     */
    protected void handleEntryNotFound(TSource source, TKey key) {
    }

    protected final boolean handleException(Member target, TypeDescriptor type, Object value, Exception ex) {
        boolean cont = !errorHandlers.isEmpty();
        for (BeanWritingErrorHandler meh : errorHandlers) {
            cont &= meh.handleError(target, type, value, ex);
        }
        return cont;
    }

    /**
     * ソースを無視する場合の処理.
     *
     * @param source
     * @param dest
     * @return
     */
    protected TDest handleIgnoreSource(TSource source, TDest dest) {
        return dest;
    }

    /**
     * ソースを無視するかどうか.
     *
     * @param source
     * @return
     */
    protected boolean isIgnoreSource(TSource source) {
        return false;
    }

    /**
     * 準備
     *
     * @param ignoreProperties
     */
    protected void prepare(String... ignoreProperties) {
        destEntries.clear();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(destType);
        for (PropertyDescriptor pd : pds) {
            Method wm = pd.getWriteMethod();
            if (wm != null) {
                Property p = new Property(pd.getPropertyType(), pd.getReadMethod(), wm);
                TypeDescriptor td = new TypeDescriptor(p);
                String name = pd.getName();
                if (!arrayContains(ignoreProperties, name)) {
                    TKey key = createKey(pd, td);
                    if (key != null) {
                        destEntries.put(name, new PropertyEntry(p, key, td));
                    }
                }
            }
        }
        Field[] fds = destType.getDeclaredFields();
        for (Field f : fds) {
            String name = f.getName();
            if (!arrayContains(ignoreProperties, name) && !destEntries.containsKey(f.getName())) {
                TypeDescriptor td = new TypeDescriptor(f);
                TKey key = createKey(f, td);
                if (key != null) {
                    destEntries.put(name, new FieldEntry(f, key, td));
                }
            }
        }
    }

    /**
     * 値の検証. エラーの場合は例外を出す.
     *
     * @param key
     * @param td
     * @param value
     */
    protected void validate(TKey key, TypeDescriptor td, Object value) {
    }

    protected static abstract class Entry<TKey> {

        @Getter
        protected final TypeDescriptor destTypeDescriptor;
        @Getter
        protected final TKey key;

        protected Entry(TKey key, TypeDescriptor typeDescriptor) {
            this.key = key;
            this.destTypeDescriptor = typeDescriptor;
        }

        public abstract Member getMember();

        public abstract void setValue(Object source, Object value) throws ReflectiveOperationException;
    }

    protected static class FieldEntry<TKey> extends Entry<TKey> {

        final Field field;

        FieldEntry(Field field, TKey key, TypeDescriptor typeDescriptor) {
            super(key, typeDescriptor);
            this.field = field;
            makeAccessible(field);
        }

        @Override
        public Member getMember() {
            return field;
        }

        @Override
        public void setValue(Object source, Object value) throws ReflectiveOperationException {
            this.field.set(source, value);
        }

    }

    protected static class PropertyEntry<TKey> extends Entry<TKey> {

        final Property property;
        final Method writeMethod;

        PropertyEntry(Property property, TKey key, TypeDescriptor typeDescriptor) {
            super(key, typeDescriptor);
            this.property = property;
            this.writeMethod = property.getWriteMethod();
            makeAccessible(this.writeMethod);
        }

        @Override
        public Member getMember() {
            return property.getWriteMethod();
        }

        @Override
        public void setValue(Object source, Object value) throws ReflectiveOperationException {
            this.property.getWriteMethod().invoke(source, value);
        }

    }

}

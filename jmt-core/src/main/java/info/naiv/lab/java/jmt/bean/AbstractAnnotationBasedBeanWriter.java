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

import static info.naiv.lab.java.jmt.Strings.*;
import info.naiv.lab.java.jmt.bean.annotation.Mapping;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import lombok.Value;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;

/**
 *
 * @author enlo
 * @param <TSource>
 * @param <TDest>
 */
public abstract class AbstractAnnotationBasedBeanWriter<TSource, TDest>
        extends AbstractBeanWriter<TSource, TDest, AbstractAnnotationBasedBeanWriter.Key> {

    public AbstractAnnotationBasedBeanWriter(Class<TDest> destType, GenericConversionService conversionService) {
        super(destType, conversionService);
        prepare();
    }

    protected abstract boolean checkResolvable(TSource source, Key key);

    @Override
    protected Key createKey(PropertyDescriptor pd, TypeDescriptor type) {
        Mapping m = type.getAnnotation(Mapping.class);
        String n = isNotBlank(m.byName()) ? m.byName() : pd.getName();
        return new Key(n, m);
    }

    @Override
    protected Key createKey(Field field, TypeDescriptor type) {
        Mapping m = type.getAnnotation(Mapping.class);
        String n = isNotBlank(m.byName()) ? m.byName() : field.getName();
        return new Key(n, m);
    }

    @Override
    protected SourceValueResolver<Key> getResolver(final TSource source) {

        return new SourceValueResolver<Key>() {
            @Override
            public boolean containsKey(Key key) {
                return checkResolvable(source, key);
            }

            @Override
            public Object getDefaultValue(Key key, TypeDescriptor type) {
                String val = key.map.defaultValue();
                if (isNotEmpty(val)) {
                    return conversionService.convert(val, type);
                }
                return null;
            }

            @Override
            public String getName(Key key) {
                return key.getName();
            }

            @Override
            public Object getValue(Key key) {
                return resolveValue(source, key);
            }

            @Override
            public TypeDescriptor getValueTypeDescriptor(Key key) {
                return resolveValueDescriptor(source, key);
            }
        };
    }

    protected abstract Object resolveValue(TSource source, Key key);

    protected abstract TypeDescriptor resolveValueDescriptor(TSource source, Key key);

    @Override
    protected void validate(Key key, TypeDescriptor td, Object value) {
        if (key.map.required() && value == null) {
            throw new NullPointerException(key.name + "is must required.");
        }
    }

    @Value
    public static class Key {

        Mapping map;
        String name;

        public Key(String name, Mapping map) {
            this.name = name;
            this.map = map;
        }

        public boolean isByName() {
            return !isByPosition();
        }

        public boolean isByPosition() {
            return 0 <= map.byIndex() && isEmpty(map.byName());
        }
    }
}

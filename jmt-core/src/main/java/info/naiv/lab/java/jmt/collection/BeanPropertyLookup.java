/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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
package info.naiv.lab.java.jmt.collection;

import java.beans.PropertyDescriptor;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import lombok.NonNull;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;

/**
 *
 * @author enlo
 */
public class BeanPropertyLookup implements Lookup<String, Object>, Iterable<Entry<String, Object>> {

    final boolean caseSensitive;
    final BeanWrapper beanWrapper;
    final Map<String, PropertyDescriptor> propertyNames;

    /**
     *
     * @param bean
     */
    public BeanPropertyLookup(@NonNull Object bean) {
        this(bean, true);
    }

    /**
     *
     * @param bean Bean.
     * @param caseSensitive プロパティ名の大文字小文字を区別するかどうか.
     */
    public BeanPropertyLookup(@NonNull Object bean, boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        this.propertyNames = createMap(pds.length, caseSensitive);
        for (PropertyDescriptor pd : pds) {
            if (this.beanWrapper.isReadableProperty(pd.getName())) {
                this.propertyNames.put(pd.getName(), pd);
            }
        }
    }

    @Override
    public boolean containsKey(String key) {
        return propertyNames.containsKey(key);
    }

    @Override
    public Object get(String key) {
        PropertyDescriptor pd = propertyNames.get(key);
        if (pd == null) {
            throw new NotReadablePropertyException(beanWrapper.getWrappedClass(),
                                                   key, "readable property not found.");
        }
        return beanWrapper.getPropertyValue(pd.getName());
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
        Set<Entry<String, Object>> result = new HashSet<>(propertyNames.size());
        for (Entry<String, PropertyDescriptor> e : propertyNames.entrySet()) {
            result.add(new SimpleImmutableEntry<>(e.getKey(), get(e.getValue().getName())));
        }
        return result.iterator();
    }

    /**
     * プロパティの数を取得する.
     *
     * @return
     */
    public int size() {
        return propertyNames.size();
    }

    private static <T> Map<String, T> createMap(int length, boolean caseSensitive) {
        if (caseSensitive) {
            return new HashMap<>(length);
        }
        else {
            return new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        }
    }

}

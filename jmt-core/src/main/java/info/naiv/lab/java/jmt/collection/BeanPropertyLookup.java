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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import lombok.NonNull;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 *
 * @author enlo
 */
public class BeanPropertyLookup implements Lookup<String, Object>, Iterable<Entry<String, Object>> {

    final BeanWrapper beanWrapper;
    final Set<String> propertyNames;

    /**
     *
     * @param bean
     */
    public BeanPropertyLookup(@NonNull Object bean) {
        beanWrapper = new BeanWrapperImpl(bean);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        propertyNames = new HashSet<>(pds.length);
        for (PropertyDescriptor pd : pds) {
            if (beanWrapper.isReadableProperty(pd.getName())) {
                propertyNames.add(pd.getName());
            }
        }
    }

    @Override
    public boolean containsKey(String key) {
        return propertyNames.contains(key);
    }

    @Override
    public Object get(String key) {
        Object value = beanWrapper.getPropertyValue(key);
        return value;
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
        Set<Entry<String, Object>> result = new HashSet<>(propertyNames.size());
        for (String name : propertyNames) {
            result.add(new SimpleImmutableEntry<>(name, get(name)));
        }
        return result.iterator();
    }

    /**
     * プロパティの数を取得する.
     *
     * @return
     */
    public int size() {
        return beanWrapper.getPropertyDescriptors().length;
    }

}

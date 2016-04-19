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
package info.naiv.lab.java.jmt.infrastructure;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;
import org.springframework.beans.TypeMismatchException;

/**
 * オブジェクトのプロパティを属性とするための実装.
 *
 * @author enlo
 */
@Slf4j
public class ObjectAttributes implements Attributes {

    private static Map<String, PropertyDescriptor> makeMap(Object object) {
        Map<String, PropertyDescriptor> result = new HashMap<>();
        PropertyDescriptor[] pds = getPropertyDescriptors(object.getClass());
        for (PropertyDescriptor pd : pds) {
            result.put(pd.getName(), pd);
        }
        return unmodifiableMap(result);
    }

    private final Object object;
    private final Map<String, PropertyDescriptor> pdmap;

    /**
     * コンストラクター
     *
     * @param object オブジェクト
     */
    public ObjectAttributes(Object object) {
        nonNull(object, "object");
        this.object = object;
        this.pdmap = makeMap(object);
    }

    @Override
    public boolean contains(String name) {
        return pdmap.containsKey(name);
    }

    @Override
    public Map<String, Object> copyTo(Map<String, Object> target) {
        for (Entry<String, PropertyDescriptor> e : pdmap.entrySet()) {
            String name = e.getKey();
            PropertyDescriptor pd = e.getValue();
            target.put(name, getAttribute(name, pd));
        }
        return target;
    }

    @Override
    public Object getAttribute(String name) {
        PropertyDescriptor pd = pdmap.get(name);
        return getAttribute(name, pd);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String name, Class<T> clazz) {
        Object val = getAttribute(name);
        if (val == null) {
            return null;
        }
        else if (clazz.isAssignableFrom(val.getClass())) {
            return (T) val;
        }
        else {
            throw new TypeMismatchException(val, clazz);
        }
    }

    @Override
    public Set<String> getAttributeNames() {
        return pdmap.keySet();
    }

    /**
     * 属性の取得.
     *
     * @param name
     * @param pd
     * @return
     */
    private Object getAttribute(String name, PropertyDescriptor pd) {
        if (pd != null) {
            try {
                return pd.getReadMethod().invoke(object);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.error("property access failed. name:{} exception:{}", name, e);
            }
        }
        else {
            logger.error("property not found. name:{}", name);
        }
        return null;
    }

}

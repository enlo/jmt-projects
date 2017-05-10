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
package info.naiv.lab.java.jmt.jdbc;

import info.naiv.lab.java.jmt.jdbc.AutoResultSetToBeanMapper.Key;
import info.naiv.lab.java.jmt.runtime.AbstractBeanWriter;
import java.beans.PropertyDescriptor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;

/**
 *
 * @author enlo
 * @param <T>
 */
@Slf4j
public class AutoResultSetToBeanMapper<T>
        extends AbstractBeanWriter<T, ResultSetAccessHelper, Key> {

    @Value
    public static class Key {

        String name;
        JDBCTypeTraits traits;
    }

    public AutoResultSetToBeanMapper(Class<T> clazz,
                                     GenericConversionService conversionService) {
        super(clazz, conversionService);
        init();
    }

    @Override
    protected Key createSourceKey(PropertyDescriptor dstPd) {
        String pdn = dstPd.getName();
        Class<?> pdt = dstPd.getPropertyType();
        JDBCTypeTraits trt = JDBCTypeTraits.valueOf(pdt);
        if (trt.equals(JDBCTypeTraits.OTHER)) {
            logger.debug("JDBC Incompatible Type. {}, {}", pdn, pdt);
        }
        return new Key(pdn, trt);
    }

    @Override
    protected String getSourceName(Key key) {
        return key.name;
    }

    @Override
    protected TypeDescriptor getSourceType(Key key, PropertyDescriptor dstProp) {
        return TypeDescriptor.valueOf(key.traits.getRecommendType());
    }

    @Override
    protected Object getSourceValue(ResultSetAccessHelper source, Key key) {
        return source.get(key.name, key.traits);
    }

}

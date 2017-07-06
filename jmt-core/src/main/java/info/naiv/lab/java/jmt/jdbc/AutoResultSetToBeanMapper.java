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

import info.naiv.lab.java.jmt.bean.AbstractAnnotationBasedBeanWriter;
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
        extends AbstractAnnotationBasedBeanWriter<ResultSetAccessHelper, T> {

    public AutoResultSetToBeanMapper(Class<T> clazz,
                                     GenericConversionService conversionService) {
        super(clazz, conversionService);
    }

    @Override
    protected boolean checkResolvable(ResultSetAccessHelper source, Key key) {
        return source.containsKey(key.getName());
    }

    @Override
    protected Object resolveValue(ResultSetAccessHelper source, Key key) {
        return source.get(key.getName());
    }

    @Override
    protected TypeDescriptor resolveValueDescriptor(ResultSetAccessHelper source, Key key) {
        Class<?> clz = source.getMappedType(key.getName());
        return TypeDescriptor.valueOf(clz);
    }

}

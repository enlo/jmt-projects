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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayContains;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * Bean をコピーするための仕組み.
 * {@link BeanUtils#copyProperties(java.lang.Object, java.lang.Object, java.lang.String...) }
 * だと都度都度解析を行うため、必要となる部分をあらかじめキャッシュしておく.
 *
 * @author enlo
 */
@Slf4j
public class SimpleBeanCopier {

    protected final GenericConversionService conversionService;
    protected final Map<String, PropertyEntry> properties;

    /**
     *
     * @param srcType
     * @param dstType
     * @param conversionService
     * @param ignoreProperties
     */
    public SimpleBeanCopier(@NonNull Class<?> srcType,
                            @NonNull Class<?> dstType,
                            GenericConversionService conversionService,
                            String... ignoreProperties) {
        PropertyDescriptor[] wrkDstPds = BeanUtils.getPropertyDescriptors(dstType);
        this.properties = new HashMap<>(wrkDstPds.length);
        this.conversionService = conversionService;
        for (PropertyDescriptor wrkDstPd : wrkDstPds) {
            Method dstMethod = wrkDstPd.getWriteMethod();
            String key = wrkDstPd.getName();
            if (dstMethod != null && !arrayContains(ignoreProperties, key)) {
                PropertyDescriptor wrkSrcPd = BeanUtils.getPropertyDescriptor(srcType, key);
                if (wrkSrcPd != null) {
                    Method srcMethod = wrkSrcPd.getReadMethod();
                    if (srcMethod != null) {
                        PropertyEntry pe = new PropertyEntry(wrkSrcPd, srcMethod, wrkDstPd, dstMethod);
                        properties.put(key, pe);
                    }
                }
            }
        }
    }

    /**
     * プロパティのコピー
     *
     * @param source コピー元
     * @param dest コピー先
     */
    public void copyProperties(@NonNull Object source, @NonNull Object dest) {
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
    }

    @Value
    protected static final class PropertyEntry {

        TypeDescriptor dstType;
        Method dstWrite;
        boolean noConversion;
        Method srcRead;
        TypeDescriptor srcType;

        PropertyEntry(PropertyDescriptor srcProp, Method srcRead, PropertyDescriptor dstProp, Method dstWrite) {
            this.srcRead = srcRead;
            if (!Modifier.isPublic(this.srcRead.getDeclaringClass().getModifiers())) {
                this.srcRead.setAccessible(true);
            }
            this.dstWrite = dstWrite;
            if (!Modifier.isPublic(this.dstWrite.getDeclaringClass().getModifiers())) {
                this.dstWrite.setAccessible(true);
            }
            this.srcType = TypeDescriptor.valueOf(srcProp.getPropertyType());
            this.dstType = TypeDescriptor.valueOf(dstProp.getPropertyType());
            this.noConversion = srcType.equals(dstType);
        }

        public void copy(Object src, Object dst) {
            try {
                Object val = srcRead.invoke(src);
                dstWrite.invoke(dst, val);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                rethrow(ex);
            }
        }

        public void copy(Object src, Object dst, GenericConversionService conversionService) {
            try {
                Object val = srcRead.invoke(src);
                if (!noConversion) {
                    val = conversionService.convert(val, srcType, dstType);
                }
                dstWrite.invoke(dst, val);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                rethrow(ex);
            }
        }

        public void rethrow(final Exception ex) throws FatalBeanException {
            String methodName = srcRead.getName();
            String message = String.format("Could not copy properties from source to target. (%s) ", methodName);
            throw new FatalBeanException(message, ex);
        }
    }

}

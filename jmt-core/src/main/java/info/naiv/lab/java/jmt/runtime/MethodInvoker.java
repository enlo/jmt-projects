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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author enlo
 */
public interface MethodInvoker {

    final int ARGC_MAX = 0xFF;

    boolean checkParameterCount(int argc);

    /**
     * アノテーションの取得.
     *
     * @param <A>
     * @param annotationClass
     * @return
     */
    <A extends Annotation> A getAnnotation(Class<A> annotationClass);

    /**
     *
     * @return パラメータタイプの取得.
     */
    Class<?>[] getParameterTypes();

    /**
     * メソッド呼び出し.
     *
     * @param target
     * @param args
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    Object invoke(Object target, Object... args) throws IllegalAccessException,
                                                        IllegalArgumentException,
                                                        InvocationTargetException;
}

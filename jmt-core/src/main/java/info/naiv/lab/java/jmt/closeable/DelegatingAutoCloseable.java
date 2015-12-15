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
package info.naiv.lab.java.jmt.closeable;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import info.naiv.lab.java.jmt.ImmutableHolder;
import info.naiv.lab.java.jmt.fx.Consumer1;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author enlo
 * @param <T>
 */
public class DelegatingAutoCloseable<T> extends ImmutableHolder<T> implements ACS<T> {

    final Consumer1<T> closeMethod;

    public DelegatingAutoCloseable(T object) throws IllegalArgumentException {
        this(object, new ReflectClose<>(object));
    }

    public DelegatingAutoCloseable(T object, Consumer1<T> closeMethod) throws IllegalArgumentException {
        super(object);
        this.closeMethod = nonNull(closeMethod, "closeMethod");
    }

    @Override
    public void close() {
        closeMethod.accept(getContent());
    }

    private static class ReflectClose<T> implements Consumer1<T> {

        final private Method method;

        ReflectClose(T object) {
            nonNull(object, "object");
            try {
                this.method = object.getClass().getDeclaredMethod("close");
            }
            catch (NoSuchMethodException | SecurityException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        @Override
        public void accept(T a1) {
            try {
                this.method.invoke(a1);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

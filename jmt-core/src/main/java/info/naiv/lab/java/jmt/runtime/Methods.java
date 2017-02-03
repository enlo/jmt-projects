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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayAppend;
import static info.naiv.lab.java.jmt.Misc.isEmpty;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author enlo
 */
public class Methods {

    public static MethodInvoker bind1st(@NonNull Method method, Object arg1) {
        Class<?>[] ptypes = method.getParameterTypes();
        if (isEmpty(ptypes)) {
            throw new IllegalArgumentException("method is no arity.");
        }
        if (arg1 != null) {
            if (!ptypes[0].isAssignableFrom(arg1.getClass())) {
                throw new IllegalArgumentException("arg1 is non compatible type.");
            }
        }
        return new MethodBinder(method, new Object[]{arg1});
    }

    public static MethodInvoker bind1st(@NonNull MethodInvoker mi, Object arg1) {
        Class<?>[] ptypes = mi.getParameterTypes();
        if (isEmpty(ptypes)) {
            throw new IllegalArgumentException("method is no arity.");
        }
        if (arg1 != null) {
            if (!ptypes[0].isAssignableFrom(arg1.getClass())) {
                throw new IllegalArgumentException("arg1 is non compatible type.");
            }
        }
        return new MethodInvokerBinder(mi, new Object[]{arg1});
    }

    private Methods() {
    }

    @Value
    static class MethodBinder implements MethodInvoker {

        Object[] bound;
        Method method;
        Class<?>[] parameterTypes;

        MethodBinder(Method method, Object[] bound) {
            this.method = method;
            this.bound = bound;
            Class<?>[] ptypes = method.getParameterTypes();
            this.parameterTypes = Arrays.copyOfRange(ptypes, bound.length, ptypes.length);
        }

        @Override
        public boolean checkParameterCount(int argc) {
            return parameterTypes.length == argc;
        }

        @Override
        public Object invoke(Object target, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Object[] newArgs = arrayAppend(bound, args);
            return method.invoke(target, newArgs);
        }

    }

    @Value
    static class MethodInvokerBinder implements MethodInvoker {

        Object[] bound;
        MethodInvoker mi;
        Class<?>[] parameterTypes;

        MethodInvokerBinder(MethodInvoker mi, Object[] bound) {
            this.mi = mi;
            this.bound = bound;
            Class<?>[] ptypes = mi.getParameterTypes();
            this.parameterTypes = Arrays.copyOfRange(ptypes, bound.length, ptypes.length);
        }

        @Override
        public boolean checkParameterCount(int argc) {
            return parameterTypes.length == argc;
        }

        @Override
        public Object invoke(Object target, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Object[] newArgs = arrayAppend(bound, args);
            return mi.invoke(target, newArgs);
        }
    }

}

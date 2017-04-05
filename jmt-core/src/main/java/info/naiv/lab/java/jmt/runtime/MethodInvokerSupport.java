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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import lombok.EqualsAndHashCode;

/**
 *
 * @author enlo
 */
@EqualsAndHashCode
public class MethodInvokerSupport {

    private final Method method;

    public MethodInvokerSupport(Method method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public Object[] resolveArgs(Object[] args) {
        return args;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    public boolean checkParameterCount(int argc) {
        return getParameterTypes().length == argc;
    }

    public Object invoke(Object target, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (args == null) {
            args = new Object[]{};
        }
        Object[] newArgs = resolveArgs(args);
        return method.invoke(target, newArgs);
    }

    public Callable<Object> toCallable(final Object target, Object[] args) {
        if (args == null) {
            args = new Object[]{};
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            if (!method.getDeclaringClass().isAssignableFrom(target.getClass())) {
                return null;
            }
        }
        if (!checkParameterCount(args.length)) {
            return null;
        }
        final Object[] newArgs = resolveArgs(args);
        if (!Methods.checkInvoke(method, newArgs)) {
            return null;
        }
        return new MethodCaller(target, method, args);
    }

    public static class MethodCaller implements Callable<Object> {

        Object target;
        Method method;
        Object[] args;

        public MethodCaller(@Nonnull Object target, @Nonnull Method method, @Nonnull Object[] args) {
            this.target = target;
            this.method = method;
            this.args = args;
        }

        @Override
        public Object call() throws Exception {
            try {
                return method.invoke(target, args);
            }
            catch (InvocationTargetException ex) {
                Throwable e = ex.getTargetException();
                if (e instanceof Exception) {
                    throw (Exception) e;
                }
                else {
                    throw ex;
                }
            }
        }

    }


    public static class ArgsResolver {
        
        
        public Object[] resolve(Object[] args) {
            int pc = args.length;
            int argc = args.length;
            Object[] dest = new Object[pc];
            if (argc < pc) {
                copyArgs(dest, args, argc);
                return dest;
            }
            else {
                copyArgs(dest, args, argc);
                return dest;
            }
        }

        protected void copyArgs(Object[] dest, Object[] args, int argc) {
            System.arraycopy(args, 0, dest, 0, argc);
        }
    }
}

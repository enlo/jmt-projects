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
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Arrays;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.Value;
import static org.springframework.util.ClassUtils.isAssignable;

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
            if (!isAssignable(ptypes[0], arg1.getClass())) {
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
            if (!isAssignable(ptypes[0], arg1.getClass())) {
                throw new IllegalArgumentException("arg1 is non compatible type.");
            }
        }
        return new MethodInvokerBinder(mi, new Object[]{arg1});
    }

    public static boolean checkInvoke(@Nonnull Method method, Object... args) {
        Class<?>[] clzs = method.getParameterTypes();
        if (clzs.length != args.length) {
            return false;
        }
        for (int i = 0; i < clzs.length; i++) {
            Class<?> clz = clzs[i];
            Object arg = args[i];
            if (arg == null) {
                if (clz.isPrimitive()) {
                    return false;
                }
            }
            else if (!clz.isAssignableFrom(arg.getClass())) {
                return false;
            }
        }
        return true;
    }

    public static <Duck> Duck duck(@Nonnull Object obj, Duck... dummy) {
        Class<?> clazz = dummy.getClass().getComponentType();
        if (clazz.isAssignableFrom(obj.getClass())) {
            return (Duck) obj;
        }

        InvocationHandler ih = new MethodInvokerInvocationHandler(obj, AccessController.getContext());
        return (Duck) InterfaceImplementor.getInterface(clazz, ih);
    }

    public static <Duck> Duck duck(Class<Duck> clazz, Object obj) {
        if (clazz.isAssignableFrom(obj.getClass())) {
            return (Duck) obj;
        }
        InvocationHandler ih = new MethodInvokerInvocationHandler(obj, AccessController.getContext());
        return InterfaceImplementor.getInterface(clazz, ih);
    }

    @Nonnull
    public static MethodInvoker toMethodInvoker(@Nonnull Method m) {
        return new SimpleMethodInvoker(m);
    }

    private Methods() {
    }

    @Value
    static class MethodBinder implements MethodInvoker {

        Object[] bound;
        Method method;
        Class<?>[] parameterTypes;
        Annotation[][] annotations;
        int annotationOffset;

        MethodBinder(Method method, Object[] bound) {
            this.method = method;
            this.bound = bound;
            Class<?>[] ptypes = method.getParameterTypes();
            this.parameterTypes = Arrays.copyOfRange(ptypes, bound.length, ptypes.length);
            this.annotations = method.getParameterAnnotations();
            this.annotationOffset = bound.length;
        }

        @Override
        public boolean checkParameterCount(int argc) {
            return parameterTypes.length == argc;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
            return method.getAnnotation(annotationClass);
        }

        @Override
        public <A extends Annotation> A getParameterAnnotation(int i, Class<A> annotationClass) {
            return MethodInvokerSupport.findAnnotation(annotations[i + annotationOffset], annotationClass);
        }

        @Override
        public Object invoke(Object target, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Object[] newArgs = arrayAppend(bound, args);
            return method.invoke(target, newArgs);
        }

        @Override
        public Callable<Object> toCallable(Object target, Object... args) {
            Object[] newArgs = arrayAppend(bound, args);
            return (new MethodInvokerSupport(method)).toCallable(target, newArgs);
        }

    }

    @Value
    static class MethodInvokerBinder implements MethodInvoker {

        Object[] bound;
        MethodInvoker mi;
        Class<?>[] parameterTypes;
        int annotationOffset;

        MethodInvokerBinder(MethodInvoker mi, Object[] bound) {
            this.mi = mi;
            this.bound = bound;
            Class<?>[] ptypes = mi.getParameterTypes();
            this.parameterTypes = Arrays.copyOfRange(ptypes, bound.length, ptypes.length);
            this.annotationOffset = bound.length;
        }

        @Override
        public boolean checkParameterCount(int argc) {
            return parameterTypes.length == argc;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
            return mi.getAnnotation(annotationClass);
        }

        @Override
        public <A extends Annotation> A getParameterAnnotation(int i, Class<A> annotationClass) {
            return mi.getParameterAnnotation(i + annotationOffset, annotationClass);
        }

        @Override
        public Object invoke(Object target, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Object[] newArgs = arrayAppend(bound, args);
            return mi.invoke(target, newArgs);
        }

        @Override
        public Callable<Object> toCallable(Object target, Object... args) {
            Object[] newArgs = arrayAppend(bound, args);
            return mi.toCallable(target, newArgs);
        }
    }

}

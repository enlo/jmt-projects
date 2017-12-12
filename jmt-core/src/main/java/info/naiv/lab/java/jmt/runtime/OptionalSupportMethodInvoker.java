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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.copyOfRangeToTypedArray;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.createTypedArray;
import info.naiv.lab.java.jmt.runtime.MethodInvokerSupport.MethodCaller;
import info.naiv.lab.java.jmt.tuple.Tuple3;
import info.naiv.lab.java.jmt.tuple.Tuples;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;
import org.springframework.util.ClassUtils;

/**
 *
 * @author enlo
 */
@Value
@EqualsAndHashCode(of = "method")
public class OptionalSupportMethodInvoker implements MethodInvoker {

    private static boolean checkVarArgs(Method m, Class<?>[] paramTypes, int i) {
        if (m.isVarArgs()) {
            return (i == (paramTypes.length - 1));
        }
        return false;
    }

    private static boolean makeOptional(Parameter p, Annotation[] annotations, boolean isVarArgs) {
        if (isVarArgs) {
            p.optional = true;
            p.defaultValue = Array.newInstance(p.type.getComponentType(), 0);
            return true;
        }
        else if (ClassUtils.matchesTypeName(p.type, "Optional")) {
            p.optional = true;
            Method fm = ClassUtils.getStaticMethod(p.type, "empty");
            try {
                p.defaultValue = fm.invoke(null);
                p.factory = ClassUtils.getStaticMethod(p.type, "ofNullable", Object.class);
                return true;
            }
            catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        return false;
    }

    private static Tuple3<Parameter[], Integer, Integer> makeParams(Method m, Class<?>[] paramTypes, Annotation[][] paramAnnos) {
        Parameter[] result = new Parameter[paramTypes.length];
        boolean checkOptional = true;
        int nonOptional = 0;
        for (int i = paramAnnos.length - 1; 0 <= i; i--) {
            Annotation[] anno = paramAnnos[i];
            Parameter p = new Parameter();
            p.type = paramTypes[i];
            if (checkOptional) {
                checkOptional = makeOptional(p, anno, checkVarArgs(m, paramTypes, i));
                if (!checkOptional) {
                    nonOptional++;
                }
            }
            else {
                nonOptional++;
            }
            result[i] = p;
        }
        return Tuples.tie(result, nonOptional, m.isVarArgs() ? ARGC_MAX : result.length);
    }

    final int argcMax;
    final int argcMin;
    final ArgsResolver argsResolver;
    final Method method;
    final Parameter[] params;

    public OptionalSupportMethodInvoker(@Nonnull Method method) {
        this.method = method;

        Annotation[][] paramAnnos = method.getParameterAnnotations();
        Class<?>[] paramTypes = method.getParameterTypes();

        Tuple3<Parameter[], Integer, Integer> tx = makeParams(method, paramTypes, paramAnnos);
        this.params = tx.getValue1();
        this.argcMin = tx.getValue2();
        this.argcMax = tx.getValue3();
        this.argsResolver = method.isVarArgs() ? new VarArgsResolver() : new ArgsResolver();
    }

    @Override
    public boolean checkParameterCount(int argc) {
        return argcMin <= argc && argc <= argcMax;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    @Override
    public <A extends Annotation> A getParameterAnnotation(int i, Class<A> annotationClass) {
        return MethodInvokerSupport.findAnnotation(method.getParameterAnnotations()[i], annotationClass);
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object invoke(Object target, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (args == null) {
            args = new Object[]{};
        }
        Object[] newArgs = argsResolver.resolve(args);
        return method.invoke(target, newArgs);
    }

    @Override
    public Callable<Object> toCallable(final Object target, Object[] args) {
        if (args == null) {
            args = new Object[]{};
        }
        if (!method.getDeclaringClass().isAssignableFrom(target.getClass())) {
            return null;
        }
        if (!checkParameterCount(args.length)) {
            return null;
        }
        final Object[] newArgs = argsResolver.resolve(args);
        if (!Methods.checkInvoke(method, newArgs)) {
            return null;
        }
        return new MethodCaller(target, method, newArgs);
    }

    private static class Parameter {

        Object defaultValue;
        Method factory;
        boolean optional;
        Class<?> type;

        @SneakyThrows
        public Object resolve(Object val) {
            if (optional) {
                if (ClassUtils.isAssignableValue(type, val)) {
                    return val;
                }
                else if (factory != null) {
                    return factory.invoke(null, val);
                }
            }
            return val;
        }
    }

    private class ArgsResolver {

        public Object[] resolve(Object[] args) {
            int pc = params.length;
            int argc = args.length;
            Object[] dest = new Object[pc];
            if (argc < pc) {
                copyArgs(dest, args, argc);
                for (int i = argc; i < pc; i++) {
                    dest[i] = params[i].defaultValue;
                }
                return dest;
            }
            else {
                copyArgs(dest, args, argc);
                return dest;
            }
        }

        protected void copyArgs(Object[] dest, Object[] args, int argc) {
            for (int i = 0; i < argc; i++) {
                dest[i] = params[i].resolve(args[i]);
            }
        }
    }

    private class VarArgsResolver extends ArgsResolver {

        @Override
        public Object[] resolve(Object[] args) {
            int pc = params.length;
            int argc = args.length;
            Object[] dest = new Object[pc];
            if (argc < pc) {
                copyArgs(dest, args, argc);
                for (int i = argc; i < pc; i++) {
                    dest[i] = params[i].defaultValue;
                }
            }
            else if (pc == argc) {
                int lidx = pc - 1;
                Object last = args[lidx];
                copyArgs(dest, args, lidx);
                if (last == null) {
                    dest[lidx] = params[lidx].defaultValue;
                }
                else if (!last.getClass().isArray()) {
                    Class<?> ctype = params[lidx].type.getComponentType();
                    dest[lidx] = createTypedArray(ctype, last);
                }
                else {
                    dest[lidx] = last;
                }
            }
            else {
                int lidx = pc - 1;
                copyArgs(dest, args, lidx);
                Class<?> ctype = params[lidx].type.getComponentType();
                dest[lidx] = copyOfRangeToTypedArray(ctype, args, lidx, argc);
            }
            return dest;
        }

    }

}

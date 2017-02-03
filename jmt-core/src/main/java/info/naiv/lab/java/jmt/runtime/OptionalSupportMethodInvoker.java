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
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import lombok.SneakyThrows;
import org.springframework.util.ClassUtils;

/**
 *
 * @author enlo
 */
public final class OptionalSupportMethodInvoker implements MethodInvoker {

    final Method method;
    final Parameter[] params;
    final ArgsResolver argsResolver;

    public OptionalSupportMethodInvoker(@Nonnull Method method) {
        this.method = method;

        Annotation[][] paramAnnos = method.getParameterAnnotations();
        Class<?>[] paramTypes = method.getParameterTypes();

        this.params = makeParams(paramTypes, paramAnnos);
        this.argsResolver = method.isVarArgs() ? new VarArgsResolver() : new ArgsResolver();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object invoke(Object target, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] newArgs = argsResolver.resolve(args);
        return method.invoke(target, newArgs);
    }

    private boolean checkVarArgs(Class<?>[] paramTypes, int i) {
        if (method.isVarArgs()) {
            return (i == (paramTypes.length - 1));
        }
        return false;
    }

    private boolean makeOptional(Parameter p, Annotation[] annotations, boolean isVarArgs) {
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

    private Parameter[] makeParams(Class<?>[] paramTypes, Annotation[][] paramAnnos) {
        Parameter[] result = new Parameter[paramTypes.length];
        boolean checkOptional = true;
        for (int i = paramAnnos.length - 1; 0 <= i; i--) {
            Annotation[] anno = paramAnnos[i];
            Parameter p = new Parameter();
            p.type = paramTypes[i];
            if (checkOptional) {
                checkOptional = makeOptional(p, anno, checkVarArgs(paramTypes, i));
            }
            result[i] = p;
        }
        return result;
    }

    private class Parameter {

        Class<?> type;
        boolean optional;
        Object defaultValue;
        Method factory;

        @SneakyThrows
        public Object valueOf(Object val) {
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
                dest[i] = params[i].valueOf(args[i]);
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

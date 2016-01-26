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
package info.naiv.lab.java.jmt.runtime;

import info.naiv.lab.java.jmt.fx.Function2;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author enlo
 */
public class InterfaceImplementorInvocationHandler implements InvocationHandler {

    protected final AccessControlContext accCtrlContext;

    @Getter
    @Setter
    protected Function2<Method, Object[], ? extends Object> externalRunner;

    public InterfaceImplementorInvocationHandler(AccessControlContext accCtrlContext) {
        this(accCtrlContext, null);
    }

    public InterfaceImplementorInvocationHandler(AccessControlContext accCtrlContext, Function2<Method, Object[], ? extends Object> externalRunner) {
        this.accCtrlContext = accCtrlContext;
        this.externalRunner = externalRunner;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        final Object[] modArgs = convertArguments(method, args);
        Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            @Override
            public Object run() throws Exception {
                return internalInvoke(method, modArgs);
            }
        }, accCtrlContext);
        return convertResult(method, result);
    }

    protected Object[] convertArguments(Method method, Object[] args) {
        return args;
    }

    protected Object convertResult(Method method, Object result) {
        return result;
    }

    protected Object internalInvoke(Method method, Object[] args) {
        if (externalRunner != null) {
            return externalRunner.apply(method, args);
        }
        return null;
    }
}

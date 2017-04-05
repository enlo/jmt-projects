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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 *
 * @author enlo
 */
public abstract class AbstractInvocationHandler implements InvocationHandler {

    /**
     *
     */
    protected final AccessControlContext accCtrlContext;

    public AbstractInvocationHandler(AccessControlContext accCtrlContext) {
        this.accCtrlContext = accCtrlContext;
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

    /**
     *
     * @param method
     * @param args
     * @return
     */
    protected Object[] convertArguments(Method method, Object[] args) {
        return args;
    }

    /**
     *
     * @param method
     * @param result
     * @return
     */
    protected Object convertResult(Method method, Object result) {
        return result;
    }

    /**
     *
     * @param method
     * @param args
     * @return
     * @throws java.lang.Exception
     */
    protected abstract Object internalInvoke(Method method, Object[] args) throws Exception;

}

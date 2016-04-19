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

import static info.naiv.lab.java.jmt.Arguments.isInterface;
import info.naiv.lab.java.jmt.fx.Function2;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessControlContext;
import java.security.AccessController;

/**
 *
 * @author enlo
 */
public class InterfaceImplementor {

    /**
     *
     * @param <T>
     * @param clazz
     * @param handler
     * @return
     */
    @ReturnNonNull
    public static <T> T getInterface(Class<T> clazz, InvocationHandler handler) {
        isInterface(clazz, "clazz");
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(),
                                              new Class[]{clazz},
                                              handler);
        return clazz.cast(proxy);
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @param externalRunner
     * @return
     */
    @ReturnNonNull
    public static <T> T getInterface(Class<T> clazz, Function2<Method, Object[], ? extends Object> externalRunner) {
        isInterface(clazz, "clazz");
        AccessControlContext accCtrlContext = AccessController.getContext();
        InvocationHandler handler = new InterfaceImplementorInvocationHandler(accCtrlContext, externalRunner);
        return getInterface(clazz, handler);
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @ReturnNonNull
    public static <T> T getInterface(Class<T> clazz) {
        return getInterface(clazz, (Function2<Method, Object[], ? extends Object>) null);
    }

    private InterfaceImplementor() {
    }
}

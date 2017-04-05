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

import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class MethodInvokerInvocationHandler extends AbstractInvocationHandler {

    private final Object target;
    private final MethodInvokerRegistry mir;

    public MethodInvokerInvocationHandler(@Nonnull Object target, AccessControlContext accCtrlContext) {
        super(accCtrlContext);
        this.target = target;
        this.mir = new MethodInvokerRegistry(target.getClass(), false);
        this.mir.prepare();
    }

    @Override
    protected Object internalInvoke(Method method, Object[] args) throws Exception {
        for (MethodInvoker mi : mir.get(method.getName())) {
            Callable<Object> c = mi.toCallable(target, args);
            if (c != null) {
                return c.call();
            }
        }
        throw new IllegalStateException(method.getName() + " is missing.");
    }

}

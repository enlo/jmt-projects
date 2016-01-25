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
package info.naiv.lab.java.jmt.infrastructure.component;

import info.naiv.lab.java.jmt.infrastructure.AbstractServiceConnection;
import info.naiv.lab.java.jmt.infrastructure.AbstractServiceContainer;
import info.naiv.lab.java.jmt.infrastructure.Tag;

/**
 *
 * @author enlo
 */
public class ComponentServiceConnection extends AbstractServiceConnection {

    final ServiceComponent<?> serviceHolder;

    public ComponentServiceConnection(ServiceComponent<?> serviceHolder, int priority, AbstractServiceContainer container, Tag tag) {
        super(priority, container, tag);
        this.serviceHolder = serviceHolder;
    }

    @Override
    public boolean contains(Class<?> serviceType, Tag tag) {
        return super.contains(serviceType, Tag.ANY)
                && serviceHolder.contains(tag, this.getContainer());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceType, Tag tag) {
        if (!serviceHolder.getClass().equals(serviceType)) {
            return (T) serviceHolder.getContent(tag, this.getContainer());
        }
        else {
            return serviceType.cast(serviceHolder);
        }
    }

    @Override
    public boolean isAssignableTo(Class<?> clazz) {
        return clazz.isAssignableFrom(serviceHolder.getClass())
                || clazz.isAssignableFrom(serviceHolder.getContentType());
    }

    @Override
    public boolean isSameInstance(Object object) {
        return serviceHolder == object;
    }

}

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
package info.naiv.lab.java.jmt.infrastructure;

import info.naiv.lab.java.jmt.Holder;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class ProxyServiceConnection extends AbstractServiceConnection {

    final Holder<?> serviceHolder;

    /**
     *
     * @param serviceHolder
     * @param priority
     * @param container
     * @param tag
     */
    public ProxyServiceConnection(@NonNull Holder<?> serviceHolder, int priority,
                                  @Nonnull AbstractServiceContainer container, Tag tag) {
        super(priority, container, tag);
        this.serviceHolder = serviceHolder;
    }

    @Override
    public <T> T getService(Class<T> serviceType, Tag tag) {
        if (!serviceHolder.getClass().equals(serviceType)) {
            return (T) serviceHolder.getContent();
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

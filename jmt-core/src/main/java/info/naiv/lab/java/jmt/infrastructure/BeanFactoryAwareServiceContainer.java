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
package info.naiv.lab.java.jmt.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 *
 * @author enlo
 */
@Slf4j
@RequiredArgsConstructor
public class BeanFactoryAwareServiceContainer implements ServiceContainer, BeanFactoryAware {

    @NonNull
    final ServiceContainer baseContainer;

    BeanFactory beanFactory;

    @Override
    public void close() throws Exception {
        baseContainer.close();
    }

    @Override
    public UUID id() {
        return baseContainer.id();
    }

    @Override
    public ServiceConnection registerService(Object service, Tag tag) throws IllegalArgumentException {
        return baseContainer.registerService(service, tag);
    }

    @Override
    public ServiceConnection registerService(Object service) throws IllegalArgumentException {
        return baseContainer.registerService(service);
    }

    @Override
    public ServiceConnection registerService(int priority, Object service) throws IllegalArgumentException {
        return baseContainer.registerService(priority, service);
    }

    @Override
    public ServiceConnection registerService(int priority, Object service, Tag tag) throws IllegalArgumentException {
        return baseContainer.registerService(priority, service, tag);
    }

    @Override
    public <T> T resolveService(Class<T> serviceType) {
        T service = baseContainer.resolveService(serviceType);
        if (service == null && beanFactory != null) {
            try {
                service = beanFactory.getBean(serviceType);
            }
            catch (BeansException ex) {
                logger.warn("bean not found. {}", ex);
            }
        }
        return service;
    }

    @Override
    public <T> T resolveService(Class<T> serviceType, Tag tag) {
        T service = baseContainer.resolveService(serviceType, tag);
        if (service == null && beanFactory != null) {
            try {
                service = beanFactory.getBean(serviceType);
            }
            catch (BeansException ex) {
                logger.warn("bean not found. {}", ex);
            }
        }
        return service;
    }

    @Override
    public <T> Collection<T> resolveServices(Class<T> serviceType) {
        List<T> services = new ArrayList<>();
        services.addAll(baseContainer.resolveServices(serviceType));
        services.add(beanFactory.getBean(serviceType));
        return services;
    }

    @Override
    public <T> Collection<T> resolveServices(Class<T> serviceType, Tag tag) {
        List<T> services = new ArrayList<>();
        services.addAll(baseContainer.resolveServices(serviceType, tag));
        services.add(beanFactory.getBean(serviceType));
        return services;
    }

    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException {
        this.beanFactory = bf;
    }

}

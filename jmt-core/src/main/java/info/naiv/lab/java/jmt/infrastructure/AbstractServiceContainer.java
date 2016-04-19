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

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import info.naiv.lab.java.jmt.Holder;
import static info.naiv.lab.java.jmt.Misc.addIfNotFound;
import static info.naiv.lab.java.jmt.closeable.Closeables.closeAll;
import info.naiv.lab.java.jmt.closeable.Guard;
import info.naiv.lab.java.jmt.closeable.ReverseGuard;
import info.naiv.lab.java.jmt.infrastructure.annotation.ServicePriority;
import info.naiv.lab.java.jmt.infrastructure.component.ComponentServiceConnection;
import info.naiv.lab.java.jmt.infrastructure.component.ServiceComponent;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.OrderUtils;

/**
 * 抽象サービスコンテナ. <br>
 * 内部で CopyOnWriteArrayList を利用することで、登録・削除時のみ同期をとる.
 *
 * @author enlo
 */
public abstract class AbstractServiceContainer implements ServiceContainer {

    final List<AbstractServiceConnection> connections;
    final ServiceProvider defaultProvider;
    final UUID uuid;

    /**
     * コンストラクター.
     *
     * @param defaultProvider 既定のサービス.
     */
    public AbstractServiceContainer(ServiceProvider defaultProvider) {
        this(defaultProvider, new CopyOnWriteArrayList<AbstractServiceConnection>());
    }

    /**
     * コピーコンストラクター.
     *
     * @param defaultProvider 既定のサービス.
     * @param connections コネクションリスト.
     */
    protected AbstractServiceContainer(ServiceProvider defaultProvider,
                                       List<AbstractServiceConnection> connections) {
        this.defaultProvider = defaultProvider;
        this.connections = connections;
        this.uuid = randomUUID();
    }

    @Override
    public void close() {
        final List<AbstractServiceConnection> copied;
        try (Guard g = readGuard()) {
            copied = new ArrayList<>(connections);
        }
        closeAll(copied);
        copied.clear();
    }

    @Override
    public final UUID id() {
        return uuid;
    }

    @Override
    public final ServiceConnection registerService(Object service) throws IllegalArgumentException {
        return registerService(getAnnotatedPriority(service), service);
    }

    @Override
    public final ServiceConnection registerService(Object service, Tag tag) throws IllegalArgumentException {
        return registerService(getAnnotatedPriority(service), service, tag);
    }

    @Override
    public final ServiceConnection registerService(int priority, Object service) throws IllegalArgumentException {
        return registerService(priority, service, Tag.NONE);
    }

    @Override
    @SuppressWarnings("unused")
    public final ServiceConnection registerService(int priority, Object service, Tag tag) throws IllegalArgumentException {
        nonNull(service, "service");

        /*
         登録時は同期をとる.
         */
        try (Guard rg = readGuard()) {
            ServiceConnection connection = null;
            final AbstractServiceConnection found = getAndUpdate(service, priority, tag);
            if (found != null) {
                connection = found;
            }

            try (Guard ug = new ReverseGuard(rg, true);
                 Guard wg = writeGuard()) {
                if (connection == null) {
                    connection = internalRegisterService(priority, service, tag);
                }
                sortConnections();
                return connection;
            }
        }
    }

    @Override
    public final <T> T resolveService(Class<T> serviceType) {
        return resolveService(serviceType, Tag.ANY);
    }

    @Override
    public final <T> T resolveService(Class<T> serviceType, Tag tag) {
        nonNull(serviceType, "serviceType");
        nonNull(tag, "tag");

        /*
         CopyOnWriteArrayList を利用し、厳密な同期は不要とする
         */
        T service = findFirst(serviceType, tag);
        if (service != null) {
            return service;
        }
        else {
            return resolveDefault(serviceType, tag);
        }
    }

    @Override
    public final <T> Collection<T> resolveServices(Class<T> serviceType) {
        return resolveServices(serviceType, Tag.ANY);
    }

    @Override
    public final <T> Collection<T> resolveServices(Class<T> serviceType, Tag tag) {
        nonNull(serviceType, "serviceType");
        nonNull(tag, "tag");

        /*
         CopyOnWriteArrayList を利用し、厳密な同期は不要とする
         */
        Set<T> result = find(serviceType, tag);
        resolveDefault(result, serviceType, tag);
        return unmodifiableSet(result);
    }

    /**
     *
     * @return
     */
    public ThreadSafeServiceContainer toThreadSafe() {
        return new ThreadSafeServiceContainer(this.defaultProvider, new ArrayList<>(this.connections));
    }

    private void sortConnections() {
        AbstractServiceConnection[] a = new AbstractServiceConnection[connections.size()];
        connections.toArray(a);
        Arrays.sort(a, PriorityComparator.INSTANCE);
        this.connections.clear();
        this.connections.addAll(Arrays.asList(a));
    }

    /**
     *
     * @param priority
     * @param service
     * @param tag
     * @return
     */
    @ReturnNonNull
    protected AbstractServiceConnection createConnection(int priority, Object service, Tag tag) {
        if (service instanceof ServiceComponent) {
            return new ComponentServiceConnection((ServiceComponent<?>) service, priority, this, tag);
        }
        if (service instanceof Holder) {
            return new ProxyServiceConnection((Holder<?>) service, priority, this, tag);
        }
        else {
            return new SimpleServiceConnection(service, priority, this, tag);
        }
    }

    /**
     *
     * @param connection コネクション.
     */
    protected void disconnect(AbstractServiceConnection connection) {
        try (Guard g = writeGuard()) {
            connections.remove(connection);
        }
    }

    /**
     *
     * @param <T>
     * @param serviceType
     * @param tag
     * @return
     */
    protected <T> Set<T> find(Class<T> serviceType, Tag tag) {
        Set<T> result = new HashSet<>();
        for (AbstractServiceConnection conn : connections) {
            if (conn.contains(serviceType, tag)) {
                result.add(conn.getService(serviceType, tag));
            }
        }
        return result;
    }

    /**
     *
     * @param <T>
     * @param serviceType
     * @param tag
     * @return
     */
    protected <T> T findFirst(Class<T> serviceType, Tag tag) {
        for (AbstractServiceConnection conn : connections) {
            if (conn.contains(serviceType, tag)) {
                return conn.getService(serviceType, tag);
            }
        }
        return null;
    }

    /**
     *
     * @param service
     * @param priority
     * @param tag
     * @return
     */
    protected AbstractServiceConnection getAndUpdate(Object service, int priority, Tag tag) {
        for (AbstractServiceConnection conn : connections) {
            if (conn.getTag().equals(tag) && conn.isSameInstance(service)) {
                conn.setPriority(priority);
                return conn;
            }
        }
        return null;
    }

    /**
     *
     * @param obj
     * @return
     */
    protected int getAnnotatedPriority(Object obj) {
        if (obj != null) {
            ServicePriority pa = AnnotationUtils.findAnnotation(obj.getClass(), ServicePriority.class);
            if (pa != null) {
                return pa.value();
            }
            else {
                return OrderUtils.getOrder(obj.getClass(), 1);
            }
        }
        return 1;
    }

    /**
     *
     * @param priority
     * @param service
     * @param tag
     * @return
     */
    @ReturnNonNull
    protected ServiceConnection internalRegisterService(int priority, Object service, Tag tag) {
        final AbstractServiceConnection conn = createConnection(priority, service, tag);
        addIfNotFound(connections, conn);
        return conn;
    }

    /**
     *
     * @return
     */
    @ReturnNonNull
    protected abstract Guard readGuard();

    /**
     *
     * @param <T>
     * @param serviceType
     * @param tag
     * @return
     */
    protected <T> T resolveDefault(Class<T> serviceType, Tag tag) {
        if (defaultProvider != null) {
            return defaultProvider.resolveService(serviceType, tag);
        }
        else {
            return null;
        }
    }

    /**
     *
     * @param <T>
     * @param result
     * @param serviceType
     * @param tag
     * @return
     */
    protected <T> Set<T> resolveDefault(Set<T> result, Class<T> serviceType, Tag tag) {
        if (defaultProvider != null) {
            result.addAll(defaultProvider.resolveServices(serviceType, tag));
        }
        return result;
    }

    /**
     *
     * @return
     */
    @ReturnNonNull
    protected abstract Guard writeGuard();

}

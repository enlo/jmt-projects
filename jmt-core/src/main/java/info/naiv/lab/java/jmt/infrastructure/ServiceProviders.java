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

import java.util.Collection;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class ServiceProviders {

    static final ServiceContainer systemContainer = new SystemServiceContainer();

    static final ThreadLocal<ServiceContainer> threadLocalContainer = new InheritableThreadLocal<ServiceContainer>() {
        @Override
        protected ServiceContainer childValue(ServiceContainer parentValue) {
            return new ThreadSafeServiceContainer(parentValue);
        }

        @Override
        protected ServiceContainer initialValue() {
            return new ThreadSafeServiceContainer(getSystemContainer());
        }
    };

    /**
     *
     * @return システムのサービスコンテナー
     */
    @Nonnull
    public static ServiceContainer getSystemContainer() {
        return systemContainer;
    }

    /**
     *
     * @return 現在のスレッドのサービスコンテナー
     */
    @Nonnull
    public static ServiceContainer getThreadContainer() {
        return threadLocalContainer.get();
    }

    /**
     *
     * @return
     */
    @Nonnull
    public static ServiceContainer newServiceContainer() {
        return new SimpleServiceContainer(getSystemContainer());
    }

    /**
     * スレッドコンテナーからサービスを取得. <br>
     * タグオブジェクトは{@link Tag#ANY}を使用する.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @return サービスオブジェクト. 見つからない場合は null.
     */
    public static <T> T resolveService(Class<T> serviceType) {
        return getThreadContainer().resolveService(serviceType);
    }

    /**
     * スレッドコンテナーからサービスを取得.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @param tag タグオブジェクト.
     * @return the T
     */
    static public <T> T resolveService(Class<T> serviceType, Tag tag) {
        return getThreadContainer().resolveService(serviceType, tag);
    }

    /**
     * スレッドコンテナーからサービスを取得. <br>
     * 複数一致するものがある場合、一致するものをすべて戻す. <br>
     * タグオブジェクトは{@link Tag#ANY}を使用する.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @return サービスオブジェクトコレクション. 見つからない場合は null.
     */
    @Nonnull
    public static <T> Collection<T> resolveServices(Class<T> serviceType) {
        return getThreadContainer().resolveServices(serviceType);
    }

    /**
     * スレッドコンテナーからサービスを取得. 複数一致するものがある場合、一致するものをすべて戻す.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @param tag タグオブジェクト.
     * @return the java.util.Collection
     */
    @Nonnull
    public static <T> Collection<T> resolveServices(Class<T> serviceType, Tag tag) {
        return getThreadContainer().resolveServices(serviceType, tag);
    }

    /**
     * スレッドのサービスコンテナーを設定.
     *
     * @param container サービスコンテナー
     * @return 今まで設定されていたコンテナー.
     */
    @Nonnull
    public static ServiceContainer setThreadContainer(ServiceContainer container) {
        ServiceContainer current = threadLocalContainer.get();
        if (container == null) {
            threadLocalContainer.remove();
        }
        else {
            threadLocalContainer.set(container);
        }
        return current;
    }

    private ServiceProviders() {
    }
}

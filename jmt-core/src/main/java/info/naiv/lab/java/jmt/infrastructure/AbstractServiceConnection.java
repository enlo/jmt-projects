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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * サービスコンテナーとのコネクション.
 *
 * @author enlo
 */
public abstract class AbstractServiceConnection implements ServiceConnection {

    final AtomicBoolean closed = new AtomicBoolean(false);
    final Reference<AbstractServiceContainer> container;
    final AtomicInteger priority;
    final Tag tag;

    /**
     * コンストラクター
     *
     * @param priority 優先度.
     * @param container コンテナー
     * @param tag タグオブジェクト.
     */
    public AbstractServiceConnection(int priority, AbstractServiceContainer container, Tag tag) {
        this.priority = new AtomicInteger(priority);
        this.container = new WeakReference<>(container);
        this.tag = tag;
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            AbstractServiceContainer cx = container.get();
            if (cx != null) {
                cx.disconnect(this);
                container.clear();
            }
        }
    }

    @Override
    public ServiceContainer getContainer() {
        ServiceContainer c = this.container.get();
        if (closed.get() || c == null) {
            throw new IllegalStateException("connection closed");
        }
        return c;
    }

    @Override
    public int getPriority() {
        return this.priority.get();
    }

    @Override
    public Tag getTag() {
        return tag;
    }

    @Override
    public  boolean isClosed() {
        return closed.get() || this.container.get() == null;
    }

    /**
     * 対象のサービス、タグと一致するかどうかチェック.
     *
     * @param serviceType サービスタイプ
     * @param tag タグ
     * @return 一致する場合は true.
     */
    protected  boolean contains(Class<?> serviceType, Tag tag) {
        return this.tag.contains(tag) && isAssignableTo(serviceType);
    }

    /**
     * サービスの取得.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスの型.
     * @param tag the value of tag
     * @return the T
     */
    protected abstract <T> T getService(Class<T> serviceType, Tag tag);

    /**
     * サービスが代入可能かどうか.
     *
     * @param clazz 型
     * @return 代入
     */
    protected abstract boolean isAssignableTo(Class<?> clazz);

    /**
     * オブジェクトの同一性をチェック.
     *
     * @param object チェックするオブジェクト.
     * @return 同一なら true.
     */
    protected abstract boolean isSameInstance(Object object);

    /**
     *
     * @param priority 優先度の設定.
     */
    protected void setPriority(int priority) {
        this.priority.set(priority);
    }
}

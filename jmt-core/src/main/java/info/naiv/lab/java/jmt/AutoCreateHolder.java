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
package info.naiv.lab.java.jmt;

import static info.naiv.lab.java.jmt.Misc.nop;
import info.naiv.lab.java.jmt.closeable.ACS;
import static info.naiv.lab.java.jmt.closeable.Closeables.lock;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * 値が null の場合に後からインスタンスを生成する {@link Holder }.
 *
 * @author enlo
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true, exclude = "rwl")
public class AutoCreateHolder<T> extends MutableHolder<T> {

    private final Function1<Class<? extends T>, T> creator;
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    /**
     * コンストラクタ. <br> {@link Class#newInstance() } を使用して値を作成する.
     *
     * @param clazz 値の型.
     */
    public AutoCreateHolder(@Nonnull Class<? extends T> clazz) {
        this(null, clazz, StandardFunctions.<T>newInstance(clazz));
    }

    /**
     * コンストラクタ. <br>
     * creator を使用して値を作成する.
     *
     * @param clazz 値の型.
     * @param creator インスタンス生成器.
     */
    public AutoCreateHolder(@Nonnull Class<? extends T> clazz, @Nonnull Function1<Class<? extends T>, T> creator) {
        this(null, clazz, creator);
    }

    /**
     * コンストラクタ. <br>
     * 初期値と、creator を設定する.
     *
     * @param object 初期値.
     * @param clazz 値の型.
     * @param creator インスタンス生成器.
     */
    public AutoCreateHolder(T object, @NonNull Class<? extends T> clazz, @NonNull Function1<Class<? extends T>, T> creator) {
        super(object, clazz);
        this.creator = creator;
    }

    @Override
    public T getContent() {
        T content;
        try (ACS<?> unused = lock(rwl.readLock())) {
            nop(unused);
            content = super.getContent();
        }
        if (content == null) {
            try (ACS<?> unused = lock(rwl.writeLock())) {
                nop(unused);
                content = super.getContent();
                if (content == null) {
                    content = createInstance();
                    super.setContent(content);
                }
            }
        }
        return content;
    }

    @Override
    public void setContent(T object) {
        try (ACS<?> unused = lock(rwl.writeLock())) {
            nop(unused);
            super.setContent(object);
        }
    }

    /**
     * インスタンス生成.
     *
     * @return 生成したインスタンス.
     */
    @Nonnull
    protected T createInstance() {
        return creator.apply(getContentType());
    }

}

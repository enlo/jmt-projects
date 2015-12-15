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

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Misc.nop;
import info.naiv.lab.java.jmt.closeable.ACS;
import static info.naiv.lab.java.jmt.closeable.Closeables.lock;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author enlo
 * @param <T>
 */
public class AutoCreateHolder<T> extends MutableHolder<T> implements Holder<T> {

    private final Function1<T, Class<? extends T>> creator;
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    public AutoCreateHolder(Class<? extends T> clazz) {
        this(null, clazz, StandardFunctions.newInstance(clazz));
    }

    public AutoCreateHolder(Class<? extends T> clazz, Function1<T, Class<? extends T>> creator) {
        this(null, clazz, creator);
    }

    public AutoCreateHolder(T object, Class<? extends T> clazz, Function1<T, Class<? extends T>> creator) {
        super(object, nonNull(clazz, "clazz"));
        this.creator = nonNull(creator, "creator");
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

    protected T createInstance() {
        return creator.apply(getContentType());
    }
}

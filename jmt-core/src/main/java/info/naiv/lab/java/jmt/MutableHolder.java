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

import static info.naiv.lab.java.jmt.Arguments.isInstanceOf;
import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static java.util.Objects.requireNonNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author enlo
 * @param <T>
 */
@EqualsAndHashCode
@ToString
public class MutableHolder<T> implements Holder<T> {

    private final Class<? extends T> clazz;
    private T object;

    @SuppressWarnings("unchecked")
    public MutableHolder(T object) {
        nonNull(object, "object");
        this.object = object;
        this.clazz = (Class<? extends T>) object.getClass();
    }

    public MutableHolder(T object, Class<? extends T> clazz) {
        this.object = object;
        this.clazz = clazz;
    }

    @Override
    public T getContent() {
        return object;
    }

    @Override
    public Class<? extends T> getContentType() {
        return requireNonNull(clazz, "class is must be non null.");
    }

    public void setContent(T object) {
        this.object = isInstanceOf(object, getContentType(), "object");
    }
}

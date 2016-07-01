/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author enlo
 * @param <T>
 */
public class ThreadLocalStack<T> {

    private final ThreadLocal<Deque<T>> tls = new ThreadLocal<Deque<T>>() {

        @Override
        protected Deque<T> initialValue() {
            return new ArrayDeque<>();
        }

    };

    /**
     * スタックの先頭要素を取得する.
     *
     * @return
     */
    public T getTop() {
        return tls.get().peekFirst();
    }

    /**
     * スタックが空かどうかを判断する.
     *
     * @return
     */
    public boolean isEmpty() {
        return tls.get().isEmpty();
    }

    /**
     * スタック先頭から要素を取り除く.
     *
     * @return
     */
    public T pop() {
        return tls.get().removeFirst();
    }

    /**
     * スタック先頭に要素を追加する.
     *
     * @param value
     */
    public void push(T value) {
        tls.get().addFirst(value);
    }

}

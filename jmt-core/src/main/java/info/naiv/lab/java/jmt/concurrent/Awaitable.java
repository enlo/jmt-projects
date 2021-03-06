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
package info.naiv.lab.java.jmt.concurrent;

import java.util.concurrent.TimeUnit;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
 * @author enlo
 */
@ThreadSafe
public interface Awaitable {

    /**
     *
     * @return 最後に発生したエラー
     */
    @CheckReturnValue
    public Exception lastException();

    /**
     * 待機.
     *
     * @return 例外なく終了した場合は true.
     */
    public boolean await();

    /**
     * 待機.
     *
     * @param timeout タイムアウトまでの時間.
     * @param unit ユニット.
     * @return 例外なく終了した場合は true. タイムアウトや割り込み等の例外が発生していれば false.
     */
    public boolean await(long timeout, @Nonnull TimeUnit unit);
}

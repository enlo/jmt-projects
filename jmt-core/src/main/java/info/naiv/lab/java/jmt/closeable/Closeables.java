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
package info.naiv.lab.java.jmt.closeable;

import info.naiv.lab.java.jmt.ImmutableHolder;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Closeables {
    
    public static void nonThrowClose(AutoCloseable object) {
        Exception e = close(object);
        if (e != null) {
            logger.trace("close failed.", e);
        }
    }

    /**
     * close.
     *
     * @see AutoCloseable#close()
     * @param object
     * @return Exception. close が失敗した場合.
     */
    public static Exception close(AutoCloseable object) {
        try {
            object.close();
            return null;
        }
        catch (Exception e) {
            return e;
        }
    }
    
    @SuppressWarnings("ThrowableResultIgnored")
    public static <T extends AutoCloseable> Map<T, Exception> closeAll(Iterable<T> list) {
        Map<T, Exception> result = new HashMap<>();
        for (T obj : list) {
            Exception th = close(obj);
            if (th != null) {
                result.put(obj, th);
            }
        }
        return result;
    }

    /**
     * {@link CloseableLock#lock} を呼び出す.
     *
     * @see CloseableLock#lock
     * @param lock ロックオブジェクト
     * @return {@link CloseableLock}
     */
    @ReturnNonNull
    public static CloseableLock lock(Lock lock) {
        return CloseableLock.lock(lock);
    }

    /**
     * ロック. ロックオブジェクトが null の場合は {@link DummyCloseable}を戻す.
     *
     * @param lock ロックオブジェクト
     * @param nullable lock の null を許容するか.
     * @return 自動クローズ可能な Lock.
     */
    @ReturnNonNull
    public static ACS<Lock> lock(Lock lock, boolean nullable) {
        if (nullable && lock == null) {
            return new DummyCloseable<>(lock, Lock.class);
        }
        else {
            return lock(lock);
        }
    }
    
    @ReturnNonNull
    public static <T> ACS<T> of(final T object) {
        if (object == null) {
            return new DummyCloseable<>(object, null);
        }
        else if (object instanceof AutoCloseable) {
            return new ACSHelper<T>(object) {
                @Override
                public void close() {
                    Exception ex = Closeables.close((AutoCloseable) object);
                    logger.debug("close throws exception.", ex);
                }
            };
        }
        else {
            try {
                return new DelegatingAutoCloseable<>(object);
            }
            catch (IllegalArgumentException e) {
                logger.debug("object is not closeable {}", e);
                return new DummyCloseable<>(object);
            }
        }
    }
    
    @ReturnNonNull
    public static <T> ACS<T> of(T object, Consumer1<T> closeMethod) {
        return new DelegatingAutoCloseable<>(object, closeMethod);
    }
    
    private Closeables() {
    }
    
    private static abstract class ACSHelper<T> extends ImmutableHolder<T> implements ACS<T> {
        
        ACSHelper(T object) {
            super(object);
        }
    }
}

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
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public abstract class AbstractAwaitable implements Awaitable {

    private static final ThreadLocal<Exception> LAST_EXCEPTION = new ThreadLocal<>();

    @Override
    public final boolean await() {
        try {
            LAST_EXCEPTION.set(null);
            return doAwait();
        }
        catch (Exception ex) {
            LAST_EXCEPTION.set(ex);
            logger.warn("failed on await. {} ", ex.getMessage());
            return false;
        }
    }

    @Override
    public final boolean await(long timeout, @NonNull TimeUnit unit) {
        try {
            LAST_EXCEPTION.set(null);
            return doAwait(timeout, unit);
        }
        catch (Exception ex) {
            LAST_EXCEPTION.set(ex);
            logger.warn("failed on await. (timeout={}, unit={}) {} ", timeout, unit, ex.getMessage());
            return false;
        }
    }

    @Override
    public final Exception lastException() {
        return LAST_EXCEPTION.get();
    }

    /**
     *
     * @return @throws Exception
     */
    protected abstract boolean doAwait() throws Exception;

    /**
     *
     * @param timeout
     * @param unit
     * @return
     * @throws Exception
     */
    protected abstract boolean doAwait(long timeout, @Nonnull TimeUnit unit) throws Exception;

}

/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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

import java.util.concurrent.atomic.AtomicInteger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author enlo
 */
@EqualsAndHashCode
@ToString
public class Initializer implements Runnable {

    private static final int INITIALIZED = 2;
    private static final int INITIALIZING = 1;
    private static final int UNINITIALIZED = 0;

    private final AtomicInteger state = new AtomicInteger(UNINITIALIZED);

    @Override
    public final void run() {
        if (enterInitializePhase()) {
            try {
                doInitialize();
                setInitialized();
            }
            catch (Throwable e) {
                state.set(UNINITIALIZED);
                throw e;
            }
        }
        spinWaitForInitialized();
    }

    public final boolean isInitialized() {
        return state.get() == INITIALIZED;
    }

    public final void runInitializer(Runnable initializer) {
        if (enterInitializePhase()) {
            try {
                initializer.run();
                setInitialized();
            }
            catch (Throwable e) {
                state.set(UNINITIALIZED);
                throw e;
            }
        }
        spinWaitForInitialized();
    }

    public final void setInitialized() {
        state.set(INITIALIZED);
    }

    protected void doInitialize() {
    }

    protected final boolean enterInitializePhase() {
        return state.compareAndSet(UNINITIALIZED, INITIALIZING);
    }

    @SuppressWarnings("CallToThreadYield")
    protected final void spinWaitForInitialized() throws IllegalStateException {
        while (state.get() < INITIALIZED) {
            Thread.yield();
            if (state.get() == UNINITIALIZED) {
                throw new IllegalStateException("Initialization failed.");
            }
        }
    }
}

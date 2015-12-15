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

import info.naiv.lab.java.jmt.mark.ReturnNonNull;

/**
 *
 * @author enlo
 */
public class Guard implements ACS<Guard> {

    public static final Guard EMPTY = new Guard();
    
    @Override
    public final void close() throws RuntimeException {
        leave();
    }

    public void enter() {
    }

    @Override
    @ReturnNonNull
    public final Guard getContent() {
        return this;
    }

    @Override
    public final Class<? extends Guard> getContentType() {
        return this.getClass();
    }

    public void leave() {
    }

    /**
     * 逆ガード
     * 
     * @param doEnter enter するかどうか
     * @return 
     */
    public Guard reverse(boolean doEnter) {
        return new ReverseGuard(this, doEnter);
    }
}

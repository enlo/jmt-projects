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
package info.naiv.lab.java.jmt.template.mvel;

import info.naiv.lab.java.jmt.ThreadLocalStack;
import javax.annotation.CheckForNull;
import org.mvel2.ParserContext;

/**
 *
 * @author enlo
 */
public class ParserContextHolder {

    private static final ThreadLocalStack<ParserContext> stack = new ThreadLocalStack<>();

    /**
     * ParserContext 取得.
     *
     * @return
     */
    @CheckForNull
    public static ParserContext get() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.getTop();
    }

    /**
     * ParserContext を戻す
     */
    public static void pop() {
        stack.pop();
    }

    /**
     * ParserContext を保存.
     *
     * @param ctx
     */
    public static void push(ParserContext ctx) {
        stack.push(ctx);
    }

    private ParserContextHolder() {
    }
}

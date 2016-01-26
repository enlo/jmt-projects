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
package info.naiv.lab.java.jmt.jdbc.sql.template.mvel.node;

import info.naiv.lab.java.jmt.Joiner;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class TemplateOutputStreamJoiner<T> extends Joiner<T, TemplateOutputStream> {

    private final Adder<T, TemplateOutputStream> FIRST_ADDR = new Adder<T, TemplateOutputStream>() {

        @Override
        public TemplateOutputStream add(TemplateOutputStream obj, T value, int idx) {
            return append(obj, value);
        }
    };
    private final Adder<T, TemplateOutputStream> MORE_ADDR = new Adder<T, TemplateOutputStream>() {

        @Override
        public TemplateOutputStream add(TemplateOutputStream obj, T value, int idx) {
            return append(obj.append(", "), value);
        }
    };

    TemplateOutputStream output;

    public TemplateOutputStreamJoiner(TemplateOutputStream output) {
        this.output = output;
    }

    protected abstract TemplateOutputStream append(TemplateOutputStream output, T value);

    protected TemplateOutputStream appendFirst(TemplateOutputStream output, T value) {
        return append(output, value);
    }

    protected TemplateOutputStream appendMore(TemplateOutputStream output, T value) {
        return append(output, value);
    }

    @Override
    protected TemplateOutputStream createResult() {
        return output;
    }

    @Override
    protected Adder<T, TemplateOutputStream> getFirst() {
        return FIRST_ADDR;
    }

    @Override
    protected Adder<T, TemplateOutputStream> getMore() {
        return MORE_ADDR;
    }

}

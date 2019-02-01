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
package info.naiv.lab.java.jmt.tquery.command;

/**
 *
 * @author enlo
 */
public class CommandParametersBuilder {

    static final int DEFAULT_CAPACITY = 20;

    final CommandParameters impl;

    public CommandParametersBuilder() {
        this(DEFAULT_CAPACITY);
    }

    public CommandParametersBuilder(int capacity) {
        impl = new DefaultCommandParameters(capacity);
    }

    public CommandParametersBuilder add(CommandParameter cp) {
        impl.add(cp);
        return this;
    }
    
    public CommandParametersBuilder addCapacity(int capacity) {
        impl.addCapacity(capacity);
        return this;
    }

    public CommandParametersBuilder addNamedValue(String name, Object value) {
        impl.addNamedValue(name, value);
        return this;
    }

    public CommandParametersBuilder addValue(Object value) {
        impl.addValue(value);
        return this;
    }

    public CommandParametersBuilder addValueWithPrefix(String prefix, Object value) {
        impl.addValueWithPrefix(prefix, value);
        return this;
    }

    /**
     * コマンドパラメーターリストを生成する.
     *
     * @return コマンドパラメーターリスト
     */
    public CommandParameters build() {
        return new DefaultCommandParameters(impl);
    }


    /**
     * コマンドパラメーター配列を生成する.
     *
     * @return コマンドパラメーターリスト
     */
    public CommandParameter[] toArray() {
        return impl.toArray();
    }
    /**
     * コマンドパラメーターリストを生成する.
     *
     * @return コマンドパラメーターリスト
     */
    public CommandParameters toList() {
        return build();
    }

}

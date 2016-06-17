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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author enlo
 */
public class CommandParameters extends ArrayList<CommandParameter> implements Cloneable, Serializable {

    public CommandParameters() {
    }

    public CommandParameters(Collection<? extends CommandParameter> c) {
        super(c);
    }

    public CommandParameters(int initialCapacity) {
        super(initialCapacity);
    }

    public void addCapacity(int capacity) {
        ensureCapacity(size() + capacity);
    }

    public String addNamedValue(String name, Object value) {
        CommandParameter p = new CommandParameter(name, value);
        add(p);
        return p.getKey();
    }

    public String addValue(Object value) {
        int index = size();
        CommandParameter p = new CommandParameter(index, value);
        add(p);
        return p.getKey();
    }

    public String addValueWithPrefix(String prefix, Object value) {
        int index = size();
        String name = prefix + index;
        return addNamedValue(name, value);
    }

    public static CommandParametersBuilder builder() {
        return new CommandParametersBuilder();
    }

    public static CommandParametersBuilder builder(Object... args) {
        CommandParametersBuilder builder = new CommandParametersBuilder(args.length);
        for (Object arg : args) {
            builder.addValue(arg);
        }
        return builder;
    }

    @Override
    public CommandParameter[] toArray() {
        CommandParameter[] result = new CommandParameter[size()];
        return super.toArray(result);
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public CommandParameters clone() {
        return (CommandParameters) super.clone();
    }

    /**
     * コマンドパラメーターをマップに変換する.
     *
     * @return コマンドパラメーターリスト
     */
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>(size());
        for (CommandParameter p : this) {
            result.put(p.getKey(), p.getValue());
        }
        return result;
    }
}

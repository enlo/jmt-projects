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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author enlo
 */
public class DefaultCommandParameters extends ArrayList<CommandParameter> implements CommandParameters {

    private static final long serialVersionUID = -8025210291274076721L;

    public static CommandParametersBuilder builder() {
        return new CommandParametersBuilder();
    }

    public static CommandParametersBuilder builder(Object... args) {
        CommandParametersBuilder builder = new CommandParametersBuilder(args.length);
        for (Object arg : args) {
            if (arg instanceof CommandParameter) {
                builder.add((CommandParameter) arg);
            }
            else {
                builder.addValue(arg);
            }
        }
        return builder;
    }

    public DefaultCommandParameters() {
    }

    public DefaultCommandParameters(Collection<? extends CommandParameter> c) {
        super(c);
    }

    public DefaultCommandParameters(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public void addCapacity(int capacity) {
        ensureCapacity(size() + capacity);
    }

    @Override
    public String addNamedValue(String name, Object value) {
        CommandParameter p = new CommandParameter(name, value);
        add(p);
        return p.getKey();
    }

    @Override
    public String addNamedValue(String name, Object value, Object typeHint) {
        CommandParameter p = new CommandParameter(name, value, typeHint);
        add(p);
        return p.getKey();
    }

    @Override
    public String addValue(Object value) {
        int index = size();
        CommandParameter p = new CommandParameter(index, value);
        add(p);
        return p.getKey();
    }

    @Override
    public String addValue(Object value, Object typeHint) {
        int index = size();
        CommandParameter p = new CommandParameter(index, value, typeHint);
        add(p);
        return p.getKey();
    }

    @Override
    public String addValueWithPrefix(String prefix, Object value) {
        int index = size();
        String name = prefix + index;
        return addNamedValue(name, value);
    }

    @Override
    public String addValueWithPrefix(String prefix, Object value, Object typeHint) {
        int index = size();
        String name = prefix + index;
        return addNamedValue(name, value, typeHint);
    }

    @Override
    public Object clone() {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommandParameters copy() {
        return new DefaultCommandParameters(this);
    }

    @Override
    public CommandParameter[] toArray() {
        CommandParameter[] result = new CommandParameter[size()];
        return super.toArray(result);
    }

    /**
     * コマンドパラメーターをマップに変換する.
     *
     * @return コマンドパラメーターリスト
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>(size());
        for (CommandParameter p : this) {
            result.put(p.getKey(), p.getValue());
        }
        return result;
    }

    @Override
    public Object[] toValueArray() {
        int sz = size();
        Object[] result = new Object[sz];
        for (int i = 0; i < sz; i++) {
            result[i] = get(i).getValue();
        }
        return result;
    }
}

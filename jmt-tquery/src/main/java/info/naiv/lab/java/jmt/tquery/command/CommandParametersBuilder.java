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

import info.naiv.lab.java.jmt.tquery.command.CommandParameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class CommandParametersBuilder implements Iterable<CommandParameter> {

    static final int DEFAULT_CAPACITY = 20;

    final ArrayList<CommandParameter> impl;

    public CommandParametersBuilder() {
        this(DEFAULT_CAPACITY);
    }

    public CommandParametersBuilder(int capacity) {
        impl = new ArrayList<>(capacity);
    }

    public CommandParametersBuilder(@NonNull List<CommandParameter> impl) {
        this.impl = new ArrayList<>(impl);
    }

    public void addCapacity(int capacity) {
        ensureCapacity(impl.size() + capacity);
    }

    public String addNamedValue(String name, Object value) {
        CommandParameter p = new CommandParameter(name, value);
        impl.add(p);
        return p.getKey();
    }

    public String addValue(Object value) {
        int index = impl.size();
        CommandParameter p = new CommandParameter(index, value);
        impl.add(p);
        return p.getKey();
    }

    public String addValueWithPrefix(String prefix, Object value) {
        int index = impl.size();
        String name = prefix + index;
        return addNamedValue(name, value);
    }

    public void ensureCapacity(int minCapacity) {
        impl.ensureCapacity(minCapacity);
    }

    @Override
    public Iterator<CommandParameter> iterator() {
        return impl.iterator();
    }

    public List<CommandParameter> toList() {
        return new ArrayList<>(impl);
    }
}

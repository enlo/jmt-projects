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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import lombok.SneakyThrows;

/**
 *
 * @author enlo
 */
public abstract class CommandParametersWithTypeHint implements CommandParameters {

    private static final long serialVersionUID = 1L;

    final Object defaultTypeHint;
    final CommandParameters impl;

    public CommandParametersWithTypeHint(CommandParameters impl, Object defaultTypeHint) {
        this.impl = impl;
        this.defaultTypeHint = defaultTypeHint;
    }

    @Override
    public boolean add(CommandParameter e) {
        return impl.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends CommandParameter> c) {
        return impl.addAll(c);
    }

    @Override
    public void addCapacity(int capacity) {
        impl.addCapacity(capacity);
    }

    @Override
    public String addNamedValue(String name, Object value) {
        return impl.addNamedValue(name, value, defaultTypeHint);
    }

    @Override
    public String addNamedValue(String name, Object value, Object typeHint) {
        return impl.addNamedValue(name, value, typeHint);
    }

    @Override
    public String addValue(Object value) {
        return impl.addValue(value, defaultTypeHint);
    }

    @Override
    public String addValue(Object value, Object typeHint) {
        return impl.addValue(value, typeHint);
    }

    @Override
    public String addValueWithPrefix(String prefix, Object value) {
        return impl.addValueWithPrefix(prefix, value, defaultTypeHint);
    }

    @Override
    public String addValueWithPrefix(String prefix, Object value, Object typeHint) {
        return impl.addValueWithPrefix(prefix, value, typeHint);
    }

    @Override
    public void clear() {
        impl.clear();
    }

    @Override
    public boolean contains(Object o) {
        return impl.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return impl.containsAll(c);
    }

    @Override
    public CommandParameters copy() {
        return new DefaultImpl(impl, defaultTypeHint);
    }

    @Override
    public CommandParameter get(int index) {
        return impl.get(index);
    }

    @Override
    public boolean isEmpty() {
        return impl.isEmpty();
    }

    @Override
    public Iterator<CommandParameter> iterator() {
        return impl.iterator();
    }

    @Override
    public boolean remove(Object o) {
        return impl.remove(o);
    }

    @Override
    public CommandParameter remove(int index) {
        return impl.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return impl.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return impl.retainAll(c);
    }

    @Override
    public CommandParameter set(int index, CommandParameter element) {
        return impl.set(index, element);
    }

    @Override
    public int size() {
        return impl.size();
    }

    @Override
    public CommandParameter[] toArray() {
        return impl.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return impl.toArray(a);
    }

    @Override
    public Map<String, Object> toMap() {
        return impl.toMap();
    }

    public static CommandParametersWithTypeHint wrap(CommandParameters base, Object typeHint) {
        return new DefaultImpl(base, typeHint);
    }

    static class DefaultImpl extends CommandParametersWithTypeHint {

        private static final long serialVersionUID = 1L;

        public DefaultImpl(CommandParameters impl, Object defaultTypeHint) {
            super(impl, defaultTypeHint);
        }

    }
}

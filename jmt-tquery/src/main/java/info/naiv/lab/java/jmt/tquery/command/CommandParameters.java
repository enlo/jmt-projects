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
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author enlo
 */
public interface CommandParameters extends Collection<CommandParameter>, Serializable {

    void addCapacity(int capacity);

    String addNamedValue(String name, Object value);

    String addNamedValue(String name, Object value, Object typeHint);

    String addValue(Object value);

    String addValue(Object value, Object typeHint);

    String addValueWithPrefix(String prefix, Object value);

    String addValueWithPrefix(String prefix, Object value, Object typeHint);

    CommandParameters copy();

    CommandParameter get(int index);

    CommandParameter remove(int index);

    CommandParameter set(int index, CommandParameter element);

    @Override
    CommandParameter[] toArray();

    /**
     * コマンドパラメーターをマップに変換する.
     *
     * @return コマンドパラメーターリスト
     */
    Map<String, Object> toMap();

    Object[] toValueArray();

}

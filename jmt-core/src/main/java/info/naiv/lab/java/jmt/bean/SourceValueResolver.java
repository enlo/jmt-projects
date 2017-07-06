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
package info.naiv.lab.java.jmt.bean;

import javax.annotation.CheckForNull;
import org.springframework.core.convert.TypeDescriptor;

/**
 *
 * @author enlo
 * @param <KeyType>
 */
public interface SourceValueResolver<KeyType> {

    /**
     * 対応する値が存在するかどうか.
     *
     * @param key
     * @return
     */
    boolean containsKey(KeyType key);

    /**
     * 既定値を取得.
     *
     * @param key
     * @param type 対象の型
     * @return
     */
    Object getDefaultValue(KeyType key, TypeDescriptor type);

    /**
     * 名称を取得.
     *
     * @param key
     * @return
     */
    String getName(KeyType key);

    /**
     * 値を取得.
     *
     * @param key
     * @return
     */
    Object getValue(KeyType key);

    /**
     * タイプ記述子を取得.
     *
     * @param key
     * @return
     */
    @CheckForNull
    TypeDescriptor getValueTypeDescriptor(KeyType key);

}

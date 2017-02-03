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
package info.naiv.lab.java.jmt.template;

import info.naiv.lab.java.jmt.collection.Lookup;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 * @param <TResult>
 */
public interface Template<TResult> extends Serializable {

    /**
     *
     * @return テンプレート名
     */
    String getName();

    /**
     * 引数無しでマージを行う.
     *
     * @return マージ結果.
     */
    TResult merge();

    /**
     * Java Bean を引数としてマージを行う.
     *
     * @param bean バインドに使用する Java Bean.
     * @return マージ結果.
     */
    TResult mergeBean(Object bean);

    /**
     * {@link Map} を引数としてマージを行う.
     *
     * @param <TArg> 値の型.
     * @param parameters パラメータ.
     * @return マージ結果.
     */
    <TArg> TResult merge(Map<String, TArg> parameters);

    /**
     * {@link Lookup} を引数としてマージを行う.
     *
     * @param <TArg> 値の型.
     * @param parameters パラメータ.
     * @return マージ結果.
     */
    <TArg> TResult merge(Lookup<String, TArg> parameters);

    /**
     *
     * @return テンプレートのもととなる文字列または、テンプレートの文字列表現
     */
    @Nonnull
    String getTemplateSource();

    /**
     *
     * @return テンプレートオブジェクト.
     */
    @Nonnull
    Object getTemplateObject();
}

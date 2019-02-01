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

import java.nio.charset.Charset;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * テンプレートローダー
 *
 * @author enlo
 * @param <TResult>
 */
public interface TemplateLoader<TResult> {

    /**
     *
     */
    static final String DEFAULT_SUFFIX = "";

    /**
     * 親のテンプレートローダーを設定.
     *
     * @param parentLoader 親のテンプレートローダー
     */
    void setParentTemplateLoader(TemplateLoader<? extends TResult> parentLoader);

    /**
     * 文字列からテンプレートオブジェクトを生成する.
     *
     * @param template テンプレート文字列
     * @return テンプレートオブジェクト
     */
    @Nonnull
    Template<TResult> fromString(String template);

    /**
     * 文字列からテンプレートオブジェクトを生成する.
     *
     * @param name テンプレート名
     * @param template テンプレート文字列
     * @return テンプレートオブジェクト
     */
    @Nonnull
    Template<TResult> fromString(String name, String template);

    @Nonnull
    Template<TResult> fromSourceResolver(TemplateSourceResolver sourceResolver);

    /**
     *
     * @return Suffix.
     */
    String getSuffix();

    /**
     *
     * テンプレートオブジェクトを読み込む.
     *
     * @param category
     * @param name
     * @return
     */
    @CheckForNull
    Template<TResult> load(String category, String name);

    /**
     *
     * テンプレートオブジェクトを読み込む.
     *
     * @param category
     * @param name
     * @param charset
     * @return
     */
    @CheckForNull
    Template<TResult> load(String category, String name, Charset charset);

    /**
     *
     * @param category
     * @return
     */
    @Nonnull
    Iterable<Template<TResult>> loadCategory(String category);

    /**
     *
     * @param category
     * @param charset
     * @return
     */
    @Nonnull
    Iterable<Template<TResult>> loadCategory(String category, Charset charset);

    /**
     * Oracle用、PostgreSql用など、読み込むSQLを分ける場合に使用する.
     *
     * @param suffix サフィックスを設定.
     */
    void setSuffix(String suffix);

    /**
     * リスナを追加.
     *
     * @param listener
     * @return
     */
    boolean addTemplateLoaderListener(@Nonnull TemplateLoaderListener listener);

    /**
     * リスナを削除.
     *
     * @param listener
     * @return
     */
    boolean removeTemplateLoaderListener(@Nonnull TemplateLoaderListener listener);
}

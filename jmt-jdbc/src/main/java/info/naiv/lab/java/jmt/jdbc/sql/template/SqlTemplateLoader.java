/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt.jdbc.sql.template;

import java.nio.charset.Charset;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public interface SqlTemplateLoader {

    /**
     *
     */
    static final String DEFAULT_SUFFIX = "";

    /**
     *
     * @param parentLoader
     */
    void setParent(SqlTemplateLoader parentLoader);

    /**
     *
     * @param template
     * @return
     */
    @Nonnull
    SqlTemplate fromString(String template);

    /**
     *
     * @param name
     * @param template
     * @return
     */
    @Nonnull
    SqlTemplate fromString(String name, String template);

    /**
     *
     * @return Suffix.
     */
    String getSuffix();

    /**
     *
     * @param category
     * @param name
     * @return
     */
    @Nonnull
    SqlTemplate load(String category, String name);

    /**
     *
     * @param category
     * @param name
     * @param charset
     * @return
     */
    @Nonnull
    SqlTemplate load(String category, String name, Charset charset);

    /**
     *
     * @param category
     * @return
     */
    @Nonnull
    Iterable<SqlTemplate> loadCategory(String category);

    /**
     *
     * @param category
     * @param charset
     * @return
     */
    @Nonnull
    Iterable<SqlTemplate> loadCategory(String category, Charset charset);

    /**
     * Oracle用、PostgreSql用など、読み込むSQLを分ける場合に使用する.
     *
     * @param suffix サフィックスを設定.
     */
    void setSuffix(String suffix);
}

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

import info.naiv.lab.java.jmt.jdbc.sql.Query;
import info.naiv.lab.java.jmt.jdbc.sql.SqlQuery;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * SqlTemplate
 *
 * @author enlo
 */
public interface SqlTemplate {

    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    @ReturnNonNull
    Query merge();

    /**
     * テンプレートから {@link SqlQuery } を作成する.<br>
     * パラメータとして、Map を渡す. <br>
     *
     * @param <T>
     * @param parameters
     * @return
     */
    @ReturnNonNull
    <T> Query merge(Map<String, T> parameters);

    /**
     * テンプレートから {@link SqlQuery } を作成する.<br>
     * パラメータとして、Map を渡す. <br>
     *
     * @param <T>
     * @param parameters
     * @return
     */
    @ReturnNonNull
    <T> Query mergeMap(Map<String, T> parameters);

    /**
     * テンプレートから {@link SqlQuery } を作成する.<br>
     * パラメータとして、パラメータソースを渡す. <br>
     *
     * @param parameters
     * @return
     */
    @ReturnNonNull
    Query mergeParameterSource(SqlParameterSource parameters);

    /**
     * テンプレートから {@link SqlQuery } を作成する.<br>
     * パラメータとして、パラメータソースを渡す. <br>
     *
     * @param parameters
     * @return
     */
    @ReturnNonNull
    Query merge(SqlParameterSource parameters);

    /**
     * テンプレートから {@link SqlQuery } を作成する.<br>
     * パラメータとして、bean, SqlParameterSource, Map を渡す. <br>
     *
     * @param bean.
     * @return クエリ.
     * @see BeanPropertySqlParameterSource
     */
    @ReturnNonNull
    Query merge(Object bean);
}

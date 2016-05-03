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
package info.naiv.lab.java.jmt.jdbc.sql;

import info.naiv.lab.java.jmt.jdbc.mapping.RowMapperFactory;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author enlo
 */
public interface Query {

    /**
     * バッチ更新
     *
     * @param jdbcOperations
     * @param batchValues
     * @return
     */
    int[] batchUpadate(JdbcOperations jdbcOperations, List<Object[]> batchValues);

    /**
     *
     * @param jdbcOperations
     * @param batchValues
     * @param columnTypes
     * @return
     */
    int[] batchUpadate(JdbcOperations jdbcOperations, final List<Object[]> batchValues, final int[] columnTypes);

    /**
     *
     * @param jdbcOperations
     * @param batchStatementSetter
     * @return
     */
    int[] batchUpadate(JdbcOperations jdbcOperations, final BatchPreparedStatementSetter batchStatementSetter);

    /**
     *
     * @param jdbcOperations
     */
    void execute(JdbcOperations jdbcOperations);

    /**
     *
     * @param <T>
     * @param jdbcOperations
     * @param action
     * @return
     */
    <T> T execute(JdbcOperations jdbcOperations, final PreparedStatementCallback<T> action);

    /**
     *
     * @return
     */
    int getFetchSize();

    /**
     *
     * @return
     */
    int getMaxRowSize();

    /**
     *
     * @param <T>
     * @param jdbcOperations
     * @param rowMapper
     * @return
     */
    <T> List<T> query(JdbcOperations jdbcOperations, RowMapper<T> rowMapper);

    /**
     *
     * @param <T>
     * @param jdbcOperations
     * @param rowMapperFactory
     * @return
     */
    <T> List<T> query(JdbcOperations jdbcOperations, RowMapperFactory<T> rowMapperFactory);

    /**
     *
     * @param <T>
     * @param jdbcOperations
     * @param resultSetExtractor
     * @return
     */
    <T> T query(JdbcOperations jdbcOperations, ResultSetExtractor<T> resultSetExtractor);

    /**
     * 単一の行を取得し、Bean に詰め替える.
     *
     * @param <T>
     * @param jdbcOperations
     * @param mappedClass
     * @return
     */
    <T> T queryForBean(JdbcOperations jdbcOperations, Class<T> mappedClass);

    /**
     * 複数行を取得し、Bean の List に詰め替える.
     *
     * @param <T>
     * @param jdbcOperations
     * @param mappedClass
     * @return
     */
    <T> List<T> queryForBeanList(JdbcOperations jdbcOperations, Class<T> mappedClass);

    /**
     * 単一行をMapで取得する.
     *
     * @param jdbcOperations
     * @return 1行のデータ。列名をKey、列の値をValueにセットする.
     */
    Map<String, Object> queryForMap(JdbcOperations jdbcOperations);

    /**
     * 複数行を Map の List として取得する.
     *
     * @param jdbcOperations
     * @return
     */
    List<Map<String, Object>> queryForMapList(JdbcOperations jdbcOperations);

    /**
     * 単一の値を取得する.
     *
     * @param <T>
     * @param jdbcOperations
     * @param rowMapper ResultSet から目的の値を取得するための RowMapper.
     * @return 単一の値
     */
    <T> T queryForObject(JdbcOperations jdbcOperations, RowMapper<T> rowMapper);

    /**
     * 単一の値を取得する.
     *
     * @param <T>
     * @param jdbcOperations
     * @param requiredType
     * @return 単一の値
     */
    <T> T queryForObject(JdbcOperations jdbcOperations, Class<T> requiredType);

    /**
     * 1列だけの結果を値のリストとして取得する.
     *
     * @param <T>
     * @param jdbcOperations
     * @param elementType
     * @return
     */
    <T> List<T> queryForObjectList(JdbcOperations jdbcOperations, Class<T> elementType);

    /**
     * クエリを実行し、RowSet を取得する.
     *
     * @param jdbcOperations
     * @return
     */
    SqlRowSet queryForRowSet(JdbcOperations jdbcOperations);

    /**
     * 引数を入れ替える. <br>
     * すでにマージされたSQLが変更されるわけではないので注意.
     *
     * @param newSetter
     * @return
     */
    Query rebind(PreparedStatementSetter newSetter);

    /**
     * 引数を入れ替える. <br>
     * すでにマージされたSQLが変更されるわけではないので注意.
     *
     * @param args
     * @return
     */
    Query rebind(List<?> args);

    /**
     * デバッグログを有効にするか.
     *
     * @param flag
     */
    void enableDebugLogging(boolean flag);

    /**
     * フェッチサイズを設定する.
     *
     * @param fetchSize
     */
    void setFetchSize(int fetchSize);

    /**
     * 取得する行数を設定する. 設定した行数以上は読み込まない.
     *
     * @param maxRowSize
     */
    void setMaxRowSize(int maxRowSize);

    /**
     * ページを設定する. 既存のSQLに対してページ指定のSQLを追加する.
     *
     * @param offset 開始行
     * @param size 取得する行数
     */
    void setPage(int offset, int size);

    /**
     * 追加・更新クエリを実行する.
     *
     * @param jdbcOperations
     * @return 影響を受けた行数
     */
    int update(JdbcOperations jdbcOperations);

    /**
     * 追加・更新クエリを実行する.
     *
     * @param jdbcOperations
     * @param keyHolder
     * @return 影響を受けた行数
     */
    int update(JdbcOperations jdbcOperations, KeyHolder keyHolder);
}

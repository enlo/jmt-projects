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

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author enlo
 */
public interface Query {

    /**
     *
     * @param <T>
     * @param jdbcOperations
     * @param batchValues
     * @return
     */
    <T> int[] batchUpadate(JdbcOperations jdbcOperations, List<Object[]> batchValues);

    <T> int[] batchUpadate(JdbcOperations jdbcOperations, final List<Object[]> batchValues, final int[] columnTypes);

    <T> int[] batchUpadate(JdbcOperations jdbcOperations, final BatchPreparedStatementSetter batchStatementSetter);

    void execute(JdbcOperations jdbcOperations);

    <T> void execute(JdbcOperations jdbcOperations, final PreparedStatementCallback<T> action);

    <T> List<T> query(JdbcOperations jdbcOperations, RowMapper<T> rowMapper);

    <T> List<T> query(JdbcOperations jdbcOperations, ResultSetExtractor<List<T>> resultSetExtractor);

    <T> List<T> queryForList(JdbcOperations jdbcOperations, Class<T> elementType);

    List<Map<String, Object>> queryForList(JdbcOperations jdbcOperations);

    Map<String, Object> queryForMap(JdbcOperations jdbcOperations);

    <T> T queryForObject(JdbcOperations jdbcOperations, RowMapper<T> rowMapper);

    <T> T queryForObject(JdbcOperations jdbcOperations, Class<T> requiredType);

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

}

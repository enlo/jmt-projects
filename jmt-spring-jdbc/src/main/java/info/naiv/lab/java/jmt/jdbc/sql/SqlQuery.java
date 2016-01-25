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
package info.naiv.lab.java.jmt.jdbc.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author enlo
 */
@Value
public class SqlQuery {

    PreparedStatementSetter setter;
    private final String sql;

    public SqlQuery(String sql, Object[] args) {
        this(new PreparedStatementCreatorFactory(sql), args);
    }

    public SqlQuery(String sql, List<?> args) {
        this(new PreparedStatementCreatorFactory(sql), args);
    }

    public SqlQuery(PreparedStatementCreatorFactory factory, List<?> args) {
        this.setter = factory.newPreparedStatementSetter(args);
        this.sql = ((SqlProvider) this.setter).getSql();
    }

    public SqlQuery(PreparedStatementCreatorFactory factory, Object[] args) {
        this.setter = factory.newPreparedStatementSetter(args);
        this.sql = ((SqlProvider) this.setter).getSql();
    }

    public SqlQuery(String sql, PreparedStatementSetter setter) {
        this.sql = sql;
        this.setter = setter;
    }

    public SqlQuery(String sql) {
        this(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
            }
        });
    }

    public <T> List<T> query(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper) {
        return query(jdbcTemplate, new RowMapperResultSetExtractor<>(rowMapper));
    }

    public <T> List<T> query(JdbcTemplate jdbcTemplate, ResultSetExtractor<List<T>> resultSetExtractor) {
        return jdbcTemplate.query(sql, setter, resultSetExtractor);
    }

    public <T> List<T> queryForList(JdbcTemplate jdbcTemplate, Class<T> elementType) {
        return jdbcTemplate.query(sql, setter, new SingleColumnRowMapper<>(elementType));
    }

    public List<Map<String, Object>> queryForList(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query(sql, setter, new ColumnMapRowMapper());
    }

    public Map<String, Object> queryForMap(JdbcTemplate jdbcTemplate) {
        return queryForObject(jdbcTemplate, new ColumnMapRowMapper());
    }

    public <T> T queryForObject(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper) {
        List<T> results = query(jdbcTemplate, new RowMapperResultSetExtractor<>(rowMapper, 1));
        return DataAccessUtils.requiredSingleResult(results);
    }

    public <T> T queryForObject(JdbcTemplate jdbcTemplate, Class<T> requiredType) {
        return queryForObject(jdbcTemplate, new SingleColumnRowMapper<>(requiredType));
    }

    public SqlRowSet queryForRowSet(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query(sql, setter, new SqlRowSetResultSetExtractor());
    }

    public SqlQuery rebind(PreparedStatementSetter newSetter) {
        return new SqlQuery(sql, newSetter);
    }
}

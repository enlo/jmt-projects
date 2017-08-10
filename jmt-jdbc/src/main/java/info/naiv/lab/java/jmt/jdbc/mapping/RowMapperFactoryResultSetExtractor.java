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
package info.naiv.lab.java.jmt.jdbc.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author enlo
 * @param <T>
 */
@Data
public class RowMapperFactoryResultSetExtractor<T>
        implements ResultSetExtractor<List<T>> {

    /**
     * 配列の初期サイズ
     */
    private int capacity = 0;
    /**
     * RowMapperFactory.
     */
    @NonNull
    private RowMapperFactory<T> rowMapperFactory;

    public RowMapperFactoryResultSetExtractor(RowMapperFactory<T> rowMapperFactory) {
        this.rowMapperFactory = rowMapperFactory;
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<T> result = (0 < capacity) ? new ArrayList<T>(capacity) : new ArrayList<T>();
        RowMapper<T> rowMapper = rowMapperFactory.createRowMapper(rs);
        int rowNum = 0;
        while (rs.next()) {
            T obj = rowMapper.mapRow(rs, rowNum++);
            result.add(obj);
        }
        return result;
    }

}

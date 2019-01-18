/*
 * The MIT License
 *
 * Copyright 2018 enlo.
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
package info.naiv.lab.java.jmt.jdbc.metadata;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.sql.DataSource;
import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.StringUtils;

/**
 *
 * @author enlo
 */
@Data
public class Table implements Serializable {

    private static final long serialVersionUID = 5356801277458353958L;

    public static Table fromDataSource(DataSource dataSource, final String schema, final String tablename) throws MetaDataAccessException {
        return (Table) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                                                     @Override
                                                     public Object processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {
                                                         return fromDatabaseMetadata(dbmd, schema, tablename);
                                                     }
                                                 });
    }

    public static Table fromDatabaseMetadata(DatabaseMetaData metaData, String schema, String tablename) throws SQLException {
        return new Table(tablename, Columns.fromDatabaseMetadata(metaData, schema, tablename));
    }

    public static Table fromJdbcTemplate(JdbcTemplate template, final String schema, final String tablename) {
        return template.execute(new ConnectionCallback<Table>() {
            @Override
            public Table doInConnection(Connection con) throws SQLException, DataAccessException {
                return fromDatabaseMetadata(con.getMetaData(), schema, tablename);
            }
        });
    }

    private List<Column> columns;
    private final String name;
    private final String originalName;

    public Table(@Nonnull String orginalTableName) {
        this(orginalTableName, new ArrayList<Column>());
    }

    public Table(@Nonnull String orginalTableName, @Nonnull List<Column> columns) {
        this.originalName = orginalTableName;
        String nm = JdbcUtils.convertUnderscoreNameToPropertyName(orginalTableName);
        this.name = StringUtils.capitalize(nm);
        this.columns = columns;
    }

    @CheckForNull
    public Column findColumn(String name) {
        return Columns.find(columns, name);
    }

    @Nonnull
    public Column[] getPrimaryKeys() {
        List<Column> pkey = new ArrayList<>();
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                pkey.add(column);
            }
        }
        Collections.sort(pkey, new Columns.PrimaryKeyComparator());
        return pkey.toArray(new Column[pkey.size()]);
    }

}

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

import info.naiv.lab.java.jmt.jdbc.JDBCTypeTraits;
import info.naiv.lab.java.jmt.jdbc.JdbcType;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author enlo
 */
public class Columns {
    
    public static List<Column> fromDatabaseMetadata(DatabaseMetaData meta, String schema, String tablename) throws SQLException {
        List<Column> columnlist = new ArrayList<>();
        try (ResultSet rs = meta.getColumns(null, schema, tablename, "%")) {
            while (rs.next()) {
                Column field = new Column(rs.getString("COLUMN_NAME"),
                                          getType(rs), rs.getString("TYPE_NAME"));
                int nullable = rs.getInt("NULLABLE");
                field.setNonNull(nullable == DatabaseMetaData.columnNoNulls);
                field.setSize(rs.getInt("COLUMN_SIZE"));
                field.setScale(rs.getInt("DECIMAL_DIGITS"));
                columnlist.add(field);
            }
        }
        if (!columnlist.isEmpty()) {
            try (ResultSet rs = meta.getPrimaryKeys(null, schema, tablename)) {
                while (rs.next()) {
                    String cname = rs.getString("COLUMN_NAME");
                    short seq = rs.getShort("KEY_SEQ");
                    for (Column field : columnlist) {
                        if (field.getOriginalName().equals(cname)) {
                            field.setPrimaryKey(true);
                            field.setPrimaryKeyIndex(seq);
                        }
                    }
                }
            }
        }
        return columnlist;
    }
    
    protected static JDBCTypeTraits getTypeTraits(final ResultSet rs) throws SQLException {
        int dataType = rs.getInt("DATA_TYPE");
        JDBCTypeTraits type = JDBCTypeTraits.valueOf(dataType);
        return type;
    }
    
    protected static Class getType(final ResultSet rs) throws SQLException {
        int dataType = rs.getInt("DATA_TYPE");
        JdbcType type = JdbcType.valueOf(dataType);
        return type.getMappedType();
    }
    
    public static class PrimaryKeyComparator implements Comparator<Column> {
        
        @Override
        public int compare(Column o1, Column o2) {
            int cx = o1.isPrimaryKey() ? o1.getPrimaryKeyIndex() : Integer.MAX_VALUE;
            int cy = o2.isPrimaryKey() ? o2.getPrimaryKeyIndex() : Integer.MAX_VALUE;
            return Integer.compare(cx, cy);
        }
        
    }
}

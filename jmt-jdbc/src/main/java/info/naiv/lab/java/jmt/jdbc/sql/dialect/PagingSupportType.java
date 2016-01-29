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
package info.naiv.lab.java.jmt.jdbc.sql.dialect;

import info.naiv.lab.java.jmt.MapBuilder;
import java.util.Map;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 * ページングのサポート.
 *
 * 参考資料の通り、ページングを使用するより、窓関数を使用したほうが高速に実行できるらしいので、<br>
 * 通常はデータベースエンジンごとに最適なSQLを書くようにすべき.
 *
 * @see http://use-the-index-luke.com/ja/sql/partial-results/fetch-next-page
 *
 * @author enlo
 */
public enum PagingSupportType {

    /**
     * SQL標準のサポート
     */
    SQLSTD(
            "@{originalSql} OFFSET @{offset} ROWS",
            "@{originalSql} OFFSET @{offset} ROWS FETCH NEXT @{rowSize} ROWS ONLY"),
    /**
     * MySQL, PostgreSQL 等
     */
    MYSQL_LIKE(
            "@{originalSql} LIMIT -1 OFFSET @{offset}",
            "@{originalSql} LIMIT @{rowSize} OFFSET @{offset}"),
    /**
     * Oracle11g 以前、SQL Server 2008 以前
     */
    ROWNUM_SUPPORT(
            "select * from ( select work_query0.*, @{dialect.rowNumber(\"\")} _row_number1"
            + " from( @{originalSql} ) work_query0) where _row_number1 > @{offset}",
            "select * from ( select work_query0.*, @{dialect.rowNumber(\"\")} _row_number1"
            + " from( @{originalSql} ) work_query0 where @{dialect.rowNumber(\"\")} <= @{rowSize + offset})"
            + " where _row_number1 > @{offset}"),
    /**
     * サポート無し. 例外を送出.
     */
    UNDEFINED("", "") {
                @Override
                public String modify(String sql, Dialect dialect, int offset, int rowSize) {
                    throw new UnsupportedOperationException("offset and rowsize not supported.");
                }
            },;

    final CompiledTemplate offsetOnly;
    final CompiledTemplate offsetAndSize;

    private PagingSupportType(String offsetOnly, String offsetAndSize) {
        this.offsetOnly = TemplateCompiler.compileTemplate(offsetOnly);
        this.offsetAndSize = TemplateCompiler.compileTemplate(offsetAndSize);
    }

    /**
     * SQLの修正.
     *
     * @param sql SQL
     * @param dialect 方言
     * @param offset オフセット
     * @param rowSize 1ページ当たりのサイズ
     * @return 結果
     */
    public String modify(String sql, Dialect dialect, int offset, int rowSize) {
        Map<String, Object> map = MapBuilder.<String, Object>hashMap("originalSql", sql)
                .put("dialect", dialect)
                .put("offset", offset)
                .put("rowSize", rowSize)
                .toMap();

        int off = 0 <= offset ? offset : 0;
        int size = 0 <= rowSize ? rowSize : -1;
        if (off == 0 && size == -1) {
            return sql;
        }
        else if (size == -1) {
            return (String) TemplateRuntime.execute(offsetOnly, map);
        }
        else {
            return (String) TemplateRuntime.execute(offsetAndSize, map);
        }
    }

}

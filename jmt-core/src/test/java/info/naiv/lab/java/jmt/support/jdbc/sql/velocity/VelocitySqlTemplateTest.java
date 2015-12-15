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
package info.naiv.lab.java.jmt.support.jdbc.sql.velocity;

import info.naiv.lab.java.jmt.support.jdbc.sql.SqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlQuery;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.IntegerSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.StringSqlParameter;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class VelocitySqlTemplateTest {

    public VelocitySqlTemplateTest() {
    }

    /**
     * Test of merge method, of class VelocityStringSqlTemplate.
     */
    @Test
    public void testMerge() {

        String template = "select * from $table where id = #jmtBind($id)";
        String table = "Emploee";
        String id = "12345";
        String expectedSql = "select * from Emploee where id = ?";

        Map<String, Object> map = new HashMap<>();
        map.put("table", table);
        map.put("id", id);

        VelocityStringSqlTemplate vst = new VelocityStringSqlTemplate(template);
        SqlQuery query = vst.merge(map);

        assertThat(query.getSql(), is(expectedSql));
        SqlParameter actual = query.getParameters().get(0);
        assertThat(actual, is(instanceOf(StringSqlParameter.class)));
        assertThat(actual.getName(), is("$id"));
        assertThat(actual.getValue(), is((Object) "12345"));
    }

    /**
     * Test of merge method, of class VelocityStringSqlTemplate.
     */
    @Test
    public void testMerge_2() {

        String template = "select * from Emploee where id in (#jmtBindMany($list))";
        int[] list = {12, 15, 24};
        String expectedSql = "select * from Emploee where id in (?,?,?)";

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);

        VelocityStringSqlTemplate vst = new VelocityStringSqlTemplate(template);
        SqlQuery query = vst.merge(map);

        assertThat(query.getSql(), is(expectedSql));
        SqlParameter actual0 = query.getParameters().get(0);
        assertThat(actual0, is(instanceOf(IntegerSqlParameter.class)));
        assertThat(actual0.getName(), is("$list0"));
        assertThat(actual0.getValue(), is((Object) 12));

        SqlParameter actual1 = query.getParameters().get(1);
        assertThat(actual1, is(instanceOf(IntegerSqlParameter.class)));
        assertThat(actual1.getName(), is("$list1"));
        assertThat(actual1.getValue(), is((Object) 15));

        SqlParameter actual2 = query.getParameters().get(2);
        assertThat(actual2, is(instanceOf(IntegerSqlParameter.class)));
        assertThat(actual2.getName(), is("$list2"));
        assertThat(actual2.getValue(), is((Object) 24));
    }
}

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

import info.naiv.lab.java.jmt.jdbc.JdbcType;
import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.ClassPathResourceMvelSqlTemplateLoader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author enlo
 */
public class SqlQueryTest {

    private EmbeddedDatabase db;

    private JdbcTemplate jdbcTemplate;

    private ClassPathResourceMvelSqlTemplateLoader loader;

    public SqlQueryTest() {
    }

    @Before
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("SQL/TEST/schema.sql")
                .addScript("SQL/TEST/sampleData.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(db);
        loader = new ClassPathResourceMvelSqlTemplateLoader("SQL");
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    /**
     * Test of batchUpadate method, of class SqlQuery.
     */
    @Test
    public void testBatchUpadate_3args() {
        Query cq = loader.fromString("select count(*) from Users").merge();
        int count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(100));

        User u = new User();
        u.setName("あいう");
        u.setCountry("Japan");
        u.setEmail("test@mail");
        Query query = loader.load("C3", "insert2").merge(u);

        final List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{"Jone", "US", "none"});
        batchArgs.add(new Object[]{"Jane", "EN", "none"});

        int[] affected = query.batchUpadate(jdbcTemplate, new BatchPreparedStatementSetter() {

            @Override
            public int getBatchSize() {
                return batchArgs.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                new ArgumentPreparedStatementSetter(batchArgs.get(i)).setValues(ps);
            }
        });
        assertArrayEquals(new int[]{1, 1, 1}, affected);

        count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(103));
    }

    /**
     * Test of batchUpadate method, of class SqlQuery.
     */
    @Test
    public void testBatchUpadate_JdbcOperations_BatchPreparedStatementSetter() {
        Query cq = loader.fromString("select count(*) from Users where Name = 'Jane'").merge();
        int count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(0));

        User u = new User();
        u.setName("あいう");
        u.setCountry("Japan");
        u.setEmail("test@mail");
        Query query = loader.load("C3", "insert2").merge(u);

        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{"Jone", "US", "none"});
        batchArgs.add(new Object[]{"Jane", "EN", "none"});

        int[] columnTypes = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,};

        int[] affected = query.batchUpadate(jdbcTemplate, batchArgs, columnTypes);
        assertArrayEquals(new int[]{1, 1, 1}, affected);

        count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(1));
    }

    /**
     * Test of batchUpadate method, of class SqlQuery.
     */
    @Test
    public void testBatchUpadate_JdbcOperations_List() {
        Query cq = loader.fromString("select count(*) from Users").merge();
        int count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(100));

        User u = new User();
        u.setName("あいう");
        u.setCountry("Japan");
        u.setEmail("test@mail");
        Query query = loader.load("C3", "insert2").merge(u);

        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{"Jone", "US", "none"});
        batchArgs.add(new Object[]{"Jane", "EN", "none"});

        int[] affected = query.batchUpadate(jdbcTemplate, batchArgs);
        assertArrayEquals(new int[]{1, 1, 1}, affected);

        count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(103));
    }

    /**
     * Test of createPreparedStatement method, of class SqlQuery.
     */
    @Test
    public void testCreatePreparedStatement() throws Exception {
        User u = new User();
        u.setName("Phillip");
        SqlQuery query = (SqlQuery) loader.fromString("select * from Users where Name like @bind{name + ' %'}").merge(u);
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        List<User> users = jdbcTemplate.query(query, rowMapper);
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(1));
        assertThat(users.get(0).id, is(2));
    }

    /**
     * Test of execute method, of class SqlQuery.
     */
    @Test
    public void testExecute_JdbcOperations() {
        Query cq = loader.fromString("select count(*) from Users").merge();
        int count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(100));

        User u = new User();
        u.setName("あいう");
        u.setCountry("Japan");
        u.setEmail("test@mail");
        u.setPhone("123-456-7890");
        u.setFax("123-456-7891");
        u.setPostal("123-4567");
        Query query = loader.load("C3", "insert1").merge(u);
        query.execute(jdbcTemplate);

        count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(101));
    }

    /**
     * Test of execute method, of class SqlQuery.
     */
    @Test
    public void testExecute_JdbcOperations_PreparedStatementCallback() {
        Query cq = loader.fromString("select count(*) from Users").merge();
        int count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(100));

        User u = new User();
        u.setName("あいう");
        u.setCountry("Japan");
        u.setEmail("test@mail");
        u.setPhone("123-456-7890");
        u.setFax("123-456-7891");
        u.setPostal("123-4567");
        Query query = loader.load("C3", "insert1").merge(u);
        int affected = query.execute(jdbcTemplate, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
        assertThat(affected, is(1));

        count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(101));
    }

    /**
     * Test of queryForList method, of class SqlQuery.
     */
    @Test
    public void testQueryForList_JdbcOperations() {
        User u = new User();
        u.setCountry("Norway");
        Query query = loader.fromString("select * from Users where Country = @bind{country}").merge(u);
        List<Map<String, Object>> users = query.queryForList(jdbcTemplate);
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(3));
        assertThat(users.get(0), is(aMapWithSize(12)));
        assertThat(users.get(1), is(aMapWithSize(12)));
        assertThat(users.get(2), is(aMapWithSize(12)));

        Set<Object> ids = new HashSet<>();
        ids.add(users.get(0).get("ID"));
        ids.add(users.get(1).get("ID"));
        ids.add(users.get(2).get("ID"));
        assertThat(ids, is(containsInAnyOrder((Object) 15, 46, 47)));
    }

    /**
     * Test of queryForList method, of class SqlQuery.
     */
    @Test
    public void testQueryForList_JdbcOperations_Class() {
        User u = new User();
        u.setCountry("Norway");
        Query query = loader.fromString("select ID from Users where Country = @bind{country} order by id").merge(u);
        List<Integer> users = query.queryForList(jdbcTemplate, Integer.class);
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(3));
        assertThat(users, is(contains((Object) 15, 46, 47)));
    }

    /**
     * Test of queryForMap method, of class SqlQuery.
     */
    @Test
    public void testQueryForMap() {
        User u = new User();
        u.setCountry("Norway");
        Query query = loader.fromString("select top(1) * from Users where Country = @bind{country} order by id").merge(u);
        Map<String, Object> user = query.queryForMap(jdbcTemplate);
        assertThat(user, is(notNullValue()));
        assertThat(user, is(aMapWithSize(12)));
        assertThat(user.get("ID"), is((Object) 15));
    }

    /**
     * Test of queryForObject method, of class SqlQuery.
     */
    @Test
    public void testQueryForObject_JdbcOperations_Class() {
        Query query = loader.fromString("select count(*) from Users").merge();
        int count = query.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(100));
    }

    /**
     * Test of queryForObject method, of class SqlQuery.
     */
    @Test
    public void testQueryForObject_JdbcOperations_RowMapper() {
        Query query = loader.fromString("select count(*) from Users").merge();
        int count = query.queryForObject(jdbcTemplate, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        });
        assertThat(count, is(100));
    }

    /**
     * Test of queryForRowSet method, of class SqlQuery.
     */
    @Test
    public void testQueryForRowSet() {
        User u = new User();
        u.setCountry("Norway");
        Query query = loader.fromString("select * from Users where Country = @bind{country}").merge(u);
        SqlRowSet users = query.queryForRowSet(jdbcTemplate);
        assertThat(users, is(notNullValue()));
        Set<Integer> ids = new HashSet<>();
        while (users.next()) {
            ids.add(users.getInt("ID"));
        }
        assertThat(ids, is(containsInAnyOrder((Object) 15, 46, 47)));
    }

    /**
     * Test of query method, of class SqlQuery.
     */
    @Test
    public void testQuery_JdbcOperations_ResultSetExtractor() {
        User u = new User();
        u.setName("Phillip");
        Query query = loader.fromString("select * from Users where Name like @bind{name + ' %'}").merge(u);
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        List<User> users = query.query(jdbcTemplate, new RowMapperResultSetExtractor(rowMapper));
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(1));
        assertThat(users.get(0).id, is(2));
    }

    /**
     * Test of query method, of class SqlQuery.
     */
    @Test
    public void testQuery_JdbcOperations_RowMapper() {
        Map<String, Object> map = new HashMap<>();
        map.put("country", "Norway");
        Query query = loader.fromString("select * from Users where Country = @bind{country} order by id").merge(map);
        List<User> users = query.query(jdbcTemplate, BeanPropertyRowMapper.newInstance(User.class));
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(3));
        assertThat(users.get(0).id, is(15));
        assertThat(users.get(1).id, is(46));
        assertThat(users.get(2).id, is(47));
    }

    /**
     * Test of rebind method, of class SqlQuery.
     */
    @Test
    public void testRebind_List() {
        User u = new User();
        u.setName("Phillip");
        Query query = loader.fromString("select * from Users where Name like @bind{name + ' %'}").merge(u);
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        List<User> users = query.query(jdbcTemplate, new RowMapperResultSetExtractor(rowMapper));
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(1));
        assertThat(users.get(0).id, is(2));

        Query query2 = query.rebind(Arrays.asList("Rowan %"));
        users = query2.query(jdbcTemplate, new RowMapperResultSetExtractor(rowMapper));
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(1));
        assertThat(users.get(0).id, is(1));
    }

    /**
     * Test of rebind method, of class SqlQuery.
     */
    @Test
    public void testRebind_PreparedStatementSetter() {
        User u = new User();
        u.setName("Phillip");
        Query query = loader.fromString("select * from Users where Name like @bind{name + ' %'}").merge(u);
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        List<User> users = query.query(jdbcTemplate, new RowMapperResultSetExtractor(rowMapper));
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(1));
        assertThat(users.get(0).id, is(2));

        Query query2 = query.rebind(new ArgumentPreparedStatementSetter(new String[]{"Rowan %"}));
        users = query2.query(jdbcTemplate, new RowMapperResultSetExtractor(rowMapper));
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(1));
        assertThat(users.get(0).id, is(1));
    }

    /**
     * Test of toPreparedStatementCreator method, of class SqlQuery.
     */
    @Test
    public void testToPreparedStatementCreator() {
        Query cq = loader.fromString("select count(*) from Users").merge();
        assertThat(cq.toPreparedStatementCreator(), is(sameInstance((Object) cq)));
    }

    /**
     * Test of update method, of class SqlQuery.
     */
    @Test
    public void testUpdate_JdbcOperations() {
        Query cq = loader.fromString("select count(*) from Users").merge();
        int count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(100));

        User u = new User();
        u.setName("あいう");
        u.setCountry("Japan");
        u.setEmail("test@mail");
        u.setPhone("123-456-7890");
        u.setFax("123-456-7891");
        u.setPostal("123-4567");
        Query query = loader.load("C3", "insert1").merge(u);
        int affected = query.update(jdbcTemplate);
        assertThat(affected, is(1));

        count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(101));
    }

    /**
     * Test of update method, of class SqlQuery.
     */
    @Test
    public void testUpdate_JdbcOperations_KeyHolder() {
        Query cq = loader.fromString("select count(*) from Users").merge();
        int count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(100));

        KeyHolder keyHolder = new GeneratedKeyHolder();

        User u = new User();
        u.setName("あいう");
        Query query = loader.load("C3", "insert1").merge(u);
        int affected = query.update(jdbcTemplate, keyHolder);
        assertThat(affected, is(1));
        assertThat((int) keyHolder.getKey(), is(greaterThan(100)));

        count = cq.queryForObject(jdbcTemplate, Integer.class);
        assertThat(count, is(101));
    }

    @Data
    public static class User {

        int id;
        String name;
        String email;
        String phone;
        String fax;
        String postal;
        String country;
    }

}
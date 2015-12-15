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

import info.naiv.lab.java.jmt.infrastructure.PackageResourceResolver;
import info.naiv.lab.java.jmt.infrastructure.jdk.JdkUsedPackageResourceResolver;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlTemplate;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public class VelocitySqlTemplateLoaderTest {

    public VelocitySqlTemplateLoaderTest() {
    }

    /**
     * Test of formString method, of class VelocitySqlTemplateLoader.
     */
    @Test
    public void testFormString() {
        String template = "select * from Emp";
        VelocitySqlTemplateLoader instance = new VelocitySqlTemplateLoader();
        SqlTemplate result = instance.formString(template);
        assertThat(result, not(nullValue()));
        assertThat(result.merge(null).getSql(), is(template));
    }

    /**
     * Test of load method, of class VelocitySqlTemplateLoader.
     */
    @Test
    public void testLoad() {
        VelocitySqlTemplateLoader instance = new VelocitySqlTemplateLoader();
        SqlTemplate result = instance.load("C1", "mysql", StandardCharsets.UTF_8);
        assertThat(result, not(nullValue()));
        Map<String, Object> params = new HashMap<>();
        params.put("id", "myid");
        assertThat(result.merge(params).getSql(), is("select * from Emploee where id = ?"));
    }

    /**
     * Test of loadCategory method, of class VelocitySqlTemplateLoader.
     */
    @Test
    public void testLoadCategory() {
        VelocitySqlTemplateLoader instance = new VelocitySqlTemplateLoader();
        Iterable<SqlTemplate> result = instance.loadCategory("C1", StandardCharsets.UTF_8);
        assertThat(result, not(nullValue()));        
        Set<String> set = new HashSet<>();
        for(SqlTemplate t : result) {
            set.add(t.getName());
        }
                
        assertThat(set, containsInAnyOrder("mysql.sql", "mysql2.sql"));
    }

    /**
     * Test of getResourceResolver method, of class VelocitySqlTemplateLoader.
     */
    @Test
    public void testGetResourceResolver() {
        VelocitySqlTemplateLoader instance = new VelocitySqlTemplateLoader();
        PackageResourceResolver result = instance.getResourceResolver();
        assertThat(result, not(nullValue()));
    }

    /**
     * Test of setResourceResolver method, of class VelocitySqlTemplateLoader.
     */
    @Test
    public void testSetResourceResolver() {
        PackageResourceResolver resourceResolver = new JdkUsedPackageResourceResolver();
        VelocitySqlTemplateLoader instance = new VelocitySqlTemplateLoader();
        instance.setResourceResolver(resourceResolver);
        PackageResourceResolver result = instance.getResourceResolver();
        assertThat(result, is(sameInstance(resourceResolver)));
    }

}

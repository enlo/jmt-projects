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
package info.naiv.lab.java.jmt.jdbc.sql.template.config;

import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import info.naiv.lab.java.jmt.io.ClassPathResourceRepository;
import info.naiv.lab.java.jmt.io.ResourceRepository;
import info.naiv.lab.java.jmt.io.SystemTempDirectory;
import info.naiv.lab.java.jmt.io.TempDirectory;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplate;
import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.ClassPathResourceMvelSqlTemplateLoader;
import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.FileSystemResourceMvelSqlTemplateLoader;
import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.MvelSqlTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
public class SqlTemplateInjectorTest {

    SqlTemplateInjector instance;

    @Before
    public void setUp() throws IOException {
        instance = new SqlTemplateInjector();
        ClassPathResourceMvelSqlTemplateLoader defaultLoader = new ClassPathResourceMvelSqlTemplateLoader();
        ServiceProviders.getSystemContainer().registerService(defaultLoader);
    }

    /**
     * Test of postProcessAfterInitialization method, of class
     * SqlTemplateInjector.
     */
    @Test
    public void testPostProcessAfterInitialization() {
        Object o = new String();
        Object result = instance.postProcessAfterInitialization(o, "");
        assertThat(result, is(sameInstance(o)));
    }

    /**
     * Test of postProcessBeforeInitialization method, of class
     * SqlTemplateInjector.
     */
    @Test
    public void testPostProcessBeforeInitialization() {
        Injectee o = new Injectee();
        Object result = instance.postProcessBeforeInitialization(o, "");
        assertThat(result, is(sameInstance((Object) o)));
        assertThat(o.insert1.getTemplateText(), is("INSERT INTO Users (Name,EMail,Phone,Fax,Postal,Country)\n"
                   + " VALUES (@bind{name},@bind{email},@bind{phone},@bind{fax},@bind{postal},@bind{country});\n\n"));
        assertThat(((MvelSqlTemplate) o.insertX).getTemplateText(), is("INSERT INTO Users (Name,EMail,Country) VALUES (@bind{name},@bind{email},@bind{country});"));
        assertThat(((MvelSqlTemplate) o.test).getTemplateText(), is("select top(1) * from Emploee where id = @bind{id}"));
        assertThat(((MvelSqlTemplate) o.test2).getTemplateText(), is("select * from 日本語テーブル"));
    }

    /**
     * Test of setBeanFactory method, of class SqlTemplateInjector.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testSetBeanFactory() throws IOException {
        try (TempDirectory tempDirectory = new SystemTempDirectory()) {
            ResourceRepository rr = new ClassPathResourceRepository("SQL");
            Resource res = rr.getResource("C1", "test.sql");
            Path newSql = tempDirectory.getPath().resolve("testX.sql");
            try (InputStream is = res.getInputStream()) {
                Files.copy(is, newSql, StandardCopyOption.REPLACE_EXISTING);
            }

            StaticListableBeanFactory factory = new StaticListableBeanFactory();
            ClassPathResourceMvelSqlTemplateLoader cprtl = new ClassPathResourceMvelSqlTemplateLoader();
            FileSystemResourceMvelSqlTemplateLoader fsrtl = new FileSystemResourceMvelSqlTemplateLoader();
            fsrtl.setRootDirectory(tempDirectory.getPath().toString());
            factory.addBean("ClassPath", cprtl);
            factory.addBean("File", fsrtl);
            instance.setBeanFactory(factory);

            Injectee2 o = new Injectee2();
            instance.postProcessBeforeInitialization(o, "");
            assertThat(((MvelSqlTemplate) o.testX).getTemplateText(),
                       is("select top(1) * from Emploee where id = @bind{id}"));
        }
    }

    @SqlTemplateCategoryOf("C3")
    static class Injectee {

        @InjectSql
        MvelSqlTemplate insert1;

        @InjectSql(name = "insert2")
        SqlTemplate insertX;

        @InjectSql(category = "C1")
        SqlTemplate test;

        @InjectSql(category = "C2", name = "test_sjis", charset = "MS932")
        SqlTemplate test2;

    }

    static class Injectee2 {

        @InjectSql(category = "", loader = "File")
        SqlTemplate testX;

    }

}

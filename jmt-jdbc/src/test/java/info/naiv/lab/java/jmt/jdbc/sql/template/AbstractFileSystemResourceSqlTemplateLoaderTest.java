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
package info.naiv.lab.java.jmt.jdbc.sql.template;

import info.naiv.lab.java.jmt.io.ClassPathResourceRepository;
import info.naiv.lab.java.jmt.io.FileSystemResourceRepository;
import info.naiv.lab.java.jmt.io.ResourceRepository;
import info.naiv.lab.java.jmt.io.SystemTempDirectory;
import info.naiv.lab.java.jmt.io.TempDirectory;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class AbstractFileSystemResourceSqlTemplateLoaderTest<T extends AbstractFileSystemResourceSqlTemplateLoader>
        extends AbstractResourceSqlTemplateLoaderTest<T> {

    /**
     *
     */
    protected String path;

    /**
     *
     */
    protected TempDirectory tempDirectory;

    /**
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        tempDirectory = new SystemTempDirectory();
        path = tempDirectory.getPath().toString();
        ResourceRepository rr = new ClassPathResourceRepository("SQL");
        for (Resource res : rr.getResources("C1").values()) {
            tempDirectory.add("C1", res);
        }
        for (Resource res : rr.getResources("C2").values()) {
            tempDirectory.add("C2", res);
        }
    }

    /**
     *
     */
    @After
    public void tearDown() {
        try {
            tempDirectory.close();
        }
        catch (IOException ex) {
        }
    }

    /**
     * Test of setResourceRepository method, of class
     * AbstractFileSystemResourceSqlTemplateLoader.
     */
    @Test
    public void testSetResourceRepository() {
        FileSystemResourceRepository mrr = mock(FileSystemResourceRepository.class);
        instance.setResourceRepository(mrr);
        assertThat(instance.getResourceRepository(), is(sameInstance(mrr)));
    }

    /**
     * Test of setRootDirectory method, of class
     * AbstractFileSystemResourceSqlTemplateLoader.
     */
    @Test
    public void testSetRootDirectory() {
        instance.setRootDirectory(path);
        String root = instance.getResourceRepository().getRootDirectory().toString();
        assertThat(root, is(equalTo(path)));
    }

}

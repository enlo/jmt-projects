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
package info.naiv.lab.java.jmt.io;

import info.naiv.lab.java.jmt.Misc;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author enlo
 */
public abstract class TempDirectoryTest {

    private final ResourceLoader loader = new DefaultResourceLoader();

    public TempDirectoryTest() {
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_File() throws IOException {
        try (TempDirectory temp1 = newConcrete("1");
             TempDirectory temp2 = newConcrete("2")) {
            Path path = temp2.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            File file1 = temp1.add("123", res).toFile();
            Path actual = temp2.add("123", file1);
            assertThat("parent", actual.getParent(), is(path.resolve("123")));
            assertThat("exists", Files.exists(actual), is(true));
            byte[] actualData = Files.readAllBytes(actual);
            assertArrayEquals(expectedData, actualData);
        }
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_File_noPrefix() throws IOException {
        try (TempDirectory temp1 = newConcrete("1");
             TempDirectory temp2 = newConcrete("2")) {
            Path path = temp2.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            File file1 = temp1.add(null, res).toFile();
            Path actual = temp2.add("", file1);
            assertThat("parent", actual.getParent(), is(path));
            assertThat("exists", Files.exists(actual), is(true));
            byte[] actualData = Files.readAllBytes(actual);
            assertArrayEquals(expectedData, actualData);
        }
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_InputStream() throws IOException {
        try (TempDirectory temp = newConcrete()) {
            Path path = temp.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            try (InputStream is = res.getInputStream()) {
                Path actual = temp.add("123", is);
                assertThat("parent", actual.getParent(), is(path.resolve("123")));
                assertThat("exists", Files.exists(actual), is(true));
                byte[] actualData = Files.readAllBytes(actual);
                assertArrayEquals(expectedData, actualData);
            }
        }
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_InputStream_noPrefix() throws IOException {
        try (TempDirectory temp = newConcrete()) {
            Path path = temp.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            try (InputStream is = res.getInputStream()) {
                Path actual = temp.add(null, is);
                assertThat("parent", actual.getParent(), is(path));
                assertThat("exists", Files.exists(actual), is(true));
                byte[] actualData = Files.readAllBytes(actual);
                assertArrayEquals(expectedData, actualData);
            }
        }
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_Path() throws IOException {
        try (TempDirectory temp1 = newConcrete("1");
             TempDirectory temp2 = newConcrete("2")) {
            Path path = temp2.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            Path file1 = temp1.add("123", res);
            Path actual = temp2.add("123", file1);
            assertThat("parent", actual.getParent(), is(path.resolve("123")));
            assertThat("exists", Files.exists(actual), is(true));
            byte[] actualData = Files.readAllBytes(actual);
            assertArrayEquals(expectedData, actualData);

        }
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_Path_noPrefix() throws IOException {
        try (TempDirectory temp1 = newConcrete("1");
             TempDirectory temp2 = newConcrete("2")) {
            Path path = temp2.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            Path file1 = temp1.add("", res);
            Path actual = temp2.add(" ", file1);
            assertThat("parent", actual.getParent(), is(path));
            assertThat("exists", Files.exists(actual), is(true));
            byte[] actualData = Files.readAllBytes(actual);
            assertArrayEquals(expectedData, actualData);
        }
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_Resource() throws IOException {
        try (TempDirectory temp = newConcrete()) {
            Path path = temp.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            Path actual = temp.add("123", res);
            assertThat("parent", actual.getParent(), is(path.resolve("123")));
            assertThat("exists", Files.exists(actual), is(true));
            byte[] actualData = Files.readAllBytes(actual);
            assertArrayEquals(expectedData, actualData);
        }
    }

    /**
     * Test of add method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAdd_String_Resource_noPrefix() throws IOException {
        try (TempDirectory temp = newConcrete()) {
            Path path = temp.getPath();
            Resource res = loader.getResource("classpath:TEXT/bomtext.txt");
            byte[] expectedData = Misc.toByteArray(res);
            Path actual = temp.add(" ", res);
            assertThat("parent", actual.getParent(), is(path));
            assertThat("exists", Files.exists(actual), is(true));
            byte[] actualData = Files.readAllBytes(actual);
            assertArrayEquals(expectedData, actualData);
        }
    }

    /**
     * Test of close method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testClose() throws IOException {
        Path path;
        try (TempDirectory temp = newConcrete()) {
            path = temp.getPath();
            assertThat(Files.exists(path), is(true));
        }
        assertThat(Files.exists(path), is(false));
    }

    /**
     * Test of getPath method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPath() throws IOException {
        TempDirectory temp = newConcrete();
        Path path = temp.getPath();
        assertThat(Files.exists(path), is(true));
    }

    /**
     * Test of toString method, of class TempDirectory.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testToString() throws IOException {
        TempDirectory temp = newConcrete();
        assertThat(temp.toString(), is(containsString(temp.getPath().toString())));
    }

    protected abstract TempDirectory newConcrete() throws IOException;

    protected abstract TempDirectory newConcrete(String prefix) throws IOException;

}

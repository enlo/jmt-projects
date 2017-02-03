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
package info.naiv.lab.java.jmt.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.util.AntPathMatcher;

/**
 *
 * @author enlo
 */
public class NIOUtilsTest {

    @Rule
    public TemporaryFolder destTempFolder = new TemporaryFolder();
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     *
     */
    public NIOUtilsTest() {
    }

    /**
     * Test of anyPathFilter method, of class NIOUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testAnyPathFilter() throws IOException {
        DirectoryStream.Filter<Path> pf1 = NIOUtils.anyPathFilter();
        DirectoryStream.Filter<Path> pf2 = NIOUtils.anyPathFilter();
        assertThat(pf1, is(sameInstance(pf2)));
        assertThat(pf1.accept(Paths.get("C:/")), is(true));
        assertThat(pf1.accept(Paths.get("D:/sample.txt")), is(true));
        assertThat(pf1.accept(Paths.get("C:/test.java")), is(true));
    }

    /**
     * Test of anyPathMatcher method, of class NIOUtils.
     */
    @Test
    public void testAnyPathMatcher() {
        PathMatcher pm1 = NIOUtils.anyPathMatcher();
        PathMatcher pm2 = NIOUtils.anyPathMatcher();

        assertThat(pm1, is(sameInstance(pm2)));
        assertThat(pm1.matches(Paths.get("C:/")), is(true));
        assertThat(pm1.matches(Paths.get("D:/sample.txt")), is(true));
        assertThat(pm1.matches(Paths.get("C:/test.java")), is(true));
    }

    /**
     * Test of copyAll method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCopyAll() throws Exception {
        initTempFolder();

        Path source = tempFolder.getRoot().toPath();
        Path dest = destTempFolder.getRoot().toPath();

        List<Path> list = NIOUtils.listPaths(dest);
        assertThat(list, hasSize(1));
        NIOUtils.copyAll(source, dest);

        Path bar;
        list = NIOUtils.listPaths(dest);
        assertThat(list, is(containsInAnyOrder(dest,
                                               dest.resolve("foo"),
                                               bar = dest.resolve("bar"),
                                               dest.resolve("sample.txt"),
                                               bar.resolve("master.dat"),
                                               bar.resolve("code.java"))));
    }

    /**
     * Test of copyAll method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test(expected = IOException.class)
    public void testCopyAll_2() throws Exception {
        initTempFolder();
        Path source = tempFolder.getRoot().toPath();
        Path dest = destTempFolder.getRoot().toPath();
        NIOUtils.copyAll(source, dest);
        NIOUtils.copyAll(source, dest);
    }

    /**
     * Test of copyAll method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCopyAll_3() throws Exception {
        initTempFolder();

        Path source = tempFolder.getRoot().toPath();
        Path dest = destTempFolder.getRoot().toPath();

        NIOUtils.copyAll(source, dest);
        NIOUtils.copyAll(source, dest, StandardCopyOption.REPLACE_EXISTING);

        Path bar;
        List<Path> list = NIOUtils.listPaths(dest);
        assertThat(list, is(containsInAnyOrder(dest,
                                               dest.resolve("foo"),
                                               bar = dest.resolve("bar"),
                                               dest.resolve("sample.txt"),
                                               bar.resolve("master.dat"),
                                               bar.resolve("code.java"))));
    }

    /**
     * Test of copy method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCopy_InputStream_OutputStream() throws Exception {
        byte[] source = "いろはにほへと".getBytes();
        InputStream is = new ByteArrayInputStream(source);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        assertThat(NIOUtils.copy(is, os), is((long) source.length));
        assertThat(os.toByteArray(), is(source));
    }

    /**
     * Test of copy method, of class NIOUtils.
     */
    @Test
    public void testCopy_Readable_Appendable() throws Exception {
        String source = "いろはにほへと";
        StringReader sr = new StringReader(source);
        StringBuilder sb = new StringBuilder();
        assertThat(NIOUtils.copy(sr, sb), is((long) source.length()));
        assertThat(sb.toString(), is(source));
    }

    /**
     * Test of deleteRecursive method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteRecursive() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        List<Path> pre = NIOUtils.listPaths(root);
        assertThat(pre, hasSize(6));
        NIOUtils.deleteRecursive(root, false);
        List<Path> post = NIOUtils.listPaths(root);
        assertThat(post, containsInAnyOrder(root, root.resolve("foo"), root.resolve("bar")));
    }

    /**
     * Test of deleteRecursive method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteRecursive_deleteDirectory() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        List<Path> pre = NIOUtils.listPaths(root);
        assertThat(pre, hasSize(6));
        NIOUtils.deleteRecursive(root, true);
        assertThat(Files.exists(root), is(false));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention2() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "dDd";
        String suffix = "Ee";
        String extension = "item";
        boolean ignoreCase = true;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.ee.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention3() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "ddd";
        String suffix = "Ee";
        String extension = "item";
        boolean ignoreCase = false;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention4() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "ddd";
        String suffix = "ee";
        String extension = ".*";
        boolean ignoreCase = false;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.ee.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention5() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item",
                "aaa/bbb/ccc/ddd.ddd.item");

        String name = ".*";
        String suffix = "ddd";
        String extension = ".*";
        boolean ignoreCase = false;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.ddd.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention6() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item",
                "aaa/bbb/ccc/ddd.ddd.item");

        String name = ".*";
        String suffix = "ddd";
        String extension = ".*";
        boolean ignoreCase = false;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.ddd.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention7() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item",
                "aaa/bbb/ccc/ddd.ddd.item");

        String name = "\\w+";
        String suffix = "xx";
        String extension = ".*";
        boolean ignoreCase = false;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention8() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd1.item",
                "aaa/bbb/ccc/ddd2.item",
                "aaa/bbb/ccc/ddd3.item");

        String name = "ddd";
        String suffix = "";
        String extension = "item";
        boolean ignoreCase = false;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention_4args() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/dDd.eE.iTem",
                "aaa/bbb/ccc/dDd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "ddd";
        String suffix = "ee";
        String extension = "item";
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.item")));
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention_5args() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/dDd.eE.iTem",
                "aaa/bbb/ccc/dDd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "ddd";
        String suffix = "ee";
        String extension = "item";
        boolean ignoreCase = true;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/dDd.eE.iTem")));
    }

    /**
     * Test of listByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testListByFilenameWithSuffixAndExtention_4args() {
        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/dDd.eE.iTem",
                "aaa/bbb/ccc/dDd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "dDd";
        String suffix = "ee";
        String extension = "item";
        Collection<Path> result = NIOUtils.listByFilenameWithSuffixAndExtention(list, name, suffix, extension);
        assertThat(result, hasSize(1));
        assertThat(result, is(containsInAnyOrder(Paths.get("aaa/bbb/ccc/dDd.ee.iTem"))));
    }

    /**
     * Test of listByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testListByFilenameWithSuffixAndExtention_5args() {
        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/dDd.eE.iTem",
                "aaa/bbb/ccc/dDd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "ddd";
        String suffix = "ee";
        String extension = "item";
        boolean ignoreCase = true;
        Collection<Path> result = NIOUtils.listByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, hasSize(2));
        assertThat(result, is(containsInAnyOrder(Paths.get("aaa/bbb/ccc/dDd.eE.iTem"),
                                                 Paths.get("aaa/bbb/ccc/ddd.item"))));
    }

    /**
     * Test of listByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testListByFilenameWithSuffixAndExtention_5args_2() {
        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/dDd.eE.iTem",
                "aaa/bbb/ccc/dDd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "ddd";
        String suffix = "ee";
        String extension = "item";
        boolean ignoreCase = false;
        Collection<Path> result = NIOUtils.listByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, hasSize(1));
        assertThat(result, is(containsInAnyOrder(Paths.get("aaa/bbb/ccc/ddd.item"))));
    }

    /**
     * Test of listFiles method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListFiles() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        Path bar = root.resolve("bar");
        List<String> list = NIOUtils.listFiles(root, "**.*", 2);
        assertThat(list, hasSize(3));
        assertThat(list, is(containsInAnyOrder(root.resolve("sample.txt").toString(),
                                               bar.resolve("master.dat").toString(),
                                               bar.resolve("code.java").toString())));
    }

    /**
     * Test of listFiles method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListFiles2() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        List<String> list = NIOUtils.listFiles(root, "**.*", 1);
        assertThat(list, is(hasSize(1)));
        assertThat(list, is(contains(root.resolve("sample.txt").toString())));
    }

    /**
     * Test of listFiles method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListFiles3() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        List<String> list = NIOUtils.listFiles(root, "**.java", 1);
        assertThat(list, is(hasSize(0)));
    }

    /**
     * Test of listPaths method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListPaths_3args() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        Path bar = root.resolve("bar");
        List<Path> list = NIOUtils.listPaths(root, "**", 2);
        assertThat(list, is(hasSize(6)));
        assertThat(list, is(containsInAnyOrder(root,
                                               root.resolve("bar"),
                                               root.resolve("foo"),
                                               root.resolve("sample.txt"),
                                               bar.resolve("master.dat"),
                                               bar.resolve("code.java"))));
    }

    /**
     * Test of listPaths method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListPaths_3args_2() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        List<Path> list = NIOUtils.listPaths(root, "**", 1);
        assertThat(list, is(hasSize(4)));
        assertThat(list, is(containsInAnyOrder(root,
                                               root.resolve("bar"),
                                               root.resolve("foo"),
                                               root.resolve("sample.txt"))));
    }

    /**
     * Test of listPaths method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListPaths_3args_3() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        List<Path> list = NIOUtils.listPaths(root, "**.java", 1);
        assertThat(list, is(hasSize(0)));
    }

    /**
     * Test of listPaths method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListPaths_Path() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        Path bar = root.resolve("bar");
        List<Path> list = NIOUtils.listPaths(root);
        assertThat(list, is(hasSize(6)));
        assertThat(list, is(containsInAnyOrder(root,
                                               root.resolve("foo"),
                                               root.resolve("bar"),
                                               root.resolve("sample.txt"),
                                               bar.resolve("master.dat"),
                                               bar.resolve("code.java"))));
    }

    /**
     * Test of listPaths method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testListPaths_Path_String() throws Exception {
        initTempFolder();
        Path root = tempFolder.getRoot().toPath();
        Path bar = root.resolve("bar");
        List<Path> list = NIOUtils.listPaths(root, "**.*");
        assertThat(list, is(hasSize(3)));
        assertThat(list, is(containsInAnyOrder(root.resolve("sample.txt"),
                                               bar.resolve("master.dat"),
                                               bar.resolve("code.java"))));
    }

    /**
     * Test of toByteArray method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testToByteArray() throws Exception {
        Charset cs = StandardCharsets.UTF_8;
        byte[] source = "いろはにほへと".getBytes(cs);
        ByteArrayInputStream is = new ByteArrayInputStream(source);
        byte[] data = NIOUtils.toByteArray(is);
        assertThat(data, is(source));
    }

    /**
     * Test of toByteBuffer method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testToByteBuffer() throws Exception {
        Charset cs = StandardCharsets.UTF_8;
        byte[] source = "いろはにほへと".getBytes(cs);
        ByteArrayInputStream is = new ByteArrayInputStream(source);
        ByteBuffer bb = NIOUtils.toByteBuffer(is);
        assertThat(bb, comparesEqualTo(ByteBuffer.wrap(source)));
    }

    /**
     * Test of toCharBuffer method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testToCharBuffer() throws Exception {
        Charset cs = StandardCharsets.UTF_8;
        String source = "いろはにほへと";
        ByteArrayInputStream is = new ByteArrayInputStream(source.getBytes(cs));
        CharBuffer cb = NIOUtils.toCharBuffer(is, cs);
        assertThat(cb, comparesEqualTo(CharBuffer.wrap(source)));
    }

    /**
     * Test of toCharBuffer method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testToCharBuffer_FileInput() throws Exception {
        Charset cs = StandardCharsets.UTF_8;
        String source = "いろはにほへと";
        ByteArrayInputStream is = new ByteArrayInputStream(source.getBytes(cs));
        CharBuffer cb = NIOUtils.toCharBuffer(is, cs);
        assertThat(cb, comparesEqualTo(CharBuffer.wrap(source)));
    }

    /**
     * Test of toPathFilter method, of class NIOUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testToPathFilter_Pattern() throws IOException {
        Pattern p = Pattern.compile(".*\\.txt$");
        DirectoryStream.Filter<Path> pf = NIOUtils.toPathFilter(p);

        Path p1 = Paths.get("C:/Test/sample.txt");
        Path p2 = Paths.get("C:/Test/sample.java");

        assertThat(pf.accept(p1), is(true));
        assertThat(pf.accept(p2), is(false));
    }

    /**
     * Test of toPathFilter method, of class NIOUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testToPathFilter_PatternNull() throws IOException {
        Pattern p = null;
        DirectoryStream.Filter<Path> pf = NIOUtils.toPathFilter(p);
        assertThat(pf, is(sameInstance(NIOUtils.anyPathFilter())));
    }

    /**
     * Test of toPathFilter method, of class NIOUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testToPathFilter_jnfPathMatcher() throws IOException {
        Path p1 = Paths.get("C:/Test/sample.txt");
        Path p2 = Paths.get("C:/Test/sample.java");

        java.nio.file.PathMatcher pm = mock(PathMatcher.class);
        when(pm.matches(p1)).thenReturn(Boolean.TRUE);
        when(pm.matches(p2)).thenReturn(Boolean.FALSE);

        DirectoryStream.Filter<Path> pf = NIOUtils.toPathFilter(pm);

        assertThat(pf.accept(p1), is(true));
        assertThat(pf.accept(p2), is(false));
    }

    /**
     * Test of toPathFilter method, of class NIOUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testToPathFilter_jnfPathMatcherNull() throws IOException {
        java.nio.file.PathMatcher pm = null;
        DirectoryStream.Filter<Path> pf = NIOUtils.toPathFilter(pm);
        assertThat(pf, is(sameInstance(NIOUtils.anyPathFilter())));
    }

    /**
     * Test of toPathFilter method, of class NIOUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testToPathFilter_null() throws IOException {
        Pattern p = Pattern.compile(".*\\.txt$");
        DirectoryStream.Filter<Path> pf = NIOUtils.toPathFilter(p);

        Path p1 = Paths.get("C:/Test/sample.txt");
        Path p2 = Paths.get("C:/Test/sample.java");

        assertThat(pf.accept(p1), is(true));
        assertThat(pf.accept(p2), is(false));
    }

    /**
     * Test of toPathMatcher method, of class NIOUtils.
     */
    @Test
    public void testToPathMatcher_Pattern() {
        Pattern p = Pattern.compile(".*\\.txt$");
        PathMatcher pm = NIOUtils.toPathMatcher(p);
        Path p1 = Paths.get("C:/Test/sample.txt");
        Path p2 = Paths.get("C:/Test/sample.java");

        assertThat(pm.matches(p1), is(true));
        assertThat(pm.matches(p2), is(false));
    }

    /**
     * Test of toPathMatcher method, of class NIOUtils.
     */
    @Test
    public void testToPathMatcher_String_osuPathMatcher() {
        String pt = "**/*.java";
        Path p1 = Paths.get("C:/Test/sample.txt");
        org.springframework.util.PathMatcher p = spy(new AntPathMatcher());
        PathMatcher pm = NIOUtils.toPathMatcher(pt, p);

        assertThat(pm.matches(p1), is(false));
        verify(p, times(1)).match(pt, p1.toString());
    }

    /**
     * Test of toPathMatcher method, of class NIOUtils.
     */
    @Test
    public void testToPathMatcher_null() {
        Pattern p = null;
        PathMatcher pm = NIOUtils.toPathMatcher(p);
        assertThat(pm, is(sameInstance(NIOUtils.anyPathMatcher())));
    }

    /**
     * Test of toPathMatcher method, of class NIOUtils.
     */
    @Test
    public void testToPathMatcher_null_osuPathMatcher() {
        org.springframework.util.PathMatcher p = spy(new AntPathMatcher());
        PathMatcher pm = NIOUtils.toPathMatcher(null, p);
        assertThat(pm, is(sameInstance(NIOUtils.anyPathMatcher())));
    }

    /**
     * Test of toPaths method, of class NIOUtils.
     */
    @Test
    public void testToPaths() {
        String s1 = "C:/Test/sample.txt";
        String s2 = "/usr/local/test/sample.txt";
        Path p1 = Paths.get(s1);
        Path p2 = Paths.get(s2);
        assertThat(NIOUtils.toPaths(Arrays.asList(s1, s2)), contains(p1, p2));
    }

    /**
     * Test of toString method, of class NIOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testToString() throws Exception {
        Charset cs = StandardCharsets.UTF_8;
        String source = "いろはにほへと";
        ByteArrayInputStream is = new ByteArrayInputStream(source.getBytes(cs));
        assertThat(NIOUtils.toString(is, cs), is(source));
    }

    private void initTempFolder() throws IOException {
        tempFolder.newFolder("foo").toPath();
        tempFolder.newFile("sample.txt").toPath();
        Path bar = tempFolder.newFolder("bar").toPath();
        Path code = tempFolder.newFile().toPath();
        Path master = tempFolder.newFile().toPath();
        Files.move(code, bar.resolve("code.java"));
        Files.move(master, bar.resolve("master.dat"));
    }
}

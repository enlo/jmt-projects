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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class NIOUtilsTest {

    /**
     *
     */
    public NIOUtilsTest() {
    }

    /**
     * Test of findByFilenameWithSuffixAndExtention method, of class NIOUtils.
     */
    @Test
    public void testFindByFilenameWithSuffixAndExtention() {

        List<String> list = Arrays.asList(
                "aaa/bbb/ccc/ddd.item",
                "aaa/bbb/ccc/ddd.ee.item",
                "aaa/bbb/ccc/ddd.gg.item");

        String name = "ddd";
        String suffix = "ee";
        String extension = "item";
        boolean ignoreCase = false;
        Path result = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, suffix, extension, ignoreCase);
        assertThat(result, is(Paths.get("aaa/bbb/ccc/ddd.ee.item")));
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
}

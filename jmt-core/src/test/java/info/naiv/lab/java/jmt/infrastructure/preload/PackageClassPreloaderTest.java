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
package info.naiv.lab.java.jmt.infrastructure.preload;

import info.naiv.lab.java.jmt.infrastructure.preload.test1.TestClass1;
import info.naiv.lab.java.jmt.infrastructure.preload.test1.TestClass2;
import info.naiv.lab.java.jmt.infrastructure.preload.test1.TestClass3;
import info.naiv.lab.java.jmt.infrastructure.preload.test1.TestClass4;
import info.naiv.lab.java.jmt.infrastructure.preload.test1.t2.TestClass5;
import info.naiv.lab.java.jmt.infrastructure.preload.test2.TestClass6;
import java.util.Arrays;
import java.util.Set;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class PackageClassPreloaderTest extends AbstractClassPreloaderTest {

    /**
     *
     */
    public PackageClassPreloaderTest() {
    }

    /**
     * Test of getExtractPattern method, of class PackageClassPreloader.
     */
    @Test
    public void testGetExtractPattern() {
        PackageClassPreloader instance = newConcrete();
        assertThat(instance.getExtractPattern(), is(empty()));
    }

    @Override
    public void testPreload() {
        PackageClassPreloader loader = newConcrete();
        loader.setScanPackages(Arrays.asList("info.naiv.lab.java.jmt.infrastructure.preload.test1",
                                             "info.naiv.lab.java.jmt.infrastructure.preload.test2"));

        Set<Class<?>> classes = loader.preload();
        assertThat(classes, is(containsInAnyOrder(TestClass1.class,
                                                  TestClass2.class,
                                                  TestClass3.class,
                                                  TestClass4.class,
                                                  TestClass5.class,
                                                  TestClass6.class)));

    }

    /**
     *
     */
    @Test
    public void testPreload_2() {
        PackageClassPreloader loader = newConcrete();
        loader.setScanPackages(Arrays.asList("info.naiv.lab.java.jmt.infrastructure.preload.test1",
                                             "info.naiv.lab.java.jmt.infrastructure.preload.test2"));

        loader.setExtractPattern(Arrays.asList(".*[4-5]"));
        Set<Class<?>> classes = loader.preload();
        assertThat(classes, is(containsInAnyOrder(TestClass1.class,
                                                  TestClass2.class,
                                                  TestClass3.class,
                                                  TestClass6.class)));

    }

    /**
     * Test of setExtractPattern method, of class PackageClassPreloader.
     */
    @Test
    public void testSetExtractPattern() {
        PackageClassPreloader loader = newConcrete();
        loader.setExtractPattern(Arrays.asList("a", "b", "b", "c"));
        assertThat(loader.getExtractPattern(), is(containsInAnyOrder("a", "b", "c")));
    }

    /**
     *
     * @return
     */
    @Override
    protected PackageClassPreloader newConcrete() {
        return new PackageClassPreloader();
    }

}

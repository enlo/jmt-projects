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

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public abstract class AbstractClassPreloaderTest {

    public AbstractClassPreloaderTest() {
    }

    protected abstract AbstractClassPreloader newConcrete();

    /**
     * Test of preload method, of class AbstractClassPreloader.
     */
    @Test
    public abstract void testPreload();

    /**
     * Test of setScanPackages method, of class AbstractClassPreloader.
     */
    @Test
    public void testSetScanPackages() {
        List<String> names = Arrays.asList("a", "b", "b", "c");
        AbstractClassPreloader instance = newConcrete();
        instance.setScanPackages(names);
        assertThat(instance.getScanPackages(), is(containsInAnyOrder("a", "b", "c")));
    }

    /**
     * Test of getScanPackages method, of class AbstractClassPreloader.
     */
    @Test
    public void testGetScanPackages() {
        assertThat(newConcrete().getScanPackages(), is(empty()));
    }

}

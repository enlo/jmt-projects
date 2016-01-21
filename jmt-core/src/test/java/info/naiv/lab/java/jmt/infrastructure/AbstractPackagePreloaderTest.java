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
package info.naiv.lab.java.jmt.infrastructure;

import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public abstract class AbstractPackagePreloaderTest {

    public AbstractPackagePreloaderTest() {
    }

    /**
     * Test of addExcludePattern method, of class AbstractPackagePreloader.
     */
    @Test
public void testAddExcludePattern() {
    
    String packageName = "info.naiv.lab.java.jmt.infrastructure.annotation";
    AbstractPackagePreloader instance = getPackagePreloader();
    instance.addPackage(packageName);
    instance.addExcludePattern(".*StringTagOf$");
    assertThat(instance.load(),
                   containsInAnyOrder(
                           "info.naiv.lab.java.jmt.infrastructure.annotation.ClassTagOf",
                           "info.naiv.lab.java.jmt.infrastructure.annotation.IntegerTagOf",
                           "info.naiv.lab.java.jmt.infrastructure.annotation.TagOf"));
}

    /**
     * Test of addPackage method, of class AbstractPackagePreloader.
     */
    @Test
    public void testAddPackage() {
        String packageName = "info.naiv.lab.java.jmt.infrastructure.annotation";
        AbstractPackagePreloader instance = getPackagePreloader();
        assertThat(instance.addPackage(packageName), is(true));
        packageName = "info.naiv.lab.java.jmt.infrastructure.annotation";
        assertThat(instance.addPackage(packageName), is(false));
    }

    /**
     * Test of getPackageNames method, of class AbstractPackagePreloader.
     */
    @Test
    public void testGetPackageNames() {

        String packageName = "info.naiv.lab.java.jmt.infrastructure.annotation";

        AbstractPackagePreloader instance = getPackagePreloader();

        assertThat(instance.getPackageNames(), emptyIterable());
        assertThat(instance.addPackage(packageName), is(true));
        assertThat(instance.getPackageNames(),
                   containsInAnyOrder(
                           "info.naiv.lab.java.jmt.infrastructure.annotation"));
    }

    /**
     * Test of load method, of class AbstractPackagePreloader.
     */
    @Test
    public void testLoad() {
        String packageName = "info.naiv.lab.java.jmt.infrastructure.annotation";
        AbstractPackagePreloader instance = getPackagePreloader();
        assertThat(instance.addPackage(packageName), is(true));
        assertThat(instance.load(),
                   containsInAnyOrder(
                           "info.naiv.lab.java.jmt.infrastructure.annotation.ClassTagOf",
                           "info.naiv.lab.java.jmt.infrastructure.annotation.IntegerTagOf",
                           "info.naiv.lab.java.jmt.infrastructure.annotation.StringTagOf",
                           "info.naiv.lab.java.jmt.infrastructure.annotation.TagOf"));
    }

    protected abstract AbstractPackagePreloader getPackagePreloader();

}

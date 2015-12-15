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

import static info.naiv.lab.java.jmt.fx.StandardFunctions.byRegex;
import info.naiv.lab.java.jmt.fx.StringPredicate;
import java.util.Set;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author enlo
 */
public abstract class AbstractPackageResourceResolverTest {

    public AbstractPackageResourceResolverTest() {
    }

    protected abstract AbstractPackageResourceResolver getPackageResourceResolver();

    /**
     * Test of setExcludePatterns method, of class
     * AbstractPackageResourceResolver.
     */
    @Test
    public void testSetExcludePatterns() {
        AbstractPackageResourceResolver instance = getPackageResourceResolver();
        StringPredicate pattern = byRegex(".*Myname");
        assertThat(instance.getExcludePatternSet(), empty());
        instance.setExcludePatterns(pattern);
        assertThat(instance.getExcludePatternSet(), containsInAnyOrder(pattern));
    }

    /**
     * Test of setIncludePatterns method, of class
     * AbstractPackageResourceResolver.
     */
    @Test
    public void testSetIncludePatterns() {
        AbstractPackageResourceResolver instance = getPackageResourceResolver();
        StringPredicate pattern = byRegex(".*Myname");
        instance.setIncludePatterns(pattern);
        assertThat(instance.getIncludePatternSet(), containsInAnyOrder(pattern));
    }

    /**
     * Test of addPackage method, of class AbstractPackageResourceResolver.
     */
    @Test
    public void testAddPackage() {
        AbstractPackageResourceResolver instance = getPackageResourceResolver();
        String packageName = "my.package";
        assertThat(instance.getPackageNames(), emptyIterable());
        instance.addPackage(packageName);
        assertThat(instance.getPackageNames(), containsInAnyOrder(packageName));
    }

    /**
     * Test of setPackageNames method, of class AbstractPackageResourceResolver.
     */
    @Test
    public void testSetPackageNames() {
        AbstractPackageResourceResolver instance = getPackageResourceResolver();
        String[] packageNames = {"my.package", "any.package"};
        assertThat(instance.getPackageNames(), emptyIterable());
        instance.setPackageNames(packageNames);
        assertThat(instance.getPackageNames(), containsInAnyOrder(packageNames));
    }

    /**
     * Test of getPackageNames method, of class AbstractPackageResourceResolver.
     */
    @Test
    public void testGetPackageNames() {
        AbstractPackageResourceResolver instance = getPackageResourceResolver();
        assertThat(instance.getPackageNames(), sameInstance((Object) instance.getPackageNameSet()));
    }

    /**
     * Test of list method, of class JdkUsedPackageResourceResolver.
     */
    @Test
    public void testList() {
        AbstractPackageResourceResolver instance = getPackageResourceResolver();
        instance.addPackage("TEXT");
        instance.setIncludePatterns(byRegex(".*\\.txt$"));
        Set<String> result = instance.list();
        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder("TEXT/bomtext.txt", "TEXT/nobomtext.txt"));
    }

}

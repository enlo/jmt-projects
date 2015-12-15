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
package info.naiv.lab.java.jmt;

import java.util.Properties;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author enlo
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, ResolvableProperties.class})
public class ResolvablePropertiesTest {

    public ResolvablePropertiesTest() {
    }

    Properties prop;

    ResolvableProperties testTarget;

    @Before
    public void setup() {
        prop = new Properties();
        prop.setProperty("itemName", "@property");
        prop.setProperty("itemName1", "${itemNameSys}");
        prop.setProperty("itemName2", "${itemNameEnv}");
        prop.setProperty("itemName3", "${itemNameOther}");

        PowerMockito.mockStatic(System.class);
        when(System.getProperty("itemNameSys")).thenReturn("@systemProp");
        when(System.getenv("itemNameEnv")).thenReturn("@env");

        testTarget = spy(new ResolvableProperties(prop));

    }

    /**
     * Test of getProperty method, of class ResolvableProperties.
     */
    @Test
    public void testGetProperty_String_String() {

        ResolvableProperties p = testTarget;

        assertThat(p.getProperty("itemName", "none"), is("@property"));
        assertThat(p.getProperty("itemNameOther", "none"), is("none"));
        assertThat(p.getProperty("itemNameOther", null), is(nullValue()));

        assertThat(p.getProperty("itemName1", "none"), is("@systemProp"));
        assertThat(p.getProperty("itemName2", "none"), is("@env"));
        assertThat(p.getProperty("itemName3", "none"), is("${itemNameOther}"));

        assertThat(p.getProperty("itemNameOther", "${itemName1}"), is("@systemProp"));
        assertThat(p.getProperty("itemNameOther", "${itemName2}"), is("@env"));
        assertThat(p.getProperty("itemNameOther", "${itemName3}"), is("${itemNameOther}"));

        p.setProperty("itemNameOther", "");
        assertThat(p.getProperty("itemName3", "none"), is(""));
        assertThat(p.getProperty("itemNameOther", "${itemName3}"), is(""));
    }

    /**
     * Test of getProperty method, of class ResolvableProperties.
     */
    @Test
    public void testGetProperty_String() {

        ResolvableProperties p = testTarget;

        assertThat(p.getProperty("itemName"), is("@property"));
        assertThat(p.getProperty("itemNameOther"), is(nullValue()));

        assertThat(p.getProperty("itemName1"), is("@systemProp"));
        assertThat(p.getProperty("itemName2"), is("@env"));
        assertThat(p.getProperty("itemName3"), is("${itemNameOther}"));

        p.setProperty("itemNameOther", "");
        assertThat(p.getProperty("itemNameOther"), is(""));
        assertThat(p.getProperty("itemName3"), is(""));

    }

    /**
     * Test of fix method, of class ResolvableProperties.
     */
    @Test
    public void testFix() {
        ResolvableProperties p = testTarget;

        Properties fixed = p.fix();

        assertThat(fixed.getProperty("itemName"), is("@property"));
        assertThat(fixed.getProperty("itemNameOther"), is(nullValue()));

        assertThat(fixed.getProperty("itemName", "none"), is("@property"));
        assertThat(fixed.getProperty("itemNameOther", "none"), is("none"));
        assertThat(fixed.getProperty("itemNameOther", null), is(nullValue()));

        assertThat(fixed.getProperty("itemName1"), is("@systemProp"));
        assertThat(fixed.getProperty("itemName2"), is("@env"));
        assertThat(fixed.getProperty("itemName3"), is("${itemNameOther}"));

        p.setProperty("itemNameOther", "");
        assertThat(fixed.getProperty("itemNameOther"), is(nullValue()));
        assertThat(fixed.getProperty("itemName3"), is("${itemNameOther}"));

        assertThat(fixed.getProperty("itemNameOther", "${itemName1}"), is("${itemName1}"));
        assertThat(fixed.getProperty("itemNameOther", "${itemName2}"), is("${itemName2}"));
        assertThat(fixed.getProperty("itemNameOther", "${itemName3}"), is("${itemName3}"));

        assertThat(fixed.getProperty("itemName3", "none"), is("${itemNameOther}"));
        assertThat(fixed.getProperty("itemNameOther", "${itemName3}"), is("${itemName3}"));

    }

}

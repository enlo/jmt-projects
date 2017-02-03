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
package info.naiv.lab.java.jmt.template.mvel;

import info.naiv.lab.java.jmt.ExpressiveProperties;
import info.naiv.lab.java.jmt.ExtendPropertiesTest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
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
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, MvelExpressiveProperties.class})
public class MvelExpressivePropertiesTest extends ExtendPropertiesTest {

    Properties prop;

    MvelExpressiveProperties testTarget;

    /**
     *
     */
    public MvelExpressivePropertiesTest() {
    }

    /**
     *
     */
    @Before
    public void setup() {
        prop = new Properties();
        prop.setProperty("itemName", "property");
        prop.setProperty("itemName1", "@{itemNameSys}");
        prop.setProperty("itemName2", "@{itemNameEnv}");
        prop.setProperty("itemName3", "@{itemNameOther}");

        Properties sysProp = new Properties();
        sysProp.setProperty("itemNameSys", "systemProp");
        Map<String, String> envMap = new HashMap<>();
        envMap.put("itemNameEnv", "env");

        PowerMockito.mockStatic(System.class);
        when(System.getProperties()).thenReturn(sysProp);
        when(System.getenv()).thenReturn(envMap);
        when(System.getProperty("itemNameSys")).thenReturn(sysProp.getProperty("itemNameSys"));
        when(System.getenv("itemNameEnv")).thenReturn(envMap.get("itemNameEnv"));
        testTarget = spy(new MvelExpressiveProperties(prop));

    }

    /**
     * Test of fix method, of class ResolvableProperties.
     */
    @Test
    @Override
    public void testFix() {
        MvelExpressiveProperties p = testTarget;

        try {
            Properties fixed = p.fix();

            assertThat(fixed.getProperty("itemName"), is("property"));
            assertThat(fixed.getProperty("itemNameOther"), is(nullValue()));

            assertThat(fixed.getProperty("itemName", "none"), is("property"));
            assertThat(fixed.getProperty("itemNameOther", "none"), is("none"));
            assertThat(fixed.getProperty("itemNameOther", null), is(nullValue()));

            assertThat(fixed.getProperty("itemName1"), is("systemProp"));
            assertThat(fixed.getProperty("itemName2"), is("env"));
            assertThat(fixed.getProperty("itemName3"), is(nullValue()));

            p.setProperty("itemNameOther", "");
            assertThat(fixed.getProperty("itemNameOther"), is(nullValue()));
            assertThat(fixed.getProperty("itemName3"), is(nullValue()));

            assertThat(fixed.getProperty("itemNameOther", "@{itemName1}"), is("@{itemName1}"));
            assertThat(fixed.getProperty("itemNameOther", "@{itemName2}"), is("@{itemName2}"));
            assertThat(fixed.getProperty("itemNameOther", "@{itemName3}"), is("@{itemName3}"));

            assertThat(fixed.getProperty("itemName3", "none"), is("none"));
            assertThat(fixed.getProperty("itemNameOther", "@{itemName3}"), is("@{itemName3}"));
        }
        catch (Exception e) {
            logger.error("e", e);
        }
    }

    /**
     * Test of getProperty method, of class ResolvableProperties.
     */
    @Test
    @Override
    public void testGetProperty_String() {

        MvelExpressiveProperties p = testTarget;

        assertThat(p.getProperty("itemName"), is("property"));
        assertThat(p.getProperty("itemNameOther"), is(nullValue()));

        assertThat(p.getProperty("itemName1"), is("systemProp"));
        assertThat(p.getProperty("itemName2"), is("env"));
        assertThat(p.getProperty("itemName3"), is(nullValue()));

        p.setProperty("itemNameOther", "");
        assertThat(p.getProperty("itemNameOther"), is(""));
        assertThat(p.getProperty("itemName3"), is(""));

    }

    /**
     * Test of getProperty method, of class ResolvableProperties.
     */
    @Test
    @Override
    public void testGetProperty_String_String() {
        MvelExpressiveProperties p = testTarget;

        assertThat(p.getProperty("itemName", "default1"), is("property"));
        assertThat(p.getProperty("itemNameOther", "default1"), is("default1"));
        assertThat(p.getProperty("itemNameOther", null), is(nullValue()));

        assertThat(p.getProperty("itemName1", "default2"), is("systemProp"));
        assertThat(p.getProperty("itemName2", "default2"), is("env"));
        assertThat(p.getProperty("itemName3", "default2"), is("default2"));

        assertThat(p.getProperty("itemNameOther", "@{itemName1}"), is("systemProp"));
        assertThat(p.getProperty("itemNameOther", "@{itemName2}"), is("env"));
        assertThat(p.getProperty("itemNameOther", "@{itemName3}"), is(nullValue()));
        assertThat(p.getProperty("itemNameOther", "@{itemName3} "), is("null "));

        p.setProperty("itemNameOther", "");
        assertThat(p.getProperty("itemName3", "none"), is(""));
        assertThat(p.getProperty("itemNameOther", "@{itemName3}"), is(""));
    }

    @Override
    protected ExpressiveProperties newInstance() {
        return new MvelExpressiveProperties();
    }

}

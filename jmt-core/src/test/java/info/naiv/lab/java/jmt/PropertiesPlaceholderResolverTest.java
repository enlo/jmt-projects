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
package info.naiv.lab.java.jmt;

import java.util.Properties;
import static org.hamcrest.Matchers.is;
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
@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, PropertiesPlaceholderResolver.class})
public class PropertiesPlaceholderResolverTest {

    Properties prop;
    PropertiesPlaceholderResolver testTarget;

    /**
     *
     */
    public PropertiesPlaceholderResolverTest() {
    }

    /**
     *
     */
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
        testTarget = spy(new PropertiesPlaceholderResolver());

    }

    /**
     * Test of resolve method, of class PropertiesPlaceholderResolver.
     */
    @Test
    public void testResolve() {
        assertThat(testTarget.resolve(prop, "${itemName}"), is("@property"));
        assertThat(testTarget.resolve(prop, "${itemNameOther}"), is("${itemNameOther}"));

        assertThat(testTarget.resolve(prop, "${itemName1} ${itemName2}"), is("@systemProp @env"));
        assertThat(testTarget.resolve(prop, "itemName3 is ${itemName3}"), is("itemName3 is ${itemNameOther}"));

        prop.setProperty("itemNameOther", "");
        assertThat(testTarget.resolve(prop, "${itemNameOther}"), is(""));
        assertThat(testTarget.resolve(prop, "${itemName3}"), is(""));
        assertThat(testTarget.resolve(prop, "itemName3"), is("itemName3"));
    }

}

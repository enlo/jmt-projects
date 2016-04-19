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
package info.naiv.lab.java.jmt.datetime;

import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import info.naiv.lab.java.jmt.infrastructure.SimpleServiceContainer;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class SimpleDateFormatComponentTest {

    SimpleDateFormatComponent comp;
    ServiceProvider provider;

    /**
     *
     */
    public SimpleDateFormatComponentTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
        comp = new SimpleDateFormatComponent();
        SimpleServiceContainer container = new SimpleServiceContainer();
        container.registerService(comp);
        provider = container;
    }

    /**
     * Test of getContentType method, of class SimpleDateFormatComponent.
     */
    @Test
    public void testGetContentType() {
        Class clz = comp.getContentType();
        assertThat(clz, is(equalTo((Class) SimpleDateFormat.class)));
    }

    /**
     * Test of getContentType method, of class SimpleDateFormatComponent.
     */
    @Test
    public void testPatternTag() {
        Date date = ClassicDateUtils.createCalendar(2015, 10, 12).getTime();

        SimpleDateFormat f1 = provider.resolveService(SimpleDateFormat.class, Tag.of("yyyyMMdd"));
        SimpleDateFormat f2 = provider.resolveService(SimpleDateFormat.class, Tag.of("yyyy-MM-dd"));

        assertThat("yyyyMMdd", f1.format(date), is("20151012"));
        assertThat("yyyyMMdd", f2.format(date), is("2015-10-12"));
    }

}

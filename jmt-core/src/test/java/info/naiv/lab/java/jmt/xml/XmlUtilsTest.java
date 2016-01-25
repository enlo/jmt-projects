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
package info.naiv.lab.java.jmt.xml;

import static info.naiv.lab.java.jmt.xml.XmlUtils.get;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpression;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author enlo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/test-application-context.xml")
public class XmlUtilsTest {

    @Autowired
    ApplicationContext context;

    public XmlUtilsTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getXPath method, of class XmlUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetXPath() throws Exception {
        Resource xml = context.getResource("classpath:TEXT/test.xml");
        try (InputStream stream = xml.getInputStream()) {
            InputSource source = new InputSource(stream);

            String key = "configuration/Properties/Property[@name='path']";
            XPathExpression xp = XmlUtils.getXPath(key);
            String actual = xp.evaluate(source);
            assertThat(key, actual, is("logs/"));

        }
        try (InputStream stream = xml.getInputStream()) {
            InputSource source = new InputSource(stream);
            String key = "//*/RollingFile[1]/@name";
            XPathExpression xp = XmlUtils.getXPath(key);
            String actual = xp.evaluate(source);
            assertThat(key, actual, is("File"));
        }
    }

    /**
     * Test of setNodeValue method, of class XmlUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testSetNodeValue() throws Exception {
        Resource xml = context.getResource("classpath:TEXT/test.xml");
        try (InputStream stream = xml.getInputStream()) {
            InputSource source = new InputSource(stream);
            Document doc = XmlUtils.toDocument(source);

            {
                String key = "configuration/Properties/Property[@name='path']/text()";
                XmlUtils.setNodeValue(doc, key, "MyLogs");
                XPathExpression xp = XmlUtils.getXPath(key);
                String actual = xp.evaluate(doc);
                assertThat(key, actual, is("MyLogs"));
            }

            {
                String key = "//*/loggers/root/@level";
                XmlUtils.setNodeValue(doc, key, "error");
                XPathExpression xp = XmlUtils.getXPath(key);
                String actual = xp.evaluate(doc);
                assertThat(key, actual, is("error"));
            }
        }
    }

    /**
     * Test of toByteArray method, of class XmlUtils.
     */
    @Test
    public void testToByteArray_DOMSource() throws Exception {
        Resource xml = context.getResource("classpath:TEXT/test.xml");
        try (InputStream stream = xml.getInputStream()) {
            InputSource source = new InputSource(stream);
            Document doc = XmlUtils.toDocument(source);
            DOMSource domSource = new DOMSource(doc);

            byte[] expected;
            {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Result result = new StreamResult(os);
                Transformer transformer = get(Transformer.class);
                transformer.transform(domSource, result);
                expected = os.toByteArray();
            }

            {
                byte[] actual = XmlUtils.toByteArray(domSource);
                assertArrayEquals(expected, actual);
            }
        }
    }

    /**
     * Test of toByteArray method, of class XmlUtils.
     */
    @Test
    public void testToByteArray_Document() throws Exception {
        Resource xml = context.getResource("classpath:TEXT/test.xml");
        try (InputStream stream = xml.getInputStream()) {
            InputSource source = new InputSource(stream);
            Document doc = XmlUtils.toDocument(source);

            byte[] expected;
            {
                DOMSource domSource = new DOMSource(doc);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Result result = new StreamResult(os);
                Transformer transformer = get(Transformer.class);
                transformer.transform(domSource, result);
                expected = os.toByteArray();
            }

            {
                byte[] actual = XmlUtils.toByteArray(doc);
                assertArrayEquals(expected, actual);
            }
        }
    }

    /**
     * Test of toDocument method, of class XmlUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testToDocument() throws Exception {
        Resource xml = context.getResource("classpath:TEXT/test.xml");
        try (InputStream stream = xml.getInputStream()) {
            InputSource source = new InputSource(stream);

            Document doc = XmlUtils.toDocument(source);
            assertThat(doc, is(notNullValue()));
            assertThat(doc.getDocumentElement().getTagName(), is("configuration"));
        }
    }

}

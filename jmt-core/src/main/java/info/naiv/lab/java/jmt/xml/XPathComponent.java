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

import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.infrastructure.component.AbstractTlsServiceComponent;
import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

/**
 *
 * @author enlo
 */
public class XPathComponent
        extends AbstractTlsServiceComponent<XPath>
        implements SystemComponent {

    /**
     *
     * @param tag
     * @param provider
     * @return
     */
    @Override
    @Nonnull
    public XPath getContent(Tag tag, ServiceProvider provider) {
        XPath xpath = super.getContent(tag, provider);
        xpath.reset();
        return xpath;
    }

    @Override
    public Class<? extends XPath> getContentType() {
        return XPath.class;
    }

    /**
     *
     * @param map
     * @param tag
     * @param provider
     * @return
     */
    @Override
    protected boolean handleNotFound(Map<Tag, XPath> map, Tag tag, ServiceProvider provider) {
        XPathFactory factory = provider.resolveService(XPathFactory.class);
        requireNonNull(factory, "XPathFactory must be registered.");
        map.put(tag, factory.newXPath());
        return true;
    }
}

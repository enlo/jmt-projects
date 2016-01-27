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
package info.naiv.lab.java.jmt.jdbc.sql.template.mvel;

import info.naiv.lab.java.jmt.jdbc.sql.template.mvel.node.CustomNodes;
import info.naiv.lab.java.jmt.io.NIOUtils;
import info.naiv.lab.java.jmt.jdbc.sql.template.AbstractClassPathResourceSqlTemplateLoader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
public class ClassPathResourceMvelSqlTemplateLoader
        extends AbstractClassPathResourceSqlTemplateLoader {

    public ClassPathResourceMvelSqlTemplateLoader() {
    }

    public ClassPathResourceMvelSqlTemplateLoader(String rootPackage) {
        setRootPackage(rootPackage);
    }

    protected MvelSqlTemplate compile(String name, String template) {
        CompiledTemplate ct = TemplateCompiler.compileTemplate(template, CustomNodes.NODES);
        return new MvelSqlTemplate(name, ct);
    }

    @Override
    protected MvelSqlTemplate createSqlTemplateFromResource(String name, Resource resource, Charset charset) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return compile(name, NIOUtils.toString(is, charset));
        }
    }

    @Override
    protected MvelSqlTemplate doFromString(String name, String template) {
        return compile(name, template);
    }

}
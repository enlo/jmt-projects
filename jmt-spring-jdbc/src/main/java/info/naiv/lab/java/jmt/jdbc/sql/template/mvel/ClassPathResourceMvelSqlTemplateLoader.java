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

import info.naiv.lab.java.jmt.infrastructure.PackageResourceResolver;
import info.naiv.lab.java.jmt.io.NIOUtils;
import info.naiv.lab.java.jmt.jdbc.sql.template.AbstractResourceSqlTemplateLoader;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author enlo
 */
public class ClassPathResourceMvelSqlTemplateLoader
        extends AbstractResourceSqlTemplateLoader {

    public ClassPathResourceMvelSqlTemplateLoader() {
    }

    public ClassPathResourceMvelSqlTemplateLoader(PackageResourceResolver resolver) {
        super(resolver);
    }

    protected SqlTemplate compile(String name, String template) {        
        CompiledTemplate ct = TemplateCompiler.compileTemplate(template, CustomNodes.NODES);
        return new MvelSqlTemplate(name, ct);
    }

    @Override
    protected SqlTemplate createSqlTemplateByFile(String path, Charset charset) throws IOException {
        ClassPathResource res = new ClassPathResource(path);
        try (InputStream is = res.getInputStream()) {
            String text = NIOUtils.toString(is, charset);
            return compile(path, text);
        }
    }

    @Override
    protected SqlTemplate doFromString(String template) {
        return compile(UUID.randomUUID().toString(), template);
    }
}

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
package info.naiv.lab.java.jmt.tquery.template.mvel;

import info.naiv.lab.java.jmt.template.Template;
import info.naiv.lab.java.jmt.template.mvel.AbstractMvelTemplateBuilder;
import info.naiv.lab.java.jmt.tquery.command.Command;
import info.naiv.lab.java.jmt.tquery.template.mvel.node.TQueryCustomNodesProvider;
import java.util.Arrays;
import java.util.List;
import org.mvel2.ParserContext;
import info.naiv.lab.java.jmt.template.mvel.MvelCompiledTemplateResolver;

/**
 *
 * @author enlo
 */
public class MvelQueryTemplateBuilder extends AbstractMvelTemplateBuilder<Command> {

    List<Class<?>> importClasses = Arrays.<Class<?>>asList(java.sql.Types.class);

    public MvelQueryTemplateBuilder() {
        this.setCustomNodesProvider(new TQueryCustomNodesProvider());
    }

    @Override
    public boolean isInstanceOf(Class<? extends Template> templateType) {
        return templateType.isAssignableFrom(MvelQueryTemplate.class);
    }

    public void setImportClasses(Class<?>... importClasses) {
        this.importClasses = Arrays.asList(importClasses);
    }

    @Override
    protected MvelQueryTemplate build(String name, MvelCompiledTemplateResolver template) {
        return new MvelQueryTemplate(name, template, contextFactory);
    }

    @Override
    protected ParserContext initParserContext(ParserContext ctx) {
        ctx = super.initParserContext(ctx);
        for (Class<?> cls : importClasses) {
            ctx.addImport(cls.getSimpleName(), cls);
        }
        ctx.addImport("jdbcTypes", java.sql.Types.class);
        return ctx;
    }

}

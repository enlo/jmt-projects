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
package info.naiv.lab.java.jmt.support.jdbc.sql.velocity;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import lombok.Getter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author enlo
 */
public class VelocityResourceSqlTemplate extends AbstractVelocitySqlTemplate {

    @Getter
    final String name;
    final Template template;

    public VelocityResourceSqlTemplate(String templateName) {
        this(templateName, StandardCharsets.UTF_8);
    }

    public VelocityResourceSqlTemplate(String templateName, Charset charset) {
        this.name = Paths.get(templateName).getFileName().toString();
        this.template = engine.getTemplate(templateName, charset);
    }
    
    @Override
    protected void mergeTemplate(Writer writer, VelocityContext context) throws IOException {
        template.merge(context, writer);
    }

}

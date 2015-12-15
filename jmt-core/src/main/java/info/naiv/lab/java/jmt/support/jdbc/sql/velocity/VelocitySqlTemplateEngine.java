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

import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.io.Writer;
import java.nio.charset.Charset;
import lombok.Getter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.log.CommonsLogLogChute;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 *
 * @author enlo
 */
public class VelocitySqlTemplateEngine implements SystemComponent {

    private final RuntimeInstance ri = new RuntimeInstance();

    @Getter
    private VelocityEngine velocityEngine;

    public VelocitySqlTemplateEngine() {
        initVelocityRuntime();
        velocityEngine = new VelocitySqlTemplateEngineFactory().getVelocityEngine();
        initVelocityEngine();
    }

    public void evaluate(VelocityContext context, Writer writer, String name, String template) {
        velocityEngine.evaluate(context, writer, name, template);
    }

    public Template getTemplate(String templateName) {
        return velocityEngine.getTemplate(templateName);
    }

    public Template getTemplate(String templateName, Charset charset) {
        return velocityEngine.getTemplate(templateName, charset.name());
    }

    public SimpleNode parse(String template, String name) throws ParseException {
        return ri.parse(template, name);
    }

    public void render(VelocityContext context, Writer writer, String name, SimpleNode node) {
        ri.render(context, writer, name, node);
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
        initVelocityEngine();
    }

    private void initVelocityEngine() {
        velocityEngine.loadDirective("info.naiv.lab.java.jmt.support.jdbc.sql.velocity.BindDirective");
        velocityEngine.loadDirective("info.naiv.lab.java.jmt.support.jdbc.sql.velocity.BindManyDirective");
    }

    private void initVelocityRuntime() {
        ri.addDirective(new BindDirective());
        ri.addDirective(new BindManyDirective());
        ri.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new CommonsLogLogChute());
    }

}

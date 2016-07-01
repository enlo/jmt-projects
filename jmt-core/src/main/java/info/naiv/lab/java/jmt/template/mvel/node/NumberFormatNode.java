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
package info.naiv.lab.java.jmt.template.mvel.node;

import info.naiv.lab.java.jmt.Arguments;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.resolveService;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.text.NumberFormatConfig;
import java.io.Serializable;
import java.text.Format;
import java.text.NumberFormat;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class NumberFormatNode extends CustomNode {

    private static final long serialVersionUID = -1865512381230309449L;

    private Serializable[] ces;

    @Override
    public String name() {
        return "numberFormat";
    }

    @Override
    protected void doEval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        Object[] values = MVEL.executeAllExpression(ces, ctx, factory);
        Object value = values[0];
        if (value != null) {
            if (1 < values.length) {
                Object format = values[1];
                if (format instanceof Format) {
                    appender.append(((Format) format).format(value));
                }
                else if (format instanceof CharSequence) {
                    NumberFormatConfig conf = new NumberFormatConfig(format.toString());
                    NumberFormat nf = resolveService(NumberFormat.class, Tag.of(conf));
                    appender.append(nf.format(value));
                }
            }
            else {
                appender.append(String.format("%s", value));
            }
        }
    }

    @Override
    protected void onSetContents() {
        super.onSetContents();
        ces = compileMultipleContents(':');
        Arguments.nonEmpty(ces, "contents");
    }
}

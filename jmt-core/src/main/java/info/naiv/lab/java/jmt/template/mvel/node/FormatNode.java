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
import java.io.Serializable;
import static java.util.Arrays.copyOfRange;
import java.util.Locale;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 *
 * @author enlo
 */
public class FormatNode extends CustomNode {

    private static final long serialVersionUID = -2569912311939889318L;

    private Serializable[] ces;

    @Override
    public String name() {
        return "format";
    }

    @Override
    protected void doEval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        Object[] items = MVEL.executeAllExpression(ces, ctx, factory);
        if (items[0] instanceof CharSequence) {
            String format = items[0].toString();
            if (1 < items.length) {
                String value = String.format(format, copyOfRange(items, 1, items.length));
                appender.append(value);
            }
            else {
                appender.append(format);
            }
        }
        else if (items[0] instanceof Locale) {
            Locale locale = (Locale) items[0];
            String format = items[1].toString();
            if (2 < items.length) {
                String value = String.format(locale, format, copyOfRange(items, 2, items.length));
                appender.append(value);
            }
            else {
                appender.append(format);
            }
        }
        else {
            appender.append(String.format("%s", items[0]));
        }

    }

    @Override
    protected void onSetContents() {
        super.onSetContents();
        ces = compileMultipleContents(',');
        Arguments.nonEmpty(ces, "contents");
    }

}

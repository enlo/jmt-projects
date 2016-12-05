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

import info.naiv.lab.java.jmt.Misc;
import static info.naiv.lab.java.jmt.Misc.isNotEmpty;
import java.io.Serializable;
import java.util.Arrays;
import static java.util.Arrays.copyOfRange;
import java.util.Locale;
import org.mvel2.MVEL;
import static org.mvel2.MVEL.executeExpression;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author enlo
 */
public class FormatNode extends MultiCompiledExpressionNode {

    private static final long serialVersionUID = -2569912311939889318L;

    public FormatNode() {
        super(',', "format");
    }

    @Override
    public String name() {
        return "format";
    }

    @Override
    protected void doEval(MultiValueMap<String, Serializable> compiledExpressions, TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {

        String format = null;
        if (compiledExpressions.containsKey("format")) {
            format = executeExpression(compiledExpressions.getFirst("format"), ctx, factory, String.class);
        }
        Locale locale = null;
        if (compiledExpressions.containsKey("locale")) {
            Object lc = executeExpression(compiledExpressions.getFirst("locale"), ctx, factory);
            locale = Misc.toLocale(lc, null);
        }
        Object[] values;
        if (compiledExpressions.containsKey("value")) {
            values = executeAllExpression(compiledExpressions.get("value"), ctx, factory);
        }
        else {
            values = new Object[]{};
        }

        if (isNotEmpty(format)) {
            if (locale == null) {
                String value = String.format(format, values);
                appender.append(value);
            }
            else {
                String value = String.format(locale, format, values);
                appender.append(value);
            }
        }
        else {
            String value = Arrays.toString(values);
            appender.append(value);
        }
    }

}

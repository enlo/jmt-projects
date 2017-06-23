/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
import info.naiv.lab.java.jmt.monad.Optional;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import static org.mvel2.MVEL.executeExpression;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author enlo
 */
public class MessageNode extends MultiCompiledExpressionNode {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_LOCALE_VARNAME = "locale";
    private static final String[] DEFAULT_BUNDLE_VARNAMES = {"messageSource", "messages", "message"};

    public MessageNode() {
        super(',', "code");
    }

    @Override
    public String name() {
        return "message";
    }

    protected String resolveMessage(MessageSourceResolvable message, Locale locale, Object ctx, VariableResolverFactory factory, Object bundle) {
        if (bundle instanceof MessageSource) {
            return ((MessageSource) bundle).getMessage(message, locale);
        }
        else if (bundle instanceof String) {
            return ResourceBundle.getBundle((String) bundle, locale).getString(message.getCodes()[0]);
        }
        else {
            return message.getDefaultMessage();
        }
    }

    @Override
    protected void doEval(MultiValueMap<String, Serializable> compiledExpressions, TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        String[] codes = (String[]) executeAllExpression(compiledExpressions.get("code"), ctx, factory);
        Locale locale = getLocale(compiledExpressions, ctx, factory);
        Object[] args = executeAllExpression(compiledExpressions.get("value"), ctx, factory);
        String defaultMessage = getDefaultMessage(compiledExpressions, ctx, factory);
        Object bundle = getBundle(compiledExpressions, ctx, factory);
        MessageSourceResolvable msr = new DefaultMessageSourceResolvable(codes, args, defaultMessage);
        String message = resolveMessage(msr, locale, ctx, factory, bundle);
        appender.append(message);
    }

    public Object getBundle(MultiValueMap<String, Serializable> compiledExpressions, Object ctx, VariableResolverFactory factory) {
        Serializable ce = compiledExpressions.getFirst("bundle");
        if (ce != null) {
            return executeExpression(ce, ctx, factory);
        }
        for (String vn : DEFAULT_BUNDLE_VARNAMES) {
            if (factory.isResolveable(vn)) {
                Object value = factory.getVariableResolver(vn).getValue();
                if (value instanceof MessageSource || value instanceof CharSequence) {
                    return value;
                }
            }
        }
        return null;
    }

    public String getDefaultMessage(MultiValueMap<String, Serializable> compiledExpressions, Object ctx, VariableResolverFactory factory) {
        Serializable ce = compiledExpressions.getFirst("defaultMessage");
        if (ce == null) {
            ce = compiledExpressions.getFirst("code");
        }
        return executeExpression(ce, ctx, factory).toString();
    }

    public Locale getLocale(MultiValueMap<String, Serializable> compiledExpressions, Object ctx, VariableResolverFactory factory) {
        Serializable ce = compiledExpressions.getFirst("locale");
        if (ce != null) {
            return Misc.toLocale(executeExpression(ce, ctx, factory), null);
        }
        else if (factory.isResolveable(DEFAULT_LOCALE_VARNAME)) {
            Object loc = factory.getVariableResolver(DEFAULT_LOCALE_VARNAME).getValue();
            if (loc instanceof Locale) {
                return (Locale) loc;
            }
        }
        return Locale.getDefault();
    }

}

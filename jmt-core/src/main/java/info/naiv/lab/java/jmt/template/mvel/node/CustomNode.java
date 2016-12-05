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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import info.naiv.lab.java.jmt.KeyValuePair;
import info.naiv.lab.java.jmt.Misc;
import static info.naiv.lab.java.jmt.Misc.getOrDefault;
import static info.naiv.lab.java.jmt.Misc.isEmpty;
import info.naiv.lab.java.jmt.template.mvel.ParserContextHolder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import static org.mvel2.MVEL.compileExpression;
import static org.mvel2.MVEL.executeExpression;
import org.mvel2.ParserContext;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;
import static org.mvel2.util.ParseTools.balancedCapture;
import static org.mvel2.util.ParseTools.createStringTrimmed;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author enlo
 */
public abstract class CustomNode extends Node {

    private static final long serialVersionUID = 1L;

    /**
     * 複数の式をそれぞれコンパイル.
     *
     * @param expressions
     * @return
     */
    protected static Serializable[] compileMultipleContents(@Nonnull List<String> expressions) {
        Serializable[] ces = new Serializable[expressions.size()];
        ParserContext ctx = ParserContextHolder.get();
        if (ctx != null) {
            for (int i = 0; i < ces.length; i++) {
                ces[i] = compileExpression(expressions.get(i), ctx);
            }
        }
        else {
            for (int i = 0; i < ces.length; i++) {
                ces[i] = compileExpression(expressions.get(i));
            }
        }
        return ces;
    }

    protected static Object[] executeAllExpression(List<? extends Serializable> exprs, Object ctx, VariableResolverFactory factory) {
        if (isEmpty(exprs)) {
            return new Object[]{};
        }
        Object[] result = new Object[exprs.size()];
        for (int i = 0; i < exprs.size(); i++) {
            result[i] = executeExpression(ctx, ctx, factory);
        }
        return result;
    }

    /**
     * 式を分割する.
     *
     * @param contents
     * @param delim
     * @return
     */
    @Nonnull
    protected static List<String> splitExpression(@Nonnull char[] contents, char delim) {
        List<String> expr = new ArrayList<>();
        int start = 0;
        int to = contents.length;
        for (int i = start; i < to; i++) {
            final char ch = contents[i];
            switch (ch) {
                case '(':
                case '[':
                case '{':
                case '"':
                case '\'':
                    i = balancedCapture(contents, i, ch);
                    break;
                default:
                    if (ch == delim) {
                        expr.add(createStringTrimmed(contents, start, i - start));
                        start = i + 1;
                    }
                    break;
            }
        }

        if (start < to) {
            expr.add(createStringTrimmed(contents, start, to - start));
        }
        return expr;
    }

    /**
     *
     */
    public CustomNode() {
    }

    /**
     *
     * @param template
     */
    @Override
    public void calculateContents(char[] template) {
        super.calculateContents(template); //To change body of generated methods, choose Tools | Templates.
        onSetContents();
    }

    /**
     * {@inheritDoc}
     *
     * @param terminatingNode
     * @param template
     * @return
     */
    @Override
    public boolean demarcate(Node terminatingNode, char[] template) {
        return false;
    }

    /**
     *
     * @param runtime
     * @param appender
     * @param ctx
     * @param factory
     * @return
     */
    @Override
    public Object eval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        doEval(runtime, appender, ctx, factory);
        return next != null ? next.eval(runtime, appender, ctx, factory) : null;
    }

    /**
     *
     * @return ノード名.
     */
@Nonnull
    public abstract String name();

    /**
     * {@inheritDoc}
     *
     * @param contents
     */
    @Override
    public void setContents(char[] contents) {
        super.setContents(contents);
        onSetContents();
    }

    @Override
    public String toString() {
        return arrayToString(getClass().getSimpleName(), name, "{",
                                                               (contents == null ? "" : new String(contents)),
                                                               "} (start=", begin, ";end=", end, ")");

    }

    /**
     * 式を区切ってそれぞれコンパイル.
     *
     * @param delim
     * @param args
     * @return
     */
    protected MultiValueMap<String, Serializable> compileMappedContents(char delim, String... args) {
        List<String> exprs = splitExpression(contents, delim);
        MultiValueMap<String, Serializable> result = new LinkedMultiValueMap<>(exprs.size());
        for (int i = 0; i < exprs.size(); i++) {
            String expr = exprs.get(i);
            String varname = getOrDefault(args, i, "value");
            KeyValuePair<String, String> pair = Misc.splitKeyValue(expr, ":=", true);
            if (pair == null) {
                result.add(varname, compileSingleContents(expr.toCharArray()));
            }
            else {
                char[] cs = pair.getValue().toCharArray();
                result.add(pair.getKey(), compileSingleContents(cs));
            }
        }
        return result;
    }

    /**
     * 式を区切ってそれぞれコンパイル.
     *
     * @param delim
     * @return
     */
    protected Serializable[] compileMultipleContents(char delim) {
        List<String> exprs = splitExpression(contents, delim);
        return compileMultipleContents(exprs);
    }

    /**
     * 単一式のコンパイル.
     *
     * @return
     */
    @Nonnull
    protected Serializable compileSingleContents() {
        return compileSingleContents(contents);
    }

    /**
     * 単一式のコンパイル.
     *
     * @param expr
     * @return
     */
    @Nonnull
    protected Serializable compileSingleContents(char[] expr) {
        ParserContext ctx = ParserContextHolder.get();
        if (ctx != null) {
            return compileExpression(expr, ctx);
        }
        else {
            return compileExpression(expr);
        }
    }

    /**
     * 処理の実行.
     *
     * @param runtime
     * @param appender
     * @param ctx
     * @param factory
     */
    protected abstract void doEval(@Nonnull TemplateRuntime runtime, @Nonnull TemplateOutputStream appender, Object ctx, VariableResolverFactory factory);

    /**
     * コンテンツが設定された後に処理を行う
     */
    protected void onSetContents() {
    }

}

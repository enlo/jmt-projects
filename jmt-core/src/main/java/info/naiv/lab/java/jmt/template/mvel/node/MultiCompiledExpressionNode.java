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

import java.io.Serializable;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.TemplateOutputStream;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author enlo
 */
public abstract class MultiCompiledExpressionNode extends CustomNode {

    private static final long serialVersionUID = 1L;

    private MultiValueMap<String, Serializable> ceMap;

    private final char delimiter;
    private final String[] args;

    public MultiCompiledExpressionNode(char delimiter, String... args) {
        this.delimiter = delimiter;
        this.args = args;
    }

    protected void checkContents(MultiValueMap<String, Serializable> ceMap) {
        if (ceMap.isEmpty()) {
            throw new IllegalArgumentException("contents is empty");
        }
    }

    @Override
    protected final void doEval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory) {
        doEval(ceMap, runtime, appender, ctx, factory);
    }

    /**
     * 式の実行.
     *
     * @param compiledExpressions
     * @param runtime
     * @param appender
     * @param ctx
     * @param factory
     */
    protected abstract void doEval(MultiValueMap<String, Serializable> compiledExpressions, TemplateRuntime runtime, TemplateOutputStream appender, Object ctx, VariableResolverFactory factory);

    @Override
    protected final void onSetContents() {
        super.onSetContents();
        ceMap = compileMappedContents(delimiter, args);
        checkContents(ceMap);
    }

}

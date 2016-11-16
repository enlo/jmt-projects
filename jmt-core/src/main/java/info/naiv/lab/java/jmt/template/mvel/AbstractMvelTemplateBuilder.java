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
package info.naiv.lab.java.jmt.template.mvel;

import static info.naiv.lab.java.jmt.Misc.toCharArray;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.template.Template;
import info.naiv.lab.java.jmt.template.TemplateBuilder;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import org.mvel2.ParserContext;
import org.mvel2.templates.CompiledTemplate;

/**
 *
 * @author enlo
 * @param <TResult>
 */
public abstract class AbstractMvelTemplateBuilder<TResult> implements TemplateBuilder<TResult> {

    /**
     * Custom node provider.
     *
     */
    @Getter
    @Setter
    protected MvelCustomNodesProvider customNodesProvider;

    /**
     * ParserContext intializer.
     */
    @Getter
    @Setter
    protected Function1<ParserContext, ParserContext> parserContextInitializer;

    @Override
    public Template<TResult> build(String name, String template) {
        CompiledTemplate compiled = compile(template.toCharArray());
        return build(name, compiled);
    }

    @Override
    public Template<TResult> build(String name, CharSequence template) {
        CompiledTemplate compiled = compile(toCharArray(template));
        return build(name, compiled);
    }

    @Override
    public Template<TResult> build(String name, char[] template) {
        CompiledTemplate compiled = compile(template);
        return build(name, compiled);
    }

    /**
     * CompiledTemplate から Template を作成する.
     *
     * @param name
     * @param template
     * @return
     */
    @Nonnull
    protected abstract Template<TResult> build(String name, @Nonnull CompiledTemplate template);

    /**
     * 文字列から CompiledTemplate を生成する.
     *
     * @param template
     * @return
     */
    @Nonnull
    protected CompiledTemplate compile(char[] template) {
        ParserContext ctx = ParserContext.create();
        return MvelTemplateUtils.compile(template, ctx, customNodesProvider);
    }

    /**
     * ParserContextの初期化.
     *
     * @param ctx
     * @return
     */
    @CheckReturnValue
    protected ParserContext initParserContext(ParserContext ctx) {
        if (parserContextInitializer != null) {
            ctx = parserContextInitializer.apply(ctx);
        }
        return ctx;
    }

}

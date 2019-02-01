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

import info.naiv.lab.java.jmt.collection.BeanPropertyLookup;
import info.naiv.lab.java.jmt.collection.Lookup;
import info.naiv.lab.java.jmt.template.Template;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRegistry;
import org.mvel2.templates.TemplateRuntime;

/**
 *
 * @author enlo
 * @param <TResult>
 */
@Getter
@Setter
public class MvelTemplate<TResult> implements Template<TResult> {

    private MvelTemplateContextFactory<TResult> contextFactory;

    private final String name;
    private TemplateRegistry templateRegistry;
    private final MvelCompiledTemplateResolver templateResolver;

    /**
     *
     * @param name
     * @param templateResolver
     * @param contextFactory
     */
    public MvelTemplate(@Nonnull String name,
                        @Nonnull MvelCompiledTemplateResolver templateResolver,
                        @Nonnull MvelTemplateContextFactory<TResult> contextFactory) {
        this.name = name;
        this.templateResolver = templateResolver;
        this.contextFactory = contextFactory;
    }

    @Override
    public CompiledTemplate getTemplateObject() {
        return templateResolver.resolve();
    }

    @Override
    public String getTemplateSource() {
        return new String(getTemplateObject().getTemplate());
    }

    @Override
    public TResult merge() {
        return merge(new HashMap<String, Object>());
    }

    @Override
    public <TArg> TResult merge(Map<String, TArg> parameters) {
        return execute(new MvelTemplateVariableResolverFactory(parameters));
    }

    @Override
    public <TArg> TResult merge(Lookup<String, TArg> parameters) {
        return execute(new MvelTemplateVariableResolverFactory(parameters));
    }

    @Override
    public TResult mergeBean(Object bean) {
        VariableResolverFactory factory = createFactory(bean);
        return execute(new MvelTemplateVariableResolverFactory(factory));
    }

    /**
     *
     * @param value
     * @return
     */
    protected VariableResolverFactory createFactory(Object value) {
        if (value instanceof Map) {
            return new MapVariableResolverFactory((Map) value);
        }
        else if (value instanceof Lookup) {
            return new LookupVariableResolverFactory((Lookup<String, Object>) value);
        }
        else if (value instanceof VariableResolverFactory) {
            return (VariableResolverFactory) value;
        }
        else {
            Lookup ps = new BeanPropertyLookup(value);
            return new LookupVariableResolverFactory(ps);
        }
    }

    /**
     * テンプレートから実際のオブジェクトを取得
     *
     * @param factory
     * @return
     */
    protected TResult execute(MvelTemplateVariableResolverFactory factory) {
        MvelTemplateContext<TResult> context = contextFactory.createContext(factory);
        context.initVariableResolverFactory(factory);
        Object result = TemplateRuntime.execute(getTemplateObject(), context, factory, templateRegistry);
        return context.createResult(result);
    }

}

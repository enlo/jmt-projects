/*
 * The MIT License
 *
 * Copyright 2019 enlo.
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

import info.naiv.lab.java.jmt.collection.Lookup;
import static java.util.Collections.unmodifiableMap;
import java.util.Map;
import lombok.Setter;
import org.mvel2.UnresolveablePropertyException;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.DefaultLocalVariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolver;
import org.mvel2.integration.impl.MapVariableResolverFactory;

/**
 *
 * @author enlo
 */
public class MvelTemplateVariableResolverFactory
        extends DefaultLocalVariableResolverFactory {

    private static final long serialVersionUID = 1L;

    @Setter
    protected boolean enableUnresolveProperty = true;

    public MvelTemplateVariableResolverFactory() {
    }

    public MvelTemplateVariableResolverFactory(VariableResolverFactory nextFactory) {
        super(nextFactory);
    }

    public MvelTemplateVariableResolverFactory(Map<String, ?> variables) {
        this(new MapVariableResolverFactory(unmodifiableMap(variables)));
    }

    public MvelTemplateVariableResolverFactory(Lookup<String, ?> variables) {
        this(new LookupVariableResolverFactory(variables));
    }

    @Override
    public VariableResolver getVariableResolver(String name) {
        try {
            return super.getVariableResolver(name);
        }
        catch (UnresolveablePropertyException ex) {
            if (enableUnresolveProperty) {
                return addResolver(name, new MapVariableResolver(variables, name));
            }
            throw ex;
        }
    }

    public MvelTemplateVariableResolverFactory initContext(MvelTemplateContext<?> context) {
        setLocalValue("_context_", context);
        context.initVariableResolverFactory(this);
        return this;
    }

    @Override
    public boolean isResolveable(String name) {
        return enableUnresolveProperty || super.isResolveable(name);
    }

    public final void setLocalValue(String name, Object value) {
        this.variables.put(name, value);
    }

}

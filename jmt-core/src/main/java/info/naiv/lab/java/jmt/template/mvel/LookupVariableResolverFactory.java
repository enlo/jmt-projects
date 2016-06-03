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

import info.naiv.lab.java.jmt.collection.Lookup;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.UnresolveablePropertyException;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.BaseVariableResolverFactory;
import org.mvel2.integration.impl.SimpleSTValueResolver;
import org.mvel2.integration.impl.SimpleValueResolver;

/**
 *
 * @author enlo
 */
@SuppressWarnings("serial")
@Slf4j
public class LookupVariableResolverFactory extends BaseVariableResolverFactory {

    /**
     *
     */
    protected final Lookup<String, Object> parameters;

    /**
     *
     * @param oarameters
     */
    public LookupVariableResolverFactory(Lookup<String, Object> oarameters) {
        this(oarameters, null);
    }

    /**
     *
     * @param parameters
     * @param nextFactory
     */
    public LookupVariableResolverFactory(Lookup<String, Object> parameters, VariableResolverFactory nextFactory) {
        this.parameters = parameters;
        this.nextFactory = nextFactory;
    }

    @Override
    public VariableResolver createVariable(String name, Object value) {
        VariableResolver vr;
        try {
            vr = getVariableResolver(name);
            vr.setValue(value);
            return vr;
        }
        catch (UnresolveablePropertyException e) {
            logger.trace("getVariableResolver() fail. {}", e.getMessage());
            vr = new SimpleValueResolver(value);
            variableResolvers.put(name, vr);
            return vr;
        }
    }

    @Override
    public VariableResolver createVariable(String name, Object value, Class<?> type) {
        VariableResolver vr;
        try {
            vr = getVariableResolver(name);
        }
        catch (UnresolveablePropertyException e) {
            logger.trace("getVariableResolver() fail. {}", e.getMessage());
            vr = null;
        }

        if (vr != null && vr.getType() != null) {
            throw new RuntimeException("variable already defined within scope: " + vr.getType() + " " + name);
        }
        else {
            vr = new SimpleSTValueResolver(value, type);
            variableResolvers.put(name, vr);
            return vr;
        }
    }

    @Override
    public VariableResolver getVariableResolver(String name) {
        VariableResolver vr = variableResolvers.get(name);
        if (vr != null) {
            return vr;
        }
        else if (parameters.containsKey(name)) {
            vr = new LookupVariableResolver(parameters, name);
            variableResolvers.put(name, vr);
            return vr;
        }
        else if (nextFactory != null) {
            return nextFactory.getVariableResolver(name);
        }

        throw new UnresolveablePropertyException("unable to resolve variable '" + name + "'");
    }

    @Override
    public boolean isResolveable(String name) {
        return variableResolvers.containsKey(name)
                || (parameters != null && name != null && parameters.containsKey(name))
                || (nextFactory != null && nextFactory.isResolveable(name));
    }

    @Override
    public boolean isTarget(String name) {
        return variableResolvers.containsKey(name);
    }

}

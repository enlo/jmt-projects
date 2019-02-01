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
import java.util.Collections;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.UnresolveablePropertyException;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.SimpleVariableResolverFactory;

/**
 *
 * @author enlo
 */
@SuppressWarnings("serial")
@Slf4j
public class LookupVariableResolverFactory extends SimpleVariableResolverFactory {

    /**
     *
     */
    protected final Lookup<String, ?> lookup;

    /**
     *
     * @param parameters
     */
    public LookupVariableResolverFactory(Lookup<String, ?> parameters) {
        this(parameters, null);
    }

    /**
     *
     * @param parameters
     * @param nextFactory
     */
    public LookupVariableResolverFactory(@Nonnull Lookup<String, ?> parameters, VariableResolverFactory nextFactory) {
        super(Collections.EMPTY_MAP);
        this.lookup = parameters;
        this.nextFactory = nextFactory;
    }

    @Override
    public VariableResolver createVariable(String name, Object value) {
        VariableResolver vr;
        try {
            vr = getVariableResolver(name);
        }
        catch (UnresolveablePropertyException e) {
            logger.trace("getVariableResolver() fail. {}", e.getMessage());
            vr = null;
        }
        if (vr instanceof LookupVariableResolver) {
            throw new RuntimeException("variable already defined within scope: " + name);
        }
        else {
            return super.createVariable(name, value);
        }
    }

    /**
     * 自スコープ内
     *
     * @param name
     * @param value
     * @param type
     * @return
     */
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

        if (vr instanceof LookupVariableResolver) {
            throw new RuntimeException("variable already defined within scope: " + vr.getType() + " " + name);
        }
        else {
            return super.createVariable(name, value, type);
        }
    }

    @Override
    public VariableResolver getVariableResolver(String name) {
        VariableResolver vr = variableResolvers.get(name);
        if (vr != null) {
            return vr;
        }
        else if (lookup.containsKey(name)) {
            vr = new LookupVariableResolver(lookup, name);
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
                || (lookup != null && name != null && lookup.containsKey(name))
                || (nextFactory != null && nextFactory.isResolveable(name));
    }

}

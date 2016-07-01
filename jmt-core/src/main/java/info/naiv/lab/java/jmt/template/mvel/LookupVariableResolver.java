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
import org.mvel2.integration.VariableResolver;

/**
 *
 * @author enlo
 */
@SuppressWarnings("serial")
public class LookupVariableResolver implements VariableResolver {

    private final Class<?> knownType;
    private final String name;
    private final Lookup<String, ?> parameters;

    /**
     *
     * @param parameters
     * @param name
     */
    public LookupVariableResolver(Lookup<String, ?> parameters, String name) {
        this.name = name;
        this.knownType = resolveType(parameters, name);
        this.parameters = parameters;
    }

    /**
     *
     * @param parameters
     * @param name
     * @param knownType
     */
    public LookupVariableResolver(Lookup<String, ?> parameters, String name, Class<?> knownType) {
        this.name = name;
        this.knownType = knownType;
        this.parameters = parameters;
    }

    @Override
    public int getFlags() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class getType() {
        return knownType;
    }

    @Override
    public Object getValue() {
        return parameters.get(name);
    }

    /**
     *
     * @param type
     */
    @Override
    public void setStaticType(Class type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Class<?> resolveType(Lookup<String, ?> parameters, String name) {
        if (parameters.containsKey(name)) {
            Object obj = parameters.get(name);
            return obj != null ? obj.getClass() : null;
        }
        return null;
    }

}

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
package info.naiv.lab.java.jmt.jdbc.sql.template.mvel;

import org.mvel2.integration.VariableResolver;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author enlo
 */
@SuppressWarnings("serial")
public class SqlParameterSourceVariableResolver implements VariableResolver {

    private final Class<?> knownType;
    private final String name;
    private final SqlParameterSource parameterSource;

    /**
     *
     * @param parameterSource
     * @param name
     */
    public SqlParameterSourceVariableResolver(SqlParameterSource parameterSource, String name) {
        this.name = name;
        this.knownType = resolveType(parameterSource, name);
        this.parameterSource = parameterSource;
    }

    /**
     *
     * @param parameterSource
     * @param name
     * @param knownType
     */
    public SqlParameterSourceVariableResolver(SqlParameterSource parameterSource, String name, Class<?> knownType) {
        this.name = name;
        this.knownType = knownType;
        this.parameterSource = parameterSource;
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
        return parameterSource.getValue(name);
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

    private Class<?> resolveType(SqlParameterSource parameterSource, String name) {
        if (parameterSource.hasValue(name)) {
            Object obj = parameterSource.getValue(name);
            return obj != null ? obj.getClass() : null;
        }
        return null;
    }

}

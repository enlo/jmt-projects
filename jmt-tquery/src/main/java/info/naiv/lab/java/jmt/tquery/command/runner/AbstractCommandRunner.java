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
package info.naiv.lab.java.jmt.tquery.command.runner;

import info.naiv.lab.java.jmt.tquery.command.Command;
import info.naiv.lab.java.jmt.tquery.command.CommandParameter;
import info.naiv.lab.java.jmt.tquery.command.CommandParameters;
import java.util.List;

/**
 *
 * @author enlo
 * @param <TSession>
 * @param <TQuery>
 * @param <ParameterType>
 */
public abstract class AbstractCommandRunner<TSession extends AutoCloseable, TQuery, ParameterType> {

    public <TResult> List<TResult> list(Command cmd, Class<TResult> clazz) {

        try (TSession session = open()) {
            TQuery query = createQuery(session, cmd.getQuery());
            CommandParameters params = cmd.getParameters();
            for (int i = 1; i <= params.size(); i++) {
                CommandParameter cp = params.get(i);
                Object val = cp.getValue();
                Object typeHint = cp.getTypeHint();
                if (val != null) {
                    setTypedParameter(query, i, val, (ParameterType) typeHint);
                }
                else {
                    setTypedParameterNull(query, i, (ParameterType) typeHint);
                }
            }
            return executeQuery(query, clazz);
        }
        catch (Exception e) {
            throw runtimenize(e);
        }
    }
    protected abstract TQuery createQuery(TSession session, String query);
    
    protected abstract <TResult> List<TResult> executeQuery(TQuery query, Class<TResult> clazz);
    protected abstract TSession open();
    protected RuntimeException runtimenize(Exception e) {
        return new RuntimeException(e);
    }

    protected abstract void setTypedParameter(TQuery query, int index, Object value, ParameterType type);

    protected abstract void setTypedParameterNull(TQuery query, int index, ParameterType type);

}

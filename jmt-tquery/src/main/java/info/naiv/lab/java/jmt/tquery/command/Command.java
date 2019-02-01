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
package info.naiv.lab.java.jmt.tquery.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;

/**
 *
 * @author enlo
 */
@Data
public class Command implements Cloneable, Serializable {

    private static final long serialVersionUID = -7816515353531445011L;


    CommandParameters parameters;
    final String query;

    /**
     * コンストラクタ.
     *
     * @param query
     * @param parameters
     */
    public Command(String query, Collection<CommandParameter> parameters) {
        this(query, new DefaultCommandParameters(parameters));
    }

    /**
     * コンストラクタ.
     *
     * @param query
     * @param parameters
     */
    public Command(String query, CommandParameters parameters) {
        this.query = query;
        this.parameters = parameters;
    }
    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public Command clone() {
        try {
            Command cmd = (Command) super.clone();
            cmd.parameters = new DefaultCommandParameters(this.parameters);
            return cmd;
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    public Object[] getParameterValueArray() {
        return parameters.toValueArray();
    }

    /**
     *
     * @return パラメータ値リスト
     */
    public List<Object> getParameterValueList() {
        List<Object> result = new ArrayList<>(parameters.size());
        for (CommandParameter p : parameters) {
            result.add(p.getValue());
        }
        return result;
    }

}

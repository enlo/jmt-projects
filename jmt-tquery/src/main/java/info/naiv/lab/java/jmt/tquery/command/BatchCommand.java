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
package info.naiv.lab.java.jmt.tquery.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 *
 * @author enlo
 */
@Data
public class BatchCommand implements Cloneable, Serializable {

    private static final long serialVersionUID = -7689234361248547232L;


    List<CommandParameters> parametersList;
    final String query;

    /**
     * コンストラクタ.
     *
     * @param query
     */
    public BatchCommand(String query) {
        this(query, new ArrayList<CommandParameters>());
    }

    /**
     * コンストラクタ.
     *
     * @param query
     * @param parameters
     */
    public BatchCommand(String query, List<CommandParameters> parameters) {
        this.query = query;
        this.parametersList = parameters;
    }
    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public BatchCommand clone() {
        try {
            BatchCommand cmd = (BatchCommand) super.clone();
            cmd.parametersList = new ArrayList<>(parametersList.size());
            for (CommandParameters commandParameters : parametersList) {
                cmd.parametersList.add(new DefaultCommandParameters(commandParameters));
            }
            return cmd;
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    public List<Object[]> getParameterValueArray() {
        List<Object[]> result = new ArrayList<>();
        for (CommandParameters parameters : parametersList) {
            result.add(parameters.toValueArray());
        }
        return result;
    }

    /**
     *
     * @return パラメータ値リスト
     */
    public List<List<Object>> getParameterValueList() {
        List<List<Object>> result = new ArrayList<>();
        for (CommandParameters parameters : parametersList) {
            result.add(Arrays.asList(parameters.toValueArray()));
        }
        return result;
    }

}

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
package info.naiv.lab.java.jmt.tquery;

import info.naiv.lab.java.jmt.tquery.command.CommandParametersBuilder;
import info.naiv.lab.java.jmt.tquery.command.ParameterBinder;
import info.naiv.lab.java.jmt.tquery.command.DefaultParameterBinder;
import lombok.Data;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
@Data
public class QueryContext {

    @NonNull
    ParameterBinder parameterBinder;

    @NonNull
    CommandParametersBuilder parametersBuilder;

    Object source;

    public QueryContext() {
        this.parameterBinder = new DefaultParameterBinder();
        this.parametersBuilder = new CommandParametersBuilder();
    }

    public QueryContext(@NonNull ParameterBinder parameterBinder) {
        this.parameterBinder = parameterBinder;
        this.parametersBuilder = new CommandParametersBuilder();
    }

    public QueryContext(@NonNull CommandParametersBuilder bundleParametersBuilder) {
        this.parameterBinder = new DefaultParameterBinder();
        this.parametersBuilder = bundleParametersBuilder;
    }

    public QueryContext(@NonNull ParameterBinder parameterBinder,
                        @NonNull CommandParametersBuilder bundleParametersBuilder) {
        this.parameterBinder = parameterBinder;
        this.parametersBuilder = bundleParametersBuilder;
    }

}

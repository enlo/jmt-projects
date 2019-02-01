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
package info.naiv.lab.java.jmt.tquery.template.mvel;

import info.naiv.lab.java.jmt.collection.Lookup;
import info.naiv.lab.java.jmt.template.mvel.MvelTemplate;
import info.naiv.lab.java.jmt.tquery.command.Command;
import info.naiv.lab.java.jmt.tquery.command.BatchCommand;
import info.naiv.lab.java.jmt.tquery.template.QueryTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import info.naiv.lab.java.jmt.template.mvel.MvelCompiledTemplateResolver;
import info.naiv.lab.java.jmt.template.mvel.MvelTemplateContextFactory;

/**
 *
 * @author enlo
 */
public class MvelQueryTemplate extends MvelTemplate<Command> implements QueryTemplate {

    public MvelQueryTemplate(String name,
                             MvelCompiledTemplateResolver templateResolver,
                             MvelTemplateContextFactory<Command> contextFactory) {
        super(name, templateResolver, contextFactory);
    }

    @Override
    public List<BatchCommand> mergeLookupList(List<Lookup<String, String>> parameters) {
        Map<String, BatchCommand> cmd = new HashMap<>(parameters.size());
        for (Lookup<String, String> param : parameters) {
            Command c = merge(param);
            String q = c.getQuery();
            BatchCommand bc = cmd.get(q);
            if (bc == null) {
                bc = new BatchCommand(q);
                cmd.put(c.getQuery(), bc);
            }
            bc.getParametersList().add(c.getParameters());
        }
        return new ArrayList<>(cmd.values());
    }

    @Override
    public List<BatchCommand> mergeMapList(List<Map<String, String>> parameters) {
        Map<String, BatchCommand> cmd = new HashMap<>(parameters.size());
        for (Map<String, String> param : parameters) {
            Command c = merge(param);
            String q = c.getQuery();
            BatchCommand bc = cmd.get(q);
            if (bc == null) {
                bc = new BatchCommand(q);
                cmd.put(c.getQuery(), bc);
            }
            bc.getParametersList().add(c.getParameters());
        }
        return new ArrayList<>(cmd.values());
    }

}

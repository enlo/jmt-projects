/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt.support.jdbc.sql.velocity;

import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.support.jdbc.sql.RuntimeSQLException;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlParameters;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlQuery;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlTemplate;
import static info.naiv.lab.java.jmt.support.jdbc.sql.velocity.VelocitySqlUtils.setArgumentParameters;
import static info.naiv.lab.java.jmt.support.jdbc.sql.velocity.VelocitySqlUtils.setResultParameters;
import java.io.*;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author enlo
 */
@Slf4j
@Getter
public abstract class AbstractVelocitySqlTemplate implements SqlTemplate {

    protected final VelocitySqlTemplateEngine engine;

    public AbstractVelocitySqlTemplate() {
        engine = getThreadContainer().resolveService(VelocitySqlTemplateEngine.class);
    }

    @Override
    public SqlQuery merge(Map<String, Object> parameters) {

        SqlParameters resultParams = new SqlParameters();
        SqlParameters argumentParams = new SqlParameters();

        VelocityContext context = new VelocityContext(parameters);
        setResultParameters(context, resultParams);
        setArgumentParameters(context, argumentParams);

        try (Writer writer = new StringWriter()) {
            mergeTemplate(writer, context);
            String sql = writer.toString();
            return new SqlQuery(sql, resultParams);
        }
        catch (IOException e) {
            logger.trace(e.getMessage());
            throw new RuntimeSQLException(e);
        }
    }

    protected abstract void mergeTemplate(Writer writer, VelocityContext context) throws IOException;
}

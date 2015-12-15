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

import static info.naiv.lab.java.jmt.Misc.isLoadable;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlParameter;
import static info.naiv.lab.java.jmt.support.jdbc.sql.SqlParameterFactory.from;
import info.naiv.lab.java.jmt.support.jdbc.sql.SqlParameters;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.CommonsLogLogChute;
import org.apache.velocity.runtime.parser.node.Node;
import org.springframework.ui.velocity.VelocityEngineFactory;

@Slf4j
public class VelocitySqlUtils {

    private static final String SQL_PARAMETERS_ARGUMENT = "SqlParameters.Argument";
    private static final String SQL_PARAMETERS_RESULT = "SqlParameters.Result";

    public static VelocityEngine createVelocityEngine() {
        VelocityEngine ve = null;
        try {
            if (isLoadable("org.springframework.ui.velocity.VelocityEngineFactory")) {
                VelocityEngineFactory vef = new VelocityEngineFactory();
                vef.setResourceLoaderPath("classpath:/");
                ve = vef.createVelocityEngine();
            }
        }
        catch (IOException | RuntimeException | Error ex) {
            logger.warn("spring VelocityEngineFactory is failed.", ex);
        }
        if (ve == null) {
            ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new CommonsLogLogChute());
            ve.setProperty(RuntimeConstants.RESOURCE_MANAGER_CLASS, "class");
        }
        return ve;
    }

    public static SqlParameters getArgumentParameters(Context context) {
        return (SqlParameters) context.get(SQL_PARAMETERS_ARGUMENT);
    }

    public static SqlParameter getOrCreateParameter(Context context, String name, Object object) {
        SqlParameters params = getArgumentParameters(context);
        SqlParameter result = params.getFirstOrNull(name);
        if (result == null) {
            result = from(name, object);
            params.add(result);
        }
        return result;
    }

    public static SqlParameters getResultParameters(Context context) {
        return (SqlParameters) context.get(SQL_PARAMETERS_RESULT);
    }

    public static String getVarName(Node node) {
        String x = node.literal();
        return x;
    }

    public static void setArgumentParameters(Context context, SqlParameters params) {
        context.put(SQL_PARAMETERS_ARGUMENT, params);
    }

    public static void setResultParameters(Context context, SqlParameters params) {
        context.put(SQL_PARAMETERS_RESULT, params);
    }

    private VelocitySqlUtils() {
    }
}

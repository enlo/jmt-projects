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
package info.naiv.lab.java.jmt.support.log4j2;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Misc.isNotEmpty;
import java.net.URI;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 *
 * @author enlo
 */
public class Log4j2JmtConfigurer {

    public static final String LOG4J2_CONFIG_PATH_KEY = "log4j2.config.path";
    public static final String LOG4J2_CONFIG_PATH_DEFAULT = "classpath:META-INF/jmt.log4j2.config.xml";

    public static void configure(Properties jmtProperties) {
        nonNull(jmtProperties, "jmtProperties");
        String location = jmtProperties.getProperty(LOG4J2_CONFIG_PATH_KEY);
        doConfigure(jmtProperties, location);
    }

    public static void configure(Properties jmtProperties, String resourcePath) {
        nonNull(jmtProperties, "jmtProperties");
        doConfigure(jmtProperties, resourcePath);
    }

    private static void doConfigure(Properties jmtProperties, String resourcePath) {
        Log4j2JmtLookup.configure(jmtProperties);
        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        if (isNotEmpty(resourcePath)) {
            URI location = URI.create(resourcePath);
            lc.setConfigLocation(location);
        } else {
            lc.reconfigure();
        }
    }

    private Log4j2JmtConfigurer() {
    }
}

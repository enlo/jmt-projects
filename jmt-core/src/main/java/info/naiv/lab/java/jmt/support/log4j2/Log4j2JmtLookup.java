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

import info.naiv.lab.java.jmt.ResolvableProperties;
import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.status.StatusLogger;

/**
 *
 * @author enlo
 */
@Plugin(name = "jmt", category = StrLookup.CATEGORY)
public class Log4j2JmtLookup extends AbstractLookup implements SystemComponent {

    private static final String DEFAULT = "jmtDefault";

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");

    private static final ConcurrentMap defaultProperties;
    private static final ConcurrentMap<String, ConcurrentMap<String, String>> loggerProperties = new ConcurrentHashMap<>();

    static {
        ConcurrentMap map = new ConcurrentHashMap<>();
        map.put("log4j2.root.level", "ERROR");
        map.put("log4j2.file.dir", "log");
        map.put("log4j2.file.basename", "jmt-log");
        map.put("log4j2.file.size", "10 MB");
        map.put("log4j2.file.rollover", "90");
        defaultProperties = map;
        PluginManager.addPackage(Log4j2JmtLookup.class.getPackage().getName());
    }

    /**
     *
     * @param properties
     */
    public static void configure(Properties properties) {
        configure(DEFAULT, properties);
    }

    /**
     *
     * @param name
     * @param properties
     */
    public static void configure(String name, Properties properties) {
        configureMap(name, new ResolvableProperties(properties).toMap());
    }

    /**
     *
     * @param name
     * @param properties
     */
    static public void configureMap(String name, ConcurrentMap<String, String> properties) {
        if (DEFAULT.equals(name)) {
            defaultProperties.putAll(properties);
        }
        loggerProperties.put(name, properties);
    }

    @Override
    public String lookup(LogEvent logEvent, String key) {
        try {
            ConcurrentMap props = defaultProperties;
            if (logEvent != null) {
                props = getProperties(logEvent.getLoggerName());
            }
            String value = getValue(props, key, null);
            if (value == null) {
                value = getValue(defaultProperties, key, "");
            }
            return value;
        }
        catch (RuntimeException | Error ex) {
            LOGGER.warn(LOOKUP, "Error while getting property [{}].", key, ex);
            return "";
        }
    }

    private ConcurrentMap getProperties(String name) {
        if (DEFAULT.equals(name)) {
            return defaultProperties;
        }
        else {
            ConcurrentMap p = loggerProperties.get(name);
            if (p == null) {
                return defaultProperties;
            }
            return p;
        }
    }

    private String getValue(ConcurrentMap<String, String> props, String key, String defaultValue) {
        if (props == null) {
            return defaultValue;
        }
        else {
            String value = props.get(key);
            if (value == null) {
                value = defaultValue;
            }
            return value;
        }
    }
}

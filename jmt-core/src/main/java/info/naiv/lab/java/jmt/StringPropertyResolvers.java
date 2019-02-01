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
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.collection.Lookup;
import static java.util.Arrays.asList;
import java.util.Map;
import java.util.Properties;

public class StringPropertyResolvers {

    public static StringPropertyResolver EMPTY = new StringPropertyResolver() {
        @Override
        public boolean containsProperty(String key) {
            return false;
        }

        @Override
        public String getProperty(String key) {
            return null;
        }

        @Override
        public String getProperty(String key, String defaultValue) {
            return defaultValue;
        }

    };

    public static StringPropertyResolver compose(final StringPropertyResolver... resolvers) {
        return compose(asList(resolvers));
    }

    public static StringPropertyResolver compose(final Iterable<StringPropertyResolver> resolvers) {
        return new StringPropertyResolver() {
            @Override
            public boolean containsProperty(String key) {
                for (StringPropertyResolver resolver : resolvers) {
                    if (resolver.containsProperty(key)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getProperty(String key) {
                return getProperty(key, null);
            }

            @Override
            public String getProperty(String key, String defaultValue) {
                for (StringPropertyResolver resolver : resolvers) {
                    String value = resolver.getProperty(key);
                    if (value != null) {
                        return value;
                    }
                }
                return defaultValue;
            }
        };
    }

    public static StringPropertyResolver wrap(final Properties props) {
        return new StringPropertyResolver() {
            @Override
            public boolean containsProperty(String key) {
                return props.getProperty(key) != null;
            }

            @Override
            public String getProperty(String key) {
                return props.getProperty(key);
            }

            @Override
            public String getProperty(String key, String defaultValue) {
                return props.getProperty(key, defaultValue);
            }
        };
    }

    public static StringPropertyResolver wrap(final Lookup<String, String> props) {
        return new StringPropertyResolver() {
            @Override
            public boolean containsProperty(String key) {
                return props.containsKey(key);
            }

            @Override
            public String getProperty(String key) {
                return props.get(key);
            }

            @Override
            public String getProperty(String key, String defaultValue) {
                String val = props.get(key);
                return val != null ? val : defaultValue;
            }
        };
    }

    public static StringPropertyResolver wrap(final Map<String, String> props) {
        return new StringPropertyResolver() {
            @Override
            public boolean containsProperty(String key) {
                return props.containsKey(key);
            }

            @Override
            public String getProperty(String key) {
                return props.get(key);
            }

            @Override
            public String getProperty(String key, String defaultValue) {
                String val = props.get(key);
                return val != null ? val : defaultValue;
            }
        };
    }

}

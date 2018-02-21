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
package info.naiv.lab.java.jmt.support.spring.env;

import info.naiv.lab.java.jmt.StringPropertyResolver;
import info.naiv.lab.java.jmt.StringPropertyResolvers;
import info.naiv.lab.java.jmt.collection.Lookup;
import java.util.Map;
import java.util.Properties;
import org.springframework.core.env.PropertySource;

/**
 *
 * @author enlo
 */
public class StringPropertyResolverPropertySource extends PropertySource<StringPropertyResolver> {

    public StringPropertyResolverPropertySource(String name, Lookup<String, String> source) {
        super(name, StringPropertyResolvers.wrap(source));
    }

    public StringPropertyResolverPropertySource(String name, Map<String, String> source) {
        super(name, StringPropertyResolvers.wrap(source));
    }

    public StringPropertyResolverPropertySource(String name, Properties source) {
        super(name, StringPropertyResolvers.wrap(source));
    }

    public StringPropertyResolverPropertySource(String name, StringPropertyResolver resolver) {
        super(name, resolver);
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }

    @Override
    public boolean containsProperty(String name) {
        return source.containsProperty(name);
    }

}

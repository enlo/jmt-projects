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
package info.naiv.lab.java.jmt.support.spring;

import info.naiv.lab.java.jmt.ResolvableProperties;
import java.io.IOException;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;

/**
 *
 * @author enlo
 */
@Slf4j
public class ExternalPropertiesPropertySource extends PropertySource<ResolvableProperties> {

    /**
     *
     * @param name
     */
    public ExternalPropertiesPropertySource(String name) {
        super(name, new ResolvableProperties());
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }

    /**
     *
     * @param location
     * @throws IOException
     */
    public final void load(String location) throws IOException {
        source.loadFromResource(location);
    }

    /**
     *
     * @param location
     * @param charset
     * @throws IOException
     */
    public final void load(String location, Charset charset) throws IOException {
        source.loadFromResource(location, charset);
    }

    /**
     *
     * @param location
     */
    public final void loadOptional(String location) {
        try {
            source.loadFromResource(location);
        }
        catch (IOException ex) {
            logger.warn("{} load failed. {}", location, ex.getMessage());
        }
    }

    /**
     *
     * @param location
     * @param charset
     */
    public final void loadOptional(String location, Charset charset) {
        try {
            source.loadFromResource(location, charset);
        }
        catch (IOException ex) {
            logger.warn("{} load failed. {}", location, ex.getMessage());
        }
    }
}

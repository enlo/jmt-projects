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
package info.naiv.lab.java.jmt.template;

import info.naiv.lab.java.jmt.io.NIOUtils;
import info.naiv.lab.java.jmt.io.ResourceRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 * @param <TResult>
 */
@Getter
@Setter
public class DelegatingTemplateLoader<TResult> extends AbstractResourceTemplateLoader<TResult> implements InitializingBean {

    private ResourceRepository resourceRepository;
    private TemplateBuilder templateBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (templateBuilder == null) {
            throw new NullPointerException("templateBuilder is null.");
        }
        if (resourceRepository == null) {
            throw new NullPointerException("resourceRepository is null.");
        }
    }

    @Override
    protected Template<TResult> createTemplateFromResource(String name, Resource resource, Charset charset) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            String template = NIOUtils.toString(is, charset);
            return templateBuilder.build(name, new DefaultTemplateSourceResolver(template));
        }
    }

    @Override
    protected Template<TResult> doFromString(String name, String template) {
        return templateBuilder.build(name, new DefaultTemplateSourceResolver(template));
    }

}

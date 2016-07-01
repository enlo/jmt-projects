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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 * @param <TResult>
 */
@Slf4j
public abstract class AbstractTemplateLoader<TResult> implements TemplateLoader<TResult> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final Set<TemplateLoaderListener<TResult>> listnenrs = new ConcurrentSkipListSet<>();
    private TemplateLoader<TResult> parentTemplateLoader;

    @Setter
    private String suffix = DEFAULT_SUFFIX;

    @Override
    public boolean addTemplateLoaderListener(@NonNull TemplateLoaderListener listener) {
        return listnenrs.add(listener);
    }

    @Override
    public Template<TResult> fromString(String template) {
        return fromString(UUID.randomUUID().toString(), template);
    }

    @Override
    public Template<TResult> fromString(String name, String template) {
        needInitialize();
        Template<TResult> templ = doFromString(name, template);
        send(templ);
        return templ;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public Template<TResult> load(String category, String name) {
        return load(category, name, StandardCharsets.UTF_8);
    }

    @Override
    public Template<TResult> load(String category, String name, Charset charset) {
        Template<TResult> result = null;
        needInitialize();
        try {
            result = doLoad(category, name, charset);
            send(result);
            return result;
        }
        catch (IOException ex) {
            logger.debug("template load failed.", ex);
        }
        if (result == null && parentTemplateLoader != null) {
            result = parentTemplateLoader.load(category, name, charset);
            if (result != null) {
                send(result);
            }
        }
        return result;
    }

    @Override
    public Iterable<Template<TResult>> loadCategory(String category) {
        return loadCategory(category, StandardCharsets.UTF_8);
    }

    @Override
    public Iterable<Template<TResult>> loadCategory(String category, Charset charset) {
        Iterable<Template<TResult>> result = null;
        needInitialize();
        try {
            result = doLoadCategory(category, charset);
            return result;
        }
        catch (IOException ex) {
            logger.debug("template load failed.", ex);
        }
        if (result == null && parentTemplateLoader != null) {
            result = parentTemplateLoader.loadCategory(category, charset);
        }
        return result;
    }

    @Override
    public boolean removeTemplateLoaderListener(TemplateLoaderListener listener) {
        if (listener == null) {
            return false;
        }
        return listnenrs.remove(listener);
    }

    @Override
    public void setParentTemplateLoader(TemplateLoader<? extends TResult> parentLoader) {
        parentTemplateLoader = (TemplateLoader<TResult>) parentLoader;
    }

    private void send(Template<TResult> templ) {
        if (templ != null) {
            for (TemplateLoaderListener<TResult> listener : listnenrs) {
                listener.onLoadTemplate(templ);
            }
        }
    }

    /**
     *
     * @param name
     * @param template
     * @return
     */
    @Nonnull
    protected abstract Template<TResult> doFromString(String name, String template);

    /**
     *
     * @param category
     * @param name
     * @param charset
     * @return
     * @throws IOException
     */
    protected abstract Template<TResult> doLoad(String category, String name, Charset charset) throws IOException;

    /**
     *
     * @param category
     * @param charset
     * @return
     * @throws IOException
     */
    @Nonnull
    protected abstract Iterable<Template<TResult>> doLoadCategory(String category, Charset charset) throws IOException;

    /**
     *
     */
    protected void initialize() {

    }

    /**
     *
     */
    protected void needInitialize() {
        if (initialized.compareAndSet(false, true)) {
            initialize();
        }
    }

}

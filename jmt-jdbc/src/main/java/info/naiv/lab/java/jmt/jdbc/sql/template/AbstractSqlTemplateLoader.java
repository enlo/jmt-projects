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
package info.naiv.lab.java.jmt.jdbc.sql.template;

import static info.naiv.lab.java.jmt.Misc.isEmpty;
import info.naiv.lab.java.jmt.jdbc.sql.dialect.Dialect;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public abstract class AbstractSqlTemplateLoader implements SqlTemplateLoader {

    @Getter
    @Setter
    private Dialect dialect;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private SqlTemplateLoader parentLoader;

    @Setter
    private String suffix = DEFAULT_SUFFIX;

    @Override
    public final SqlTemplate fromString(String template) {
        return fromString(UUID.randomUUID().toString(), template);
    }

    @Override
    public SqlTemplate fromString(String name, String template) {
        needInitialize();
        return doFromString(name, template);
    }

    @Override
    public final String getSuffix() {
        if (isEmpty(suffix)) {
            if (dialect != null) {
                return dialect.getKeyword();
            }
        }
        return suffix;
    }

    @Override
    public final SqlTemplate load(String category, String name) {
        return load(category, name, StandardCharsets.UTF_8);
    }

    @Override
    public final SqlTemplate load(String category, String name, Charset charset) {
        SqlTemplate st = null;
        needInitialize();
        try {
            st = doLoad(category, name, charset);
            return st;
        }
        catch (IOException ex) {
            logger.debug("template load failed.", ex);
        }
        if (st == null && parentLoader != null) {
            st = parentLoader.load(category, name, charset);
        }
        return st;
    }

    @Override
    public final Iterable<SqlTemplate> loadCategory(String category) {
        return loadCategory(category, StandardCharsets.UTF_8);
    }

    @Override
    public final Iterable<SqlTemplate> loadCategory(String category, Charset charset) {
        Iterable<SqlTemplate> itr = null;
        try {
            needInitialize();
            itr = doLoadCategory(category, charset);
        }
        catch (IOException ex) {
            logger.debug("template load failed.", ex);
        }
        if (isEmpty(itr) && parentLoader != null) {
            itr = parentLoader.loadCategory(category, charset);
        }
        return itr;
    }

    /**
     *
     * @param parentLoader
     */
    @Override
    public void setParent(SqlTemplateLoader parentLoader) {
        this.parentLoader = parentLoader;
    }

    /**
     *
     * @param name
     * @param template
     * @return
     */
    protected abstract SqlTemplate doFromString(String name, String template);

    /**
     *
     * @param category
     * @param name
     * @param charset
     * @return
     * @throws IOException
     */
    protected abstract SqlTemplate doLoad(String category, String name, Charset charset) throws IOException;

    /**
     *
     * @param category
     * @param charset
     * @return
     * @throws IOException
     */
    protected abstract Iterable<SqlTemplate> doLoadCategory(String category, Charset charset) throws IOException;

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

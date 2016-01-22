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
import info.naiv.lab.java.jmt.jdbc.sql.RuntimeSQLException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author enlo
 */
public abstract class AbstractSqlTemplateLoader implements SqlTemplateLoader {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private SqlTemplateLoader parentLoader;
    @Getter
    @Setter
    private String suffix = DEFAULT_SUFFIX;

    @Override
    public final SqlTemplate formString(String template) {
        needInitialize();
        return doFromString(template);
    }

    @Override
    public final SqlTemplate load(String category, String name) {
        return load(category, name, StandardCharsets.UTF_8);
    }

    @Override
    public final SqlTemplate load(String category, String name, Charset charset) {
        try {
            needInitialize();
            SqlTemplate st = doLoad(category, name, charset);
            if (st == null && parentLoader != null) {
                st = parentLoader.load(category, name, charset);
            }
            return st;
        }
        catch (IOException ex) {
            throw new RuntimeSQLException("template load failed.", ex);
        }
    }

    @Override
    public final Iterable<SqlTemplate> loadCategory(String category) {
        return loadCategory(category, StandardCharsets.UTF_8);
    }

    @Override
    public final Iterable<SqlTemplate> loadCategory(String category, Charset charset) {
        try {
            needInitialize();
            Iterable<SqlTemplate> itr = doLoadCategory(category, charset);
            if (isEmpty(itr) && parentLoader != null) {
                itr = parentLoader.loadCategory(category, charset);
            }
            return itr;
        }
        catch (IOException ex) {
            throw new RuntimeSQLException("template load failed.", ex);
        }
    }

    @Override
    public void setParent(SqlTemplateLoader parentLoader) {
        this.parentLoader = parentLoader;
    }

    protected abstract SqlTemplate doFromString(String template);

    protected void initialize() {

    }

    protected abstract SqlTemplate doLoad(String category, String name, Charset charset) throws IOException;

    protected abstract Iterable<SqlTemplate> doLoadCategory(String category, Charset charset) throws IOException;

    protected void needInitialize() {
        if (initialized.compareAndSet(false, true)) {
            initialize();
        }
    }

}

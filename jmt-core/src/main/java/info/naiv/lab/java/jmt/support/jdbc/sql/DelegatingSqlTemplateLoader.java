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
package info.naiv.lab.java.jmt.support.jdbc.sql;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author enlo
 */
public class DelegatingSqlTemplateLoader implements SqlTemplateLoader {

    SqlTemplateLoader defaultLoader;
    final List<SqlTemplateLoader> loaderList = new CopyOnWriteArrayList<>();

    String suffix = DEFAULT_SUFFIX;

    public void addLoader(SqlTemplateLoader loader) {
        this.loaderList.add(loader);
    }

    @Override
    public SqlTemplate formString(String template) {
        if (loaderList.isEmpty()) {
            return defaultLoader.formString(template);
        } else {
            return loaderList.get(0).formString(template);
        }
    }

    @Override
    public String getSuffix() {
        return defaultLoader.getSuffix();
    }

    @Override
    public void initialize() {
        for (SqlTemplateLoader loader : loaderList) {
            loader.initialize();
        }
        SqlTemplateLoader dl = defaultLoader;
        if (dl != null) {
            dl.initialize();
        }
    }

    public void insertLoader(int position, SqlTemplateLoader loader) {
        this.loaderList.add(position, loader);
    }

    @Override
    public SqlTemplate load(String category, String name) {
        for (SqlTemplateLoader loader : loaderList) {
            SqlTemplate templ = loader.load(category, name);
            if (templ != null) {
                return templ;
            }
        }
        SqlTemplateLoader dl = defaultLoader;
        if (dl != null) {
            return dl.load(category, name);
        }
        return null;
    }

    @Override
    public SqlTemplate load(String category, String name, Charset charset) {
        for (SqlTemplateLoader loader : loaderList) {
            SqlTemplate templ = loader.load(category, name, charset);
            if (templ != null) {
                return templ;
            }
        }
        SqlTemplateLoader dl = defaultLoader;
        if (dl != null) {
            return dl.load(category, name, charset);
        }
        return null;
    }

    @Override
    public Iterable<SqlTemplate> loadCategory(String category) {
        for (SqlTemplateLoader loader : loaderList) {
            Iterable<SqlTemplate> templ = loader.loadCategory(category);
            if (templ != null) {
                return templ;
            }
        }
        SqlTemplateLoader dl = defaultLoader;
        if (dl != null) {
            return dl.loadCategory(category);
        }
        return null;
    }

    @Override
    public Iterable<SqlTemplate> loadCategory(String category, Charset charset) {
        for (SqlTemplateLoader loader : loaderList) {
            Iterable<SqlTemplate> templ = loader.loadCategory(category, charset);
            if (templ != null) {
                return templ;
            }
        }
        SqlTemplateLoader dl = defaultLoader;
        if (dl != null) {
            return dl.loadCategory(category, charset);
        }
        return null;
    }

    public void removeLoader(SqlTemplateLoader loader) {
        this.loaderList.remove(loader);
    }

    public void setDefaultLoader(SqlTemplateLoader defaultLoader) {
        this.defaultLoader = defaultLoader;
    }

    @Override
    public void setSuffix(String suffix) {
        throw new UnsupportedOperationException("do not set suffix.");
    }

}

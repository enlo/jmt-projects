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
package info.naiv.lab.java.jmt.jdbc.sql.template;

import info.naiv.lab.java.jmt.io.ResourceRepository;
import info.naiv.lab.java.jmt.io.SuffixAndExtensionFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
public abstract class AbstractResourceSqlTemplateLoader extends AbstractSqlTemplateLoader {

    @Getter
    @Setter
    private String extension = "sql";

    /**
     *
     * @param name
     * @param resource
     * @param charset
     * @return
     * @throws IOException
     */
    protected abstract SqlTemplate createSqlTemplateFromResource(String name, Resource resource, Charset charset) throws IOException;

    /**
     *
     * @param category
     * @param name
     * @param charset
     * @return
     * @throws IOException
     */
    @Override
    protected SqlTemplate doLoad(String category, String name, Charset charset) throws IOException {
        SuffixAndExtensionFilter filter = new SuffixAndExtensionFilter(name, getSuffix(), getExtension(), true);
        Resource res = getResourceRepository().getResource(category, filter);
        if (res != null) {
            return createSqlTemplateFromResource(res.getFilename(), res, charset);
        }
        else {
            return null;
        }
    }

    /**
     *
     * @param category
     * @param charset
     * @return
     * @throws IOException
     */
    @Override
    protected Iterable<SqlTemplate> doLoadCategory(String category, Charset charset) throws IOException {
        SuffixAndExtensionFilter filter = new SuffixAndExtensionFilter("[^.]+", getSuffix(), getExtension(), true);
        Map<String, Resource> list = getResourceRepository().getResources(category, filter);
        List<SqlTemplate> result = new ArrayList<>(list.size());
        for (Resource res : list.values()) {
            SqlTemplate st = createSqlTemplateFromResource(res.getFilename(), res, charset);
            if (st != null) {
                result.add(st);
            }
        }
        return result;
    }

    /**
     *
     * @return
     */
    protected abstract ResourceRepository getResourceRepository();

}

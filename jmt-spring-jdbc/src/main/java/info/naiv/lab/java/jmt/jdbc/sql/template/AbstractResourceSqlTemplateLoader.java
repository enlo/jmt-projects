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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import info.naiv.lab.java.jmt.infrastructure.PackageResourceResolver;
import info.naiv.lab.java.jmt.infrastructure.PackageResourceResolverFactory;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.io.NIOUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractResourceSqlTemplateLoader extends AbstractSqlTemplateLoader {

    protected static PackageResourceResolver createResolver() {
        PackageResourceResolverFactory resolverFactory
                = getThreadContainer()
                .resolveService(PackageResourceResolverFactory.class);
        return resolverFactory.newInstanceForResource();
    }
    
    @Getter
    @Setter
    private String extension = "sql";

    @Getter
    @Setter
    private PackageResourceResolver resourceResolver;

    @Getter
    private String rootPackage;

    private Set<String> templates;

    public AbstractResourceSqlTemplateLoader() {
        this(createResolver());
    }

    public AbstractResourceSqlTemplateLoader(PackageResourceResolver resourceResolver) {
        this.rootPackage = "SQL";
        this.resourceResolver = resourceResolver;
        this.resourceResolver.setPackageNames(rootPackage);
    }

    @Override
    public Iterable<SqlTemplate> doLoadCategory(String category, Charset charset) {
        Set<String> list = filterByCategory(templates, category);
        Collection<Path> paths = NIOUtils.listByFilenameWithSuffixAndExtention(list, ".+", getSuffix(), getExtension());
        List<SqlTemplate> result = new ArrayList<>();
        for (Path path : paths) {
            SqlTemplate st = doLoadOneFile(path, charset);
            if (st != null) {
                result.add(st);
            }
        }
        return result;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
        this.resourceResolver.setPackageNames(rootPackage);
    }

    private void createFilelist() {
        Set<String> filelist = resourceResolver.list();
        templates = filelist;
    }

    private Set<String> filterByCategory(Set<String> templates, String category) {
        Set<String> filtered = new HashSet<>();
        for (String item : templates) {
            if (item.startsWith(arrayToString(rootPackage, "/", category))) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    protected abstract SqlTemplate createSqlTemplateByFile(String path, Charset charset) throws IOException;

    @Override
    protected void doInitialize() {
        createFilelist();
    }

    @Override
    protected SqlTemplate doLoad(String category, String name, Charset charset) {
        Set<String> list = filterByCategory(templates, category);
        Path path = NIOUtils.findByFilenameWithSuffixAndExtention(list, name, getSuffix(), getExtension());
        return doLoadOneFile(path, charset);
    }

    protected SqlTemplate doLoadOneFile(Path path, Charset charset) {
        try {
            return createSqlTemplateByFile(path.toString(), charset);
        }
        catch (IOException | RuntimeException e) {
            logger.debug("'{}' not found - {}", path, e);
            return null;
        }
    }

}

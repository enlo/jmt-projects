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
package info.naiv.lab.java.jmt.io;

import info.naiv.lab.java.jmt.StringJoiner;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 *
 * @author enlo
 */
public class ClassPathResourceRepository extends AbstractResourceRepository {

    private static final StringJoiner joiner = StringJoiner.valueOf("/");

    @Getter
    private String rootPath;

    @NonNull
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private ResourcePatternResolver resolver;

    @Override
    protected ResourceLoader getResourceLoader() {
        return resolver;
    }

    /**
     *
     */
    public ClassPathResourceRepository() {
        resolver = new PathMatchingResourcePatternResolver();
    }

    /**
     *
     * @param location
     */
    public ClassPathResourceRepository(String location) {
        resolver = new PathMatchingResourcePatternResolver();
        rootPath = location;
    }

    /**
     *
     * @param location
     */
    public final void setRootPath(String location) {
        rootPath = location;
    }

    private Resource[] listResources(String category, String globPattern) throws IOException {
        if (globPattern == null) {
            globPattern = "*";
        }
        String pattern = joiner.join(new String[]{ResourceLoader.CLASSPATH_URL_PREFIX, rootPath, category, globPattern}).toString();
        return resolver.getResources(pattern);
    }

    /**
     *
     * @param category
     * @param name
     * @return
     */
    @Override
    protected String resolveLocation(String category, String name) {
        return joiner.join(new String[]{rootPath, category, name}).toString();
    }

    /**
     *
     * @param category
     * @param globPattern
     * @return
     * @throws IOException
     */
    @Override
    protected Map<String, String> resolveLocations(String category, String globPattern) throws IOException {
        Map<String, String> result = new HashMap<>();
        Resource[] resources = listResources(category, globPattern);
        for (Resource res : resources) {
            if (res.isReadable()) {
                String name = res.getFilename();
                if (name != null) {
                    result.put(name, res.getURL().toString());
                }
            }
        }
        return result;
    }
}

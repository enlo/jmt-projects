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
package info.naiv.lab.java.jmt.infrastructure;

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.infrastructure.typefilter.AnyTypeFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;

/**
 *
 * @author enlo
 */
@Getter
@Setter
@Slf4j
public class AnnotationMetadataSetResolver {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    static final AntPathMatcher matcher = new AntPathMatcher("/");

    /**
     *
     * @return
     */
    public static AnnotationMetadataSetResolver newAnyType() {
        AnnotationMetadataSetResolver resolver = new AnnotationMetadataSetResolver();
        resolver.includeTypeFilters.add(AnyTypeFilter.INSTANCE);
        return resolver;
    }

    private final List<TypeFilter> excludeTypeFilters = new CopyOnWriteArrayList<>();
    private final List<TypeFilter> includeTypeFilters = new CopyOnWriteArrayList<>();

    @NonNull
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

    private PropertyResolver propertyResolver;

    @NonNull
    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    @NonNull
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     *
     * @param basePackage
     * @return
     * @throws IOException
     */
    public Set<AnnotationMetadata> resolve(String basePackage) throws IOException {
        return resolve(basePackage, null);
    }

    /**
     *
     * @param basePackage
     * @param predicate
     * @return
     * @throws IOException
     */
    public Set<AnnotationMetadata> resolve(String basePackage, Predicate1<AnnotationMetadata> predicate) throws IOException {
        String packageSearchPath = arrayToString(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX,
                                                 resolveBasePackage(basePackage), "/", this.resourcePattern);
        Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
        Set<AnnotationMetadata> metadataSet = new HashSet<>(resources.length);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    if (isTarget(metadataReader)) {
                        AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
                        if (predicate == null || predicate.test(metadata)) {
                            metadataSet.add(metadata);
                        }
                    }
                }
                catch (IOException ex) {
                    logger.debug("metadata read error.", ex);
                }
            }
        }
        return metadataSet;
    }

    /**
     *
     * @param metadataReader
     * @return
     * @throws IOException
     */
    protected boolean isTarget(MetadataReader metadataReader) throws IOException {
        List<TypeFilter> filters = getExcludeTypeFilters();
        MetadataReaderFactory factory = getMetadataReaderFactory();
        for (TypeFilter filter : filters) {
            if (filter.match(metadataReader, factory)) {
                return false;
            }
        }
        filters = getIncludeTypeFilters();
        for (TypeFilter filter : filters) {
            if (filter.match(metadataReader, factory)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param basePackage
     * @return
     */
    protected String resolveBasePackage(String basePackage) {
        if (propertyResolver != null) {
            basePackage = propertyResolver.resolveRequiredPlaceholders(basePackage);
        }
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }

}

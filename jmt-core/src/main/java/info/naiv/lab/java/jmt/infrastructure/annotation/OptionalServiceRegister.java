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
package info.naiv.lab.java.jmt.infrastructure.annotation;

import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.infrastructure.AnnotationMetadataSetResolver;
import info.naiv.lab.java.jmt.infrastructure.ServiceConnection;
import info.naiv.lab.java.jmt.infrastructure.ServiceContainer;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

/**
 *
 * @author enlo
 */
@Slf4j
public class OptionalServiceRegister {

    private static final TypeFilter FILTER = new AnnotationTypeFilter(OptionalService.class);

    @Getter
    final AnnotationMetadataSetResolver resolver = new AnnotationMetadataSetResolver();

    /**
     *
     */
    public OptionalServiceRegister() {
        resolver.getIncludeTypeFilters().add(FILTER);
    }

    /**
     *
     * @param container
     * @param scanPackage
     * @return
     */
    public List<ServiceConnection> register(ServiceContainer container, String scanPackage) {
        List<ServiceConnection> result = new ArrayList<>();
        try {
            Set<AnnotationMetadata> metadataSet = resolver.resolve(scanPackage, null);
            for (AnnotationMetadata metadata : metadataSet) {
                for (Class<?> clazz : Misc.resolveClassName(metadata.getClassName())) {
                    OptionalService anno = AnnotationUtils.findAnnotation(clazz, OptionalService.class);
                    if (checkLoadable(anno.dependOn())) {
                        Object obj = Misc.newInstance(clazz);
                        if (obj != null) {
                            result.add(container.registerService(obj));
                        }
                    }
                }
            }
        }
        catch (IOException ex) {
            logger.debug("register failed.", ex);
        }
        return result;
    }

    /**
     *
     * @param scanPackage
     * @return
     */
    public List<ServiceConnection> register(String scanPackage) {
        return register(ServiceProviders.getSystemContainer(), scanPackage);
    }

    /**
     *
     * @param depends
     * @return
     */
    protected boolean checkLoadable(String[] depends) {
        if (depends != null) {
            for (String depend : depends) {
                if (!Misc.isLoadable(depend)) {
                    return false;
                }
            }
        }
        return true;
    }
}

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
package info.naiv.lab.java.jmt.infrastructure.preload;

import info.naiv.lab.java.jmt.infrastructure.AnnotationMetadataSetResolver;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public abstract class AbstractClassPreloader implements ClassPreloader {

    @Getter
    @NonNull
    protected Set<String> scanPackages = new HashSet<>();

    public void setScanPackages(Collection<String> scanPackages) {
        this.scanPackages = new HashSet<>(scanPackages);
    }

    protected final AnnotationMetadataSetResolver resolver = new AnnotationMetadataSetResolver();

    public AbstractClassPreloader() {
    }

    @Override
    public Set<Class<?>> preload() {
        Set<Class<?>> classSet = new HashSet<>();
        for (String scanPackage : scanPackages) {
            preloadOne(scanPackage, classSet);
        }
        return classSet;
    }

    protected abstract void preloadOne(String scanPackage, Set<Class<?>> classSet);

}

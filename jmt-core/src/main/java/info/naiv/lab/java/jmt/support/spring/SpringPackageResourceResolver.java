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
package info.naiv.lab.java.jmt.support.spring;

import info.naiv.lab.java.jmt.fx.StringPredicate;
import info.naiv.lab.java.jmt.infrastructure.AbstractPackageResourceResolver;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;

/**
 *
 * @author enlo
 */
public class SpringPackageResourceResolver extends AbstractPackageResourceResolver {

    public SpringPackageResourceResolver() {
    }

    @Override
    public Set<String> list() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        for (StringPredicate pattern : getIncludePatternSet()) {
            provider.addIncludeFilter(new Filter(pattern));
        }
        for (StringPredicate pattern : getExcludePatternSet()) {
            provider.addExcludeFilter(new Filter(pattern));
        }
        Set<String> result = new HashSet<>();
        for (String packageName : getPackageNameSet()) {
            listPackage(result, provider, packageName);
        }
        return result;
    }

    private void listPackage(Set<String> result, ClassPathScanningCandidateComponentProvider provider, String packageName) {
        Set<BeanDefinition> classSet = provider.findCandidateComponents(packageName);
        for (BeanDefinition clz : classSet) {
            result.add(clz.getBeanClassName());
        }
    }

    static class Filter extends AbstractClassTestingTypeFilter {

        final StringPredicate sp;

        Filter(StringPredicate sp) {
            this.sp = sp;
        }

        @Override
        protected boolean match(ClassMetadata cm) {
            return sp.test(cm.getClassName());
        }

    }
}

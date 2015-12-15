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
package info.naiv.lab.java.jmt.infrastructure;

import info.naiv.lab.java.jmt.fx.StringPredicate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public abstract class AbstractPackagePreloader implements PackagePreloader {

    static String process(String fqcn) {
        String dotted = fqcn.replace('/', '.');
        return dotted.replaceAll("\\.class$", "");
    }

    @Override
    public synchronized void addExcludePattern(String pattern) {
        ClassPathRegexPredicate p = new ClassPathRegexPredicate(pattern);
        getPackageResourceResolver().setExcludePatterns(p);
    }

    @Override
    public boolean addPackage(String packageName) {
        return getPackageResourceResolver().addPackage(packageName);
    }

    public Iterable<String> getPackageNames() {
        return getPackageResourceResolver().getPackageNames();
    }

    @Override
    public Set<String> load() {
        Set<String> result = new HashSet<>();
        Set<String> classNames = getPackageResourceResolver().list();
        for (String fqcn : classNames) {
            fqcn = process(fqcn);
            try {
                Class.forName(fqcn);
                result.add(fqcn);
                logger.info("preload {}", fqcn);
            }
            catch (ClassNotFoundException ex) {
                logger.warn("preload failed.", ex);
            }
        }
        return result;
    }

    protected abstract PackageResourceResolver getPackageResourceResolver();

    protected static class ClassPathRegexPredicate implements StringPredicate {

        final Pattern pattern;

        ClassPathRegexPredicate(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        @Override
        public boolean test(String obj) {
            return pattern.matcher(process(obj)).find();
        }
    }

}

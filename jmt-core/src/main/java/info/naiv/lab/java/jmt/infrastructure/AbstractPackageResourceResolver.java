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
import static java.util.Arrays.asList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public abstract class AbstractPackageResourceResolver implements PackageResourceResolver {

    Set<StringPredicate> excludePatternSet;
    Set<StringPredicate> includePatternSet;
    Set<String> packageNameSet;

    public AbstractPackageResourceResolver() {
        excludePatternSet = new CopyOnWriteArraySet<>();
        includePatternSet = new CopyOnWriteArraySet<>();
        packageNameSet = new CopyOnWriteArraySet<>();

        this.includePatternSet.add(StringPredicate.NO_CHECK);
    }

    @Override
    public final boolean addPackage(String packageName) {
        return getPackageNameSet().add(packageName);
    }

    @Override
    public final Iterable<String> getPackageNames() {
        return getPackageNameSet();
    }

    @Override
    public final void setExcludePatterns(StringPredicate... patterns) {
        setExcludePatternSet(new CopyOnWriteArraySet<>(asList(patterns)));
    }

    @Override
    public void setIncludePatterns(StringPredicate... patterns) {
        setIncludePatternSet(new CopyOnWriteArraySet<>(asList(patterns)));
    }

    @Override
    public final void setPackageNames(String... packageNames) {
        setPackageNameSet(new CopyOnWriteArraySet<>(asList(packageNames)));
    }

}

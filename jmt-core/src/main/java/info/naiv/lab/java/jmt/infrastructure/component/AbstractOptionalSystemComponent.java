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
package info.naiv.lab.java.jmt.infrastructure.component;

import info.naiv.lab.java.jmt.Misc;
import static info.naiv.lab.java.jmt.Misc.isLoadable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public abstract class AbstractOptionalSystemComponent implements OptionalSystemComponent {

    @Override
    public SystemComponent loadOptionalComponent() {
        String fqdn = checkClass();
        if (fqdn != null) {
            return createComponent(fqdn);
        }
        return null;
    }

    protected final String checkClass() {
        String found = null;
        for (String fqdn : getTargetClasses()) {
            if (!isLoadable(fqdn)) {
                logger.debug("class not found '{}'", fqdn);
            }
            else {
                found = fqdn;
                break;
            }
        }
        return found;
    }

    protected final SystemComponent createComponent(String className) {
        String realClassName = resolveClassName(className);
        if (realClassName == null) {
            return null;
        }
        return createInstance(realClassName);
    }

    protected SystemComponent createInstance(String className) {
        return (SystemComponent) Misc.newInstance(className).orElse(null);
    }

    protected abstract Iterable<String> getTargetClasses();

    protected String resolveClassName(String className) {
        return className;
    }

}

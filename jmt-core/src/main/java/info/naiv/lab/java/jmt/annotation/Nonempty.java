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
package info.naiv.lab.java.jmt.annotation;

import static info.naiv.lab.java.jmt.Misc.isNotEmpty;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Map;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

/**
 *
 * @author enlo
 */
@Documented
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Nonempty {

    When when() default When.ALWAYS;

    static class Checker implements TypeQualifierValidator<Nonempty> {

        @Override
        public When forConstantValue(Nonempty annotation, Object value) {
            if (value instanceof CharSequence) {
                if (isNotEmpty((CharSequence) value)) {
                    return When.ALWAYS;
                }
            }
            else if (value instanceof Collection) {
                if (isNotEmpty((Collection) value)) {
                    return When.ALWAYS;
                }
            }
            else if (value instanceof Map) {
                if (isNotEmpty((Map) value)) {
                    return When.ALWAYS;
                }
            }
            else if (value instanceof Iterable) {
                if (isNotEmpty((Iterable) value)) {
                    return When.ALWAYS;
                }
            }
            return When.NEVER;
        }

    }
}

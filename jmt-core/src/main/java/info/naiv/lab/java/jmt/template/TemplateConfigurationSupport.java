/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
package info.naiv.lab.java.jmt.template;

import info.naiv.lab.java.jmt.template.mvel.StringMvelTemplateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author enlo
 */
public class TemplateConfigurationSupport {

    public static <TResult> ClasspathResourceTemplateLoaderBuilder<TResult>
            getClasspathResourceTemplateLoaderBuilder(TemplateBuilder<TResult> templateBuilder) {
        return new ClasspathResourceTemplateLoaderBuilder<>(templateBuilder);
    }

    public static StringMvelTemplateBuilder stringMvelTemplateBuilder() {
        return new StringMvelTemplateBuilder();
    }

    @Accessors(fluent = true)
    @RequiredArgsConstructor
    @Setter
    public static class ClasspathResourceTemplateLoaderBuilder<TResult> {

        final TemplateBuilder<TResult> templateBuilder;

        private String suffix;

        private String extension;

        public ClasspathResourceTemplateLoader<TResult> build() {
            ClasspathResourceTemplateLoader l = new ClasspathResourceTemplateLoader();
            l.setTemplateBuilder(templateBuilder);
            if (suffix != null) {
                l.setSuffix(suffix);
            }
            if (extension != null) {
                l.setExtension(extension);
            }

            return l;
        }

    }
}

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
package info.naiv.lab.java.jmt.template.annotation;

import info.naiv.lab.java.jmt.template.TemplateLoader;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * カテゴリーとテンプレート名称から、{@link SqlTemplate} をDIするたの注釈型.
 *
 * @author enlo
 */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD, METHOD})
@Documented
public @interface InjectTemplate {

    /**
     * SqlTemplateLoader の名前. 通常は NULL.
     *
     * @return SqlTemplateLoader の名前.
     */
    String loader() default "";

    /**
     * TemplateLoader クラス指定.
     *
     * @return
     */
    Class<? extends TemplateLoader> loaderClass() default TemplateLoader.class;

    /**
     * カテゴリー
     *
     * @return カテゴリー
     */
    String category() default "";

    /**
     * テンプレート名称
     *
     * @return テンプレート名称
     */
    String name() default "";

    /**
     * 文字コード.
     *
     * @return
     */
    String charset() default "";
}

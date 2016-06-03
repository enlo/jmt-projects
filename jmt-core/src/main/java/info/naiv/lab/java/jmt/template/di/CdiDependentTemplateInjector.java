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
package info.naiv.lab.java.jmt.template.di;

import info.naiv.lab.java.jmt.template.Template;
import info.naiv.lab.java.jmt.template.TemplateLoader;
import info.naiv.lab.java.jmt.template.annotation.InjectTemplate;
import info.naiv.lab.java.jmt.template.annotation.TemplateCategoryOf;
import java.lang.reflect.Field;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
@Dependent
public class CdiDependentTemplateInjector extends AbstractTemplateInjector {

    /**
     *
     * @param ip
     * @return
     */
    @Produces
    @InjectTemplate
    public Template getService(InjectionPoint ip) {
        if (ip.getMember() instanceof Field) {
            Field field = (Field) ip.getMember();
            InjectTemplate anno = ip.getAnnotated().getAnnotation(InjectTemplate.class);
            TemplateCategoryOf category = ip.getAnnotated().getAnnotation(TemplateCategoryOf.class);
            return getTemplate(anno, category, field);
        }
        else {
            logger.error("injection target is not field. {}", ip.getMember());
            return null;
        }
    }

    /**
     *
     * @param loaderName
     * @return
     */
    @Override
    protected TemplateLoader getTemplateLoaderCore(String loaderName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

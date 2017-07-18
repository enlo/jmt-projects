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

import static info.naiv.lab.java.jmt.Misc.isEmpty;
import info.naiv.lab.java.jmt.OrderedItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Synchronized;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author enlo
 */
public class SimpleTemplateRepository implements TemplateRepository {

    private static final String NON_CATEGORY = "(NOCATEGORY)";

    private final List<OrderedItem<TemplateLoader>> templateLoaderList = new CopyOnWriteArrayList<>();

    private final ConcurrentMap<String, MultiValueMap<String, Template>> templates = new ConcurrentHashMap<>();

    public void addTemplateLoader(TemplateLoader loader) {
        addTemplateLoader(1, loader);
    }

    public void addTemplateLoader(int priority, TemplateLoader loader) {
        templateLoaderList.add(new OrderedItem<>(priority, loader));
        Collections.sort(templateLoaderList);
    }

    public void clearTemplates() {
        templates.clear();
    }

    @Override
    public <T extends Template> T getTemplate(String category, String name, Class<T> clazz) {

        MultiValueMap<String, Template> locals = getTemplatesInCategory(category);

        T t = null;
        List<Template> sameNames = getOrLoad(locals, category, name);
        if (sameNames != null) {
            for (Template tpl : sameNames) {
                if (ClassUtils.isAssignableValue(clazz, tpl)) {
                    t = (T) tpl;
                    break;
                }
            }
        }
        return t;
    }

    @Override
    public <T extends Template> List<T> getTemplateList(String category, Class<T> clazz) {
        MultiValueMap<String, Template> locals = getTemplatesInCategory(category);

        if (locals.isEmpty()) {
            loadCategory(locals, category);
        }

        List<T> result = new ArrayList<>();
        for (String key : locals.keySet()) {
            List<Template> values = locals.get(key);
            for (Template tpl : values) {
                if (ClassUtils.isAssignableValue(clazz, tpl)) {
                    result.add((T) tpl);
                }
            }
        }
        return result;
    }

    @Synchronized
    public void setTemplateLoader(TemplateLoader loader) {
        templateLoaderList.clear();
        addTemplateLoader(loader);
    }

    @Synchronized
    public void setTemplateLoaders(TemplateLoader... loaders) {
        templateLoaderList.clear();
        for (TemplateLoader loader : loaders) {
            templateLoaderList.add(new OrderedItem<>(1, loader));
        }
        Collections.sort(templateLoaderList);
    }

    @Synchronized
    private List<Template> getOrLoad(MultiValueMap<String, Template> locals, String category, String name) {
        List<Template> sameNames = locals.get(name);
        if (isEmpty(sameNames)) {
            for (OrderedItem<TemplateLoader> oi : templateLoaderList) {
                Template tpl = oi.getValue().load(category, name);
                if (tpl != null) {
                    locals.add(name, tpl);
                }
            }
        }
        return sameNames;
    }

    private MultiValueMap<String, Template> getTemplatesInCategory(String category) {
        if (category == null) {
            category = NON_CATEGORY;
        }
        MultiValueMap<String, Template> locals = templates.get(category);
        if (locals == null) {
            locals = new LinkedMultiValueMap<>();
            MultiValueMap<String, Template> prev = templates.putIfAbsent(category, locals);
            if (prev != null) {
                locals = prev;
            }
        }
        return locals;
    }

    @Synchronized
    private void loadCategory(MultiValueMap<String, Template> locals, String category) {
        if (locals.isEmpty()) {
            for (OrderedItem<TemplateLoader> oi : templateLoaderList) {
                Iterable<Template> tpls = oi.getValue().loadCategory(category);
                for (Template tpl : tpls) {
                    locals.add(tpl.getName(), tpl);
                }
            }
        }
    }

}

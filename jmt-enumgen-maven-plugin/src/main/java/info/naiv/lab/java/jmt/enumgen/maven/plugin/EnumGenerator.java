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
package info.naiv.lab.java.jmt.enumgen.maven.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;

/**
 *
 * @author enlo
 */
public class EnumGenerator {

    final CompiledTemplate classTemplate;
    final String enumName;
    final String fieldPrefix;
    final String packageName;

    public EnumGenerator(String packageName, String enumName, String fieldPrefix, CompiledTemplate classTemplate) {
        this.packageName = packageName;
        this.enumName = enumName;
        this.fieldPrefix = fieldPrefix;
        this.classTemplate = classTemplate;
    }

    public CodeData propertiesToCode(Properties properties) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("packageName", packageName);
        params.put("enumName", enumName);
        params.put("properties", properties);
        params.put("generateDate", new Date());

        List<PropertyEntry> entries = new ArrayList<>(properties.size());
        for (String key : properties.stringPropertyNames()) {
            PropertyEntry e = new PropertyEntry(key, properties.getProperty(key), fieldPrefix);
            entries.add(e);
        }
        Collections.sort(entries);
        params.put("entries", entries);

        String code = (String) TemplateRuntime.execute(classTemplate, params);
        return new CodeData(enumName, code);
    }

}

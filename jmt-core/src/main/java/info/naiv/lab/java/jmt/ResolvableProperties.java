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
package info.naiv.lab.java.jmt;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.PropertyPlaceholderHelper;

public class ResolvableProperties extends Properties {

    /**
     * Placeholder Helper.
     */
    private static final PropertyPlaceholderHelper defaultHelper = new PropertyPlaceholderHelper("${", "}");
    private static final long serialVersionUID = -1903901302631713092L;

    private final PropertyPlaceholderHelper helper;

    /**
     * コピーコンストラクター
     *
     * @param other もとのプロパティ
     */
    public ResolvableProperties(Properties other) {
        this(defaultHelper, other);
    }

    public ResolvableProperties() {
        this.helper = defaultHelper;
    }

    /**
     * コンストラクター
     *
     * @param helper プレイホルダーヘルパー
     * @param defaultProps 既定のプロパティ
     */
    protected ResolvableProperties(PropertyPlaceholderHelper helper, Properties defaultProps) {
        super(defaultProps);
        this.helper = nonNull(helper, "helper");
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public synchronized ResolvableProperties clone() {
        return (ResolvableProperties) super.clone();
    }

    /**
     * プロパティの内容を固定化する.
     *
     * @return プロパティ.
     */
    public Properties fix() {
        Properties props = new Properties();
        for (String key : this.stringPropertyNames()) {
            props.setProperty(key, this.getProperty(key));
        }
        return props;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return getPropertyCore(key, defaultValue);
    }

    @Override
    public String getProperty(String key) {
        return getPropertyCore(key, null);
    }

    /**
     * リソースからプロパティを読み込み.
     *
     * @see PropertiesLoaderUtils#fillProperties(java.util.Properties,
     * org.springframework.core.io.Resource)
     * @param uriLocation URI 文字列.
     * @return
     * @throws IOException
     */
    public ResolvableProperties loadFromResource(String uriLocation) throws IOException {
        String path = eval(uriLocation);
        Resource res = (new DefaultResourceLoader()).getResource(path);
        PropertiesLoaderUtils.fillProperties(this, res);
        return this;
    }

    public ResolvableProperties loadFromResource(String uriLocation, Charset charset) throws IOException {
        String path = eval(uriLocation);
        Resource r = (new DefaultResourceLoader()).getResource(path);
        EncodedResource er = new EncodedResource(r, charset);
        PropertiesLoaderUtils.fillProperties(this, er);
        return this;
    }

    public ConcurrentMap<String, String> toMap() {
        ConcurrentMap<String, String> result = new ConcurrentHashMap<>();
        for (String key : this.stringPropertyNames()) {
            result.put(key, this.getProperty(key));
        }
        return result;
    }

    /**
     * プロパティを取得し、プレイスホルダーを評価する.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private String getPropertyCore(String key, String defaultValue) {
        String value = super.getProperty(key);
        if (value == null) {
            if (defaultValue == null) {
                return null;
            }
            else {
                return eval(defaultValue);
            }
        }
        else {
            return eval(value);
        }
    }

    private String onPropertyNotFound(String propertyName) {
        String value = System.getProperty(propertyName);
        if (value == null) {
            value = System.getenv(propertyName);
        }
        return value;
    }

    /**
     * プレイスホルダーを実際の値に変換し、戻す.
     *
     * @param value 値
     * @return プレイスホルダー評価後の値
     */
    protected String eval(String value) {
        return helper.replacePlaceholders(value, new PropertyPlaceholderHelper.PlaceholderResolver() {
            @Override
            public String resolvePlaceholder(String placeholderName) {
                return resolveProperty(placeholderName);
            }
        });
    }

    protected String resolveProperty(String propertyName) {
        String value = getPropertyCore(propertyName, null);
        if (value == null) {
            value = onPropertyNotFound(propertyName);
        }
        return value;
    }
}

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
package info.naiv.lab.java.jmt;

import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public abstract class ExpressiveProperties extends Properties {

    private static final long serialVersionUID = 1L;

    public ExpressiveProperties() {
    }

    public ExpressiveProperties(Properties defaults) {
        super(defaults);
    }

    @Override
    public synchronized Object clone() {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * プロパティの内容を固定化したものを戻す.
     *
     * @return 固定したプロパティ.
     */
    @Nonnull
    public Properties fix() {
        Properties props = new Properties();
        for (String key : this.stringPropertyNames()) {
            String value = this.getProperty(key);
            if (value != null) {
                props.setProperty(key, value);
            }
        }
        return props;
    }

    @Override
    public final String getProperty(String key, String defaultValue) {
        return getPropertyCore(key, defaultValue);
    }

    @Override
    public final String getProperty(String key) {
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
    @Nonnull
    public ExpressiveProperties loadFromResource(@NonNull String uriLocation) throws IOException {
        return loadFromResources(uriLocation);
    }

    /**
     *
     * @param uriLocation
     * @param charset
     * @return
     * @throws IOException
     */
    @Nonnull
    public ExpressiveProperties loadFromResource(@NonNull String uriLocation, @NonNull Charset charset) throws IOException {
        return loadFromResources(charset, uriLocation);
    }

    @Nonnull
    public ExpressiveProperties loadFromResources(@NonNull Charset charset, String... uriLocations) throws IOException {
        DefaultResourceLoader drl = new DefaultResourceLoader();
        for (String uriLocation : uriLocations) {
            String path = eval(uriLocation);
            Resource r = drl.getResource(path);
            EncodedResource er = new EncodedResource(r, charset);
            PropertiesLoaderUtils.fillProperties(this, er);
        }
        return this;
    }

    @Nonnull
    public ExpressiveProperties loadFromResources(String... uriLocations) throws IOException {
        DefaultResourceLoader drl = new DefaultResourceLoader();
        for (String uriLocation : uriLocations) {
            String path = eval(uriLocation);
            Resource r = drl.getResource(path);
            PropertiesLoaderUtils.fillProperties(this, r);
        }
        return this;
    }

    /**
     *
     * @param propertyName
     * @param value
     * @return
     */
    @Nonnull
    public ExpressiveProperties setPropertyIfValueNotBlank(@Nonnull String propertyName, String value) {
        if (isNotBlank(value)) {
            setProperty(propertyName, value);
        }
        return this;
    }

    /**
     *
     * @return
     */
    @Nonnull
    public ConcurrentMap<String, String> toMap() {
        ConcurrentMap<String, String> result = new ConcurrentHashMap<>();
        for (String key : this.stringPropertyNames()) {
            result.put(key, this.getProperty(key));
        }
        return result;
    }

    /**
     * Placeholder を実際の値に変換し、戻す.
     *
     * @param value 値. null を考慮すること.
     * @return Placeholder 評価後の値
     */
    protected abstract String eval(String value);

    /**
     * プロパティを取得し、プレイスホルダーを評価する.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getPropertyCore(String key, String defaultValue) {
        String value = eval(super.getProperty(key));
        if (value == null) {
            return eval(defaultValue);
        }
        return value;
    }

}

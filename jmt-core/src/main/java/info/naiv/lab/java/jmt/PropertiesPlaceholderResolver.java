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
package info.naiv.lab.java.jmt;

import static info.naiv.lab.java.jmt.Misc.isEmpty;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Properties;
import javax.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 *
 * @author enlo
 */
public class PropertiesPlaceholderResolver implements Serializable {

    /**
     *
     */
    public static final PropertiesPlaceholderResolver DEFAULT = new PropertiesPlaceholderResolver();

    private static final long serialVersionUID = 1L;

    private transient PropertyPlaceholderHelper helper;

    private final String prefix;
    private final String suffix;

    /**
     *
     */
    public PropertiesPlaceholderResolver() {
        this("${", "}");
    }

    /**
     *
     * @param placeholderPrefix
     * @param placeholderSuffix
     */
    public PropertiesPlaceholderResolver(@NonNull String placeholderPrefix,
                                         @NonNull String placeholderSuffix) {
        prefix = placeholderPrefix;
        suffix = placeholderSuffix;
        helper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix);
    }

    /**
     *
     * @param props
     * @param value
     * @return
     */
    public String resolve(final Properties props, String value) {
        if (isEmpty(value)) {
            return value;
        }
        return helper.replacePlaceholders(value, new PropertyPlaceholderHelper.PlaceholderResolver() {
                                      @Override
                                      public String resolvePlaceholder(String placeholderName) {
                                          return resolveProperty(props, placeholderName);
                                      }
                                  });
    }

    @SuppressWarnings("unused")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        helper = new PropertyPlaceholderHelper(prefix, suffix);
    }

    /**
     *
     * @param propertyName
     * @return
     */
    protected String onPropertyNotFound(@Nonnull String propertyName) {
        String value = System.getProperty(propertyName);
        if (value == null) {
            value = System.getenv(propertyName);
        }
        return value;
    }

    /**
     *
     * @param props
     * @param propertyName
     * @return
     */
    protected String resolveProperty(@Nonnull Properties props, @Nonnull String propertyName) {
        String value = props.getProperty(propertyName, null);
        if (value == null) {
            value = onPropertyNotFound(propertyName);
        }
        return value;
    }
}

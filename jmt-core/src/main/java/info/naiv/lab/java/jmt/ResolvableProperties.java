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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Properties;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
@EqualsAndHashCode(callSuper = true)
public class ResolvableProperties extends ExpressiveProperties {

    private static final PropertiesPlaceholderResolver defaultResolver = PropertiesPlaceholderResolver.DEFAULT;
    private static final long serialVersionUID = 1L;

    /**
     * Placeholder resolver.
     */
    private final PropertiesPlaceholderResolver resolver;

    /**
     * コピーコンストラクター
     *
     * @param other もとのプロパティ
     */
    public ResolvableProperties(Properties other) {
        this(defaultResolver, other);
    }

    /**
     *
     */
    public ResolvableProperties() {
        this.resolver = defaultResolver;
    }

    /**
     * コンストラクター
     *
     * @param helper プレイホルダーヘルパー
     * @param defaultProps 既定のプロパティ
     */
    protected ResolvableProperties(@NonNull PropertiesPlaceholderResolver helper,
                                   Properties defaultProps) {
        super(defaultProps);
        this.resolver = helper;
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public synchronized ResolvableProperties clone() {
        return (ResolvableProperties) super.clone();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    /**
     * プレイスホルダーを実際の値に変換し、戻す.
     *
     * @param value 値
     * @return プレイスホルダー評価後の値
     */
    @Override
    protected String eval(String value) {
        return resolver.resolve(this, value);
    }
}

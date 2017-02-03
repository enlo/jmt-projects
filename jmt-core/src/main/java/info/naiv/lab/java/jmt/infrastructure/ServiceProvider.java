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
package info.naiv.lab.java.jmt.infrastructure;

import java.util.Collection;
import javax.annotation.Nonnull;

/**
 * サービス提供
 *
 * @author enlo
 */
public interface ServiceProvider extends AutoCloseable {

    /**
     * 型からサービスを取得. <br>
     * タグオブジェクトは{@link Tag#ANY}を使用する.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @return サービスオブジェクト. 見つからない場合は null.
     */
    <T> T resolveService(@Nonnull Class<T> serviceType);

    /**
     * 型からサービスを取得. <br>
     * 複数一致するものがある場合、一致するものをすべて戻す. <br>
     * タグオブジェクトは{@link Tag#ANY}を使用する.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @return サービスオブジェクトコレクション. 見つからない場合は null.
     */
    @Nonnull
    <T> Collection<T> resolveServices(@Nonnull Class<T> serviceType);

    /**
     * 型からサービスを取得.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @param tag タグオブジェクト.
     * @return the T
     */
    <T> T resolveService(@Nonnull Class<T> serviceType, Tag tag);

    /**
     * 型からサービスを取得. 複数一致するものがある場合、一致するものをすべて戻す.
     *
     * @param <T> サービスの型.
     * @param serviceType サービスタイプ
     * @param tag タグオブジェクト.
     * @return the java.util.Collection
     */
    @Nonnull
    <T> Collection<T> resolveServices(@Nonnull Class<T> serviceType, @Nonnull Tag tag);
}

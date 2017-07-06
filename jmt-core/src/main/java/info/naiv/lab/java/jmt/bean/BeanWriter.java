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
package info.naiv.lab.java.jmt.bean;

import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 * @param <TDest>
 * @param <TSource>
 */
public interface BeanWriter<TSource, TDest> {

    /**
     * エラーハンドラーの追加
     *
     * @param errorHandler
     */
    void addErrorHandler(@Nonnull BeanWritingErrorHandler errorHandler);

    /**
     * エラーハンドラーの削除
     *
     * @param errorHandler
     */
    void removeErrorHandler(@Nonnull BeanWritingErrorHandler errorHandler);

    /**
     * もとになるオブジェクトから、新しいオブジェクト作成する.
     *
     * @param source
     * @return
     */
    TDest createFrom(TSource source);

    /**
     * もとになるオブジェクトから値を書き込む. <br>
     * input, output は apache commons 準拠.
     *
     * @param source
     * @param dest
     * @return
     */
    TDest write(@Nonnull TSource source, @Nonnull TDest dest);

}

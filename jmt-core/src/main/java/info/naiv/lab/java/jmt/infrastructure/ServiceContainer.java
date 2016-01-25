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

import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.util.UUID;

/**
 * サービスコンテナー
 *
 * @author enlo
 */
public interface ServiceContainer extends ServiceProvider {

    /**
     * @return UUID
     */
    @ReturnNonNull
    UUID id();

    /**
     * サービスの登録. <br>
     * 優先度を1として、{@link #registerService(int, Object, Tag)
     * }
     * を呼び出す.
     *
     * @param service 登録したいインスタンス.
     * @param tag タグオブジェクト.
     * @throws IllegalArgumentException service が null.
     * @return the info.naiv.java.jmt.component.ServiceConnection
     */
    @ReturnNonNull
    ServiceConnection registerService(Object service, Tag tag) throws IllegalArgumentException;

    /**
     * サービスの登録. <br>
     * 優先度を1として、{@link #registerService(int, java.lang.Object) }
     * を呼び出す.
     *
     * @param service 登録したいインスタンス.
     * @throws IllegalArgumentException service が null.
     * @return the info.naiv.java.jmt.component.ServiceConnection
     */
    @ReturnNonNull
    ServiceConnection registerService(Object service) throws IllegalArgumentException;

    /**
     * サービスの登録. <br>
     * 同一オブジェクトが登録されている場合は、優先度を変更する. タグは {@link Tag#NONE} を使用する.
     *
     * @param priority 優先順位. 大きいほど優先度が高い.
     * @param service 登録したいインスタンス.
     * @throws IllegalArgumentException service が null.
     * @return the info.naiv.java.jmt.component.ServiceConnection
     */
    @ReturnNonNull
    ServiceConnection registerService(int priority, Object service) throws IllegalArgumentException;

    /**
     * サービスの登録. <br>
     * 同一オブジェクトが登録されている場合は、優先度を変更する.
     *
     * @param priority 優先順位. 大きいほど優先度が高い.
     * @param service 登録したいインスタンス.
     * @param tag タグオブジェクト.
     * @throws IllegalArgumentException service が null.
     * @return the info.naiv.java.jmt.component.ServiceConnection
     */
    @ReturnNonNull
    ServiceConnection registerService(int priority, Object service, Tag tag) throws IllegalArgumentException;

}

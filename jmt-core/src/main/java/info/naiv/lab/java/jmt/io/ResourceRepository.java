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
package info.naiv.lab.java.jmt.io;

import java.io.IOException;
import java.nio.file.Watchable;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import info.naiv.lab.java.jmt.monad.Optional;

/**
 * カテゴリーと名称を指定して {@link Resource} を取得する. <br>
 * 自分自身が管理するリポジトリ内に {@link Resource} が存在しなかった場合、<br>
 * 親のリポジトリに対して問い合わせを行う.
 *
 * @see ResourceLoader
 * @author enlo
 */
public interface ResourceRepository {

    /**
     * リポジトリ全体に対して監視が可能であれば、 {@link Watchable} を戻す.
     *
     * @return 監視可能であれば、{@link Watchable}
     */
    Optional<Watchable> getWatchable();

    /**
     * 指定されたカテゴリーに対して監視が可能であれば、{@link Watchable} を戻す.
     *
     * @param category 監視したいカテゴリー
     * @return カテゴリーが監視可能であれば、{@link Watchable}
     */
    Optional<Watchable> getWatchable(String category);

    /**
     *
     * @return 親のリポジトリを戻す.
     */
    ResourceRepository getParent();

    /**
     * 親のリポジトリを設定する.
     *
     * @param parent 親のリポジトリ.
     */
    void setParent(ResourceRepository parent);

    /**
     * リポジトリー内からリソースを取得する.
     *
     * @param category カテゴリー
     * @param name 名称.
     * @return リソース.
     * @throws IOException リソースの読み込みに失敗した場合など.
     */
    Resource getResource(String category, String name) throws IOException;

    /**
     * リポジトリー内からリソースを取得する.
     *
     * @param category カテゴリー
     * @param filter 名称フィルター.
     * @return リソース.
     * @throws IOException リソースの読み込みに失敗した場合など.
     */
    Resource getResource(String category, FilenamePatternFilter filter) throws IOException;

    /**
     * リポジトリー内の特定カテゴリにある全リソースを取得する.
     *
     * @param category カテゴリー
     * @return 名前別リソース.
     * @throws IOException リソースの読み込みに失敗した場合など.
     */
    Map<String, Resource> getResources(String category) throws IOException;

    /**
     * リポジトリー内の特定カテゴリにある全リソースを取得する.
     *
     * @param category カテゴリー
     * @param globPattern 対象とするファイル名パターン.
     * @return 名前別リソース.
     * @throws IOException リソースの読み込みに失敗した場合など.
     */
    Map<String, Resource> getResources(String category, String globPattern) throws IOException;

    /**
     * リポジトリー内の特定カテゴリにある全リソースを取得する.
     *
     * @param category カテゴリー
     * @param filter フィルター.
     * @return 名前別リソース.
     * @throws IOException リソースの読み込みに失敗した場合など.
     */
    Map<String, Resource> getResources(String category, FilenamePatternFilter filter) throws IOException;

}

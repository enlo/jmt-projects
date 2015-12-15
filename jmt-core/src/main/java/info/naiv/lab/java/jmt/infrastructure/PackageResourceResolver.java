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

import info.naiv.lab.java.jmt.fx.StringPredicate;
import java.util.Set;

/**
 *
 * @author enlo
 */
public interface PackageResourceResolver {

    /**
     * 除外パターンの追加.
     *
     * @param patterns 事前ロードしないクラス名のパターン.
     */
    void setExcludePatterns(StringPredicate... patterns);

    /**
     * 読み込みパターンの設定.
     *
     * @param patterns
     */
    void setIncludePatterns(StringPredicate... patterns);

    /**
     * パッケージの追加
     *
     * @param packageName
     * @return
     */
    boolean addPackage(String packageName);

    /**
     *
     * @return パッケージ名
     */
    Iterable<String> getPackageNames();

    /**
     * パッケージ名の設定.
     *
     * @param packageNames
     */
    void setPackageNames(String... packageNames);

    /**
     * FQCNリスト
     *
     * @return パッケージ内のFQCNリスト.
     */
    Set<String> list();

}

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
package info.naiv.lab.java.jmt.runtime;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author enlo
 */
@Getter
@Setter
public class MethodInvokerFactory {

    private static final ConcurrentMap<Class<?>, Map<String, List<MethodInvoker>>> METHOD_CHACHE = new ConcurrentHashMap<>();
    private static final Object STATIC_LOCK = new Object();

    private boolean ignoreCase = false;

    /**
     * メソッドセットの作成.
     *
     * @param clz クラス情報.
     * @return メソッドセット.
     */
    public Map<String, List<MethodInvoker>> prepare(Class<?> clz) {
        Map<String, List<MethodInvoker>> result = METHOD_CHACHE.get(clz);
        if (result != null) {
            return result;
        }

        // キャッシュに無い場合、一度ロック.
        synchronized (STATIC_LOCK) {
            // ロック取得後に、再度問い合わせを行い、オブジェクトが無ければ初期化処理.
            if (METHOD_CHACHE.containsKey(clz)) {
                return METHOD_CHACHE.get(clz);
            }
            Method[] methods = clz.getDeclaredMethods();
            result = new HashMap<>(methods.length);
            for (Method m : methods) {
                MethodAlias alias = m.getAnnotation(MethodAlias.class);
                if (alias != null) {
                    for (String aname : alias.value()) {

                    }
                }
                else {
                    String name = m.getName();
                    if (ignoreCase) {
                        name = name.toLowerCase();
                    }
                    List<MethodInvoker> mil = result.get(name);
                    if (mil == null) {
                        mil = new ArrayList<>();
                        result.put(name, mil);
                    }
                    MethodInvoker mi = createMethodInvoker(m);
                    mil.add(mi);
                }
            }
            METHOD_CHACHE.put(clz, result);
            return result;
        }
    }

    private MethodInvoker createMethodInvoker(Method m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

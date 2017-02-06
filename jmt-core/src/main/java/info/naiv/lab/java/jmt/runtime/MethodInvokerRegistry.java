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

import static info.naiv.lab.java.jmt.Misc.isEmpty;
import info.naiv.lab.java.jmt.collection.MultiValueLookup;
import info.naiv.lab.java.jmt.monad.Iteratee;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class MethodInvokerRegistry implements MultiValueLookup<String, MethodInvoker> {

    private static final ConcurrentMap<Class<?>, Map<String, List<MethodInvoker>>> METHOD_CHACHE = new ConcurrentHashMap<>();
    private static final Object STATIC_LOCK = new Object();
    protected final boolean ignoreCase;

    @Getter
    protected final Class<?> targetClass;
    Map<String, List<MethodInvoker>> methodInvokers;

    public MethodInvokerRegistry(@NonNull Class<?> clazz, boolean ignoreCase) {
        this.targetClass = clazz;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean containsKey(String key) {
        prepare();
        return methodInvokers.containsKey(key);
    }

    @Override
    public Iterable<MethodInvoker> get(String key) {
        prepare();
        return Iteratee.of(methodInvokers.get(key));
    }

    @Override
    public MethodInvoker getFirst(String key) {
        prepare();
        List<MethodInvoker> found = methodInvokers.get(key);
        if (isEmpty(found)) {
            return null;
        }
        return found.get(0);
    }

    public void prepare() {
        if (methodInvokers == null) {
            if (ignoreCase) {
                Map<String, List<MethodInvoker>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                map.putAll(prepareCore());
                methodInvokers = map;
            }
            else {
                methodInvokers = prepareCore();
            }
        }
    }

    protected Iterable<String> resolveNames(Method m) {
        List<String> list = new ArrayList<>();
        list.add(m.getName());

        MethodAlias alias = m.getAnnotation(MethodAlias.class);
        if (alias != null) {
            for (String aname : alias.value()) {
                list.add(aname);
            }
        }
        return list;
    }

    protected MethodInvoker createMethodInvoker(Method m) {
        return new OptionalSupportMethodInvoker(m);
    }

    /**
     * メソッドセットの作成.
     *
     * @return メソッドセット.
     */
    protected Map<String, List<MethodInvoker>> prepareCore() {
        Map<String, List<MethodInvoker>> result = METHOD_CHACHE.get(targetClass);
        if (result != null) {
            return result;
        }

        // キャッシュに無い場合、一度ロック.
        synchronized (STATIC_LOCK) {
            // ロック取得後に、再度問い合わせを行い、オブジェクトが無ければ初期化処理.
            if (METHOD_CHACHE.containsKey(targetClass)) {
                return METHOD_CHACHE.get(targetClass);
            }
            Method[] methods = targetClass.getDeclaredMethods();
            result = new HashMap<>(methods.length);
            for (Method m : methods) {
                Iterable<String> names = resolveNames(m);
                for (String name : names) {
                    registerInvoker(result, name, m);
                }
            }
            METHOD_CHACHE.put(targetClass, result);
            return result;
        }
    }

    protected void registerInvoker(Map<String, List<MethodInvoker>> result, String name, Method m) {
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

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
package info.naiv.lab.java.jmt.infrastructure.component;

import info.naiv.lab.java.jmt.Holders;
import info.naiv.lab.java.jmt.infrastructure.HolderTag;
import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 * @param <T>
 */
public abstract class AbstractServiceComponent<T> implements ServiceComponent<T> {

    /**
     *
     * @param tag
     * @param provider
     * @return
     */
    @Override
    public boolean contains(Tag tag, ServiceProvider provider) {
        Map<Tag, T> map = getComponentMap();
        if (map.containsKey(tag)) {
            return true;
        }
        else if (tag instanceof HolderTag) {
            HolderTag<?> ht = (HolderTag<?>) tag;
            @SuppressWarnings("unchecked")
            T obj = (T) Holders.as(ht, getContentType());
            if (obj != null) {
                map.put(tag, obj);
                return true;
            }
        }
        return handleNotFound(map, tag, provider);
    }

    /**
     *
     * @param tag
     * @param provider
     * @return
     */
    @Override
    public T getContent(Tag tag, ServiceProvider provider) {
        Map<Tag, T> map = getComponentMap();
        for (Map.Entry<Tag, T> e : map.entrySet()) {
            if (e.getKey().contains(tag)) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    @Nonnull
    protected abstract Map<Tag, T> getComponentMap();

    /**
     *
     * @param map
     * @param tag
     * @param provider
     * @return
     */
    abstract protected boolean handleNotFound(Map<Tag, T> map, Tag tag, ServiceProvider provider);

}

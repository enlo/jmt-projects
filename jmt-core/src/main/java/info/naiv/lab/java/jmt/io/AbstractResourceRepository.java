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

import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.monad.Optional;
import java.io.IOException;
import java.nio.file.Watchable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author enlo
 */
@Getter
@Setter
public abstract class AbstractResourceRepository implements ResourceRepository {

    private ResourceRepository parent;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public ResourceRepository getParent() {
        return parent;
    }

    @Override
    public Resource getResource(String category, String name) throws IOException {
        String location = resolveLocation(category, name);
        Resource res = getResourceLoader().getResource(location);
        if (res != null && res.exists()) {
            return res;
        }
        else {
            ResourceRepository prr = getParent();
            if (prr != null) {
                res = getParent().getResource(category, name);
            }
            return res;
        }
    }

    @Override
    public Resource getResource(String category, FilenamePatternFilter filter) throws IOException {
        Map<String, Resource> source = getResources(category);
        Map<String, Resource> result = new HashMap<>();
        for (Resource res : source.values()) {
            if (filter.filter(res, result)) {
                break;
            }
        }
        return Misc.getFirst(result.values());
    }

    @Override
    public Map<String, Resource> getResources(String category) throws IOException {
        return getResources(category, "*");
    }

    @Override
    public Map<String, Resource> getResources(String category, String globPattern) throws IOException {
        Map<String, String> locations = resolveLocations(category, globPattern);
        Map<String, Resource> result;
        ResourceRepository prr = getParent();
        if (prr != null) {
            result = prr.getResources(category);
        }
        else {
            result = new HashMap<>(locations.size());
        }
        for (Entry<String, String> location : locations.entrySet()) {
            Resource res = getResourceLoader().getResource(location.getValue());
            if (res != null && res.exists()) {
                result.put(location.getKey(), res);
            }
        }
        return result;
    }

    @Override
    public Map<String, Resource> getResources(String category, FilenamePatternFilter filter) throws IOException {
        Map<String, Resource> source = getResources(category);
        Map<String, Resource> result = new HashMap<>();
        for (Resource res : source.values()) {
            filter.filter(res, result);
        }
        return result;
    }

    @Override
    public Optional<Watchable> getWatchable() {
        return Optional.EMPTY;
    }

    @Override
    public Optional<Watchable> getWatchable(String category) {
        return Optional.EMPTY;
    }

    @Override
    public void setParent(ResourceRepository parent) {
        this.parent = parent;
    }

    protected abstract String resolveLocation(String category, String name) throws IOException;

    protected abstract Map<String, String> resolveLocations(String category, String globPattern) throws IOException;

}

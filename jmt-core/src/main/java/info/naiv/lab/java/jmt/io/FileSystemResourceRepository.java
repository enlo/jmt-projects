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

import info.naiv.lab.java.jmt.monad.Optional;
import info.naiv.lab.java.jmt.monad.OptionalImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Watchable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResourceLoader;

/**
 * ファイルシステムを使用したリポジトリ.
 *
 *
 * @author enlo
 */
@Getter
@Setter
@Slf4j
public class FileSystemResourceRepository extends AbstractResourceRepository {

    final ConcurrentMap<String, Path> categories = new ConcurrentHashMap<>();
    @NonNull
    Path rootDirectory;

    /**
     *
     */
    public FileSystemResourceRepository() {
        setResourceLoader(new FileSystemResourceLoader());
    }

    /**
     *
     * @param filePattern
     * @param rootDirectory
     */
    public FileSystemResourceRepository(String filePattern, Path rootDirectory) {
        this();
        this.rootDirectory = rootDirectory;
    }

    @Override
    public Optional<Watchable> getWatchable() {
        return OptionalImpl.<Watchable>of(rootDirectory);
    }

    @Override
    public Optional<Watchable> getWatchable(String category) {
        Path path = getCategoryPath(category);
        if (path != null && Files.exists(path)) {
            return OptionalImpl.<Watchable>of(path);
        }
        else {
            return OptionalImpl.<Watchable>empty();
        }
    }

    private Path getCategoryPath(String category) {
        if (categories.containsKey(category)) {
            return categories.get(category);
        }
        else {
            Path root = rootDirectory != null ? rootDirectory : Paths.get(".");
            Path path = root.resolve(category);
            Path res = categories.putIfAbsent(category, path);
            return res == null ? path : res;
        }
    }

    /**
     *
     * @param target
     * @param globPattern
     * @return
     * @throws IOException
     */
    protected Iterable<Path> listPath(Path target, String globPattern) throws IOException {
        if (globPattern == null || "*".equals(globPattern)) {
            return Files.newDirectoryStream(target);
        }
        else {
            return Files.newDirectoryStream(target, globPattern);
        }
    }

    /**
     *
     * @param category
     * @param name
     * @return
     */
    @Override
    protected String resolveLocation(String category, String name) {
        Path path = getCategoryPath(category).resolve(name);
        return path.toString();
    }

    /**
     *
     * @param category
     * @param globPattern
     * @return
     * @throws IOException
     */
    @Override
    protected Map<String, String> resolveLocations(String category, String globPattern) throws IOException {
        Map<String, String> result = new HashMap<>();
        Path target = getCategoryPath(category);
        Iterable<Path> list = listPath(target, globPattern);
        for (Path p : list) {
            String key = p.getFileName().toString();
            String value = p.toString();
            result.put(key, value);
        }
        return result;
    }
}

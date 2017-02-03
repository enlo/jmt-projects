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
package info.naiv.lab.java.jmt.runtime;

import static info.naiv.lab.java.jmt.io.NIOUtils.listPaths;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
@Data
public class URLClassLoaderBuilder {

    final Set<URL> jarFiles = new CopyOnWriteArraySet<>();

    ClassLoader parentClassLoader = this.getClass().getClassLoader();

    /**
     *
     * @return URLClassLoaderBuilder instance.
     */
    @Nonnull
    public static URLClassLoaderBuilder builder() {
        return new URLClassLoaderBuilder();
    }

    public URLClassLoaderBuilder setParentClassLoader(@NonNull ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
        return this;
    }

    @Nonnull
    public URLClassLoaderBuilder addDirectory(String directory) throws IOException {
        return addDirectory(Paths.get(directory));
    }

    @Nonnull
    public URLClassLoaderBuilder addDirectory(File directory) throws IOException {
        return addDirectory(directory.toPath());
    }

    @Nonnull
    public URLClassLoaderBuilder addDirectory(Path directory) throws IOException {
        listJarFiles(directory);
        return this;
    }

    @Nonnull
    public URLClassLoaderBuilder addJarFile(String jarfile) throws IOException {
        return addJarFile(Paths.get(jarfile));
    }

    @Nonnull
    public URLClassLoaderBuilder addJarFile(File jarfile) throws IOException {
        return addJarFile(jarfile.toPath());
    }

    @Nonnull
    public URLClassLoaderBuilder addJarFile(Path jarfile) throws IOException {
        if (Files.exists(jarfile)) {
            URL u = jarfile.toUri().toURL();
            jarFiles.add(u);
        }
        return this;
    }

    @Nonnull
    public URLClassLoader build() throws IOException {
        URLClassLoader urlLoader = newClassLoader();
        return urlLoader;
    }

    protected void listJarFiles(Path directory) throws IOException {
        List<Path> found = listPaths(directory, "**.jar", 1);
        for (Path p : found) {
            URL u = p.toUri().toURL();
            jarFiles.add(u);
        }
    }

    @Nonnull
    public URLClassLoaderBuilder removeJars(@NonNull Collection<URL> paths) {
        jarFiles.removeAll(paths);
        return this;
    }

    @Nonnull
    protected URLClassLoader newClassLoader() throws IOException {
        URL[] array = new URL[jarFiles.size()];
        if (logger.isDebugEnabled()) {
            for (URL url : array) {
                logger.debug("load jar file -> {} .", url);
            }
        }

        ClassLoader p = this.parentClassLoader;
        URLClassLoader cl = URLClassLoader.newInstance(jarFiles.toArray(array), p);
        return cl;
    }

}

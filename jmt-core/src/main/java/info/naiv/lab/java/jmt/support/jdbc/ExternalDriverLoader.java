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
package info.naiv.lab.java.jmt.support.jdbc;

import static info.naiv.lab.java.jmt.io.NIOUtils.listFiles;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.sql.Driver;
import static java.sql.DriverManager.registerDriver;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 *
 * @author enlo
 */
public class ExternalDriverLoader {

    private final Set<URL> loaded = new HashSet<>();
    private final ClassLoader parentClassLoader;

    public ExternalDriverLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

    public ExternalDriverLoader() {
        this.parentClassLoader = this.getClass().getClassLoader();
    }

    @ReturnNonNull
    public synchronized Iterable<Driver> load(List<Path> paths) throws IOException, SQLException {
        URLClassLoader loader = newClassLoader(paths);
        ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class, loader);
        for (Driver drv : drivers) {
            registerDriver(new JdbcProxyDriver(drv));
        }
        return drivers;
    }

    protected void listJarFiles(Set<URL> result, Path path) throws IOException {
        List<Path> found = listFiles(path, "*.jar", 1);
        for (Path p : found) {
            URL u = p.toUri().toURL();
            if (!loaded.contains(u)) {
                result.add(u);
            }
        }
    }

    @ReturnNonNull
    protected URLClassLoader newClassLoader(List<Path> paths) throws IOException {
        Set<URL> urls = new HashSet<>();
        for (Path p : paths) {
            listJarFiles(urls, p);
        }
        URL[] array = new URL[urls.size()];
        ClassLoader p = this.parentClassLoader;
        URLClassLoader cl = URLClassLoader.newInstance(urls.toArray(array), p);
        loaded.addAll(urls);
        return cl;
    }

}

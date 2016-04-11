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
package info.naiv.lab.java.jmt.jdbc.driver;

import static info.naiv.lab.java.jmt.io.NIOUtils.listPaths;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.sql.Driver;
import java.sql.DriverManager;
import static java.sql.DriverManager.registerDriver;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public class ExternalJdbcDriverLoader {

    private final Set<URL> loadedUrl = new HashSet<>();
    private final ClassLoader parentClassLoader;

    public ExternalJdbcDriverLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

    public ExternalJdbcDriverLoader() {
        this.parentClassLoader = this.getClass().getClassLoader();
    }

    @ReturnNonNull
    public synchronized Set<Driver> load(Iterable<Path> paths) throws IOException, SQLException {
        return load(paths, Collections.EMPTY_LIST);
    }

    @ReturnNonNull
    public synchronized Set<Driver> load(Iterable<Path> paths, Iterable<String> classicDriverNames) throws IOException, SQLException {
        URLClassLoader loader = newClassLoader(paths);
        ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class, loader);
        Set<Driver> result = new HashSet<>();
        for (Driver drv : drivers) {
            if (drv != null) {
                registerDriver(new JdbcDriverProxy(drv));
                result.add(drv);
                logger.info("Load JDBC Driver. {}", drv);
            }
        }
        loadClassicJdbcDrivers(classicDriverNames, loader, result);

        return result;
    }

    protected void listJarFiles(Set<URL> result, Path path) throws IOException {
        List<Path> found = listPaths(path, "**.jar", 1);
        for (Path p : found) {
            URL u = p.toUri().toURL();
            if (!loadedUrl.contains(u)) {
                logger.debug("add {}", p);
                result.add(u);
            }
        }
    }

    /**
     * JDBC3 以前の古いドライバを読み込む. <br> {@link Class#forName(java.lang.String) }
     * で読み込み出来ない場合は、Jar から読み込みを試みる. <br>
     * 外部の jar からドライバーを読み込んだ場合、ClassLoader が異なることによる
     * {@link DriverManager#getDriver(java.lang.String) } でのスキップを防ぐために、
     * {@link JdbcDriverProxy}でラップする.
     *
     * @param classicDriverNames
     * @param loader
     * @param result
     * @throws SQLException
     */
    protected void loadClassicJdbcDrivers(Iterable<String> classicDriverNames, URLClassLoader loader, Set<Driver> result) throws SQLException {
        for (String classicDriverName : classicDriverNames) {
            try {
                Class.forName(classicDriverName);
            }
            catch (ClassNotFoundException ex) {
                try {
                    Class<?> driverClass = Class.forName(classicDriverName, true, loader);
                    if (Driver.class.isAssignableFrom(driverClass)) {
                        Driver driver = (Driver) driverClass.newInstance();
                        registerDriver(new JdbcDriverProxy(driver));
                        result.add(driver);
                        logger.info("Load JDBC Driver. {}", driver);
                    }
                }
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException ex1) {
                    logger.warn("JDBC Driver load failed. {}", classicDriverName);
                }
            }
        }
    }

    @ReturnNonNull
    protected URLClassLoader newClassLoader(Iterable<Path> paths) throws IOException {
        Set<URL> urls = new HashSet<>();
        for (Path p : paths) {
            listJarFiles(urls, p);
        }
        URL[] array = new URL[urls.size()];
        ClassLoader p = this.parentClassLoader;
        URLClassLoader cl = URLClassLoader.newInstance(urls.toArray(array), p);
        loadedUrl.addAll(urls);
        return cl;
    }

}

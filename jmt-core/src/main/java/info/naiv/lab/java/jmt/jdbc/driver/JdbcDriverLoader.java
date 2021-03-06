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
package info.naiv.lab.java.jmt.jdbc.driver;

import info.naiv.lab.java.jmt.runtime.URLClassLoaderBuilder;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.sql.Driver;
import java.sql.DriverManager;
import static java.sql.DriverManager.registerDriver;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public class JdbcDriverLoader {

    @NonNull
    private final ClassLoader parentClassLoader;

    /**
     *
     * @param parentClassLoader
     */
    public JdbcDriverLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

    /**
     *
     */
    public JdbcDriverLoader() {
        this.parentClassLoader = this.getClass().getClassLoader();
    }

    /**
     * 指定されたディレクトリにあるドライバーを読み込む.
     *
     * @param paths JDBCドライバーがあるディレクトリのリスト.
     * @return 読み込んだJDBCドライバー
     * @throws IOException
     * @throws SQLException
     */
    @Nonnull
    public Set<Driver> load(Collection<Path> paths) throws IOException, SQLException {
        return load(paths, Collections.EMPTY_LIST);
    }

    /**
     * 指定されたディレクトリにあるドライバーを読み込み、古い形式のドライバーをロードする.
     *
     * @param paths JDBCドライバーがあるディレクトリのリスト.
     * @param classicDriverNames 古いドライバーのクラス名.
     * @return 読み込んだJDBCドライバー
     * @throws IOException
     * @throws SQLException
     */
    @Nonnull
    public Set<Driver> load(Collection<Path> paths, Iterable<String> classicDriverNames) throws IOException, SQLException {
        URLClassLoader loader = newClassLoader(paths);
        return load(loader, classicDriverNames);
    }

    /**
     * 指定されたクラスローダー内にあるドライバーを読み込み、古い形式のドライバーをロードする.
     *
     * @param loader クラスローダー
     * @param classicDriverNames 古いドライバーのクラス名.
     * @return 読み込んだJDBCドライバー
     * @throws SQLException
     */
    @Nonnull
    public synchronized Set<Driver> load(ClassLoader loader, Iterable<String> classicDriverNames) throws SQLException {
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
    protected void loadClassicJdbcDrivers(Iterable<String> classicDriverNames, ClassLoader loader, Set<Driver> result) throws SQLException {
        for (String classicDriverName : classicDriverNames) {
            try {
                Class.forName(classicDriverName);
            }
            catch (ClassNotFoundException ex) {
                try {
                    Class<?> driverClass = Class.forName(classicDriverName, true, loader);
                    // 読み込んだクラスがJDBCドライバーとして扱えるかどうかチェックする.
                    // クラスローダーが別だったり、JDBCドライバーではないクラスであった場合、
                    // 登録処理は行わない.
                    if (Driver.class.isAssignableFrom(driverClass)) {
                        Driver driver = (Driver) driverClass.newInstance();
                        registerDriver(new JdbcDriverProxy(driver));
                        result.add(driver);
                        logger.info("Load JDBC Driver. {}", driver);
                    }
                    else {
                        logger.warn("{} is not JDBC Driver. ", driverClass);
                    }
                }
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException ex1) {
                    logger.warn("JDBC Driver load failed. {}", classicDriverName);
                }
            }
        }
    }

    /**
     *
     * @param paths
     * @return
     * @throws IOException
     */
    @Nonnull
    protected URLClassLoader newClassLoader(Collection<Path> paths) throws IOException {
        URLClassLoaderBuilder builder = URLClassLoaderBuilder.builder();
        builder.setParentClassLoader(parentClassLoader);
        for (Path p : paths) {
            builder.addDirectory(p);
        }
        URLClassLoader cl = builder.build();
        return cl;
    }

}

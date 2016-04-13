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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Setter;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 *
 * @author enlo
 */
@Setter
public class ExternalJdbcDriverSetFactoryBean
        extends AbstractFactoryBean<ExternalJdbcDriverSet> {

    List<String> classicJdbcDriverNames = Collections.EMPTY_LIST;

    List<String> jarDirectories = Collections.EMPTY_LIST;

    @Override
    public Class<?> getObjectType() {
        return ExternalJdbcDriverSet.class;
    }

    public void setClassicJdbcDriverName(String classicJdbcDriverName) {
        classicJdbcDriverNames = Arrays.asList(classicJdbcDriverName);
    }

    public void setJarDirectory(String jarDirectory) {
        jarDirectories = Arrays.asList(jarDirectory);
    }

    @Override
    protected ExternalJdbcDriverSet createInstance() throws Exception {
        Set<Path> paths = new HashSet<>();
        for (String dir : jarDirectories) {
            paths.add(Paths.get(dir));
        }
        Set<String> names = new HashSet<>(classicJdbcDriverNames);
        return new ExternalJdbcDriverSet(paths, names);
    }

}

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
package info.naiv.lab.java.jmt.support.spring;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
public class FileSystemMessageSource extends ReloadableResourceBundleMessageSource {

    Path baseDirectory;
    ResourceLoader localResourceLoader;

    /**
     *
     */
    public FileSystemMessageSource() {
        localResourceLoader = new ResourceLoader();
        super.setResourceLoader(localResourceLoader);
    }

    /**
     *
     * @param baseDirectory
     */
    public void setBaseDirectory(String baseDirectory) {
        if (baseDirectory == null) {
            this.baseDirectory = null;
        }
        else {
            this.baseDirectory = Paths.get(baseDirectory);
        }
    }

    class ResourceLoader extends FileSystemResourceLoader {

        @Override
        protected Resource getResourceByPath(String path) {
            if (path != null && baseDirectory != null) {
                Path p = Paths.get(path);
                if (!p.isAbsolute()) {
                    p = baseDirectory.resolve(p);
                }
                if (Files.exists(p)) {
                    path = p.toString();
                }
            }
            return super.getResourceByPath(path);
        }
    }

}

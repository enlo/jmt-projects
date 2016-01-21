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

import java.io.IOException;
import java.nio.file.Path;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class SystemTempDirectoryTest extends TempDirectoryTest {

    public SystemTempDirectoryTest() {
    }

    @Test()
    public void testCtor() throws IOException {
        try (TempDirectory temp1 = newConcrete();
             TempDirectory temp2 = newConcrete(temp1.getPath(), "");
             TempDirectory temp3 = newConcrete(temp1.getPath(), "")) {

           assertThat(temp2.getPath().getParent(), is(temp1.getPath()));
           assertThat(temp3.getPath().getParent(), is(temp1.getPath()));
           assertThat(temp3.getPath(), is(not(temp2.getPath())));
        }
    }

    @Override
    protected TempDirectory newConcrete() throws IOException {
        TempDirectory td = new SystemTempDirectory();
        return td;
    }

    @Override
    protected TempDirectory newConcrete(String prefix) throws IOException {
        TempDirectory td = new SystemTempDirectory(prefix);
        return td;
    }

    protected TempDirectory newConcrete(Path root, String prefix) throws IOException {
        TempDirectory td = new SystemTempDirectory(root, prefix);
        return td;
    }
}

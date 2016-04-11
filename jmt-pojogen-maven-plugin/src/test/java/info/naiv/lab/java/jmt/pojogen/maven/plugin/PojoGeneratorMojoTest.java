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
package info.naiv.lab.java.jmt.pojogen.maven.plugin;

import info.naiv.lab.java.jmt.io.NIOUtils;
import java.io.File;
import java.nio.file.Paths;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;

/**
 *
 * @author enlo
 */
public class PojoGeneratorMojoTest {

    @Rule
    public MojoRule mojoRule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources();

    public PojoGeneratorMojoTest() {
    }

    /**
     * Test of execute method, of class BeanCreationMojo.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testExecute() throws Exception {
        // src/test/projects/project/pom.xml に書かれた設定を元にMojoインスタンスを作成
        File baseDir = resources.getBasedir("project");
        File pom = new File(baseDir, "pom.xml");

        // 'help' ゴールを実行
        Mojo mojo = mojoRule.lookupMojo("jmt-pojogen", pom);
        assertThat(mojo, is(notNullValue()));
        assertThat(mojo, is(instanceOf(PojoGeneratorMojo.class)));

        PojoGeneratorMojo bcmojo = (PojoGeneratorMojo) mojo;
        String value = bcmojo.toString();
        System.out.println(value);
        String path = bcmojo.outputDirectory;
//        try {
//            bcmojo.execute();
//        }
//        finally {
//            NIOUtils.deleteRecursive(Paths.get(path), true);
//        }
    }

}

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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 *
 * @author enlo
 */
public class FileSystemMessageSourceTest {

    private FileSystemMessageSource messageSource;
    private MessageSource parentMessageSource;

    /**
     *
     */
    public FileSystemMessageSourceTest() {
    }

    /**
     * Test of setBaseDirectory method, of class FileSystemMessageSource.
     */
    @Test
    public void getJapaneseMessageTest1() {
        String hello = messageSource.getMessage("hello.message", null, Locale.JAPAN);
        String bye = messageSource.getMessage("bye.message", null, Locale.JAPAN);
        assertThat(hello, is("こんにちは"));
        assertThat(bye, is("Bye"));
    }

    /**
     * Test of setBaseDirectory method, of class FileSystemMessageSource.
     */
    @Test
    public void getJapaneseMessageTest2() {

        Properties p = new Properties();
        p.setProperty("hello.message", "ハロー");
        messageSource.setCommonMessages(p);

        String hello = messageSource.getMessage("hello.message", null, Locale.JAPAN);
        String bye = messageSource.getMessage("bye.message", null, Locale.JAPAN);
        assertThat(hello, is("ハロー"));
        assertThat(bye, is("Bye"));
    }

    /**
     * Test of setBaseDirectory method, of class FileSystemMessageSource.
     *
     * @throws java.io.IOException
     */
    @Test
    public void getJapaneseMessageTest3() throws IOException {

        Path tempDir = Files.createTempDirectory("test");
        Path tempFile = tempDir.resolve("message_ja_JP.properties");
        try {
            Properties p = new Properties();
            p.setProperty("hello.message", "やあ");
            try (OutputStream os = Files.newOutputStream(tempFile)) {
                p.store(os, "");
            }

            messageSource.setBaseDirectory(tempDir.toString());
            String hello = messageSource.getMessage("hello.message", null, Locale.JAPAN);
            String bye = messageSource.getMessage("bye.message", null, Locale.JAPAN);
            assertThat(hello, is("やあ"));
            assertThat(bye, is("Bye"));
        }
        finally {
            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(tempDir);
        }

    }

    /**
     * Test of setBaseDirectory method, of class FileSystemMessageSource.
     */
    @Test
    public void getMessageTest() {
        String hello = messageSource.getMessage("hello.message", null, Locale.ENGLISH);
        assertThat(hello, is("Hello"));
    }

    /**
     *
     */
    @Before
    public void setup() {
        ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
        parent.setBasename("message");
        parent.setFallbackToSystemLocale(false);
        parentMessageSource = parent;

        messageSource = new FileSystemMessageSource();
        messageSource.setBasename("message");
        messageSource.setParentMessageSource(parent);
    }

}

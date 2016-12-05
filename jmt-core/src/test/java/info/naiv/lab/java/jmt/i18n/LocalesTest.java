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
package info.naiv.lab.java.jmt.i18n;

import java.util.Locale;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class LocalesTest {

    public LocalesTest() {
    }

    /**
     * Test of toLocale method, of class Locales.
     */
    @Test
    public void testToLocale() {
        Locale loc1 = Locales.toLocale("ja-JP-x-lvariant-JP");
        assertThat(loc1.toLanguageTag(), is("ja-JP-u-ca-japanese-x-lvariant-JP"));

        Locale loc2 = Locales.toLocale("th-TH-x-lvariant-TH");
        assertThat(loc2.toLanguageTag(), is("th-TH-u-nu-thai-x-lvariant-TH"));
    }

    /**
     * Test of toLocale method, of class Locales.
     */
    @Test
    public void testToLocale_parseToString() {
        Locale loc1 = Locales.toLocale("en");
        assertThat(loc1, is(new Locale("en")));

        Locale loc2 = Locales.toLocale("de_DE");
        assertThat(loc2, is(new Locale("de", "DE")));

        Locale loc3 = Locales.toLocale("_GB");
        assertThat(loc3, is(new Locale("", "GB")));

        Locale loc4 = Locales.toLocale("en_US_WIN");
        assertThat(loc4, is(new Locale("en", "US", "WIN")));

        Locale loc5 = Locales.toLocale("de__POSIX");
        assertThat(loc5, is(new Locale("de", "", "POSIX")));

        Locale loc6 = Locales.toLocale("zh_CN_#Hans");
        assertThat(loc6, is(new Locale.Builder().setLanguage("zh")
                   .setRegion("CN").setScript("Hans").build()));

        Locale loc7 = Locales.toLocale("zh_TW_#Hant-x-java");
        assertThat(loc7, is(new Locale.Builder().setLanguage("zh")
                   .setRegion("TW").setScript("Hant").setExtension('x', "java").build()));

        Locale loc8 = Locales.toLocale("th_TH_#u-nu-thai-x-java");
        assertThat(loc8, is(new Locale.Builder().setLanguage("th")
                   .setRegion("TH").setExtension('u', "nu-thai")
                   .setExtension('x', "java").build()));

        Locale loc9 = Locales.toLocale("zh_TW_#Hant_x-java-u-nu-thai");
        assertThat(loc9, is(new Locale.Builder().setLanguage("zh")
                   .setRegion("TW").setScript("Hant")
                   .setExtension('u', "nu-thai").setExtension('x', "java").build()));
    }
}

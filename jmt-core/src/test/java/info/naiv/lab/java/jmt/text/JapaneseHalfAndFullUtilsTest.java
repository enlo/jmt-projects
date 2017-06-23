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
package info.naiv.lab.java.jmt.text;

import java.util.Arrays;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author enlo
 */
public class JapaneseHalfAndFullUtilsTest {

    /**
     *
     */
    public JapaneseHalfAndFullUtilsTest() {
    }

    /**
     * Test of convertFullKanaToHalfKana method, of class
     * JapaneseHalfAndFullUtils.
     */
    @Test
    public void testConvertFullKanaToHalfKana() {

        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ\n"
                + "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ\n"
                + "１２３４５６７８９０\n"
                + "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［＼］＾＿｛｜｝～"
                + "あいうえお アイウエオ ヴ\n"
                + "かきくけこ カキクケコ がぎぐげご ガギグゲゴ\n"
                + "さしすせそ サシスセソ ざじずぜぞ ザジズゼゾ\n"
                + "たちつてと タチツテト だぢづでど ダヂヅデド\n"
                + "なにぬねの ナニヌネノ\n"
                + "はひふへほ ハヒフヘホ ばびぶべぼ バビブベボ ぱぴぷぺぽ パピプペポ\n"
                + "まみむめも マミムメモ\n"
                + "やゆよ ヤユヨ\n"
                + "らりるれろ ラリルレロ\n"
                + "わをん ワヲン\n"
                + "ぁぃぅぇぉ ァィゥェォ っゅょ ッュョー"
                + "神\uFA1E";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ\n"
                + "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ\n"
                + "１２３４５６７８９０\n"
                + "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［＼］＾＿｛｜｝～"
                + "あいうえお ｱｲｳｴｵ ｳﾞ\n"
                + "かきくけこ ｶｷｸｹｺ がぎぐげご ｶﾞｷﾞｸﾞｹﾞｺﾞ\n"
                + "さしすせそ ｻｼｽｾｿ ざじずぜぞ ｻﾞｼﾞｽﾞｾﾞｿﾞ\n"
                + "たちつてと ﾀﾁﾂﾃﾄ だぢづでど ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ\n"
                + "なにぬねの ﾅﾆﾇﾈﾉ\n"
                + "はひふへほ ﾊﾋﾌﾍﾎ ばびぶべぼ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ぱぴぷぺぽ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ\n"
                + "まみむめも ﾏﾐﾑﾒﾓ\n"
                + "やゆよ ﾔﾕﾖ\n"
                + "らりるれろ ﾗﾘﾙﾚﾛ\n"
                + "わをん ﾜｦﾝ\n"
                + "ぁぃぅぇぉ ｧｨｩｪｫ っゅょ ｯｭｮｰ"
                + "神\uFA1E";
        String actual = JapaneseHalfAndFullUtils.convertFullKanaToHalfKana(source, false);
        assertThat(actual, is(expected));
    }

    /**
     * Test of convertFullKanaToHalfKana method, of class
     * JapaneseHalfAndFullUtils.
     */
    @Test
    public void testConvertFullKanaToHalfKana_02() {

        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "あいうえお アイウエオ ヴ\n"
                + "かきくけこ カキクケコ がぎぐげご ガギグゲゴ\n"
                + "さしすせそ サシスセソ ざじずぜぞ ザジズゼゾ\n"
                + "たちつてと タチツテト だぢづでど ダヂヅデド\n"
                + "なにぬねの ナニヌネノ\n"
                + "はひふへほ ハヒフヘホ ばびぶべぼ バビブベボ ぱぴぷぺぽ パピプペポ\n"
                + "まみむめも マミムメモ\n"
                + "やゆよ ヤユヨ\n"
                + "らりるれろ ラリルレロ\n"
                + "わをん ワヲン\n"
                + "ぁぃぅぇぉ ァィゥェォ っゅょ ッュョー"
                + "神\uFA1E";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ｱｲｳｴｵ ｱｲｳｴｵ ｳﾞ\n"
                + "ｶｷｸｹｺ ｶｷｸｹｺ ｶﾞｷﾞｸﾞｹﾞｺﾞ ｶﾞｷﾞｸﾞｹﾞｺﾞ\n"
                + "ｻｼｽｾｿ ｻｼｽｾｿ ｻﾞｼﾞｽﾞｾﾞｿﾞ ｻﾞｼﾞｽﾞｾﾞｿﾞ\n"
                + "ﾀﾁﾂﾃﾄ ﾀﾁﾂﾃﾄ ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ\n"
                + "ﾅﾆﾇﾈﾉ ﾅﾆﾇﾈﾉ\n"
                + "ﾊﾋﾌﾍﾎ ﾊﾋﾌﾍﾎ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ\n"
                + "ﾏﾐﾑﾒﾓ ﾏﾐﾑﾒﾓ\n"
                + "ﾔﾕﾖ ﾔﾕﾖ\n"
                + "ﾗﾘﾙﾚﾛ ﾗﾘﾙﾚﾛ\n"
                + "ﾜｦﾝ ﾜｦﾝ\n"
                + "ｧｨｩｪｫ ｧｨｩｪｫ ｯｭｮ ｯｭｮｰ"
                + "神\uFA1E";
        String actual = JapaneseHalfAndFullUtils.convertFullKanaToHalfKana(source, true);
        assertThat(actual, is(expected));
    }

    /**
     * Test of convertFullToHalf method, of class JapaneseHalfAndFullUtils.
     */
    @Test
    public void testConvertFullToHalf_String_boolean() {

        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~\n"
                + "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ\n"
                + "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ\n"
                + "１２３４５６７８９０\n"
                + "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［＼］＾＿｛｜｝～\n"
                + "あいうえお アイウエオ ヴ\n"
                + "かきくけこ カキクケコ がぎぐげご ガギグゲゴ\n"
                + "さしすせそ サシスセソ ざじずぜぞ ザジズゼゾ\n"
                + "たちつてと タチツテト だぢづでど ダヂヅデド\n"
                + "なにぬねの ナニヌネノ\n"
                + "はひふへほ ハヒフヘホ ばびぶべぼ バビブベボ ぱぴぷぺぽ パピプペポ\n"
                + "まみむめも マミムメモ\n"
                + "やゆよ ヤユヨ\n"
                + "らりるれろ ラリルレロ\n"
                + "わをん ワヲン\n"
                + "ぁぃぅぇぉ ァィゥェォ っゅょ ッュョー"
                + "神\uFA1E";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~\n"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~\n"
                + "あいうえお ｱｲｳｴｵ ｳﾞ\n"
                + "かきくけこ ｶｷｸｹｺ がぎぐげご ｶﾞｷﾞｸﾞｹﾞｺﾞ\n"
                + "さしすせそ ｻｼｽｾｿ ざじずぜぞ ｻﾞｼﾞｽﾞｾﾞｿﾞ\n"
                + "たちつてと ﾀﾁﾂﾃﾄ だぢづでど ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ\n"
                + "なにぬねの ﾅﾆﾇﾈﾉ\n"
                + "はひふへほ ﾊﾋﾌﾍﾎ ばびぶべぼ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ぱぴぷぺぽ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ\n"
                + "まみむめも ﾏﾐﾑﾒﾓ\n"
                + "やゆよ ﾔﾕﾖ\n"
                + "らりるれろ ﾗﾘﾙﾚﾛ\n"
                + "わをん ﾜｦﾝ\n"
                + "ぁぃぅぇぉ ｧｨｩｪｫ っゅょ ｯｭｮｰ"
                + "神\uFA1E";
        String actual = JapaneseHalfAndFullUtils.convertFullToHalf(source, false);
        assertThat(actual, is(expected));
    }

    /**
     * Test of convertFullToHalf method, of class JapaneseHalfAndFullUtils.
     */
    @Test
    public void testConvertFullToHalf_String_boolean_02() {
        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ\n"
                + "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ\n"
                + "１２３４５６７８９０\n"
                + "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［＼］＾＿｛｜｝～"
                + "あいうえお アイウエオ ヴ\n"
                + "かきくけこ カキクケコ がぎぐげご ガギグゲゴ\n"
                + "さしすせそ サシスセソ ざじずぜぞ ザジズゼゾ\n"
                + "たちつてと タチツテト だぢづでど ダヂヅデド\n"
                + "なにぬねの ナニヌネノ\n"
                + "はひふへほ ハヒフヘホ ばびぶべぼ バビブベボ ぱぴぷぺぽ パピプペポ\n"
                + "まみむめも マミムメモ\n"
                + "やゆよ ヤユヨ\n"
                + "らりるれろ ラリルレロ\n"
                + "わをん ワヲン\n"
                + "ぁぃぅぇぉ ァィゥェォ っゅょ ッュョー"
                + "神\uFA1E";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ｱｲｳｴｵ ｱｲｳｴｵ ｳﾞ\n"
                + "ｶｷｸｹｺ ｶｷｸｹｺ ｶﾞｷﾞｸﾞｹﾞｺﾞ ｶﾞｷﾞｸﾞｹﾞｺﾞ\n"
                + "ｻｼｽｾｿ ｻｼｽｾｿ ｻﾞｼﾞｽﾞｾﾞｿﾞ ｻﾞｼﾞｽﾞｾﾞｿﾞ\n"
                + "ﾀﾁﾂﾃﾄ ﾀﾁﾂﾃﾄ ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ\n"
                + "ﾅﾆﾇﾈﾉ ﾅﾆﾇﾈﾉ\n"
                + "ﾊﾋﾌﾍﾎ ﾊﾋﾌﾍﾎ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ\n"
                + "ﾏﾐﾑﾒﾓ ﾏﾐﾑﾒﾓ\n"
                + "ﾔﾕﾖ ﾔﾕﾖ\n"
                + "ﾗﾘﾙﾚﾛ ﾗﾘﾙﾚﾛ\n"
                + "ﾜｦﾝ ﾜｦﾝ\n"
                + "ｧｨｩｪｫ ｧｨｩｪｫ ｯｭｮ ｯｭｮｰ"
                + "神\uFA1E";
        String actual = JapaneseHalfAndFullUtils.convertFullToHalf(source, true);
        assertThat(actual, is(expected));
    }

    /**
     * Test of convertHalfKanaToFullKana method, of class
     * JapaneseHalfAndFullUtils.
     */
    @Test
    public void testConvertHalfKanaToFullKana() {
        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ\n"
                + "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ\n"
                + "１２３４５６７８９０\n"
                + "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［＼］＾＿｛｜｝～"
                + "ｱｲｳｴｵ ｳﾞ\n"
                + "ｶｷｸｹｺ ｶﾞｷﾞｸﾞｹﾞｺﾞ\n"
                + "ｻｼｽｾｿ ｻﾞｼﾞｽﾞｾﾞｿﾞ\n"
                + "ﾀﾁﾂﾃﾄ ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ\n"
                + "ﾅﾆﾇﾈﾉ\n"
                + "ﾊﾋﾌﾍﾎ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ\n"
                + "ﾏﾐﾑﾒﾓ\n"
                + "ﾔﾕﾖ\n"
                + "ﾗﾘﾙﾚﾛ\n"
                + "ﾜｦﾝ\n"
                + "ｧｨｩｪｫ ｯｭｮｰ"
                + "神\uFA1E";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ\n"
                + "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ\n"
                + "１２３４５６７８９０\n"
                + "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［＼］＾＿｛｜｝～"
                + "アイウエオ ヴ\n"
                + "カキクケコ ガギグゲゴ\n"
                + "サシスセソ ザジズゼゾ\n"
                + "タチツテト ダヂヅデド\n"
                + "ナニヌネノ\n"
                + "ハヒフヘホ バビブベボ パピプペポ\n"
                + "マミムメモ\n"
                + "ヤユヨ\n"
                + "ラリルレロ\n"
                + "ワヲン\n"
                + "ァィゥェォ ッュョー"
                + "神\uFA1E";

        String actual = JapaneseHalfAndFullUtils.convertHalfKanaToFullKana(source);
        assertThat(actual, is(expected));
    }

    /**
     * Test of convertHalfToFull method, of class JapaneseHalfAndFullUtils.
     */
    @Test
    public void testConvertHalfToFull_String() {
        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
                + "abcdefghijklmnopqrstuvwxyz\n"
                + "1234567890\n"
                + "!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~"
                + "ｱｲｳｴｵ ｳﾞ\n"
                + "ｶｷｸｹｺ ｶﾞｷﾞｸﾞｹﾞｺﾞ\n"
                + "ｻｼｽｾｿ ｻﾞｼﾞｽﾞｾﾞｿﾞ\n"
                + "ﾀﾁﾂﾃﾄ ﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞ\n"
                + "ﾅﾆﾇﾈﾉ\n"
                + "ﾊﾋﾌﾍﾎ ﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ\n"
                + "ﾏﾐﾑﾒﾓ\n"
                + "ﾔﾕﾖ\n"
                + "ﾗﾘﾙﾚﾛ\n"
                + "ﾜｦﾝ\n"
                + "ｧｨｩｪｫ ｯｭｮｰ"
                + "神\uFA1E";
        String expected = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ\n"
                + "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ\n"
                + "１２３４５６７８９０\n"
                + "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［＼］＾＿｛｜｝～"
                + "アイウエオ ヴ\n"
                + "カキクケコ ガギグゲゴ\n"
                + "サシスセソ ザジズゼゾ\n"
                + "タチツテト ダヂヅデド\n"
                + "ナニヌネノ\n"
                + "ハヒフヘホ バビブベボ パピプペポ\n"
                + "マミムメモ\n"
                + "ヤユヨ\n"
                + "ラリルレロ\n"
                + "ワヲン\n"
                + "ァィゥェォ ッュョー"
                + "神\uFA1E";

        String actual = JapaneseHalfAndFullUtils.convertHalfToFull(source);
        assertThat(actual, is(expected));
    }

    /**
     * 各テーブルがキー順にソートされていることを確認.
     */
    @Test
    public void testTableOrder() {
        checkTableOrder("FULLWIDTH_SYMBOL_VARIANTS_TABLE", JapaneseHalfAndFullUtils.FULLWIDTH_SYMBOL_VARIANTS_TABLE);
        checkTableOrder("HALFWIDTH_CJK_PUNCTUATION_TABLE", JapaneseHalfAndFullUtils.HALFWIDTH_CJK_PUNCTUATION_TABLE);
        checkTableOrder("HALFWIDTH_HANGUL_VARIANTS_TABLE", JapaneseHalfAndFullUtils.HALFWIDTH_HANGUL_VARIANTS_TABLE);
        checkTableOrder("HALFWIDTH_KATAKANA_VARIANTS_TABLE", JapaneseHalfAndFullUtils.HALFWIDTH_KATAKANA_VARIANTS_TABLE);
        checkTableOrder("HALFWIDTH_SYMBOL_VARIANTS_TABLE", JapaneseHalfAndFullUtils.HALFWIDTH_SYMBOL_VARIANTS_TABLE);
    }

    private void checkTableOrder(String name, int[][] table) {
        int[] keys = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            keys[i] = table[i][0];
        }
        int[] sorted = Arrays.copyOf(keys, keys.length);
        Arrays.sort(sorted);
        assertThat(name, keys, is(sorted));
    }

}

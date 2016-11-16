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

import java.nio.IntBuffer;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 * 伝統的な半角全角処理. <br>
 * 本来なら ICU を使用して変換を行うべき.
 *
 * @author enlo
 */
public class JapaneseHalfAndFullUtils {

    /**
     * Full width ASCII variants offset
     */
    public static final int ASCII_VARIANTS_OFFSET = 0xFEE0;

    /**
     * Full width brackets offset
     */
    public static final int BRACKEATS_OFFSET = 0xD5DA;

    /**
     * Full width ASCII variants from
     */
    public static final int FULLWIDTH_ASCII_VARIANTS_FROM = 0xFF01;
    /**
     * Full width ASCII variants to
     */
    public static final int FULLWIDTH_ASCII_VARIANTS_TO = 0xFF5E;

    /**
     * Full width brackets from
     */
    public static final int FULLWIDTH_BRACKETS_FROM = 0xFF5F;

    /**
     * Full width brackets to
     */
    public static final int FULLWIDTH_BRACKETS_TO = 0xFF60;

    /**
     * Full width symbol variants table
     */
    public static final int[][] FULLWIDTH_SYMBOL_VARIANTS_TABLE = {
        {0xffe0, 0x00a2}, {0xffe1, 0x00a3}, {0xffe2, 0x00ac}, {0xffe3, 0x00af},
        {0xffe4, 0x00a6}, {0xffe5, 0x00a5}, {0xffe6, 0x20a9}};
    /**
     * 半角全角開始.
     */
    public static final int HALFWIDTH_AND_FULLWIDTH_FORMS_BEGIN = 0xFF00;
    /**
     * 半角全角終了.
     */
    public static final int HALFWIDTH_AND_FULLWIDTH_FORMS_END = 0xFFEF;

    /**
     * Half width CJK punctuation table
     */
    public static final int[][] HALFWIDTH_CJK_PUNCTUATION_TABLE = {
        {0xFF61, 0x3002}, {0xFF62, 0x300C}, {0xFF63, 0x300D}, {0xFF64, 0x3001}};

    /**
     * Half width Hangul variants table
     */
    public static final int[][] HALFWIDTH_HANGUL_VARIANTS_TABLE = {
        {0xFFA0, 0x3164}, {0xFFA1, 0x3131}, {0xFFA2, 0x3132}, {0xFFA3, 0x3133},
        {0xFFA4, 0x3134}, {0xFFA5, 0x3135}, {0xFFA6, 0x3136}, {0xFFA7, 0x3137},
        {0xFFA8, 0x3138}, {0xFFA9, 0x3139}, {0xFFAA, 0x313A}, {0xFFAB, 0x313B},
        {0xFFAC, 0x313C}, {0xFFAD, 0x313D}, {0xFFAE, 0x313E}, {0xFFAF, 0x313F},
        {0xFFB0, 0x3140}, {0xFFB1, 0x3141}, {0xFFB2, 0x3142}, {0xFFB3, 0x3143},
        {0xFFB4, 0x3144}, {0xFFB5, 0x3145}, {0xFFB6, 0x3146}, {0xFFB7, 0x3147},
        {0xFFB8, 0x3148}, {0xFFB9, 0x3149}, {0xFFBA, 0x314A}, {0xFFBB, 0x314B},
        {0xFFBC, 0x314C}, {0xFFBD, 0x314D}, {0xFFBE, 0x314E}, {0xFFC2, 0x314F},
        {0xFFC3, 0x3150}, {0xFFC4, 0x3151}, {0xFFC5, 0x3152}, {0xFFC6, 0x3153},
        {0xFFC7, 0x3154}, {0xFFCA, 0x3155}, {0xFFCB, 0x3156}, {0xFFCC, 0x3157},
        {0xFFCD, 0x3158}, {0xFFCE, 0x3159}, {0xFFCF, 0x315A}, {0xFFD2, 0x315B},
        {0xFFD3, 0x315C}, {0xFFD4, 0x315D}, {0xFFD5, 0x315E}, {0xFFD6, 0x315F},
        {0xFFD7, 0x3160}, {0xFFDA, 0x3161}, {0xFFDB, 0x3162}, {0xFFDC, 0x3163},};

    /**
     *
     */
    public static final int HALFWIDTH_JP_SONANT_MARK = 0xFF9E;

    /**
     * Halfwidth katakana begin.
     */
    public static final int HALFWIDTH_KATAKANA_BEGIN = 0xFF66;

    /**
     * Halfwidth katakana end.
     */
    public static final int HALFWIDTH_KATAKANA_END = 0xFF9D;
    /**
     * 半濁点テーブル
     */
    public static final int[][] HALFWIDTH_KATAKANA_SEMI_SONANT_VARIANTS_TABLE = {
        {0xFF8A, 0x30D1}, {0xFF8B, 0x30D4}, {0xFF8C, 0x30D7}, {0xFF8D, 0x30DA}, {0xFF8E, 0x30DD},};

    /**
     * 濁点テーブル
     */
    public static final int[][] HALFWIDTH_KATAKANA_SONANT_VARIANTS_TABLE = {
        {0xFF73, 0x30F4},
        {0xFF76, 0x30AC}, {0xFF77, 0x30AE}, {0xFF78, 0x30B0}, {0xFF79, 0x30B2}, {0xFF7A, 0x30B4},
        {0xFF7B, 0x30B6}, {0xFF7C, 0x30B8}, {0xFF7D, 0x30BA}, {0xFF7E, 0x30BC}, {0xFF7F, 0x30BE},
        {0xFF80, 0x30C0}, {0xFF81, 0x30C2}, {0xFF82, 0x30C5}, {0xFF83, 0x30C7}, {0xFF84, 0x30C9},
        {0xFF8A, 0x30D0}, {0xFF8B, 0x30D3}, {0xFF8C, 0x30D6}, {0xFF8D, 0x30D9}, {0xFF8E, 0x30DC},};

    /**
     * Half width Katakana variants table
     */
    public static final int[][] HALFWIDTH_KATAKANA_VARIANTS_TABLE = {
        {0xFF65, 0x30FB}, {0xFF66, 0x30F2},
        {0xFF67, 0x30A1}, {0xFF68, 0x30A3}, {0xFF69, 0x30A5}, {0xFF6A, 0x30A7}, {0xFF6B, 0x30A9},
        {0xFF6C, 0x30E3}, {0xFF6D, 0x30E5}, {0xFF6E, 0x30E7}, {0xFF6F, 0x30C3}, {0xFF70, 0x30FC},
        {0xFF71, 0x30A2}, {0xFF72, 0x30A4}, {0xFF73, 0x30A6}, {0xFF74, 0x30A8}, {0xFF75, 0x30AA},
        {0xFF76, 0x30AB}, {0xFF77, 0x30AD}, {0xFF78, 0x30AF}, {0xFF79, 0x30B1}, {0xFF7A, 0x30B3},
        {0xFF7B, 0x30B5}, {0xFF7C, 0x30B7}, {0xFF7D, 0x30B9}, {0xFF7E, 0x30BB}, {0xFF7F, 0x30BD},
        {0xFF80, 0x30BF}, {0xFF81, 0x30C1}, {0xFF82, 0x30C4}, {0xFF83, 0x30C6}, {0xFF84, 0x30C8},
        {0xFF85, 0x30CA}, {0xFF86, 0x30CB}, {0xFF87, 0x30CC}, {0xFF88, 0x30CD}, {0xFF89, 0x30CE},
        {0xFF8A, 0x30CF}, {0xFF8B, 0x30D2}, {0xFF8C, 0x30D5}, {0xFF8D, 0x30D8}, {0xFF8E, 0x30DB},
        {0xFF8F, 0x30DE}, {0xFF90, 0x30DF}, {0xFF91, 0x30E0}, {0xFF92, 0x30E1}, {0xFF93, 0x30E2},
        {0xFF94, 0x30E4}, {0xFF95, 0x30E6}, {0xFF96, 0x30E8},
        {0xFF97, 0x30E9}, {0xFF98, 0x30EA}, {0xFF99, 0x30EB}, {0xFF9A, 0x30EC}, {0xFF9B, 0x30ED},
        {0xFF9C, 0x30EF}, {0xFF9D, 0x30F3},
        {0xFF9E, 0x3099}, {0xFF9F, 0x309A},};

    /**
     * Half width symbol variants from
     */
    public static final int[][] HALFWIDTH_SYMBOL_VARIANTS_TABLE = {
        {0xffe8, 0x2502}, {0xffe9, 0x2190}, {0xffea, 0x2191}, {0xffeb, 0x2192},
        {0xffec, 0x2193}, {0xffed, 0x25a0}, {0xffee, 0x25cb}};

    /**
     * Half width table
     */
    public static final int[][][] HALFWIDTH_TABLE = {
        HALFWIDTH_CJK_PUNCTUATION_TABLE,
        HALFWIDTH_HANGUL_VARIANTS_TABLE,
        HALFWIDTH_KATAKANA_VARIANTS_TABLE,
        HALFWIDTH_SYMBOL_VARIANTS_TABLE
    };
    private static final int HALFWIDTH_JP_SEMI_SONANT_MARK = 0xFF9F;

    /**
     * 全角カタカナを半角カタカナに変換する. <br>
     * 全角ひらがなも半角カタカナに変換する場合は、convertHiragana に true を設定する.<br>
     *
     * @param source
     * @param convertHiragana ひらがなを変換する場合は true.
     * @return
     */
    public static String convertFullKanaToHalfKana(String source, final boolean convertHiragana) {

        ConvertCallback converter = new ConvertCallback() {
            @Override
            public int convert(IntBuffer buff, int prev, int ch) {
                if (convertHiragana) {
                    ch = convertHiraganaToKatakana(ch);
                }
                return doConvertFullKanaToHalfKana(ch, buff);
            }
        };

        return convert(source, converter);
    }

    /**
     * 全角英数カナを半角英数カナに変換する. 全角ひらがなも半角カタカナに変換する場合は、convertHiragana に true を設定する.
     *
     * @param source
     * @param convertHiragana ひらがなを変換する場合は true.
     * @return
     */
    public static String convertFullToHalf(String source, final boolean convertHiragana) {

        ConvertCallback converter = new ConvertCallback() {
            @Override
            public int convert(IntBuffer buff, int prev, int ch) {
                if (convertHiragana) {
                    ch = convertHiraganaToKatakana(ch);
                }
                int x = doConvertFullKanaToHalfKana(ch, buff);
                if (x != ch) {
                    return x;
                }
                return convertFullToHalf(ch);
            }
        };
        return convert(source, converter);
    }

    /**
     * 文字を全角から半角に変換.
     *
     * @param ch 文字.
     * @return 変換後の文字.
     */
    public static int convertFullToHalf(int ch) {
        if (FULLWIDTH_ASCII_VARIANTS_FROM <= ch && ch <= FULLWIDTH_ASCII_VARIANTS_TO) {
            return ch - ASCII_VARIANTS_OFFSET;
        }
        else if (FULLWIDTH_BRACKETS_FROM <= ch && ch <= FULLWIDTH_BRACKETS_TO) {
            return ch - BRACKEATS_OFFSET;
        }
        else {
            return findHalfwidthValue(ch);
        }
    }

    /**
     * 半角カタカナを全角カタカナに変換する.
     *
     * @param source
     * @return
     */
    public static String convertHalfKanaToFullKana(String source) {

        ConvertCallback converter = new ConvertCallback() {
            @Override
            public int convert(IntBuffer buff, int prev, int ch) {
                return doConvertHalfKanaToFullKana(ch, prev, buff);
            }
        };

        return convert(source, converter);
    }

    /**
     * 半角英数カナを全角英数カナに変換する.
     *
     * @param source
     * @return
     */
    public static String convertHalfToFull(String source) {

        return convert(source, new ConvertCallback() {

            @Override
            public int convert(IntBuffer buf, int prev, int ch) {
                int x = doConvertHalfKanaToFullKana(ch, prev, buf);
                if (x != ch) {
                    return x;
                }
                return convertHalfToFull(ch);
            }
        });
    }

    /**
     * 文字を半角から全角に変換.
     *
     * @param ch 文字.
     * @return 変換後の文字.
     */
    public static int convertHalfToFull(int ch) {
        int x = ch + ASCII_VARIANTS_OFFSET;
        if (FULLWIDTH_ASCII_VARIANTS_FROM <= x && x <= FULLWIDTH_ASCII_VARIANTS_TO) {
            return x;
        }
        else {
            x = ch + BRACKEATS_OFFSET;
            if (FULLWIDTH_BRACKETS_FROM <= x && x <= FULLWIDTH_BRACKETS_TO) {
                return x;
            }
            else {
                return findHalfwidthKey(ch);
            }
        }
    }

    /**
     * ひらがなからカタカナに変換.
     *
     * @param cp 文字コード
     * @return 変換結果
     */
    public static int convertHiraganaToKatakana(int cp) {
        if ('ぁ' <= cp && cp <= 'ん') {
            cp = (cp - 'ぁ' + 'ァ');
        }
        return cp;
    }

    private static String convert(@NonNull String source, @Nonnull ConvertCallback converter) {
        int len = source.length();
        int size = source.codePointCount(0, len);
        IntBuffer buff = IntBuffer.allocate(size * 2);
        int prev = -1;
        for (int i = 0; i < len; i = source.offsetByCodePoints(i, 1)) {
            int ch = source.codePointAt(i);
            int res = converter.convert(buff, prev, ch);
            if (0 < res) {
                buff.put(res);
            }
            prev = ch;
        }
        buff.flip();
        String result = new String(buff.array(), 0, buff.limit());
        return result;
    }

    private static int doConvertFullKanaToHalfKana(int ch, IntBuffer buff) {
        if ((ch & 0xFF00) == 0x3000) {
            int x = findValue(HALFWIDTH_KATAKANA_SEMI_SONANT_VARIANTS_TABLE, ch);
            if (x != ch) {
                buff.put(x);
                return HALFWIDTH_JP_SEMI_SONANT_MARK;
            }
            x = findValue(HALFWIDTH_KATAKANA_SONANT_VARIANTS_TABLE, ch);
            if (x != ch) {
                buff.put(x);
                return HALFWIDTH_JP_SONANT_MARK;
            }
            x = findValue(HALFWIDTH_KATAKANA_VARIANTS_TABLE, ch);
            return x;
        }
        return ch;
    }

    private static int doConvertHalfKanaToFullKana(int ch, int prev, IntBuffer buff) {
        if (ch == HALFWIDTH_JP_SONANT_MARK) {
            if (HALFWIDTH_KATAKANA_BEGIN <= prev && prev <= HALFWIDTH_KATAKANA_END) {
                ch = findKey(HALFWIDTH_KATAKANA_SONANT_VARIANTS_TABLE, prev);
                buff.put(buff.position() - 1, ch);
                return -1;
            }
        }
        else if (ch == HALFWIDTH_JP_SEMI_SONANT_MARK) {
            if (HALFWIDTH_KATAKANA_BEGIN <= prev && prev <= HALFWIDTH_KATAKANA_END) {
                ch = findKey(HALFWIDTH_KATAKANA_SEMI_SONANT_VARIANTS_TABLE, prev);
                buff.put(buff.position() - 1, ch);
                return -1;
            }
        }
        else if (HALFWIDTH_KATAKANA_BEGIN <= ch && ch <= HALFWIDTH_KATAKANA_END) {
            ch = findKey(HALFWIDTH_KATAKANA_VARIANTS_TABLE, ch);
        }
        return ch;
    }

    private static int findHalfwidthKey(int code) {
        for (int[][] table : HALFWIDTH_TABLE) {
            int result = findKey(table, code);
            if (result != code) {
                return result;
            }
        }
        return code;
    }

    private static int findHalfwidthValue(int code) {
        for (int[][] table : HALFWIDTH_TABLE) {
            int result = findValue(table, code);
            if (result != code) {
                return result;
            }
        }
        return code;
    }

    private static int findKey(int[][] table, int keyCode) {
        int min = table[0][0];
        int max = table[table.length - 1][0];
        if (keyCode < min || max < keyCode) {
            return keyCode;
        }
        for (int[] table1 : table) {
            if (table1[0] == keyCode) {
                return table1[1];
            }
        }
        return keyCode;
    }

    private static int findValue(int[][] table, int valueCode) {
        for (int[] table1 : table) {
            if (table1[1] == valueCode) {
                return table1[0];
            }
        }
        return valueCode;
    }

    private JapaneseHalfAndFullUtils() {
    }

    private static interface ConvertCallback {

        int convert(IntBuffer buf, int prev, int ch);
    }

}

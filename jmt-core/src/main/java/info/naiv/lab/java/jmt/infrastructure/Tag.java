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
package info.naiv.lab.java.jmt.infrastructure;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import java.io.Serializable;
import javax.annotation.Nonnull;
import lombok.Data;

/**
 * タグ情報
 *
 * @author enlo
 */
@Data
public class Tag {

    /**
     * あらゆるタグに一致
     */
    public static final Tag ANY;

    /**
     * ANYのみ一致
     */
    public static final Tag NONE;

    static {
        ANY = new AnyTag();
        NONE = new NoneTag();
    }

    /**
     * ファクトリメソッド.
     *
     * @param id ID
     * @return タグ
     */
    @Nonnull
    public static Tag of(Serializable id) {
        return new Tag(id);
    }

    private final Serializable id;

    /**
     * コンストラクター
     *
     * @param id ID
     */
    public Tag(Serializable id) {
        nonNull(id, "id");
        this.id = id;
    }

    /**
     * タグが一致するかどうかをチェックする.<br>
     *
     * @param tag
     * @return
     */
    public boolean contains(Tag tag) {
        return equals(tag) || ANY.equals(tag);
    }

    private static class AnyTag extends Tag {

        AnyTag() {
            super(AnyTag.class);
        }

        @Override
        public boolean contains(Tag tag) {
            return true;
        }
    }

    private static class NoneTag extends Tag {

        NoneTag() {
            super(NoneTag.class);
        }

        @Override
        public boolean contains(Tag tag) {
            return ANY.equals(tag);
        }
    }
}

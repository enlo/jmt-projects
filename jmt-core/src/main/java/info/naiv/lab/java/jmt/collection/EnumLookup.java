/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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
package info.naiv.lab.java.jmt.collection;

import info.naiv.lab.java.jmt.IntegerEnum;
import java.util.EnumSet;
import lombok.Value;

/**
 *
 * @author enlo
 */
public class EnumLookup {

    public static <TEnum extends Enum & IntegerEnum>
            Lookup<Integer, TEnum> byIntegerEnum(Class<TEnum> clazz) {
        return new IntegerEnumLookup<>(EnumSet.allOf(clazz));
    }

    public static <TKey extends Comparable, TEnum extends Enum & KeyedValue<TKey>>
            Lookup<TKey, TEnum> byKeyedEnum(Class<TEnum> clazz) {
        return new KeyedValueLookup<>(EnumSet.allOf(clazz));
    }

    public static <TEnum extends Enum> Lookup<String, TEnum> byName(Class<TEnum> clazz) {
        return new NamedLookup<>(EnumSet.allOf(clazz));
    }

    private EnumLookup() {
    }

    @Value
    public static class IntegerEnumLookup<TEnum extends Enum & IntegerEnum>
            implements Lookup<Integer, TEnum> {

        Iterable<TEnum> values;

        @Override
        public boolean containsKey(Integer key) {
            return get(key) != null;
        }

        @Override
        public TEnum get(Integer key) {
            int k = key;
            for (TEnum e : values) {
                if (e.getValue() == k) {
                    return e;
                }
            }
            return null;
        }

    }

    @Value
    public static class NamedLookup<String, TEnum extends Enum>
            implements Lookup<String, TEnum> {

        Iterable<TEnum> values;

        @Override
        public boolean containsKey(String key) {
            return get(key) != null;
        }

        @Override
        public TEnum get(String key) {
            for (TEnum e : values) {
                if (e.name().equals(key)) {
                    return e;
                }
            }
            return null;
        }

    }

}

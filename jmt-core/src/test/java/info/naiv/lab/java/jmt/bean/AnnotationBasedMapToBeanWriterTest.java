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
package info.naiv.lab.java.jmt.bean;

import info.naiv.lab.java.jmt.bean.annotation.Mapping;
import info.naiv.lab.java.jmt.datetime.DateOnly;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author enlo
 */
public class AnnotationBasedMapToBeanWriterTest {

    public AnnotationBasedMapToBeanWriterTest() {
    }

    @Test
    public void testMap01() {

        AnnotationBasedMapToBeanWriter<Person> m1 = new AnnotationBasedMapToBeanWriter<>(Person.class);

        Map<String, Object> s01 = new HashMap<>();
        s01.put("名前", "山田 太郎");
        s01.put("年齢", "21");
        s01.put("メール", "taro@yamada.foo.bar");
        s01.put("住所", "XX県 YY市");

        Person p = new Person();
        assertThat(p.getName(), is(nullValue()));
        assertThat(p.getAge(), is(0));
        assertThat(p.getEmail(), is(nullValue()));
        assertThat(p.getAddress(), is(nullValue()));
        assertThat(p.getComment(), is(nullValue()));

        m1.write(s01, p);
        assertThat(p.getName(), is("山田 太郎"));
        assertThat(p.getAge(), is(21));
        assertThat(p.getEmail(), is("taro@yamada.foo.bar"));
        assertThat(p.getAddress(), is("XX県 YY市"));
        assertThat(p.getComment(), is("特になし"));
    }

    @Test
    public void testMap02() {

        AnnotationBasedMapToBeanWriter<Book> m1 = new AnnotationBasedMapToBeanWriter<>(Book.class);

        Map<String, Object> s01 = new HashMap<>();
        s01.put("名前", "てすと本");
        s01.put("発行日", "2017/07/06");

        Book p = new Book();
        assertThat(p.getName(), is(nullValue()));
        assertThat(p.getPublish(), is(nullValue()));

        Date date = new DateOnly(2017, 7, 6);

        m1.write(s01, p);
        assertThat(p.getName(), is("てすと本"));
        assertThat(p.getPublish(), is(date));

    }

    @Test
    public void testMap03() {

        AnnotationBasedMapToBeanWriter<Person> m1 = new AnnotationBasedMapToBeanWriter<>(Person.class);

        Map<String, Object> s01 = new HashMap<>();
        s01.put("年齢", "twelve");
        s01.put("メール", "taro@yamada.foo.bar");
        s01.put("住所", "XX県 YY市");

        Person p = new Person();

        final List<String> messages = new ArrayList<>();
        m1.addErrorHandler(new BeanWritingErrorHandler() {
            @Override
            public boolean handleError(Member target, TypeDescriptor type, Object value, Exception ex) {
                Mapping m = type.getAnnotation(Mapping.class);
                messages.add(m.byName() + "が不正です. " + value);
                return true;
            }
        });

        m1.write(s01, p);
        assertThat(p.getName(), is(nullValue()));
        assertThat(p.getAge(), is(0));
        assertThat(p.getEmail(), is("taro@yamada.foo.bar"));
        assertThat(p.getAddress(), is("XX県 YY市"));
        assertThat(p.getComment(), is("特になし"));

        assertThat(messages, containsInAnyOrder("名前が不正です. null", "年齢が不正です. twelve"));
    }

    @Getter
    static class Book {

        private String name;

        private Date publish;

        @Mapping(byName = "名前", required = true)
        public void setName(String name) {
            this.name = name;
        }

        @DateTimeFormat(pattern = "yyyy/MM/dd")
        @Mapping(byName = "発行日")
        public void setPublish(Date publish) {
            this.publish = publish;
        }

    }

    @Getter
    static class Person {

        @Mapping(byName = "住所")
        private String address;

        @Mapping(byName = "年齢", required = true)
        private int age;

        @Mapping(byName = "コメント", defaultValue = "特になし")
        private String comment;

        @Mapping(byName = "メール")
        private String email;

        @Mapping(byName = "名前", required = true)
        private String name;

    }
}

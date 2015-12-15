
package info.naiv.lab.java.jmt.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.TypeMismatchException;

/**
 *
 * @author enlo
 */
public class ObjectAttributesTest {

    private Attributes attrs;
    private TestObject2 to2;

    @Before
    public void beforeTest() {
        to2 = new TestObject2();
        attrs = new ObjectAttributes(to2);
    }

    @Test
    public void testContains() {
        assertThat(attrs.contains("ok"), is(true));
        assertThat(attrs.contains("object"), is(false));
    }

    @Test
    public void testCopyTo() {
        Map<String, Object> actual = new HashMap<>();
        assertThat(attrs.copyTo(actual), is(sameInstance(actual)));
        assertThat(actual.size(), is(4));
        assertThat(actual, hasEntry("ok", (Object) to2.isOk()));
        assertThat(actual, hasEntry("name", (Object) to2.getName()));
        assertThat(actual, hasEntry("number", (Object) to2.getNumber()));
    }

    @Test
    public void testGetAttrbiute_String_Class() {
        assertThat(attrs.getAttribute("ok", Boolean.class), is(to2.isOk()));
        assertThat(attrs.getAttribute("name", String.class), is(to2.getName()));
        assertThat(attrs.getAttribute("number", Integer.class), is((Object) to2.getNumber()));
        assertThat(attrs.getAttribute("object", String.class), is(nullValue()));
    }

    @Test
    public void testGetAttrbute_String() {
        assertThat(attrs.getAttribute("ok"), is((Object) to2.isOk()));
        assertThat(attrs.getAttribute("name"), is((Object) to2.getName()));
        assertThat(attrs.getAttribute("number"), is((Object) to2.getNumber()));
        assertThat(attrs.getAttribute("object"), is(nullValue()));
    }

    @Test
    public void testGetAttributeNames() {
        Set<String> actual = attrs.getAttributeNames();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(4));
        assertThat(actual, hasItems("ok", "name", "number", "class"));
    }

    @Test(expected = TypeMismatchException.class)
    public void testTypemismatch() {
        attrs.getAttribute("ok", int.class);
    }

    static class TestObject1 {

        public String getName() {
            return "TestObject1";
        }

        public boolean isOk() {
            return true;
        }
    }

    static class TestObject2 extends TestObject1 {

        @Override
        public String getName() {
            return "TestObject1";
        }

        public int getNumber() {
            return 12;
        }
    }
}

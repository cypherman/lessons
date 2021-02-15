import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PowerSetTest {

    private PowerSet powerSet;

    @Before
    public void setup() {
        powerSet = new PowerSet();
    }

    @Test
    public void testPut() {
        Assert.assertEquals(0, powerSet.size());

        powerSet.put("0123456789");
        Assert.assertEquals(1, powerSet.size());
        powerSet.put("0123456789");
        Assert.assertEquals(1, powerSet.size());

        powerSet.put("hello");
        Assert.assertEquals(2, powerSet.size());
        powerSet.put("hello");
        Assert.assertEquals(2, powerSet.size());

        powerSet.put(null);
        Assert.assertEquals(2, powerSet.size());
    }

    @Test
    public void testGet() {
        Assert.assertFalse(powerSet.get("hello"));

        powerSet.put("hello");
        Assert.assertTrue(powerSet.get("hello"));

        powerSet.put("0123456789");
        Assert.assertTrue(powerSet.get("0123456789"));

        Assert.assertFalse(powerSet.get(null));
    }

    @Test
    public void testRemove() {
        Assert.assertFalse(powerSet.remove("hello"));
        Assert.assertEquals(0, powerSet.size());

        powerSet.put("hello");
        powerSet.put("world");
        powerSet.put("test");
        powerSet.put("0123456789");
        powerSet.put("deleted");
        powerSet.put("remove me");
        powerSet.put("foo");
        powerSet.put("bar");
        Assert.assertEquals(8, powerSet.size());

        powerSet.remove(null);
        Assert.assertEquals(8, powerSet.size());

        Assert.assertTrue(powerSet.remove("hello"));
        Assert.assertEquals(7, powerSet.size());
        Assert.assertFalse(powerSet.remove("hello"));
        Assert.assertEquals(7, powerSet.size());
        Assert.assertTrue(powerSet.remove("world"));
        Assert.assertEquals(6, powerSet.size());
        Assert.assertTrue(powerSet.remove("test"));
        Assert.assertEquals(5, powerSet.size());
        Assert.assertTrue(powerSet.remove("0123456789"));
        Assert.assertEquals(4, powerSet.size());
        Assert.assertTrue(powerSet.remove("deleted"));
        Assert.assertEquals(3, powerSet.size());
        Assert.assertTrue(powerSet.remove("remove me"));
        Assert.assertEquals(2, powerSet.size());
        Assert.assertTrue(powerSet.remove("foo"));
        Assert.assertEquals(1, powerSet.size());
        Assert.assertTrue(powerSet.remove("bar"));
        Assert.assertEquals(0, powerSet.size());
    }

    @Test
    public void testIntersection() {
        powerSet.put("hello");
        powerSet.put("world");
        powerSet.put("test");
        powerSet.put("0123456789");
        powerSet.put("deleted");
        powerSet.put("remove me");
        powerSet.put("foo");
        powerSet.put("bar");

        PowerSet set = new PowerSet();
        set.put("");
        set.put("hello");
        set.put("world");
        set.put("test");
        set.put("0123456789");
        set.put("not found");
        set.put("me");
        set.put("foobar");

        List<String> expected = Arrays.asList("hello", "world", "test", "0123456789");
        List<String> actual = powerSet.intersection(set).toList();
        Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));

        set.remove("hello");
        set.remove("world");
        set.remove("test");
        set.remove("0123456789");
        Assert.assertNull(powerSet.intersection(set));

        set.remove("");
        set.remove("not found");
        set.remove("me");
        set.remove("foobar");
        Assert.assertNull(powerSet.intersection(set));

        set.put("word");
        powerSet.remove("hello");
        powerSet.remove("world");
        powerSet.remove("test");
        powerSet.remove("0123456789");
        powerSet.remove("deleted");
        powerSet.remove("remove me");
        powerSet.remove("foo");
        powerSet.remove("bar");
        Assert.assertNull(powerSet.intersection(set));
    }

    @Test
    public void testUnion() {
        PowerSet set = new PowerSet();
        Assert.assertNull(powerSet.union(set));

        Assert.assertNull(powerSet.union(null));

        powerSet.put("hello");
        powerSet.put("world");
        powerSet.put("test");
        powerSet.put("0123456789");
        powerSet.put("deleted");
        powerSet.put("remove me");
        powerSet.put("foo");
        powerSet.put("bar");

        List<String> expected = new ArrayList<>();
        expected.addAll(powerSet.toList());
        List<String> actual = powerSet.union(set).toList();
        Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));

        set.put("");
        set.put("hello");
        set.put("world");
        set.put("test");
        set.put("0123456789");
        set.put("not found");
        set.put("me");
        set.put("foobar");

        expected.addAll(set.toList());
        actual = powerSet.union(set).toList();
        Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));

        powerSet.remove("hello");
        powerSet.remove("world");
        powerSet.remove("test");
        powerSet.remove("0123456789");
        powerSet.remove("deleted");
        powerSet.remove("remove me");
        powerSet.remove("foo");
        powerSet.remove("bar");

        expected = set.toList();
        actual = powerSet.union(set).toList();
        Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void testDifference() {
        PowerSet set = new PowerSet();
        Assert.assertNull(powerSet.difference(set));

        Assert.assertNull(powerSet.difference(null));

        powerSet.put("hello");
        powerSet.put("world");
        powerSet.put("test");
        powerSet.put("0123456789");
        powerSet.put("deleted");
        powerSet.put("remove me");
        powerSet.put("foo");
        powerSet.put("bar");

        List<String> expected = new ArrayList<>();
        expected.addAll(powerSet.toList());
        List<String> actual = powerSet.difference(set).toList();
        Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
        actual = powerSet.difference(null).toList();
        Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));

        set.put("");
        set.put("hello");
        set.put("world");
        set.put("test");
        set.put("0123456789");
        set.put("not found");
        set.put("me");
        set.put("foobar");

        expected = Arrays.asList("deleted", "remove me", "foo", "bar");
        actual = powerSet.difference(set).toList();
        Assert.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));

        powerSet.remove("hello");
        powerSet.remove("world");
        powerSet.remove("test");
        powerSet.remove("0123456789");
        powerSet.remove("deleted");
        powerSet.remove("remove me");
        powerSet.remove("foo");
        powerSet.remove("bar");

        Assert.assertNull(powerSet.difference(set));
    }

    @Test
    public void testIsSubset() {
        PowerSet set = new PowerSet();
        Assert.assertFalse(powerSet.isSubset(set));
        Assert.assertFalse(powerSet.isSubset(null));

        powerSet.put("hello");
        powerSet.put("world");
        powerSet.put("test");
        powerSet.put("0123456789");
        powerSet.put("deleted");
        powerSet.put("remove me");
        powerSet.put("foo");
        powerSet.put("bar");

        set.put("");
        set.put("world");
        set.put("test");
        set.put("0123456789");
        Assert.assertFalse(powerSet.isSubset(set));

        set.remove("");
        Assert.assertTrue(powerSet.isSubset(set));

        powerSet.remove("hello");
        powerSet.remove("world");
        powerSet.remove("test");
        powerSet.remove("0123456789");
        powerSet.remove("deleted");
        powerSet.remove("remove me");
        powerSet.remove("foo");
        powerSet.remove("bar");
        Assert.assertFalse(powerSet.isSubset(set));
    }
}
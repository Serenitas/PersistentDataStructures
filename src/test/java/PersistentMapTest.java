import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PersistentMapTest {

    private PersistentMap<Integer, String> persistentMap = null;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void size() {
        persistentMap = new PersistentMap<>();
        assertEquals(0, persistentMap.size());
        int size = 8;
        for (int i = 0; i < size; i++) {
            persistentMap.put(i, "object");
        }
        assertEquals(size, persistentMap.size());
        persistentMap.remove(0);
        assertEquals(size - 1, persistentMap.size());
        assertEquals(size, persistentMap.size(size));
    }

    @Test
    public void sizeWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().size(3);
    }

    @Test
    public void isEmpty() {
        persistentMap = new PersistentMap<>();
        assertEquals(true, persistentMap.isEmpty());
        persistentMap.put(0, "0");
        assertEquals(false, persistentMap.isEmpty());
        assertEquals(true, persistentMap.isEmpty(0));
    }

    @Test
    public void isEmptyWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().isEmpty(3);
    }

    @Test
    public void containsKey() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        assertEquals(true, persistentMap.containsKey(0));
        assertEquals(false, persistentMap.containsKey(1));
        persistentMap.put(1, "1");
        assertEquals(false, persistentMap.containsKey(1, 1));
        assertEquals(true, persistentMap.containsKey(1, 2));
        persistentMap.remove(0);
        assertEquals(false, persistentMap.containsKey(0, 3));
    }

    @Test
    public void containsKeyWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().containsKey(0, 3);
    }

    @Test
    public void containsValue() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        assertEquals(true, persistentMap.containsValue("0"));
        assertEquals(false, persistentMap.containsValue("1"));
        persistentMap.put(1, "1");
        assertEquals(false, persistentMap.containsValue("1", 1));
        assertEquals(true, persistentMap.containsValue("1", 2));
        persistentMap.remove(0);
        assertEquals(false, persistentMap.containsValue("0", 3));
        assertEquals(false, persistentMap.containsValue(null));
        persistentMap.put(0, null);
        assertEquals(true, persistentMap.containsValue(null));
    }

    @Test
    public void containsValueWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().containsValue("0", 3);
    }

    @Test
    public void get() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        assertEquals("0", persistentMap.get(0));
        persistentMap.put(0, "1");
        assertEquals("0", persistentMap.get(0, 1));
        assertEquals("1", persistentMap.get(0, 2));
        assertEquals(null, persistentMap.get(1));
        persistentMap.remove(0);
        assertEquals(null, persistentMap.get(0));
    }

    @Test
    public void getWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().get(0, 3);
    }

    @Test
    public void putAll() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(0, "0");
        hashMap.put(1, "1");
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "-1");
        persistentMap.putAll(hashMap);
        assertEquals("0", persistentMap.get(0));
        assertEquals("1", persistentMap.get(1));
    }

    @Test
    public void clear() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        persistentMap.clear();
        assertEquals(true, persistentMap.isEmpty());
    }

    @Test
    public void keySet() {
        persistentMap = new PersistentMap<>();
        int size = 8;
        for (int i = 0; i < size; i++)
            persistentMap.put(i, "object");
        persistentMap.put(-1, "not_object");
        persistentMap.remove(-1);
        Set set = persistentMap.keySet(size);
        int i = 0;
        for (Object s : set) {
            assertEquals(i, (int) s);
            i++;
        }
    }

    @Test
    public void keySetWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().keySet(3);
    }

    @Test
    public void values() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        persistentMap.put(-1, "-1");
        persistentMap.put(1, "1");
        persistentMap.remove(-1);
        Collection c = persistentMap.values();
        assertEquals("0", c.toArray()[0]);
        assertEquals("1", c.toArray()[1]);
        assertEquals(2, c.size());
        c = persistentMap.values(3);
        assertEquals(true, c.contains("-1"));
    }

    @Test
    public void valuesWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().values(3);
    }

    @Test
    public void entrySet() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        persistentMap.put(-1, "-1");
        persistentMap.put(1, "1");
        persistentMap.remove(-1);
        Set<Map.Entry> entrySet = persistentMap.entrySet();
        assertEquals(2, entrySet.size());
    }

    @Test
    public void entrySetWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().entrySet(3);
    }

    @Test
    public void getOrDefault() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        assertEquals("0", persistentMap.getOrDefault(0, "1"));
        assertEquals("1", persistentMap.getOrDefault(1, "1"));
    }

    @Test
    public void getOrDefaultWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentMap<>().getOrDefault(3, 2, 3);
    }

    @Test
    public void replaceAll() {
        PersistentMap<Double, Double> pmap = new PersistentMap<>();
        pmap.put(2.0, 2.0);
        pmap.put(3.0, 2.0);
        pmap.put(4.0, 3.0);
        pmap.replaceAll((k, v) -> Math.pow((double) k, (double) v));
        assertEquals(4.0, (double) pmap.get(2.0), 1e-7);
        assertEquals(9.0, (double) pmap.get(3.0), 1e-7);
        assertEquals(64.0, (double) pmap.get(4.0), 1e-7);
    }

    @Test
    public void removeKeyValue() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        persistentMap.put(1, "1");
        assertEquals(true, persistentMap.remove(0, "0"));
        assertEquals(false, persistentMap.remove(1, "0"));
    }

    @Test
    public void replace() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        assertEquals("0", persistentMap.replace(0, "2"));
        assertEquals(null, persistentMap.replace(1, "2"));
    }

    @Test
    public void replaceKeyValue() {
        persistentMap = new PersistentMap<>();
        persistentMap.put(0, "0");
        persistentMap.put(1, "1");
        assertEquals(true, persistentMap.replace(0, "0", "2"));
        assertEquals(false, persistentMap.replace(1, "0", "2"));
    }
}
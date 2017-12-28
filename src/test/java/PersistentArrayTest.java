import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class PersistentArrayTest {
    private PersistentArray<Integer> array = null;
    private int initialCapacity = 10;

    @Before
    public void setUp() throws Exception {
        array = new PersistentArray<>(5);
        array.set(3, 40);
        array.set(3, 42);
        array.set(3, 36);
        array.add(1);
        array.remove();
        array.remove();
        PersistentArray<String> arrayWithDefaultSize = new PersistentArray<>();
        arrayWithDefaultSize.add("Hello World!");
        PersistentArray<Object> arrayWithSizeOne = new PersistentArray<>(1);
        arrayWithSizeOne.remove();
        boolean checkRemoveNothing = false;
    }

    @Test
    public void checkInitialCapacity() {
        array = new PersistentArray<>();
        assertEquals(array.getLength(), initialCapacity);
    }

    @Test
    public void getVersioned() throws Exception {
        array = new PersistentArray<>(5);
        array.set(3, 40);
        array.set(3, 42);
        array.set(3, 36);
        assertEquals(array.get(3, 0), null);
        assertEquals((long) array.get(3, 1), (long) 40);
        assertEquals((long) array.get(3, 2), (long) 42);
        assertEquals((long) array.get(3, 3), (long) 36);
    }

    @Test
    public void get() throws Exception {
        int obj = 6;
        array = new PersistentArray<>();
        array.add(obj);
        assertEquals((int) array.get(PersistentArray.INIT_CAPACITY), (int) obj);
    }

    @Test
    public void set() throws Exception {

    }

    @Test
    public void getLength() throws Exception {

    }

    @Test
    public void getLength1() throws Exception {

    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

}
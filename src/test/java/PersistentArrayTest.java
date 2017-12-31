import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;


public class PersistentArrayTest {
    private PersistentArray<Integer> array = null;
    private int initialCapacity = 10;

    @Rule
    public ExpectedException ex = ExpectedException.none();

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
        array = new PersistentArray<>();
        assertEquals(array.set(0, 1), 1);
        assertEquals(array.set(1, 2), 2);
        ex.expect(ArrayIndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.ARRAY_INDEX_OUT_OF_BOUNDS);
        array.set(initialCapacity + 5, 1);
    }

    @Test
    public void getLength() throws Exception {
        array = new PersistentArray<>();
        assertEquals(array.getLength(), initialCapacity);
        array.add(1);
        assertEquals(array.getLength(), initialCapacity + 1);
    }

    @Test
    public void getLengthVersioned() throws Exception {
        array = new PersistentArray<>();
        assertEquals(array.getLength(0), initialCapacity);
        array.add(1);
        assertEquals(array.getLength(1), initialCapacity + 1);
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        array.getLength(10);
    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

}
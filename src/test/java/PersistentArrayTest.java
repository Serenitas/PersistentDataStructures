import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


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
        Assert.assertEquals(array.getLength(), initialCapacity);
    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void get1() throws Exception {

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
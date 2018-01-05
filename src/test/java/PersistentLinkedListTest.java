import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class PersistentLinkedListTest {

    private PersistentLinkedList<Integer> persistentLinkedList = null;
    private int setSize = 26;
    private HashSet<Integer> set = new HashSet<Integer>();

    {
        for (int i = 0; i < setSize; i++)
            set.add(i);
    }

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void size() {
        persistentLinkedList = new PersistentLinkedList<>();
        assertEquals(0, persistentLinkedList.size());
        persistentLinkedList = new PersistentLinkedList<>(set);
        assertEquals(setSize, persistentLinkedList.size());
        assertEquals(persistentLinkedList.size(0), persistentLinkedList.size());
        persistentLinkedList.remove(0);
        assertEquals(setSize - 1, persistentLinkedList.size());
        assertEquals(setSize - 1, persistentLinkedList.size(1));
    }

    @Test
    public void sizeWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentLinkedList<>().size(3);
    }

    @Test
    public void isEmpty() {
        persistentLinkedList = new PersistentLinkedList<>();
        assertEquals(true, persistentLinkedList.isEmpty());
        persistentLinkedList.add(1);
        assertEquals(false, persistentLinkedList.isEmpty());
        assertEquals(true, persistentLinkedList.isEmpty(0));
        persistentLinkedList.remove(0);
        assertEquals(true, persistentLinkedList.isEmpty());
        assertEquals(true, persistentLinkedList.isEmpty(0));
        assertEquals(false, persistentLinkedList.isEmpty(1));
    }

    @Test
    public void isEmptyWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentLinkedList<>().size(-1);
    }

    @Test
    public void contains() {
        persistentLinkedList = new PersistentLinkedList<>();
        assertEquals(false, persistentLinkedList.contains(1));
        persistentLinkedList.add(1);
        assertEquals(true, persistentLinkedList.contains(1));
        persistentLinkedList.remove((Integer) 1);
        assertEquals(false, persistentLinkedList.contains(1));
        assertEquals(true, persistentLinkedList.contains(1, 1));
        persistentLinkedList.add(null);
        assertEquals(true, persistentLinkedList.contains(null));
        assertEquals(false, persistentLinkedList.contains("Rose"));
    }

    @Test
    public void containsWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentLinkedList<>().contains(1, -1);
    }

    @Test
    public void iterator() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        int i = 0;
        for (Object obj : persistentLinkedList) {
            assertEquals(i, obj);
            i++;
        }
    }

    @Test
    public void iteratorWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentLinkedList<>().iterator(-1);
    }

    @Test
    public void toArray() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        Object[] arr = persistentLinkedList.toArray();
        int i = 0;
        for (Object o : set) {
            assertEquals(arr[i], o);
            i++;
        }
    }

    @Test
    public void toArrayWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentLinkedList<>().toArray(-1);
    }

    @Test
    public void add() {
    }

    @Test
    public void remove() {
        persistentLinkedList = new PersistentLinkedList<>();
        assertEquals(false, persistentLinkedList.remove("Nothing"));
        persistentLinkedList.add("object");
        persistentLinkedList.add("object2");
        persistentLinkedList.add("object3");
        persistentLinkedList.add("object");
        persistentLinkedList.remove("object2");
        assertEquals("object3", persistentLinkedList.get(1));
        persistentLinkedList.remove("object");
        assertEquals(true, persistentLinkedList.contains("object"));
        assertEquals(false, persistentLinkedList.remove("Nothing"));
        persistentLinkedList.remove("object");
        persistentLinkedList.remove("object3");
        assertEquals(true, persistentLinkedList.isEmpty());
    }

    @Test
    public void addAll() {
        persistentLinkedList = new PersistentLinkedList<>();
        persistentLinkedList.add("object");
        persistentLinkedList.add("other object");
        assertEquals(false, persistentLinkedList.addAll(new LinkedList()));
        persistentLinkedList.addAll(set);
        for (Object o : set)
            assertEquals(true, persistentLinkedList.contains(o));
    }

    @Test
    public void addAllWrongIndex() {
        ex.expect(IndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        new PersistentLinkedList().addAll(26, set);
    }

    @Test
    public void retainAll() {
        persistentLinkedList = new PersistentLinkedList<>();
        persistentLinkedList.add("object");
        persistentLinkedList.addAll(set);
        persistentLinkedList.retainAll(set);
        assertEquals(setSize, persistentLinkedList.size());
    }

    @Test
    public void removeAll() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.removeAll(set);
        assertEquals(true, persistentLinkedList.isEmpty());
    }

    @Test
    public void containsAll() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        assertEquals(true, persistentLinkedList.containsAll(set));
        persistentLinkedList.remove(0);
        assertEquals(false, persistentLinkedList.containsAll(set));
    }

    @Test
    public void containsAll1() {
    }

    @Test
    public void toArray2() {
    }

    @Test
    public void replaceAll() {
    }

    @Test
    public void sort() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void get() {
    }

    @Test
    public void get1() {
    }

    @Test
    public void set() {
    }

    @Test
    public void add1() {
    }

    @Test
    public void remove1() {
    }

    @Test
    public void indexOf() {
    }

    @Test
    public void indexOf1() {
    }

    @Test
    public void lastIndexOf() {
    }

    @Test
    public void lastIndexOf1() {
    }

    @Test
    public void versionedListIterator() {
    }

    @Test
    public void versionedListIterator1() {
    }

    @Test
    public void listIterator() {
    }

    @Test
    public void listIterator1() {
    }

    @Test
    public void subList() {
    }

    @Test
    public void subList1() {
    }

    @Test
    public void spliterator() {
    }
}
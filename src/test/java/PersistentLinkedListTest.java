import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

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
        new PersistentLinkedList<>().isEmpty(-1);
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
        persistentLinkedList = new PersistentLinkedList<>();
        persistentLinkedList.add(0, 2);
        persistentLinkedList.add(0, 0);
        persistentLinkedList.add(1, 1);
        int i = 0;
        for (Object o : persistentLinkedList) {
            assertEquals(i, o);
            i++;
        }
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
        assertEquals(false, persistentLinkedList.retainAll(set));
        assertEquals(false, persistentLinkedList.retainAll(new LinkedList()));
        assertEquals(false, new PersistentLinkedList().retainAll(set));
    }

    @Test
    public void removeAll() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        assertEquals(false, persistentLinkedList.removeAll(new LinkedList()));
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
    public void containsAllWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        new PersistentLinkedList<>(set).containsAll(set, -1);
    }

    @Test
    public void replaceAll() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.replaceAll(o -> (int) o * (int) o);
        int i = 0;
        for (Integer o : set) {
            assertEquals(o * o, persistentLinkedList.get(i));
            i++;
        }
    }

    @Test
    public void sort() {
        ex.expect(UnsupportedOperationException.class);
        new PersistentLinkedList().sort(null);
    }

    @Test
    public void clear() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.clear();
        assertEquals(true, persistentLinkedList.isEmpty());
    }

    @Test
    public void getWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.get(0, 6);
    }

    @Test
    public void getWrongIndex() {
        ex.expect(IndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.get(setSize + 1, 0);
    }

    @Test
    public void setWrongIndex() {
        ex.expect(IndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.set(setSize + 1, 0);
    }

    @Test
    public void addWrongIndex() {
        ex.expect(IndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.add(setSize + 5, 0);
    }

    @Test
    public void removeIndex() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        persistentLinkedList.remove(setSize - 1);
        ex.expect(IndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        persistentLinkedList.remove(setSize + 1);
    }

    @Test
    public void indexOf() {
        persistentLinkedList = new PersistentLinkedList<>();
        assertEquals(-1, persistentLinkedList.indexOf("object"));
        persistentLinkedList.add("object1");
        persistentLinkedList.add("object");
        assertEquals(1, persistentLinkedList.indexOf("object"));
    }

    @Test
    public void indexOfWrongVersion() {
        persistentLinkedList = new PersistentLinkedList<>();
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        persistentLinkedList.indexOf("object", 26);
    }

    @Test
    public void lastIndexOf() {
        persistentLinkedList = new PersistentLinkedList<>();
        assertEquals(-1, persistentLinkedList.lastIndexOf("object"));
        persistentLinkedList.add("object");
        persistentLinkedList.add("object");
        assertEquals(1, persistentLinkedList.lastIndexOf("object"));
    }

    @Test
    public void lastIndexOfWrongVersion() {
        persistentLinkedList = new PersistentLinkedList<>();
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        persistentLinkedList.lastIndexOf("object", 26);
    }

    @Test
    public void versionedListIterator() {
        persistentLinkedList = new PersistentLinkedList<>();
        persistentLinkedList.addAll(set);
        ListIterator li = persistentLinkedList.versionedListIterator(1, 1);
        assertEquals(true, li.hasNext());
        assertEquals(set.toArray()[2], li.next());
    }

    @Test
    public void versionedListIteratorWrongIndex() {
        ex.expect(IndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        persistentLinkedList = new PersistentLinkedList<>();
        persistentLinkedList.addAll(set);
        ListIterator li = persistentLinkedList.versionedListIterator(1, setSize + 1);
    }

    @Test
    public void versionedListIteratorWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        persistentLinkedList = new PersistentLinkedList<>();
        persistentLinkedList.addAll(set);
        ListIterator li = persistentLinkedList.versionedListIterator(10, 1);
    }

    @Test
    public void listIterator() {
        persistentLinkedList = new PersistentLinkedList<>();
        persistentLinkedList.addAll(set);
        ListIterator li = persistentLinkedList.listIterator(1);
        assertEquals(true, li.hasNext());
        assertEquals(set.toArray()[2], li.next());
        li = persistentLinkedList.listIterator();
        assertEquals(true, li.hasNext());
        assertEquals(set.toArray()[0], li.next());
    }

    @Test
    public void subList() {
        persistentLinkedList = new PersistentLinkedList<>(set);
        List l = persistentLinkedList.subList(0, setSize - 1);
        assertEquals(l.get(0), persistentLinkedList.get(0));
    }

    @Test
    public void subListWrongIndex() {
        ex.expect(IndexOutOfBoundsException.class);
        ex.expectMessage(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        persistentLinkedList = new PersistentLinkedList<>(set);
        List l = persistentLinkedList.subList(2, 1);
    }

    @Test
    public void subListWrongVersion() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage(Exceptions.NO_SUCH_VERSION);
        persistentLinkedList = new PersistentLinkedList<>(set);
        List l = persistentLinkedList.subList(1, setSize - 1, 10);
    }

    @Test
    public void spliterator() {
        ex.expect(UnsupportedOperationException.class);
        new PersistentLinkedList<>().spliterator();
    }

    @Test
    public void toArray1() {
        ex.expect(UnsupportedOperationException.class);
        new PersistentLinkedList<>().toArray(new Object[5]);
    }
}
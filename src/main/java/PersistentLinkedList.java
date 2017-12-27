import java.util.*;
import java.util.function.UnaryOperator;

public class PersistentLinkedList<E> implements List {
    private int currentVersion = 0;
    private TreeMap<Integer, Integer> versionsLengths;
    private TreeMap<Integer, PersistentListNode<E>> versionedHeads;
    private TreeMap<Integer, PersistentListNode<E>> versionedTails;

    public PersistentLinkedList() {
        versionedHeads = new TreeMap<>();
        versionedTails = new TreeMap<>();
        versionsLengths = new TreeMap<>();
        versionsLengths.put(0, 0);
    }

    public PersistentLinkedList(Collection<E> c) {
        versionedHeads = new TreeMap<>();
        versionedTails = new TreeMap<>();
        versionsLengths = new TreeMap<>();
        versionsLengths.put(0, c.size());

        for (E obj: c) {
            add(obj, 0);
        }
    }

    public int size(int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.floorEntry(version).getValue();
    }

    @Override
    public int size() {
        return size(currentVersion);
    }

    public boolean isEmpty(int version) { return size(version) == 0; }

    @Override
    public boolean isEmpty() {
        return isEmpty(currentVersion);
    }

    public boolean contains(Object o, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (isEmpty(version))
            return false;
        PersistentListNode<E> current = versionedHeads.floorEntry(currentVersion).getValue();
        int size = versionsLengths.floorEntry(currentVersion).getValue();
        for (int i = 0; i < size; i++) {
            if (current.getObject().equals(o))
                return true;
            current = current.getNext();
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return contains(o, currentVersion);
    }

    //TODO iterator
    @Override
    public Iterator iterator() {
        return null;
    }

    public Object[] toArray(int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        Object[] array = new Object[versionsLengths.floorEntry(version).getValue()];
        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int i = 0; i < array.length; i++) {
            array[i] = current.getObject();
            current = current.getNext();
        }
        return array;
    }

    @Override
    public Object[] toArray() {
        return toArray(currentVersion);
    }

    private boolean add(Object o, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (versionedHeads.size() == 0 || versionedHeads.floorEntry(version).getValue() == null) {
            PersistentListNode<E> current = new PersistentListNode<>((E) o, version, null, null);
            versionedHeads.put(version, current);
            versionedTails.put(version, current);
            versionsLengths.put(version, 1);
        } else {
            PersistentListNode<E> current = new PersistentListNode<>((E) o, version, null, versionedTails.floorEntry(version).getValue());
            versionedTails.put(version, current);
            versionsLengths.put(version, versionsLengths.floorEntry(version).getValue() + 1);
        }
        return true;
    }

    @Override
    public boolean add(Object o) {
        currentVersion++;
        return add(o, currentVersion);
    }

    // TODO remove
    private boolean remove(Object o, int version) {
        if (isEmpty(version)) {
            return false;
        }
        PersistentListNode<E> current = versionedHeads.floorEntry(currentVersion).getValue();
        int size = versionsLengths.floorEntry(currentVersion).getValue();
        for (int i = 0; i < size; i++) {
            if (current.getObject().equals(o)) {
                if (i == 0) {
                    //current.getNext().setPrev(null);
                    versionedHeads.put(currentVersion, current.getNext());
                } else if (i == size - 1) {

                } else {

                }
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
    // TODO
    @Override
    public boolean remove(Object o) {
        currentVersion++;
        return remove(o, currentVersion);
    }

    @Override
    public boolean addAll(Collection c) {
        currentVersion++;
        for (Object o : c) {
            add(o, currentVersion);
        }
        return true;
    }

    // TODO check
    @Override
    public boolean addAll(int index, Collection c) {
        currentVersion++;
        for (Object o : c) {
            add(index, o, currentVersion);
            index++;
        }
        return true;
    }
    // TODO check
    @Override
    public boolean retainAll(Collection c) {
        //TODO check empty
        PersistentListNode<E> current = versionedHeads.get(currentVersion);
        int size = versionsLengths.get(currentVersion);
        currentVersion++;
        for (int i = 0; i < size; i++) {
            if (!c.contains(current.getObject())) {
                remove(current, currentVersion);
            }
        }
        return true;
    }
    // TODO check
    @Override
    public boolean removeAll(Collection c) {
        currentVersion++;
        for (Object o : c) {
            remove(o, currentVersion);
        }
        return true;
    }

    public boolean containsAll(Collection c, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        for (Object o : c) {
            if (!contains(o, version))
                return false;
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        return containsAll(c, currentVersion);
    }

    //TODO понять чо тут вообще хотят
    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
    // TODO
    @Override
    public void replaceAll(UnaryOperator operator) {

    }
    // TODO
    @Override
    public void sort(Comparator c) {

    }
    // TODO
    @Override
    public void clear() {

    }

    public Object get(int index, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        int size = versionsLengths.floorEntry(version).getValue();
        if (index >= size)
            throw new ArrayIndexOutOfBoundsException(Exceptions.INDEX_OUT_OF_BOUNDS);

        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int i = 0; i < size; i++) {
            if (i == index)
                return current.getObject();
            current = current.getNext();
        }
        return null;
    }

    @Override
    public Object get(int index) {
        return get(index, currentVersion);
    }
    // TODO
    @Override
    public Object set(int index, Object element) {
        return null;
    }
    // TODO
    private void add(int index, Object element, int version) {

    }
    // TODO
    @Override
    public void add(int index, Object element) {

    }
    // TODO
    @Override
    public Object remove(int index) {
        return null;
    }

    public int indexOf(Object o, int version) {
        int result = -1;
        int size = versionsLengths.floorEntry(version).getValue();
        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int ind = 0; ind < size; ind++) {
            if (current.getObject().equals(o)) {
                result = ind;
                break;
            }
            current = current.getNext();
        }
        return result;
    }

    @Override
    public int indexOf(Object o) {
        return indexOf(o, currentVersion);
    }

    public int lastIndexOf(Object o, int version) {
        int result = -1;
        int size = versionsLengths.floorEntry(version).getValue();
        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int ind = 0; ind < size; ind++) {
            if (current.getObject().equals(o)) {
                result = ind;
            }
            current = current.getNext();
        }
        return result;
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOf(o, currentVersion);
    }
    // TODO
    @Override
    public ListIterator listIterator() {
        return null;
    }
    // TODO
    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    public List subList(int fromIndex, int toIndex, int version) {
        int size = versionsLengths.floorEntry(version).getValue();
        if (fromIndex < 0 || toIndex >= size) {
            // TODO add message to exception
            throw new IndexOutOfBoundsException();
        }

        List<E> result = new ArrayList<E>();
        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int i = 0; i < size; i++) {
            result.add(current.getObject());
            current = current.getNext();
        }
        return result;
    }
    @Override
    public List subList(int fromIndex, int toIndex) {
        return subList(fromIndex, toIndex, currentVersion);
    }
    // TODO
    @Override
    public Spliterator spliterator() {
        return null;
    }
}
import java.util.*;
import java.util.function.UnaryOperator;

public class PersistentLinkedList<E> implements List {
    private int currentVersion = 0;
    private HashMap<Integer, Integer> versionsLengths;
    private HashMap<Integer, PersistentListNode<E>> versionedHeads;
    private HashMap<Integer, PersistentListNode<E>> versionedTails;
    //PersistentListNode<E> head = null;
    //PersistentListNode<E> tail = null;

    public PersistentLinkedList() {
        //versionedData = new ArrayList<>();
        versionedHeads = new HashMap<>();
        versionedTails = new HashMap<>();
        versionsLengths = new HashMap<>();
        versionsLengths.put(0, 0);
    }

    public PersistentLinkedList(Collection<E> c) {
        //versionedData = new ArrayList<>();
        versionedHeads = new HashMap<>();
        versionedTails = new HashMap<>();
        versionsLengths = new HashMap<>();
        //TODO конструктор от коллекции
    }


    public int size(int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.get(version);
    }

    @Override
    public int size() {
        return size(currentVersion);
    }

    public boolean isEmpty(int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.get(version) == 0;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(currentVersion);
    }

    public boolean contains(Object o, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (isEmpty(version))
            return false;
        PersistentListNode<E> current = versionedHeads.get(currentVersion);
        int size = versionsLengths.get(currentVersion);
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

    @Override
    public Iterator iterator() {
        //TODO iterator
        return null;
    }

    public Object[] toArray(int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        Object[] array = new Object[versionsLengths.get(version)];
        PersistentListNode current = versionedHeads.get(version);
        for (int i = 0; i < array.length; i++) {
            array[i] = current;
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
        if (versionedHeads.size() == 0 || versionedHeads.get(version) == null) {
            PersistentListNode<E> current = new PersistentListNode<>((E) o, version, null, null);
            versionedHeads.put(version, current);
            versionedTails.put(version, current);
            versionsLengths.put(version, 1);
        } else {
            PersistentListNode<E> current = new PersistentListNode<>((E) o, version, versionedTails.get(version), null);
            versionedHeads.put(version, current);
            versionedTails.put(version, current);
            versionsLengths.put(version, versionsLengths.get(version) + 1);
        }
        return true;
    }

    @Override
    public boolean add(Object o) {
        if (versionedHeads.size() == 0 || versionedHeads.get(currentVersion) == null) {
            currentVersion++;
            PersistentListNode<E> current = new PersistentListNode<>((E) o, currentVersion - 1, null, null);
            versionedHeads.put(currentVersion, current);
            versionedTails.put(currentVersion, current);
            versionsLengths.put(currentVersion, 1);
        } else {
            PersistentListNode<E> current = new PersistentListNode<>((E) o, currentVersion + 1, versionedTails.get(currentVersion), null);
            versionedHeads.put(currentVersion, current);
            versionedTails.put(currentVersion, current);
            versionsLengths.put(currentVersion, versionsLengths.get(currentVersion) + 1);
        }
        return true;
    }

    private boolean remove(Object o, int version) {
        if (isEmpty(version)) {
            return false;
        }
        PersistentListNode<E> current = versionedHeads.get(currentVersion);
        int size = versionsLengths.get(currentVersion);
        for (int i = 0; i < size; i++) {
            if (current.getObject().equals(o)) {
                currentVersion++;
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

    @Override
    public boolean remove(Object o) {
        remove(o, currentVersion);
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        currentVersion++;
        for (Object o : c) {
            add(o, currentVersion);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        currentVersion++;
        for (Object o : c) {
            add(index, o, currentVersion);
            index++;
        }
        return true;
    }

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
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public Object[] toArray(Object[] a) {
        //TODO понять чо тут вообще хотят
        return new Object[0];
    }

    @Override
    public void replaceAll(UnaryOperator operator) {

    }

    @Override
    public void sort(Comparator c) {

    }

    @Override
    public void clear() {

    }

    public Object get(int index, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        PersistentListNode<E> current = versionedHeads.get(version);
        int size = versionsLengths.get(version);
        if (index >= size)
            throw new ArrayIndexOutOfBoundsException(Exceptions.INDEX_OUT_OF_BOUNDS);
        for (int i = 0; i < size; i++) {
            if (i < index)
                current = current.getNext();
            else {

            }
        }
        return null;
    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    private void add(int index, Object element, int version) {

    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Spliterator spliterator() {
        return null;
    }
}

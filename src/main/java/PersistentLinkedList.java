import java.util.*;
import java.util.function.Consumer;
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

        for (E obj : c) {
            add(obj, 0);
        }
    }

    public int size(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.floorEntry(version).getValue();
    }

    @Override
    public int size() {
        return size(currentVersion);
    }

    public boolean isEmpty(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return size(version) == 0;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(currentVersion);
    }

    //TODO check
    public boolean contains(Object o, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (isEmpty(version))
            return false;
        PersistentListNode<E> current = versionedHeads.floorEntry(currentVersion).getValue();
        int size = versionsLengths.floorEntry(currentVersion).getValue();
        for (int i = 0; i < size; i++) {
            if (current.getObject() == null)
                if (o == null) return true;
                else if (current.getObject().equals(o))
                    return true;
            current = current.getNext();
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return contains(o, currentVersion);
    }

    //TODO check
    public Iterator iterator(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return new Iterator() {
            ListIterator listIterator = versionedListIterator(version);

            @Override
            public boolean hasNext() {
                return listIterator.hasNext();
            }

            @Override
            public Object next() {
                return listIterator.next();
            }
        };
    }

    @Override
    public Iterator iterator() {
        return iterator(currentVersion);
    }

    public Object[] toArray(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        Object[] array = new Object[size(version)];
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
        if (version < 0 || version > currentVersion)
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
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (isEmpty(version))
            return false;
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

    // TODO check
    @Override
    public boolean addAll(int index, Collection c) {
        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        if (c.isEmpty())
            return false;
        currentVersion++;
        for (Object o : c) {
            add(index, o, currentVersion);
            index++;
        }
        return true;
    }

    //TODO check
    @Override
    public boolean addAll(Collection c) {
        return addAll(0, c);
    }

    // TODO check
    @Override
    public boolean retainAll(Collection c) {
        if (c.isEmpty() || isEmpty())
            return false;
        int size = size();
        PersistentListNode<E> current = versionedHeads.get(currentVersion);
        boolean isChanged = false;
        currentVersion++;
        for (int i = 0; i < size; i++) {
            if (!c.contains(current.getObject())) {
                isChanged = isChanged || remove(current, currentVersion);
            }
        }
        return isChanged;
    }

    // TODO check
    @Override
    public boolean removeAll(Collection c) {
        currentVersion++;
        boolean isChanged = false;
        for (Object o : c) {
            isChanged = isChanged || remove(o, currentVersion);
        }
        return isChanged;
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

    // TODO check
    @Override
    public void replaceAll(UnaryOperator operator) {
        int size = size();
        PersistentListNode currElement = versionedHeads.floorEntry(currentVersion).getValue();
        currentVersion++;
        for (int i = 0; i < size; i++) {
            set(i, operator.apply(currElement.getObject()), currentVersion); //
        }
    }

    // TODO
    @Override
    public void sort(Comparator c) {

    }

    // TODO check
    @Override
    public void clear() {
        currentVersion++;
        versionsLengths.put(currentVersion, 0);
        versionedHeads.put(currentVersion, null);
        versionedTails.put(currentVersion, null);
    }

    public Object get(int index, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);

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

    private Object set(int index, Object element, int version) {
        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);

        return null;
    }

    // TODO check
    @Override
    public Object set(int index, Object element) {
        return set(index, element, currentVersion++);
    }

    // TODO
    private void add(int index, Object element, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        int size = size(version);
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);




    }

    // TODO check
    @Override
    public void add(int index, Object element) {
        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        currentVersion++;
        add(index, element, currentVersion);
    }

    // TODO
    @Override
    public Object remove(int index) {
        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);




        return null;
    }

    public int indexOf(Object o, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
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
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
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

    //TODO check
    public ListIterator versionedListIterator(int version, int index) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        int size = size(version);
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        return new ListIterator() {
            int currIndex = index;
            int _version = version;
            PersistentListNode currElement = versionedHeads.get(_version);

            {
                for (int i = 0; i < size; i++) {
                    if (i == index)
                        break;
                    currElement = currElement.getNext();
                }
            }


            @Override
            public boolean hasNext() {
                return currElement.getNext() != null;
            }

            @Override
            public Object next() {
                if (hasNext()) {
                    currElement = currElement.getNext();
                    currIndex++;
                    return currElement.getObject();
                } else
                    throw new NoSuchElementException(Exceptions.NO_SUCH_ELEMENT);
            }

            @Override
            public boolean hasPrevious() {
                return currElement.getPrev() != null;
            }

            @Override
            public Object previous() {
                if (hasPrevious()) {
                    currElement = currElement.getPrev();
                    currIndex--;
                    return currElement.getObject();
                } else
                    throw new NoSuchElementException(Exceptions.NO_SUCH_ELEMENT);
            }

            @Override
            public int nextIndex() {
                return currIndex + 1;
            }

            @Override
            public int previousIndex() {
                return currIndex - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(Object o) {
                throw new UnsupportedOperationException();
            }
        };
    }

    //TODO check
    public ListIterator versionedListIterator(int version) {
        return versionedListIterator(version, 0);
    }

    //TODO check
    @Override
    public ListIterator listIterator() {
        return versionedListIterator(currentVersion, 0);
    }

    //TODO check
    @Override
    public ListIterator listIterator(int index) {
        return versionedListIterator(currentVersion, index);
    }

    public List subList(int fromIndex, int toIndex, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        int size = size(version);
        if (fromIndex < 0 || toIndex >= size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
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
        return new Spliterator() {
            @Override
            public boolean tryAdvance(Consumer action) {
                return false;
            }

            @Override
            public Spliterator trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };
    }
}
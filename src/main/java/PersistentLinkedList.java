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

    public boolean contains(Object o, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (isEmpty(version))
            return false;

        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int i = 0; i < size(version); i++) {
            if (current.getObject(version) == null) {
                if (o == null) return true;
            } else {
                if (current.getObject(version).equals(o))
                    return true;
            }
            current = current.getNext(version);
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return contains(o, currentVersion);
    }

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
            array[i] = current.getObject(version);
            current = current.getNext(version);
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
            PersistentListNode<E> prev = versionedTails.floorEntry(version).getValue();
            PersistentListNode<E> current = new PersistentListNode<>((E) o, version, prev, null);
            prev.setNext(version, current);

            versionedTails.put(version, current);
            versionsLengths.put(version, size(version) + 1);
        }
        return true;
    }

    @Override
    public boolean add(Object o) {
        currentVersion++;
        return add(o, currentVersion);
    }

    private boolean remove(Object o, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (isEmpty(version))
            return false;
        PersistentListNode<E> current = versionedHeads.floorEntry(currentVersion).getValue();
        for (int i = 0; i < size(version); i++) {
            if (current.getObject(version).equals(o)) {
                PersistentListNode<E> prevEl = current.getPrev(version);
                PersistentListNode<E> nextEl = current.getNext(version);
                if (prevEl != null) {
                    prevEl.setNext(version, nextEl);
                } else {
                    versionedHeads.put(version, nextEl);
                }
                if (nextEl != null) {
                    nextEl.setNext(version, prevEl);
                } else {
                    versionedTails.put(version, prevEl);
                }
                versionsLengths.put(version, size(version) - 1);
                return true;
            }
            current = current.getNext(version);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        currentVersion++;
        return remove(o, currentVersion);
    }

    // TODO O(N*M) -> O(N + M)
    @Override
    public boolean addAll(int index, Collection c) {
        if (index < 0 || index > size())
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

    @Override
    public boolean addAll(Collection c) {
        return addAll(size(), c);
    }

    @Override
    public boolean retainAll(Collection c) {
        if (c.isEmpty() || isEmpty())
            return false;
        PersistentListNode<E> current = versionedHeads.floorEntry(currentVersion).getValue();
        boolean isChanged = false;
        for (int i = 0; i < size(); i++) {
            if (!c.contains(current.getObject(currentVersion + 1))) {
                isChanged = isChanged || remove(current, currentVersion + 1);
                current = current.getNext(currentVersion + 1);
            }
        }
        if (isChanged) { currentVersion++; }
        return isChanged;
    }

    // TODO O(N*M) -> O(N + M)
    @Override
    public boolean removeAll(Collection c) {
        boolean isChanged = false;
        for (Object o : c) {
            while (remove(o, currentVersion + 1)) {
                isChanged = true;
            }
        }
        if (isChanged) { currentVersion++; }
        return isChanged;
    }

    public boolean containsAll(Collection c, int version) {
        if (version < 0 || version > currentVersion)
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

    @Override
    public void replaceAll(UnaryOperator operator) {
        PersistentListNode currElement = versionedHeads.floorEntry(currentVersion).getValue();
        currentVersion++;
        for (int i = 0; i < size(); i++) {
            set(i, operator.apply(currElement.getObject(currentVersion)), currentVersion);
            currElement = currElement.getNext(currentVersion);
        }
    }

    // TODO
    @Override
    public void sort(Comparator c) {

    }

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
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);

        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int i = 0; i < size(); i++) {
            if (i == index)
                return current.getObject(version);
            current = current.getNext(version);
        }
        return null;
    }

    @Override
    public Object get(int index) {
        return get(index, currentVersion);
    }

    private Object set(int index, Object element, int version) {
        if (index < 0 || index >= size(version))
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);

        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int i = 0; i < index; i++) {
            current = current.getNext(version);
        }
        Object prevObj = current.getObject(version);
        current.setObject(version, (E) element);

        return prevObj;
    }

    @Override
    public Object set(int index, Object element) {
        currentVersion++;
        return set(index, element, currentVersion);
    }

    private void add(int index, Object element, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (index < 0 || index > size(version))
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);

        if (index == size(version)) {
            add(element, version);
            return;
        }

        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int i = 0; i < index; i++) {
            current = current.getNext(version);
        } // after that current is element needed to shift

        PersistentListNode<E> prev = current.getPrev(version);
        PersistentListNode<E> newEl = new PersistentListNode<>((E)element, version, prev, current);
        if (null != prev) {
            prev.setNext(version, newEl);
        } else {
            versionedHeads.put(version, newEl);
        }
        current.setPrev(version, newEl);
        versionsLengths.put(version, size(version) + 1);
    }

    @Override
    public void add(int index, Object element) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        currentVersion++;
        add(index, element, currentVersion);
    }

    @Override
    public Object remove(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);

        PersistentListNode<E> current = versionedHeads.floorEntry(currentVersion).getValue();
        for (int i = 0; i < index; i++) {
            current = current.getNext(currentVersion);
        }

        PersistentListNode<E> prevEl = current.getPrev(currentVersion);
        PersistentListNode<E> nextEl = current.getNext(currentVersion);
        currentVersion++;
        if (prevEl != null) {
            prevEl.setNext(currentVersion, nextEl);
        } else {
            versionedHeads.put(currentVersion, nextEl);
        }
        if (nextEl != null) {
            nextEl.setNext(currentVersion, prevEl);
        } else {
            versionedTails.put(currentVersion, prevEl);
        }
        versionsLengths.put(currentVersion, size(currentVersion) - 1);

        return null;
    }

    public int indexOf(Object o, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);

        int result = -1;
        if (isEmpty(version)) {
            return result;
        }

        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int ind = 0; ind < size(version); ind++) {
            if (current.getObject(version).equals(o)) {
                result = ind;
                break;
            }
            current = current.getNext(version);
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
        if (isEmpty(version)) {
            return result;
        }
        PersistentListNode<E> current = versionedHeads.floorEntry(version).getValue();
        for (int ind = 0; ind < size(version); ind++) {
            if (current.getObject(version).equals(o)) {
                result = ind;
            }
            current = current.getNext(version);
        }
        return result;
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOf(o, currentVersion);
    }

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
                for (int i = 0; i < index; i++) {
                    currElement = currElement.getNext(_version);
                }
            }


            @Override
            public boolean hasNext() {
                return currElement.getNext(_version) != null;
            }

            @Override
            public Object next() {
                if (hasNext()) {
                    currElement = currElement.getNext(_version);
                    currIndex++;
                    return currElement.getObject(_version);
                } else
                    throw new NoSuchElementException(Exceptions.NO_SUCH_ELEMENT);
            }

            @Override
            public boolean hasPrevious() {
                return currElement.getPrev(_version) != null;
            }

            @Override
            public Object previous() {
                if (hasPrevious()) {
                    currElement = currElement.getPrev(_version);
                    currIndex--;
                    return currElement.getObject(_version);
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

    public ListIterator versionedListIterator(int version) {
        return versionedListIterator(version, 0);
    }

    @Override
    public ListIterator listIterator() {
        return versionedListIterator(currentVersion, 0);
    }

    @Override
    public ListIterator listIterator(int index) {
        return versionedListIterator(currentVersion, index);
    }

    // TODO what form of list must be returned?
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
            result.add(current.getObject(version));
            current = current.getNext(version);
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
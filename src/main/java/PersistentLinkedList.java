import java.util.*;
import java.util.function.UnaryOperator;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PersistentLinkedList<E> implements List {
    private int currentVersion = 0;
    private TreeMap<Integer, Integer> versionsLengths;
    private TreeMap<Integer, PersistentListNode<E>> versionedHeads;
    private TreeMap<Integer, PersistentListNode<E>> versionedTails;

    /**
     * Constructs an empty persistent list.
     */
    public PersistentLinkedList() {
        versionedHeads = new TreeMap<>();
        versionedTails = new TreeMap<>();
        versionsLengths = new TreeMap<>();
        versionsLengths.put(0, 0);
    }

    /**
     * Constructs a persistent list from specified collection.
     *
     * @param c specified collection
     */
    public PersistentLinkedList(Collection<E> c) {
        versionedHeads = new TreeMap<>();
        versionedTails = new TreeMap<>();
        versionsLengths = new TreeMap<>();
        versionsLengths.put(0, c.size());

        for (E obj : c) {
            add(obj, 0);
        }
    }

    /**
     * Returns the number of elements in the specified version of this list.
     * @param version version of this list
     * @return number of elements in the specified version of this list.
     */
    public int size(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.floorEntry(version).getValue();
    }

    /**
     * Returns the number of elements in the current version of this list.
     * @return number of elements in the current version of this list.
     */
    @Override
    public int size() {
        return size(currentVersion);
    }

    /**
     * Returns true if the specified version of this list contains no elements.
     * @param version version of this list
     * @return true if the specified version of this list contains no elements, false otherwise
     */
    public boolean isEmpty(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return size(version) == 0;
    }

    /**
     * Returns true if the current version of this list contains no elements.
     * @return true if the current version of this list contains no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return isEmpty(currentVersion);
    }

    /**
     * Returns true if specified version of this list contains the specified element. More formally, returns true if and only if specified version of this list contains at least one element e such that (o==null ? e==null : o.equals(e)).
     * @param o object for checking
     * @return true if current version of this list contains the specified element, false otherwise
     */
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

    /**
     * Returns true if current version of this list contains the specified element. More formally, returns true if and only if current version of this list contains at least one element e such that (o==null ? e==null : o.equals(e)).
     * @param o object for checking
     * @return true if current version of this list contains the specified element, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        return contains(o, currentVersion);
    }

    /**
     * Returns an iterator over the elements in the specified version of this list in proper sequence.
     * @return an iterator over the elements in the specified version of this list in proper sequence.
     */
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

    /**
     * Returns an iterator over the elements in the current version of this list in proper sequence.
     * @return an iterator over the elements in the current version of this list in proper sequence.
     */
    @Override
    public Iterator iterator() {
        return iterator(currentVersion);
    }

    /**
     * Returns an array containing all of the elements in the specified version of this list in proper sequence (from first to last element).
     *The returned array will be "safe" in that no references to it are maintained by this list. The caller is thus free to modify the returned array.
     *This method acts as bridge between array-based and collection-based APIs.
     * @return an array containing all of the elements in the specified version of this list in proper sequence
     */
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

    /**
     * Returns an array containing all of the elements in the current version of this list in proper sequence (from first to last element).
     *The returned array will be "safe" in that no references to it are maintained by this list. The caller is thus free to modify the returned array.
     *This method acts as bridge between array-based and collection-based APIs.
     * @return an array containing all of the elements in the current version of this list in proper sequence
     */
    @Override
    public Object[] toArray() {
        return toArray(currentVersion);
    }

    /**
     * Adds the object to specified version of this list (only for multiple update operations).
     * @param o element for adding
     * @return true if this collection changed as a result of the call
     */
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

    /**
     * Adds the object to this list.
     * @param o element for adding
     * @return true if this collection changed as a result of the call
     */
    @Override
    public boolean add(Object o) {
        currentVersion++;
        return add(o, currentVersion);
    }

    /**
     * Removes the first occurrence of the specified element from the specified version of this list, if it is present (only for multiple update operations).
     * If this list does not contain the element, it is unchanged.
     * More formally, removes the element with the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
     * @param o specified element
     * @return true if this list contained the specified element (or equivalently, if this list changed as a result of the call).
     */
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
                    nextEl.setPrev(version, prevEl);
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

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     * If this list does not contain the element, it is unchanged.
     * More formally, removes the element with the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
     * @param o specified element
     * @return true if this list contained the specified element (or equivalently, if this list changed as a result of the call).
     */
    @Override
    public boolean remove(Object o) {
        currentVersion++;
        return remove(o, currentVersion);
    }

    /**
     * Inserts all of the elements in the specified collection into this list at the specified position.
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (increases their indices).
     * The new elements will appear in this list in the order that they are returned by the specified collection's iterator.
     * The behavior of this operation is undefined if the specified collection is modified while the operation is in progress.
     * (Note that this will occur if the specified collection is this list, and it's nonempty.)
     * @param index index at which to insert the first element from the specified collection
     * @param c collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     */
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

    /**
     * Inserts all of the elements in the specified collection to the end of this list at the specified position.
     * The new elements will appear in this list in the order that they are returned by the specified collection's iterator.
     * The behavior of this operation is undefined if the specified collection is modified while the operation is in progress.
     * (Note that this will occur if the specified collection is this list, and it's nonempty.)
     * @param c collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     */
    @Override
    public boolean addAll(Collection c) {
        return addAll(size(), c);
    }

    /**
     * Retains only the elements in this list that are contained in the specified collection.
     * In other words, removes from this list all of its elements that are not contained in the specified collection.
     * @param c collection containing elements to be retained in this list
     * @return true if this list changed as a result of the call
     */
    @Override
    public boolean retainAll(Collection c) {
        if (c.isEmpty() || isEmpty())
            return false;
        PersistentListNode<E> current = versionedHeads.floorEntry(currentVersion).getValue();
        boolean isChanged = false;
        currentVersion++;
        for (int i = 0; i < size(); i++) {
            if (!c.contains(current.getObject(currentVersion))) {
                isChanged = isChanged || remove(current.getObject(currentVersion), currentVersion);
                current = current.getNext(currentVersion);
            }
        }
        if (!isChanged) {
            currentVersion--; }
        return isChanged;
    }

    /**
     * Removes from this list all of its elements that are contained in the specified collection (optional operation).
     * @param c collection containing elements to be removed from this list
     * @return true if this list changed as a result of the call
     */
    // TODO O(N*M) -> O(N + M)
    @Override
    public boolean removeAll(Collection c) {
        boolean isChanged = false;
        currentVersion++;
        for (Object o : c) {
            while (remove(o, currentVersion)) {
                isChanged = true;
            }
        }
        if (!isChanged) {
            currentVersion--; }
        return isChanged;
    }

    /**
     * Returns true if the specified version of this list contains all of the elements of the specified collection.
     * @param c collection to be checked for containment in the specified version of this list
     * @param version specified version of this list
     * @return true if the specified version of this list contains all of the elements of the specified collection
     */
    public boolean containsAll(Collection c, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        for (Object o : c) {
            if (!contains(o, version))
                return false;
        }
        return true;
    }

    /**
     * Returns true if this list contains all of the elements of the specified collection.
     * @param c collection to be checked for containment in this list
     * @return true if this list contains all of the elements of the specified collection
     */
    @Override
    public boolean containsAll(Collection c) {
        return containsAll(c, currentVersion);
    }

    //TODO понять чо тут вообще хотят
    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    /**
     * Replaces each element of this list with the result of applying the operator to that element.
     * Errors or runtime exceptions thrown by the operator are relayed to the caller.
     * @param operator the operator to apply to each element
     */
    @Override
    public void replaceAll(UnaryOperator operator) {
        PersistentListNode currElement = versionedHeads.floorEntry(currentVersion).getValue();
        currentVersion++;
        for (int i = 0; i < size(); i++) {
            set(i, operator.apply(currElement.getObject(currentVersion)), currentVersion);
            currElement = currElement.getNext(currentVersion);
        }
    }

    // TODO sort
    @Override
    public void sort(Comparator c) {

    }

    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     */
    @Override
    public void clear() {
        currentVersion++;
        versionsLengths.put(currentVersion, 0);
        versionedHeads.put(currentVersion, null);
        versionedTails.put(currentVersion, null);
    }

    /**
     * Returns the element at the specified position in the specified version of this list.
     * @param index index of the element to return
     * @param version specified version of this list
     * @return the element at the specified position in the specified version of this list
     */
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

    /**
     * Returns the element at the specified position in the current version of this list.
     * @param index index of the element to return
     * @return the element at the specified position in the current version of this list
     */
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

    /**
     * Replaces the element at the specified position in this list with the specified element (optional operation).
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     */
    @Override
    public Object set(int index, Object element) {
        currentVersion++;
        return set(index, element, currentVersion);
    }

    /**
     * Inserts the specified element at the specified position in the specified version of this list (only for multiple update operations).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     * @param index index at which the specified element is to be inserted
     * @param version specified version of this list
     * @param element element to be inserted
     */
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

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, Object element) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        currentVersion++;
        add(index, element, currentVersion);
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     */
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
            nextEl.setPrev(currentVersion, prevEl);
        } else {
            versionedTails.put(currentVersion, prevEl);
        }
        versionsLengths.put(currentVersion, size(currentVersion) - 1);

        return null;
    }

    /**
     * Returns the index of the first occurrence of the specified element in the specifies version of this list, or -1 if the specified version of this list does not contain the element.
     * More formally, returns the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such index.
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element
     */
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

    /**
     * Returns the index of the first occurrence of the specified element in the current version of this list, or -1 if the current version of this list does not contain the element.
     * More formally, returns the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such index.
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element
     */
    @Override
    public int indexOf(Object o) {
        return indexOf(o, currentVersion);
    }

    /**
     * Returns the index of the last occurrence of the specified element in the specified version of  this list, or -1 if the specified version of this list does not contain the element. More formally, returns the highest index i such that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such index.
     * @param o element to search for
     * @ the index of the last occurrence of the specified element in the specified version of this list, or -1 if the specified version of this list does not contain the element
     */
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

    /**
     * Returns the index of the last occurrence of the specified element in the current version of this list, or -1 if the current version of this list does not contain the element. More formally, returns the highest index i such that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such index.
     * @param o element to search for
     * @ the index of the last occurrence of the specified element in the current version of this list, or -1 if the current version of this list does not contain the element
     */
    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOf(o, currentVersion);
    }

    /**
     * Returns a list iterator over the elements in the specified version of this list (in proper sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be returned by an initial call to next.
     * An initial call to previous would return the element with the specified index minus one.
     * @param index index of the first element to be returned from the list iterator (by a call to next)
     * @return a list iterator over the elements in the specified version of this list (in proper sequence), starting at the specified position in the list
     */
    public ListIterator versionedListIterator(int version, int index) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        int size = size(version);
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Exceptions.LIST_INDEX_OUT_OF_BOUNDS);
        return new ListIterator() {
            int currIndex = index - 1;
            int _version = version;
            PersistentListNode currElement = versionedHeads.get(_version);

            {
                for (int i = 0; i < index; i++) {
                    currElement = currElement.getNext(_version);
                }
            }


            @Override
            public boolean hasNext() {
                if (currIndex < 0) return currElement != null;
                return currElement.getNext(_version) != null;
            }

            @Override
            public Object next() {
                if (hasNext()) {
                    if (currIndex >= 0)
                        currElement = currElement.getNext(_version);
                    currIndex++;
                    return currElement.getObject(_version);
                } else
                    throw new NoSuchElementException(Exceptions.NO_SUCH_ELEMENT);
            }

            @Override
            public boolean hasPrevious() {
                if (currIndex >= size) return currElement != null;
                return currElement.getPrev(_version) != null;
            }

            @Override
            public Object previous() {
                if (hasPrevious()) {
                    if (currIndex < size)
                        currElement = currElement.getPrev(_version);
                    currIndex--;
                    return currElement.getObject(_version);
                } else
                    throw new NoSuchElementException(Exceptions.NO_SUCH_ELEMENT);
            }

            @Override
            public int nextIndex() {
                return min(currIndex + 1, size);
            }

            @Override
            public int previousIndex() {
                return max(currIndex - 1, -1);
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

    /**
     * Returns a list iterator over the elements in the specified version of this list (in proper sequence).
     * @return a list iterator over the elements in the specified version of this list (in proper sequence)
     */
    public ListIterator versionedListIterator(int version) {
        return versionedListIterator(version, 0);
    }

    /**
     * Returns a list iterator over the elements in the current version of this list (in proper sequence).
     * @return a list iterator over the elements in the current version of this list (in proper sequence)
     */
    @Override
    public ListIterator listIterator() {
        return versionedListIterator(currentVersion, 0);
    }

    /**
     * Returns a list iterator over the elements in the current version of this list (in proper sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be returned by an initial call to next.
     * An initial call to previous would return the element with the specified index minus one.
     * @param index index of the first element to be returned from the list iterator (by a call to next)
     * @return a list iterator over the elements in this list (in proper sequence), starting at the specified position in the list
     */
    @Override
    public ListIterator listIterator(int index) {
        return versionedListIterator(currentVersion, index);
    }

    /**
     * Returns a view of the portion of the specified version of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
     * (If fromIndex and toIndex are equal, the returned list is empty.)
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex high endpoint (exclusive) of the subList
     * @return a view of the specified range within the specified version of this list
     */
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

    /**
     * Returns a view of the portion of the current version of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
     * (If fromIndex and toIndex are equal, the returned list is empty.)
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex high endpoint (exclusive) of the subList
     * @return a view of the specified range within the current version of this list
     */
    @Override
    public List subList(int fromIndex, int toIndex) {
        return subList(fromIndex, toIndex, currentVersion);
    }

    // TODO or not to do that's spliterator
    @Override
    public Spliterator spliterator() {
        throw new UnsupportedOperationException();
    }
}
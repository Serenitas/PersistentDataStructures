import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.TreeMap;


public class PersistentArray <E> {

    private static final int INIT_CAPACITY = 10;
    private int currentVersion = 0;
    private TreeMap <Integer, Integer> versionsLengths;
    private ArrayList <TreeMap <Integer, E>> versionedData;

    /**
     * Constructs an empty array with the initial capacity of ten.
     */
    public PersistentArray() {
        versionedData = new ArrayList<>();
        for (int i = 0; i < INIT_CAPACITY; i++) {
            versionedData.add(new TreeMap<>());
            versionedData.get(i).put(currentVersion, null);
        }
        versionsLengths = new TreeMap<>();
        versionsLengths.put(currentVersion, INIT_CAPACITY);
    }

    /**
     * Constructs an empty array with the specifies initial capacity.
     */
    public PersistentArray(int capacity) {
        versionedData = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            versionedData.add(new TreeMap<>());
            versionedData.get(i).put(currentVersion, null);
        }
        versionsLengths = new TreeMap<>();
        versionsLengths.put(currentVersion, capacity);
    }

    /**
     * Returns the element at the specified position in the specified version of the array.
     *
     * @param index   index of the element to return.
     * @param version version of array to get element.
     * @return the element at the specified position in the specified version of the array.
     */
    public E get(int index, int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        if (versionsLengths.floorEntry(version).getValue() <= index)
            throw new ArrayIndexOutOfBoundsException(Exceptions.INDEX_OUT_OF_BOUNDS);
        return versionedData.get(index).floorEntry(version).getValue();
    }

    /**
     * Returns the element at the specified position in the current version of the array.
     *
     * @param index index of the element to return.
     * @return the element at the specified position in the current version of the array.
     */
    public E get(int index) {
        return get(index, currentVersion);
    }

    /**
     * Replaces the element at the specified position in the last version of this array with the specified element.
     *
     * @param index index of the element to replace
     * @param obj element to be stored at the specified position
     * @return number of current version of the array
     */
    public int set(int index, E obj) {
        int curLen = versionsLengths.floorEntry(currentVersion).getValue();
        if (curLen <= index)
            throw new ArrayIndexOutOfBoundsException(Exceptions.INDEX_OUT_OF_BOUNDS);
        currentVersion++;
        versionedData.get(index).put(currentVersion, obj);
        return currentVersion;
    }

    /**
     * Returns the length of the specified version of this array.
     *
     * @param version version of array
     * @return length of the specified version of this array
     */
    public int getLength(int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.floorEntry(version).getValue();
    }

    /**
     * Returns the length of the current version of this array.
     *
     * @return length of the current version of this array
     */
    public int getLength() {
        return getLength(currentVersion);
    }

    /**
     * Adds the element as last in the last version of this array
     *
     * @param obj object to be added
     * @return current version of this array
     */
    public int add(E obj) {
        int curLen = getLength();
        if (curLen >= versionedData.size()) {
            versionedData.add(new TreeMap<>());
        }
        currentVersion++;
        versionedData.get(curLen).put(currentVersion, obj);
        versionsLengths.put(currentVersion, curLen + 1);
        return currentVersion;
    }

    /**
     * Removes last element in the last version of this array.
     *
     * @return current version of this array
     */
    public int remove() {
        int curLen = getLength();
        if (curLen == 0) {
            throw new ArrayIndexOutOfBoundsException(Exceptions.NOTHING_TO_REMOVE);
        }
        currentVersion++;
        versionsLengths.put(currentVersion, curLen - 1);
        return currentVersion;
    }
}
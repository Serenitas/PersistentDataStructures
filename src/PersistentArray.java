import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.TreeMap;


public class PersistentArray <E> {

    private static final int INIT_CAPACITY = 10;
    private int currentVersion = 0;
    private ArrayList <Integer> versionsLengths;
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
        versionsLengths = new ArrayList<>();
        versionsLengths.add(INIT_CAPACITY);
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
        versionsLengths = new ArrayList<>();
        versionsLengths.add(capacity);
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
        if (versionsLengths.get(version) <= index)
            throw new ArrayIndexOutOfBoundsException(Exceptions.INDEX_OUT_OF_BOUNDS);
        return versionedData.get(index).floorEntry(version).getValue();
    }

    /**
     * Returns the element at the specified position in the current version of the array.
     *
     * @param index index of the element to return.
     * @return the element at the specified position in the specified version of the array.
     */
    public E get(int index) {
        return get(index, currentVersion);
    }

    public void set(int index, E obj) {
        int curLen = versionsLengths.get(currentVersion);
        if (curLen <= index)
            throw new ArrayIndexOutOfBoundsException(Exceptions.INDEX_OUT_OF_BOUNDS);
        currentVersion++;
        versionedData.get(index).put(currentVersion, obj);
        versionsLengths.add(curLen);
    }

    public int getLength(int version) {
        if (version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.get(version);
    }

    public int getLength() {
        return getLength(currentVersion);
    }

    public void add(E obj) {
        int curLen = getLength();
        if (curLen >= versionedData.size()) {
            versionedData.add(new TreeMap<>());
        }
        currentVersion++;
        versionedData.get(curLen).put(currentVersion, obj);
        versionsLengths.add(curLen + 1);
    }

    public void remove() {
        int curLen = getLength();
        if (curLen == 0) {
            throw new ArrayIndexOutOfBoundsException(Exceptions.NOTHING_TO_REMOVE);
        }
        currentVersion++;
        versionsLengths.add(curLen - 1);
    }
}

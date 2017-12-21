import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by Svetlana on 21.12.2017.
 */
public class PersistentArray <E> {
    private static final int INIT_CAPACITY = 10;
    private int currentVersion = 0;
    private ArrayList <Integer> versionsLengths;
    private ArrayList <TreeMap <Integer, E>> versionedData;

    public PersistentArray() {
        versionedData = new ArrayList<>();
        for (int i = 0; i < INIT_CAPACITY; i++) {
            versionedData.add(new TreeMap<>());
            versionedData.get(i).put(currentVersion, null);
        }
        versionsLengths = new ArrayList<>();
        versionsLengths.add(INIT_CAPACITY);
    }

    public PersistentArray(int capacity) {
        versionedData = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            versionedData.add(new TreeMap<>());
            versionedData.get(i).put(currentVersion, null);
        }
        versionsLengths = new ArrayList<>();
        versionsLengths.add(capacity);
    }

    public E get(int index, int version) {
        if (versionsLengths.get(version) <= index)
            throw new ArrayIndexOutOfBoundsException();
        return versionedData.get(index).floorEntry(version).getValue();
    }

    public E get(int index) {
        return get(index, currentVersion);
    }

    public void set(int index, E obj) {
        if (versionsLengths.get(currentVersion) <= index)
            throw new ArrayIndexOutOfBoundsException();
        currentVersion++;
        versionedData.get(index).put(currentVersion, obj);
    }

    public int getLength(int version) {
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
        currentVersion++;
        versionsLengths.add(curLen - 1);
    }
}

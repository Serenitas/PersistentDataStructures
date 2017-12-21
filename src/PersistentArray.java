import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by Svetlana on 21.12.2017.
 */
public class PersistentArray <E> {
    private int currentVersion = 0;
    private ArrayList <TreeMap <Integer, E>> versionedData;

    public PersistentArray(int minCapacity) {
        versionedData = new ArrayList<>(minCapacity);
        for (int i = 0; i < minCapacity; i++) {
            versionedData.set(i, new TreeMap<Integer, E>());
            versionedData.get(i).put(currentVersion, null);
        }
    }

    public E get(int index, int version) {
        return versionedData.get(index).floorEntry(version).getValue();
    }

    public E get(int index) {
        return get(index, currentVersion);
    }

    public void set(int index, E obj) {
        currentVersion++;
        versionedData.get(index).put(currentVersion, obj);
    }

    public int getLength() {
        return versionedData.size();
    }
}

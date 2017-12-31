import java.util.TreeMap;

public class PersistentListNode<E> {

    private TreeMap<Integer, E> versionedData;
    private TreeMap<Integer, PersistentListNode<E>> versionedPrev;
    private TreeMap<Integer, PersistentListNode<E>> versionedNext;

    public PersistentListNode(E object, int version, PersistentListNode<E> prev, PersistentListNode<E> next) {
        versionedData = new TreeMap<>();
        versionedPrev = new TreeMap<>();
        versionedNext = new TreeMap<>();

        versionedData.put(version, object);
        versionedPrev.put(version, prev);
        versionedNext.put(version, next);
    }

    public PersistentListNode<E> getNext(int version) {
        return versionedNext.floorEntry(version).getValue();
    }

    public void setNext(int version, PersistentListNode<E> next) {
        versionedNext.put(version, next);
    }

    public PersistentListNode<E> getPrev(int version) {
        return versionedPrev.floorEntry(version).getValue();
    }

    public void setPrev(int version, PersistentListNode<E> prev) {
        versionedPrev.put(version, prev);
    }

    public E getObject(int version) { return versionedData.floorEntry(version).getValue(); }

    public void setObject(int version, E obj) {
        versionedData.put(version, obj);
    }
}

import java.util.TreeMap;

public class PersistentMapNode<E> {
    private class InnerNode<E> {
        private boolean isRemoved;
        private E object;

        InnerNode() {
            object = null;
            isRemoved = false;
        }

        InnerNode(E obj) {
            object = obj;
            isRemoved = false;
        }

        InnerNode(E obj, boolean removed) {
            object = obj;
            isRemoved = removed;
        }

        E getObject() {
            return object;
        }

        boolean isRemoved() {
            return isRemoved;
        }
    }

    private TreeMap<Integer, InnerNode<E>> versionedData;

    PersistentMapNode() {
        versionedData = new TreeMap<>();
    }

    PersistentMapNode(E object, int version) {
        versionedData = new TreeMap<>();
        setObject(version, object);
    }

    public E getObject(int version) { return versionedData.floorEntry(version).getValue().getObject(); }

    public void setObject(int version, E obj) {
        versionedData.put(version, new InnerNode<>(obj));
    }

    public void removeObject(int version) {
        versionedData.put(version, new InnerNode<>(null, true));
    }

    public boolean isRemoved(int version) {
        return versionedData.floorEntry(version).getValue().isRemoved();
    }
}

public class PersistentListNode<E> {
    private E object;
    private int version;

    private PersistentListNode<E> next = null, prev = null;

    public PersistentListNode(E object, int version, PersistentListNode<E> next, PersistentListNode<E> prev) {
        this.object = object;
        this.version = version;
        this.next = next;
        this.prev = prev;
    }

    public PersistentListNode<E> getNext() {
        return next;
    }

    public void setNext(PersistentListNode<E> next) {
        this.next = next;
    }

    public PersistentListNode<E> getPrev() {
        return prev;
    }

    public void setPrev(PersistentListNode<E> prev) {
        this.prev = prev;
    }

    public E getObject() {
        return object;
    }

    public int getVersion() {
        return version;
    }
}

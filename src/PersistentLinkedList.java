import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by Svetlana on 21.12.2017.
 */
public class PersistentLinkedList extends LinkedList
{

    public PersistentLinkedList()
    {
        super();
    }

    public PersistentLinkedList(Collection c)
    {
        super(c);
    }

    @Override
    public Object getFirst()
    {
        return super.getFirst();
    }

    @Override
    public Object getLast()
    {
        return super.getLast();
    }

    @Override
    public Object removeFirst()
    {
        return super.removeFirst();
    }

    @Override
    public Object removeLast()
    {
        return super.removeLast();
    }

    @Override
    public void addFirst(Object o)
    {
        super.addFirst(o);
    }

    @Override
    public void addLast(Object o)
    {
        super.addLast(o);
    }

    @Override
    public boolean contains(Object o)
    {
        return super.contains(o);
    }

    @Override
    public int size()
    {
        return super.size();
    }

    @Override
    public boolean isEmpty()
    {
        return super.isEmpty();
    }

    @Override
    public boolean add(Object o)
    {
        return super.add(o);
    }

    @Override
    public boolean remove(Object o)
    {
        return super.remove(o);
    }

    @Override
    public boolean containsAll(Collection c)
    {
        return super.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c)
    {
        return super.addAll(c);
    }

    @Override
    public boolean removeIf(Predicate filter)
    {
        return false;
    }

    @Override
    public boolean retainAll(Collection c)
    {
        return super.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection c)
    {
        return super.removeAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c)
    {
        return super.addAll(index, c);
    }

    @Override
    public void replaceAll(UnaryOperator operator)
    {

    }

    @Override
    public void sort(Comparator c)
    {

    }

    @Override
    public Iterator iterator()
    {
        return super.iterator();
    }

    @Override
    public void forEach(Consumer action)
    {

    }

    @Override
    public ListIterator listIterator()
    {
        return super.listIterator();
    }

    @Override
    public void clear()
    {
        super.clear();
    }

    @Override
    public Object get(int index)
    {
        return super.get(index);
    }

    @Override
    public Object set(int index, Object element)
    {
        return super.set(index, element);
    }

    @Override
    public void add(int index, Object element)
    {
        super.add(index, element);
    }

    @Override
    public Object remove(int index)
    {
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o)
    {
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return super.lastIndexOf(o);
    }

    @Override
    public Object peek()
    {
        return super.peek();
    }

    @Override
    public Object element()
    {
        return super.element();
    }

    @Override
    public Object poll()
    {
        return super.poll();
    }

    @Override
    public Object remove()
    {
        return super.remove();
    }

    @Override
    public boolean offer(Object o)
    {
        return super.offer(o);
    }

    @Override
    public boolean offerFirst(Object o)
    {
        return super.offerFirst(o);
    }

    @Override
    public boolean offerLast(Object o)
    {
        return super.offerLast(o);
    }

    @Override
    public Object peekFirst()
    {
        return super.peekFirst();
    }

    @Override
    public Object peekLast()
    {
        return super.peekLast();
    }

    @Override
    public Object pollFirst()
    {
        return super.pollFirst();
    }

    @Override
    public Object pollLast()
    {
        return super.pollLast();
    }

    @Override
    public void push(Object o)
    {
        super.push(o);
    }

    @Override
    public Object pop()
    {
        return super.pop();
    }

    @Override
    public boolean removeFirstOccurrence(Object o)
    {
        return super.removeFirstOccurrence(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o)
    {
        return super.removeLastOccurrence(o);
    }

    @Override
    public ListIterator listIterator(int index)
    {
        return super.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex)
    {
        return super.subList(fromIndex, toIndex);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex)
    {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public Iterator descendingIterator()
    {
        return super.descendingIterator();
    }

    @Override
    public Object clone()
    {
        return super.clone();
    }

    @Override
    public Object[] toArray()
    {
        return super.toArray();
    }

    @Override
    public Spliterator spliterator()
    {
        return super.spliterator();
    }

    @Override
    public Object[] toArray(Object[] a)
    {
        return super.toArray(a);
    }
}

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Created by Svetlana on 21.12.2017.
 */
public class PersistentArray extends ArrayList
{
    public PersistentArray(int initialCapacity)
    {
        super(initialCapacity);
    }

    public PersistentArray()
    {
        super();
    }

    public PersistentArray(Collection c)
    {
        super(c);
    }

    @Override
    public void trimToSize()
    {
        super.trimToSize();
    }

    @Override
    public void ensureCapacity(int minCapacity)
    {
        super.ensureCapacity(minCapacity);
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
    public boolean contains(Object o)
    {
        return super.contains(o);
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
    public Object[] toArray(Object[] a)
    {
        return super.toArray(a);
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
    public boolean add(Object o)
    {
        return super.add(o);
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
    public void clear()
    {
        super.clear();
    }

    @Override
    public boolean addAll(Collection c)
    {
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c)
    {
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex)
    {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean removeAll(Collection c)
    {
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c)
    {
        return super.retainAll(c);
    }

    @Override
    public ListIterator listIterator(int index)
    {
        return super.listIterator(index);
    }

    @Override
    public ListIterator listIterator()
    {
        return super.listIterator();
    }

    @Override
    public Iterator iterator()
    {
        return super.iterator();
    }

    @Override
    public List subList(int fromIndex, int toIndex)
    {
        return super.subList(fromIndex, toIndex);
    }

    @Override
    public void forEach(Consumer action)
    {
        super.forEach(action);
    }

    @Override
    public Spliterator spliterator()
    {
        return super.spliterator();
    }

    @Override
    public boolean removeIf(Predicate filter)
    {
        return super.removeIf(filter);
    }

    @Override
    public void replaceAll(UnaryOperator operator)
    {
        super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator c)
    {
        super.sort(c);
    }
}

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public class PersistentMap implements Map
{

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean containsKey(Object key)
    {
        return false;
    }

    @Override
    public boolean containsValue(Object value)
    {
        return false;
    }

    @Override
    public Object get(Object key)
    {
        return null;
    }

    @Override
    public Object put(Object key, Object value)
    {
        return null;
    }

    @Override
    public Object remove(Object key)
    {
        return null;
    }

    @Override
    public void putAll(Map m)
    {

    }

    @Override
    public void clear()
    {

    }

    @Override
    public Set keySet()
    {
        return null;
    }

    @Override
    public Collection values()
    {
        return null;
    }

    @Override
    public Set<Entry> entrySet()
    {
        return null;
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue)
    {
        return null;
    }

    @Override
    public void forEach(BiConsumer action)
    {

    }

    @Override
    public void replaceAll(BiFunction function)
    {

    }

    @Override
    public Object putIfAbsent(Object key, Object value)
    {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value)
    {
        return false;
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue)
    {
        return false;
    }

    @Override
    public Object replace(Object key, Object value)
    {
        return null;
    }

    @Override
    public Object computeIfAbsent(Object key, Function mappingFunction)
    {
        return null;
    }

    @Override
    public Object computeIfPresent(Object key, BiFunction remappingFunction)
    {
        return null;
    }

    @Override
    public Object compute(Object key, BiFunction remappingFunction)
    {
        return null;
    }

    @Override
    public Object merge(Object key, Object value, BiFunction remappingFunction)
    {
        return null;
    }
}

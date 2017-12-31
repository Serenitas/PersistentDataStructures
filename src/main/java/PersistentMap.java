import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public class PersistentMap<K, V> implements Map {
    private int currentVersion = 0;
    private TreeMap<Integer, Integer> versionsLengths;
    private TreeMap<K, TreeMap<Integer, V>> versionedData;

    public PersistentMap() {

    }


    public int size(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        return versionsLengths.floorEntry(version).getValue();
    }

    @Override
    public int size() {
        return size(currentVersion);
    }

    public boolean isEmpty(int version)
    {
        return size(version) == 0;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(currentVersion);
    }

    public boolean containsKey(Object key, int version) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey(key, currentVersion);
    }

    public boolean containsValue(Object value, int version) {
        return false;
    }

    @Override
    public boolean containsValue(Object value)
    {
        return containsValue(value, currentVersion);
    }

    public Object get(Object key, int version) {
        return null;
    }

    @Override
    public Object get(Object key)
    {
        return get(key, currentVersion);
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

    public Set keySet(int version) {
        return null;
    }

    @Override
    public Set keySet() {
        return keySet(currentVersion);
    }

    public Collection values(int version) {
        return null;
    }

    @Override
    public Collection values() {
        return values(currentVersion);
    }

    public Set<Entry> entrySet(int version) {
        return null;
    }

    @Override
    public Set<Entry> entrySet() {
        return entrySet(currentVersion);
    }

    public Object getOrDefault(Object key, Object defaultValue, int version) {
        return null;
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return getOrDefault(key, defaultValue, currentVersion);
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

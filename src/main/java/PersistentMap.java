import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public class PersistentMap<K, V> implements Map {

    private class PersistentMapEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        public PersistentMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    private int currentVersion = 0;
    private TreeMap<Integer, Integer> versionsLengths;
    private TreeMap<K, PersistentMapNode<V>> versionedData;

    public PersistentMap() {
        versionsLengths = new TreeMap<>();
        versionedData = new TreeMap<>();
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
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);

        if (versionedData.containsKey(key)) {
            PersistentMapNode node = versionedData.get(key);
            if (!node.isRemoved(version)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey(key, currentVersion);
    }

    public boolean containsValue(Object value, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        for (PersistentMapNode<V> node: versionedData.values()) {
            if (!node.isRemoved(version)) {
                if (null == value) {
                    if (node.getObject(version) == null)
                        return true;
                } else {
                    if (value.equals(node.getObject(version)))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value)
    {
        return containsValue(value, currentVersion);
    }

    public Object get(Object key, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        PersistentMapNode node = versionedData.get(key);
        if (!node.isRemoved(version)) {
            return node.getObject(version);
        }
        return null;
    }

    @Override
    public Object get(Object key)
    {
        return get(key, currentVersion);
    }

    @Override
    public Object put(Object key, Object value) {
        Object oldValue = null;
        PersistentMapNode node = versionedData.get(key);
        currentVersion++;
        if (null == node) {
            versionedData.put((K)key, new PersistentMapNode<V>((V)value, currentVersion));
        } else {
            oldValue = node.getObject(currentVersion - 1);
            node.setObject(currentVersion, value);
        }
        return oldValue;
    }

    @Override
    public Object remove(Object key) {
        Object oldValue = null;
        PersistentMapNode node = versionedData.get(key);
        currentVersion++;
        if (null != node) {
            oldValue = node.getObject(currentVersion - 1);
            node.removeObject(currentVersion);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map m) {
        currentVersion++;
        for (Object entry : m.entrySet()) {
            K key = ((Map.Entry<K, V>) entry).getKey();
            V value = ((Map.Entry<K, V>) entry).getValue();

            PersistentMapNode node = versionedData.get(key);
            if (null == node) {
                versionedData.put(key, new PersistentMapNode<V>(value, currentVersion));
            } else {
                node.setObject(currentVersion, value);
            }
        }
    }

    @Override
    public void clear() {
        currentVersion++;
        for (Map.Entry<K, PersistentMapNode<V>> entry : versionedData.entrySet()) {
            if (entry.getValue().isRemoved(currentVersion - 1)) {
                continue;
            }
            entry.getValue().removeObject(currentVersion); // is that change value in real map?
        }
    }

    public Set keySet(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        Set<K> keys = versionedData.keySet();
        Set<K> resultKeys = versionedData.keySet();

        for (K key : keys) {
            if (versionedData.get(key).isRemoved(version)) {
                resultKeys.remove(key);
            }
        }
        return resultKeys;
    }

    @Override
    public Set keySet() {
        return keySet(currentVersion);
    }

    public Collection values(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);

        LinkedList<V> result = new LinkedList<V>();
        for (Map.Entry<K, PersistentMapNode<V>> entry : versionedData.entrySet()) {
            if (!entry.getValue().isRemoved(version)) {
                result.add(entry.getValue().getObject(version));
            }
        }
        return result;
    }

    @Override
    public Collection values() {
        return values(currentVersion);
    }

    public Set<Entry> entrySet(int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);
        Set<Entry> result = new TreeSet<>();

        for (Map.Entry<K, PersistentMapNode<V>> entry : versionedData.entrySet()) {
            if (!entry.getValue().isRemoved(version)) {
                result.add(new PersistentMapEntry<>(entry.getKey(), entry.getValue().getObject(version)));
            }
        }
        return result;
    }

    @Override
    public Set<Entry> entrySet() {
        return entrySet(currentVersion);
    }

    public Object getOrDefault(Object key, Object defaultValue, int version) {
        if (version < 0 || version > currentVersion)
            throw new NoSuchElementException(Exceptions.NO_SUCH_VERSION);

        if (versionedData.get(key) != null && !versionedData.get(key).isRemoved(version)) {
            return versionedData.get(key).getObject(version);
        }
        return defaultValue;
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return getOrDefault(key, defaultValue, currentVersion);
    }

    @Override
    public void forEach(BiConsumer action) {
        for (Map.Entry<K, PersistentMapNode<V>> entry : versionedData.entrySet()) {
            if (!entry.getValue().isRemoved(currentVersion)) {
                action.accept(entry.getKey(), entry.getValue().getObject(currentVersion));
            }
        }
    }

    @Override
    public void replaceAll(BiFunction function) {
        for (Map.Entry<K, PersistentMapNode<V>> entry : versionedData.entrySet()) {
            if (!entry.getValue().isRemoved(currentVersion)) {
                entry.getValue().setObject(currentVersion + 1, (V)function.apply(entry.getKey(), entry.getValue().getObject(currentVersion)));
            }
        }
        currentVersion++;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        Object oldValue = null;
        PersistentMapNode node = versionedData.get(key);
        currentVersion++;
        if (null == node) {
            versionedData.put((K)key, new PersistentMapNode<V>((V)value, currentVersion));
        } else {
            oldValue = node.getObject(currentVersion - 1);
            if (null == oldValue) {
                node.setObject(currentVersion, value);
            }
        }
        return oldValue;
    }

    @Override
    public boolean remove(Object key, Object value)
    {
        PersistentMapNode node = versionedData.get(key);
        int curSize = size();

        if (null != node && node.getObject(currentVersion).equals(value) && !node.isRemoved(currentVersion)) {
            currentVersion++;
            node.removeObject(currentVersion);
            versionsLengths.put(currentVersion, curSize - 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        PersistentMapNode node = versionedData.get(key);

        if (null != node && null != node.getObject(currentVersion) &&
                !node.isRemoved(currentVersion) && node.getObject(currentVersion).equals(oldValue)) {
            currentVersion++;
            node.setObject(currentVersion, newValue);
            return true;
        }
        return false;
    }

    @Override
    public Object replace(Object key, Object value) {
        Object oldValue = null;
        PersistentMapNode node = versionedData.get(key);

        if (null != node && !node.isRemoved(currentVersion)) {
            oldValue = node.getObject(currentVersion);
            currentVersion++;
            node.setObject(currentVersion, value);
        }
        return oldValue;
    }

    @Override
    public Object computeIfAbsent(Object key, Function mappingFunction) {
        PersistentMapNode node = versionedData.get(key);

        if (null != node && !node.isRemoved(currentVersion) && node.getObject(currentVersion) != null) {
            return node.getObject(currentVersion);
        }

        Object value = mappingFunction.apply(key);
        if (null != value) {
            currentVersion++;
            if (null == node) {
                versionedData.put((K)key, new PersistentMapNode<V>((V)value, currentVersion));
            } else {
                node.setObject(currentVersion, value);
            }
        }

        return value;
    }

    @Override
    public Object computeIfPresent(Object key, BiFunction remappingFunction) {
        PersistentMapNode node = versionedData.get(key);

        if (null == node || node.isRemoved(currentVersion) || node.getObject(currentVersion) == null) {
            return null;
        }

        Object oldValue = node.getObject(currentVersion);
        Object value = remappingFunction.apply(key, oldValue);
        currentVersion++;
        if (null != value) {
            node.setObject(currentVersion, value);
        } else {
            node.removeObject(currentVersion);
        }

        return value;
    }

    @Override
    public Object compute(Object key, BiFunction remappingFunction) {
        PersistentMapNode node = versionedData.get(key);

        Object oldValue = null;
        if (null != node && !node.isRemoved(currentVersion)) {
            oldValue = node.getObject(currentVersion);
        }

        Object value = remappingFunction.apply(key, oldValue);
        currentVersion++;
        if (null != value) {
            node.setObject(currentVersion, value);
        } else {
            if (null != oldValue) {
                node.removeObject(currentVersion);
            }
        }

        return value;
    }

    @Override
    public Object merge(Object key, Object value, BiFunction remappingFunction) {
        PersistentMapNode node = versionedData.get(key);
        if (null == node || node.isRemoved(currentVersion) || node.getObject(currentVersion) == null) {
            currentVersion++;
            node.setObject(currentVersion, value);
            return value;
        }

        Object oldValue = node.getObject(currentVersion);
        Object newValue = remappingFunction.apply(key, oldValue);
        currentVersion++;

        if (null != newValue) {
            node.setObject(currentVersion, newValue);
        } else {
            node.removeObject(currentVersion);
        }

        return newValue;
    }
}

/***************************************************************************
 * Project file:    NPlugins - NCore - AbstractBiMap.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.bimap.AbstractBiMap
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.bimap;

import java.util.Map;

public abstract class AbstractBiMap<K, V> implements BiMap<K, V> {

    protected final Map<K, V> map;
    protected final Map<V, K> inverseMap;

    /* package */ AbstractBiMap(final Map<K, V> map, final Map<V, K> inverseMap) {
        this.map = map;
        this.inverseMap = inverseMap;
    }

    @Override
    public void put(final K key, final V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("BiMap doesn't support null key and/or value");
        } else if (this.map.containsKey(key)) {
            throw new IllegalArgumentException("BiMap already contains key " + key);
        } else if (this.inverseMap.containsKey(value)) {
            throw new IllegalArgumentException("BiMap already contains value " + value);
        }
        this.map.put(key, value);
        this.inverseMap.put(value, key);
    }

    @Override
    public boolean containsKey(final K key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(final V value) {
        return this.inverseMap.containsKey(value);
    }

    @Override
    public V removeKey(final K key) {
        final V value = this.map.remove(key);
        this.inverseMap.remove(value);
        return value;
    }

    @Override
    public K removeValue(final V value) {
        final K key = this.inverseMap.remove(value);
        this.map.remove(key);
        return key;
    }

    @Override
    public V getValue(final K key) {
        return this.map.get(key);
    }

    @Override
    public K getKey(final V value) {
        return this.inverseMap.get(value);
    }
}

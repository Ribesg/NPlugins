/***************************************************************************
 * Project file:    NPlugins - NCore - TreeBiMap.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.bimap.TreeBiMap
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.bimap;

import java.util.SortedMap;
import java.util.TreeMap;

public class TreeBiMap<K, V> extends AbstractBiMap<K, V> implements SortedBiMap<K, V> {

    public TreeBiMap() {
        super(new TreeMap<K, V>(), new TreeMap<V, K>());
    }

    @Override
    public K firstKey() {
        return ((SortedMap<K, V>)this.map).firstKey();
    }

    @Override
    public K lastKey() {
        return ((SortedMap<K, V>)this.map).lastKey();
    }

    @Override
    public V firstValue() {
        return ((SortedMap<V, K>)this.inverseMap).firstKey();
    }

    @Override
    public V lastValue() {
        return ((SortedMap<V, K>)this.inverseMap).lastKey();
    }

    @Override
    public V removeFirstKey() {
        return this.removeKey(this.firstKey());
    }

    @Override
    public V removeLastKey() {
        return this.removeKey(this.lastKey());
    }

    @Override
    public K removeFirstValue() {
        return this.removeValue(this.firstValue());
    }

    @Override
    public K removeLastValue() {
        return this.removeValue(this.lastValue());
    }
}

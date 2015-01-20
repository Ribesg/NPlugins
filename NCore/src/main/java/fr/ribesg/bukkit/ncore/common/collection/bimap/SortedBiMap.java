/***************************************************************************
 * Project file:    NPlugins - NCore - SortedBiMap.java                    *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.bimap.SortedBiMap
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.bimap;

/**
 * A simple sorted BiMap.
 *
 * @author Ribesg
 */
public interface SortedBiMap<K, V> extends BiMap<K, V> {

    public K firstKey();

    public K lastKey();

    public V firstValue();

    public V lastValue();

    public V removeFirstKey();

    public V removeLastKey();

    public K removeFirstValue();

    public K removeLastValue();
}

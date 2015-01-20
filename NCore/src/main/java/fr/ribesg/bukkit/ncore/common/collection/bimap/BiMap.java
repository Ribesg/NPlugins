/***************************************************************************
 * Project file:    NPlugins - NCore - BiMap.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.bimap.BiMap   *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.bimap;

/**
 * A simple BiMap.
 *
 * @author Ribesg
 */
public interface BiMap<K, V> {

    public void put(final K key, final V value);

    public boolean containsKey(final K key);

    public boolean containsValue(final V value);

    public V removeKey(final K key);

    public K removeValue(final V value);

    public V getValue(final K key);

    public K getKey(final V value);
}

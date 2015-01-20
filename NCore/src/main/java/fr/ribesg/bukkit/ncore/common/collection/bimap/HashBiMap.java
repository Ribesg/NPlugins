/***************************************************************************
 * Project file:    NPlugins - NCore - HashBiMap.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.bimap.HashBiMap
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.bimap;

import java.util.HashMap;

public class HashBiMap<K, V> extends AbstractBiMap<K, V> {

    public HashBiMap() {
        super(new HashMap<K, V>(), new HashMap<V, K>());
    }
}

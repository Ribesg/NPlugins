/***************************************************************************
 * Project file:    NPlugins - NCore - PairList.java                       *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.pairlist.PairList
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.pairlist;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PairList<K, V> implements Iterable<Pair<K, V>> {

	private final List<Pair<K, V>> pairList;

	public PairList() {
		this.pairList = new ArrayList<>();
	}

	public Iterator<Pair<K, V>> iterator() {
		return pairList.iterator();
	}

	public void clear() {
		this.pairList.clear();
	}

	public int size() {
		return this.pairList.size();
	}

	public boolean isEmpty() {
		return this.pairList.isEmpty();
	}

	public void put(final K key, final V value) {
		this.pairList.add(new Pair<>(key, value));
	}
}

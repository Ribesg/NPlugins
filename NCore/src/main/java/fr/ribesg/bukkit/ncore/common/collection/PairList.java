package fr.ribesg.bukkit.ncore.common.collection;
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

	public void put(K key, V value) {
		this.pairList.add(new Pair<>(key, value));
	}
}

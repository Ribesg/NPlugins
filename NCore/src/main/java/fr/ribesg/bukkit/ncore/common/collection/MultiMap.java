package fr.ribesg.bukkit.ncore.common.collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiMap<K, V> implements Iterable<Pair<K, V>> {

	private final List<Pair<K, V>> pairSet;

	public MultiMap() {
		this.pairSet = new ArrayList<>();
	}

	public void put(K key, V value) {
		this.pairSet.add(new Pair<>(key, value));
	}

	public void clear() {
		pairSet.clear();
	}

	public boolean isEmpty() {
		return pairSet.isEmpty();
	}

	public int size() {
		return pairSet.size();
	}

	public Iterator<Pair<K, V>> iterator() {
		return pairSet.iterator();
	}
}

package fr.ribesg.bukkit.ncore.common.collection;
public class Pair<K, V> {

	private K key;
	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Pair pair = (Pair) o;

		if (key != null ? !key.equals(pair.key) : pair.key != null) {
			return false;
		}
		if (value != null ? !value.equals(pair.value) : pair.value != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Pair{" +
		       "key=" + key +
		       ", value=" + value +
		       '}';
	}
}

package fr.ribesg.bukkit.nworld.warp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author Ribesg */
public class Warps implements Iterable<Warp> {

	private final Map<String, Warp> warps;

	public Warps() {
		this.warps = new HashMap<>();
	}

	@Override
	public Iterator<Warp> iterator() {
		List<Warp> list = new ArrayList<>(warps.values());
		Collections.sort(list);
		return list.iterator();
	}

	public void worldEnabled(String worldName) {
		for (Warp warp : this) {
			if (!warp.isEnabled() && warp.getLocation().getWorldName().equals(worldName)) {
				warp.setEnabled(true);
			}
		}
	}

	public void worldDisabled(String worldName) {
		for (Warp warp : this) {
			if (warp.isEnabled() && warp.getLocation().getWorldName().equals(worldName)) {
				warp.setEnabled(false);
			}
		}
	}

	/////////////////
	// Map methods //
	/////////////////

	public int size() {
		return warps.size();
	}

	public Collection<Warp> values() {
		return warps.values();
	}

	public Warp remove(String key) {
		return warps.remove(key.toLowerCase());
	}

	public Warp get(String key) {
		return warps.get(key.toLowerCase());
	}

	public Set<String> keySet() {
		return warps.keySet();
	}

	public boolean containsKey(String key) {
		return warps.containsKey(key.toLowerCase());
	}

	public void clear() {
		warps.clear();
	}

	public boolean containsValue(Warp value) {
		return warps.containsValue(value);
	}

	public boolean isEmpty() {
		return warps.isEmpty();
	}

	public Set<Map.Entry<String, Warp>> entrySet() {
		return warps.entrySet();
	}

	public Warp put(String key, Warp value) {
		return warps.put(key.toLowerCase(), value);
	}

	public void putAll(Map<? extends String, ? extends Warp> m) {
		warps.putAll(m);
	}
}

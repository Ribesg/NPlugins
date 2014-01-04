/***************************************************************************
 * Project file:    NPlugins - NWorld - Warps.java                         *
 * Full Class name: fr.ribesg.bukkit.nworld.warp.Warps                     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
		final List<Warp> list = new ArrayList<>(warps.values());
		Collections.sort(list);
		return list.iterator();
	}

	public void worldEnabled(final String worldName) {
		for (final Warp warp : this.warps.values()) {
			if (!warp.isEnabled() && warp.getLocation().getWorldName().equals(worldName)) {
				warp.setEnabled(true);
			}
		}
	}

	public void worldDisabled(final String worldName) {
		for (final Warp warp : this.warps.values()) {
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

	public Warp remove(final String key) {
		return warps.remove(key.toLowerCase());
	}

	public Warp get(final String key) {
		return warps.get(key.toLowerCase());
	}

	public Set<String> keySet() {
		return warps.keySet();
	}

	public boolean containsKey(final String key) {
		return warps.containsKey(key.toLowerCase());
	}

	public void clear() {
		warps.clear();
	}

	public boolean containsValue(final Warp value) {
		return warps.containsValue(value);
	}

	public boolean isEmpty() {
		return warps.isEmpty();
	}

	public Set<Map.Entry<String, Warp>> entrySet() {
		return warps.entrySet();
	}

	public Warp put(final String key, final Warp value) {
		return warps.put(key.toLowerCase(), value);
	}

	public void putAll(final Map<String, Warp> map) {
		for (final Map.Entry<String, Warp> e : map.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}
}

/***************************************************************************
 * Project file:    NPlugins - NWorld - Worlds.java                        *
 * Full Class name: fr.ribesg.bukkit.nworld.world.Worlds                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.world;
import fr.ribesg.bukkit.nworld.world.GeneralWorld.WorldType;

import java.util.*;

/** @author Ribesg */
public class Worlds implements Iterable<GeneralWorld> {

	private final Map<String, GeneralWorld> worlds;

	public Worlds() {
		this.worlds = new HashMap<>();
	}

	@Override
	public Iterator<GeneralWorld> iterator() {
		final List<GeneralWorld> list = new ArrayList<>(worlds.values());
		Collections.sort(list);
		return list.iterator();
	}

	public SortedMap<String, StockWorld> getStock() {
		final SortedMap<String, StockWorld> result = new TreeMap<>();
		for (final GeneralWorld world : this) {
			if (WorldType.isStock(world)) {
				result.put(world.getWorldName(), (StockWorld) world);
			}
		}
		return result;
	}

	public SortedMap<String, AdditionalWorld> getAdditional() {
		final SortedMap<String, AdditionalWorld> result = new TreeMap<>();
		for (final GeneralWorld world : this) {
			if (world.getType() == WorldType.ADDITIONAL) {
				result.put(world.getWorldName(), (AdditionalWorld) world);
			}
		}
		return result;
	}

	public SortedMap<String, AdditionalSubWorld> getAdditionalSub() {
		final SortedMap<String, AdditionalSubWorld> result = new TreeMap<>();
		for (final GeneralWorld world : this) {
			if (world.getType() == WorldType.ADDITIONAL_SUB_NETHER || world.getType() == WorldType.ADDITIONAL_SUB_END) {
				result.put(world.getWorldName(), (AdditionalSubWorld) world);
			}
		}
		return result;
	}

	/////////////////
	// Map methods //
	/////////////////

	public int size() {
		return worlds.size();
	}

	public int sizeNormal() {
		int size = 0;
		for (final GeneralWorld w : worlds.values()) {
			if (w.getType() == WorldType.STOCK || w.getType() == WorldType.ADDITIONAL) {
				size++;
			}
		}
		return size;
	}

	public int sizeNether() {
		int size = 0;
		for (final GeneralWorld w : worlds.values()) {
			if (w.getType() == WorldType.STOCK_NETHER || w.getType() == WorldType.ADDITIONAL_SUB_NETHER) {
				size++;
			}
		}
		return size;
	}

	public int sizeEnd() {
		int size = 0;
		for (final GeneralWorld w : worlds.values()) {
			if (w.getType() == WorldType.STOCK_END || w.getType() == WorldType.ADDITIONAL_SUB_END) {
				size++;
			}
		}
		return size;
	}

	public boolean isEmpty() {
		return worlds.isEmpty();
	}

	public boolean containsKey(final String key) {
		return worlds.containsKey(key);
	}

	public boolean containsValue(final GeneralWorld value) {
		return worlds.containsValue(value);
	}

	public GeneralWorld get(final String key) {
		return worlds.get(key);
	}

	public GeneralWorld put(final String key, final GeneralWorld value) {
		return worlds.put(key, value);
	}

	public void putAll(final Map<? extends String, ? extends GeneralWorld> m) {
		worlds.putAll(m);
	}

	public GeneralWorld remove(final String key) {
		return worlds.remove(key);
	}

	public void clear() {
		worlds.clear();
	}

	public Set<String> keySet() {
		return worlds.keySet();
	}

	public Collection<GeneralWorld> values() {
		return worlds.values();
	}

	public Set<Map.Entry<String, GeneralWorld>> entrySet() {
		return worlds.entrySet();
	}
}

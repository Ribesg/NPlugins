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
		List<GeneralWorld> list = new ArrayList<>(worlds.values());
		Collections.sort(list);
		return list.iterator();
	}

	public SortedMap<String, StockWorld> getStock() {
		SortedMap<String, StockWorld> result = new TreeMap<>();
		for (GeneralWorld world : this) {
			if (WorldType.isStock(world)) {
				result.put(world.getWorldName(), (StockWorld) world);
			}
		}
		return result;
	}

	public SortedMap<String, AdditionalWorld> getAdditional() {
		SortedMap<String, AdditionalWorld> result = new TreeMap<>();
		for (GeneralWorld world : this) {
			if (world.getType() == WorldType.ADDITIONAL) {
				result.put(world.getWorldName(), (AdditionalWorld) world);
			}
		}
		return result;
	}

	public SortedMap<String, AdditionalSubWorld> getAdditionalSub() {
		SortedMap<String, AdditionalSubWorld> result = new TreeMap<>();
		for (GeneralWorld world : this) {
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

	public boolean isEmpty() {
		return worlds.isEmpty();
	}

	public boolean containsKey(String key) {
		return worlds.containsKey(key);
	}

	public boolean containsValue(GeneralWorld value) {
		return worlds.containsValue(value);
	}

	public GeneralWorld get(String key) {
		return worlds.get(key);
	}

	public GeneralWorld put(String key, GeneralWorld value) {
		return worlds.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends GeneralWorld> m) {
		worlds.putAll(m);
	}

	public GeneralWorld remove(String key) {
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
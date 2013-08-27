package fr.ribesg.bukkit.nworld.world;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nworld.NWorld;
import org.bukkit.World;

/** @author Ribesg */
public class StockWorld extends GeneralWorld {

	public StockWorld(NWorld instance,
	                  String worldName,
	                  NLocation spawnLocation,
	                  String requiredPermission,
	                  boolean enabled,
	                  boolean hidden) {
		super(instance, worldName, spawnLocation, requiredPermission, enabled, hidden);
	}

	@Override
	public World create() {
		throw new UnsupportedOperationException();
	}

	@Override
	public World load() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void unload() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getSeed() {
		throw new UnsupportedOperationException();
	}
}

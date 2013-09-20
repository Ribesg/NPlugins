package fr.ribesg.bukkit.nworld.world;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nworld.NWorld;

import java.util.Random;

/**
 * Represents a World for which the config part was malformed
 *
 * @author Ribesg
 */
public class MalformedWorld extends GeneralWorld {

	public MalformedWorld(NWorld instance, String worldName) {
		super(instance, worldName, new NLocation(worldName, 0, 0, 0, 0, 0), "random.permission." + new Random().nextLong(), false, true);
		setType(WorldType.UNKNOWN);
	}

	@Override
	public long getSeed() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isMalformed() {
		return true;
	}
}

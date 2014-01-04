/***************************************************************************
 * Project file:    NPlugins - NWorld - AdditionalSubWorld.java            *
 * Full Class name: fr.ribesg.bukkit.nworld.world.AdditionalSubWorld       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.world;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nworld.NWorld;
import org.bukkit.World.Environment;

/** @author Ribesg */
public class AdditionalSubWorld extends GeneralWorld {

	private final AdditionalWorld parentWorld;

	public AdditionalSubWorld(final NWorld instance,
	                          final AdditionalWorld parentWorld,
	                          final NLocation spawnLocation,
	                          final String requiredPermission,
	                          final boolean enabled,
	                          final boolean hidden,
	                          final Environment type) {
		super(instance);
		this.parentWorld = parentWorld;
		String worldName = parentWorld.getWorldName();
		if (type == Environment.NETHER) {
			worldName += "_nether";
			setType(WorldType.ADDITIONAL_SUB_NETHER);
			parentWorld.setNetherWorld(this);
		} else if (type == Environment.THE_END) {
			worldName += "_the_end";
			setType(WorldType.ADDITIONAL_SUB_END);
			parentWorld.setEndWorld(this);
		} else {
			throw new IllegalArgumentException("Invalid sub-world type: " + type.toString());
		}
		setWorldName(worldName);
		setSpawnLocation(spawnLocation);
		setRequiredPermission(requiredPermission);
		setEnabled(enabled);
		setHidden(hidden);
		if (!plugin.getWorlds().containsKey(worldName)) {
			plugin.getWorlds().put(worldName, this);
		}
	}

	public long getSeed() {
		return parentWorld.getSeed();
	}

	public boolean isMalformed() {
		return false;
	}

	public AdditionalWorld getParentWorld() {
		return parentWorld;
	}
}

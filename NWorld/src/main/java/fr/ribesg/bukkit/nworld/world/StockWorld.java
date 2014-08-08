/***************************************************************************
 * Project file:    NPlugins - NWorld - StockWorld.java                    *
 * Full Class name: fr.ribesg.bukkit.nworld.world.StockWorld               *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.world;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nworld.NWorld;

import org.bukkit.World;

/**
 * @author Ribesg
 */
public class StockWorld extends GeneralWorld {

	public StockWorld(final NWorld instance, final String worldName, final WorldType type, final NLocation spawnLocation, final String requiredPermission, final boolean enabled, final boolean hidden) {
		super(instance, worldName, spawnLocation, requiredPermission, enabled, hidden);
		this.setType(type);
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

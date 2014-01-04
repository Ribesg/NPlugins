/***************************************************************************
 * Project file:    NPlugins - NWorld - MalformedWorld.java                *
 * Full Class name: fr.ribesg.bukkit.nworld.world.MalformedWorld           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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

	public MalformedWorld(final NWorld instance, final String worldName) {
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

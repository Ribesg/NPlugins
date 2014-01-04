/***************************************************************************
 * Project file:    NPlugins - NWorld - AdditionalWorld.java               *
 * Full Class name: fr.ribesg.bukkit.nworld.world.AdditionalWorld          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.world;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nworld.NWorld;

/** @author Ribesg */
public class AdditionalWorld extends GeneralWorld {

	private final long               seed;
	private       boolean            hasNether;
	private       AdditionalSubWorld netherWorld;
	private       boolean            hasEnd;
	private       AdditionalSubWorld endWorld;

	public AdditionalWorld(final NWorld instance,
	                       final String worldName,
	                       final long seed,
	                       final NLocation spawnLocation,
	                       final String requiredPermission,
	                       final boolean enabled,
	                       final boolean hidden,
	                       final boolean hasNether,
	                       final boolean hasEnd) {
		super(instance, worldName, spawnLocation, requiredPermission, enabled, hidden);
		this.seed = seed;
		this.hasNether = hasNether;
		netherWorld = null;
		this.hasEnd = hasEnd;
		endWorld = null;
		setType(WorldType.ADDITIONAL);
	}

	public long getSeed() {
		return seed;
	}

	public boolean hasNether() {
		return hasNether;
	}

	public void setNether(final boolean hasNether) {
		this.hasNether = hasNether;
	}

	public boolean hasEnd() {
		return hasEnd;
	}

	public void setEnd(final boolean hasEnd) {
		this.hasEnd = hasEnd;
	}

	public AdditionalSubWorld getEndWorld() {
		return endWorld;
	}

	public void setEndWorld(final AdditionalSubWorld endWorld) {
		this.endWorld = endWorld;
	}

	public AdditionalSubWorld getNetherWorld() {
		return netherWorld;
	}

	public void setNetherWorld(final AdditionalSubWorld netherWorld) {
		this.netherWorld = netherWorld;
	}

	public boolean isMalformed() {
		return false;
	}
}

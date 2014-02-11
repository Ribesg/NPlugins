/***************************************************************************
 * Project file:    NPlugins - NCuboid - GroupConfig.java                  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.config.GroupConfig            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.config;
/**
 * @author Ribesg
 */
public class GroupConfig {

	private final String groupName;
	private final int    maxRegionNb;
	private final int    maxRegion1DSize;
	private final int    maxRegion3DSize;

	public GroupConfig(final String groupName, final int maxRegionNb, final int maxRegion1DSize, final int maxRegion3DSize) {
		this.groupName = groupName;
		this.maxRegionNb = maxRegionNb;
		this.maxRegion1DSize = maxRegion1DSize;
		this.maxRegion3DSize = maxRegion3DSize;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getGroupPerm() {
		return "group." + groupName.toLowerCase();
	}

	public int getMaxRegionNb() {
		return maxRegionNb;
	}

	public int getMaxRegion1DSize() {
		return maxRegion1DSize;
	}

	public int getMaxRegion3DSize() {
		return maxRegion3DSize;
	}
}

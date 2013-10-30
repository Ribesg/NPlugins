package fr.ribesg.bukkit.ncuboid.config;
/** @author Ribesg */
public class GroupConfig {

	private final String groupName;
	private final int    maxRegionNb;
	private final int    maxRegion1DSize;
	private final int    maxRegion3DSize;

	public GroupConfig(String groupName, int maxRegionNb, int maxRegion1DSize, int maxRegion3DSize) {
		this.groupName = groupName;
		this.maxRegionNb = maxRegionNb;
		this.maxRegion1DSize = maxRegion1DSize;
		this.maxRegion3DSize = maxRegion3DSize;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getGroupPerm() {
		return "group." + groupName;
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

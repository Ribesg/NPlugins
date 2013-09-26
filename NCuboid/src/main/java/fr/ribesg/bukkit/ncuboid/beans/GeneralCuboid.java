package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;

public abstract class GeneralCuboid extends Cuboid {

	public static enum CuboidType {
		// RectCuboid
		RECT,

		// WorldCuboid
		WORLD;
	}

	// Identification / informations related
	private String     worldName;
	private CuboidType type;

	// Protection related
	private final Rights rights;
	private       int    priority;

	// Flags related
	private final Flags          flags;
	private final FlagAttributes flagAtts;

	// This is for Dynmap!
	private final boolean dynmapable;
	private       boolean shownOnDynmap;

	// Create a new Cuboid, when user select points etc
	public GeneralCuboid(final String worldName, final CuboidType type) {
		this(worldName, type, new Rights(), 0, new Flags(), new FlagAttributes(), true);
	}

	public GeneralCuboid(final String worldName,
	                     final CuboidType type,
	                     final Rights rights,
	                     final int priority,
	                     final Flags flags,
	                     final FlagAttributes flagAtts,
	                     final boolean shownOnDynmap) {
		setWorldName(worldName);
		setType(type);
		switch (type) {
			case RECT:
				this.dynmapable = true;
				this.shownOnDynmap = shownOnDynmap;
				break;
			case WORLD:
			default:
				this.dynmapable = false;
				this.shownOnDynmap = false;
				break;
		}
		this.rights = rights;
		setPriority(priority);
		this.flags = flags;
		this.flagAtts = flagAtts;
	}

	// Location check
	public boolean contains(final Location loc) {
		return contains(new NLocation(loc));
	}

	public abstract boolean contains(final NLocation loc);

	public abstract String getCuboidName();

	public abstract long getTotalSize();

	public FlagAttributes getFlagAtts() {
		return flagAtts;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public CuboidType getType() {
		return type;
	}

	public void setType(CuboidType type) {
		this.type = type;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public boolean isDynmapable() {
		return this.dynmapable;
	}

	public boolean isShownOnDynmap() {
		return shownOnDynmap;
	}

	public void setShownOnDynmap(boolean shownOnDynmap) {
		this.shownOnDynmap = shownOnDynmap;
	}

	public boolean getFlag(Flag f) {
		return flags.getFlag(f);
	}

	public void setFlag(Flag f, boolean b) {
		flags.setFlag(f, b);
	}

	public void allowCommand(String command) {
		rights.allowCommand(command);
	}

	public void denyGroup(String groupName) {
		rights.denyGroup(groupName);
	}

	public boolean isAllowedPlayer(Player p) {
		return rights.isAllowedPlayer(p);
	}

	public void allowGroup(String groupName) {
		rights.allowGroup(groupName);
	}

	public void denyCommand(String command) {
		rights.denyCommand(command);
	}

	public void allowPlayer(String playerName) {
		rights.allowPlayer(playerName);
	}

	public boolean isAllowedPlayerName(String playerName) {
		return rights.isAllowedPlayerName(playerName);
	}

	public boolean isAllowedCommand(String command) {
		return rights.isAllowedCommand(command);
	}

	public void denyPlayer(String playerName) {
		rights.denyPlayer(playerName);
	}

	public boolean isAllowedGroupName(String groupName) {
		return rights.isAllowedGroupName(groupName);
	}

	public Integer getIntFlagAtt(FlagAtt f) {
		return flagAtts.getIntFlagAtt(f);
	}

	public void setIntFlagAtt(FlagAtt f, Integer i) {
		flagAtts.setIntFlagAtt(f, i);
	}

	public Location getLocFlagAtt(FlagAtt f) {
		return flagAtts.getLocFlagAtt(f);
	}

	public Vector getVectFlagAtt(FlagAtt f) {
		return flagAtts.getVectFlagAtt(f);
	}

	public void setLocFlagAtt(FlagAtt f, Location loc) {
		flagAtts.setLocFlagAtt(f, loc);
	}

	public void setVectFlagAtt(FlagAtt f, Vector v) {
		flagAtts.setVectFlagAtt(f, v);
	}

	public Set<String> getDisallowedCommands() {
		return rights.getDisallowedCommands();
	}

	public Set<String> getAllowedPlayers() {
		return rights.getAllowedPlayers();
	}

	public Set<String> getAllowedGroups() {
		return rights.getAllowedGroups();
	}
}

/***************************************************************************
 * Project file:    NPlugins - NCuboid - GeneralRegion.java                *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.GeneralRegion           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class GeneralRegion extends Region implements Comparable<GeneralRegion> {

	public static enum RegionType {
		// Cuboid Region
		CUBOID,

		// World Region
		WORLD
	}

	// Identification / informations related
	private String     worldName;
	private RegionType type;

	// Protection related
	private final Rights rights;
	private       int    priority;

	// Flags related
	private final Flags      flags;
	private final Attributes attributes;

	// This is for Dynmap!
	private final boolean dynmapable;

	// Create a new Region, when user select points etc
	public GeneralRegion(final String worldName, final RegionType type, final int priority) {
		this(worldName, type, new Rights(), priority, new Flags(), new Attributes());
	}

	public GeneralRegion(final String worldName, final RegionType type, final Rights rights, final int priority, final Flags flags, final Attributes attributes) {
		super();
		this.setWorldName(worldName);
		this.setType(type);
		switch (type) {
			case CUBOID:
				this.dynmapable = true;
				break;
			case WORLD:
			default:
				this.dynmapable = false;
				break;
		}
		this.rights = rights;
		this.setPriority(priority);
		this.flags = flags;
		this.attributes = attributes;
	}

	// Location check
	public boolean contains(final Location loc) {
		return this.contains(new NLocation(loc));
	}

	public abstract boolean contains(final NLocation loc);

	public abstract String getRegionName();

	public abstract long getTotalSize();

	public Attributes getAttributes() {
		return this.attributes;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public RegionType getType() {
		return this.type;
	}

	public void setType(final RegionType type) {
		this.type = type;
	}

	public String getWorldName() {
		return this.worldName;
	}

	public void setWorldName(final String worldName) {
		this.worldName = worldName;
	}

	public boolean isDynmapable() {
		return this.dynmapable;
	}

	public boolean getFlag(final Flag f) {
		return this.flags.getFlag(f);
	}

	public void setFlag(final Flag f, final boolean b) {
		this.flags.setFlag(f, b);
	}

	public String getStringAttribute(final Attribute att) {
		return this.attributes.getStringAttribute(att);
	}

	public void setStringAttribute(final Attribute att, final String s) {
		this.attributes.setStringAttribute(att, s);
	}

	public Integer getIntegerAttribute(final Attribute att) {
		return this.attributes.getIntegerAttribute(att);
	}

	public void setIntegerAttribute(final Attribute att, final Integer i) {
		this.attributes.setIntegerAttribute(att, i);
	}

	public Location getLocationAttribute(final Attribute att) {
		return this.attributes.getLocationAttribute(att);
	}

	public void setLocationAttribute(final Attribute att, final Location loc) {
		this.attributes.setLocationAttribute(att, loc);
	}

	public Vector getVectorAttribute(final Attribute att) {
		return this.attributes.getVectorAttribute(att);
	}

	public void setVectorAttribute(final Attribute att, final Vector v) {
		this.attributes.setVectorAttribute(att, v);
	}

	public String getStringRepresentation(final Attribute att) {
		return this.attributes.getStringRepresentation(att);
	}

	public boolean isUser(final Player player) {
		return this.rights.isUser(player);
	}

	public boolean isUser(final Player player, final boolean async) {
		return this.rights.isUser(player, async);
	}

	public boolean isUserId(final UUID id) {
		return this.rights.isUserId(id);
	}

	public boolean isAdmin(final Player player) {
		return this.rights.isAdmin(player);
	}

	public boolean isAdminId(final UUID id) {
		return this.rights.isAdminId(id);
	}

	public boolean isAllowedGroup(final String groupName) {
		return this.rights.isAllowedGroup(groupName);
	}

	public boolean isAllowedCommand(final String command) {
		return this.rights.isAllowedCommand(command);
	}

	public Set<UUID> getUsers() {
		return this.rights.getUsers();
	}

	public Set<UUID> getAdmins() {
		return this.rights.getAdmins();
	}

	public Set<String> getAllowedGroups() {
		return this.rights.getAllowedGroups();
	}

	public Set<String> getDisallowedCommands() {
		return this.rights.getDisallowedCommands();
	}

	public void removeUser(final UUID id) {
		this.rights.removeUser(id);
	}

	public void addUser(final UUID id) {
		this.rights.addUser(id);
	}

	public void addAdmin(final UUID id) {
		this.rights.addAdmin(id);
	}

	public void removeAdmin(final UUID id) {
		this.rights.removeAdmin(id);
	}

	public void allowGroup(final String groupName) {
		this.rights.allowGroup(groupName);
	}

	public void denyGroup(final String groupName) {
		this.rights.denyGroup(groupName);
	}

	public void allowCommand(final String command) {
		this.rights.allowCommand(command);
	}

	public void denyCommand(final String command) {
		this.rights.denyCommand(command);
	}

	@Override
	public int compareTo(final GeneralRegion o) {
		int res = Integer.compare(this.priority, o.priority);
		if (res != 0) {
			return -res;
		} else {
			res = Long.compare(this.getTotalSize(), o.getTotalSize());
			if (res != 0) {
				return res;
			} else {
				return this.getRegionName().compareTo(o.getRegionName());
			}
		}
	}
}

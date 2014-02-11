/***************************************************************************
 * Project file:    NPlugins - NWorld - Warp.java                          *
 * Full Class name: fr.ribesg.bukkit.nworld.warp.Warp                      *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.warp;
import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.Location;

/**
 * @author Ribesg
 */
public class Warp implements Comparable<Warp> {

	private String    name;
	private NLocation location;
	private boolean   enabled;
	private String    requiredPermission;
	private boolean   hidden;

	public Warp(final String name, final NLocation location, final boolean enabled, final String requiredPermission, final boolean hidden) {
		this.name = name;
		this.location = location;
		this.enabled = enabled;
		this.requiredPermission = requiredPermission;
		this.hidden = hidden;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public NLocation getLocation() {
		return location;
	}

	public void setLocation(final NLocation location) {
		this.location = location;
	}

	public void setLocation(final Location location) {
		setLocation(new NLocation(location));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public String getRequiredPermission() {
		return requiredPermission;
	}

	public void setRequiredPermission(final String requiredPermission) {
		this.requiredPermission = requiredPermission;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public int compareTo(final Warp o) {
		return name.compareTo(o.name);
	}
}

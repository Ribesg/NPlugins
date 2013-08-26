package fr.ribesg.bukkit.nworld.warp;
import fr.ribesg.bukkit.ncore.utils.NLocation;
import org.bukkit.Location;

/** @author Ribesg */
public class Warp implements Comparable<Warp> {

	private String    name;
	private NLocation location;
	private boolean   enabled;
	private String    requiredPermission;
	private boolean   hidden;

	public Warp(String name, NLocation location, boolean enabled, String requiredPermission, boolean hidden) {
		this.name = name;
		this.location = location;
		this.enabled = enabled;
		this.requiredPermission = requiredPermission;
		this.hidden = hidden;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NLocation getLocation() {
		return location;
	}

	public void setLocation(NLocation location) {
		this.location = location;
	}

	public void setLocation(Location location) {
		setLocation(new NLocation(location));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRequiredPermission() {
		return requiredPermission;
	}

	public void setRequiredPermission(String requiredPermission) {
		this.requiredPermission = requiredPermission;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public int compareTo(Warp o) {
		return name.compareTo(o.name);
	}
}

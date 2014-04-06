/***************************************************************************
 * Project file:    NPlugins - NPermissions - PermissionsSet.java          *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.PermissionsSet*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents a Set of Permissions, with a name and a priority.
 *
 * @author Ribesg
 */
public abstract class PermissionsSet {

	/**
	 * A regex matching any String starting with 'group.' of 'maingroup.'.
	 */
	private static final Pattern DENIED_PERMISSIONS_REGEX = Pattern.compile("^(?:main)?group\\..*$");

	/**
	 * The Permissions Manager
	 */
	protected final PermissionsManager manager;

	/**
	 * The name of this Permissions Set
	 */
	protected String name;

	/**
	 * The priority of this Permissions Set
	 */
	protected final int priority;

	/**
	 * Permissions explicitly allowed or denied by this Permissions Set
	 */
	protected final Map<String, Boolean> permissions;

	/**
	 * Permissions explicitly allowed or denied by this Permissions Set
	 * and/or any dependency of this PermissionsSet
	 */
	protected Map<String, Boolean> computedPermissions;

	/**
	 * Permissions Set constructor.
	 *
	 * @param manager  the Permissions Manager
	 * @param name     the name of this Permissions Set
	 * @param priority the priority of this Permissions Set
	 */
	protected PermissionsSet(final PermissionsManager manager, final String name, final int priority) {
		this.manager = manager;
		this.name = name;
		this.priority = priority;
		this.permissions = new LinkedHashMap<>();
	}

	/**
	 * Gets the name of this Permissions Set.
	 *
	 * @return the name of this Permissions Set
	 */
	protected String getName() {
		return this.name;
	}

	/**
	 * Gets the priority of this Permissions Set.
	 *
	 * @return the priority of this Permissions Set
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * Adds a new allowed or denied Permission to this Permissions Set.
	 *
	 * @param permission the allowed or denied Permission to add to this
	 *                   Permissions Set
	 *
	 * @throws PermissionException if it's an attempt to register an
	 *                             internal permission or if there's a
	 *                             duplicate permission declaration
	 */
	public void add(final String permission, final boolean value) throws PermissionException {
		final String lowerCasedPerm = permission.toLowerCase();
		if (DENIED_PERMISSIONS_REGEX.matcher(lowerCasedPerm).matches()) {
			throw new PermissionException("Attempt to register internal permission '" + lowerCasedPerm + "'");
		} else {
			final Boolean currentValue = this.permissions.get(lowerCasedPerm);
			if (currentValue == null) {
				this.permissions.put(lowerCasedPerm, value);
			} else {
				if (currentValue != value) {
					this.permissions.put(lowerCasedPerm, false);
					throw new PermissionException("Permission '" + lowerCasedPerm + "' is both allowed and denied. Setting to denied.");
				} else {
					this.permissions.put(lowerCasedPerm, value);
					throw new PermissionException("Permission '" + lowerCasedPerm + "' is set to " + value + " twice. Duplicate will be removed");
				}
			}
		}

	}

	/**
	 * Saves this PermissionsSet priority (if non-default), allow and deny
	 * permissions list under the provided ConfigurationSection.
	 *
	 * @param thisSection the ConfigurationSection under which this
	 *                    PermissionsSet's informations will be saved
	 */
	protected void saveCommon(final ConfigurationSection thisSection) {
		if (this.priority != this.getDefaultPriority()) {
			thisSection.set("priority", this.priority);
		}

		final List<String> allowList = new LinkedList<>();
		final List<String> denyList = new LinkedList<>();
		for (final Map.Entry<String, Boolean> e : this.permissions.entrySet()) {
			if (!DENIED_PERMISSIONS_REGEX.matcher(e.getKey()).matches()) {
				if (e.getValue()) {
					allowList.add(e.getKey());
				} else {
					denyList.add(e.getKey());
				}
			}
		}

		if (allowList.size() > 0) {
			thisSection.set("allow", new LinkedList<>(allowList));
		}
		if (denyList.size() > 0) {
			thisSection.set("deny", new LinkedList<>(denyList));
		}
	}

	/**
	 * Gets the default priority for this PermissionsSet.
	 *
	 * @return 0 for a Group, 1 for a Player
	 */
	protected abstract int getDefaultPriority();

	public Map<String, Boolean> getComputedPermissions() {
		if (this.computedPermissions == null) {
			this.computePermissions();
		}
		return this.computedPermissions;
	}

	/**
	 * Compute a Map of permissions that this PermissionsSet explicitly
	 * allows or denies.
	 * <p>
	 * Certain implementations of this may be a bit heavy due to having to
	 * browse through dependency of this PermissionsSet.
	 */
	public void computePermissions() {
		this.computedPermissions = computePermissions(new HashMap<String, Boolean>());
	}

	/**
	 * This is the method that should be implemented to compute denied
	 * Permissions for this PermissionsSet based on subtype.
	 *
	 * @param resultMap the resultMap to provision
	 *
	 * @return the same resultMap, update with local information
	 */
	protected abstract Map<String, Boolean> computePermissions(Map<String, Boolean> resultMap);
}

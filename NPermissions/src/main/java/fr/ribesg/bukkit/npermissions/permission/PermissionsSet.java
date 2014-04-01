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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
	protected final String name;

	/**
	 * The priority of this Permissions Set
	 */
	protected final int priority;

	/**
	 * Permissions explicitly allowed by this Permissions Set
	 */
	protected final Set<String> allow;

	/**
	 * Permissions explicitly denied by this Permissions Set
	 */
	protected final Set<String> deny;

	/**
	 * Permissions explicitly allowed by this Permissions Set and/or
	 * any dependency of this PermissionsSet
	 */
	protected Set<String> computedAllowed;

	/**
	 * Permissions explicitly denied by this Permissions Set and/or
	 * any dependency of this PermissionsSet
	 */
	protected Set<String> computedDenied;

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
		this.allow = new HashSet<>();
		this.deny = new HashSet<>();
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
	 * Adds a new allowed Permission to this Permissions Set.
	 * <p>
	 * Note: any permission that is already denied by this Permissions Set
	 * will not be added.
	 *
	 * @param permission the allowed Permission to add to this Permissions
	 *                   Set
	 *
	 * @throws PermissionException if it's an attempt to register an internal permission
	 */
	public void addAllow(final String permission) throws PermissionException {
		final String lowerCasedPerm = permission.toLowerCase();
		if (DENIED_PERMISSIONS_REGEX.matcher(lowerCasedPerm).matches()) {
			throw new PermissionException("Attempt to register internal permission '" + lowerCasedPerm + "'");
		} else if (!this.deny.contains(lowerCasedPerm)) {
			this.allow.add(permission.toLowerCase());
		}

	}

	/**
	 * Gets the computed Set of permissions this PermissionSet and/or all
	 * dependencies of this PermissionsSet explicitly allow.
	 *
	 * @return the computed Set of permissions this PermissionSet and/or all
	 * dependencies of this PermissionsSet explicitly allow
	 */
	public Set<String> getComputedAllowed() {
		return this.computedAllowed;
	}

	/**
	 * Adds a new denied Permission to this Permissions Set.
	 * <p>
	 * Note: any permission that is already allowed by this Permissions Set
	 * will be added to the denied Permissions and removed from the allowed
	 * Permissions.
	 *
	 * @param permission the denied Permission to add to this Permissions Set
	 *
	 * @throws PermissionException if it's an attempt to register an internal permission
	 */
	public void addDeny(final String permission) throws PermissionException {
		final String lowerCasedPerm = permission.toLowerCase();
		if (DENIED_PERMISSIONS_REGEX.matcher(lowerCasedPerm).matches()) {
			throw new PermissionException("Attempt to register internal permission '" + lowerCasedPerm + "'");
		} else {
			this.allow.remove(lowerCasedPerm);
			this.deny.add(lowerCasedPerm);
		}
	}

	/**
	 * Gets the computed Set of permissions this PermissionSet and/or all
	 * dependencies of this PermissionsSet explicitly deny.
	 *
	 * @return the computed Set of permissions this PermissionSet and/or all
	 * dependencies of this PermissionsSet explicitly deny
	 */
	public Set<String> getComputedDenied() {
		return this.computedDenied;
	}

	/**
	 * Saves this PermissionsSet priority (if non-default), allow and deny
	 * permissions list under the provided ConfigurationSection.
	 *
	 * @param thisSection the ConfigurationSection under which this
	 *                    PermissionsSet's informations will be saved
	 */
	protected void save(final ConfigurationSection thisSection) {
		if (this.priority != this.getDefaultPriority()) {
			thisSection.set("priority", this.priority);
		}

		final List<String> allowList = new LinkedList<>(this.allow);
		final Iterator<String> itAllow = allowList.iterator();
		while (itAllow.hasNext()) {
			if (DENIED_PERMISSIONS_REGEX.matcher(itAllow.next()).matches()) {
				itAllow.remove();
			}
		}
		if (allowList.size() > 0) {
			thisSection.set("allow", new LinkedList<>(allowList));
		}

		final List<String> denyList = new LinkedList<>(this.deny);
		final Iterator<String> itDeny = allowList.iterator();
		while (itDeny.hasNext()) {
			if (DENIED_PERMISSIONS_REGEX.matcher(itDeny.next()).matches()) {
				itDeny.remove();
			}
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

	/**
	 * Compute a Set of permissions that this PermissionsSet explicitly
	 * allows.
	 * <p>
	 * Certain implementations of this may be a bit heavy due to having to
	 * browse through dependency of this PermissionsSet.
	 *
	 * @return a Set of permissions that this PermissionsSet explicitly
	 * allows
	 */
	public Set<String> computeAllowedPermissions() {
		return computeAllowedPermissions(new HashSet<String>());
	}

	/**
	 * Compute a Set of permissions that this PermissionsSet explicitly
	 * denies.
	 * <p>
	 * Certain implementations of this may be a bit heavy due to having to
	 * browse through dependency of this PermissionsSet.
	 *
	 * @return a Set of permissions that this PermissionsSet explicitly
	 * denies
	 */
	public Set<String> computeDeniedPermissions() {
		return computeDeniedPermissions(new HashSet<String>());
	}

	/**
	 * This is the method that should be implemented to compute allowed
	 * Permissions for this PermissionsSet based on subtype.
	 *
	 * @param resultSet the resultSet to provision
	 *
	 * @return the same resultSet, update with local information
	 */
	protected abstract Set<String> computeAllowedPermissions(Set<String> resultSet);

	/**
	 * This is the method that should be implemented to compute denied
	 * Permissions for this PermissionsSet based on subtype.
	 *
	 * @param resultSet the resultSet to provision
	 *
	 * @return the same resultSet, update with local information
	 */
	protected abstract Set<String> computeDeniedPermissions(Set<String> resultSet);
}

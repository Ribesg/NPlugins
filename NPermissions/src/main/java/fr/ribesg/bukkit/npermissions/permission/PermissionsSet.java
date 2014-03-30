/***************************************************************************
 * Project file:    NPlugins - NPermissions - PermissionsSet.java          *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.PermissionsSet*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import java.util.HashSet;
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
	private static final Pattern DENIED_PERMISSIONS_REGEX = Pattern.compile("^(?:main)?group\\.");

	/**
	 * The name of this Permissions Set
	 */
	protected final String name;

	/**
	 * The priority of this Permissions Set
	 */
	protected final int priority;

	/**
	 * Permissions allowed by this Permissions Set
	 */
	protected final Set<String> allow;

	/**
	 * Permissions denied by this Permissions Set
	 */
	protected final Set<String> deny;

	/**
	 * Permissions Set constructor.
	 *
	 * @param name     the name of this Permissions Set
	 * @param priority the priority of this Permissions Set
	 */
	protected PermissionsSet(final String name, final int priority) {
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
	public String getName() {
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
	 * Checks if this Permissions Set explicitly allows a Permission.
	 *
	 * @param permission the Permission to check
	 *
	 * @return true if this Permissions Set explicitly allows the provided
	 * Permission, false otherwise
	 */
	public boolean allows(final String permission) {
		// We don't have to check for the deny Set because a permission
		// can't be in both Sets.
		return this.allow.contains(permission.toLowerCase());
	}

	/**
	 * Checks if this Permissions Set explicitly denies a Permission.
	 *
	 * @param permission the Permission to check
	 *
	 * @return true if this Permissions Set explicitly denies the provided
	 * Permission, false otherwise
	 */
	public boolean denies(final String permission) {
		return this.deny.contains(permission.toLowerCase());
	}
}

/***************************************************************************
 * Project file:    NPlugins - NPermissions - PermissionException.java     *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.PermissionException
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;

/**
 * Exception that may be thrown when an error occurs in the Permissions
 * system.
 *
 * @author Ribesg
 */
public class PermissionException extends Exception {

	public PermissionException(final String message) {
		super(message);
	}

	public PermissionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public PermissionException(final Throwable cause) {
		super(cause);
	}
}

/***************************************************************************
 * Project file:    NPlugins - NPermissions - NListener.java               *
 * Full Class name: fr.ribesg.bukkit.npermissions.NListener                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions;
import org.bukkit.event.Listener;

/**
 * NPermissions Listener. Binds PermissionAttachments to
 * connecting players and clear disconnecting players.
 *
 * @author Ribesg
 */
public class NListener implements Listener {

	/**
	 * The NPermissions plugin instance
	 */
	private final NPermissions plugin;

	/**
	 * NPermissions Listener constructor.
	 *
	 * @param instance the NPermissions plugin instance
	 */
	public NListener(final NPermissions instance) {
		this.plugin = instance;
	}

	// TODO Attach! Detach!
}

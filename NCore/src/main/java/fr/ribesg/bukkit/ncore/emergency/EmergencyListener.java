/***************************************************************************
 * Project file:    NPlugins - NCore - EmergencyListener.java              *
 * Full Class name: fr.ribesg.bukkit.ncore.emergency.EmergencyListener     *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.emergency;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import org.bukkit.event.Listener;

/** @author Ribesg */
public class EmergencyListener implements Listener {

	private NPlugin plugin;

	public EmergencyListener(NPlugin instance) {
		this.plugin = instance;
	}
}

/***************************************************************************
 * Project file:    NPlugins - NCuboid - AbstractListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.AbstractListener    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners;

import fr.ribesg.bukkit.ncuboid.NCuboid;

import org.bukkit.event.Listener;

public abstract class AbstractListener implements Listener {

	private final NCuboid plugin;

	public AbstractListener(final NCuboid instance) {
		this.plugin = instance;
	}

	public NCuboid getPlugin() {
		return this.plugin;
	}
}

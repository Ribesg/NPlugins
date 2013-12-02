/***************************************************************************
 * Project file:    NPlugins - NGeneral - GeneralFeature.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.GeneralFeature       *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature;
import fr.ribesg.bukkit.ngeneral.NGeneral;

public abstract class GeneralFeature {

	protected final NGeneral plugin;

	protected GeneralFeature(final NGeneral instance) {
		this.plugin = instance;
	}

	public NGeneral getPlugin() {
		return this.plugin;
	}

	public abstract void init();

	public void disable() {
		// Should be overriden by Features that have to do something onDisable
	}
}

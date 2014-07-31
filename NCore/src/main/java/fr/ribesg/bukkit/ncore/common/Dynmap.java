/***************************************************************************
 * Project file:    NPlugins - NCore - Dynmap.java                         *
 * Full Class name: fr.ribesg.bukkit.ncore.common.Dynmap                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dynmap.DynmapCommonAPI;

public class Dynmap {

	private static final String DYNMAP_PLUGIN_NAME = "dynmap";

	private static Dynmap instance;

	public static boolean init() {
		if (!Dynmap.hasDynmap()) {
			Dynmap.instance = new Dynmap();
		}
		return Dynmap.hasDynmap();
	}

	public static boolean hasDynmap() {
		return Dynmap.instance != null && Dynmap.instance._hasDynmap();
	}

	public static DynmapCommonAPI getApi() {
		return Dynmap.hasDynmap() ? Dynmap.instance.api : null;
	}

	public static boolean showPlayer(final Player player) {
		return Dynmap.hasDynmap() && Dynmap.instance._showPlayer(player);
	}

	public static boolean hidePlayer(final Player player) {
		return Dynmap.hasDynmap() && Dynmap.instance._hidePlayer(player);
	}

	private final DynmapCommonAPI api;

	public Dynmap() {
		this.api = (DynmapCommonAPI) Bukkit.getPluginManager().getPlugin(DYNMAP_PLUGIN_NAME);
	}

	private boolean _hasDynmap() {
		return this.api != null;
	}

	private boolean _showPlayer(final Player player) {
		return this.setPlayerVisibility(player.getName(), true);
	}

	private boolean _hidePlayer(final Player player) {
		return this.setPlayerVisibility(player.getName(), false);
	}

	private boolean setPlayerVisibility(final String playerName, final boolean visible) {
		if (this._hasDynmap()) {
			this.api.setPlayerVisiblity(playerName, visible);
		}
		return this._hasDynmap();
	}
}

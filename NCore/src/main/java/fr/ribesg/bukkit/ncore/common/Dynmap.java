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

/**
 * A Dynmap bridge
 *
 * @author Ribesg
 */
public class Dynmap {

    /**
     * Name of the Dynmap plugin
     */
    private static final String DYNMAP_PLUGIN_NAME = "dynmap";

    /**
     * Instance of the Dynmap bridge
     */
    private static Dynmap instance;

    /**
     * Initializes the Dynmap bridge.
     *
     * @return true if Dynmap exists, false otherwise
     */
    public static boolean init() {
        if (!Dynmap.hasDynmap()) {
            Dynmap.instance = new Dynmap();
        }
        return Dynmap.hasDynmap();
    }

    /**
     * Checks if Dynmap exists.
     *
     * @return true if Dynmap exists, false otherwise
     *
     * @see #_hasDynmap()
     */
    public static boolean hasDynmap() {
        return Dynmap.instance != null && Dynmap.instance._hasDynmap();
    }

    /**
     * Gets the Dynmap API.
     *
     * @return the Dynmap API, if any
     */
    public static DynmapCommonAPI getApi() {
        return Dynmap.hasDynmap() ? Dynmap.instance.api : null;
    }

    /**
     * Shows the provided Player on Dynmap.
     *
     * @param player the Player
     *
     * @return true if Dynmap exists, false otherwise
     *
     * @see #_showPlayer(Player)
     */
    public static boolean showPlayer(final Player player) {
        return Dynmap.hasDynmap() && Dynmap.instance._showPlayer(player);
    }

    /**
     * Hides the provided Player on Dynmap.
     *
     * @param player the Player
     *
     * @return true if Dynmap exists, false otherwise
     *
     * @see #_hidePlayer(Player)
     */
    public static boolean hidePlayer(final Player player) {
        return Dynmap.hasDynmap() && Dynmap.instance._hidePlayer(player);
    }

    /**
     * The Dynmap API
     */
    private final DynmapCommonAPI api;

    /**
     * Builds a Dynmap bridge.
     */
    public Dynmap() {
        this.api = (DynmapCommonAPI)Bukkit.getPluginManager().getPlugin(DYNMAP_PLUGIN_NAME);
    }

    /**
     * Checks if Dynmap exists.
     *
     * @return true if Dynmap exists, false otherwise
     */
    private boolean _hasDynmap() {
        return this.api != null;
    }

    /**
     * Shows the provided Player on Dynmap.
     *
     * @param player the Player
     *
     * @return true if Dynmap exists, false otherwise
     */
    private boolean _showPlayer(final Player player) {
        return this.setPlayerVisibility(player.getName(), true);
    }

    /**
     * Hides the provided Player on Dynmap.
     *
     * @param player the Player
     *
     * @return true if Dynmap exists, false otherwise
     */
    private boolean _hidePlayer(final Player player) {
        return this.setPlayerVisibility(player.getName(), false);
    }

    /**
     * Sets the provided Player's visibility on Dynmap.
     *
     * @param playerName the name of the Player
     * @param visible    the new visible state of the Player
     *
     * @return true if Dynmap exists, false otherwise
     */
    private boolean setPlayerVisibility(final String playerName, final boolean visible) {
        if (this._hasDynmap()) {
            this.api.setPlayerVisiblity(playerName, visible);
        }
        return this._hasDynmap();
    }
}

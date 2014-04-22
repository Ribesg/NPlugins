/***************************************************************************
 * Project file:    NPlugins - NCore - AsyncPermAccessor.java              *
 * Full Class name: fr.ribesg.bukkit.ncore.util.AsyncPermAccessor          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This tool allow Asynchronous access to a Player's permission.
 * The {@link #init(org.bukkit.plugin.Plugin, int)} method should be
 * called by at least one Plugin synchronously before any sync or async
 * access.
 * <b>
 * Note: There is a maximum of {@link #updateDelay} seconds delay between
 * this tool's state and the reality. This tool should not be used for
 * critical checks.
 * </b>
 *
 * @author Ribesg
 */
public final class AsyncPermAccessor {

	// ########################### //
	// ## Public static methods ## //
	// ########################### //

	/**
	 * Checks if a Player had a Permission on the last update.
	 *
	 * @param playerName     the player's name
	 * @param permissionNode the permission to check
	 *
	 * @return true if the provided Player had the provided Permission on
	 * the last update, false otherwise
	 */
	public static boolean has(final String playerName, final String permissionNode) {
		checkState();
		return instance._has(playerName, permissionNode);
	}

	/**
	 * Checks if a Player was Op on the last update.
	 *
	 * @param playerName the player's name
	 *
	 * @return true if the provided Player was Op on the last update,
	 * false otherwise
	 */
	public static boolean isOp(final String playerName) {
		checkState();
		return instance._isOp(playerName);
	}

	/**
	 * Create the unique instance.
	 * <p>
	 * Should be called sync.
	 *
	 * @param updateDelay the delay between updates, in seconds.
	 *                    The lower, the heaviest.
	 */
	public static void init(final Plugin plugin, final int updateDelay) {
		if (instance == null) {
			instance = new AsyncPermAccessor(plugin, updateDelay);
		}
	}

	// ################### //
	// ## Static object ## //
	// ################### //

	/**
	 * The unique instance of this tool.
	 */
	private static AsyncPermAccessor instance;

	/**
	 * Checks if the tool has been initialized.
	 */
	private static void checkState() {
		if (instance == null) {
			throw new IllegalStateException("AsyncPermAccessor has not been initialized");
		}
	}

	// ######################## //
	// ## Non-static content ## //
	// ######################## //

	/**
	 * Will store the permissions per player. Sets will be backed by
	 * Concurrent maps.
	 */
	private final ConcurrentMap<String, Set<String>> permissions;

	/**
	 * Will store all connected op players. This Set will be backed by
	 * a Concurrent map.
	 */
	private final Set<String> ops;

	/**
	 * The plugin on which the task is attached.
	 */
	private final Plugin plugin;

	/**
	 * The update rate, in seconds.
	 */
	private final int updateDelay;

	/**
	 * Construct the AsyncPermAccessor.
	 *
	 * @param plugin      the plugin on which the taks will be attached
	 * @param updateDelay the update rate, in seconds
	 */
	private AsyncPermAccessor(final Plugin plugin, final int updateDelay) {
		this.permissions = new ConcurrentHashMap<>();
		this.ops = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
		this.plugin = plugin;
		this.updateDelay = updateDelay;

		update();
		launchUpdateTask();
	}

	/**
	 * @see #has(String, String)
	 */
	private boolean _has(final String playerName, final String permissionNode) {
		final Set<String> playerPerms = this.permissions.get(playerName);
		return playerPerms != null && playerPerms.contains(permissionNode);
	}

	/**
	 * @see #isOp(String)
	 */
	private boolean _isOp(final String playerName) {
		return this.ops.contains(playerName);
	}

	/**
	 * Update all permissions and op state of all connected players.
	 */
	private void update() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			updatePlayer(player);
		}
	}

	/**
	 * Update all permissions and op state of the provided player.
	 */
	private void updatePlayer(final Player player) {
		final String playerName = player.getName();
		if (player.isOp()) {
			this.ops.add(playerName);
		} else {
			this.ops.remove(playerName);
		}
		Set<String> playerPerms = this.permissions.get(playerName);
		if (playerPerms == null) {
			playerPerms = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
		} else {
			playerPerms.clear();
		}
		for (final PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
			if (perm.getValue()) {
				playerPerms.add(perm.getPermission());
			}
		}
		this.permissions.put(playerName, playerPerms);
	}

	/**
	 * Launch the update task.
	 */
	private void launchUpdateTask() {
		final long tickDelay = this.updateDelay * 20L;
		Bukkit.getScheduler().runTaskTimer(this.plugin, new BukkitRunnable() {

			@Override
			public void run() {
				AsyncPermAccessor.this.update();
			}
		}, tickDelay, tickDelay);
	}

}

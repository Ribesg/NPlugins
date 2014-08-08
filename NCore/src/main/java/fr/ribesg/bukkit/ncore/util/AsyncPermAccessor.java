/***************************************************************************
 * Project file:    NPlugins - NCore - AsyncPermAccessor.java              *
 * Full Class name: fr.ribesg.bukkit.ncore.util.AsyncPermAccessor          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * This tool allow Asynchronous access to a Player's permission.
 * The {@link #init(Plugin)} method should be
 * called by at least one Plugin synchronously before any sync or async
 * access.
 * <p>
 * Note: There is a delay between this tool's state and the reality. This
 * tool should not be used for critical checks.
 *
 * @author Ribesg
 */
public final class AsyncPermAccessor implements Listener {

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
        UPDATE_LOCK.readLock().lock();
        try {
            return instance._has(playerName, permissionNode);
        } finally {
            UPDATE_LOCK.readLock().unlock();
        }
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
        UPDATE_LOCK.readLock().lock();
        try {
            return instance._isOp(playerName);
        } finally {
            UPDATE_LOCK.readLock().unlock();
        }
    }

    /**
     * Create the unique instance.
     * <p>
     * Should be called sync.
     *
     * @param plugin the plugin on which the taks will be attached
     */
    public static void init(final Plugin plugin) {
        if (instance == null) {
            instance = new AsyncPermAccessor(plugin);
        } else {
            instance.addPlugin(plugin);
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
     * A lock
     */
    private static final ReentrantReadWriteLock UPDATE_LOCK = new ReentrantReadWriteLock();

    /**
     * Checks if the tool has been initialized.
     */
    private static void checkState() {
        if (instance == null) {
            throw new IllegalStateException("AsyncPermAccessor has not been initialized");
        } else {
            if (!instance.plugin.isEnabled()) {
                UPDATE_LOCK.writeLock().lock();
                try {
                    instance.plugins.remove(instance.plugin);
                    instance.task.cancel();
                    try {
                        final Set<Plugin> plugins = instance.plugins;
                        instance.plugin = instance.plugins.iterator().next();
                        instance = new AsyncPermAccessor(instance.plugin);
                        instance.plugins.addAll(plugins);
                        synchronized (UPDATE_LOCK) {
                            try {
                                UPDATE_LOCK.wait();
                            } catch (final InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (final NoSuchElementException e) {
                        instance = null;
                    }
                    if (instance == null || instance.permissions.isEmpty()) {
                        throw new IllegalStateException("AsyncPermAccessor has an invalid state");
                    }
                } finally {
                    UPDATE_LOCK.writeLock().unlock();
                }
            }
        }
    }

    // ######################## //
    // ## Non-static content ## //
    // ######################## //

    /**
     * Stores permissions per player, sets backed by Concurrent maps
     */
    private final Map<String, Set<String>> permissions;

    /**
     * Stores all connected Ops, backed by a ConcurrentMap
     */
    private final Set<String> ops;

    /**
     * The plugin on which the task is attached
     */
    private Plugin plugin;

    /**
     * Set of plugins using this tool
     */
    private final Set<Plugin> plugins;

    /**
     * Set of online players
     */
    private final Set<Player> players;

    /**
     * Amount of online players
     */
    private int playerCount;

    /**
     * The updating task
     */
    private final BukkitTask task;

    /**
     * Construct the AsyncPermAccessor.
     *
     * @param plugin the plugin on which the task will be attached
     */
    private AsyncPermAccessor(final Plugin plugin) {
        this.permissions = new HashMap<>();
        this.ops = new HashSet<>();
        this.plugin = plugin;
        this.plugins = new HashSet<>();
        this.plugins.add(this.plugin);
        this.players = new ConcurrentSkipListSet<>(new Comparator<Player>() {

            @Override
            public int compare(final Player a, final Player b) {
                return a.getName().compareTo(b.getName());
            }
        });
        Collections.addAll(this.players, Bukkit.getOnlinePlayers());
        this.playerCount = this.players.size();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        this.task = this.launchUpdateTask();
    }

    /**
     * Checks if a Player had a Permission on the last update.
     *
     * @param playerName     the player's name
     * @param permissionNode the permission to check
     *
     * @return true if the provided Player had the provided Permission on
     * the last update, false otherwise
     *
     * @see #has(String, String)
     */
    private boolean _has(final String playerName, final String permissionNode) {
        final Set<String> playerPerms = this.permissions.get(playerName);
        return playerPerms != null && playerPerms.contains(permissionNode);
    }

    /**
     * Checks if a Player was Op on the last update.
     *
     * @param playerName the player's name
     *
     * @return true if the provided Player was Op on the last update,
     * false otherwise
     *
     * @see #isOp(String)
     */
    private boolean _isOp(final String playerName) {
        return this.ops.contains(playerName);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        this.players.add(player);
        this.updatePlayer(player);
        this.playerCount++;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this.forgetPlayer(player);
        this.playerCount--;
    }

    /**
     * Adds a plugin to the list of plugins relying on this tool.
     *
     * @param plugin the plugin
     */
    private void addPlugin(final Plugin plugin) {
        this.plugins.add(plugin);
    }

    /**
     * Update all permissions and op state of all connected players.
     */
    private void update() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.updatePlayer(player);
        }
    }

    /**
     * Update all permissions and op state of the provided player.
     *
     * @param player the player
     */
    private void updatePlayer(final Player player) {
        if (player.isOnline()) {
            final String playerName = player.getName();
            if (player.isOp()) {
                this.ops.add(playerName);
            } else {
                this.ops.remove(playerName);
            }
            Set<String> playerPerms = this.permissions.get(playerName);
            if (playerPerms == null) {
                playerPerms = new HashSet<>();
            } else {
                playerPerms.clear();
            }
            for (final PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
                if (perm.getValue()) {
                    playerPerms.add(perm.getPermission());
                }
            }
            this.permissions.put(playerName, playerPerms);
        } else {
            this.forgetPlayer(player);
        }
    }

    /**
     * Forgets about a Player.
     *
     * @param player the player
     */
    private void forgetPlayer(final Player player) {
        final String playerName = player.getName();
        this.players.remove(player);
        this.ops.remove(playerName);
        this.permissions.remove(playerName);
    }

    /**
     * Launch the update task.
     *
     * @return the task
     */
    private BukkitTask launchUpdateTask() {
        final int delay = 2;
        return new BukkitRunnable() {

            private Iterator<Player> it = AsyncPermAccessor.this.players.iterator();
            private boolean firstRun = true;

            @Override
            public void run() {
                UPDATE_LOCK.writeLock().lock();
                try {
                    if (this.firstRun) {
                        this.firstRun = false;
                        AsyncPermAccessor.this.update();
                        synchronized (UPDATE_LOCK) {
                            UPDATE_LOCK.notifyAll();
                        }
                    } else {
                        int i = 0;
                        while (i++ < (AsyncPermAccessor.this.playerCount == 0 ? 0 : 1 + fr.ribesg.bukkit.ncore.util.AsyncPermAccessor.this.playerCount / (5 * 20L / delay))) {
                            if (!this.it.hasNext()) {
                                this.it = AsyncPermAccessor.this.players.iterator();
                            }
                            AsyncPermAccessor.this.updatePlayer(this.it.next());
                        }
                    }
                } finally {
                    UPDATE_LOCK.writeLock().unlock();
                }
            }
        }.runTaskTimer(this.plugin, 20L, delay);
    }
}

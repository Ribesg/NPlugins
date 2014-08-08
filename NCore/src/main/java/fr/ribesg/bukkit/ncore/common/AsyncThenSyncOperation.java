/***************************************************************************
 * Project file:    NPlugins - NCore - AsyncThenSyncOperation.java         *
 * Full Class name: fr.ribesg.bukkit.ncore.common.AsyncThenSyncOperation   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Represents an operation composed of an asynchronous part and
 * a synchronous part. The latter will be executed only after the
 * former completed its execution.
 */
public abstract class AsyncThenSyncOperation {

    private final Plugin  plugin;
    private       boolean ran;

    /**
     * Builds an AsyncThenSyncOperation.
     * <p>
     * Call {@link #run()} to launch the operation.
     *
     * @param plugin the plugin instance to link BukkitRunnables to
     */
    public AsyncThenSyncOperation(final Plugin plugin) {
        this(plugin, false);
    }

    /**
     * Builds an AsyncThenSyncOperation.
     * <p>
     * If you set the runNow parameter to false, call {@link #run()} later to
     * launch the operation.
     *
     * @param plugin the plugin instance to link BukkitRunnables to
     * @param runNow if the constructor should call {@link #run()} itself or
     *               not
     */
    public AsyncThenSyncOperation(final Plugin plugin, final boolean runNow) {
        this.plugin = plugin;
        this.ran = false;
        if (runNow) {
            this.run();
        }
    }

    /**
     * Actually runs the Operation. Can't be called multiple times.
     */
    public void run() {
        if (this.ran) {
            throw new IllegalStateException("Can only run once.");
        }
        this.ran = true;
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new BukkitRunnable() {
            @Override
            public void run() {
                fr.ribesg.bukkit.ncore.common.AsyncThenSyncOperation.this.execAsyncFirst();
                Bukkit.getScheduler().runTask(fr.ribesg.bukkit.ncore.common.AsyncThenSyncOperation.this.plugin, new BukkitRunnable() {
                    @Override
                    public void run() {
                        fr.ribesg.bukkit.ncore.common.AsyncThenSyncOperation.this.execSyncThen();
                    }
                });
            }
        });
    }

    /**
     * This method will be executed on a different thread than the main one.
     * It should be used to do some blocking network call, and other
     * heavy/long/slow things not requiring the Bukkit API.
     */
    protected abstract void execAsyncFirst();

    /**
     * This method will be executed on the main thread only after
     * {@link #execAsyncFirst()} has completed.
     * It should be used to use data computed/acquired in the Async part
     * of this operation.
     */
    protected abstract void execSyncThen();
}

/***************************************************************************
 * Project file:    NPlugins - NGeneral - AutoAfkTask.java                 *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.autoafk.AutoAfkTask  *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.autoafk;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoAfkTask extends BukkitRunnable {

    private final AutoAfkFeature feature;
    private final int            delayMillis;

    public AutoAfkTask(final AutoAfkFeature feature) {
        super();
        this.feature = feature;
        this.delayMillis = feature.getPlugin().getPluginConfig().getAutoAfkDelay() * 1000;
    }

    @Override
    public void run() {
        final Iterator<Map.Entry<String, Long>> it = this.feature.getLastUpdateMap().entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, Long> e = it.next();
            final String playerName = e.getKey();
            final long lastUpdate = e.getValue();
            final Player player = Bukkit.getPlayerExact(playerName);
            if (player == null || !player.isOnline()) {
                it.remove();
            } else {
                if (lastUpdate + this.delayMillis < System.currentTimeMillis()) {
                    final String playerListName = player.getPlayerListName();
                    if (playerName.startsWith(playerListName)) {
                        // Not BUSY, not already AFK
                        Bukkit.getScheduler().callSyncMethod(this.feature.getPlugin(), new Callable() {

                            @Override
                            public Object call() throws Exception {
                                fr.ribesg.bukkit.ngeneral.feature.autoafk.AutoAfkTask.this.feature.setAfk(playerName, true, null);
                                return null;
                            }
                        });
                    }
                }
            }
        }
    }
}

/***************************************************************************
 * Project file:    NPlugins - NGeneral - FlyModeListener.java             *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeListener
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.flymode;

import fr.ribesg.bukkit.ngeneral.Perms;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyModeListener implements Listener {

    private final FlyModeFeature feature;

    public FlyModeListener(final FlyModeFeature feature) {
        this.feature = feature;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (this.feature.hasFlyMode(event.getPlayer())) {
            event.getPlayer().setAllowFlight(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (!Perms.hasFly(event.getPlayer()) && this.feature.hasFlyMode(event.getPlayer())) {
            event.getPlayer().setAllowFlight(false);
            this.feature.setFlyMode(event.getPlayer(), false);
            event.getPlayer().setFallDistance(-100f);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitCreative(final PlayerGameModeChangeEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            final boolean wasFlying = event.getPlayer().isFlying();
            final Player player = event.getPlayer();
            if (Perms.hasFly(event.getPlayer()) && this.feature.hasFlyMode(event.getPlayer())) {
                Bukkit.getScheduler().runTaskLater(this.feature.getPlugin(), new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (player.isOnline()) {
                            player.setAllowFlight(true);
                            player.setFlying(wasFlying);
                        }
                    }
                }, 1L);
            }
        }
    }
}

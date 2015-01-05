/***************************************************************************
 * Project file:    NPlugins - NGeneral - SpyModeFeature.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.spymode.SpyModeFeature
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.spymode;

import fr.ribesg.bukkit.ncore.common.Dynmap;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.Feature;
import fr.ribesg.bukkit.ngeneral.feature.FeatureType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpyModeFeature extends Feature {

    private final Map<UUID, NLocation> spyPlayers;

    public SpyModeFeature(final NGeneral instance) {
        super(instance, FeatureType.SPY_MODE, instance.getPluginConfig().hasSpyModeFeature());
        this.spyPlayers = new HashMap<>();
    }

    @Override
    public void initialize() {
        final SpyModeListener listener = new SpyModeListener(this);
        final SpyModeCommandExecutor executor = new SpyModeCommandExecutor(this);

        Dynmap.init();

        Bukkit.getPluginManager().registerEvents(listener, this.plugin);
        this.plugin.setCommandExecutor("spy", executor);
    }

    public Map<UUID, NLocation> getSpyPlayers() {
        return this.spyPlayers;
    }

    public boolean hasSpyMode(final UUID playerId) {
        return this.spyPlayers.containsKey(playerId);
    }

    public void setSpyMode(final Player spy, final Player spied) {
        this.spyPlayers.put(spy.getUniqueId(), spied == null ? null : new NLocation(spy.getLocation()));
        for (final Player other : Bukkit.getOnlinePlayers()) {
            if (!Perms.hasSpy(other)) {
                other.hidePlayer(spy);
            }
        }
        Dynmap.hidePlayer(spy);
        if (spied != null) {
            Bukkit.getScheduler().runTaskLater(this.getPlugin(), new BukkitRunnable() {

                @Override
                public void run() {
                    spy.teleport(spied.getLocation());
                }
            }, 1L);
        }
    }

    public void unSetSpyMode(final Player spy) {
        final NLocation previousLocation = this.spyPlayers.remove(spy.getUniqueId());
        if (previousLocation != null) {
            spy.teleport(previousLocation.toBukkitLocation());
        }
        Bukkit.getScheduler().runTaskLater(this.getPlugin(), new BukkitRunnable() {

            @Override
            public void run() {
                final CuboidNode cuboid = fr.ribesg.bukkit.ngeneral.feature.spymode.SpyModeFeature.this.getPlugin().getCore().getCuboidNode();
                if (cuboid == null || !cuboid.isInInvisibleRegion(spy)) {
                    for (final Player other : Bukkit.getOnlinePlayers()) {
                        other.showPlayer(spy);
                    }
                }
                Dynmap.showPlayer(spy);
            }
        }, 1L);
    }
}

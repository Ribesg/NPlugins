/***************************************************************************
 * Project file:    NPlugins - NGeneral - GodModeFeature.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeFeature
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.godmode;

import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.Feature;
import fr.ribesg.bukkit.ngeneral.feature.FeatureType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GodModeFeature extends Feature {

    private final Set<UUID> godPlayers;

    public GodModeFeature(final NGeneral instance) {
        super(instance, FeatureType.GOD_MODE, instance.getPluginConfig().hasGodModeFeature());
        this.godPlayers = new HashSet<>();
    }

    @Override
    public void initialize() {
        final GodModeListener listener = new GodModeListener(this);
        final GodModeCommandExecutor executor = new GodModeCommandExecutor(this);

        Bukkit.getPluginManager().registerEvents(listener, this.plugin);
        this.plugin.setCommandExecutor("god", executor);
    }

    public Set<UUID> getGodPlayers() {
        return this.godPlayers;
    }

    public boolean hasGodMode(final Player player) {
        return this.godPlayers.contains(player.getUniqueId());
    }

    public void setGodMode(final Player player, final boolean value) {
        if (value) {
            this.godPlayers.add(player.getUniqueId());
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20.0f);
        } else {
            this.godPlayers.remove(player.getUniqueId());
        }
    }
}

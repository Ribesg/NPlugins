/***************************************************************************
 * Project file:    NPlugins - NGeneral - GodModeListener.java             *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.godmode;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GodModeListener implements Listener {

	private final GodModeFeature feature;

	public GodModeListener(final GodModeFeature feature) {
		this.feature = feature;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDamage(final EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			final Player player = (Player) event.getEntity();
			if (feature.hasGodMode(player)) {
				event.setCancelled(true);
				if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
					player.setFireTicks(0);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerStarve(final FoodLevelChangeEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			final Player p = (Player) event.getEntity();
			event.setCancelled(p.getFoodLevel() > event.getFoodLevel() && feature.hasGodMode(p));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		if (!Perms.hasGod(event.getPlayer()) && feature.hasGodMode(event.getPlayer())) {
			feature.setGodMode(event.getPlayer(), false);
		}
	}
}

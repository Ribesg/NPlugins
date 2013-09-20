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

	public GodModeListener(GodModeFeature feature) {
		this.feature = feature;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			final Player player = (Player) event.getEntity();
			if (feature.hasGodMode(player.getName())) {
				event.setCancelled(true);
				if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
					player.setFireTicks(0);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerStarve(FoodLevelChangeEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			final Player p = (Player) event.getEntity();
			event.setCancelled(p.getFoodLevel() > event.getFoodLevel() && feature.hasGodMode(p.getName()));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (!Perms.hasGod(event.getPlayer()) && feature.hasGodMode(event.getPlayer().getName())) {
			feature.setGodMode(event.getPlayer(), false);
		}
	}
}

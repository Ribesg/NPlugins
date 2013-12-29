/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - DamageListener.java          *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.listener.DamageListener  *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.listener;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Handles Damage events
 *
 * @author Ribesg
 */
public class DamageListener implements Listener {

	private final static Random rand = new Random();

	private final NTheEndAgain plugin;

	public DamageListener(final NTheEndAgain instance) {
		plugin = instance;
	}

	/**
	 * Player => EnderDragon:
	 * - Counts damages done to EnderDragons by players or by projectiles thrown by players
	 * <p/>
	 * EnderDragon => Player:
	 * - Handles Damage Multiplier
	 * - Simulates EnderDragon pushing Player behaviour
	 *
	 * @param event an EntityDamageByEntityEvent
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
		// EnderDragon damaged by Player
		if (event.getEntityType() == EntityType.ENDER_DRAGON) {
			final Player player;
			if (event.getDamager().getType() == EntityType.PLAYER) {
				player = (Player) event.getDamager();
			} else if (event.getDamager() instanceof Projectile &&
			           ((Projectile) event.getDamager()).getShooter().getType() == EntityType.PLAYER) {
				player = (Player) ((Projectile) event.getDamager()).getShooter();
			} else {
				// Not caused by a Player
				return;
			}
			final EnderDragon dragon = (EnderDragon) event.getEntity();
			final World endWorld = dragon.getWorld();
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(endWorld.getName()));
			if (handler != null) {
				handler.playerHitED(dragon.getUniqueId(), player.getName(), event.getDamage());
			}
		}

		// Player damaged by EnderDragon
		else if (event.getEntityType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.ENDER_DRAGON) {
			final World endWorld = event.getEntity().getWorld();
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(endWorld.getName()));
			if (handler != null) {
				event.setDamage(Math.round(event.getDamage() * handler.getConfig().getEdDamageMultiplier()));
				if (handler.getConfig().getEdPushesPlayers() == 1) {
					// Simulate ED pushing player
					final Vector velocity = event.getDamager().getLocation().toVector();
					velocity.subtract(event.getEntity().getLocation().toVector());
					velocity.normalize().multiply(-1);
					if (velocity.getY() < 0.05f) {
						velocity.setY(0.05f);
					}
					if (rand.nextFloat() < 0.025f) {
						velocity.setY(10);
					}
					velocity.normalize().multiply(1.75f);
					Bukkit.getScheduler().runTask(plugin, new BukkitRunnable() {

						@Override
						public void run() {
							event.getEntity().setVelocity(velocity);
						}
					});
				}
			}
		}
	}
}

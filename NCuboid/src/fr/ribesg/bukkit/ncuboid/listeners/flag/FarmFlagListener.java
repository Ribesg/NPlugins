package fr.ribesg.bukkit.ncuboid.listeners.flag;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

public class FarmFlagListener extends AbstractListener {

    private static Set<EntityType> animals;

    private static Set<EntityType> getAnimals() {
        if (animals == null) {
            animals = new HashSet<EntityType>();
            animals.add(EntityType.BAT);
            animals.add(EntityType.CHICKEN);
            animals.add(EntityType.COW);
            animals.add(EntityType.IRON_GOLEM);
            animals.add(EntityType.MUSHROOM_COW);
            animals.add(EntityType.OCELOT);
            animals.add(EntityType.PIG);
            animals.add(EntityType.SHEEP);
            animals.add(EntityType.SNOWMAN);
            animals.add(EntityType.SQUID);
            animals.add(EntityType.VILLAGER);
            animals.add(EntityType.WOLF);
        }
        return animals;
    }

    public FarmFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamageByEntity(final ExtendedEntityDamageEvent ext) {
        if (ext.getBaseEvent() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ext.getBaseEvent();
            Player p;
            if (event.getDamager().getType() == EntityType.PLAYER) {
                p = (Player) event.getDamager();
            } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter().getType() == EntityType.PLAYER) {
                p = (Player) ((Projectile) event.getDamager()).getShooter();
            } else {
                return;
            }
            if (getAnimals().contains(event.getEntityType()) && ext.getEntityCuboid() != null && ext.getEntityCuboid().getFlag(Flag.FARM) && !ext.getEntityCuboid().isAllowedPlayer(p)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractEvent(final ExtendedPlayerInteractEvent ext) {
        final PlayerInteractEvent event = (PlayerInteractEvent) ext.getBaseEvent();
        if (event.getAction() == Action.PHYSICAL) {
            if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.FARM) && !ext.getCuboid().isAllowedPlayer(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerShearEntity(final PlayerShearEntityEvent event) {
        final PlayerCuboid cuboid = getPlugin().getDb().getPriorByLoc(event.getEntity().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.FARM) && !cuboid.isAllowedPlayer(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

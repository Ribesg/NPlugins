/***************************************************************************
 * Project file:    NPlugins - NCuboid - UseFlagListener.java              *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.UseFlagListener*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEntityEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseFlagListener extends AbstractListener {

    private static Set<EntityType> denyUseEntities;
    private static Set<Material>   denyUseMaterials;

    private static Set<EntityType> getDenyUseEntity() {
        if (denyUseEntities == null) {
            denyUseEntities = EnumSet.of(
                    EntityType.BOAT,
                    EntityType.ITEM_FRAME,
                    EntityType.MINECART,
                    EntityType.MINECART_COMMAND,
                    EntityType.MINECART_FURNACE
            );
        }
        return denyUseEntities;
    }

    private static Set<Material> getDenyUseMaterials() {
        if (denyUseMaterials == null) {
            denyUseMaterials = EnumSet.of(
                    Material.ANVIL,
                    Material.BED_BLOCK,
                    Material.CAKE_BLOCK,
                    Material.CAULDRON,
                    Material.COMMAND,
                    Material.DRAGON_EGG,
                    Material.ENCHANTMENT_TABLE,
                    Material.ENDER_CHEST,
                    Material.ENDER_PORTAL_FRAME,
                    Material.FENCE_GATE,
                    Material.FLOWER_POT,
                    Material.GLOWING_REDSTONE_ORE,
                    Material.GOLD_PLATE,
                    Material.IRON_PLATE,
                    Material.JUKEBOX,
                    Material.LEVER,
                    Material.NOTE_BLOCK,
                    Material.REDSTONE_ORE,
                    Material.SIGN_POST,
                    Material.SOIL,
                    Material.STONE_BUTTON,
                    Material.STONE_PLATE,
                    Material.TRAP_DOOR,
                    Material.WALL_SIGN,
                    Material.WOOD_BUTTON,
                    Material.WOOD_PLATE,
                    Material.WOODEN_DOOR,
                    Material.WORKBENCH
            );
        }
        return denyUseMaterials;
    }

    public UseFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
        final PlayerInteractEvent event = (PlayerInteractEvent)ext.getBaseEvent();
        if (event.hasBlock()) {
            if (ext.getClickedRegion() != null &&
                ext.getClickedRegion().getFlag(Flag.USE) &&
                !ext.getClickedRegion().isUser(event.getPlayer()) &&
                getDenyUseMaterials().contains(event.getClickedBlock().getType())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractEntity(final ExtendedPlayerInteractEntityEvent ext) {
        final PlayerInteractEntityEvent event = (PlayerInteractEntityEvent)ext.getBaseEvent();
        if (ext.getRegion() != null &&
            ext.getRegion().getFlag(Flag.USE) &&
            !ext.getRegion().isUser(event.getPlayer()) &&
            getDenyUseEntity().contains(event.getRightClicked().getType())) {
            event.setCancelled(true);
        }
    }
}

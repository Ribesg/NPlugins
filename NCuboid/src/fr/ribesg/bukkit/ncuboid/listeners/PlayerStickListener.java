package fr.ribesg.bukkit.ncuboid.listeners;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid.CuboidState;
import fr.ribesg.bukkit.ncuboid.beans.RectCuboid;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.lang.Messages;

public class PlayerStickListener extends AbstractListener {

    public PlayerStickListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
        final PlayerInteractEvent event = (PlayerInteractEvent) ext.getBaseEvent();
        if (event.hasItem() && event.getItem().getType() == Material.STICK) {
            final Action action = event.getAction();
            final Player p = event.getPlayer();
            if (event.hasBlock()) {
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    // Selection tool
                    final RectCuboid selection = (RectCuboid) getPlugin().getDb().getTmp(p.getName());
                    final Location clickedBlockLocation = event.getClickedBlock().getLocation();
                    if (selection == null) {
                        getPlugin().getDb().addTmp(new RectCuboid("tmp" + p.getName(), p.getName(), clickedBlockLocation.getWorld(), clickedBlockLocation));
                        getPlugin().sendMessage(p, MessageId.cuboid_firstPointSelected, Utils.toString(clickedBlockLocation));
                    } else if (selection.getState() == CuboidState.TMPSTATE1) {
                        selection.secondPoint(clickedBlockLocation);
                        getPlugin().sendMessage(p, MessageId.cuboid_secondPointSelected, Utils.toString(clickedBlockLocation), selection.getSizeString());
                    } else if (selection.getState() == CuboidState.TMPSTATE2) {
                        if (selection.contains(clickedBlockLocation)) {
                            getPlugin().sendMessage(p, MessageId.cuboid_blockInSelection);
                        } else {
                            getPlugin().sendMessage(p, MessageId.cuboid_blockNotInSelection);
                        }
                    }
                } else { // Action.LEFT_CLICK_BLOCK
                    // Info tool
                    final Set<PlayerCuboid> cuboids = ext.getCuboids();
                    if (cuboids == null || cuboids.size() == 0) {
                        getPlugin().sendMessage(p, MessageId.cuboid_blockNotProtected);
                    } else if (cuboids.size() == 1) {
                        final PlayerCuboid cuboid = cuboids.iterator().next();
                        getPlugin().sendMessage(p, MessageId.cuboid_blockProtectedOneCuboid, cuboid.getInfoLine());
                    } else {
                        final String[] strings = new String[cuboids.size()];
                        int i = 0;
                        for (final PlayerCuboid c : cuboids) {
                            strings[i++] = c.getInfoLine();
                        }
                        Arrays.sort(strings);
                        getPlugin().sendMessage(p, MessageId.cuboid_blockProtectedMultipleCuboids, String.valueOf(strings.length), Messages.merge(strings));
                    }
                }
            }
            else if (action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR) {
                // Selection reset
                final PlayerCuboid deletedCuboid = getPlugin().getDb().delTmp(p.getName());
                if (deletedCuboid == null) {
                    getPlugin().sendMessage(p, MessageId.cuboid_noSelection);
                } else {
                    getPlugin().sendMessage(p, MessageId.cuboid_selectionReset);
                }
            } else {
                return;
            }
            event.setCancelled(true);
        }
    }
}

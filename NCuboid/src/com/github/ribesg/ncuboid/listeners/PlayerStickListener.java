package com.github.ribesg.ncuboid.listeners;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.ribesg.ncore.Utils;
import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.beans.PlayerCuboid.CuboidState;
import com.github.ribesg.ncuboid.beans.RectCuboid;
import com.github.ribesg.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import com.github.ribesg.ncuboid.lang.Messages;
import com.github.ribesg.ncuboid.lang.Messages.MessageId;

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
                    final RectCuboid selection = (RectCuboid) CuboidDB.getInstance().getTmp(p.getName());
                    final Location clickedBlockLocation = event.getClickedBlock().getLocation();
                    if (selection == null) {
                        CuboidDB.getInstance().addTmp(new RectCuboid("tmp" + p.getName(), p.getName(), clickedBlockLocation.getWorld(), clickedBlockLocation));
                        getPlugin().sendMessage(p, MessageId.firstPointSelected, Utils.toString(clickedBlockLocation));
                    } else if (selection.getState() == CuboidState.TMPSTATE1) {
                        selection.secondPoint(clickedBlockLocation);
                        getPlugin().sendMessage(p, MessageId.secondPointSelected, Utils.toString(clickedBlockLocation), selection.getSizeString());
                    } else if (selection.getState() == CuboidState.TMPSTATE2) {
                        if (selection.contains(clickedBlockLocation)) {
                            getPlugin().sendMessage(p, MessageId.blockInSelection);
                        } else {
                            getPlugin().sendMessage(p, MessageId.blockNotInSelection);
                        }
                    }
                } else { // Action.LEFT_CLICK_BLOCK
                    // Info tool
                    final Set<PlayerCuboid> cuboids = ext.getCuboids();
                    if (cuboids == null || cuboids.size() == 0) {
                        getPlugin().sendMessage(p, MessageId.blockNotProtected);
                    } else if (cuboids.size() == 1) {
                        final PlayerCuboid cuboid = cuboids.iterator().next();
                        getPlugin().sendMessage(p, MessageId.blockProtectedOneCuboid, cuboid.getInfoLine());
                    } else {
                        final String[] strings = new String[cuboids.size()];
                        int i = 0;
                        for (final PlayerCuboid c : cuboids) {
                            strings[i++] = c.getInfoLine();
                        }
                        Arrays.sort(strings);
                        getPlugin().sendMessage(p, MessageId.blockProtectedMultipleCuboids, String.valueOf(strings.length), Messages.merge(strings));
                    }
                }
            }
            else if (action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR) {
                // Selection reset
                final PlayerCuboid deletedCuboid = CuboidDB.getInstance().delTmp(p.getName());
                if (deletedCuboid == null) {
                    getPlugin().sendMessage(p, MessageId.noSelection);
                } else {
                    getPlugin().sendMessage(p, MessageId.selectionReset);
                }
            } else {
                return;
            }
            event.setCancelled(true);
        }
    }
}

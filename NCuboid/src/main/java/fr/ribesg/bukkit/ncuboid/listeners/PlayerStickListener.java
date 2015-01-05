/***************************************************************************
 * Project file:    NPlugins - NCuboid - PlayerStickListener.java          *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.PlayerStickListener *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.CuboidRegion;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion.RegionType;
import fr.ribesg.bukkit.ncuboid.beans.PlayerRegion;
import fr.ribesg.bukkit.ncuboid.beans.PlayerRegion.RegionState;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.lang.Messages;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerStickListener extends AbstractListener {

    public PlayerStickListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
        final PlayerInteractEvent event = (PlayerInteractEvent)ext.getBaseEvent();
        if (event.hasItem() && event.getItem().getType() == Material.STICK) {
            final Action action = event.getAction();
            final Player p = event.getPlayer();
            if (event.hasBlock()) {
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    // Selection tool
                    final CuboidRegion selection = (CuboidRegion)this.getPlugin().getDb().getSelection(p.getUniqueId());
                    final Location clickedBlockLocation = event.getClickedBlock().getLocation();
                    if (selection == null) {
                        this.getPlugin().getDb().addSelection(new CuboidRegion("tmp" + p.getName(), p.getUniqueId(), clickedBlockLocation.getWorld().getName(), new NLocation(clickedBlockLocation)));
                        this.getPlugin().sendMessage(p, MessageId.cuboid_firstPointSelected, NLocation.toString(clickedBlockLocation));
                    } else if (selection.getState() == RegionState.TMPSTATE1) {
                        selection.secondPoint(clickedBlockLocation);
                        this.getPlugin().sendMessage(p, MessageId.cuboid_secondPointSelected, NLocation.toString(clickedBlockLocation), selection.getSizeString());
                    } else if (selection.getState() == RegionState.TMPSTATE2) {
                        if (selection.contains(clickedBlockLocation)) {
                            this.getPlugin().sendMessage(p, MessageId.cuboid_blockInSelection);
                        } else {
                            this.getPlugin().sendMessage(p, MessageId.cuboid_blockNotInSelection);
                        }
                    }
                } else { // Action.LEFT_CLICK_BLOCK
                    // Info tool
                    final Set<GeneralRegion> regions = ext.getClickedRegions();
                    if (regions == null ||
                        regions.isEmpty() ||
                        regions.size() == 1 && regions.iterator().next().getType() == RegionType.WORLD) {
                        this.getPlugin().sendMessage(p, MessageId.cuboid_blockNotProtected);
                    } else {
                        int size = regions.size();
                        boolean containsWorldRegion = false;
                        for (final GeneralRegion c : regions) {
                            if (c.getType() == RegionType.WORLD) {
                                size--;
                                containsWorldRegion = true;
                                break;
                            }
                        }
                        if (size == 1) {
                            final Iterator<GeneralRegion> it = regions.iterator();
                            GeneralRegion region = it.next();
                            if (region.getType() == RegionType.WORLD) {
                                region = it.next();
                            }
                            this.getPlugin().sendMessage(p, MessageId.cuboid_blockProtectedOneRegion, ((PlayerRegion)region).getInfoLine());
                        } else {
                            final String[] strings = new String[regions.size() - (containsWorldRegion ? 1 : 0)];
                            int i = 0;
                            for (final GeneralRegion region : regions) {
                                if (region.getType() != RegionType.WORLD) {
                                    strings[i++] = ((PlayerRegion)region).getInfoLine();
                                }
                            }
                            Arrays.sort(strings);
                            this.getPlugin().sendMessage(p, MessageId.cuboid_blockProtectedMultipleRegions, String.valueOf(strings.length), Messages.merge(strings));
                        }
                    }
                }
            } else if (action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR) {
                // Selection reset
                final PlayerRegion removedRegion = this.getPlugin().getDb().removeSelection(p.getUniqueId());
                if (removedRegion == null) {
                    this.getPlugin().sendMessage(p, MessageId.cuboid_noSelection);
                } else {
                    this.getPlugin().sendMessage(p, MessageId.cuboid_selectionReset);
                }
            } else {
                return;
            }
            event.setCancelled(true);
        }
    }
}

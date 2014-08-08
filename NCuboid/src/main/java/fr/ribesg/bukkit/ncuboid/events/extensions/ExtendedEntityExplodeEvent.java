/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedEntityExplodeEvent.java   *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityExplodeEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExtendedEntityExplodeEvent extends AbstractExtendedEvent {

    private final GeneralRegion             entityRegion;
    private final Map<Block, GeneralRegion> blockRegionsMap;

    public ExtendedEntityExplodeEvent(final RegionDb db, final EntityExplodeEvent event) {
        super(db.getPlugin(), event);
        this.blockRegionsMap = new HashMap<>();
        for (final Block b : event.blockList()) {
            final GeneralRegion cuboid = db.getPriorByLocation(b.getLocation());
            if (cuboid != null) {
                this.blockRegionsMap.put(b, cuboid);
            }
        }
        this.entityRegion = this.blockRegionsMap.get(event.getLocation().getBlock());
    }

    public Map<Block, GeneralRegion> getBlockRegionsMap() {
        return this.blockRegionsMap;
    }

    public GeneralRegion getEntityRegion() {
        return this.entityRegion;
    }
}

/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPlayerJoinEvent.java      *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerJoinEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

import java.util.Set;
import java.util.SortedSet;

import org.bukkit.event.player.PlayerJoinEvent;

public class ExtendedPlayerJoinEvent extends AbstractExtendedEvent {

    private final GeneralRegion            region;
    private final SortedSet<GeneralRegion> regions;

    public ExtendedPlayerJoinEvent(final RegionDb db, final PlayerJoinEvent event) {
        super(db.getPlugin(), event);
        this.regions = db.getAllByLocation(event.getPlayer().getLocation());
        this.region = db.getPrior(this.regions);
    }

    public GeneralRegion getRegion() {
        return this.region;
    }

    public Set<GeneralRegion> getRegions() {
        return this.regions;
    }
}

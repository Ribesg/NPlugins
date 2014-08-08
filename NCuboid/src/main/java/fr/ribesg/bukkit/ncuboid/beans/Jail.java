/***************************************************************************
 * Project file:    NPlugins - NCuboid - Jail.java                         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Jail                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;

public class Jail {

    private final String        name;
    private final NLocation     location;
    private final GeneralRegion region;

    public Jail(final String name, final NLocation location, final GeneralRegion region) {
        this.name = name;
        this.location = location;
        this.region = region;
    }

    public String getName() {
        return this.name;
    }

    public NLocation getLocation() {
        return this.location;
    }

    public GeneralRegion getRegion() {
        return this.region;
    }

    @Override
    public String toString() {
        return "Jail{" +
               "name='" + this.name + '\'' +
               ", location=" + this.location +
               ", region=" + this.region +
               '}';
    }
}

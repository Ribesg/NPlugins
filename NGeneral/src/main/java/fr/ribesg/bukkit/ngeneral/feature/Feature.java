/***************************************************************************
 * Project file:    NPlugins - NGeneral - Feature.java                     *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.Feature              *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature;

import fr.ribesg.bukkit.ngeneral.NGeneral;

/**
 * Represents an autonomous feature.
 */
public abstract class Feature {

    protected final NGeneral    plugin;
    protected final FeatureType type;
    protected final boolean     enabled;

    /**
     * Must not interact with Bukkit in any way
     */
    protected Feature(final NGeneral instance, final FeatureType type, final boolean enabled) {
        this.plugin = instance;
        this.type = type;
        this.enabled = enabled;
    }

    public NGeneral getPlugin() {
        return this.plugin;
    }

    public FeatureType getType() {
        return this.type;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Must be overridden by Features to start interacting with Bukkit.
     */
    public abstract void initialize();

    /**
     * Should be overridden by Features that have to do something
     * onDisable.
     */
    public void terminate() {
    }
}

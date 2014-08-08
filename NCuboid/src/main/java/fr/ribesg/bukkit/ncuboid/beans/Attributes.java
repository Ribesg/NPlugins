/***************************************************************************
 * Project file:    NPlugins - NCuboid - Attributes.java                   *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Attributes              *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;

import java.util.EnumMap;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Attributes {

    // Default map
    private static EnumMap<Attribute, Object> getDefaultAttributesMap() {
        final EnumMap<Attribute, Object> defaultAttributesMap = new EnumMap<>(Attribute.class);

        // We do not put anything in the map, we do not want to store useless objects / references

        return defaultAttributesMap;
    }

    private EnumMap<Attribute, Object> attributes;

    public Attributes() {
        this.attributes = null;
    }

    private void newMap() {
        this.attributes = getDefaultAttributesMap();
    }

    // Strings handling
    public String getStringAttribute(final Attribute att) {
        if (this.attributes == null) {
            return null;
        } else if (Attribute.isStringAttribute(att)) {
            return (String)this.attributes.get(att);
        } else {
            new IllegalArgumentException(att.name()).printStackTrace();
            return null;
        }
    }

    public void setStringAttribute(final Attribute att, final String s) {
        if (this.attributes == null) {
            this.newMap();
        }
        if (Attribute.isStringAttribute(att)) {
            this.attributes.put(att, s);
            this.checkStringAttributeCorrectness();
        } else {
            new IllegalArgumentException(att == null ? "null" : att.name()).printStackTrace();
        }
    }

    private void setStringAttributeNoCheck(final Attribute att, final String s) {
        this.attributes.put(att, s);
    }

    private void checkStringAttributeCorrectness() {
        // Nothing to do yet
    }

    // Integers handling
    public Integer getIntegerAttribute(final Attribute att) {
        if (this.attributes == null) {
            return null;
        } else if (Attribute.isIntegerAttribute(att)) {
            return (Integer)this.attributes.get(att);
        } else {
            new IllegalArgumentException(att.name()).printStackTrace();
            return null;
        }
    }

    public void setIntegerAttribute(final Attribute att, final Integer i) {
        if (this.attributes == null) {
            this.newMap();
        }
        if (Attribute.isIntegerAttribute(att)) {
            this.attributes.put(att, i);
            this.checkIntegerAttributeCorrectness();
        } else {
            new IllegalArgumentException(att == null ? "null" : att.name()).printStackTrace();
        }
    }

    private void setIntegerAttributeNoCheck(final Attribute att, final Integer i) {
        this.attributes.put(att, i);
    }

    private void checkIntegerAttributeCorrectness() {
        if (this.getIntegerAttribute(Attribute.HEAL_TIMER) != null && this.getIntegerAttribute(Attribute.HEAL_TIMER) < 5) {
            this.setIntegerAttributeNoCheck(Attribute.HEAL_TIMER, 5);
        }
        if (this.getIntegerAttribute(Attribute.FEED_TIMER) != null && this.getIntegerAttribute(Attribute.FEED_TIMER) < 5) {
            this.setIntegerAttributeNoCheck(Attribute.FEED_TIMER, 5);
        }
        if (this.getIntegerAttribute(Attribute.HEAL_AMOUNT) != null && this.getIntegerAttribute(Attribute.HEAL_AMOUNT) < -20) {
            this.setIntegerAttributeNoCheck(Attribute.HEAL_AMOUNT, -20);
        } else if (this.getIntegerAttribute(Attribute.HEAL_AMOUNT) != null && this.getIntegerAttribute(Attribute.HEAL_AMOUNT) > 20) {
            this.setIntegerAttributeNoCheck(Attribute.HEAL_AMOUNT, 20);
        }
        if (this.getIntegerAttribute(Attribute.FEED_AMOUNT) != null && this.getIntegerAttribute(Attribute.FEED_AMOUNT) < -20) {
            this.setIntegerAttributeNoCheck(Attribute.FEED_AMOUNT, -20);
        } else if (this.getIntegerAttribute(Attribute.FEED_AMOUNT) != null && this.getIntegerAttribute(Attribute.FEED_AMOUNT) > 20) {
            this.setIntegerAttributeNoCheck(Attribute.FEED_AMOUNT, 20);
        }
        for (final Attribute att : new Attribute[]{
                Attribute.HEAL_MIN_HEALTH,
                Attribute.HEAL_MAX_HEALTH,
                Attribute.FEED_MIN_FOOD,
                Attribute.FEED_MAX_FOOD
        }) {
            if (this.getIntegerAttribute(att) != null && this.getIntegerAttribute(att) < 0) {
                this.setIntegerAttributeNoCheck(att, 0);
            } else if (this.getIntegerAttribute(att) != null && this.getIntegerAttribute(att) > 20) {
                this.setIntegerAttributeNoCheck(att, 20);
            }
        }
        if (this.getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) != null && this.getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) < 0) {
            this.setIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP, 0);
        } else if (this.getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) != null && this.getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) > 100) {
            this.setIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP, 100);
        }
    }

    // Locations handling
    public Location getLocationAttribute(final Attribute att) {
        if (this.attributes == null) {
            return null;
        } else if (Attribute.isLocationAttribute(att)) {
            return (Location)this.attributes.get(att);
        } else {
            new IllegalArgumentException(att.name()).printStackTrace();
            return null;
        }
    }

    public void setLocationAttribute(final Attribute att, final Location loc) {
        if (this.attributes == null) {
            this.newMap();
        }
        if (Attribute.isLocationAttribute(att)) {
            this.attributes.put(att, loc);
            this.checkLocationAttributeCorrectness();
        } else {
            new IllegalArgumentException(att.name()).printStackTrace();
        }
    }

    private void checkLocationAttributeCorrectness() {
        // Nothing to do yet
    }

    // Vectors handling
    public Vector getVectorAttribute(final Attribute att) {
        if (this.attributes == null) {
            return null;
        } else if (Attribute.isVectorAttribute(att)) {
            return (Vector)this.attributes.get(att);
        } else {
            new IllegalArgumentException(att.name()).printStackTrace();
            return null;
        }
    }

    public void setVectorAttribute(final Attribute att, final Vector v) {
        if (this.attributes == null) {
            this.newMap();
        }
        if (Attribute.isVectorAttribute(att)) {
            this.attributes.put(att, v);
            this.checkVectorAttributeCorrectness();
        } else {
            new IllegalArgumentException(att.name()).printStackTrace();
        }
    }

    private void checkVectorAttributeCorrectness() {
        if (this.getVectorAttribute(Attribute.BOOSTER_VECTOR) != null && this.getVectorAttribute(Attribute.BOOSTER_VECTOR).lengthSquared() > 100) {
            // XXX: Bukkit does not allow > 10 m/s Velocity
            this.setVectorAttribute(Attribute.BOOSTER_VECTOR, this.getVectorAttribute(Attribute.BOOSTER_VECTOR).normalize().multiply(9.95D));
        }
    }

    public String getStringRepresentation(final Attribute att) {
        if (Attribute.isStringAttribute(att)) {
            return this.getStringAttribute(att);
        } else if (Attribute.isIntegerAttribute(att)) {
            return Integer.toString(this.getIntegerAttribute(att));
        } else if (Attribute.isVectorAttribute(att)) {
            final Vector v = this.getVectorAttribute(att);
            return "<" + v.getX() + ';' + v.getY() + ';' + v.getZ() + '>';
        } else if (Attribute.isLocationAttribute(att)) {
            return NLocation.toStringPlus(this.getLocationAttribute(att));
        } else {
            throw new UnsupportedOperationException("Not yet implemented for " + att.name());
        }
    }
}

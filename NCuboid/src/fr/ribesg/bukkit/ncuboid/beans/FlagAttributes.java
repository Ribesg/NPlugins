package fr.ribesg.bukkit.ncuboid.beans;

import static fr.ribesg.bukkit.ncuboid.beans.FlagAtt.*;
import java.util.EnumMap;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class FlagAttributes {
    
    private EnumMap<FlagAtt, Object> atts;
    
    public FlagAttributes() {
        atts = null;
    }
    
    private void newMap() {
        atts = getDefaultFlagAttMap();
    }
    
    // Integers handling
    public Integer getIntFlagAtt(final FlagAtt f) {
        if (atts == null) {
            return null;
        } else if (isIntFlagAtt(f)) {
            return (Integer) atts.get(f);
        } else {
            new IllegalArgumentException(f.name()).printStackTrace();
            return null;
        }
    }
    
    public void setIntFlagAtt(final FlagAtt f, final Integer i) {
        if (atts == null) {
            newMap();
        }
        if (isIntFlagAtt(f)) {
            atts.put(f, i);
            checkIntFlagAttCorrection();
        } else {
            new IllegalArgumentException(f.name()).printStackTrace();
        }
    }
    
    private void setIntFlagAttNoCheck(final FlagAtt f, final Integer i) {
        atts.put(f, i);
    }
    
    private void checkIntFlagAttCorrection() {
        if (getIntFlagAtt(FlagAtt.HEAL_TIMER) != null && getIntFlagAtt(FlagAtt.HEAL_TIMER) < 5) {
            setIntFlagAttNoCheck(FlagAtt.HEAL_TIMER, 5);
        }
        if (getIntFlagAtt(FlagAtt.FEED_TIMER) != null && getIntFlagAtt(FlagAtt.FEED_TIMER) < 5) {
            setIntFlagAttNoCheck(FlagAtt.FEED_TIMER, 5);
        }
        if (getIntFlagAtt(FlagAtt.HEAL_AMOUNT) != null && getIntFlagAtt(FlagAtt.HEAL_AMOUNT) < -20) {
            setIntFlagAttNoCheck(FlagAtt.HEAL_AMOUNT, -20);
        } else if (getIntFlagAtt(FlagAtt.HEAL_AMOUNT) != null && getIntFlagAtt(FlagAtt.HEAL_AMOUNT) > 20) {
            setIntFlagAttNoCheck(FlagAtt.HEAL_AMOUNT, 20);
        }
        if (getIntFlagAtt(FlagAtt.FEED_AMOUNT) != null && getIntFlagAtt(FlagAtt.FEED_AMOUNT) < -20) {
            setIntFlagAttNoCheck(FlagAtt.FEED_AMOUNT, -20);
        } else if (getIntFlagAtt(FlagAtt.FEED_AMOUNT) != null && getIntFlagAtt(FlagAtt.FEED_AMOUNT) > 20) {
            setIntFlagAttNoCheck(FlagAtt.FEED_AMOUNT, 20);
        }
        for (final FlagAtt f : new FlagAtt[] { FlagAtt.HEAL_MIN_HEALTH, FlagAtt.HEAL_MAX_HEALTH, FlagAtt.FEED_MIN_FOOD, FlagAtt.FEED_MAX_FOOD }) {
            if (getIntFlagAtt(f) != null && getIntFlagAtt(f) < 0) {
                setIntFlagAttNoCheck(f, 0);
            } else if (getIntFlagAtt(f) != null && getIntFlagAtt(f) > 20) {
                setIntFlagAttNoCheck(f, 20);
            }
        }
        if (getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) != null && getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) < 0) {
            setIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP, 0);
        } else if (getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) != null && getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) > 100) {
            setIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP, 100);
        }
    }
    
    // Locations handling
    public Location getLocFlagAtt(final FlagAtt f) {
        if (atts == null) {
            return null;
        } else if (isLocFlagAtt(f)) {
            return (Location) atts.get(f);
        } else {
            new IllegalArgumentException(f.name()).printStackTrace();
            return null;
        }
    }
    
    public void setLocFlagAtt(final FlagAtt f, final Location loc) {
        if (atts == null) {
            newMap();
        }
        if (isLocFlagAtt(f)) {
            atts.put(f, loc);
            checkLocFlagAttCorrection();
        } else {
            new IllegalArgumentException(f.name()).printStackTrace();
        }
    }
    
    private void checkLocFlagAttCorrection() {
        // Nothing to do yet
    }
    
    // Vectors handling
    public Vector getVectFlagAtt(final FlagAtt f) {
        if (atts == null) {
            return null;
        } else if (isVectFlagAtt(f)) {
            return (Vector) atts.get(f);
        } else {
            new IllegalArgumentException(f.name()).printStackTrace();
            return null;
        }
    }
    
    public void setVectFlagAtt(final FlagAtt f, final Vector v) {
        if (atts == null) {
            newMap();
        }
        if (isVectFlagAtt(f)) {
            atts.put(f, v);
            checkVectFlagAttCorrection();
        } else {
            new IllegalArgumentException(f.name()).printStackTrace();
        }
    }
    
    private void checkVectFlagAttCorrection() {
        if (getVectFlagAtt(FlagAtt.BOOSTER_VECTOR) != null && getVectFlagAtt(FlagAtt.BOOSTER_VECTOR).lengthSquared() > 100) {
            // XXX: Bukkit does not allow > 10 m/s Velocity
            setVectFlagAtt(FlagAtt.BOOSTER_VECTOR, getVectFlagAtt(FlagAtt.BOOSTER_VECTOR).normalize().multiply(10));
        }
    }
    
    // Default map
    private static EnumMap<FlagAtt, Object> getDefaultFlagAttMap() {
        final EnumMap<FlagAtt, Object> defaultFlagMap = new EnumMap<FlagAtt, Object>(FlagAtt.class);
        
        // We do not put anything in the map, we do not want to store useless objects / references
        
        return defaultFlagMap;
    }
}

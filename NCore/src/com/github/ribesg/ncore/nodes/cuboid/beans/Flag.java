package com.github.ribesg.ncore.nodes.cuboid.beans;

public enum Flag {
    BOOSTER,
    BUILD,
    CHAT,
    CHEST,
    CLOSED,
    CREATIVE,
    DROP,
    ENDERMAN,
    EXPLOSION,
    FARM,
    FEED,
    FIRE,
    GOD,
    HEAL,
    HIDDEN,
    INVISIBLE,
    JAIL,
    MOB,
    PASS,
    PERMANENT,
    PICKUP,
    PVP,
    SNOW,
    TELEPORT,
    USE,
    WARPGATE;

    public static Flag get(final String val) {
        final String in = val.toUpperCase();
        Flag f = null;
        try {
            f = Flag.valueOf(in);
        } catch (final IllegalArgumentException e) {
            if (in.equals("BOOST")) {
                f = BOOSTER;
            } else if (in.equals("SPEAK")) {
                f = CHAT;
            } else if (in.equals("EXP")) {
                f = EXPLOSION;
            } else if (in.equals("ENDERMAN")) {
                f = ENDERMAN;
            } else if (in.equals("HIDE")) {
                f = HIDDEN;
            } else if (in.equals("PERM")) {
                f = PERMANENT;
            } else if (in.equals("TEL")) {
                f = TELEPORT;
            } else if (in.equals("WARP")) {
                f = WARPGATE;
            }
        }
        return f;
    }
}

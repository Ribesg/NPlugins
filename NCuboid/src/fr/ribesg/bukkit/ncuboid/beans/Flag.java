package fr.ribesg.bukkit.ncuboid.beans;

public enum Flag {
    BOOSTER, BUILD, CHAT, CHEST, CLOSED, CREATIVE, DROP, ENDERMAN, EXPLOSION_BLOCK, EXPLOSION_PLAYER, EXPLOSION_ITEM, FARM, FEED, FIRE, GOD, HEAL, HIDDEN, INVISIBLE, JAIL, MOB, PASS, PERMANENT, PICKUP, PVP, SNOW, TELEPORT, USE, WARPGATE;

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
            } else if (in.equals("EXPLOSIONBLOCK") || in.equals("EXP_BLOCK") || in.equals("EXPBLOCK")) {
                f = EXPLOSION_BLOCK;
            } else if (in.equals("EXPLOSIONPLAYER") || in.equals("EXP_PLAYER") || in.equals("EXPPLAYER")) {
                f = EXPLOSION_PLAYER;
            } else if (in.equals("EXPLOSIONITEM") || in.equals("EXP_ITEM") || in.equals("EXPITEM")) {
                f = EXPLOSION_ITEM;
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

package fr.ribesg.bukkit.ncuboid.beans;

public enum Flag {
	BOOSTER,
	BUILD,
	CHAT,
	CHEST,
	CLOSED,
	CREATIVE,
	DROP,
	ENDERMANGRIEF,
	EXPLOSION_BLOCK,
	EXPLOSION_PLAYER,
	EXPLOSION_ITEM,
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
		Flag f;
		try {
			f = Flag.valueOf(in);
		} catch (final IllegalArgumentException e) {
			switch (in) {
				case "BOOST":
					f = BOOSTER;
					break;
				case "SPEAK":
					f = CHAT;
					break;
				case "EXPLOSIONBLOCK":
				case "EXP_BLOCK":
				case "EXPBLOCK":
					f = EXPLOSION_BLOCK;
					break;
				case "EXPLOSIONPLAYER":
				case "EXP_PLAYER":
				case "EXPPLAYER":
					f = EXPLOSION_PLAYER;
					break;
				case "EXPLOSIONITEM":
				case "EXP_ITEM":
				case "EXPITEM":
					f = EXPLOSION_ITEM;
					break;
				case "ENDERMAN":
					f = ENDERMANGRIEF;
					break;
				case "HIDE":
					f = HIDDEN;
					break;
				case "PERM":
					f = PERMANENT;
					break;
				case "TEL":
					f = TELEPORT;
					break;
				case "WARP":
					f = WARPGATE;
					break;
				default:
					f = null;
					break;
			}
		}
		return f;
	}
}

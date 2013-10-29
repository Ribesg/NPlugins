package fr.ribesg.bukkit.ncuboid.beans;

public enum FlagAtt {

	// Integer
	HEAL_AMOUNT,
	HEAL_TIMER,
	HEAL_MIN_HEALTH,
	HEAL_MAX_HEALTH,
	FEED_AMOUNT,
	FEED_TIMER,
	FEED_MIN_FOOD,
	FEED_MAX_FOOD,
	EXPLOSION_BLOCK_DROP,

	// Location
	EXTERNAL_POINT,
	INTERNAL_POINT,

	// Vector
	BOOSTER_VECTOR;

	public static FlagAtt get(final String val) {
		final String in = val.toUpperCase();
		FlagAtt fa;
		try {
			fa = FlagAtt.valueOf(in);
		} catch (final IllegalArgumentException e) {
			switch (in) {
				case "HA":
				case "HAMOUNT":
				case "H_AMOUNT":
					fa = HEAL_AMOUNT;
					break;
				case "HT":
				case "HTIMER":
				case "H_TIMER":
					fa = HEAL_TIMER;
					break;
				case "HMIN":
				case "HEALMIN":
					fa = HEAL_MIN_HEALTH;
					break;
				case "HMAX":
				case "HEALMAX":
					fa = HEAL_MAX_HEALTH;
					break;
				case "FA":
				case "FAMOUNT":
				case "F_AMOUNT":
					fa = FEED_AMOUNT;
					break;
				case "FT":
				case "FTIMER":
				case "F_TIMER":
					fa = FEED_TIMER;
					break;
				case "FMIN":
				case "FEEDMIN":
					fa = FEED_MIN_FOOD;
					break;
				case "FMAX":
				case "FEEDMAX":
					fa = FEED_MAX_FOOD;
					break;
				case "EXPLOSION_DROP":
				case "EXPLOSIONDROP":
				case "EXP_BLOCK_DROP":
				case "EXPBLOCKDROP":
				case "EXP_DROP":
				case "EXPDROP":
					fa = EXPLOSION_BLOCK_DROP;
					break;
				case "EP":
				case "EXTERNALPOINT":
				case "EXTPOINT":
				case "EXT_POINT":
					fa = EXTERNAL_POINT;
					break;
				case "IP":
				case "INTERNALPOINT":
				case "INTPOINT":
				case "INT_POINT":
					fa = INTERNAL_POINT;
					break;
				case "BV":
				case "BVECTOR":
				case "B_VECTOR":
				case "BOOSTERVECTOR":
					fa = BOOSTER_VECTOR;
					break;
				default:
					fa = null;
					break;
			}
		}

		return fa;
	}

	public static boolean isIntFlagAtt(final FlagAtt f) {
		return f != null && HEAL_AMOUNT.compareTo(f) <= 0 && EXPLOSION_BLOCK_DROP.compareTo(f) >= 0;
	}

	public static boolean isLocFlagAtt(final FlagAtt f) {
		return f != null && EXTERNAL_POINT.compareTo(f) <= 0 && INTERNAL_POINT.compareTo(f) >= 0;
	}

	public static boolean isVectFlagAtt(final FlagAtt f) {
		return f != null && f == BOOSTER_VECTOR;
	}
}

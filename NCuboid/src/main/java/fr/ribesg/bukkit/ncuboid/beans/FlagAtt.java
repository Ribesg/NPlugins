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

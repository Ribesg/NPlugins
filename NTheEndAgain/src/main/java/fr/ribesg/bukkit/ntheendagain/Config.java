package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.common.FrameBuilder;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Random;

public class Config extends AbstractConfig<NTheEndAgain> {

	private final static Random rand = new Random();

	private final String worldName;

	// General
	private final static int DEFAULT_filterMovedTooQuicklySpam = 0;
	private int filterMovedTooQuicklySpam;

	// EnderDragon
	private final static int DEFAULT_edHealth = 200;
	private int edHealth;

	private final static float DEFAULT_edDamageMultiplier = 1.0f;
	private float edDamageMultiplier;

	private final static int DEFAULT_edPushesPlayers = 1;
	private int edPushesPlayers;

	private final static int DEFAULT_edEggHandling = 0;
	private int edEggHandling;

	private final static int DEFAULT_edExpHandling = 0;
	private int edExpHandling;

	private final static int DEFAULT_edExpReward = 12_000;
	private int edExpReward;

	private final static int DEFAULT_edPortalSpawn = 0;
	private int edPortalSpawn;

	// EnderCrystals
	private final static float DEFAULT_ecHealthRegainRate = 1.0f;
	private float ecHealthRegainRate;

	// Regeneration
	private final static int DEFAULT_regenType = 0;
	private int regenType;

	private final static int DEFAULT_regenTimer = 86_400; // 24 hours
	private int regenTimer;

	private final static int DEFAULT_regenMethod = 0;
	private int regenMethod;

	private final static int DEFAULT_regenAction = 0;
	private int regenAction;

	private final static int DEFAULT_hardRegenOnStop = 0;
	private int hardRegenOnStop;

	private final static int DEFAULT_slowSoftRegenChunks = 5;
	private int slowSoftRegenChunks;

	private final static int DEFAULT_slowSoftRegenTimer = 5;
	private int slowSoftRegenTimer;

	// Respawn

	private final static int DEFAULT_respawnNumber = 1;
	private int respawnNumber;

	private final static int DEFAULT_respawnType = 0;
	private int respawnType;

	private final static int DEFAULT_respawnTimerMin = 7_200;
	private int respawnTimerMin;

	private final static int DEFAULT_respawnTimerMax = 14_400;
	private int respawnTimerMax;

	// Data

	private final static long DEFAULT_nextRegenTaskTime = 0;
	private long nextRegenTaskTime;

	private final static long DEFAULT_nextRespawnTaskTime = 0;
	private long nextRespawnTaskTime;

	public Config(final NTheEndAgain instance, final String world) {
		super(instance);
		worldName = world;

		// General
		setFilterMovedTooQuicklySpam(DEFAULT_filterMovedTooQuicklySpam);

		// EnderDragon
		setEdHealth(DEFAULT_edHealth);
		setEdDamageMultiplier(DEFAULT_edDamageMultiplier);
		setEdPushesPlayers(DEFAULT_edPushesPlayers);
		setEdEggHandling(DEFAULT_edEggHandling);
		setEdExpHandling(DEFAULT_edExpHandling);
		setEdExpReward(DEFAULT_edExpReward);
		setEdPortalSpawn(DEFAULT_edPortalSpawn);

		// EnderCrystals
		setEcHealthRegainRate(DEFAULT_ecHealthRegainRate);

		// Regeneration
		setRegenType(DEFAULT_regenType);
		setRegenTimer(DEFAULT_regenTimer);
		setRegenMethod(DEFAULT_regenMethod);
		setRegenAction(DEFAULT_regenAction);
		setHardRegenOnStop(DEFAULT_hardRegenOnStop);
		setSlowSoftRegenChunks(DEFAULT_slowSoftRegenChunks);
		setSlowSoftRegenTimer(DEFAULT_slowSoftRegenTimer);

		// Respawn
		setRespawnNumber(DEFAULT_respawnNumber);
		setRespawnType(DEFAULT_respawnType);
		setRespawnTimerMin(DEFAULT_respawnTimerMin);
		setRespawnTimerMax(DEFAULT_respawnTimerMax);

		// Data
		setNextRegenTaskTime(DEFAULT_nextRegenTaskTime);
		setNextRespawnTaskTime(DEFAULT_nextRespawnTaskTime);
	}

	/** @see AbstractConfig#handleValues(YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) {

		final String fileName = StringUtils.toLowerCamelCase(worldName) + "Config.yml";

		// General
		setFilterMovedTooQuicklySpam(config.getInt("filterMovedTooQuicklySpam", DEFAULT_filterMovedTooQuicklySpam));
		if (!match(getFilterMovedTooQuicklySpam(), 0, 1)) {
			wrongValue(fileName, "filterMovedTooQuicklySpam", getFilterMovedTooQuicklySpam(), DEFAULT_filterMovedTooQuicklySpam);
			setFilterMovedTooQuicklySpam(DEFAULT_filterMovedTooQuicklySpam);
		}

		// EnderDragon
		setEdHealth(config.getInt("edHealth", DEFAULT_edHealth));
		if (!match(getEdHealth(), 1, Integer.MAX_VALUE)) {
			wrongValue(fileName, "edHealth", getEdHealth(), DEFAULT_edHealth);
			setEdHealth(DEFAULT_edHealth);
		}

		setEdDamageMultiplier((float) config.getDouble("edDamageMultiplier", DEFAULT_edDamageMultiplier));
		if (!match(getEdDamageMultiplier(), 0f, Float.MAX_VALUE)) {
			wrongValue(fileName, "edDamageMultiplier", getEdDamageMultiplier(), DEFAULT_edDamageMultiplier);
			setEdDamageMultiplier(DEFAULT_edDamageMultiplier);
		}

		setEdPushesPlayers(config.getInt("edPushesPlayers", DEFAULT_edPushesPlayers));
		if (!match(getEdPushesPlayers(), 0, 1)) {
			wrongValue(fileName, "edPushesPlayers", getEdPushesPlayers(), DEFAULT_edPushesPlayers);
			setEdPushesPlayers(DEFAULT_edPushesPlayers);
		}

		setEdEggHandling(config.getInt("edEggHandling", DEFAULT_edEggHandling));
		if (!match(getEdEggHandling(), 0, 1)) {
			wrongValue(fileName, "edEggHandling", getEdEggHandling(), DEFAULT_edEggHandling);
			setEdEggHandling(DEFAULT_edEggHandling);
		}

		setEdExpHandling(config.getInt("edExpHandling", DEFAULT_edExpHandling));
		if (!match(getEdExpHandling(), 0, 1)) {
			wrongValue(fileName, "edExpHandling", getEdExpHandling(), DEFAULT_edExpHandling);
			setEdExpHandling(DEFAULT_edExpHandling);
		}

		setEdExpReward(config.getInt("edExpReward", DEFAULT_edExpReward));
		if (!match(getEdExpReward(), 0, Integer.MAX_VALUE)) {
			wrongValue(fileName, "edExpReward", getEdExpReward(), DEFAULT_edExpReward);
			setEdExpReward(DEFAULT_edExpReward);
		}

		setEdPortalSpawn(config.getInt("edPortalSpawn", DEFAULT_edPortalSpawn));
		if (!match(getEdPortalSpawn(), 0, 2)) {
			wrongValue(fileName, "edPortalSpawn", getEdPortalSpawn(), DEFAULT_edPortalSpawn);
			setEdPortalSpawn(DEFAULT_edPortalSpawn);
		}

		// EnderCrystals

		setEcHealthRegainRate((float) config.getDouble("ecHealthRegainRate", DEFAULT_ecHealthRegainRate));
		if (!match(getEcHealthRegainRate(), 0f, Float.MAX_VALUE)) {
			wrongValue(fileName, "ecHealthRegainRate", getEcHealthRegainRate(), DEFAULT_ecHealthRegainRate);
			setEcHealthRegainRate(DEFAULT_ecHealthRegainRate);
		}

		// Regeneration
		setRegenType(config.getInt("regenType", DEFAULT_regenType));
		if (!match(getRegenType(), 0, 4)) {
			wrongValue(fileName, "regenType", getRegenType(), DEFAULT_regenType);
			setRegenType(DEFAULT_regenType);
		}

		setRegenTimer(config.getInt("regenTimer", DEFAULT_regenTimer));
		if (!match(getRegenTimer(), 0, Integer.MAX_VALUE)) {
			wrongValue(fileName, "regenTimer", getRegenTimer(), DEFAULT_regenTimer);
			setRegenTimer(DEFAULT_regenTimer);
		}

		if (getRegenTimer() == 0 && match(getRegenType(), 3, 4)) {
			plugin.getLogger().warning("Can't use regenTimer=0 with regenType=" + getRegenType() + "!");
			wrongValue(fileName, "regenType", getRegenType(), 0);
			setRegenType(0);
		}

		setRegenMethod(config.getInt("regenMethod", DEFAULT_regenMethod));
		if (!match(getRegenMethod(), 0, 2)) {
			wrongValue(fileName, "regenMethod", getRegenMethod(), DEFAULT_regenMethod);
			setRegenMethod(DEFAULT_regenMethod);
		}

		setRegenAction(config.getInt("regenAction", DEFAULT_regenAction));
		if (!match(getRegenAction(), 0, 1)) {
			wrongValue(fileName, "regenAction", getRegenAction(), DEFAULT_regenAction);
			setRegenAction(DEFAULT_regenAction);
		}

		setHardRegenOnStop(config.getInt("hardRegenOnStop", DEFAULT_hardRegenOnStop));
		if (!match(getHardRegenOnStop(), 0, 1)) {
			wrongValue(fileName, "hardRegenOnStop", getHardRegenOnStop(), DEFAULT_hardRegenOnStop);
			setHardRegenOnStop(DEFAULT_hardRegenOnStop);
		}

		setSlowSoftRegenChunks(config.getInt("slowSoftRegenChunks", DEFAULT_slowSoftRegenChunks));
		if (!match(getSlowSoftRegenChunks(), 1, Integer.MAX_VALUE)) {
			wrongValue(fileName, "slowSoftRegenChunks", getSlowSoftRegenChunks(), DEFAULT_slowSoftRegenChunks);
			setSlowSoftRegenChunks(DEFAULT_slowSoftRegenChunks);
		}

		setSlowSoftRegenTimer(config.getInt("slowSoftRegenTimer", DEFAULT_slowSoftRegenTimer));
		if (!match(getSlowSoftRegenTimer(), 1, Integer.MAX_VALUE)) {
			wrongValue(fileName, "slowSoftRegenTimer", getSlowSoftRegenTimer(), DEFAULT_slowSoftRegenTimer);
			setSlowSoftRegenTimer(DEFAULT_slowSoftRegenTimer);
		}

		// Respawn
		setRespawnNumber(config.getInt("respawnNumber", DEFAULT_respawnNumber));
		if (!match(getRespawnNumber(), 0, Integer.MAX_VALUE)) {
			wrongValue(fileName, "respawnNumber", getRespawnNumber(), DEFAULT_respawnNumber);
			setRespawnNumber(DEFAULT_respawnNumber);
		}

		setRespawnType(config.getInt("respawnType", DEFAULT_respawnType));
		if (!match(getRegenType(), 0, 5)) {
			wrongValue(fileName, "respawnType", getRespawnType(), DEFAULT_respawnType);
			setRespawnType(DEFAULT_respawnType);
		}

		setRespawnTimerMin(config.getInt("respawnTimerMin", DEFAULT_respawnTimerMin));
		if (!match(getRespawnTimerMin(), 0, Integer.MAX_VALUE)) {
			wrongValue(fileName, "respawnTimerMin", getRespawnTimerMin(), DEFAULT_respawnTimerMin);
			setRespawnTimerMin(DEFAULT_respawnTimerMin);
		}

		setRespawnTimerMax(config.getInt("respawnTimerMax", DEFAULT_respawnTimerMax));
		if (!match(getRespawnTimerMax(), getRespawnTimerMin(), Integer.MAX_VALUE)) {
			wrongValue(fileName, "respawnTimerMax", getRespawnTimerMax(), getRespawnTimerMin());
			setRespawnTimerMin(getRespawnTimerMin());
		}

		// Data
		setNextRegenTaskTime(config.getLong("nextRegenTaskTime", DEFAULT_nextRegenTaskTime));
		if (!match(getNextRegenTaskTime(), 0, Long.MAX_VALUE)) {
			wrongValue(fileName, "nextRegenTaskTime", getNextRegenTaskTime(), DEFAULT_nextRegenTaskTime);
			setNextRegenTaskTime(DEFAULT_nextRegenTaskTime);
		}

		setNextRespawnTaskTime(config.getLong("nextRespawnTaskTime", DEFAULT_nextRespawnTaskTime));
		if (!match(getNextRespawnTaskTime(), 0, Long.MAX_VALUE)) {
			wrongValue(fileName, "nextRespawnTaskTime", getNextRespawnTaskTime(), DEFAULT_nextRespawnTaskTime);
			setNextRespawnTaskTime(DEFAULT_nextRespawnTaskTime);
		}
	}

	/** @see AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// ############
		// ## HEADER ##
		// ############

		frame = new FrameBuilder();
		frame.addLine("Config file for NTheEndAgain plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		content.append("\n# This config file is about the world \"" + worldName + "\"\n\n");

		// #############
		// ## GENERAL ##
		// #############

		frame = new FrameBuilder();
		frame.addLine("GENERAL CONFIGURATION", FrameBuilder.Option.CENTER);
		for (final String line : frame.build()) {
			content.append(line);
			content.append('\n');
		}
		content.append('\n');

		// filterMovedTooQuicklySpam
		content.append("# Do we hide the 'Player Moved Too Quickly!' spam? Default: " + DEFAULT_filterMovedTooQuicklySpam + "\n");
		content.append("# /!\\ This feature is not compatible with any other plugin using Bukkit's Logger filters\n");
		content.append("#\n");
		content.append("#       0: Disabled.\n");
		content.append("#       1: Enabled.\n");
		content.append("#\n");
		content.append("# Note: to completely disable the filter and allow compatibility with other plugins using it,\n");
		content.append("#       please be sure to set it to 0 in EVERY End World config file.\n");
		content.append("#\n");
		content.append("filterMovedTooQuicklySpam: " + getFilterMovedTooQuicklySpam() + "\n\n");

		// #################
		// ## ENDERDRAGON ##
		// #################

		frame = new FrameBuilder();
		frame.addLine("ENDERDRAGON CONFIGURATION", FrameBuilder.Option.CENTER);
		for (final String line : frame.build()) {
			content.append(line);
			content.append('\n');
		}
		content.append('\n');

		// edHealth
		content.append("# The health value EnderDragons will spawn with. Default: " + DEFAULT_edHealth + "\n");
		content.append("edHealth: " + getEdHealth() + "\n\n");

		// edDamageMultiplier
		content.append("# Scale damages done by EnderDragon. Default: " + DEFAULT_edDamageMultiplier + "\n");
		content.append("edDamageMultiplier: " + getEdDamageMultiplier() + "\n\n");

		// edPushesPlayers
		content.append("# Do we 'simulate' the EnderDragon-Pushes-Player behaviour? Default: " + DEFAULT_edPushesPlayers + "\n");
		content.append("# This feature apply a kind-of random velocity to a Player after it has been damaged by an EnderDragon\n");
		content.append("#\n");
		content.append("#       0: Disabled.\n");
		content.append("#       1: Enabled.\n");
		content.append("#\n");
		content.append("edPushesPlayers: " + getEdPushesPlayers() + "\n\n");

		// edEggHandling
		content.append("# The way the DragonEgg will spawn. Default: " + DEFAULT_edEggHandling + "\n");
		content.append("#\n");
		content.append("#       0: Disabled. The egg will spawn normally if portalHandling is set to 0 or 1.\n");
		content.append("#       1: Enabled. The egg will be semi-randomly given to one of the best fighters.\n");
		content.append("#\n");
		content.append("edEggHandling: " + getEdEggHandling() + "\n\n");

		// edExpHandling
		content.append("# The way the reward XP will be given to player. Default: " + DEFAULT_edExpHandling + "\n");
		content.append("#\n");
		content.append("#       0: Disabled. XP orbs will spawn normally.\n");
		content.append("#       1: Enabled. XP will be splitted between fighters, more XP for better fighters.\n");
		content.append("#\n");
		content.append("edExpHandling: " + getEdExpHandling() + "\n\n");

		// edExpReward
		content.append("# The value of the XP drop. Default: " + DEFAULT_edExpReward + "\n");
		content.append("edExpReward: " + getEdExpReward() + "\n\n");

		// edPortalSpawn
		content.append("# The way portal spawn will be handled. Default: " + DEFAULT_edPortalSpawn + "\n");
		content.append("#\n");
		content.append("#       0: Disabled. Portal will spawn normally.\n");
		content.append("#       1: Egg. Portal will be removed but not the DragonEgg\n");
		content.append("#       2: Enabled. Portal will not spawn. No more cut obsidian towers. /!\\ No Egg if dragonEggHandling=0.\n");
		content.append("#\n");
		content.append("edPortalSpawn: " + getEdPortalSpawn() + "\n\n");

		// ###################
		// ## ENDERCRYSTALS ##
		// ###################

		frame = new FrameBuilder();
		frame.addLine("ENDERCRYSTALS CONFIGURATION", FrameBuilder.Option.CENTER);
		for (final String line : frame.build()) {
			content.append(line);
			content.append('\n');
		}
		content.append('\n');

		// ecHealthRegainRate
		content.append("# Change EnderCrystals behaviour relative to the EnderDragon. Default: " + DEFAULT_ecHealthRegainRate + "\n");
		content.append("# One important thing to understand is that Health is integer (for now).\n");
		content.append("#\n");
		content.append("#       < 1.0: Acts as a \"chance that the Dragon will regain 1 HP\" each tick\n");
		content.append("#       = 1.0: Vanilla. EnderDragon gain 1 HP per tick.\n");
		content.append("#       > 1.0: EnderDragon gain x HP per tick, so please set it to an integer value like 2 or more.\n");
		content.append("#\n");
		content.append("ecHealthRegainRate: " + getEcHealthRegainRate() + "\n\n");

		// ##################
		// ## REGENERATION ##
		// ##################

		frame = new FrameBuilder();
		frame.addLine("REGENERATION CONFIGURATION", FrameBuilder.Option.CENTER);
		for (final String line : frame.build()) {
			content.append(line);
			content.append('\n');
		}
		content.append('\n');

		// regenType
		content.append("# Select the regeneration type. Default: " + DEFAULT_regenType + "\n");
		content.append("#\n");
		content.append("#       0: Disabled. No hot regeneration.\n");
		content.append("#       1: Before EnderDragon respawn (only if no EnderDragon alive)\n");
		content.append("#       2: Periodic - From load time. Regen every <regenTimer> seconds after boot/load.\n");
		content.append("#       3: Periodic - Persistent. Regen every <regenTimer> seconds, persistent through reboots/reloads\n");
		content.append("#\n");
		content.append("regenType: " + getRegenType() + "\n\n");

		// regenMethod
		content.append("# Select your definition of \"regen\". Default: " + DEFAULT_regenMethod + "\n");
		content.append("#\n");
		content.append("#       0: Hard Regen. Regen every chunks at once. Laggy. Not recommended.\n");
		content.append("#       1: Soft Regen. Regen chunks when they are loaded. A lot less laggy.\n");
		content.append("#       2: Crystals only. Does not modify any block, only respawn the EnderCrystals.\n");
		content.append("#\n");
		content.append("# Note: Regeneration does not regenerate Protected chunks.\n");
		content.append("#\n");
		content.append("regenMethod: " + getRegenMethod() + "\n\n");

		// regenTimer
		content.append("# The time between each regen. Ignored if regenType is not Periodic (2 or 3). Default: " +
		               DEFAULT_regenTimer +
		               "\n");
		content.append("#\n");
		content.append("# Here are some example values:\n");
		content.append("#   Value   --   Description\n");
		content.append("#       1800: 30 minutes\n");
		content.append("#       3600: 1 hour\n");
		content.append("#       7200: 2 hours\n");
		content.append("#      10800: 3 hours\n");
		content.append("#      14400: 4 hours\n");
		content.append("#      21600: 6 hours\n");
		content.append("#      28800: 8 hours\n");
		content.append("#      43200: 12 hours\n");
		content.append("#      86400: 24 hours - 1 day\n");
		content.append("#     172800: 48 hours - 2 days\n");
		content.append("#     604800: 7 days\n");
		content.append("#\n");
		content.append("# You can use *any* strictly positive value you want, just be sure to convert it to seconds.\n");
		content.append("#\n");
		content.append("# Note: You should NOT use low value. Some hours of delay are recommended.\n");
		content.append("#\n");
		content.append("regenTimer: " + getRegenTimer() + "\n\n");

		// regenAction
		content.append("# What do we do to players in the End when we want to regen the world? Default: " + DEFAULT_regenAction + "\n");
		content.append("#\n");
		content.append("#       0: Kick them. This way they can rejoin immediatly in the End at the same place.\n");
		content.append("#          WARNING: Mass rejoin after mass kick in the End could cause lag if regenMethod=1\n");
		content.append("#\n");
		content.append("#       1: Teleport them to the spawn point of the Main (= first) world.\n");
		content.append("#\n");
		content.append("regenAction: " + getRegenAction() + "\n\n");

		// hardRegenOnStop
		content.append("# Activate hard regeneration on server stop. This will only slow down server stop.\n");
		content.append("# This is nice to clean the End occasionnaly when using Soft or Crystal regen.\n");
		content.append("#\n");
		content.append("#       0: Disabled.\n");
		content.append("#       1: Enabled.\n");
		content.append("#\n");
		content.append("hardRegenOnStop: " + getHardRegenOnStop() + "\n\n");

		// slowSoftRegenChunks
		content.append("# Select the number of chunks to be regen every slowSoftRegenTimer after a Soft Regeneration has started.\n");
		content.append("# Default value: " + DEFAULT_slowSoftRegenChunks + "\n");
		content.append("slowSoftRegenChunks: " + getSlowSoftRegenChunks() + "\n\n");

		// slowSoftRegenTimer
		content.append("# Select the at which rate slowSoftRegenChunks chunks will be regenerated after a\n");
		content.append("# Soft Regeneration has started. Default value: " + DEFAULT_slowSoftRegenTimer + "\n");
		content.append("slowSoftRegenTimer: " + getSlowSoftRegenTimer() + "\n\n");

		// #############
		// ## RESPAWN ##
		// #############

		frame = new FrameBuilder();
		frame.addLine("RESPAWN CONFIGURATION", FrameBuilder.Option.CENTER);
		for (final String line : frame.build()) {
			content.append(line);
			content.append('\n');
		}
		content.append('\n');

		// respawnNumber
		content.append("# This is the amount of EnderDragons you want to be spawned. Default: " + DEFAULT_respawnNumber + "\n");
		content.append("respawnNumber: " + getRespawnNumber() + "\n\n");

		// respawnType
		content.append("# Select when you want to respawn Dragons automagically. Default: " + DEFAULT_respawnType + "\n");
		content.append("#\n");
		content.append("#       0: Disabled. No automatic respawn.\n");
		content.append("#       1: X seconds after each Dragon's death. Not really good with regenType=1.\n");
		content.append("#       2: X seconds after the last Dragon alive's death.\n");
		content.append("#       3: On server start.\n");
		content.append("#       4: Periodic - From load time. Respawn every X seconds after boot/load.\n");
		content.append("#       5: Periodic - Persistent. Respawn every X seconds, persistent through reboots/reloads\n");
		content.append("#\n");
		content.append("respawnType: " + getRespawnType() + "\n\n");

		// respawnTimer
		content.append("# The X value in the previous comments. Defaults: " +
		               DEFAULT_respawnTimerMin +
		               " < " +
		               DEFAULT_respawnTimerMax +
		               "\n");
		content.append("# A value will be randomly chosen for each iteration. The chosen value vill be between min and max\n");
		content.append("#\n");
		content.append("# Here are some example values (again!):\n");
		content.append("#   Value   --   Description\n");
		content.append("#       1800: 30 minutes\n");
		content.append("#       3600: 1 hour\n");
		content.append("#       7200: 2 hours\n");
		content.append("#      10800: 3 hours\n");
		content.append("#      14400: 4 hours\n");
		content.append("#      21600: 6 hours\n");
		content.append("#      28800: 8 hours\n");
		content.append("#      43200: 12 hours\n");
		content.append("#      86400: 24 hours - 1 day\n");
		content.append("#     172800: 48 hours - 2 days\n");
		content.append("#     604800: 7 days\n");
		content.append("#\n");
		content.append("# You can use *any* strictly positive value you want, just be sure to convert it to seconds.\n");
		content.append("#\n");
		content.append("# Note: You CAN use low value if regenType is not set to 1.\n");
		content.append("#       But maybe you should consider using respawnType=1 or respawnType=2 instead of a low periodic.\n");
		content.append("#\n");
		content.append("respawnTimerMin: " + getRespawnTimerMin() + "\n");
		content.append("respawnTimerMax: " + getRespawnTimerMax() + "\n\n");

		// ##########
		// ## DATA ##
		// ##########

		frame = new FrameBuilder();
		frame.addLine("DATA - PLEASE DO NOT TOUCH!", FrameBuilder.Option.CENTER);
		for (final String line : frame.build()) {
			content.append(line);
			content.append('\n');
		}
		content.append('\n');

		// nextRegenTaskTime
		content.append("# Used to allow Regen task timer persistence. /!\\ PLEASE DO NOT TOUCH THIS !\n");
		content.append("nextRegenTaskTime: " + (getRegenTimer() == 0 ? "0" : getNextRegenTaskTime()) + "\n\n");

		// nextRespawnTaskTime
		content.append("# Used to allow Respawn task timer persistence. /!\\ PLEASE DO NOT TOUCH THIS !\n");
		content.append("nextRespawnTaskTime: " + (getRespawnTimerMax() == 0 ? "0" : getNextRespawnTaskTime()) + "\n\n");

		return content.toString();
	}

	// General

	public int getFilterMovedTooQuicklySpam() {
		return filterMovedTooQuicklySpam;
	}

	private void setFilterMovedTooQuicklySpam(int filterMovedTooQuicklySpam) {
		this.filterMovedTooQuicklySpam = filterMovedTooQuicklySpam;
	}

	// EnderDragons

	public float getEdDamageMultiplier() {
		return edDamageMultiplier;
	}

	private void setEdDamageMultiplier(float edDamageMultiplier) {
		this.edDamageMultiplier = edDamageMultiplier;
	}

	public int getEdEggHandling() {
		return edEggHandling;
	}

	private void setEdEggHandling(int edEggHandling) {
		this.edEggHandling = edEggHandling;
	}

	public int getEdExpHandling() {
		return edExpHandling;
	}

	private void setEdExpHandling(int edExpHandling) {
		this.edExpHandling = edExpHandling;
	}

	public int getEdExpReward() {
		return edExpReward;
	}

	private void setEdExpReward(int edExpReward) {
		this.edExpReward = edExpReward;
	}

	public int getEdHealth() {
		return edHealth;
	}

	private void setEdHealth(int edHealth) {
		this.edHealth = edHealth;
	}

	public int getEdPortalSpawn() {
		return edPortalSpawn;
	}

	private void setEdPortalSpawn(int edPortalSpawn) {
		this.edPortalSpawn = edPortalSpawn;
	}

	public int getEdPushesPlayers() {
		return edPushesPlayers;
	}

	private void setEdPushesPlayers(int edPushesPlayers) {
		this.edPushesPlayers = edPushesPlayers;
	}

	// EnderCrystals

	public float getEcHealthRegainRate() {
		return ecHealthRegainRate;
	}

	public void setEcHealthRegainRate(float ecHealthRegainRate) {
		this.ecHealthRegainRate = ecHealthRegainRate;
	}

	// Regeneration

	public int getHardRegenOnStop() {
		return hardRegenOnStop;
	}

	private void setHardRegenOnStop(int hardRegenOnStop) {
		this.hardRegenOnStop = hardRegenOnStop;
	}

	public int getRegenAction() {
		return regenAction;
	}

	private void setRegenAction(int regenAction) {
		this.regenAction = regenAction;
	}

	public int getRegenMethod() {
		return regenMethod;
	}

	private void setRegenMethod(int regenMethod) {
		this.regenMethod = regenMethod;
	}

	public int getRegenTimer() {
		return regenTimer;
	}

	private void setRegenTimer(int regenTimer) {
		this.regenTimer = regenTimer;
	}

	public int getRegenType() {
		return regenType;
	}

	private void setRegenType(int regenType) {
		this.regenType = regenType;
	}

	public int getSlowSoftRegenChunks() {
		return slowSoftRegenChunks;
	}

	public void setSlowSoftRegenChunks(int slowSoftRegenChunks) {
		this.slowSoftRegenChunks = slowSoftRegenChunks;
	}

	public int getSlowSoftRegenTimer() {
		return slowSoftRegenTimer;
	}

	public void setSlowSoftRegenTimer(int slowSoftRegenTimer) {
		this.slowSoftRegenTimer = slowSoftRegenTimer;
	}

	// Respawn

	public int getRespawnNumber() {
		return respawnNumber;
	}

	private void setRespawnNumber(int respawnNumber) {
		this.respawnNumber = respawnNumber;
	}

	public int getRespawnTimerMax() {
		return respawnTimerMax;
	}

	private void setRespawnTimerMax(int respawnTimerMax) {
		this.respawnTimerMax = respawnTimerMax;
	}

	public int getRespawnTimerMin() {
		return respawnTimerMin;
	}

	private void setRespawnTimerMin(int respawnTimerMin) {
		this.respawnTimerMin = respawnTimerMin;
	}

	public int getRandomRespawnTimer() {
		return rand.nextInt(getRespawnTimerMax() - getRespawnTimerMin()) + getRespawnTimerMin();
	}

	public int getRespawnType() {
		return respawnType;
	}

	private void setRespawnType(int respawnType) {
		this.respawnType = respawnType;
	}

	// Data

	public long getNextRegenTaskTime() {
		return nextRegenTaskTime;
	}

	public void setNextRegenTaskTime(long nextRegenTaskTime) {
		this.nextRegenTaskTime = nextRegenTaskTime;
	}

	public long getNextRespawnTaskTime() {
		return nextRespawnTaskTime;
	}

	public void setNextRespawnTaskTime(long nextRespawnTaskTime) {
		this.nextRespawnTaskTime = nextRespawnTaskTime;
	}

	// Others

	public String getWorldName() {
		return worldName;
	}
}

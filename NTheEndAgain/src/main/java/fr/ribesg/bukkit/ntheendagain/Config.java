/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - Config.java                  *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.Config                   *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.common.collection.pairlist.Pair;
import fr.ribesg.bukkit.ncore.common.collection.pairlist.PairList;
import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ncore.util.inventory.InventoryUtilException;
import fr.ribesg.bukkit.ncore.util.inventory.ItemStackUtil;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Config extends AbstractConfig<NTheEndAgain> {

    private static final Random RANDOM = new Random();

    private final String worldName;

    // General
    private static final int DEFAULT_filterMovedTooQuicklySpam = 0;
    private int filterMovedTooQuicklySpam;

    // EnderDragon
    private static final int DEFAULT_edHealth = 200;
    private int edHealth;

    private static final float DEFAULT_edDamageMultiplier = 1.0f;
    private float edDamageMultiplier;

    private static final int DEFAULT_edPushesPlayers = 1;
    private int edPushesPlayers;

    private static final float DEFAULT_edPushForce = 1.75f;
    private float edPushForce;

    private static final int DEFAULT_edEggHandling = 0;
    private int edEggHandling;

    private static final int DEFAULT_edExpHandling = 0;
    private int edExpHandling;

    private static final int DEFAULT_edExpReward = 12_000;
    private int edExpReward;

    private static final int DEFAULT_edPortalSpawn = 0;
    private int edPortalSpawn;

    // EnderCrystals
    private static final float DEFAULT_ecHealthRegainRate = 1.0f;
    private float ecHealthRegainRate;

    // Regeneration
    private static final int DEFAULT_regenType = 0;
    private int regenType;

    private static final int DEFAULT_regenTimer = 86_400; // 24 hours
    private int regenTimer;

    private static final int DEFAULT_regenMethod = 0;
    private int regenMethod;

    private static final int DEFAULT_regenAction = 0;
    private int regenAction;

    private static final int DEFAULT_hardRegenOnStop = 0;
    private int hardRegenOnStop;

    private static final int DEFAULT_slowSoftRegenChunks = 5;
    private int slowSoftRegenChunks;

    private static final int DEFAULT_slowSoftRegenTimer = 5;
    private int slowSoftRegenTimer;

    // Respawn
    private static final int DEFAULT_respawnNumber = 1;
    private int respawnNumber;

    private static final int DEFAULT_respawnType = 0;
    private int respawnType;

    private static final int DEFAULT_respawnTimerMin = 7_200;
    private int respawnTimerMin;

    private static final int DEFAULT_respawnTimerMax = 14_400;
    private int respawnTimerMax;

    // Drop Table
    private static final int DEFAULT_dropTableHandling = 1;
    private int dropTableHandling;

    private final PairList<ItemStack, Float> dropTable;

    // Chunk Protection
    private static final boolean DEFAULT_defaultProtected = false;
    private boolean defaultProtected;

    // Data
    private static final long DEFAULT_nextRegenTaskTime = 0;
    private long nextRegenTaskTime;

    private static final long DEFAULT_nextRespawnTaskTime = 0;
    private long nextRespawnTaskTime;

    public Config(final NTheEndAgain instance, final String world) {
        super(instance);
        this.worldName = world;

        // General
        this.setFilterMovedTooQuicklySpam(DEFAULT_filterMovedTooQuicklySpam);

        // EnderDragon
        this.setEdHealth(DEFAULT_edHealth);
        this.setEdDamageMultiplier(DEFAULT_edDamageMultiplier);
        this.setEdPushesPlayers(DEFAULT_edPushesPlayers);
        this.setEdPushForce(DEFAULT_edPushForce);
        this.setEdEggHandling(DEFAULT_edEggHandling);
        this.setEdExpHandling(DEFAULT_edExpHandling);
        this.setEdExpReward(DEFAULT_edExpReward);
        this.setEdPortalSpawn(DEFAULT_edPortalSpawn);

        // EnderCrystals
        this.setEcHealthRegainRate(DEFAULT_ecHealthRegainRate);

        // Regeneration
        this.setRegenType(DEFAULT_regenType);
        this.setRegenTimer(DEFAULT_regenTimer);
        this.setRegenMethod(DEFAULT_regenMethod);
        this.setRegenAction(DEFAULT_regenAction);
        this.setHardRegenOnStop(DEFAULT_hardRegenOnStop);
        this.setSlowSoftRegenChunks(DEFAULT_slowSoftRegenChunks);
        this.setSlowSoftRegenTimer(DEFAULT_slowSoftRegenTimer);

        // Respawn
        this.setRespawnNumber(DEFAULT_respawnNumber);
        this.setRespawnType(DEFAULT_respawnType);
        this.setRespawnTimerMin(DEFAULT_respawnTimerMin);
        this.setRespawnTimerMax(DEFAULT_respawnTimerMax);

        // Drop Table
        this.setDropTableHandling(DEFAULT_dropTableHandling);
        this.dropTable = new PairList<>();

        // Data
        this.setNextRegenTaskTime(DEFAULT_nextRegenTaskTime);
        this.setNextRespawnTaskTime(DEFAULT_nextRespawnTaskTime);
    }

    @Override
    protected void handleValues(final YamlConfiguration config) {

        final String fileName = StringUtil.toLowerCamelCase(this.worldName) + "Config.yml";

        // General
        this.setFilterMovedTooQuicklySpam(config.getInt("filterMovedTooQuicklySpam", DEFAULT_filterMovedTooQuicklySpam));
        if (!this.match(this.filterMovedTooQuicklySpam, 0, 1)) {
            this.wrongValue(fileName, "filterMovedTooQuicklySpam", this.filterMovedTooQuicklySpam, DEFAULT_filterMovedTooQuicklySpam);
            this.setFilterMovedTooQuicklySpam(DEFAULT_filterMovedTooQuicklySpam);
        }

        // EnderDragon
        this.setEdHealth(config.getInt("edHealth", DEFAULT_edHealth));
        if (!this.match(this.edHealth, 1, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "edHealth", this.edHealth, DEFAULT_edHealth);
            this.setEdHealth(DEFAULT_edHealth);
        }

        this.setEdDamageMultiplier((float)config.getDouble("edDamageMultiplier", DEFAULT_edDamageMultiplier));
        if (!this.match(this.edDamageMultiplier, 0f, Float.MAX_VALUE)) {
            this.wrongValue(fileName, "edDamageMultiplier", this.edDamageMultiplier, DEFAULT_edDamageMultiplier);
            this.setEdDamageMultiplier(DEFAULT_edDamageMultiplier);
        }

        this.setEdPushesPlayers(config.getInt("edPushesPlayers", DEFAULT_edPushesPlayers));
        if (!this.match(this.edPushesPlayers, 0, 1)) {
            this.wrongValue(fileName, "edPushesPlayers", this.edPushesPlayers, DEFAULT_edPushesPlayers);
            this.setEdPushesPlayers(DEFAULT_edPushesPlayers);
        }

        this.setEdPushForce((float)config.getDouble("edPushForce", DEFAULT_edPushForce));
        if (!this.match(this.edPushForce, 0.01f, 10f)) {
            this.wrongValue(fileName, "edPushForce", this.edPushForce, DEFAULT_edPushForce);
            this.setEdPushForce(DEFAULT_edPushForce);
        }

        this.setEdEggHandling(config.getInt("edEggHandling", DEFAULT_edEggHandling));
        if (!this.match(this.edEggHandling, 0, 1)) {
            this.wrongValue(fileName, "edEggHandling", this.edEggHandling, DEFAULT_edEggHandling);
            this.setEdEggHandling(DEFAULT_edEggHandling);
        }

        this.setEdExpHandling(config.getInt("edExpHandling", DEFAULT_edExpHandling));
        if (!this.match(this.edExpHandling, 0, 1)) {
            this.wrongValue(fileName, "edExpHandling", this.edExpHandling, DEFAULT_edExpHandling);
            this.setEdExpHandling(DEFAULT_edExpHandling);
        }

        this.setEdExpReward(config.getInt("edExpReward", DEFAULT_edExpReward));
        if (!this.match(this.edExpReward, 0, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "edExpReward", this.edExpReward, DEFAULT_edExpReward);
            this.setEdExpReward(DEFAULT_edExpReward);
        }

        this.setEdPortalSpawn(config.getInt("edPortalSpawn", DEFAULT_edPortalSpawn));
        if (!this.match(this.edPortalSpawn, 0, 2)) {
            this.wrongValue(fileName, "edPortalSpawn", this.edPortalSpawn, DEFAULT_edPortalSpawn);
            this.setEdPortalSpawn(DEFAULT_edPortalSpawn);
        }

        // EnderCrystals
        this.setEcHealthRegainRate((float)config.getDouble("ecHealthRegainRate", DEFAULT_ecHealthRegainRate));
        if (!this.match(this.ecHealthRegainRate, 0f, Float.MAX_VALUE)) {
            this.wrongValue(fileName, "ecHealthRegainRate", this.ecHealthRegainRate, DEFAULT_ecHealthRegainRate);
            this.setEcHealthRegainRate(DEFAULT_ecHealthRegainRate);
        }

        // Regeneration
        this.setRegenType(config.getInt("regenType", DEFAULT_regenType));
        if (!this.match(this.regenType, 0, 4)) {
            this.wrongValue(fileName, "regenType", this.regenType, DEFAULT_regenType);
            this.setRegenType(DEFAULT_regenType);
        }

        this.setRegenTimer(config.getInt("regenTimer", DEFAULT_regenTimer));
        if (!this.match(this.regenTimer, 0, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "regenTimer", this.regenTimer, DEFAULT_regenTimer);
            this.setRegenTimer(DEFAULT_regenTimer);
        }

        if (this.regenTimer == 0 && this.match(this.regenType, 3, 4)) {
            this.plugin.getLogger().warning("Can't use regenTimer=0 with regenType=" + this.regenType + '!');
            this.wrongValue(fileName, "regenType", this.regenType, 0);
            this.setRegenType(0);
        }

        this.setRegenMethod(config.getInt("regenMethod", DEFAULT_regenMethod));
        if (!this.match(this.regenMethod, 0, 2)) {
            this.wrongValue(fileName, "regenMethod", this.regenMethod, DEFAULT_regenMethod);
            this.setRegenMethod(DEFAULT_regenMethod);
        }

        this.setRegenAction(config.getInt("regenAction", DEFAULT_regenAction));
        if (!this.match(this.regenAction, 0, 2)) {
            this.wrongValue(fileName, "regenAction", this.regenAction, DEFAULT_regenAction);
            this.setRegenAction(DEFAULT_regenAction);
        } else if (this.regenAction == 2 && this.regenMethod != 2) {
            this.plugin.getLogger().warning("Cannot use regenAction=2 without regenMethod=2!");
            this.wrongValue(fileName, "regenAction", this.regenAction, DEFAULT_regenAction);
            this.setRegenAction(DEFAULT_regenAction);
        }

        this.setHardRegenOnStop(config.getInt("hardRegenOnStop", DEFAULT_hardRegenOnStop));
        if (!this.match(this.hardRegenOnStop, 0, 1)) {
            this.wrongValue(fileName, "hardRegenOnStop", this.hardRegenOnStop, DEFAULT_hardRegenOnStop);
            this.setHardRegenOnStop(DEFAULT_hardRegenOnStop);
        }

        this.setSlowSoftRegenChunks(config.getInt("slowSoftRegenChunks", DEFAULT_slowSoftRegenChunks));
        if (!this.match(this.slowSoftRegenChunks, 1, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "slowSoftRegenChunks", this.slowSoftRegenChunks, DEFAULT_slowSoftRegenChunks);
            this.setSlowSoftRegenChunks(DEFAULT_slowSoftRegenChunks);
        }

        this.setSlowSoftRegenTimer(config.getInt("slowSoftRegenTimer", DEFAULT_slowSoftRegenTimer));
        if (!this.match(this.slowSoftRegenTimer, 1, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "slowSoftRegenTimer", this.slowSoftRegenTimer, DEFAULT_slowSoftRegenTimer);
            this.setSlowSoftRegenTimer(DEFAULT_slowSoftRegenTimer);
        }

        // Respawn
        this.setRespawnNumber(config.getInt("respawnNumber", DEFAULT_respawnNumber));
        if (!this.match(this.respawnNumber, 0, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "respawnNumber", this.respawnNumber, DEFAULT_respawnNumber);
            this.setRespawnNumber(DEFAULT_respawnNumber);
        }

        this.setRespawnType(config.getInt("respawnType", DEFAULT_respawnType));
        if (!this.match(this.regenType, 0, 5)) {
            this.wrongValue(fileName, "respawnType", this.respawnType, DEFAULT_respawnType);
            this.setRespawnType(DEFAULT_respawnType);
        }

        this.setRespawnTimerMin(config.getInt("respawnTimerMin", DEFAULT_respawnTimerMin));
        if (!this.match(this.respawnTimerMin, 0, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "respawnTimerMin", this.respawnTimerMin, DEFAULT_respawnTimerMin);
            this.setRespawnTimerMin(DEFAULT_respawnTimerMin);
        }

        this.setRespawnTimerMax(config.getInt("respawnTimerMax", DEFAULT_respawnTimerMax));
        if (!this.match(this.respawnTimerMax, this.respawnTimerMin, Integer.MAX_VALUE)) {
            this.wrongValue(fileName, "respawnTimerMax", this.respawnTimerMax, this.respawnTimerMin);
            this.setRespawnTimerMin(this.respawnTimerMin);
        }

        // Drop Table
        this.setDropTableHandling(config.getInt("dropTableHandling", DEFAULT_dropTableHandling));
        if (!this.match(this.dropTableHandling, 0, 1)) {
            this.wrongValue(fileName, "dropTableHandling", this.dropTableHandling, DEFAULT_dropTableHandling);
            this.setDropTableHandling(DEFAULT_dropTableHandling);
        }

        if (config.isConfigurationSection("dropTable")) {
            this.dropTable.clear();
            final ConfigurationSection dropTableSection = config.getConfigurationSection("dropTable");
            for (final String drop : dropTableSection.getKeys(false)) {
                final ConfigurationSection dropSection = dropTableSection.getConfigurationSection(drop);
                final float probability = (float)dropSection.getDouble("probability", -1);
                if (probability > 1 || probability <= 0) {
                    this.plugin.error("Invalid probability value in configuration for world '" + this.worldName + "' (drop '" + drop + "')");
                } else {
                    try {
                        final ItemStack is = ItemStackUtil.loadFromConfig(dropSection, "itemStack");
                        this.dropTable.put(is, probability);
                    } catch (final InventoryUtilException e) {
                        this.plugin.error("Invalid ItemStack in configuration for world '" + this.worldName + "' (drop '" + drop + "')");
                    }
                }
            }
        }

        // Chunk Protection
        this.setDefaultProtected(config.getBoolean("defaultProtected", false));

        // Data
        this.setNextRegenTaskTime(config.getLong("nextRegenTaskTime", DEFAULT_nextRegenTaskTime));
        if (!this.match(this.nextRegenTaskTime, 0, Long.MAX_VALUE)) {
            this.wrongValue(fileName, "nextRegenTaskTime", this.nextRegenTaskTime, DEFAULT_nextRegenTaskTime);
            this.setNextRegenTaskTime(DEFAULT_nextRegenTaskTime);
        }

        this.setNextRespawnTaskTime(config.getLong("nextRespawnTaskTime", DEFAULT_nextRespawnTaskTime));
        if (!this.match(this.nextRespawnTaskTime, 0, Long.MAX_VALUE)) {
            this.wrongValue(fileName, "nextRespawnTaskTime", this.nextRespawnTaskTime, DEFAULT_nextRespawnTaskTime);
            this.setNextRespawnTaskTime(DEFAULT_nextRespawnTaskTime);
        }
    }

    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        FrameBuilder frame;

        // ############ //
        // ## HEADER ## //
        // ############ //

        frame = new FrameBuilder();
        frame.addLine("Config file for NTheEndAgain plugin", FrameBuilder.Option.CENTER);
        frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
        frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
        for (final String line : frame.build()) {
            content.append(line + '\n');
        }

        content.append("\n# This config file is about the world \"" + this.worldName + "\"\n\n");

        // ############# //
        // ## GENERAL ## //
        // ############# //

        frame = new FrameBuilder();
        frame.addLine("GENERAL CONFIGURATION", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // filterMovedTooQuicklySpam
        content.append("# Do we hide the 'Player Moved Too Quickly!' spam? Default: " + DEFAULT_filterMovedTooQuicklySpam + '\n');
        content.append("# /!\\ This feature is not compatible with any other plugin using Bukkit's Logger filters\n");
        content.append("#\n");
        content.append("#       0: Disabled.\n");
        content.append("#       1: Enabled.\n");
        content.append("#\n");
        content.append("# Note: to completely disable the filter and allow compatibility with other plugins using it,\n");
        content.append("#       please be sure to set it to 0 in EVERY End World config file.\n");
        content.append("#\n");
        content.append("filterMovedTooQuicklySpam: " + this.filterMovedTooQuicklySpam + "\n\n");

        // ################# //
        // ## ENDERDRAGON ## //
        // ################# //

        frame = new FrameBuilder();
        frame.addLine("ENDERDRAGON CONFIGURATION", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // edHealth
        content.append("# The health value EnderDragons will spawn with. Default: " + DEFAULT_edHealth + '\n');
        content.append("edHealth: " + this.edHealth + "\n\n");

        // edDamageMultiplier
        content.append("# Scale damages done by EnderDragon. Default: " + DEFAULT_edDamageMultiplier + '\n');
        content.append("edDamageMultiplier: " + this.edDamageMultiplier + "\n\n");

        // edPushesPlayers
        content.append("# Do we 'simulate' the EnderDragon-Pushes-Player behaviour? Default: " + DEFAULT_edPushesPlayers + '\n');
        content.append("# This feature apply a kind-of random velocity to a Player after it has been damaged by an EnderDragon\n");
        content.append("#\n");
        content.append("#       0: Disabled.\n");
        content.append("#       1: Enabled.\n");
        content.append("#\n");
        content.append("edPushesPlayers: " + this.edPushesPlayers + "\n\n");

        // edPushForce
        content.append("# Simulated EnderDragon Push force. Default: " + DEFAULT_edPushForce + '\n');
        content.append("# Should be a value between 0.01 and 10.0\n");
        content.append("edPushForce: " + this.edPushForce + "\n\n");

        // edEggHandling
        content.append("# The way the DragonEgg will spawn. Default: " + DEFAULT_edEggHandling + '\n');
        content.append("#\n");
        content.append("#       0: Disabled. The egg will spawn normally if portalHandling is set to 0 or 1.\n");
        content.append("#       1: Enabled. The egg will be semi-randomly given to one of the best fighters.\n");
        content.append("#\n");
        content.append("edEggHandling: " + this.edEggHandling + "\n\n");

        // edExpHandling
        content.append("# The way the reward XP will be given to player. Default: " + DEFAULT_edExpHandling + '\n');
        content.append("#\n");
        content.append("#       0: Disabled. XP orbs will spawn normally.\n");
        content.append("#       1: Enabled. XP will be splitted between fighters, more XP for better fighters.\n");
        content.append("#\n");
        content.append("edExpHandling: " + this.edExpHandling + "\n\n");

        // edExpReward
        content.append("# The value of the XP drop. Default: " + DEFAULT_edExpReward + '\n');
        content.append("edExpReward: " + this.edExpReward + "\n\n");

        // edPortalSpawn
        content.append("# The way portal spawn will be handled. Default: " + DEFAULT_edPortalSpawn + '\n');
        content.append("#\n");
        content.append("#       0: Disabled. Portal will spawn normally.\n");
        content.append("#       1: Egg. Portal will be removed but not the DragonEgg\n");
        content.append("#       2: Enabled. Portal will not spawn. No more cut obsidian towers. /!\\ No Egg if dragonEggHandling=0.\n");
        content.append("#\n");
        content.append("edPortalSpawn: " + this.edPortalSpawn + "\n\n");

        // ################### //
        // ## ENDERCRYSTALS ## //
        // ################### //

        frame = new FrameBuilder();
        frame.addLine("ENDERCRYSTALS CONFIGURATION", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // ecHealthRegainRate
        content.append("# Change EnderCrystals behaviour relative to the EnderDragon. Default: " + DEFAULT_ecHealthRegainRate + '\n');
        content.append("# One important thing to understand is that Health is integer (for now).\n");
        content.append("#\n");
        content.append("#       < 1.0: Acts as a \"chance that the Dragon will regain 1 HP\" each tick\n");
        content.append("#       = 1.0: Vanilla. EnderDragon gain 1 HP per tick.\n");
        content.append("#       > 1.0: EnderDragon gain x HP per tick, so please set it to an integer value like 2 or more.\n");
        content.append("#\n");
        content.append("ecHealthRegainRate: " + this.ecHealthRegainRate + "\n\n");

        // ################## //
        // ## REGENERATION ## //
        // ################## //

        frame = new FrameBuilder();
        frame.addLine("REGENERATION CONFIGURATION", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // regenType
        content.append("# Select the regeneration type. Default: " + DEFAULT_regenType + '\n');
        content.append("#\n");
        content.append("#       0: Disabled. No hot regeneration.\n");
        content.append("#       1: Before EnderDragon respawn (only if no EnderDragon alive)\n");
        content.append("#       2: Periodic - From load time. Regen every <regenTimer> seconds after boot/load.\n");
        content.append("#       3: Periodic - Persistent. Regen every <regenTimer> seconds, persistent through reboots/reloads\n");
        content.append("#\n");
        content.append("regenType: " + this.regenType + "\n\n");

        // regenMethod
        content.append("# Select your definition of \"regen\". Default: " + DEFAULT_regenMethod + '\n');
        content.append("#\n");
        content.append("#       0: Hard Regen. Regen every chunks at once. Laggy. Not recommended.\n");
        content.append("#       1: Soft Regen. Regen chunks when they are loaded. A lot less laggy.\n");
        content.append("#       2: Crystals only. Does not modify any block, only respawn the EnderCrystals.\n");
        content.append("#\n");
        content.append("# Note: Regeneration does not regenerate Protected chunks.\n");
        content.append("#\n");
        content.append("regenMethod: " + this.regenMethod + "\n\n");

        // regenTimer
        content.append("# The time between each regen. Ignored if regenType is not Periodic (2 or 3). Default: " +
                       DEFAULT_regenTimer +
                       '\n');
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
        content.append("regenTimer: " + this.regenTimer + "\n\n");

        // regenAction
        content.append("# What do we do to players in the End when we want to regen the world? Default: " + DEFAULT_regenAction + '\n');
        content.append("#\n");
        content.append("#       0: Kick them. This way they can rejoin immediatly in the End at the same place.\n");
        content.append("#          WARNING: Mass rejoin after mass kick in the End could cause lag if regenMethod=1\n");
        content.append("#\n");
        content.append("#       1: Teleport them to the spawn point of the Main (= first) world.\n");
        content.append("#\n");
        content.append("#       2: Do nothing. Can only be used with regenMethod=2\n");
        content.append("#\n");
        content.append("regenAction: " + this.regenAction + "\n\n");

        // hardRegenOnStop
        content.append("# Activate hard regeneration on server stop. This will only slow down server stop.\n");
        content.append("# This is nice to clean the End occasionnaly when using Soft or Crystal regen.\n");
        content.append("#\n");
        content.append("#       0: Disabled.\n");
        content.append("#       1: Enabled.\n");
        content.append("#\n");
        content.append("hardRegenOnStop: " + this.hardRegenOnStop + "\n\n");

        // slowSoftRegenChunks
        content.append("# Select the number of chunks to be regen every slowSoftRegenTimer after a Soft Regeneration has started.\n");
        content.append("# Default value: " + DEFAULT_slowSoftRegenChunks + '\n');
        content.append("slowSoftRegenChunks: " + this.slowSoftRegenChunks + "\n\n");

        // slowSoftRegenTimer
        content.append("# Select the at which rate slowSoftRegenChunks chunks will be regenerated after a\n");
        content.append("# Soft Regeneration has started. Default value: " + DEFAULT_slowSoftRegenTimer + '\n');
        content.append("slowSoftRegenTimer: " + this.slowSoftRegenTimer + "\n\n");

        // ############# //
        // ## RESPAWN ## //
        // ############# //

        frame = new FrameBuilder();
        frame.addLine("RESPAWN CONFIGURATION", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // respawnNumber
        content.append("# This is the amount of EnderDragons you want to be spawned. Default: " + DEFAULT_respawnNumber + '\n');
        content.append("respawnNumber: " + this.respawnNumber + "\n\n");

        // respawnType
        content.append("# Select when you want to respawn Dragons automagically. Default: " + DEFAULT_respawnType + '\n');
        content.append("#\n");
        content.append("#       0: Disabled. No automatic respawn.\n");
        content.append("#       1: X seconds after each Dragon's death. Not really good with regenType=1.\n");
        content.append("#       2: X seconds after the last Dragon alive's death.\n");
        content.append("#       3: On server start.\n");
        content.append("#       4: Periodic - From load time. Respawn every X seconds after boot/load.\n");
        content.append("#       5: Periodic - Persistent. Respawn every X seconds, persistent through reboots/reloads\n");
        content.append("#\n");
        content.append("# IMPORTANT NOTE: Regen type 6 is buggy and was removed. Sorry for this, it will be possible again in a future version (a complete rewrite).\n");
        content.append("#\n");
        content.append("respawnType: " + this.respawnType + "\n\n");

        // respawnTimer
        content.append("# The X value in the previous comments. Defaults: " +
                       DEFAULT_respawnTimerMin +
                       " < " +
                       DEFAULT_respawnTimerMax +
                       '\n');
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
        content.append("respawnTimerMin: " + this.respawnTimerMin + '\n');
        content.append("respawnTimerMax: " + this.respawnTimerMax + "\n\n");

        // ################ //
        // ## DROP TABLE ## //
        // ################ //

        frame = new FrameBuilder();
        frame.addLine("DROP TABLE", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // dropTableHandling
        content.append("# The way the Drops will spawn. Default: " + DEFAULT_dropTableHandling + '\n');
        content.append("#\n");
        content.append("#       0: Stock. Drops will just fall from the EnderDragon death Location\n");
        content.append("#       1: Distribution. Drops will be distributed exactly like the DragonEgg\n");
        content.append("#\n");
        content.append("dropTableHandling: " + this.dropTableHandling + "\n\n");

        content.append("# Drop table for the EnderDragons. Complete informations: http://ribe.sg/is-config\n");
        content.append("# Example drop table:\n");
        content.append("#\n");
        try {
            final YamlConfiguration dummyConfig = new YamlConfiguration();
            final ConfigurationSection dummySection = dummyConfig.createSection("dropTable");
            final ConfigurationSection exampleDropSection = dummySection.createSection("drop1");
            final ItemStack is = new ItemStack(Material.DIAMOND_SWORD);
            final ItemMeta meta = is.getItemMeta();
            meta.setDisplayName("The Great Example Sword");
            meta.setLore(Arrays.asList("Such sword", "Very diamond", "Wow"));
            is.setItemMeta(meta);
            exampleDropSection.set("probability", 0.25);
            ItemStackUtil.saveToConfigSection(exampleDropSection, "itemStack", is);
            content.append(StringUtil.prependLines(dummyConfig.saveToString(), "# "));
        } catch (final InventoryUtilException e) {
            this.plugin.error("Failed to save example ItemStack!", e);
        }
        content.append('\n');
        try {
            final YamlConfiguration dummyConfig = new YamlConfiguration();
            final ConfigurationSection dummySection = dummyConfig.createSection("dropTable");
            int i = 0;
            for (final Pair<ItemStack, Float> p : this.dropTable) {
                final ConfigurationSection exampleDropSection = dummySection.createSection("drop" + ++i);
                exampleDropSection.set("probability", p.getValue());
                ItemStackUtil.saveToConfigSection(exampleDropSection, "itemStack", p.getKey());
            }
            content.append(dummyConfig.saveToString());
        } catch (final InventoryUtilException e) {
            this.plugin.error("Failed to save DropTable!", e);
        }
        content.append('\n');

        // ###################### //
        // ## CHUNK PROTECTION ## //
        // ###################### //

        frame = new FrameBuilder();
        frame.addLine("CHUNK PROTECTION", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // defaultProtected
        content.append("# Default state of newly first-generated chunks. Default: " + DEFAULT_defaultProtected + '\n');
        content.append("#\n");
        content.append("#       true:  Protected from regeneration\n");
        content.append("#       false: Unprotected from regeneration\n");
        content.append("#\n");
        content.append("defaultProtected: " + this.defaultProtected + "\n\n");

        // ########## //
        // ## DATA ## //
        // ########## //

        frame = new FrameBuilder();
        frame.addLine("DATA - PLEASE DO NOT TOUCH!", FrameBuilder.Option.CENTER);
        for (final String line : frame.build()) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // nextRegenTaskTime
        content.append("# Used to allow Regen task timer persistence. /!\\ PLEASE DO NOT TOUCH THIS !\n");
        content.append("nextRegenTaskTime: " + (this.regenTimer == 0 ? "0" : this.nextRegenTaskTime) + "\n\n");

        // nextRespawnTaskTime
        content.append("# Used to allow Respawn task timer persistence. /!\\ PLEASE DO NOT TOUCH THIS !\n");
        content.append("nextRespawnTaskTime: " + (this.respawnTimerMax == 0 ? "0" : this.nextRespawnTaskTime) + "\n\n");

        return content.toString();
    }

    // General

    public int getFilterMovedTooQuicklySpam() {
        return this.filterMovedTooQuicklySpam;
    }

    private void setFilterMovedTooQuicklySpam(final int filterMovedTooQuicklySpam) {
        this.filterMovedTooQuicklySpam = filterMovedTooQuicklySpam;
    }

    // EnderDragons

    public float getEdDamageMultiplier() {
        return this.edDamageMultiplier;
    }

    private void setEdDamageMultiplier(final float edDamageMultiplier) {
        this.edDamageMultiplier = edDamageMultiplier;
    }

    public int getEdEggHandling() {
        return this.edEggHandling;
    }

    private void setEdEggHandling(final int edEggHandling) {
        this.edEggHandling = edEggHandling;
    }

    public int getEdExpHandling() {
        return this.edExpHandling;
    }

    private void setEdExpHandling(final int edExpHandling) {
        this.edExpHandling = edExpHandling;
    }

    public int getEdExpReward() {
        return this.edExpReward;
    }

    private void setEdExpReward(final int edExpReward) {
        this.edExpReward = edExpReward;
    }

    public int getEdHealth() {
        return this.edHealth;
    }

    private void setEdHealth(final int edHealth) {
        this.edHealth = edHealth;
    }

    public int getEdPortalSpawn() {
        return this.edPortalSpawn;
    }

    private void setEdPortalSpawn(final int edPortalSpawn) {
        this.edPortalSpawn = edPortalSpawn;
    }

    public int getEdPushesPlayers() {
        return this.edPushesPlayers;
    }

    private void setEdPushesPlayers(final int edPushesPlayers) {
        this.edPushesPlayers = edPushesPlayers;
    }

    public float getEdPushForce() {
        return this.edPushForce;
    }

    public void setEdPushForce(final float edPushForce) {
        this.edPushForce = edPushForce;
    }

    // EnderCrystals

    public float getEcHealthRegainRate() {
        return this.ecHealthRegainRate;
    }

    public void setEcHealthRegainRate(final float ecHealthRegainRate) {
        this.ecHealthRegainRate = ecHealthRegainRate;
    }

    // Regeneration

    public int getHardRegenOnStop() {
        return this.hardRegenOnStop;
    }

    private void setHardRegenOnStop(final int hardRegenOnStop) {
        this.hardRegenOnStop = hardRegenOnStop;
    }

    public int getRegenAction() {
        return this.regenAction;
    }

    private void setRegenAction(final int regenAction) {
        this.regenAction = regenAction;
    }

    public int getRegenMethod() {
        return this.regenMethod;
    }

    private void setRegenMethod(final int regenMethod) {
        this.regenMethod = regenMethod;
    }

    public int getRegenTimer() {
        return this.regenTimer;
    }

    private void setRegenTimer(final int regenTimer) {
        this.regenTimer = regenTimer;
    }

    public int getRegenType() {
        return this.regenType;
    }

    private void setRegenType(final int regenType) {
        this.regenType = regenType;
    }

    public int getSlowSoftRegenChunks() {
        return this.slowSoftRegenChunks;
    }

    public void setSlowSoftRegenChunks(final int slowSoftRegenChunks) {
        this.slowSoftRegenChunks = slowSoftRegenChunks;
    }

    public int getSlowSoftRegenTimer() {
        return this.slowSoftRegenTimer;
    }

    public void setSlowSoftRegenTimer(final int slowSoftRegenTimer) {
        this.slowSoftRegenTimer = slowSoftRegenTimer;
    }

    // Respawn

    public int getRespawnNumber() {
        return this.respawnNumber;
    }

    private void setRespawnNumber(final int respawnNumber) {
        this.respawnNumber = respawnNumber;
    }

    public int getRespawnTimerMax() {
        return this.respawnTimerMax;
    }

    private void setRespawnTimerMax(final int respawnTimerMax) {
        this.respawnTimerMax = respawnTimerMax;
    }

    public int getRespawnTimerMin() {
        return this.respawnTimerMin;
    }

    private void setRespawnTimerMin(final int respawnTimerMin) {
        this.respawnTimerMin = respawnTimerMin;
    }

    public int getRandomRespawnTimer() {
        final int respawnTimerDiff = this.respawnTimerMax - this.respawnTimerMin;
        return respawnTimerDiff == 0 ? 0 : RANDOM.nextInt(respawnTimerDiff) + this.respawnTimerMin;
    }

    public int getRespawnType() {
        return this.respawnType;
    }

    private void setRespawnType(final int respawnType) {
        this.respawnType = respawnType;
    }

    // Drop Table

    public int getDropTableHandling() {
        return this.dropTableHandling;
    }

    private void setDropTableHandling(final int dropTableHandling) {
        this.dropTableHandling = dropTableHandling;
    }

    public PairList<ItemStack, Float> getDropTable() {
        return this.dropTable;
    }

    // Chunk Protection

    public boolean getDefaultProtected() {
        return this.defaultProtected;
    }

    public void setDefaultProtected(final boolean defaultProtected) {
        this.defaultProtected = defaultProtected;
    }

    // Data

    public long getNextRegenTaskTime() {
        return this.nextRegenTaskTime;
    }

    public void setNextRegenTaskTime(final long nextRegenTaskTime) {
        this.nextRegenTaskTime = nextRegenTaskTime;
    }

    public long getNextRespawnTaskTime() {
        return this.nextRespawnTaskTime;
    }

    public void setNextRespawnTaskTime(final long nextRespawnTaskTime) {
        this.nextRespawnTaskTime = nextRespawnTaskTime;
    }

    // Others

    public String getWorldName() {
        return this.worldName;
    }
}

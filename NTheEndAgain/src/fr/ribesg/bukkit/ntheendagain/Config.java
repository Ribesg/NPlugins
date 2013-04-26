package fr.ribesg.bukkit.ntheendagain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.Utils;

public class Config extends AbstractConfig<NTheEndAgain> {

    private final String                               worldName;

    // General
    private final static int                           DEFAULT_filterMovedTooQuicklySpam = 1;
    @Getter @Setter(AccessLevel.PRIVATE) private int   filterMovedTooQuicklySpam;

    // EnderDragon

    private final static int                           DEFAULT_edHealth                  = 200;
    @Getter @Setter(AccessLevel.PRIVATE) private int   edHealth;

    private final static float                         DEFAULT_edDamageMultiplier        = 1.0f;
    @Getter @Setter(AccessLevel.PRIVATE) private float edDamageMultiplier;

    private final static int                           DEFAULT_edPushesPlayers           = 1;
    @Getter @Setter(AccessLevel.PRIVATE) private int   edPushesPlayers;

    private final static int                           DEFAULT_edEggHandling             = 0;
    @Getter @Setter(AccessLevel.PRIVATE) private int   edEggHandling;

    private final static int                           DEFAULT_edExpHandling             = 0;
    @Getter @Setter(AccessLevel.PRIVATE) private int   edExpHandling;

    private final static int                           DEFAULT_edExpReward               = 12_000;
    @Getter @Setter(AccessLevel.PRIVATE) private int   edExpReward;

    private final static int                           DEFAULT_edPortalSpawn             = 0;
    @Getter @Setter(AccessLevel.PRIVATE) private int   edPortalSpawn;

    // Regeneration
    private final static int                           DEFAULT_regenType                 = 0;
    @Getter @Setter(AccessLevel.PRIVATE) private int   regenType;
    // 0 - No Regen
    // 1 - Before respawn
    // 2 - On server stop (on reboot)
    // 3 - Periodic - From boot time
    // 4 - Periodic - Persistent

    private final static int                           DEFAULT_regenMethod               = 0;
    @Getter @Setter(AccessLevel.PRIVATE) private int   regenMethod;
    // 0 - Hard regen (Recommended option for regenType 2)
    // 1 - Soft regen
    // 2 - Crystals only

    private final static int                           DEFAULT_regenTimer                = 86_400; // 24 hours
    @Getter @Setter(AccessLevel.PRIVATE) private int   regenTimer;

    private final static int                           DEFAULT_regenAction               = 0;
    @Getter @Setter(AccessLevel.PRIVATE) private int   regenAction;

    // Respawn

    private final static int                           DEFAULT_respawnNumber             = 1;
    @Getter @Setter(AccessLevel.PRIVATE) private int   respawnNumber;

    private final static int                           DEFAULT_respawnType               = 0;
    @Getter @Setter(AccessLevel.PRIVATE) private int   respawnType;
    // 0 - No respawn
    // 1 - After 1 Dragon killed
    // 2 - After all Dragons killed
    // 3 - On server start (on reboot)
    // 4 - Periodic with random time range - From boot time
    // 5 - Periodic with random time range - Persistent

    private final static int                           DEFAULT_respawnTimerMin           = 7_200;
    @Getter @Setter(AccessLevel.PRIVATE) private int   respawnTimerMin;

    private final static int                           DEFAULT_respawnTimerMax           = 14_400;
    @Getter @Setter(AccessLevel.PRIVATE) private int   respawnTimerMax;

    // Data

    private final static long                          DEFAULT_nextRegenTaskTime         = 0;
    @Getter @Setter private long                       nextRegenTaskTime;

    private final static long                          DEFAULT_nextRespawnTaskTime       = 0;
    @Getter @Setter private long                       nextRespawnTaskTime;

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

        // Regeneration
        setRegenType(DEFAULT_regenType);
        setRegenTimer(DEFAULT_regenTimer);
        setRegenMethod(DEFAULT_regenMethod);
        setRegenAction(DEFAULT_regenAction);

        // Respawn
        setRespawnNumber(DEFAULT_respawnNumber);
        setRespawnType(DEFAULT_respawnType);
        setRespawnTimerMin(DEFAULT_respawnTimerMin);
        setRespawnTimerMax(DEFAULT_respawnTimerMax);

        // Data
        setNextRegenTaskTime(DEFAULT_nextRegenTaskTime);
        setNextRespawnTaskTime(DEFAULT_nextRespawnTaskTime);
    }

    /**
     * @see AbstractConfig#setValues(YamlConfiguration)
     */
    @Override
    protected void setValues(final YamlConfiguration config) {

        String fileName = Utils.toLowerCamelCase(worldName) + "Config.yml";

        // General
        setFilterMovedTooQuicklySpam(config.getInt("filterMovedTooQuicklySpam", DEFAULT_filterMovedTooQuicklySpam));
        if (getFilterMovedTooQuicklySpam() < 0 || getFilterMovedTooQuicklySpam() > 1) {
            wrongValue(fileName, "filterMovedTooQuicklySpam", getFilterMovedTooQuicklySpam(), DEFAULT_filterMovedTooQuicklySpam);
            setFilterMovedTooQuicklySpam(DEFAULT_filterMovedTooQuicklySpam);
        }

        // EnderDragon
        setEdHealth(config.getInt("edHealth", DEFAULT_edHealth));
        if (getEdHealth() < 1) {
            wrongValue(fileName, "edHealth", getEdHealth(), DEFAULT_edHealth);
            setEdHealth(DEFAULT_edHealth);
        }

        setEdDamageMultiplier((float) config.getDouble("edDamageMultiplier", DEFAULT_edDamageMultiplier));
        if (getEdDamageMultiplier() < 0) {
            wrongValue(fileName, "edDamageMultiplier", getEdDamageMultiplier(), DEFAULT_edDamageMultiplier);
            setEdDamageMultiplier(DEFAULT_edDamageMultiplier);
        }
        
        // TODO Finish Config refactor !

    }

    /**
     * @see AbstractConfig#getConfigString()
     */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();

        // ############
        // ## HEADER ##
        // ############

        String[] headerLines = new String[3];
        headerLines[0] = "Config file for NTheEndAgain plugin. If you don't understand something,";
        headerLines[1] = "please ask on dev.bukkit.org or on forum post.";
        headerLines[2] = "                                                          Ribesg";
        for (String line : Utils.frame(headerLines)) {
            content.append(line);
        }

        content.append("# This config file is about the world \"" + worldName + "\"\n\n");

        // #############
        // ## GENERAL ##
        // #############

        for (String line : Utils.frame("GENERAL CONFIGURATION")) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // filterMovedTooQuicklySpam
        content.append("# Do we hide the 'Player Moved Too Quickly!' spam? Default: " + DEFAULT_filterMovedTooQuicklySpam + "\n");
        content.append("# /!\\ This feature is not compatible with any other plugin using Bukkit's Logger filters\n");
        content.append("#       0: Disabled.\n");
        content.append("#       1: Enabled.\n");
        content.append("# Note: to completely disable the filter and allow compatibility with other plugins using it,\n");
        content.append("#       please be sure to set it to 0 in EVERY End World config file.\n");
        content.append("filterMovedTooQuicklySpam: " + getFilterMovedTooQuicklySpam() + "\n\n");

        // #################
        // ## ENDERDRAGON ##
        // #################

        content.append('\n');
        for (String line : Utils.frame("ENDERDRAGON CONFIGURATION")) {
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
        content.append("#       0: Disabled.\n");
        content.append("#       1: Enabled.\n");
        content.append("edPushesPlayers: " + getEdPushesPlayers() + "\n\n");

        // edEggHandling
        content.append("# The way the DragonEgg will spawn. Default: " + DEFAULT_edEggHandling + "\n");
        content.append("#       0: Disabled. The egg will spawn normally if portalHandling is set to 0 or 1.\n");
        content.append("#       1: Enabled. The egg will be semi-randomly given to one of the best fighters.\n");
        content.append("edEggHandling: " + getEdEggHandling() + "\n\n");

        // edExpHandling
        content.append("# The way the reward XP will be given to player. Default: " + DEFAULT_edExpHandling + "\n");
        content.append("#       0: Disabled. XP orbs will spawn normally.\n");
        content.append("#       1: Enabled. XP will be splitted between fighters, more XP for better fighters.\n");
        content.append("edExpHandling: " + getEdExpHandling() + "\n\n");

        // edExpReward
        content.append("# The value of the XP drop. Default: " + DEFAULT_edExpReward + "\n");
        content.append("edExpReward: " + getEdExpReward() + "\n\n");

        // edPortalSpawn
        content.append("# The way portal spawn will be handled. Default: " + DEFAULT_edPortalSpawn + "\n");
        content.append("#       0: Disabled. Portal will spawn normally.\n");
        content.append("#       1: Egg. Portal will be removed but not the DragonEgg\n");
        content.append("#       2: Enabled. Portal will not spawn. No more cut obsidian towers. /!\\ No Egg if dragonEggHandling=0.\n");
        content.append("edPortalSpawn: " + getEdPortalSpawn() + "\n\n");

        // ##################
        // ## REGENERATION ##
        // ##################

        content.append('\n');
        for (String line : Utils.frame("REGENERATION CONFIGURATION")) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // regenType
        content.append("# Select the regeneration type. Default: " + DEFAULT_regenType + "\n");
        content.append("#       0: Disabled. No regeneration.\n");
        content.append("#       1: Before EnderDragon respawn (only if no EnderDragon alive)\n");
        content.append("#       2: On server stop. Use this with Hard Regen method.\n");
        content.append("#       3: Periodic - From load time. Regen every <regenTimer> seconds after boot/load.\n");
        content.append("#       4: Periodic - Persistent. Regen every <regenTimer> seconds, persistent through reboots/reloads\n");
        content.append("regenType: " + getRegenType() + "\n\n");

        // regenMethod
        content.append("# Select your definition of \"regen\". Default: " + DEFAULT_regenMethod + "\n");
        content.append("#       0: Hard Regen. Regen every chunks at once. Laggy. Not recommended without regenType=2");
        content.append("#       1: Soft Regen. Regen chunks when they are loaded. A lot less laggy.");
        content.append("#       2: Crystals only. Does not modify any block, only respawn the EnderCrystals.");
        content.append("regenMethod: " + getRegenMethod() + "\n\n");

        // regenTimer
        content.append("# The time between each regen. Ignored if regenType is not Periodic. Default: " + DEFAULT_regenTimer + "\n");
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
        content.append("# Note: You should NOT use low value. Some hours of delay are recommended.\n");
        content.append("regenTimer: " + getRegenTimer() + "\n\n");

        // regenAction
        content.append("# What do we do to players in the End when we want to regen the world? Default: " + DEFAULT_regenAction + "\n");
        content.append("#       0: Kick them. This way they can rejoin immediatly in the End\n");
        content.append("#          WARNING: Mass rejoin after mass kick in the End could cause lag if regenMethod=1\n");
        content.append("#       1: Teleport them to the spawn point of the Main (= first) world.\n");
        content.append("regenAction: " + getRegenAction() + "\n\n");

        // #############
        // ## RESPAWN ##
        // #############

        content.append('\n');
        for (String line : Utils.frame("RESPAWN CONFIGURATION")) {
            content.append(line);
            content.append('\n');
        }
        content.append('\n');

        // respawnNumber
        content.append("# This is the amount of EnderDragons you want to be spawned. Default: " + DEFAULT_respawnNumber + "\n");
        content.append("respawnNumber: " + getRespawnNumber() + "\n\n");

        // respawnType
        content.append("# Select when you want to respawn Dragons automagically. Default: " + DEFAULT_respawnType + "\n");
        content.append("#       0: Disabled. No automatic respawn.\n");
        content.append("#       1: After each Dragon's death. Not really good with regenType=1.\n");
        content.append("#       2: After the last Dragon alive's death.\n");
        content.append("#       3: On server start.\n");
        content.append("#       4: Periodic - From load time. Respawn every X seconds after boot/load.\n");
        content.append("#       5: Periodic - Persistent. Respawn every X seconds, persistent through reboots/reloads\n");
        content.append("respawnType: " + getRespawnType() + "\n\n");

        // respawnTimer
        content.append("# The X value in the previous comments. Defaults: " + DEFAULT_respawnTimerMin + " < " + DEFAULT_respawnTimerMax + "\n");
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
        content.append("# You can use *any* strictly positive value you want, just be sure to convert it to seconds.\n");
        content.append("# Note: You CAN use low value if regenType is not set to 1.\n");
        content.append("#       But maybe you should consider using respawnType=1 or respawnType=2 instead of a low periodic.\n");
        content.append("respawnTimerMin: " + getRespawnTimerMin() + "\n");
        content.append("respawnTimerMax: " + getRespawnTimerMax() + "\n\n");

        // ##########
        // ## DATA ##
        // ##########

        content.append('\n');
        for (String line : Utils.frame("DATA - PLEASE DO NOT TOUCH !")) {
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
}

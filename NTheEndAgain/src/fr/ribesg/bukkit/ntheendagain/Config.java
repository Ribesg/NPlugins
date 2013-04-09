package fr.ribesg.bukkit.ntheendagain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncore.lang.MessageId;

public class Config extends AbstractConfig {

    private final NTheEndAgain                         plugin;
    private final String                               worldName;

    @Getter @Setter(AccessLevel.PRIVATE) private int   nbEnderDragons;
    @Getter @Setter(AccessLevel.PRIVATE) private int   enderDragonHealth;
    @Getter @Setter(AccessLevel.PRIVATE) private float enderDragonDamageMultiplier;
    @Getter @Setter(AccessLevel.PRIVATE) private int   portalHandling;
    @Getter @Setter(AccessLevel.PRIVATE) private int   dragonEggHandling;
    @Getter @Setter(AccessLevel.PRIVATE) private int   xpHandling;
    @Getter @Setter(AccessLevel.PRIVATE) private int   xpReward;
    @Getter @Setter(AccessLevel.PRIVATE) private int   respawnTimer;
    @Getter @Setter(AccessLevel.PRIVATE) private int   respawnOnBoot;
    @Getter @Setter(AccessLevel.PRIVATE) private int   regenOnRespawn;
    @Getter @Setter(AccessLevel.PRIVATE) private int   actionOnRegen;
    @Getter @Setter private long                       lastTaskExecTime;

    public Config(final NTheEndAgain instance, final String world) {
        plugin = instance;
        worldName = world;

        setNbEnderDragons(1);
        setEnderDragonHealth(200);
        setEnderDragonDamageMultiplier(1.0f);
        setPortalHandling(0);
        setDragonEggHandling(0);
        setXpHandling(0);
        setXpReward(20_000);
        setRespawnTimer(0);
        setRespawnOnBoot(1);
        setRegenOnRespawn(1);
        setActionOnRegen(0);
        setLastTaskExecTime(0);
    }

    /**
     * @see AbstractConfig#setValues(YamlConfiguration)
     */
    @Override
    protected void setValues(final YamlConfiguration config) {

        // nbEnderDragons. Default: 1. Possible values: positive integers
        setNbEnderDragons(config.getInt("nbEnderDragons", 1));
        if (getNbEnderDragons() < 0) {
            setNbEnderDragons(1);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "nbEnderDragons", "1");
        }

        // enderDragonHealth. Default: 200. Possible values: positive integers
        setEnderDragonHealth(config.getInt("enderDragonHealth", 200));
        if (getEnderDragonHealth() < 0) {
            setEnderDragonHealth(200);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "enderDragonHealth", "200");
        }

        // enderDragonDamageMultiplier. Default: 1.0. Possible values: positive floats
        setEnderDragonDamageMultiplier((float) config.getDouble("enderDragonDamageMultiplier", 1.0f));
        if (getEnderDragonDamageMultiplier() < 0.0) {
            setEnderDragonDamageMultiplier(1.0f);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml",
                            "enderDragonDamageMultiplier", "1.0");
        }

        // portalHandling. Default: 0. Possible values: 0,1,2
        setPortalHandling(config.getInt("portalHandling", 0));
        if (getPortalHandling() < 0 || getPortalHandling() > 2) {
            setPortalHandling(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "portalHandling", "0");
        }

        // dragonEggHandling. Default: 0. Possible values: 0,1
        setDragonEggHandling(config.getInt("dragonEggHandling", 0));
        if (getDragonEggHandling() < 0 || getDragonEggHandling() > 1) {
            setDragonEggHandling(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "dragonEggHandling", "0");
        }

        // xpHandling. Default: 0. Possible values: 0,1
        setXpHandling(config.getInt("xpHandling", 0));
        if (getXpHandling() < 0 || getXpHandling() > 1) {
            setXpHandling(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "xpHandling", "0");
        }

        // xpReward. Default: 20 000. Possible values: positive or null integers
        setXpReward(config.getInt("xpReward", 20_000));
        if (getXpReward() < 0) {
            setXpReward(20_000);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "xpReward", "20 000");
        }

        // respawnTimer. Default: 0. Possible values: positive or null integers
        setRespawnTimer(config.getInt("respawnTimer", 0));
        if (getRespawnTimer() < 0) {
            setRespawnTimer(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "respawnTimer", "0");
        }

        // respawnOnBoot. Default: 1. Possible values: 0,1
        setRespawnOnBoot(config.getInt("respawnOnBoot", 1));
        if (getRespawnOnBoot() < 0 || getRespawnOnBoot() > 1) {
            setRespawnOnBoot(1);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "respawnOnBoot", "1");
        }

        // regenOnRespawn. Default: 1. Possible values: 0,1
        setRegenOnRespawn(config.getInt("regenOnRespawn", 1));
        if (getRegenOnRespawn() < 0 || getRegenOnRespawn() > 1) {
            setRegenOnRespawn(1);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "regenOnRespawn", "1");
        }

        // actionOnRegen. Default: 0. Possible values: 0,1
        setActionOnRegen(config.getInt("actionOnRegen", 0));
        if (getActionOnRegen() < 0 || getActionOnRegen() > 1) {
            setActionOnRegen(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "actionOnRegen", "0");
        }

        // lastTaskStartTime.
        setLastTaskExecTime(config.getInt("lastTaskStartTime", 0));
        if (getLastTaskExecTime() < 0 || getLastTaskExecTime() > System.currentTimeMillis()) {
            setLastTaskExecTime(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, Utils.toLowerCamelCase(worldName) + "Config.yml", "lastTaskStartTime", "0");
        }

    }

    /**
     * @see AbstractConfig#getConfigString()
     */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();

        // Header
        content.append("################################################################################\n");
        content.append("# Config file for NTheEndAgain plugin. If you don't understand something,      #\n");
        content.append("# please ask on dev.bukkit.org or on forum post.                        Ribesg #\n");
        content.append("################################################################################\n\n");

        content.append("# This config file is about the world \"" + worldName + "\"\n\n");

        // nbEnderDragons. Default: 1
        content.append("# The number of EnderDragons that will be at the same time in an End world. Default: 1\n");
        content.append("nbEnderDragons: " + getNbEnderDragons() + "\n\n");

        // enderDragonHealth. Default: 200
        content.append("# The health value EnderDragons will spawn with. Default: 200\n");
        content.append("enderDragonHealth: " + getEnderDragonHealth() + "\n\n");

        // enderDragonDamageMultiplier. Default: 1.0
        content.append("# Scale damages done by EnderDragon. Default: 1.0\n");
        content.append("enderDragonDamageMultiplier: " + getEnderDragonDamageMultiplier() + "\n\n");

        // portalHandling. Default: 0
        content.append("# The way portal spawn will be handled. Default: 0\n");
        content.append("# 	0: Disabled. Portal will spawn normally.\n");
        content.append("# 	1: Egg. Portal will be removed but not the DragonEgg\n");
        content.append("# 	2: Enabled. Portal will not spawn. No more cuted obsidian towers. No Egg if dragonEggHandling=0.\n");
        content.append("portalHandling: " + getPortalHandling() + "\n\n");

        // dragonEggHandling. Default: 0
        content.append("# The way the DragonEgg will spawn. Default: 0\n");
        content.append("# 	0: Disabled. The egg will spawn normally if portalHandling is set to 0 or 1.\n");
        content.append("# 	1: Enabled. The egg will be semi-randomly given to one of the best fighters.\n");
        content.append("dragonEggHandling: " + getDragonEggHandling() + "\n\n");

        // xpHandling. Default: 0
        content.append("# The way the reward XP will be given to player. Default: 0\n");
        content.append("# 	0: Disabled. XP orbs will spawn normally.\n");
        content.append("# 	1: Enabled. XP will be splitted between fighters, more XP for better fighters.\n");
        content.append("xpHandling: " + getXpHandling() + "\n\n");

        // xpReward. Default: 20 000
        content.append("# The value of the XP drop. Default: 20 000\n");
        content.append("xpReward: " + getXpReward() + "\n\n");

        // respawnTimer. Default: 21 600 (6 hours)
        content.append("# The time between checks for respawning EnderDragons, in seconds. Default: 0 (Disabled)\n");
        content.append("# Here are some values:\n");
        content.append("#           0: Disabled\n");
        content.append("#       1 800: 30 minutes\n");
        content.append("#       3 600: 1 hour\n");
        content.append("#       7 200: 2 hours\n");
        content.append("#      10 800: 3 hours\n");
        content.append("#      14 400: 4 hours\n");
        content.append("#      21 600: 6 hours\n");
        content.append("#      28 800: 8 hours\n");
        content.append("#      43 200: 12 hours\n");
        content.append("#      86 400: 24 hours - 1 day\n");
        content.append("#     172 800: 48 hours - 2 days\n");
        content.append("#     604 800: 7 days\n");
        content.append("respawnTimer: " + getRespawnTimer() + "\n\n");

        // respawnOnBoot. Default: 1
        content.append("# Should we respawn EnderDragons at server boot. Default: 1\n");
        content.append("#       0: Disabled.\n");
        content.append("#       1: Enabled. There will be nbEnderDragons (" + getNbEnderDragons() + ") in this world after each reboot\n");
        content.append("respawnOnBoot: " + getRespawnOnBoot() + "\n\n");

        // regenOnRespawn. Default: 1
        content.append("# Should we regen the End world before respawning Dragons ? Default: 1\n");
        content.append("#       0: Disabled.\n");
        content.append("#       1: Enabled. World will be regen if:\n");
        content.append("#                                           1) There is no EnderDragon alive\n");
        content.append("#                                           2) We are respawning one or more Dragons\n");
        content.append("regenOnRespawn: " + getRegenOnRespawn() + "\n\n");

        // actionOnRegen. Default: 0
        content.append("# What do we do to players in the End when we want to regen the world ? Default: 1\n");
        content.append("#       0: Kick them. This way they can rejoin immediatly in the End\n");
        content.append("#          WARNING: Mass rejoin after mass kick in the End could cause lag because chunks are\n");
        content.append("#                   regen on chunk loading and mass join = mass load of chunks at the same time\n");
        content.append("#       1: Teleport them to the spawn point of the Main (= first) world.\n");
        content.append("actionOnRegen: " + getActionOnRegen() + "\n\n");

        // lastTaskStartTime. Default: 0
        content.append("# Used to allow task timer persistence. /!\\ PLEASE DO NOT TOUCH THIS !\n");
        content.append("lastTaskStartTime: " + getLastTaskExecTime() + "\n\n");

        return content.toString();
    }
}

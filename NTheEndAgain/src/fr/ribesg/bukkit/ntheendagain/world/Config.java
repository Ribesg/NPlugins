package fr.ribesg.bukkit.ntheendagain.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.lang.Messages.MessageId;

public class Config {

    private final static Charset                         CHARSET = Charset.defaultCharset();

    private final NTheEndAgain                           plugin;

    @Getter @Setter(AccessLevel.PRIVATE) private int     nbEnderDragons;
    @Getter @Setter(AccessLevel.PRIVATE) private int     enderDragonHealth;
    @Getter @Setter(AccessLevel.PRIVATE) private float   enderDragonDamageMultiplier;
    @Getter @Setter(AccessLevel.PRIVATE) private int     portalHandling;
    @Getter @Setter(AccessLevel.PRIVATE) private int     dragonEggHandling;
    @Getter @Setter(AccessLevel.PRIVATE) private int     xpHandling;
    @Getter @Setter(AccessLevel.PRIVATE) private int     xpReward;
    @Getter @Setter(AccessLevel.PRIVATE) private int     respawnTimer;
    @Getter @Setter(AccessLevel.PRIVATE) private boolean respawnOnBoot;
    @Getter @Setter(AccessLevel.PRIVATE) private int     regenOnRespawn;

    public Config(final NTheEndAgain instance) {
        plugin = instance;
    }

    public void loadConfig(final Path pathConfig) throws IOException {
        if (!Files.exists(pathConfig)) {
            Files.createFile(pathConfig);
            writeConfig(pathConfig);
        } else {
            final YamlConfiguration config = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(pathConfig, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine() + '\n');
                }
                config.loadFromString(s.toString());
            } catch (final Exception e) {
                e.printStackTrace();
            }

            // nbEnderDragons. Default: 1. Possible values : positive integers
            setNbEnderDragons(config.getInt("nbEnderDragons", 1));
            if (getNbEnderDragons() < 0) {
                setNbEnderDragons(1);
                plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, pathConfig.getFileName().toString(), "nbEnderDragons", "1");
            }

            // enderDragonHealth. Default: 200. Possible values : positive integers
            setEnderDragonHealth(config.getInt("enderDragonHealth", 200));
            if (getEnderDragonHealth() < 0) {
                setEnderDragonHealth(200);
                plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, pathConfig.getFileName().toString(), "enderDragonHealth", "200");
            }

            // enderDragonDamageMultiplier. Default: 1.0. Possible values : positive floats
            setEnderDragonDamageMultiplier((float) config.getDouble("enderDragonDamageMultiplier", 1.0f));
            if (getEnderDragonDamageMultiplier() < 0.0) {
                setEnderDragonDamageMultiplier(1.0f);
                plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, pathConfig.getFileName().toString(), "enderDragonDamageMultiplier", "1.0");
            }

            // portalHandling. Default: 0. Possible values : 0,1,2
            setPortalHandling(config.getInt("portalHandling", 0));
            if (getPortalHandling() < 0 || getPortalHandling() > 2) {
                setPortalHandling(0);
                plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, pathConfig.getFileName().toString(), "portalHandling", "0");
            }

            // dragonEggHandling. Default: 0. Possible values : 0,1
            setDragonEggHandling(config.getInt("dragonEggHandling", 0));
            if (getDragonEggHandling() < 0 || getDragonEggHandling() > 1) {
                setDragonEggHandling(0);
                plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, pathConfig.getFileName().toString(), "dragonEggHandling", "0");
            }

            // xpHandling. Default: 0. Possible values : 0,1
            setXpHandling(config.getInt("xpHandling", 0));
            if (getXpHandling() < 0 || getXpHandling() > 1) {
                setXpHandling(0);
                plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, pathConfig.getFileName().toString(), "xpHandling", "0");
            }

            // xpReward. Default: 20 000. Possible values : positive (or null) integers
            setXpReward(config.getInt("xpReward", 20_000));
            if (getXpReward() < 0) {
                setXpReward(20_000);
                plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, pathConfig.getFileName().toString(), "xpReward", "20000");
            }

            // Rewrite the config to "clean" it
            writeConfig(pathConfig);
        }
    }

    public void writeConfig(final Path pathConfig) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(pathConfig, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            final StringBuilder content = new StringBuilder();

            // Header
            content.append("################################################################################\n");
            content.append("# Config file for NTheEndAgain plugin. If you don't understand something,      #\n");
            content.append("# please ask on dev.bukkit.org or on forum post.                        Ribesg #\n");
            content.append("################################################################################\n\n");

            content.append("# This config file is about the world \"" + pathConfig.getFileName().toString().substring(0, pathConfig.getFileName().toString().length() - 4) + "\"\n\n");

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
            content.append("# 	2: Enabled. Portal will not spawn. No more cuted obsidian towers. No Egg.\n");
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

            writer.write(content.toString());
        }
    }
}

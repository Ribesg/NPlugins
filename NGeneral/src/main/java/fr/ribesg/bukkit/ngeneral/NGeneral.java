/***************************************************************************
 * Project file:    NPlugins - NGeneral - NGeneral.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.NGeneral                     *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral;

import fr.ribesg.bukkit.ncore.info.Info;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ngeneral.config.Config;
import fr.ribesg.bukkit.ngeneral.config.DbConfig;
import fr.ribesg.bukkit.ngeneral.feature.Features;
import fr.ribesg.bukkit.ngeneral.feature.spymode.SpyModeFeature;
import fr.ribesg.bukkit.ngeneral.lang.Messages;
import fr.ribesg.bukkit.ngeneral.simplefeature.BusyCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.FlySpeedCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.HealFoodCommands;
import fr.ribesg.bukkit.ngeneral.simplefeature.NightVisionCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.RepairCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.SignColorsListener;
import fr.ribesg.bukkit.ngeneral.simplefeature.TeleportCommands;
import fr.ribesg.bukkit.ngeneral.simplefeature.TimeCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WalkSpeedCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WeatherCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WelcomeListener;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

public class NGeneral extends NPlugin implements GeneralNode {

    // Configs
    private Messages messages;
    private Config   pluginConfig;
    private DbConfig dbConfig;

    // Features
    private Features features;

    @Override
    protected String getMinCoreVersion() {
        return "0.6.10";
    }

    @Override
    protected void loadMessages() throws IOException {
        this.debug("Loading plugin Messages...");
        if (!this.getDataFolder().isDirectory()) {
            this.getDataFolder().mkdir();
        }

        final Messages messages = new Messages();
        messages.loadMessages(this);

        this.messages = messages;
    }

    @Override
    protected boolean onNodeEnable() {
        // Config
        try {
            this.pluginConfig = new Config(this);
            this.pluginConfig.loadConfig();
        } catch (final IOException | InvalidConfigurationException e) {
            this.getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            this.getLogger().severe("This error occured when NGeneral tried to load config.yml");
            return false;
        }

        // Features
        this.features = new Features(this);

        // Db
        try {
            this.dbConfig = new DbConfig(this);
            this.dbConfig.loadConfig("db.yml");
        } catch (final IOException | InvalidConfigurationException e) {
            this.getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            this.getLogger().severe("This error occured when NGeneral tried to load db.yml");
            return false;
        }

        // Feature init
        this.features.initialize();

        // Simple features - Self-registered
        new FlySpeedCommand(this);
        new WalkSpeedCommand(this);
        new BusyCommand(this);
        new TimeCommand(this);
        new WeatherCommand(this);
        new RepairCommand(this);
        new NightVisionCommand(this);
        new SignColorsListener(this);
        new TeleportCommands(this);
        new WelcomeListener(this);
        new HealFoodCommands(this);

        return true;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if ("ngeneral".equals(cmd.getName())) {
            if (args.length < 1) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                case "rld":
                    if (Perms.hasReload(sender)) {
                        if (args.length != 2) {
                            return false;
                        }
                        switch (args[1].toLowerCase()) {
                            case "messages":
                            case "mess":
                            case "mes":
                                try {
                                    this.loadMessages();
                                    this.sendMessage(sender, MessageId.cmdReloadMessages);
                                } catch (final IOException e) {
                                    this.error("An error occured when NPlayer tried to load messages.yml", e);
                                    this.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
                                }
                                return true;
                            default:
                                return false;
                        }
                    } else {
                        this.sendMessage(sender, MessageId.noPermissionForCommand);
                        return true;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void handleOtherNodes() {
        // NOP
    }

    @Override
    protected void onNodeDisable() {
        this.features.terminate();

        try {
            this.pluginConfig.writeConfig();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            this.dbConfig.writeConfig("db.yml");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Messages getMessages() {
        return this.messages;
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    public DbConfig getDbConfig() {
        return this.dbConfig;
    }

    public Features getFeatures() {
        return this.features;
    }

    // API for other nodes

    @Override
    public String getNodeName() {
        return GENERAL;
    }

    @Override
    public void populateInfo(final CommandSender sender, final String query, final Info infoObject) {
        // TODO Implement method
    }

    @Override
    public boolean isSpy(final UUID playerId) {
        final SpyModeFeature spyMode = this.features.get(SpyModeFeature.class);
        return spyMode != null && spyMode.hasSpyMode(playerId);
    }
}

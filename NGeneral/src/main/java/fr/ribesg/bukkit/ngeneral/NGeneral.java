/***************************************************************************
 * Project file:    NPlugins - NGeneral - NGeneral.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.NGeneral                     *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral;

import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ngeneral.config.Config;
import fr.ribesg.bukkit.ngeneral.config.DbConfig;
import fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.ItemNetworkFeature;
import fr.ribesg.bukkit.ngeneral.feature.protectionsign.ProtectionSignFeature;
import fr.ribesg.bukkit.ngeneral.lang.Messages;
import fr.ribesg.bukkit.ngeneral.simplefeature.AfkCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.FlySpeedCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.SignColorsListener;
import fr.ribesg.bukkit.ngeneral.simplefeature.TeleportCommands;
import fr.ribesg.bukkit.ngeneral.simplefeature.TimeCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WalkSpeedCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WeatherCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WelcomeListener;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class NGeneral extends GeneralNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;
	private DbConfig dbConfig;

	// Features
	private GodModeFeature        godMode;
	private FlyModeFeature        flyMode;
	private ProtectionSignFeature protectionSign;
	private ItemNetworkFeature    itemNetwork;

	// Simple features
	private FlySpeedCommand    flySpeedCommand;
	private WalkSpeedCommand   walkSpeedCommand;
	private AfkCommand         afkCommand;
	private TimeCommand        timeCommand;
	private WeatherCommand     weatherCommand;
	private SignColorsListener signColorsListener;
	private TeleportCommands   teleportCommands;
	private WelcomeListener    welcomeListener;

	@Override
	protected String getMinCoreVersion() {
		return "0.5.0";
	}

	@Override
	protected boolean onNodeEnable() {
		// Messages first !
		try {
			if (!getDataFolder().isDirectory()) {
				boolean res = getDataFolder().mkdir();
				if (!res) {
					getLogger().severe("Unable to create subfolder in /plugins/");
					return false;
				}
			}
			messages = new Messages();
			messages.loadMessages(this);
		} catch (final IOException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NGeneral tried to load messages.yml");
			return false;
		}

		// Config
		try {
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NGeneral tried to load config.yml");
			return false;
		}

		// Features
		if (pluginConfig.hasGodModeFeature()) {
			godMode = new GodModeFeature(this);
		}
		if (pluginConfig.hasFlyModeFeature()) {
			flyMode = new FlyModeFeature(this);
		}
		if (pluginConfig.hasProtectionSignFeature()) {
			protectionSign = new ProtectionSignFeature(this);
		}
		if (pluginConfig.hasItemNetworkFeature()) {
			itemNetwork = new ItemNetworkFeature(this);
		}

		// Db
		try {
			dbConfig = new DbConfig(this);
			dbConfig.loadConfig("db.yml");
		} catch (final IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NGeneral tried to load db.yml");
			return false;
		}

		// Feature init
		if (pluginConfig.hasGodModeFeature()) {
			godMode.init();
		}
		if (pluginConfig.hasFlyModeFeature()) {
			flyMode.init();
		}
		if (pluginConfig.hasProtectionSignFeature()) {
			protectionSign.init();
		}
		if (pluginConfig.hasItemNetworkFeature()) {
			itemNetwork.init();
		}

		// Simple commands - Self-registered
		flySpeedCommand = new FlySpeedCommand(this);
		walkSpeedCommand = new WalkSpeedCommand(this);
		afkCommand = new AfkCommand(this);
		timeCommand = new TimeCommand(this);
		weatherCommand = new WeatherCommand(this);
		signColorsListener = new SignColorsListener(this);
		teleportCommands = new TeleportCommands(this);
		welcomeListener = new WelcomeListener(this);

		return true;
	}

	@Override
	protected void onNodeDisable() {
		if (pluginConfig.hasFlyModeFeature()) {
			flyMode.disable();
		}
		try {
			getPluginConfig().writeConfig();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		try {
			getDbConfig().writeConfig("db.yml");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void handleOtherNodes() {
		// NOP
	}

	@Override
	protected void linkCore() {
		getCore().setGeneralNode(this);
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	public DbConfig getDbConfig() {
		return dbConfig;
	}

	// Feature Getters

	public GodModeFeature getGodMode() {
		return godMode;
	}

	public FlyModeFeature getFlyMode() {
		return flyMode;
	}

	public ItemNetworkFeature getItemNetwork() {
		return itemNetwork;
	}
}

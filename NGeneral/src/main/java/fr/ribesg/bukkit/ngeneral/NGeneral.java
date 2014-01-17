/***************************************************************************
 * Project file:    NPlugins - NGeneral - NGeneral.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.NGeneral                     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral;

import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ngeneral.config.Config;
import fr.ribesg.bukkit.ngeneral.config.DbConfig;
import fr.ribesg.bukkit.ngeneral.feature.Features;
import fr.ribesg.bukkit.ngeneral.lang.Messages;
import fr.ribesg.bukkit.ngeneral.simplefeature.AfkCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.BusyCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.FlySpeedCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.SignColorsListener;
import fr.ribesg.bukkit.ngeneral.simplefeature.TeleportCommands;
import fr.ribesg.bukkit.ngeneral.simplefeature.TimeCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WalkSpeedCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WeatherCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.WelcomeListener;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class NGeneral extends NPlugin implements GeneralNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;
	private DbConfig dbConfig;

	// Features
	private Features features;

	@Override
	protected String getMinCoreVersion() {
		return "0.5.1";
	}

	@Override
	protected boolean onNodeEnable() {
		// Messages first !
		try {
			if (!getDataFolder().isDirectory()) {
				final boolean res = getDataFolder().mkdir();
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
		this.features = new Features(this);

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
		this.features.initialize();

		// Simple commands - Self-registered
		new FlySpeedCommand(this);
		new WalkSpeedCommand(this);
		new AfkCommand(this);
		new BusyCommand(this);
		new TimeCommand(this);
		new WeatherCommand(this);
		new SignColorsListener(this);
		new TeleportCommands(this);
		new WelcomeListener(this);

		return true;
	}

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes() */
	@Override
	protected void handleOtherNodes() {
		// NOP
	}

	@Override
	protected void onNodeDisable() {
		this.features.terminate();

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
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	public DbConfig getDbConfig() {
		return dbConfig;
	}

	public Features getFeatures() {
		return features;
	}

	// API for other nodes

	@Override
	public String getNodeName() {
		return GENERAL;
	}
}

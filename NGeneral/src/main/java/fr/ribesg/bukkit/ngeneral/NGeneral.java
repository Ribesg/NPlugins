/***************************************************************************
 * Project file:    NPlugins - NGeneral - NGeneral.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.NGeneral                     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ngeneral.config.Config;
import fr.ribesg.bukkit.ngeneral.config.DbConfig;
import fr.ribesg.bukkit.ngeneral.feature.Features;
import fr.ribesg.bukkit.ngeneral.lang.Messages;
import fr.ribesg.bukkit.ngeneral.simplefeature.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
		return "0.6.1";
	}

	@Override
	protected void loadMessages() throws IOException {
		debug("Loading plugin Messages...");
		if (!getDataFolder().isDirectory()) {
			getDataFolder().mkdir();
		}

		final Messages messages = new Messages();
		messages.loadMessages(this);

		this.messages = messages;
	}

	@Override
	protected boolean onNodeEnable() {
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
		new BusyCommand(this);
		new TimeCommand(this);
		new WeatherCommand(this);
		new RepairCommand(this);
		new NightVisionCommand(this);
		new SignColorsListener(this);
		new TeleportCommands(this);
		new WelcomeListener(this);

		return true;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (cmd.getName().equals("ngeneral")) {
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
									loadMessages();
									sendMessage(sender, MessageId.cmdReloadMessages);
								} catch (final IOException e) {
									error("An error occured when NPlayer tried to load messages.yml", e);
									sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
								}
								return true;
							default:
								return false;
						}
					} else {
						sendMessage(sender, MessageId.noPermissionForCommand);
						return true;
					}
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes()
	 */
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

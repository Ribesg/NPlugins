package fr.ribesg.bukkit.ngeneral;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ngeneral.config.Config;
import fr.ribesg.bukkit.ngeneral.config.DbConfig;
import fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.protectionsign.ProtectionSignFeature;
import fr.ribesg.bukkit.ngeneral.lang.Messages;
import fr.ribesg.bukkit.ngeneral.simplefeature.AfkCommand;
import fr.ribesg.bukkit.ngeneral.simplefeature.FlySpeedCommand;
import org.bukkit.command.CommandSender;
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

	// Simple commands
	private FlySpeedCommand flySpeedCommand;
	private AfkCommand      afkCommand;

	@Override
	protected String getMinCoreVersion() {
		return "0.3.3";
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

		// Simple commands - Self-registered
		flySpeedCommand = new FlySpeedCommand(this);
		afkCommand = new AfkCommand(this);

		return true;
	}

	@Override
	protected void onNodeDisable() {
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

	public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
		final String[] m = messages.get(messageId, args);
		to.sendMessage(m);
	}

	public void broadcastMessage(final MessageId messageId, final String... args) {
		final String[] m = messages.get(messageId, args);
		for (final String mes : m) {
			getServer().broadcastMessage(mes);
		}
	}

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
}

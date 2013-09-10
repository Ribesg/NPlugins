package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.common.AsyncPermAccessor;
import fr.ribesg.bukkit.ncore.node.talk.TalkNode;
import fr.ribesg.bukkit.ntalk.format.Formater;
import fr.ribesg.bukkit.ntalk.lang.Messages;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;

public class NTalk extends TalkNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;

	// Useful Nodes
	// // None

	// Formater
	private Formater formater;

	@Override
	protected String getMinCoreVersion() {
		return "0.4.0";
	}

	@Override
	public boolean onNodeEnable() {
		// Messages first !
		try {
			if (!getDataFolder().isDirectory()) {
				getDataFolder().mkdir();
			}
			messages = new Messages();
			messages.loadMessages(this);
		} catch (final IOException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NTalk tried to load messages.yml");
			return false;
		}

		// Config
		try {
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NTalk tried to load config.yml");
			return false;
		}
		formater = new Formater(this);

		// Listeners
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new TalkListener(this), this);

		// Command
		final TalkCommandExecutor executor = new TalkCommandExecutor(this);
		getCommand("pm").setExecutor(executor);
		getCommand("pr").setExecutor(executor);
		getCommand("nick").setExecutor(executor);

		// We need to access permissions in the AsyncPlayerChatEvent handler
		// For this purpose, we need to use the AsyncPermAccessor
		AsyncPermAccessor.init(this, 3);

		return true;
	}

	@Override
	public void onNodeDisable() {
		try {
			getPluginConfig().writeConfig();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes() */
	@Override
	protected void handleOtherNodes() {
		// Nothing to do here for now
	}

	public Formater getFormater() {
		return formater;
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}
}

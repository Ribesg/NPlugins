/***************************************************************************
 * Project file:    NPlugins - NTalk - NTalk.java                          *
 * Full Class name: fr.ribesg.bukkit.ntalk.NTalk                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.talk.TalkNode;
import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;
import fr.ribesg.bukkit.ntalk.filter.ChatFilter;
import fr.ribesg.bukkit.ntalk.format.Formater;
import fr.ribesg.bukkit.ntalk.lang.Messages;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;

public class NTalk extends NPlugin implements TalkNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;

	// Useful Nodes
	// // None

	// Formater
	private Formater formater;

	// Chat Filter
	private ChatFilter chatFilter;

	@Override
	protected String getMinCoreVersion() {
		return "0.6.6";
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
	public boolean onNodeEnable() {
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

		// Chat filter
		if (pluginConfig.isChatFiltersEnabled()) {
			try {
				chatFilter = new ChatFilter(this);
				chatFilter.loadConfig("filters.yml");
			} catch (final IOException | InvalidConfigurationException e) {
				getLogger().severe("An error occured, stacktrace follows:");
				e.printStackTrace();
				getLogger().severe("This error occured when NTalk tried to load filters.yml");
				return false;
			}
		}

		formater = new Formater(this);

		// Listeners
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new TalkListener(this), this);

		// Command
		final TalkCommandExecutor executor = new TalkCommandExecutor(this);
		setCommandExecutor("ntalk", executor);
		setCommandExecutor("pm", executor);
		setCommandExecutor("pr", executor);
		setCommandExecutor("nick", executor);

		// We need to access permissions in the AsyncPlayerChatEvent handler
		// For this purpose, we need to use the AsyncPermAccessor
		AsyncPermAccessor.init(this, 3);

		return true;
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes()
	 */
	@Override
	protected void handleOtherNodes() {
		// Nothing to do here for now
	}

	@Override
	public void onNodeDisable() {
		try {
			getPluginConfig().writeConfig();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public Formater getFormater() {
		return formater;
	}

	public ChatFilter getChatFilter() {
		return chatFilter;
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	// API for other nodes

	@Override
	public String getNodeName() {
		return TALK;
	}
}

/***************************************************************************
 * Project file:    NPlugins - NTalk - NTalk.java                          *
 * Full Class name: fr.ribesg.bukkit.ntalk.NTalk                           *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.info.Info;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.talk.TalkNode;
import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;
import fr.ribesg.bukkit.ntalk.filter.ChatFilter;
import fr.ribesg.bukkit.ntalk.format.Formater;
import fr.ribesg.bukkit.ntalk.lang.Messages;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

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
        return "0.6.9";
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
    public boolean onNodeEnable() {
        // Config
        try {
            this.debug("Loading configuration...");
            this.pluginConfig = new Config(this);
            this.pluginConfig.loadConfig();
        } catch (final IOException | InvalidConfigurationException e) {
            this.error("An error occured when NTalk tried to load config.yml", e);
            return false;
        }

        // Chat filter
        if (this.pluginConfig.isChatFiltersEnabled()) {
            try {
                this.debug("Loading Chat Filters...");
                this.chatFilter = new ChatFilter(this);
                this.chatFilter.loadConfig("filters.yml");
            } catch (final IOException | InvalidConfigurationException e) {
                this.error("An error occured when NTalk tried to load filters.yml", e);
                return false;
            }
        }

        this.debug("Building formater...");
        this.formater = new Formater(this);

        // Listeners
        this.debug("Registering Listeners...");
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new TalkListener(this), this);

        // Command
        this.debug("Registering Commands...");
        final TalkCommandExecutor executor = new TalkCommandExecutor(this);
        this.setCommandExecutor("ntalk", executor);
        this.setCommandExecutor("pm", executor);
        this.setCommandExecutor("pr", executor);
        this.setCommandExecutor("nick", executor);

        // We need to access permissions in the AsyncPlayerChatEvent handler
        // For this purpose, we need to use the AsyncPermAccessor
        this.debug("Initializing Asynchronous Permissions Accessor...");
        AsyncPermAccessor.init(this);

        return true;
    }

    @Override
    protected void handleOtherNodes() {
        // Nothing to do here for now
    }

    @Override
    public void onNodeDisable() {
        try {
            this.pluginConfig.writeConfig();
        } catch (final IOException e) {
            this.error("An error occured when NTalk tried to save config.yml", e);
        }
    }

    public Formater getFormater() {
        return this.formater;
    }

    public ChatFilter getChatFilter() {
        return this.chatFilter;
    }

    @Override
    public Messages getMessages() {
        return this.messages;
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    // API for other nodes

    @Override
    public String getNodeName() {
        return TALK;
    }

    @Override
    public void populateInfo(final CommandSender sender, final String query, final Info infoObject) {
        // TODO Implement method
    }
}

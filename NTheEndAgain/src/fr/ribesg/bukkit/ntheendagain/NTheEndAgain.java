package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import fr.ribesg.bukkit.ntheendagain.lang.Messages;
import fr.ribesg.bukkit.ntheendagain.listener.ChunkListener;
import fr.ribesg.bukkit.ntheendagain.listener.DamageListener;
import fr.ribesg.bukkit.ntheendagain.listener.EnderDragonListener;
import fr.ribesg.bukkit.ntheendagain.listener.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class NTheEndAgain extends TheEndAgainNode {

	// Configs
	private Messages messages;

	// Useful Nodes
	// // None

	// Actual plugin data
	private HashMap<String, EndWorldHandler> worldHandlers;

	@Override
	protected String getMinCoreVersion() {
		return "0.3.2";
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
			getLogger().severe("This error occured when NTheEndAgain tried to load messages.yml");
			return false;
		}

		// Load End worlds configs and chunks data
		worldHandlers = new HashMap<>();
		boolean res = true;
		for (final World w : Bukkit.getWorlds()) {
			if (w.getEnvironment() == Environment.THE_END) {
				try {
					res = loadWorld(w);
					if (!res) {
						break;
					}
				} catch (InvalidConfigurationException e) {
					getLogger().severe("An error occured, stacktrace follows:");
					e.printStackTrace();
					getLogger().severe("This error occured when NTheEndAgain tried to load \"" + w.getName() + "\"'s config file.");
					break;
				}
			}
		}
		if (!res) {
			for (EndWorldHandler handler : worldHandlers.values()) {
				handler.cancelTasks();
			}
			return false;
		}

		activateFilter();

		getServer().getPluginManager().registerEvents(new WorldListener(this), this);
		getServer().getPluginManager().registerEvents(new ChunkListener(this), this);
		getServer().getPluginManager().registerEvents(new EnderDragonListener(this), this);
		getServer().getPluginManager().registerEvents(new DamageListener(this), this);

		getCommand("nend").setExecutor(new TheEndAgainCommandExecutor(this));

		return true;
	}

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes() */
	@Override
	protected void handleOtherNodes() {
		// Nothing to do here for now
	}

	@Override
	public void onNodeDisable() {
		for (final EndWorldHandler handler : worldHandlers.values()) {
			try {
				handler.unload(true);
			} catch (InvalidConfigurationException e) {
				getLogger().severe("Unable to disable \"" + handler.getEndWorld().getName() + "\"'s world handler. Server should be " +
				                   "stopped now (Were you reloading?)");
				e.printStackTrace();
			}
		}
	}

	public Path getConfigFilePath(final String fileName) {
		return Paths.get(getDataFolder().getPath(), fileName + ".yml");
	}

	public boolean loadWorld(World endWorld) throws InvalidConfigurationException {
		final EndWorldHandler handler = new EndWorldHandler(this, endWorld);
		try {
			handler.loadConfig();
			handler.loadChunks();
			worldHandlers.put(handler.getCamelCaseWorldName(), handler);
			handler.init();
			return true;
		} catch (final IOException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NTheEndAgain tried to load " + e.getMessage() + ".yml");
			return false;
		}
	}

	/**
	 * @param lowerCamelCaseWorldName Key
	 *
	 * @return Value
	 */
	public EndWorldHandler getHandler(final String lowerCamelCaseWorldName) {
		return worldHandlers.get(lowerCamelCaseWorldName);
	}

	/**
	 * Activate the "Moved too quickly!" messages filter if at least one
	 * End world require it
	 */
	public void activateFilter() {
		boolean filterActivated = false;
		for (final EndWorldHandler handler : worldHandlers.values()) {
			if (handler.getConfig().getFilterMovedTooQuicklySpam() == 1) {
				filterActivated = true;
				break;
			}
		}
		if (filterActivated) {
			Bukkit.getLogger().setFilter(new MovedTooQuicklyFilter(this));
		}
	}

	/**
	 * Send a message with arguments TODO <b>This may be moved<b>
	 *
	 * @param to        Receiver
	 * @param messageId The Message Id
	 * @param args      The arguments
	 */
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

	public HashMap<String, EndWorldHandler> getWorldHandlers() {
		return worldHandlers;
	}
}

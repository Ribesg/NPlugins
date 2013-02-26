package com.github.ribesg.nchat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ribesg.nchat.lang.Messages;
import com.github.ribesg.nchat.lang.Messages.MessageId;
import com.github.ribesg.nchat.listeners.PlayerChatListener;
import com.github.ribesg.ncore.NCore;

public class NChat extends JavaPlugin {

	// Constants
	public static final String	NCORE			= "NCore";
	public static final String	F_MESSAGES		= "messages.yml";
	public static final String	F_CONFIG		= "config.yml";

	// Core plugin related
	@Getter public NCore		core;
	public NChatAPI				api;

	// Useful Nodes
	// // None

	// Formater
	@Getter private Formater	formater;

	// Files
	@Getter private Path		pathConfig;
	@Getter private Path		pathMessages;

	// Set to true by afterEnable() call
	// Prevent multiple calls to afterEnable
	private boolean				loadingComplete	= false;

	@Override
	public void onEnable() {
		// Messages first !
		try {
			if (!getDataFolder().isDirectory()) {
				getDataFolder().mkdir();
			}
			pathMessages = Paths.get(getDataFolder().getPath(), F_MESSAGES);
			Messages.loadConfig(pathMessages);
		} catch (final IOException e) {
			e.printStackTrace();
			sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, F_MESSAGES);
			getServer().getPluginManager().disablePlugin(this);
		}

		// Config
		formater = new Formater();
		try {
			pathConfig = Paths.get(getDataFolder().getPath(), F_CONFIG);
			formater.load(pathConfig);
		} catch (final IOException e) {
			e.printStackTrace();
			sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, F_CONFIG);
			getServer().getPluginManager().disablePlugin(this);
		}

		// Listeners
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerChatListener(this), this);

		// Command
		//getCommand("command").setExecutor(new MyCommandExecutor(this));

		// Dependencies handling
		if (linkCore()) {
			afterEnable();
		}
	}

	public void afterEnable() {
		if (!loadingComplete) {
			loadingComplete = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run() {
					// Interact with other Nodes here

				}
			});
		}
	}

	@Override
	public void onDisable() {

	}

	public boolean linkCore() {
		if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
			return false;
		} else {
			core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
			api = new NChatAPI(this);
			core.setChatNode(api);
			return true;
		}
	}

	public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
		final String[] m = Messages.get(messageId, args);
		to.sendMessage(m);
	}
}

package fr.ribesg.bukkit.ntalk;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TalkListener implements Listener {

	private final NTalk plugin;

	public TalkListener(final NTalk instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChatFirst(final AsyncPlayerChatEvent event) {
		if (event.getMessage().startsWith("@") && !event.getMessage().startsWith("@ ")) {
			final String[] split = event.getMessage().substring(1).split(" ");
			if (split.length >= 2) {
				event.setCancelled(true);
				final String targetName = split[0];
				final String message = event.getMessage().substring(targetName.length() + 1);
				final StringBuilder command = new StringBuilder("pm ");
				command.append(targetName);
				command.append(message);
				plugin.getServer().dispatchCommand(event.getPlayer(), command.toString());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChatLast(final AsyncPlayerChatEvent event) {
		event.setFormat(plugin.getFormater().getFormat(event.getPlayer(), true));
		if (Perms.hasColor(event.getPlayer(), true)) {
			event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage())); // Reformat the message
		}
	}

	public NTalk getPlugin() {
		return plugin;
	}
}

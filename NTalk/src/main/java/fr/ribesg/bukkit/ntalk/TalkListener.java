/***************************************************************************
 * Project file:    NPlugins - NTalk - TalkListener.java                   *
 * Full Class name: fr.ribesg.bukkit.ntalk.TalkListener                    *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ntalk.filter.ChatFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.Filter;
import fr.ribesg.bukkit.ntalk.filter.bean.ReplaceFilter;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TalkListener implements Listener {

	private final NTalk      plugin;
	private final ChatFilter filter;

	public TalkListener(final NTalk instance) {
		plugin = instance;
		filter = plugin.getChatFilter();
	}

	/** Handles "@playerName message" PMs */
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
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

	/** Handles chat filter */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerChatThen(final AsyncPlayerChatEvent event) {
		final String message = event.getMessage();
		final String uncoloredMessage = ColorUtils.stripColorCodes(message);
		final Filter result = filter.check(uncoloredMessage);
		switch (result.getResponseType()) {
			case DENY:
				event.setCancelled(true);
				break;
			case REPLACE:
				final ReplaceFilter filter = (ReplaceFilter) result;
				String newMessage;
				if (filter.isRegex()) {
					newMessage = message.replaceAll(filter.getFilteredString(), filter.getReplacement());
				} else {
					newMessage = message;
					while (newMessage.contains(filter.getFilteredString())) {
						newMessage = newMessage.replace(filter.getFilteredString(), filter.getReplacement());
					}
				}
				event.setMessage(newMessage);
				break;
			case TEMPORARY_MUTE:
				// TODO Handle TempMute filters
				break;
			case TEMPORARY_BAN:
				// TODO Handle TempBan filters
				break;
			case TEMPORARY_JAIL:
				// TODO Handle TempJail filters
				break;
			case DIVINE_PUNISHMENT:
				// TODO Handle Special Punishment filters
				break;
			default:
				break;
		}
	}

	/** Handles colors in chat */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChatLast(final AsyncPlayerChatEvent event) {
		event.setFormat(plugin.getFormater().getFormat(event.getPlayer(), true));
		if (Perms.hasColor(event.getPlayer(), true)) {
			event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage())); // Reformat the message
		}
	}
}

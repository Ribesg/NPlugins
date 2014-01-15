/***************************************************************************
 * Project file:    NPlugins - NTalk - TalkListener.java                   *
 * Full Class name: fr.ribesg.bukkit.ntalk.TalkListener                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ntalk.filter.ChatFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.BanFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.Filter;
import fr.ribesg.bukkit.ntalk.filter.bean.JailFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.MuteFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.ReplaceFilter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class TalkListener implements Listener {

	private static final Logger LOGGER = Logger.getLogger(TalkListener.class.getName());

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
		if (filter != null) {
			final String message = event.getMessage();
			final String uncoloredMessage = ColorUtils.stripColorCodes(message);
			final Filter result = filter.check(' ' + uncoloredMessage + ' ');
			if (result != null) {
				switch (result.getResponseType()) {
					case DENY:
						event.setCancelled(true);
						break;
					case REPLACE:
						final ReplaceFilter replaceFilter = (ReplaceFilter) result;
						String newMessage;
						if (replaceFilter.isRegex()) {
							newMessage = message.replaceAll(replaceFilter.getFilteredString(), replaceFilter.getReplacement());
						} else {
							newMessage = message;
							while (newMessage.contains(replaceFilter.getFilteredString())) {
								newMessage = newMessage.replace(replaceFilter.getFilteredString(), replaceFilter.getReplacement());
							}
						}
						event.setMessage(newMessage);
						break;
					case TEMPORARY_MUTE:
						final MuteFilter muteFilter = (MuteFilter) result;
						final String mutePlayerName = event.getPlayer().getName();
						final long muteDuration = muteFilter.getDuration();
						final String muteReason = plugin.getMessages()
						                                .get(MessageId.talk_filterMutedReason, muteFilter.getOutputString())[0];
						final String muteCommand = plugin.getPluginConfig().getTempMuteCommand(mutePlayerName, muteDuration, muteReason);
						Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), muteCommand);
								return null;
							}
						});
						break;
					case TEMPORARY_BAN:
						final BanFilter banFilter = (BanFilter) result;
						final String banPlayerName = event.getPlayer().getName();
						final long banDuration = banFilter.getDuration();
						final String banReason = plugin.getMessages()
						                               .get(MessageId.talk_filterBannedReason, banFilter.getOutputString())[0];
						final String banCommand = plugin.getPluginConfig().getTempBanCommand(banPlayerName, banDuration, banReason);
						Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), banCommand);
								return null;
							}
						});
						break;
					case TEMPORARY_JAIL:
						final JailFilter jailFilter = (JailFilter) result;
						final String jailPlayerName = event.getPlayer().getName();
						final long jailDuration = jailFilter.getDuration();
						final String jailName = jailFilter.getJailName();
						final String jailReason = plugin.getMessages()
						                                .get(MessageId.talk_filterJailedReason, jailFilter.getOutputString())[0];
						final String jailCommand = plugin.getPluginConfig()
						                                 .getTempJailCommand(jailPlayerName, jailDuration, jailName, jailReason);
						Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), jailCommand);
								return null;
							}
						});
						break;
					case DIVINE_PUNISHMENT:
						// TODO
						LOGGER.severe("Divine Punishment has not yet been implemented! Please don't use it!");
						break;
					default:
						break;
				}
			}
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

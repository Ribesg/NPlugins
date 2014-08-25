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
import fr.ribesg.bukkit.ncore.util.ColorUtil;
import fr.ribesg.bukkit.ntalk.filter.ChatFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.BanFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.Filter;
import fr.ribesg.bukkit.ntalk.filter.bean.JailFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.MuteFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.ReplaceFilter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TalkListener implements Listener {

    private static final Logger LOGGER = Logger.getLogger(TalkListener.class.getName());

    private final NTalk      plugin;
    private final ChatFilter filter;

    public TalkListener(final NTalk instance) {
        this.plugin = instance;
        this.filter = this.plugin.getChatFilter();
    }

    /**
     * Handles "@playerName message" PMs
     */
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
                this.plugin.getServer().dispatchCommand(event.getPlayer(), command.toString());
            }
        }
    }

    /**
     * Handles chat filter
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChatThen(final AsyncPlayerChatEvent event) {
        if (this.filter != null) {
            final String message = event.getMessage();
            final String uncoloredMessage = ColorUtil.stripColorCodes(message);
            final Set<Filter> result = this.filter.check(' ' + uncoloredMessage + ' ');
            if (!result.isEmpty()) {
                for (final Filter f : result) {
                    switch (f.getResponseType()) {
                        case DENY:
                            event.setCancelled(true);
                            break;
                        case REPLACE:
                            final ReplaceFilter replaceFilter = (ReplaceFilter)f;
                            String newMessage;
                            if (replaceFilter.isRegex()) {
                                newMessage = message.replaceAll(replaceFilter.getFilteredString(), replaceFilter.getReplacement());
                            } else {
                                final String filtered = replaceFilter.getFilteredString().trim();
                                final String replacement = replaceFilter.getReplacement();
                                newMessage = message;
                                int i = newMessage.indexOf(filtered);
                                while (i != -1) {
                                    newMessage = newMessage.substring(0, i) +
                                                 replacement +
                                                 newMessage.substring(i += filtered.length());
                                    i = newMessage.indexOf(filtered, i);
                                }
                            }
                            event.setMessage(newMessage);
                            break;
                        case TEMPORARY_MUTE:
                            final MuteFilter muteFilter = (MuteFilter)f;
                            final String mutePlayerName = event.getPlayer().getName();
                            final long muteDuration = muteFilter.getDuration();
                            final String muteReason = this.plugin.getMessages().get(MessageId.talk_filterMutedReason, muteFilter.getOutputString())[0];
                            final String muteCommand = this.plugin.getPluginConfig().getTempMuteCommand(mutePlayerName, muteDuration, muteReason);
                            Bukkit.getScheduler().callSyncMethod(this.plugin, new Callable<Object>() {

                                @Override
                                public Object call() throws Exception {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), muteCommand);
                                    return null;
                                }
                            });
                            break;
                        case TEMPORARY_BAN:
                            final BanFilter banFilter = (BanFilter)f;
                            final String banPlayerName = event.getPlayer().getName();
                            final long banDuration = banFilter.getDuration();
                            final String banReason = this.plugin.getMessages().get(MessageId.talk_filterBannedReason, banFilter.getOutputString())[0];
                            final String banCommand = this.plugin.getPluginConfig().getTempBanCommand(banPlayerName, banDuration, banReason);
                            Bukkit.getScheduler().callSyncMethod(this.plugin, new Callable<Object>() {

                                @Override
                                public Object call() throws Exception {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), banCommand);
                                    return null;
                                }
                            });
                            break;
                        case TEMPORARY_JAIL:
                            final JailFilter jailFilter = (JailFilter)f;
                            final String jailPlayerName = event.getPlayer().getName();
                            final long jailDuration = jailFilter.getDuration();
                            final String jailName = jailFilter.getJailName();
                            final String jailReason = this.plugin.getMessages().get(MessageId.talk_filterJailedReason, jailFilter.getOutputString())[0];
                            final String jailCommand = this.plugin.getPluginConfig().getTempJailCommand(jailPlayerName, jailDuration, jailName, jailReason);
                            Bukkit.getScheduler().callSyncMethod(this.plugin, new Callable<Object>() {

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
    }

    /**
     * Handles colors in chat
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChatLast(final AsyncPlayerChatEvent event) {
        final String[] formats = this.plugin.getFormater().getFormat(event.getPlayer(), true);
        event.setFormat(formats[0]);
        String message = event.getMessage().trim();
        if (Perms.hasColor(event.getPlayer())) {
            message = ColorUtil.colorize(message);
        }
        event.setMessage(message); // Reformat the message

        final Iterator<Player> it = event.getRecipients().iterator();
        final Set<Player> players = new HashSet<>();
        while (it.hasNext()) {
            final Player player = it.next();
            if (!Perms.hasSeeNicks(player)) {
                it.remove();
                players.add(player);
            }
        }

        final String normalMessage = String.format(formats[1], event.getPlayer().getDisplayName(), message);
        Bukkit.getScheduler().runTask(this.plugin, new BukkitRunnable() {

            @Override
            public void run() {
                for (final Player p : players) {
                    p.sendMessage(normalMessage);
                }
            }
        });
    }
}

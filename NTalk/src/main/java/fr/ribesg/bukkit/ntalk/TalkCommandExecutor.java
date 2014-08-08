/***************************************************************************
 * Project file:    NPlugins - NTalk - TalkCommandExecutor.java            *
 * Full Class name: fr.ribesg.bukkit.ntalk.TalkCommandExecutor             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author ribes
 */
public class TalkCommandExecutor implements CommandExecutor {

    private static final String CONSOLE_NAME = Bukkit.getConsoleSender().getName();

    private final NTalk                   plugin;
    private final HashMap<String, String> lastReceivedPmMap;

    public TalkCommandExecutor(final NTalk instance) {
        this.plugin = instance;
        this.lastReceivedPmMap = new HashMap<>();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        switch (command.getName()) {
            case "ntalk":
                if (args.length < 1) {
                    return false;
                }
                switch (args[0].toLowerCase()) {
                    case "reload":
                    case "rld":
                        if (Perms.hasReload(sender)) {
                            return this.cmdReload(sender, args);
                        } else {
                            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    default:
                        return false;
                }
            case "pm":
                if (Perms.hasPrivateMessage(sender)) {
                    return this.cmdPrivateMessage(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case "pr":
                if (Perms.hasPrivateResponse(sender)) {
                    return this.cmdPrivateResponse(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case "nick":
                if (Perms.hasNick(sender)) {
                    return this.cmdNick(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            default:
                return false;
        }
    }

    private boolean cmdReload(final CommandSender sender, final String[] args) {
        if (args.length != 2) {
            return false;
        }
        switch (args[1].toLowerCase()) {
            case "messages":
            case "mess":
            case "mes":
                try {
                    this.plugin.loadMessages();
                    this.plugin.sendMessage(sender, MessageId.cmdReloadMessages);
                } catch (final IOException e) {
                    this.plugin.error("An error occured when NTalk tried to load messages.yml", e);
                    this.plugin.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
                }
                return true;
            default:
                return false;
        }
    }

    private boolean cmdPrivateMessage(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            return false;
        } else {
            final String[] targetsName = args[0].split(",");
            final HashSet<CommandSender> targets = new HashSet<>();
            final HashSet<CommandSender> spies = new HashSet<>();
            for (final String target : targetsName) {
                final Player p = this.plugin.getServer().getPlayer(target);
                if (p != null) {
                    targets.add(p);
                } else if ("CONSOLE".equalsIgnoreCase(target) || "SERVER".equalsIgnoreCase(target)) {
                    targets.add(this.plugin.getServer().getConsoleSender());
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, target);
                }
            }
            for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
                if (Perms.hasSpy(p)) {
                    spies.add(p);
                }
            }
            spies.add(this.plugin.getServer().getConsoleSender());
            if (targets.isEmpty()) {
                return true;
            }

            final StringBuilder messageBuilder = new StringBuilder(args[1]);
            for (int i = 2; i < args.length; i++) {
                messageBuilder.append(' ').append(args[i]);
            }

            this.sendMessages(sender, targets, spies, messageBuilder.toString());
            return true;
        }
    }

    private boolean cmdPrivateResponse(final CommandSender sender, final String[] args) {
        if (args.length < 1) {
            return false;
        } else if (this.lastReceivedPmMap.containsKey(sender.getName())) {
            final String targetName = this.lastReceivedPmMap.get(sender.getName());
            final CommandSender target;
            if (CONSOLE_NAME.equals(targetName)) {
                target = this.plugin.getServer().getConsoleSender();
            } else {
                target = this.plugin.getServer().getPlayerExact(targetName);
            }
            if (target != null) {
                final StringBuilder messageBuilder = new StringBuilder(args[0]);
                for (int i = 1; i < args.length; i++) {
                    messageBuilder.append(' ').append(args[i]);
                }
                final String[] formattedMessage = this.sendMessage(sender, target, messageBuilder.toString());
                for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
                    if (Perms.hasSpy(p) && p != target && p != sender) {
                        p.sendMessage(formattedMessage[Perms.hasSeeNicks(p, false) ? 0 : 1]);
                    }
                }
                if (target != this.plugin.getServer().getConsoleSender()) {
                    this.plugin.getServer().getConsoleSender().sendMessage(formattedMessage[0]);
                }
                return true;
            } else {
                this.plugin.sendMessage(sender, MessageId.talk_nobodyToRespond);
                return true;
            }
        } else {
            this.plugin.sendMessage(sender, MessageId.talk_nobodyToRespond);
            return true;
        }
    }

    private boolean cmdNick(final CommandSender sender, final String[] args) {
        if (args.length < 1 || args.length > 2) {
            return false;
        } else if (args.length == 1) {
            final String realName = args[0];
            if (!PlayerIdsUtil.isValidMinecraftUserName(realName)) {
                this.plugin.sendMessage(sender, MessageId.talk_invalidUsername, realName);
                return true;
            }
            final UUID id = UuidDb.getId(Node.TALK, realName);
            this.plugin.getPluginConfig().getPlayerNicknames().remove(id);
            this.plugin.sendMessage(sender, MessageId.talk_youDeNickNamed, realName);
            final Player p = this.plugin.getServer().getPlayerExact(realName);
            if (p != null && p != sender) {
                this.plugin.sendMessage(this.plugin.getServer().getPlayerExact(realName), MessageId.talk_youWereDeNickNamed, sender.getName());
            }
            return true;
        } else {
            final String realName = args[0];
            final String nick = args[1];
            if (!PlayerIdsUtil.isValidMinecraftUserName(realName)) {
                this.plugin.sendMessage(sender, MessageId.talk_invalidUsername, realName);
                return true;
            } else if (!PlayerIdsUtil.isValidNickName(nick)) {
                this.plugin.sendMessage(sender, MessageId.talk_invalidNickname, nick);
                return true;
            }
            final UUID id = UuidDb.getId(Node.TALK, realName);
            this.plugin.getPluginConfig().getPlayerNicknames().put(id, nick);
            this.plugin.sendMessage(sender, MessageId.talk_youNickNamed, realName, nick);
            final Player p = this.plugin.getServer().getPlayerExact(realName);
            if (p != null && p != sender) {
                this.plugin.sendMessage(this.plugin.getServer().getPlayerExact(realName), MessageId.talk_youWereNickNamed, nick, sender.getName());
            }
            return true;
        }
    }

    private void sendMessages(final CommandSender from, final Set<CommandSender> toSet, final Set<CommandSender> spySet, final String message) {
        for (final CommandSender to : toSet) {
            final String[] formattedMessage = this.sendMessage(from, to, message);
            for (final CommandSender spy : spySet) {
                if (spy != from && spy != to) {
                    spy.sendMessage(formattedMessage[Perms.hasSeeNicks(spy, false) ? 0 : 1]);
                }
            }
        }
    }

    private String[] sendMessage(final CommandSender from, final CommandSender to, final String message) {
        final String[] formattedMessage = this.plugin.getFormater().parsePM(from, to, message);
        from.sendMessage(formattedMessage[Perms.hasSeeNicks(from, false) ? 0 : 1]);
        to.sendMessage(formattedMessage[Perms.hasSeeNicks(to, false) ? 0 : 1]);
        this.lastReceivedPmMap.put(to.getName(), from.getName());
        return formattedMessage;
    }
}

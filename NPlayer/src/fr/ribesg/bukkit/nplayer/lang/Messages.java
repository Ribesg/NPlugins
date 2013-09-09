package fr.ribesg.bukkit.nplayer.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

/**
 * Messages for NCuboid
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	/**
	 */
	public Messages() {
		super("Player");
	}

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage() */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<Message>();

		// General plugin messages
		newMessages.add(new Message(MessageId.incorrectValueInConfiguration,
		                            "&cIncorrect value in configuration %filename% : '%value%' " + "was restored to default (%default%)",
		                            new String[] {"%filename%", "%value%", "%default%"},
		                            null,
		                            true));

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand,
		                            "&cYou do not have the permission to use that command",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null, true));

		// Command - PLAYER
		newMessages.add(new Message(MessageId.player_registerFirst,
		                            "&cYou need to register before login!##&cUse the '/register' command",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_loginFirst,
		                            "&cYou need to login first!##&cUse the '/login' command",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_welcomeBack, "&aYou're now logged in! Have fun!", null, null, true));
		newMessages.add(new Message(MessageId.player_wrongPassword, "&cThe password you entered is wrong.", null, null, true));
		newMessages.add(new Message(MessageId.player_welcomeToTheServer,
		                            "&aYou're now registered, welcome to the server!",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_passwordChanged, "&aYour password was changed!", null, null, true));
		newMessages.add(new Message(MessageId.player_alreadyRegistered,
		                            "&cThis username is already registered, please login!",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_autoLogoutEnabled, "&aYou enabled auto-logout!", null, null, true));
		newMessages.add(new Message(MessageId.player_autoLogoutDisabled, "&aYou disabled auto-logout!", null, null, true));
		newMessages.add(new Message(MessageId.player_loggedOut, "&aYou're now logged out!", null, null, true));
		newMessages.add(new Message(MessageId.player_unknownUser,
		                            "&cUnknown player name: %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_userHasNoHome,
		                            "&cPlayer has no home: %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_teleportingToUserHome,
		                            "&aTeleporting to the home of %playerName%...",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_youHaveNoHome, "&cYou have no home !", null, null, true));
		newMessages.add(new Message(MessageId.player_teleportingToYourHome, "&aTeleporting to your home...", null, null, true));
		newMessages.add(new Message(MessageId.player_userHomeSet,
		                            "&aHome of %playerName% set !",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_yourHomeSet, "&aHome set !", null, null, true));

		// Command - PUNISHMENT
		newMessages.add(new Message(MessageId.player_kickMessage,
		                            "&cYou have been kicked: %reason%",
		                            new String[] {"%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_broadcastedKickMessage,
		                            "&e%userName% has been kicked (%reason%)",
		                            new String[] {"%userName%", "%reason%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_kickPermBanned,
		                            "&cYou have been permanently banned: %reason%",
		                            new String[] {"%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_permBannedBroadcast,
		                            "&e%userName% has been permanently banned (%reason%)",
		                            new String[] {"%userName%", "%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_kickTempBanned,
		                            "&cYou have been banned for %duration%: %reason%",
		                            new String[] {"%reason%", "%duration%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_tempBannedBroadcast,
		                            "&e%userName% has been banned for %duration% (%reason%)",
		                            new String[] {"%userName%", "%duration%", "%reason%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_unknownIp, "&cUnknown IP: %ip%", new String[] {"%ip%"}, null, true));
		newMessages.add(new Message(MessageId.player_kickPermIpBanned,
		                            "&cYour IP has been permanently banned: %reason%",
		                            new String[] {"%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_permIpBannedBroadcast,
		                            "&eThe IP %ip% has been permanently banned (%reason%)",
		                            new String[] {"%ip%", "%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_kickTempIpBanned,
		                            "&cYour IP has been banned for %duration%: %reason%",
		                            new String[] {"%reason%", "%duration%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_tempIpBannedBroadcast,
		                            "&eThe IP %ip% has been banned for %duration% (%reason%)",
		                            new String[] {"%ip%", "%duration%", "%reason%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_permMuted,
		                            "&cYou have been permanently muted: %reason%",
		                            new String[] {"%reason%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_permMutedBroadcast,
		                            "&e%userName% has been permanently muted (%reason%)",
		                            new String[] {"%userName%", "%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_tempMuted,
		                            "&cYou have been muted for %duration%: %reason%",
		                            new String[] {"%reason%", "%duration%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_tempMutedBroadcast,
		                            "&e%userName% has been muted for %duration% (%reason%)",
		                            new String[] {"%userName%", "%duration%", "%reason%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_unBannedBroadcast,
		                            "&e%userName% has been unbanned",
		                            new String[] {"%userName%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_notBanned, "&c%userName% is not banned", new String[] {"%userName%"}, null, true));

		newMessages.add(new Message(MessageId.player_unBannedIpBroadcast,
		                            "&eThe IP %ip% has been unbanned",
		                            new String[] {"%ip%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_notBannedIp, "&cThe IP %ip% is not banned", new String[] {"%ip%"}, null, true));

		newMessages.add(new Message(MessageId.player_unMutedBroadcast,
		                            "&e%userName% has been unmuted",
		                            new String[] {"%userName%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_notMuted, "&c%userName% is not muted", new String[] {"%userName%"}, null, true));

		newMessages.add(new Message(MessageId.player_deniedPermBanned,
		                            "&cYou are permanently banned: %reason%",
		                            new String[] {"%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_deniedTempBanned,
		                            "&cYou are banned for %duration%: %reason%",
		                            new String[] {"%reason%", "%duration%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_deniedPermIpBanned,
		                            "&cYour IP is permanently banned: %reason%",
		                            new String[] {"%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_deniedTempIpBanned,
		                            "&cYour IP is banned for %duration%: %reason%",
		                            new String[] {"%reason%", "%duration%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_deniedPermMuted,
		                            "&cYou are permanently muted: %reason%",
		                            new String[] {"%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.player_deniedTempMuted,
		                            "&cYou are muted for %duration%: %reason%",
		                            new String[] {"%reason%", "%duration%"},
		                            null,
		                            false));

		newMessages.add(new Message(MessageId.player_alreadyBanned,
		                            "&c%userName% is already banned",
		                            new String[] {"%userName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_alreadyBannedIp,
		                            "&cThe IP %ip% is already banned",
		                            new String[] {"%ip%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.player_alreadyMuted,
		                            "&c%userName% is already muted",
		                            new String[] {"%userName%"},
		                            null,
		                            true));

		newMessages.add(new Message(MessageId.player_standardKickMessage,
		                            "&e%userName% has been kicked",
		                            new String[] {"%userName%"},
		                            null,
		                            false));

		return newMessages;
	}
}

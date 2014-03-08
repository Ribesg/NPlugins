/***************************************************************************
 * Project file:    NPlugins - NPlayer - Messages.java                     *
 * Full Class name: fr.ribesg.bukkit.nplayer.lang.Messages                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

/**
 * Messages for NPlayer
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	public Messages() {
		super("Player");
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage()
	 */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", true, null));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", true, null));

		// Welcome
		newMessages.add(new Message(MessageId.player_pleaseLogin, "&6Please login using the /login command", false, null));
		newMessages.add(new Message(MessageId.player_pleaseRegister, "&6Please register using the /register command", true, null));
		newMessages.add(new Message(MessageId.player_autoLogged, "&aYou have been automatically logged in!", false, null));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadError, "&An error occured while loading %file%!", true, new String[] {"%file%"}));

		// Command - PLAYER
		newMessages.add(new Message(MessageId.player_registerFirst, "&cYou need to register before login!##&cUse the '/register' command", true, null));
		newMessages.add(new Message(MessageId.player_loginFirst, "&cYou need to login first!##&cUse the '/login' command", true, null));
		newMessages.add(new Message(MessageId.player_welcomeBack, "&aYou're now logged in! Have fun!", true, null));
		newMessages.add(new Message(MessageId.player_wrongPassword, "&cThe password you entered is wrong.", true, null));
		newMessages.add(new Message(MessageId.player_welcomeToTheServer, "&aYou're now registered, welcome to the server!", true, null));
		newMessages.add(new Message(MessageId.player_passwordChanged, "&aYour password was changed!", true, null));
		newMessages.add(new Message(MessageId.player_alreadyRegistered, "&cThis username is already registered, please login!", true, null));
		newMessages.add(new Message(MessageId.player_autoLogoutEnabled, "&aYou enabled auto-logout!", true, null));
		newMessages.add(new Message(MessageId.player_autoLogoutDisabled, "&aYou disabled auto-logout!", true, null));
		newMessages.add(new Message(MessageId.player_loggedOut, "&aYou're now logged out!", true, null));
		newMessages.add(new Message(MessageId.player_unknownUser, "&cUnknown player name: %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.player_userHasNoHome, "&cPlayer has no home: %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.player_teleportingToUserHome, "&aTeleporting to the home of %playerName%...", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.player_youHaveNoHome, "&cYou have no home !", true, null));
		newMessages.add(new Message(MessageId.player_teleportingToYourHome, "&aTeleporting to your home...", true, null));
		newMessages.add(new Message(MessageId.player_userHomeSet, "&aHome of %playerName% set !", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.player_yourHomeSet, "&aHome set !", true, null));

		// Command - PUNISHMENT
		newMessages.add(new Message(MessageId.player_noPermissionForPermanent, "&cYou do not have the permission to use the command %command% with an infinite duration", true, new String[] {"%command%"}));
		newMessages.add(new Message(MessageId.player_kickMessage, "&cYou have been kicked: %reason%", false, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_broadcastedKickMessage, "&e%userName% has been kicked (%reason%)", false, new String[] {
				"%userName%",
				"%reason%"
		}));

		newMessages.add(new Message(MessageId.player_kickPermBanned, "&cYou have been permanently banned: %reason%", false, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_permBannedBroadcast, "&e%userName% has been permanently banned (%reason%)", false, new String[] {
				"%userName%",
				"%reason%"
		}));
		newMessages.add(new Message(MessageId.player_kickTempBanned, "&cYou have been banned for %duration%: %reason%", false, new String[] {
				"%reason%",
				"%duration%"
		}));
		newMessages.add(new Message(MessageId.player_tempBannedBroadcast, "&e%userName% has been banned for %duration% (%reason%)", false, new String[] {
				"%userName%",
				"%duration%",
				"%reason%"
		}));

		newMessages.add(new Message(MessageId.player_unknownIp, "&cUnknown IP: %ip%", true, new String[] {"%ip%"}));
		newMessages.add(new Message(MessageId.player_kickPermIpBanned, "&cYour IP has been permanently banned: %reason%", false, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_permIpBannedBroadcast, "&eThe IP %ip% has been permanently banned (%reason%)", false, new String[] {
				"%ip%",
				"%reason%"
		}));
		newMessages.add(new Message(MessageId.player_kickTempIpBanned, "&cYour IP has been banned for %duration%: %reason%", false, new String[] {
				"%reason%",
				"%duration%"
		}));
		newMessages.add(new Message(MessageId.player_tempIpBannedBroadcast, "&eThe IP %ip% has been banned for %duration% (%reason%)", false, new String[] {
				"%ip%",
				"%duration%",
				"%reason%"
		}));

		newMessages.add(new Message(MessageId.player_permMuted, "&cYou have been permanently muted: %reason%", true, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_permMutedBroadcast, "&e%userName% has been permanently muted (%reason%)", false, new String[] {
				"%userName%",
				"%reason%"
		}));
		newMessages.add(new Message(MessageId.player_tempMuted, "&cYou have been muted for %duration%: %reason%", true, new String[] {
				"%reason%",
				"%duration%"
		}));
		newMessages.add(new Message(MessageId.player_tempMutedBroadcast, "&e%userName% has been muted for %duration% (%reason%)", false, new String[] {
				"%userName%",
				"%duration%",
				"%reason%"
		}));

		newMessages.add(new Message(MessageId.player_unBannedBroadcast, "&e%userName% has been unbanned", false, new String[] {"%userName%"}));

		newMessages.add(new Message(MessageId.player_notBanned, "&c%userName% is not banned", true, new String[] {"%userName%"}));

		newMessages.add(new Message(MessageId.player_unBannedIpBroadcast, "&eThe IP %ip% has been unbanned", false, new String[] {"%ip%"}));

		newMessages.add(new Message(MessageId.player_notBannedIp, "&cThe IP %ip% is not banned", true, new String[] {"%ip%"}));

		newMessages.add(new Message(MessageId.player_unMutedBroadcast, "&e%userName% has been unmuted", false, new String[] {"%userName%"}));

		newMessages.add(new Message(MessageId.player_notMuted, "&c%userName% is not muted", true, new String[] {"%userName%"}));

		newMessages.add(new Message(MessageId.player_deniedPermBanned, "&cYou are permanently banned: %reason%", false, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_deniedTempBanned, "&cYou are banned for %duration%: %reason%", false, new String[] {
				"%reason%",
				"%duration%"
		}));
		newMessages.add(new Message(MessageId.player_deniedPermIpBanned, "&cYour IP is permanently banned: %reason%", false, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_deniedTempIpBanned, "&cYour IP is banned for %duration%: %reason%", false, new String[] {
				"%reason%",
				"%duration%"
		}));
		newMessages.add(new Message(MessageId.player_deniedPermMuted, "&cYou are permanently muted: %reason%", false, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_deniedTempMuted, "&cYou are muted for %duration%: %reason%", false, new String[] {
				"%reason%",
				"%duration%"
		}));

		newMessages.add(new Message(MessageId.player_alreadyBanned, "&c%userName% is already banned", true, new String[] {"%userName%"}));
		newMessages.add(new Message(MessageId.player_alreadyBannedIp, "&cThe IP %ip% is already banned", true, new String[] {"%ip%"}));
		newMessages.add(new Message(MessageId.player_alreadyMuted, "&c%userName% is already muted", true, new String[] {"%userName%"}));

		newMessages.add(new Message(MessageId.player_standardKickMessage, "&e%userName% has been kicked", false, new String[] {"%userName%"}));

		newMessages.add(new Message(MessageId.player_cuboidNodeRequired, "&cJail feature disabled without NCuboid", true, null));
		newMessages.add(new Message(MessageId.player_unknownJail, "&cUnknown jail: %jailName%", true, new String[] {"%jailName%"}));
		newMessages.add(new Message(MessageId.player_alreadyJailed, "&c%userName% is already jailed!", true, new String[] {"%userName%"}));
		newMessages.add(new Message(MessageId.player_permJailed, "&cYou were permanently jailed: %reason%", true, new String[] {"%reason%"}));
		newMessages.add(new Message(MessageId.player_permJailedBroadcast, "&e%userName% has been permanently jailed (%reason%)", false, new String[] {
				"%userName%",
				"%reason%"
		}));
		newMessages.add(new Message(MessageId.player_tempJailed, "&cYou have been jailed for %duration%: %reason%", true, new String[] {
				"%reason%",
				"%duration%"
		}));
		newMessages.add(new Message(MessageId.player_tempJailedBroadcast, "&e%userName% has been jailed for %duration% (%reason%)", false, new String[] {
				"%userName%",
				"%duration%",
				"%reason%"
		}));
		newMessages.add(new Message(MessageId.player_unJailedBroadcast, "&e%userName% has been unjailed", false, new String[] {"%userName%"}));
		newMessages.add(new Message(MessageId.player_notJailed, "&c%userName% is not jailed", true, new String[] {"%userName%"}));

		// Login
		newMessages.add(new Message(MessageId.player_loginAttemptsKickMessage, "&cYou have been kicked: Too many login attempts", false, null));
		newMessages.add(new Message(MessageId.player_loginAttemptsBroadcastedKickMessage, "&e%userName% has been kicked (Too many login attempts)", false, new String[] {"%userName%"}));
		newMessages.add(new Message(MessageId.player_loginAttemptsTempBanMessage, "&cYou have been banned for %duration%: Too many login attempts", false, new String[] {"%duration%"}));
		newMessages.add(new Message(MessageId.player_loginAttemptsBroadcastedTempBanMessage, "&e%userName% has been banned for %duration% (Too many login attempts)", false, new String[] {
				"%userName%",
				"%duration%"
		}));
		newMessages.add(new Message(MessageId.player_loginAttemptsPermBanMessage, "&cYou have been permanently banned : Too many login attempts", false, null));
		newMessages.add(new Message(MessageId.player_loginAttemptsBroadcastedPermBanMessage, "&e%userName% has been permanently banned (Too many login attempts)", false, new String[] {"%userName%"}));
		newMessages.add(new Message(MessageId.player_loginAttemptsTooMany, "&cToo many login attempts", false, null));

		// Force Login
		newMessages.add(new Message(MessageId.player_somebodyForcedLoginYou, "&6You have been logged in by %admin%", true, new String[] {"%admin%"}));
		newMessages.add(new Message(MessageId.player_youForcedLogin, "&6You logged in %player%", true, new String[] {"%player%"}));
		return newMessages;
	}
}

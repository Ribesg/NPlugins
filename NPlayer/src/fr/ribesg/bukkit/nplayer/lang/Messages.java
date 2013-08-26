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
		                            null));

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null));

		// Command - PLAYER
		newMessages.add(new Message(MessageId.player_registerFirst,
		                            "&cYou need to register before login!##&cUse the '/register' command",
		                            null,
		                            null));
		newMessages.add(new Message(MessageId.player_loginFirst, "&cYou need to login first!##&cUse the '/login' command", null, null));
		newMessages.add(new Message(MessageId.player_welcomeBack, "&aYou're now logged in! Have fun!", null, null));
		newMessages.add(new Message(MessageId.player_wrongPassword, "&cThe password you entered is wrong.", null, null));
		newMessages.add(new Message(MessageId.player_welcomeToTheServer, "&aYou're now registered, welcome to the server!", null, null));
		newMessages.add(new Message(MessageId.player_passwordChanged, "&aYour password was changed!", null, null));
		newMessages.add(new Message(MessageId.player_alreadyRegistered,
		                            "&cThis username is already registered, please login!",
		                            null,
		                            null));
		newMessages.add(new Message(MessageId.player_autoLogoutEnabled, "&aYou enabled auto-logout!", null, null));
		newMessages.add(new Message(MessageId.player_autoLogoutDisabled, "&aYou disabled auto-logout!", null, null));
		newMessages.add(new Message(MessageId.player_loggedOut, "&aYou're now logged out!", null, null));
		newMessages.add(new Message(MessageId.player_unknownUser,
		                            "&cUnknown player name: %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.player_userHasNoHome,
		                            "&cPlayer has no home: %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.player_teleportingToUserHome,
		                            "&aTeleporting to the home of %playerName%...",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.player_youHaveNoHome, "&cYou have no home !", null, null));
		newMessages.add(new Message(MessageId.player_teleportingToYourHome, "&aTeleporting to your home...", null, null));
		newMessages.add(new Message(MessageId.player_userHomeSet, "&aHome of %playerName% set !", new String[] {"%playerName%"}, null));
		newMessages.add(new Message(MessageId.player_yourHomeSet, "&aHome set !", null, null));

		return newMessages;
	}

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		content.append("###################################################################################\n");
		content.append("# List of NPlayer messages. You're free to change text/colors/language here.      #\n");
		content.append("# Supports both '§' and '&' characters for colors.                         Ribesg #\n");
		content.append("###################################################################################\n\n");
		for (final Message m : getMessagesMap().values()) {
			content.append("# Default value    : " + m.getDefaultMessage() + '\n');
			content.append("# Awaited arguments: " + m.getAwaitedArgsString() + '\n');
			content.append(m.getId().name() +
			               ": \"" +
			               (m.getConfigMessage() != null ? m.getConfigMessage() : m.getDefaultMessage()) +
			               "\"\n\n");
		}
		return content.toString();
	}
}

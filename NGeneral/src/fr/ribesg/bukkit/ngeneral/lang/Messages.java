package fr.ribesg.bukkit.ngeneral.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

/**
 * Messages for NTalk
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	/**
	 */
	public Messages() {
		super("General");
	}

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage() */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General plugin messages
		newMessages.add(new Message(MessageId.incorrectValueInConfiguration,
		                            "&cIncorrect value in configuration %filename% : '%value%' was restored to default (%default%)",
		                            new String[] {"%filename%", "%value%", "%default%"},
		                            null));

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null));
		newMessages.add(new Message(MessageId.noPlayerFoundForGivenName,
		                            "&cNo online player found for input %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null));

		// Command - GOD
		newMessages.add(new Message(MessageId.general_god_enabled, "&aGod Mode enabled", null, null));
		newMessages.add(new Message(MessageId.general_god_disabled, "&aGod Mode disabled", null, null));
		newMessages.add(new Message(MessageId.general_god_enabledFor,
		                            "&aGod Mode enabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_god_disabledFor,
		                            "&aGod Mode disabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_god_enabledBy,
		                            "&aGod Mode enabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_god_disabledBy,
		                            "&aGod Mode disabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null));

		// Command - FLY
		newMessages.add(new Message(MessageId.general_fly_enabled, "&aFly Mode enabled", null, null));
		newMessages.add(new Message(MessageId.general_fly_disabled, "&aFly Mode disabled", null, null));
		newMessages.add(new Message(MessageId.general_fly_enabledFor,
		                            "&aFly Mode enabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_fly_disabledFor,
		                            "&aFly Mode disabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_fly_enabledBy,
		                            "&aFly Mode enabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_fly_disabledBy,
		                            "&aFly Mode disabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null));

		// Command - FLYSPEED
		newMessages.add(new Message(MessageId.general_flyspeed_set, "&aFly Speed set to %value%", new String[] {"%value%"}, null));
		newMessages.add(new Message(MessageId.general_flyspeed_reset, "&aFly Speed reset", null, null));
		newMessages.add(new Message(MessageId.general_flyspeed_setFor,
		                            "&aFly Speed set to %value% for %playerName%",
		                            new String[] {"%value%", "%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_flyspeed_setBy,
		                            "&aFly Speed set to %value% by %playerName%",
		                            new String[] {"%value%", "%playerName%"},
		                            null));

		// Command - AFK
		newMessages.add(new Message(MessageId.general_afk_nowAfk, "&aYou are now AFK", null, null));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfk, "&aYou are no longer AFK", null, null));
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcast,
		                            "&a%playerName% is now AFK",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcast,
		                            "&a%playerName% is no longer AFK",
		                            new String[] {"%playerName%"},
		                            null));
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcastReason,
		                            "&a%playerName% is now AFK (%reason%)",
		                            new String[] {"%playerName%", "%reason%"},
		                            null));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcastReason,
		                            "&a%playerName% is no longer AFK (%reason%)",
		                            new String[] {"%playerName%", "%reason%"},
		                            null));

		// Feature - SIGN COLORS
		newMessages.add(new Message(MessageId.general_signcolors_permissionDenied,
		                            "&cYou do not have the permission to use colors on signs",
		                            null,
		                            null));

		// Feature - PROTECTION SIGNS
		newMessages.add(new Message(MessageId.general_protectionsign_accessDenied, "&cAccess denied by a Private sign.", null, null));
		newMessages.add(new Message(MessageId.general_protectionsign_breakDenied,
		                            "&cThis bloc is protected by a Private sign.",
		                            null,
		                            null));

		return newMessages;
	}

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		content.append("###################################################################################\n");
		content.append("# List of NGeneral messages. You're free to change text/colors/language here.     #\n");
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

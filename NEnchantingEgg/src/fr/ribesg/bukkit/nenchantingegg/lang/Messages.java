package fr.ribesg.bukkit.nenchantingegg.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

public class Messages extends AbstractMessages {

	/**
	 */
	public Messages() {
		super("EnchantingEgg");
	}

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage() */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<Message>();

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

		// Altars
		newMessages.add(new Message(MessageId.egg_altarCreated, "&6You feel strange... Dark powers...", null, null));
		newMessages.add(new Message(MessageId.egg_altarDestroyed, "&aThis area seems to be purified", null, null));
		newMessages.add(new Message(MessageId.egg_altarProtectedSkullAtNight, "&cI am too powerful for you...", null, null));
		newMessages.add(new Message(MessageId.egg_altarProtectedBlock, "&cDo not play with fire...", null, null));
		newMessages.add(new Message(MessageId.egg_cantPlaceOnAltar, "&cAre you trying to provoke me?", null, null));
		newMessages.add(new Message(MessageId.egg_altarTooClose, "&cSomething already drain the dark powers of this area...", null, null));
		newMessages.add(new Message(MessageId.egg_altarEggProvided, "&6Aaaah... Power!", null, null));

		return newMessages;
	}

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		content.append("#####################################################################################\n");
		content.append("# List of NEnchantingEgg messages. You're free to change text/colors/language here. #\n");
		content.append("# Supports both '§' and '&' characters for colors.                           Ribesg #\n");
		content.append("#####################################################################################\n\n");
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

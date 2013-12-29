/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Messages.java              *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.lang.Messages          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
		final Set<Message> newMessages = new HashSet<>();

		// General plugin messages
		newMessages.add(new Message(MessageId.incorrectValueInConfiguration,
		                            "&cIncorrect value in configuration %filename% : '%value%' was restored to default (%default%)",
		                            new String[] {
				                            "%filename%",
				                            "%value%",
				                            "%default%"
		                            },
		                            null,
		                            true));

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand,
		                            "&cYou do not have the permission to use that command",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.noPlayerFoundForGivenName,
		                            "&cNo online player found for input %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null, true));

		// Altars
		newMessages.add(new Message(MessageId.egg_altarCreated, "&6You feel strange... Dark powers...", null, null, true));
		newMessages.add(new Message(MessageId.egg_altarDestroyed, "&aThis area seems to be purified", null, null, true));
		newMessages.add(new Message(MessageId.egg_altarProtectedSkullAtNight, "&cI am too powerful for you...", null, null, true));
		newMessages.add(new Message(MessageId.egg_altarProtectedBlock, "&cDo not play with fire...", null, null, true));
		newMessages.add(new Message(MessageId.egg_cantPlaceOnAltar, "&cAre you trying to provoke me?", null, null, true));
		newMessages.add(new Message(MessageId.egg_altarTooClose,
		                            "&cSomething already drain the dark powers of this area...",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.egg_altarEggProvided, "&6Aaaah... Power!", null, null, true));

		return newMessages;
	}
}

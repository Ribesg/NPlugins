/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Messages.java              *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.lang.Messages          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

/**
 * Messages for NEnchantingEgg
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	public Messages() {
		super("EnchantingEgg");
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage()
	 */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", true));
		newMessages.add(new Message(MessageId.noPlayerFoundForGivenName, "&cNo online player found for input %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true));
		newMessages.add(new Message(MessageId.cmdReloadError, "&An error occured while loading %file%!", true, "%file%"));

		// Altars
		newMessages.add(new Message(MessageId.egg_altarCreated, "&6You feel strange... Dark powers...", true));
		newMessages.add(new Message(MessageId.egg_altarDestroyed, "&aThis area seems to be purified", true));
		newMessages.add(new Message(MessageId.egg_altarProtectedSkullAtNight, "&cI am too powerful for you...", true));
		newMessages.add(new Message(MessageId.egg_altarProtectedBlock, "&cDo not play with fire...", true));
		newMessages.add(new Message(MessageId.egg_cantPlaceOnAltar, "&cAre you trying to provoke me?", true));
		newMessages.add(new Message(MessageId.egg_altarTooClose, "&cSomething already drain the dark powers of this area...", true));
		newMessages.add(new Message(MessageId.egg_altarEggProvided, "&6Aaaah... Power!", true));

		return newMessages;
	}
}

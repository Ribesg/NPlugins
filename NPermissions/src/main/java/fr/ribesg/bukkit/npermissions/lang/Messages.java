/***************************************************************************
 * Project file:    NPlugins - NPermissions - Messages.java                *
 * Full Class name: fr.ribesg.bukkit.npermissions.lang.Messages            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

/**
 * Messages for NPermissions
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	public Messages() {
		super("Permissions");
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage()
	 */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", true, null));
		newMessages.add(new Message(MessageId.noPlayerFoundForGivenName, "&cNo player found for input %playerName%", true, new String[] {"%playerName%"}));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadGroups, "&aGroups reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadPlayers, "&aPlayers reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadError, "&cAn error occured while loading %file%!", true, new String[] {"%file%"}));

		// Command - SETGROUP
		newMessages.add(new Message(MessageId.permissions_unknown, "&cWhat's '%unknown%' supposed to be?", true, new String[] {"%unknown%"}));
		newMessages.add(new Message(MessageId.permissions_unknownGroup, "&cUnknown group '%groupName%'", true, new String[] {"%groupName%"}));
		newMessages.add(new Message(MessageId.permissions_newPlayer, "&6Registered new player with UUID '%uuid%' in group '%groupName%'", true, new String[] {
				"%uuid%",
				"%groupName%"
		}));
		newMessages.add(new Message(MessageId.permissions_unknownUuid, "&cUnknown UUID '%uuid%'", true, new String[] {"%uuid%"}));
		newMessages.add(new Message(MessageId.permissions_changedGroup, "&aChanged main group of player '%playerName%' to '%groupName%'", true, new String[] {
				"%playerName%",
				"%groupName%"
		}));
		newMessages.add(new Message(MessageId.permissions_newLegacyPlayer, "&6Registered new legacy player with name '%playerName%' in group '%groupName%'", true, new String[] {
				"%playerName%",
				"%groupName%"
		}));
		newMessages.add(new Message(MessageId.permissions_unknownPlayer, "&cUnknown player '%playerName%'", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.permissions_changedLegacyGroup, "&aChanged main group of legacy player '%playerName%' to '%groupName%'", true, new String[] {
				"%playerName%",
				"%groupName%"
		}));
		newMessages.add(new Message(MessageId.permissions_alreadyMainGroup, "&cMain group of Player '%playerName%' is already '%groupName%'", true, new String[] {
				"%playerName%",
				"%groupName%"
		}));

		return newMessages;
	}
}

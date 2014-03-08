/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - Messages.java                *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.lang.Messages            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

/**
 * Messages for NTheEndAgain
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	public Messages() {
		super("TheEndAgain");
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

		newMessages.add(new Message(MessageId.missingWorldArg, "&cNon-player user should provide a world name", true, null));
		newMessages.add(new Message(MessageId.unknownWorld, "&cUnknown world '%world%'", true, new String[] {"%world%"}));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadError, "&An error occured while loading %file%!", true, new String[] {"%file%"}));

		// Command - END
		newMessages.add(new Message(MessageId.theEndAgain_unkownSubCmd, "&cUnknown sub-command: %arg%", true, new String[] {"%arg%"}));
		newMessages.add(new Message(MessageId.theEndAgain_regenerating, "&aRegeneration of world %world% in progress", true, new String[] {"%world%"}));
		newMessages.add(new Message(MessageId.theEndAgain_respawned1, "&aAn EnderDragon has been respawned in world %world%", true, new String[] {"%world%"}));
		newMessages.add(new Message(MessageId.theEndAgain_respawnedX, "&a%nb% EnderDragons have been respawned in world %world%", true, new String[] {
				"%nb%",
				"%world%"
		}));
		newMessages.add(new Message(MessageId.theEndAgain_nbAlive0, "&aThere is no EnderDragon alive in world %world%", true, new String[] {"%world%"}));
		newMessages.add(new Message(MessageId.theEndAgain_nbAlive1, "&aThere is 1 EnderDragon alive in world %world%", true, new String[] {"%world%"}));
		newMessages.add(new Message(MessageId.theEndAgain_nbAliveX, "&aThere are %nb% EnderDragons alive in world %world%", true, new String[] {
				"%nb%",
				"%world%"
		}));
		newMessages.add(new Message(MessageId.theEndAgain_notInAnEndWorld, "&cYou're not in an End world, please provide an End world name.", true, null));

		newMessages.add(new Message(MessageId.theEndAgain_protectedChunkInfo, "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais protected", true, new String[] {
				"%x%",
				"%z%",
				"%world%"
		}));
		newMessages.add(new Message(MessageId.theEndAgain_protectedChunkProtect, "&cChunk (&6%x%&c,&6%z%&c) in world &6%world% &cis already protected", true, new String[] {
				"%x%",
				"%z%",
				"%world%"
		}));
		newMessages.add(new Message(MessageId.theEndAgain_protectedChunkUnprotect, "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais no longer protected", true, new String[] {
				"%x%",
				"%z%",
				"%world%"
		}));

		newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkInfo, "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais not protected", true, new String[] {
				"%x%",
				"%z%",
				"%world%"
		}));
		newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkProtect, "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais now protected", true, new String[] {
				"%x%",
				"%z%",
				"%world%"
		}));
		newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkUnprotect, "&cChunk (&6%x%&c,&6%z%&c) in world &6%world% &cis already not protected", true, new String[] {
				"%x%",
				"%z%",
				"%world%"
		}));

		// Others - Events
		newMessages.add(new Message(MessageId.theEndAgain_worldRegenerating, "&aEnd world regenerating", true, null));
		newMessages.add(new Message(MessageId.theEndAgain_receivedXP, "&aYou received %nb% XP points", true, new String[] {"%nb%"}));
		newMessages.add(new Message(MessageId.theEndAgain_receivedDragonEgg, "&aYou received a Dragon Egg !", true, null));
		newMessages.add(new Message(MessageId.theEndAgain_droppedDragonEgg, "&aYour inventory is full, dropped a DragonEgg !", true, null));
		newMessages.add(new Message(MessageId.theEndAgain_receivedDrop, "&aYou received a Drop !", true, null));
		newMessages.add(new Message(MessageId.theEndAgain_droppedDrop, "&aYour inventory is full, dropped something !", true, null));

		return newMessages;
	}
}

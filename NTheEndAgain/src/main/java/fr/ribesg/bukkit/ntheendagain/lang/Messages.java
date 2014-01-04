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
 * Messages for NCuboid
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	public Messages() {
		super("TheEndAgain");
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
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null, true));

		newMessages.add(new Message(MessageId.missingWorldArg, "&cNon-player user should provide a world name", null, null, true));
		newMessages.add(new Message(MessageId.unknownWorld, "&cUnknown world '%world%'", new String[] {"%world%"}, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null, true));

		// Command - END
		newMessages.add(new Message(MessageId.theEndAgain_unkownSubCmd,
		                            "&cUnknown sub-command: %arg%",
		                            new String[] {"%arg%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_regenerating,
		                            "&aRegeneration of world %world% in progress",
		                            new String[] {"%world%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_respawned1,
		                            "&aAn EnderDragon has been respawned in world %world%",
		                            new String[] {"%world%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_respawnedX,
		                            "&a%nb% EnderDragons have been respawned in world %world%",
		                            new String[] {
				                            "%nb%",
				                            "%world%"
		                            },
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_nbAlive0,
		                            "&aThere is no EnderDragon alive in world %world%",
		                            new String[] {"%world%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_nbAlive1,
		                            "&aThere is 1 EnderDragon alive in world %world%",
		                            new String[] {"%world%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_nbAliveX, "&aThere are %nb% EnderDragons alive in world %world%", new String[] {
				"%nb%",
				"%world%"
		}, null, true));
		newMessages.add(new Message(MessageId.theEndAgain_notInAnEndWorld,
		                            "&cYou're not in an End world, please provide an End world name.",
		                            null,
		                            null,
		                            true));

		newMessages.add(new Message(MessageId.theEndAgain_protectedChunkInfo,
		                            "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais protected",
		                            new String[] {
				                            "%x%",
				                            "%z%",
				                            "%world%"
		                            },
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_protectedChunkProtect,
		                            "&cChunk (&6%x%&c,&6%z%&c) in world &6%world% &cis already protected",
		                            new String[] {
				                            "%x%",
				                            "%z%",
				                            "%world%"
		                            },
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_protectedChunkUnprotect,
		                            "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais no longer protected",
		                            new String[] {
				                            "%x%",
				                            "%z%",
				                            "%world%"
		                            },
		                            null,
		                            true));

		newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkInfo,
		                            "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais not protected",
		                            new String[] {
				                            "%x%",
				                            "%z%",
				                            "%world%"
		                            },
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkProtect,
		                            "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais now protected",
		                            new String[] {
				                            "%x%",
				                            "%z%",
				                            "%world%"
		                            },
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkUnprotect,
		                            "&cChunk (&6%x%&c,&6%z%&c) in world &6%world% &cis already not protected",
		                            new String[] {
				                            "%x%",
				                            "%z%",
				                            "%world%"
		                            },
		                            null,
		                            true));

		// Others - Events
		newMessages.add(new Message(MessageId.theEndAgain_worldRegenerating, "&aEnd world regenerating", null, null, true));
		newMessages.add(new Message(MessageId.theEndAgain_receivedXP, "&aYou received %nb% XP points", new String[] {"%nb%"}, null, true));
		newMessages.add(new Message(MessageId.theEndAgain_receivedDragonEgg, "&aYou received a Dragon Egg !", null, null, true));
		newMessages.add(new Message(MessageId.theEndAgain_droppedDragonEgg,
		                            "&aYour inventory is full, dropped a DragonEgg !",
		                            null,
		                            null,
		                            true));

		return newMessages;
	}
}

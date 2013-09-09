package fr.ribesg.bukkit.ncuboid.lang;

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

	/**
	 */
	public Messages() {
		super("Cuboid");
	}

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage() */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General deny response
		newMessages.add(new Message(MessageId.cuboid_actionCancelledByCuboid,
		                            "&cAction cancelled by the cuboid %cuboid%",
		                            new String[] {"%cuboid%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.noPermissionForCommand,
		                            "&cYou do not have the permission to use that command",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null, true));

		// PlayerStickListener
		newMessages.add(new Message(MessageId.cuboid_firstPointSelected,
		                            "&aFirst point selected : %coords%",
		                            new String[] {"%coords%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_secondPointSelected,
		                            "&aSecond point selected : %coords%%%&aSelection Size : %size%",
		                            new String[] {"%coords%", "%size%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_blockInSelection, "&aThis block is in your selection", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockNotInSelection, "&cThis block is not in your selection", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockNotProtected, "&aThis block is not protected", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedOneCuboid,
		                            "&cThis block is protected by one cuboid:" + LINE_SEPARATOR + "%cuboidInfo%",
		                            new String[] {"%cuboidInfo%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedMultipleCuboids,
		                            "&cThis block is protected by %nb% cuboids:" + LINE_SEPARATOR + "%cuboidsInfos%",
		                            new String[] {"%nb%", "%cuboidsInfos%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_selectionReset, "&aYour selection has been reset", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_noSelection, "&cYou have no selection to reset", null, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cuboid_cmdReloadCuboids, "&aCuboids reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null, true));

		// Command - CREATE
		newMessages.add(new Message(MessageId.cuboid_cmdCreateAlreadyExists, "&cThis cuboid already exists !", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateCreated,
		                            "&aYou created the cuboid &6%cuboidName% &a!",
		                            new String[] {"%cuboidName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateNoValidSelection,
		                            "&cYou need a vallid selection to create a cuboid !",
		                            null,
		                            null,
		                            true));

		// Command - DELETE
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteDoesNotExist, "&cThis cuboid does not exist !", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteDeleted,
		                            "&aThe cuboid &6%cuboidName% &ahas been deleted !",
		                            new String[] {"%cuboidName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteNoPermission,
		                            "&cYou do not have the permission to delete &6%cuboidName% &c!",
		                            new String[] {"%cuboidName%"},
		                            null,
		                            true));
		return newMessages;
	}
}

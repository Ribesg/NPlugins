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
		                            "&cAction cancelled by the region %region%",
		                            new String[] {"%region%"},
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
		                            "&aFirst point selected: %coords%",
		                            new String[] {"%coords%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_secondPointSelected,
		                            "&aSecond point selected: %coords%" + LINE_SEPARATOR + "&aSelection Size : %size%",
		                            new String[] {"%coords%", "%size%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_blockInSelection, "&aThis block is in your selection", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockNotInSelection, "&cThis block is not in your selection", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockNotProtected, "&aThis block is not protected", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedOneRegion,
		                            "&cThis block is protected by one region:" + LINE_SEPARATOR + "%regionInfo%",
		                            new String[] {"%cuboidInfo%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedMultipleRegions,
		                            "&cThis block is protected by %nb% regions:" + LINE_SEPARATOR + "%regionsInfos%",
		                            new String[] {"%nb%", "%regionsInfos%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_selectionReset, "&aYour selection has been reset", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_noSelection, "&cYou have no selection to reset", null, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cuboid_cmdReloadRegions, "&aRegions reloaded!", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded!", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadError,
		                            "&An error occured while loading %file%!",
		                            new String[] {"%file%"},
		                            null,
		                            true));

		// Command - CREATE
		newMessages.add(new Message(MessageId.cuboid_cmdCreateAlreadyExists, "&cThis region already exists !", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateCreated,
		                            "&aYou created the region &6%regionName%&a!",
		                            new String[] {"%regionName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateNoValidSelection,
		                            "&cYou need a valid selection to create a region!",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateForbiddenName,
		                            "&cSorry, you can't use a name prefixed by 'world_'!",
		                            null,
		                            null,
		                            true));

		// Command - DELETE
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteDoesNotExist, "&cThis region does not exist!", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteDeleted,
		                            "&aThe region &6%regionName% &ahas been deleted!",
		                            new String[] {"%regionName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteNoPermission,
		                            "&cYou do not have the permission to delete &6%regionName%&c!",
		                            new String[] {"%regionName%"},
		                            null,
		                            true));
		return newMessages;
	}
}

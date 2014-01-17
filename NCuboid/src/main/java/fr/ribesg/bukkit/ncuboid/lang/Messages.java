/***************************************************************************
 * Project file:    NPlugins - NCuboid - Messages.java                     *
 * Full Class name: fr.ribesg.bukkit.ncuboid.lang.Messages                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
		newMessages.add(new Message(MessageId.cuboid_actionCancelledByCuboid, "&cAction cancelled by the region %region%", new String[] {"%region%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_doesNotExist, "&cThe region '%region%' does not exist!", new String[] {"%region%"}, null, true));
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null, true));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_notCuboidOwner, "&cYou need to be the owner of '%region%'!", new String[] {"%region%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_notCuboidAdmin, "&cYou don't have the right to modify '%region%'!", new String[] {"%region%"}, null, true));

		// PlayerStickListener
		newMessages.add(new Message(MessageId.cuboid_firstPointSelected, "&aFirst point selected: %coords%", new String[] {"%coords%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_secondPointSelected, "&aSecond point selected: %coords%" + LINE_SEPARATOR + "&aSelection Size : %size%", new String[] {
				"%coords%",
				"%size%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockInSelection, "&aThis block is in your selection", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockNotInSelection, "&cThis block is not in your selection", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockNotProtected, "&aThis block is not protected", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedOneRegion, "&cThis block is protected by one region:" + LINE_SEPARATOR + "%regionInfo%", new String[] {"%regionInfo%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedMultipleRegions, "&cThis block is protected by %nb% regions:" + LINE_SEPARATOR + "%regionsInfos%", new String[] {
				"%nb%",
				"%regionsInfos%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_selectionReset, "&aYour selection has been reset", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_noSelection, "&cYou have no selection to reset", null, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cuboid_cmdReloadRegions, "&aRegions reloaded!", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded!", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadError, "&An error occured while loading %file%!", new String[] {"%file%"}, null, true));

		// Command - CREATE
		newMessages.add(new Message(MessageId.cuboid_cmdCreateAlreadyExists, "&cThis region already exists !", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateCreated, "&aYou created the region &6%regionName%&a!", new String[] {"%regionName%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateNoValidSelection, "&cYou need a valid selection to create a region!", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateForbiddenName, "&cSorry, you can't use a name prefixed by 'world_'!", null, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateTooMuchRegions, "&cYou are only allowed to have %max% regions, and you currently have %value% regions!", new String[] {
				"%max%",
				"%value%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateRegionTooLong, "&cYour selection is too long (%value%), your maximum allowed length is %max%!", new String[] {
				"%max%",
				"%value%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateRegionTooBig, "&cYour selection is too big (%value% blocks), your maximum allowed size is %max% blocks!", new String[] {
				"%max%",
				"%value%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateOverlap, "&cYour selection overlaps with the region '%regionName%'!", new String[] {"%regionName%"}, null, true));

		// Command - DELETE
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteDeleted, "&aThe region &6%regionName% &ahas been deleted!", new String[] {"%regionName%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteNoPermission, "&cYou do not have the permission to delete &6%regionName%&c!", new String[] {"%regionName%"}, null, true));

		// Command - FLAG
		newMessages.add(new Message(MessageId.cuboid_cmdFlagUnknownFlag, "&cUnknown flag: '%flagName%'", new String[] {"%flagName%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagUnknownValue, "&cUnknown value: '%value%'", new String[] {"%value%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagAlreadySet, "&6The flag '%flag%' is already '%value%' for region '%region%'", new String[] {
				"%flag%",
				"%value%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagSet, "&aThe flag '%flag%' is now '%value%' for region '%region%'", new String[] {
				"%flag%",
				"%value%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagNoPermission, "&cYou don't have the permissions to use flag '%flagName%'", new String[] {"%flagName%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagValue, "&aThe flag '%flag%' is '%value%' for region '%region%'", new String[] {
				"%flag%",
				"%value%",
				"%region%"
		}, null, true));

		// Command - FLAG ATTRIBUTE
		newMessages.add(new Message(MessageId.cuboid_cmdFlagAttUnknownFlagAtt, "&cUnknown flag attribute: '%flagAttName%'", new String[] {"%flagAttName%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagAttNoPermission, "&cYou don't have the permissions to use flag attribute '%flagAttName%'", new String[] {"%flagAttName%"}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagAttValue, "&aThe flag attribute '%flagAtt%' is currently set to '%value%' for region '%region%'", new String[] {
				"%flagAtt%",
				"%value%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagAttSet, "&aThe flag attribute '%flagAtt%' is now set to '%value%' for region '%region%'", new String[] {
				"%flagAtt%",
				"%value%",
				"%region%"
		}, null, true));

		// Command - ADMIN
		newMessages.add(new Message(MessageId.cuboid_cmdAdminAdded, "&a%player% has been added to admins of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdAdminAlreadyAdmin, "&c%player% is already admin of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdAdminRemoved, "&a%player% is no longer admin of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdAdminRemoved, "&c%player% is not admin of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));

		// Command - USER
		newMessages.add(new Message(MessageId.cuboid_cmdUserAdded, "&a%player% has been added to users of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdUserAlreadyUser, "&c%player% is already user of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdUserRemoved, "&a%player% is no longer user of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));
		newMessages.add(new Message(MessageId.cuboid_cmdUserRemoved, "&c%player% is not user of '%region%'", new String[] {
				"%player%",
				"%region%"
		}, null, true));

		return newMessages;
	}
}

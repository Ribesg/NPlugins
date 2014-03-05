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
 * Messages for NCuboid
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	public Messages() {
		super("Cuboid");
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage()
	 */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General deny response
		newMessages.add(new Message(MessageId.cuboid_actionCancelledByCuboid, "&cAction cancelled by the region %region%", true, new String[] {"%region%"}));
		newMessages.add(new Message(MessageId.cuboid_doesNotExist, "&cThe region '%region%' does not exist!", true, new String[] {"%region%"}));
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", true, null));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", true, null));
		newMessages.add(new Message(MessageId.cuboid_notCuboidOwner, "&cYou need to be the owner of '%region%'!", true, new String[] {"%region%"}));
		newMessages.add(new Message(MessageId.cuboid_notCuboidAdmin, "&cYou don't have the right to modify '%region%'!", true, new String[] {"%region%"}));

		// PlayerStickListener
		newMessages.add(new Message(MessageId.cuboid_firstPointSelected, "&aFirst point selected: %coords%", true, new String[] {"%coords%"}));
		newMessages.add(new Message(MessageId.cuboid_secondPointSelected, "&aSecond point selected: %coords%" + LINE_SEPARATOR + "&aSelection Size : %size%", true, new String[] {
				"%coords%",
				"%size%"
		}));
		newMessages.add(new Message(MessageId.cuboid_blockInSelection, "&aThis block is in your selection", true, null));
		newMessages.add(new Message(MessageId.cuboid_blockNotInSelection, "&cThis block is not in your selection", true, null));
		newMessages.add(new Message(MessageId.cuboid_blockNotProtected, "&aThis block is not protected", true, null));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedOneRegion, "&cThis block is protected by one region:" + LINE_SEPARATOR + "%regionInfo%", true, new String[] {"%regionInfo%"}));
		newMessages.add(new Message(MessageId.cuboid_blockProtectedMultipleRegions, "&cThis block is protected by %nb% regions:" + LINE_SEPARATOR + "%regionsInfos%", true, new String[] {
				"%nb%",
				"%regionsInfos%"
		}));
		newMessages.add(new Message(MessageId.cuboid_selectionReset, "&aYour selection has been reset", true, null));
		newMessages.add(new Message(MessageId.cuboid_noSelection, "&cYou have no selection to reset", true, null));

		// PVP entering/exiting messages
		newMessages.add(new Message(MessageId.cuboid_enteringPvpArea, "&6You are entering a PVP area: %region%", true, new String[] {"%region%"}));
		newMessages.add(new Message(MessageId.cuboid_exitingPvpArea, "&6You are leaving a PVP area: %region%", true, new String[] {"%region%"}));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cuboid_cmdReloadRegions, "&aRegions reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadError, "&An error occured while loading %file%!", true, new String[] {"%file%"}));

		// Command - CREATE
		newMessages.add(new Message(MessageId.cuboid_cmdCreateAlreadyExists, "&cThis region already exists !", true, null));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateCreated, "&aYou created the region &6%regionName%&a!", true, new String[] {"%regionName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateNoValidSelection, "&cYou need a valid selection to create a region!", true, null));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateForbiddenName, "&cSorry, you can't use a name prefixed by 'world_'!", true, null));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateTooMuchRegions, "&cYou are only allowed to have %max% regions, and you currently have %value% regions!", true, new String[] {
				"%max%",
				"%value%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateRegionTooLong, "&cYour selection is too long (%value%), your maximum allowed length is %max%!", true, new String[] {
				"%max%",
				"%value%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateRegionTooBig, "&cYour selection is too big (%value% blocks), your maximum allowed size is %max% blocks!", true, new String[] {
				"%max%",
				"%value%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdCreateOverlap, "&cYour selection overlaps with the region '%regionName%'!", true, new String[] {"%regionName%"}));

		// Command - DELETE
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteDeleted, "&aThe region &6%regionName% &ahas been deleted!", true, new String[] {"%regionName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdDeleteNoPermission, "&cYou do not have the permission to delete &6%regionName%&c!", true, new String[] {"%regionName%"}));

		// Command - FLAG
		newMessages.add(new Message(MessageId.cuboid_cmdFlagUnknownFlag, "&cUnknown flag: '%flagName%'", true, new String[] {"%flagName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagUnknownValue, "&cUnknown value: '%value%'", true, new String[] {"%value%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagAlreadySet, "&6The flag '%flag%' is already '%value%' for region '%region%'", true, new String[] {
				"%flag%",
				"%value%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagSet, "&aThe flag '%flag%' is now '%value%' for region '%region%'", true, new String[] {
				"%flag%",
				"%value%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagNoPermission, "&cYou don't have the permissions to use flag '%flagName%'", true, new String[] {"%flagName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagValue, "&aThe flag '%flag%' is '%value%' for region '%region%'", true, new String[] {
				"%flag%",
				"%value%",
				"%region%"
		}));

		// Command - ATTRIBUTE
		newMessages.add(new Message(MessageId.cuboid_cmdAttUnknownFlagAtt, "&cUnknown attribute: '%attName%'", true, new String[] {"%attName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdAttNoPermission, "&cYou don't have the permissions to use attribute '%attName%'", true, new String[] {"%attName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdAttValue, "&aThe attribute '%att%' is currently set to '%value%' for region '%region%'", true, new String[] {
				"%att%",
				"%value%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdFlagAttSet, "&aThe attribute '%att%' is now set to '%value%' for region '%region%'", true, new String[] {
				"%att%",
				"%value%",
				"%region%"
		}));

		// Command - ADMIN
		newMessages.add(new Message(MessageId.cuboid_cmdAdminAdded, "&a%player% has been added to admins of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdAdminAlreadyAdmin, "&c%player% is already admin of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdAdminRemoved, "&a%player% is no longer admin of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdAdminRemoved, "&c%player% is not admin of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));

		// Command - USER
		newMessages.add(new Message(MessageId.cuboid_cmdUserAdded, "&a%player% has been added to users of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdUserAlreadyUser, "&c%player% is already user of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdUserRemoved, "&a%player% is no longer user of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdUserRemoved, "&c%player% is not user of '%region%'", true, new String[] {
				"%player%",
				"%region%"
		}));

		// Command - GROUP
		newMessages.add(new Message(MessageId.cuboid_cmdGroupAdded, "&a%groupName% has been added to allowed groups of '%region%'", true, new String[] {
				"%groupName%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdGroupAlreadyGroup, "&cThe group %groupName% is already allowed for Region '%region%'", true, new String[] {
				"%groupName%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdGroupRemoved, "&aThe group %groupName% is no longer allowed for Region '%region%'", true, new String[] {
				"%groupName%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdGroupRemoved, "&cThe group %groupName% is not allowed in Region '%region%'", true, new String[] {
				"%groupName%",
				"%region%"
		}));

		// Command - JAIL
		newMessages.add(new Message(MessageId.cuboid_cmdJailNotJailCuboid, "&cThe Region %region% is not a Jail Region", true, new String[] {"%region%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdJailNotInRegion, "&cThe Location %location% is not in the Region %region%", true, new String[] {
				"%location%",
				"%region%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdJailCreated, "&aCreated jail '%jailName%' in Region %region% at location %location%", true, new String[] {
				"%jailName%",
				"%region%",
				"%location%"
		}));
		newMessages.add(new Message(MessageId.cuboid_cmdJailAlreadyExists, "&cA Jail with the name %jailName% already exists", true, new String[] {"%jailName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdJailRemoved, "&aThe jail '%jailName%' has been removed", true, new String[] {"%jailName%"}));
		newMessages.add(new Message(MessageId.cuboid_cmdJailUnknown, "&cUnknown jail: %jailName%", true, new String[] {"%jailName%"}));

		return newMessages;
	}
}

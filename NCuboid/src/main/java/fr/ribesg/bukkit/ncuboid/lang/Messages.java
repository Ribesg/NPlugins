/***************************************************************************
 * Project file:    NPlugins - NCuboid - Messages.java                     *
 * Full Class name: fr.ribesg.bukkit.ncuboid.lang.Messages                 *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
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

    @Override
    protected Set<Message> createMessage() {
        final Set<Message> newMessages = new HashSet<>();

        // General deny response
        newMessages.add(new Message(MessageId.cuboid_actionCancelledByCuboid, "&cAction cancelled by the region %region%", true, "%region%"));
        newMessages.add(new Message(MessageId.cuboid_doesNotExist, "&cThe region '%region%' does not exist!", true, "%region%"));
        newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", true));
        newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", true));
        newMessages.add(new Message(MessageId.cuboid_notCuboidOwner, "&cYou need to be the owner of '%region%'!", true, "%region%"));
        newMessages.add(new Message(MessageId.cuboid_notCuboidAdmin, "&cYou don't have the right to modify '%region%'!", true, "%region%"));

        // PlayerStickListener
        newMessages.add(new Message(MessageId.cuboid_firstPointSelected, "&aFirst point selected: %coords%", true, "%coords%"));
        newMessages.add(new Message(MessageId.cuboid_secondPointSelected, "&aSecond point selected: %coords%" + LINE_SEPARATOR + "&aSelection Size : %size%", true, "%coords%", "%size%"));
        newMessages.add(new Message(MessageId.cuboid_blockInSelection, "&aThis block is in your selection", true));
        newMessages.add(new Message(MessageId.cuboid_blockNotInSelection, "&cThis block is not in your selection", true));
        newMessages.add(new Message(MessageId.cuboid_blockNotProtected, "&aThis block is not protected", true));
        newMessages.add(new Message(MessageId.cuboid_blockProtectedOneRegion, "&cThis block is protected by one region:" + LINE_SEPARATOR + "%regionInfo%", true, "%regionInfo%"));
        newMessages.add(new Message(MessageId.cuboid_blockProtectedMultipleRegions, "&cThis block is protected by %nb% regions:" + LINE_SEPARATOR + "%regionsInfos%", true, "%nb%", "%regionsInfos%"));
        newMessages.add(new Message(MessageId.cuboid_selectionReset, "&aYour selection has been reset", true));
        newMessages.add(new Message(MessageId.cuboid_noSelection, "&cYou have no selection to reset", true));

        // PVP entering/exiting messages
        newMessages.add(new Message(MessageId.cuboid_enteringPvpArea, "&6You are entering a PVP area: %region%", true, "%region%"));
        newMessages.add(new Message(MessageId.cuboid_exitingPvpArea, "&6You are leaving a PVP area: %region%", true, "%region%"));

        // Command - RELOAD
        newMessages.add(new Message(MessageId.cuboid_cmdReloadRegions, "&aRegions reloaded!", true));
        newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded!", true));
        newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true));
        newMessages.add(new Message(MessageId.cmdReloadError, "&An error occured while loading %file%!", true, "%file%"));

        // Command - CREATE
        newMessages.add(new Message(MessageId.cuboid_cmdCreateAlreadyExists, "&cThis region already exists !", true));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateCreated, "&aYou created the region &6%regionName%&a!", true, "%regionName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateNoValidSelection, "&cYou need a valid selection to create a region!", true));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateForbiddenName, "&cSorry, you can't use a name prefixed by 'world_'!", true));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateTooMuchRegions, "&cYou are only allowed to have %max% regions, and you currently have %value% regions!", true, "%max%", "%value%"));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateRegionTooLong, "&cYour selection is too long (%value%), your maximum allowed length is %max%!", true, "%max%", "%value%"));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateRegionTooBig, "&cYour selection is too big (%value% blocks), your maximum allowed size is %max% blocks!", true, "%max%", "%value%"));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateOverlap, "&cYour selection overlaps with the region '%regionName%'!", true, "%regionName%"));

        // Command - DELETE
        newMessages.add(new Message(MessageId.cuboid_cmdDeleteDeleted, "&aThe region &6%regionName% &ahas been deleted!", true, "%regionName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdDeleteNoPermission, "&cYou do not have the permission to delete &6%regionName%&c!", true, "%regionName%"));

        // Command - FLAG
        newMessages.add(new Message(MessageId.cuboid_cmdFlagUnknownFlag, "&cUnknown flag: '%flagName%'", true, "%flagName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdFlagUnknownValue, "&cUnknown value: '%value%'", true, "%value%"));
        newMessages.add(new Message(MessageId.cuboid_cmdFlagAlreadySet, "&6The flag '%flag%' is already '%value%' for region '%region%'", true, "%flag%", "%value%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdFlagSet, "&aThe flag '%flag%' is now '%value%' for region '%region%'", true, "%flag%", "%value%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdFlagNoPermission, "&cYou don't have the permissions to use flag '%flagName%'", true, "%flagName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdFlagValue, "&aThe flag '%flag%' is '%value%' for region '%region%'", true, "%flag%", "%value%", "%region%"));

        // Command - ATTRIBUTE
        newMessages.add(new Message(MessageId.cuboid_cmdAttUnknownFlagAtt, "&cUnknown attribute: '%attName%'", true, "%attName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdAttNoPermission, "&cYou don't have the permissions to use attribute '%attName%'", true, "%attName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdAttValue, "&aThe attribute '%att%' is currently set to '%value%&a' for region '%region%'", true, "%att%", "%value%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdFlagAttSet, "&aThe attribute '%att%' is now set to '%value%' for region '%region%'", true, "%att%", "%value%", "%region%"));

        // Command - ADMIN
        newMessages.add(new Message(MessageId.cuboid_cmdAdminAdded, "&a%player% has been added to admins of '%region%'", true, "%player%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdAdminAlreadyAdmin, "&c%player% is already admin of '%region%'", true, "%player%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdAdminRemoved, "&a%player% is no longer admin of '%region%'", true, "%player%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdAdminRemoved, "&c%player% is not admin of '%region%'", true, "%player%", "%region%"));

        // Command - USER
        newMessages.add(new Message(MessageId.cuboid_cmdUserAdded, "&a%player% has been added to users of '%region%'", true, "%player%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdUserAlreadyUser, "&c%player% is already user of '%region%'", true, "%player%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdUserRemoved, "&a%player% is no longer user of '%region%'", true, "%player%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdUserRemoved, "&c%player% is not user of '%region%'", true, "%player%", "%region%"));

        // Command - GROUP
        newMessages.add(new Message(MessageId.cuboid_cmdGroupAdded, "&a%groupName% has been added to allowed groups of '%region%'", true, "%groupName%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdGroupAlreadyGroup, "&cThe group %groupName% is already allowed for Region '%region%'", true, "%groupName%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdGroupRemoved, "&aThe group %groupName% is no longer allowed for Region '%region%'", true, "%groupName%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdGroupRemoved, "&cThe group %groupName% is not allowed in Region '%region%'", true, "%groupName%", "%region%"));

        // Command - JAIL
        newMessages.add(new Message(MessageId.cuboid_cmdJailNotJailCuboid, "&cThe Region %region% is not a Jail Region", true, "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdJailNotInRegion, "&cThe Location %location% is not in the Region %region%", true, "%location%", "%region%"));
        newMessages.add(new Message(MessageId.cuboid_cmdJailCreated, "&aCreated jail '%jailName%' in Region %region% at location %location%", true, "%jailName%", "%region%", "%location%"));
        newMessages.add(new Message(MessageId.cuboid_cmdJailAlreadyExists, "&cA Jail with the name %jailName% already exists", true, "%jailName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdJailRemoved, "&aThe jail '%jailName%' has been removed", true, "%jailName%"));
        newMessages.add(new Message(MessageId.cuboid_cmdJailUnknown, "&cUnknown jail: %jailName%", true, "%jailName%"));

        return newMessages;
    }
}

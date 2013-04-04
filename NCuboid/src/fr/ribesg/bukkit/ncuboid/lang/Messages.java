package fr.ribesg.bukkit.ncuboid.lang;

import java.util.HashSet;
import java.util.Set;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

/**
 * Messages for NTheEndAgain
 * 
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

    /**
     */
    public Messages() {
        super("NCuboid");
    }

    /**
     * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage()
     */
    @Override
    protected Set<Message> createMessage() {
        final Set<Message> newMessages = new HashSet<Message>();

        // General deny response
        newMessages.add(new Message(MessageId.cuboid_actionCancelledByCuboid, "&cAction cancelled by the cuboid %cuboid%", new String[] { "%cuboid%" }, null));
        newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null));
        newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null));

        // PlayerStickListener
        newMessages.add(new Message(MessageId.cuboid_firstPointSelected, "&aFirst point selected : %coords%", new String[] { "%coords%" }, null));
        newMessages.add(new Message(MessageId.cuboid_secondPointSelected, "&aSecond point selected : %coords%%%&aSelection Size : %size%", new String[] { "%coords%", "%size%" }, null));
        newMessages.add(new Message(MessageId.cuboid_blockInSelection, "&aThis block is in your selection", null, null));
        newMessages.add(new Message(MessageId.cuboid_blockNotInSelection, "&cThis block is not in your selection", null, null));
        newMessages.add(new Message(MessageId.cuboid_blockNotProtected, "&aThis block is not protected", null, null));
        newMessages.add(new Message(MessageId.cuboid_blockProtectedOneCuboid, "&cThis block is protected by one cuboid:" + LINE_SEPARATOR + "%cuboidInfo%", new String[] { "%cuboidInfo%" }, null));
        newMessages.add(new Message(MessageId.cuboid_blockProtectedMultipleCuboids, "&cThis block is protected by %nb% cuboids:" + LINE_SEPARATOR + "%cuboidsInfos%", new String[] { "%nb%", "%cuboidsInfos%" }, null));
        newMessages.add(new Message(MessageId.cuboid_selectionReset, "&aYour selection has been reset", null, null));
        newMessages.add(new Message(MessageId.cuboid_noSelection, "&cYou have no selection to reset", null, null));

        // Command - RELOAD
        newMessages.add(new Message(MessageId.cuboid_cmdReloadCuboids, "&aCuboids reloaded !", null, null));
        newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null));
        newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null));

        // Command - CREATE
        newMessages.add(new Message(MessageId.cuboid_cmdCreateAlreadyExists, "&cThis cuboid already exists !", null, null));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateCreated, "&aYou created the cuboid &6%cuboidName% &a!", new String[] { "%cuboidName%" }, null));
        newMessages.add(new Message(MessageId.cuboid_cmdCreateNoValidSelection, "&cYou need a vallid selection to create a cuboid !", null, null));

        // Command - DELETE
        newMessages.add(new Message(MessageId.cuboid_cmdDeleteDoesNotExist, "&cThis cuboid does not exist !", null, null));
        newMessages.add(new Message(MessageId.cuboid_cmdDeleteDeleted, "&aThe cuboid &6%cuboidName% &ahas been deleted !", new String[] { "%cuboidName%" }, null));
        newMessages.add(new Message(MessageId.cuboid_cmdDeleteNoPermission, "&cYou do not have the permission to delete &6%cuboidName% &c!", new String[] { "%cuboidName%" }, null));
        return newMessages;
    }

    /**
     * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#getConfigString()
     */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        content.append("################################################################################\n");
        content.append("# List of NCuboid messages. You're free to change text/colors/language here.   #\n");
        content.append("# Supports both '§' and '&' characters for colors.                      Ribesg #\n");
        content.append("################################################################################\n\n");
        for (final Message m : getMessagesMap().values()) {
            content.append("# Default value    : " + m.getDefaultMessage() + '\n');
            content.append("# Awaited arguments: " + m.getAwaitedArgsString() + '\n');
            content.append(m.getId().name() + ": \"" + (m.getConfigMessage() != null ? m.getConfigMessage() : m.getDefaultMessage()) + "\"\n\n");
        }
        return content.toString();
    }

}

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
        final Set<Message> newMessages = new HashSet<Message>();

        // General plugin messages
        newMessages.add(new Message(MessageId.incorrectValueInConfiguration,
                                    "&cIncorrect value in configuration %filename% : '%value%' was restored to default (%default%)",
                                    new String[] {"%filename%", "%value%", "%default%"},
                                    null));

        // General deny response
        newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null));
        newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null));

        // Command - RELOAD
        newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null));
        newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null));

        // Command - END
        newMessages.add(new Message(MessageId.theEndAgain_unkownSubCmd, "&cUnknown sub-command: %arg%", new String[] {"%arg%"}, null));
        newMessages.add(new Message(MessageId.theEndAgain_unknownWorld, "&cUnknown world", null, null));
        newMessages.add(new Message(MessageId.theEndAgain_regenerating,
                                    "&aRegeneration of world %world% in progress",
                                    new String[] {"%world%"},
                                    null));
        newMessages.add(new Message(MessageId.theEndAgain_respawned,
                                    "&a%nb% EnderDragons have been respawned in world %world%",
                                    new String[] {"%nb%", "%world%"},
                                    null));
        newMessages.add(new Message(MessageId.theEndAgain_nbAlive,
                                    "&aThere are %nb% EnderDragons alive in world %world%",
                                    new String[] {"%nb%", "%world%"},
                                    null));
        newMessages.add(new Message(MessageId.theEndAgain_notInAnEndWorld,
                                    "&cYou're not in an End world, please provide an End world name.",
                                    null,
                                    null));

        newMessages.add(new Message(MessageId.theEndAgain_protectedChunkInfo,
                                    "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais protected",
                                    new String[] {"%x%", "%z%", "%world%"},
                                    null));
        newMessages.add(new Message(MessageId.theEndAgain_protectedChunkProtect,
                                    "&cChunk (&6%x%&c,&6%z%&c) in world &6%world% &cis already protected",
                                    new String[] {"%x%", "%z%", "%world%"},
                                    null));
        newMessages.add(new Message(MessageId.theEndAgain_protectedChunkUnprotect,
                                    "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais no longer protected",
                                    new String[] {"%x%", "%z%", "%world%"},
                                    null));

        newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkInfo,
                                    "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais not protected",
                                    new String[] {"%x%", "%z%", "%world%"},
                                    null));
        newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkProtect,
                                    "&aChunk (&6%x%&a,&6%z%&a) in world &6%world% &ais now protected",
                                    new String[] {"%x%", "%z%", "%world%"},
                                    null));
        newMessages.add(new Message(MessageId.theEndAgain_unprotectedChunkUnprotect,
                                    "&cChunk (&6%x%&c,&6%z%&c) in world &6%world% &cis already not protected",
                                    new String[] {"%x%", "%z%", "%world%"},
                                    null));

        newMessages.add(new Message(MessageId.theEndAgain_missingWorldArg, "&cNon-player user should provide a world name", null, null));

        // Others - Events
        newMessages.add(new Message(MessageId.theEndAgain_worldRegenerating, "&aEnd world regenerating", null, null));
        newMessages.add(new Message(MessageId.theEndAgain_receivedXP, "&aYou received %nb% XP points", new String[] {"%nb%"}, null));
        newMessages.add(new Message(MessageId.theEndAgain_receivedDragonEgg, "&aYou received a Dragon Egg !", null, null));
        newMessages.add(new Message(MessageId.theEndAgain_droppedDragonEgg, "&aYour inventory is full, dropped a DragonEgg !", null, null));

        return newMessages;
    }

    /** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#getConfigString() */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        content.append("###################################################################################\n");
        content.append("# List of NTheEndAgain messages. You're free to change text/colors/language here. #\n");
        content.append("# Supports both '§' and '&' characters for colors.                         Ribesg #\n");
        content.append("###################################################################################\n\n");
        for (final Message m : getMessagesMap().values()) {
            content.append("# Default value    : " + m.getDefaultMessage() + '\n');
            content.append("# Awaited arguments: " + m.getAwaitedArgsString() + '\n');
            content.append(m.getId().name() +
                           ": \"" +
                           (m.getConfigMessage() != null ? m.getConfigMessage() : m.getDefaultMessage()) +
                           "\"\n\n");
        }
        return content.toString();
    }
}

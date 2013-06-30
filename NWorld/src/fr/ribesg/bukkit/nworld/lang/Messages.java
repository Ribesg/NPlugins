package fr.ribesg.bukkit.nworld.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
import java.util.Set;

/**
 * Messages for NWorld
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

    /**
     */
    public Messages() {
        super("World");
    }

    /**
     * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage()
     */
    @Override
    protected Set<Message> createMessage() {
        final Set<Message> newMessages = new HashSet<Message>();

        // General plugin messages
        newMessages.add(new Message(MessageId.incorrectValueInConfiguration, "&cIncorrect value in configuration %filename% : '%value%' was restored to default (%default%)", new String[] {"%filename%", "%value%", "%default%"}, null));

        // General deny response
        newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null));
        newMessages.add(new Message(MessageId.noPlayerFoundForGivenName, "&cNo online player found for input %playerName%", new String[] {"%playerName%"}, null));
        newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null));

        // Command - RELOAD
        newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null));
        newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null));

        // Command - WORLD
        newMessages.add(new Message(MessageId.world_availableWorlds, "&aList of loaded worlds:", null, null));
        newMessages.add(new Message(MessageId.world_teleportedTo, "&aYou teleported yourself to the world %worldName%", new String[] {"%worldName%"}, null));

        newMessages.add(new Message(MessageId.world_unknownWorld, "&cUnkown world: %worldName%", new String[] {"%worldName%"}, null));
        newMessages.add(new Message(MessageId.world_notLoaded, "&cThe world %worldName% exists but is not loaded", new String[] {"%worldName%"}, null));
        newMessages.add(new Message(MessageId.world_warpToThisWorldDisallowed, "&cYou are not allowed to teleport yourself to the world %worldName%", new String[] {"%worldName%"}, null));

        newMessages.add(new Message(MessageId.world_alreadyExists, "&cThe world %worldName% already exists !", new String[] {"%worldName%"}, null));
        newMessages.add(new Message(MessageId.world_alreadyLoaded, "&cThe world %worldName% is already loaded !", new String[] {"%worldName%"}, null));

        newMessages.add(new Message(MessageId.world_creatingWorldMayBeLaggy, "&6Creating a new World, server could be laggy for some seconds", null, null));
        newMessages.add(new Message(MessageId.world_created, "&aWorld creation terminated", null, null));

        newMessages.add(new Message(MessageId.world_loadingWorldMayBeLaggy, "&6Loading a World, server could be laggy for some seconds", null, null));
        newMessages.add(new Message(MessageId.world_loaded, "&aWorld creation terminated", null, null));

        newMessages.add(new Message(MessageId.world_unloadingWorldMayBeLaggy, "&6Unloading a World, server could be laggy for some seconds", null, null));
        newMessages.add(new Message(MessageId.world_unloaded, "&aWorld unload terminated", null, null));

        newMessages.add(new Message(MessageId.world_alreadyAllowed, "&cTeleportation already allowed in world %worldName%", new String[] {"%worldName%"}, null));
        newMessages.add(new Message(MessageId.world_allowedWarp, "&aTeleportation allowed in world %worldName%", new String[] {"%worldName%"}, null));

        newMessages.add(new Message(MessageId.world_alreadyDisallowed, "&cTeleportation already disallowed in world %worldName%", new String[] {"%worldName%"}, null));
        newMessages.add(new Message(MessageId.world_disallowedWarp, "&aTeleportation disallowed in world %worldName%", new String[] {"%worldName%"}, null));

        newMessages.add(new Message(MessageId.world_teleportingToSpawn, "&aTeleportation to this world's spawn point...", null, null));
        newMessages.add(new Message(MessageId.world_settingSpawnPoint, "&aSpawn point of world %worldName% set", new String[] {"%worldName%"}, null));

        return newMessages;
    }

    /**
     * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#getConfigString()
     */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        content.append("###################################################################################\n");
        content.append("# List of NWorld messages. You're free to change text/colors/language here.       #\n");
        content.append("# Supports both '§' and '&' characters for colors.                         Ribesg #\n");
        content.append("###################################################################################\n\n");
        for (final Message m : getMessagesMap().values()) {
            content.append("# Default value    : " + m.getDefaultMessage() + '\n');
            content.append("# Awaited arguments: " + m.getAwaitedArgsString() + '\n');
            content.append(m.getId().name() + ": \"" + (m.getConfigMessage() != null ? m.getConfigMessage() : m.getDefaultMessage()) + "\"\n\n");
        }
        return content.toString();
    }
}

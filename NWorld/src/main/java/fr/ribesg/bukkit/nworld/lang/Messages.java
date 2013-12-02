/***************************************************************************
 * Project file:    NPlugins - NWorld - Messages.java                      *
 * Full Class name: fr.ribesg.bukkit.nworld.lang.Messages                  *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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

	/** @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage() */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General plugin messages
		newMessages.add(new Message(MessageId.incorrectValueInConfiguration,
		                            "&cIncorrect value in configuration %filename% : '%value%' was restored to default (%default%)",
		                            new String[] {"%filename%", "%value%", "%default%"},
		                            null,
		                            true));

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand,
		                            "&cYou do not have the permission to use that command",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.noPlayerFoundForGivenName,
		                            "&cNo online player found for input %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null, true));
		newMessages.add(new Message(MessageId.unknownWorld, "&cUnknown world '%world%'", new String[] {"%world%"}, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null, true));

		// Command - WORLD
		newMessages.add(new Message(MessageId.world_availableWorlds, "&aList of loaded worlds:", null, null, true));
		newMessages.add(new Message(MessageId.world_teleportedToWorld,
		                            "&aYou teleported yourself to the world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_notLoaded,
		                            "&cThe world %worldName% exists but is not loaded",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_warpToThisWorldDisallowed,
		                            "&cYou are not allowed to teleport yourself to the world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_alreadyExists,
		                            "&cThe world %worldName% already exists !",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_alreadyLoaded,
		                            "&cThe world %worldName% is already loaded !",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_creatingWorldMayBeLaggy,
		                            "&6Creating a new World, server could be laggy for some seconds",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_created, "&aWorld creation terminated", null, null, true));
		newMessages.add(new Message(MessageId.world_loadingWorldMayBeLaggy,
		                            "&6Loading a World, server could be laggy for some seconds",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_loaded, "&aWorld creation terminated", null, null, true));
		newMessages.add(new Message(MessageId.world_unloadingWorldMayBeLaggy,
		                            "&6Unloading a World, server could be laggy for some seconds",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_unloaded, "&aWorld unload terminated", null, null, true));
		newMessages.add(new Message(MessageId.world_worldHiddenTrue,
		                            "&aYou hid the world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldHiddenFalse,
		                            "&aYou stopped hidding the world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_changedWorldRequiredPermission,
		                            "&aYou changed the required permissions for the world %worldName% to &c%requiredPermission%",
		                            new String[] {"%worldName%", "%requiredPermission%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldNetherEnabled,
		                            "&aThe Nether dimension has been enabled for the world %worldName% (%worldName%_nether)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldNetherDisabled,
		                            "&aThe Nether dimension has been disabled for the world %worldName% (%worldName%_nether)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldNetherAlreadyEnabled,
		                            "&cThere is already a Nether dimension for the world %worldName% (%worldName%_nether)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldNetherAlreadyDisabled,
		                            "&cThere is no Nether dimension for the world %worldName% (%worldName%_nether)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldEndEnabled,
		                            "&aThe End dimension has been enabled for the world %worldName% (%worldName%_the_end)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldEndDisabled,
		                            "&aThe End dimension has been disabled for the world %worldName% (%worldName%_the_end)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldEndAlreadyEnabled,
		                            "&cThere is already a End dimension for the world %worldName% (%worldName%_the_end)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_worldEndAlreadyDisabled,
		                            "&cThere is no End dimension for the world %worldName% (%worldName%_the_end)",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_alreadyAllowedWorldWarp,
		                            "&cTeleportation already allowed in world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_allowedWorldWarp,
		                            "&aTeleportation allowed in world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_alreadyDisallowedWorldWarp,
		                            "&cTeleportation already disallowed in world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_disallowedWorldWarp,
		                            "&aTeleportation disallowed in world %worldName%",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));

		// Command - SPAWN
		newMessages.add(new Message(MessageId.world_teleportingToSpawn, "&aTeleportation to the spawn point...", null, null, true));
		newMessages.add(new Message(MessageId.world_settingSpawnPoint,
		                            "&aSpawn point of world %worldName% set",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));

		// Command - WARP
		newMessages.add(new Message(MessageId.world_availableWarps, "&aList of existing warps:", null, null, true));
		newMessages.add(new Message(MessageId.world_teleportedToWarp,
		                            "&aYou teleported yourself to the warp %warpName%",
		                            new String[] {"%warpName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_warpToThisWarpDisallowed,
		                            "&cYou are not allowed to use the warp %warpName%",
		                            new String[] {"%warpName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_unknownWarp, "&cUnknown warp: %warpName%", new String[] {"%warpName%"}, null, true));
		newMessages.add(new Message(MessageId.world_warpHiddenTrue,
		                            "&aYou hid the warp %warpName%",
		                            new String[] {"%warpName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_warpHiddenFalse,
		                            "&aYou stopped hidding the warp %warpName%",
		                            new String[] {"%warpName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_changedWarpRequiredPermission,
		                            "&aYou changed the required permission for the warp %warpName% to &c%requiredPermission%",
		                            new String[] {"%warpName%", "%requiredPermission%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_settingWarpPoint,
		                            "&aWarp point %warpName% set",
		                            new String[] {"%warpName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_warpRemoved,
		                            "&aWarp point %warpName% removed",
		                            new String[] {"%warpName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.world_teleportedBecauseOfWorldUnload,
		                            "&cUnloading the world you were in. Teleportation to " + "main world's spawn point.",
		                            null,
		                            null,
		                            true));
		return newMessages;
	}
}

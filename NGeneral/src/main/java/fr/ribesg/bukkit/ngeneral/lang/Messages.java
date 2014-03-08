/***************************************************************************
 * Project file:    NPlugins - NGeneral - Messages.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.lang.Messages                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Messages for NGeneral
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	public Messages() {
		super("General");
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.lang.AbstractMessages#createMessage()
	 */
	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", true, null));
		newMessages.add(new Message(MessageId.noPlayerFoundForGivenName, "&cNo online player found for input %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", true, null));
		newMessages.add(new Message(MessageId.missingWorldArg, "&cNon-player user should provide a world name", true, null));
		newMessages.add(new Message(MessageId.unknownWorld, "&cUnknown world '%world%'", true, new String[] {"%world%"}));

		// Welcome Message
		final List<String> variablesList = new ArrayList<>();
		variablesList.add("%bukkitVersion%");
		variablesList.add("%ip%");
		variablesList.add("%maxPlayers%");
		variablesList.add("%motd%");
		variablesList.add("%name%");
		variablesList.add("%onlineMode%");
		variablesList.add("%onlinePlayersCount%");
		variablesList.add("%port%");
		variablesList.add("%serverId%");
		variablesList.add("%serverName%");
		variablesList.add("%version%");
		variablesList.add("%viewDistance%");

		final StringBuilder textBuilder = new StringBuilder("&aWelcome to this Server!" + Messages.LINE_SEPARATOR);
		textBuilder.append("&7This example welcome message will show you all the available variables." + Messages.LINE_SEPARATOR);
		textBuilder.append("&7Your not forced to use all (or even any) of them!" + Messages.LINE_SEPARATOR);
		for (final String v : variablesList) {
			textBuilder.append("&a").append(v.substring(1, v.length() - 1)).append(": &c").append(v).append(Messages.LINE_SEPARATOR);
		}
		textBuilder.append("&aTell me if you have an idea of a fun variable!");

		newMessages.add(new Message(MessageId.general_welcome, textBuilder.toString(), false, variablesList.toArray(new String[variablesList.size()])));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true, null));
		newMessages.add(new Message(MessageId.cmdReloadError, "&An error occured while loading %file%!", true, new String[] {"%file%"}));

		// Command - GOD
		newMessages.add(new Message(MessageId.general_god_enabled, "&aGod Mode enabled", true, null));
		newMessages.add(new Message(MessageId.general_god_disabled, "&aGod Mode disabled", true, null));
		newMessages.add(new Message(MessageId.general_god_enabledFor, "&aGod Mode enabled for %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_god_disabledFor, "&aGod Mode disabled for %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_god_enabledBy, "&aGod Mode enabled by %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_god_disabledBy, "&aGod Mode disabled by %playerName%", true, new String[] {"%playerName%"}));

		// Command - FLY
		newMessages.add(new Message(MessageId.general_fly_enabled, "&aFly Mode enabled", true, null));
		newMessages.add(new Message(MessageId.general_fly_disabled, "&aFly Mode disabled", true, null));
		newMessages.add(new Message(MessageId.general_fly_enabledFor, "&aFly Mode enabled for %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_fly_disabledFor, "&aFly Mode disabled for %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_fly_enabledBy, "&aFly Mode enabled by %playerName%", true, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_fly_disabledBy, "&aFly Mode disabled by %playerName%", true, new String[] {"%playerName%"}));

		// Command - FLYSPEED
		newMessages.add(new Message(MessageId.general_flySpeed_set, "&aFly Speed set to %value%", true, new String[] {"%value%"}));
		newMessages.add(new Message(MessageId.general_flySpeed_reset, "&aFly Speed reset", true, null));
		newMessages.add(new Message(MessageId.general_flySpeed_setFor, "&aFly Speed set to %value% for %playerName%", true, new String[] {
				"%value%",
				"%playerName%"
		}));
		newMessages.add(new Message(MessageId.general_flySpeed_setBy, "&aFly Speed set to %value% by %playerName%", true, new String[] {
				"%value%",
				"%playerName%"
		}));

		// Command - WALKSPEED
		newMessages.add(new Message(MessageId.general_walkSpeed_set, "&aWalk Speed set to %value%", true, new String[] {"%value%"}));
		newMessages.add(new Message(MessageId.general_walkSpeed_reset, "&aWalk Speed reset", true, null));
		newMessages.add(new Message(MessageId.general_walkSpeed_setFor, "&aWalk Speed set to %value% for %playerName%", true, new String[] {
				"%value%",
				"%playerName%"
		}));
		newMessages.add(new Message(MessageId.general_walkSpeed_setBy, "&aWalk Speed set to %value% by %playerName%", true, new String[] {
				"%value%",
				"%playerName%"
		}));

		// Command - AFK
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcast, "&e%playerName% is now AFK", false, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcast, "&e%playerName% is no longer AFK", false, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcastReason, "&e%playerName% is now AFK (%reason%)", false, new String[] {
				"%playerName%",
				"%reason%"
		}));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcastReason, "&e%playerName% is no longer AFK (%reason%)", false, new String[] {
				"%playerName%",
				"%reason%"
		}));

		// Command - BUSY
		newMessages.add(new Message(MessageId.general_busy_nowBusyBroadcast, "&e%playerName% is now BUSY", false, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_busy_noLongerBusyBroadcast, "&e%playerName% is no longer BUSY", false, new String[] {"%playerName%"}));
		newMessages.add(new Message(MessageId.general_busy_nowBusyBroadcastReason, "&e%playerName% is now BUSY (%reason%)", false, new String[] {
				"%playerName%",
				"%reason%"
		}));
		newMessages.add(new Message(MessageId.general_busy_noLongerBusyBroadcastReason, "&e%playerName% is no longer BUSY (%reason%)", false, new String[] {
				"%playerName%",
				"%reason%"
		}));

		// Command - TIME
		newMessages.add(new Message(MessageId.general_timeSet, "&aTime set to %value% in world %world% by %changer%", true, new String[] {
				"%value%",
				"%world%",
				"%changer%"
		}));

		// Command - WEATHER
		newMessages.add(new Message(MessageId.general_weatherSet, "&aWeather changed to %value% in world %world% by %changer% for %duration% seconds", true, new String[] {
				"%value%",
				"%world%",
				"%changer%",
				"%duration%"
		}));

		// Command - REPAIR
		newMessages.add(new Message(MessageId.general_repair_cannot, "&cYou need to have a repairable item in hand", true, null));
		newMessages.add(new Message(MessageId.general_repair_done, "&aItem repaired!", true, null));

		// Command - NIGHT VISION
		newMessages.add(new Message(MessageId.general_nightvision_enabled, "&aNight Vision enabled", true, null));
		newMessages.add(new Message(MessageId.general_nightvision_disabled, "&aNight Vision disabled", true, null));

		// Commands - TP / TPHERE / TPTHERE / TPWORLD / TPBACK
		newMessages.add(new Message(MessageId.general_tp_youToTarget, "&aYou teleported yourself to %target%", true, new String[] {"%target%"}));
		newMessages.add(new Message(MessageId.general_tp_somebodyToTarget, "&a%teleporter% teleported you to %target%", true, new String[] {
				"%teleporter%",
				"%target%"
		}));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToTarget, "&aYou teleported %player% to %target%", true, new String[] {
				"%player%",
				"%target%"
		}));
		newMessages.add(new Message(MessageId.general_tp_somebodyToHim, "&a%teleporter% teleported you to him", true, new String[] {"%teleporter%"}));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToYou, "&aYou teleported %player% to you", true, new String[] {"%player%"}));
		newMessages.add(new Message(MessageId.general_tp_noTarget, "&cCould not find a correct location to teleport", true, null));
		newMessages.add(new Message(MessageId.general_tp_youToLocation, "&aYou teleported yourself to location %location%", true, new String[] {"%location%"}));
		newMessages.add(new Message(MessageId.general_tp_somebodyToLocation, "&a%teleporter% teleported you here", true, new String[] {"%teleporter%"}));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToLocation, "&aYou teleported %player% to location %location%", true, new String[] {
				"%player%",
				"%location%"
		}));
		newMessages.add(new Message(MessageId.general_tp_worldNotFound, "&cUnknown world '%worldName%'", true, new String[] {"%worldName%"}));
		newMessages.add(new Message(MessageId.general_tp_youToWorld, "&aYou teleported yourself to %worldName%", true, new String[] {"%worldName%"}));
		newMessages.add(new Message(MessageId.general_tp_somebodyToWorld, "&a%teleporter% teleported you to %worldName%", true, new String[] {
				"%teleporter%",
				"%worldName%"
		}));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToWorld, "&aYou teleported %player% to %worldName%", true, new String[] {
				"%player%",
				"%worldName%"
		}));
		newMessages.add(new Message(MessageId.general_tp_youNoKnownBack, "&cCould not find a location to go back to", true, null));
		newMessages.add(new Message(MessageId.general_tp_youBackWorldUnloaded, "&cThe back location is in an unloaded world, '%worldName%'", true, new String[] {"%worldName%"}));
		newMessages.add(new Message(MessageId.general_tp_youTeleportedBack, "&aYou teleported yourself back to the location you were before", true, null));
		newMessages.add(new Message(MessageId.general_tp_somebodyNoKnownBack, "&cCould not find a location to teleport %player% back to", true, new String[] {"%player%"}));
		newMessages.add(new Message(MessageId.general_tp_somebodyBackWorldUnloaded, "&cThe back location of %player% is in an unloaded world, '%worldName%'", true, new String[] {
				"%player%",
				"%worldName%"
		}));
		newMessages.add(new Message(MessageId.general_tp_somebodyTeleportedYouBack, "&a%teleporter% teleported you back to the location you were before", true, new String[] {"%teleporter%"}));
		newMessages.add(new Message(MessageId.general_tp_youTeleportedSomebodyBack, "&aYou teleported %player% back to the location he was before", true, new String[] {"%player%"}));

		// Feature - SIGN COLORS
		newMessages.add(new Message(MessageId.general_signcolors_permissionDenied, "&cYou do not have the permission to use colors on signs", true, null));

		// Feature - PROTECTION SIGNS
		newMessages.add(new Message(MessageId.general_protectionsign_accessDenied, "&cAccess denied by a Private sign.", true, null));
		newMessages.add(new Message(MessageId.general_protectionsign_breakDenied, "&cThis bloc is protected by a Private sign.", true, null));

		// Feature - NICKNAME FILTER
		newMessages.add(new Message(MessageId.general_nicknameFilter_invalid, "&cInvalid Nick: '%playerName%'", false, new String[] {"%playerName%"}));

		// Feature - ITEM NETWORK
		newMessages.add(new Message(MessageId.general_itemnetwork_youNeedToBeCreator, "&cYou need to be the creator of the Item Network to do that!", true, null));
		newMessages.add(new Message(MessageId.general_itemnetwork_alreadyExists, "&cThe Item Network '%networkName%' already exists!", true, new String[] {"%networkName%"}));
		newMessages.add(new Message(MessageId.general_itemnetwork_unknown, "&cUnkown Item Network '%networkName%'", true, new String[] {"%networkName%"}));
		newMessages.add(new Message(MessageId.general_itemnetwork_created, "&aThe Item Network '%networkName%' has been created!", true, new String[] {"%networkName%"}));
		newMessages.add(new Message(MessageId.general_itemnetwork_deleted, "&aThe Item Network '%networkName%' has been deleted!", true, new String[] {"%networkName%"}));

		return newMessages;
	}

}

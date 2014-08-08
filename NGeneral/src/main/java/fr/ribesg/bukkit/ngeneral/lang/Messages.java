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

	@Override
	protected Set<Message> createMessage() {
		final Set<Message> newMessages = new HashSet<>();

		// General deny response
		newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", true));
		newMessages.add(new Message(MessageId.noPlayerFoundForGivenName, "&cNo online player found for input %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", true));
		newMessages.add(new Message(MessageId.missingWorldArg, "&cNon-player user should provide a world name", true));
		newMessages.add(new Message(MessageId.unknownWorld, "&cUnknown world '%world%'", true, "%world%"));

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
		variablesList.add("%plugins%");
		variablesList.add("%pluginCount%");
		variablesList.add("%playerName%");
		variablesList.add("%playerIp%");
		variablesList.add("%playerGamemode%");
		variablesList.add("%playerWorld%");
		variablesList.add("%playerWorldType%");
		variablesList.add("%playerWorldDifficulty%");

		final StringBuilder textBuilder = new StringBuilder("&aWelcome to this Server!" + Messages.LINE_SEPARATOR);
		textBuilder.append("&7This example welcome message will show you all the available variables." + Messages.LINE_SEPARATOR);
		textBuilder.append("&7You're not forced to use all (or even any) of them!" + Messages.LINE_SEPARATOR);
		for (final String v : variablesList) {
			textBuilder.append("&a").append(v.substring(1, v.length() - 1)).append(": &c").append(v).append(Messages.LINE_SEPARATOR);
		}
		textBuilder.append("&aTell me if you have an idea of a fun variable!");

		newMessages.add(new Message(MessageId.general_welcome, textBuilder.toString(), false, variablesList.toArray(new String[variablesList.size()])));

		newMessages.add(new Message(MessageId.general_welcome_gameMode_survival, "Survival", false));
		newMessages.add(new Message(MessageId.general_welcome_gameMode_creative, "Creative", false));
		newMessages.add(new Message(MessageId.general_welcome_gameMode_adventure, "Adventure", false));
		newMessages.add(new Message(MessageId.general_welcome_worldType_normal, "Normal", false));
		newMessages.add(new Message(MessageId.general_welcome_worldType_nether, "Nether", false));
		newMessages.add(new Message(MessageId.general_welcome_worldType_end, "End", false));
		newMessages.add(new Message(MessageId.general_welcome_difficulty_peaceful, "Peaceful", false));
		newMessages.add(new Message(MessageId.general_welcome_difficulty_easy, "Easy", false));
		newMessages.add(new Message(MessageId.general_welcome_difficulty_normal, "Normal", false));
		newMessages.add(new Message(MessageId.general_welcome_difficulty_hard, "Hard", false));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded!", true));
		newMessages.add(new Message(MessageId.cmdReloadError, "&cAn error occured while loading %file%!", true, "%file%"));

		// Command - GOD
		newMessages.add(new Message(MessageId.general_god_enabled, "&aGod Mode enabled", true));
		newMessages.add(new Message(MessageId.general_god_disabled, "&aGod Mode disabled", true));
		newMessages.add(new Message(MessageId.general_god_enabledFor, "&aGod Mode enabled for %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.general_god_disabledFor, "&aGod Mode disabled for %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.general_god_enabledBy, "&aGod Mode enabled by %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.general_god_disabledBy, "&aGod Mode disabled by %playerName%", true, "%playerName%"));

		// Command - FLY
		newMessages.add(new Message(MessageId.general_fly_enabled, "&aFly Mode enabled", true));
		newMessages.add(new Message(MessageId.general_fly_disabled, "&aFly Mode disabled", true));
		newMessages.add(new Message(MessageId.general_fly_enabledFor, "&aFly Mode enabled for %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.general_fly_disabledFor, "&aFly Mode disabled for %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.general_fly_enabledBy, "&aFly Mode enabled by %playerName%", true, "%playerName%"));
		newMessages.add(new Message(MessageId.general_fly_disabledBy, "&aFly Mode disabled by %playerName%", true, "%playerName%"));

		// Command - FLYSPEED
		newMessages.add(new Message(MessageId.general_flySpeed_set, "&aFly Speed set to %value%", true, "%value%"));
		newMessages.add(new Message(MessageId.general_flySpeed_reset, "&aFly Speed reset", true));
		newMessages.add(new Message(MessageId.general_flySpeed_setFor, "&aFly Speed set to %value% for %playerName%", true, "%value%", "%playerName%"));
		newMessages.add(new Message(MessageId.general_flySpeed_setBy, "&aFly Speed set to %value% by %playerName%", true, "%value%", "%playerName%"));

		// Command - WALKSPEED
		newMessages.add(new Message(MessageId.general_walkSpeed_set, "&aWalk Speed set to %value%", true, "%value%"));
		newMessages.add(new Message(MessageId.general_walkSpeed_reset, "&aWalk Speed reset", true));
		newMessages.add(new Message(MessageId.general_walkSpeed_setFor, "&aWalk Speed set to %value% for %playerName%", true, "%value%", "%playerName%"));
		newMessages.add(new Message(MessageId.general_walkSpeed_setBy, "&aWalk Speed set to %value% by %playerName%", true, "%value%", "%playerName%"));

		// Command - AFK
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcast, "&e%playerName% is now AFK", false, "%playerName%"));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcast, "&e%playerName% is no longer AFK", false, "%playerName%"));
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcastReason, "&e%playerName% is now AFK (%reason%)", false, "%playerName%", "%reason%"));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcastReason, "&e%playerName% is no longer AFK (%reason%)", false, "%playerName%", "%reason%"));

		// Command - BUSY
		newMessages.add(new Message(MessageId.general_busy_nowBusyBroadcast, "&e%playerName% is now BUSY", false, "%playerName%"));
		newMessages.add(new Message(MessageId.general_busy_noLongerBusyBroadcast, "&e%playerName% is no longer BUSY", false, "%playerName%"));
		newMessages.add(new Message(MessageId.general_busy_nowBusyBroadcastReason, "&e%playerName% is now BUSY (%reason%)", false, "%playerName%", "%reason%"));
		newMessages.add(new Message(MessageId.general_busy_noLongerBusyBroadcastReason, "&e%playerName% is no longer BUSY (%reason%)", false, "%playerName%", "%reason%"));

		// Command - TIME
		newMessages.add(new Message(MessageId.general_timeSet, "&aTime set to %value% in world %world% by %changer%", true, "%value%", "%world%", "%changer%"));

		// Command - WEATHER
		newMessages.add(new Message(MessageId.general_weatherSet, "&aWeather changed to %value% in world %world% by %changer% for %duration% seconds", true, "%value%", "%world%", "%changer%", "%duration%"));

		// Command - REPAIR
		newMessages.add(new Message(MessageId.general_repair_cannot, "&cYou need to have a repairable item in hand", true));
		newMessages.add(new Message(MessageId.general_repair_done, "&aItem repaired!", true));

		// Command - NIGHT VISION
		newMessages.add(new Message(MessageId.general_nightvision_enabled, "&aNight Vision enabled", true));
		newMessages.add(new Message(MessageId.general_nightvision_disabled, "&aNight Vision disabled", true));

		// Commands - TP / TPHERE / TPTHERE / TPWORLD / TPBACK
		newMessages.add(new Message(MessageId.general_tp_youToTarget, "&aYou teleported yourself to %target%", true, "%target%"));
		newMessages.add(new Message(MessageId.general_tp_somebodyToTarget, "&a%teleporter% teleported you to %target%", true, "%teleporter%", "%target%"));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToTarget, "&aYou teleported %player% to %target%", true, "%player%", "%target%"));
		newMessages.add(new Message(MessageId.general_tp_somebodyToHim, "&a%teleporter% teleported you to him", true, "%teleporter%"));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToYou, "&aYou teleported %player% to you", true, "%player%"));
		newMessages.add(new Message(MessageId.general_tp_noTarget, "&cCould not find a correct location to teleport", true));
		newMessages.add(new Message(MessageId.general_tp_youToLocation, "&aYou teleported yourself to location %location%", true, "%location%"));
		newMessages.add(new Message(MessageId.general_tp_somebodyToLocation, "&a%teleporter% teleported you here", true, "%teleporter%"));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToLocation, "&aYou teleported %player% to location %location%", true, "%player%", "%location%"));
		newMessages.add(new Message(MessageId.general_tp_worldNotFound, "&cUnknown world '%worldName%'", true, "%worldName%"));
		newMessages.add(new Message(MessageId.general_tp_youToWorld, "&aYou teleported yourself to %worldName%", true, "%worldName%"));
		newMessages.add(new Message(MessageId.general_tp_somebodyToWorld, "&a%teleporter% teleported you to %worldName%", true, "%teleporter%", "%worldName%"));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToWorld, "&aYou teleported %player% to %worldName%", true, "%player%", "%worldName%"));
		newMessages.add(new Message(MessageId.general_tp_youNoKnownBack, "&cCould not find a location to go back to", true));
		newMessages.add(new Message(MessageId.general_tp_youBackWorldUnloaded, "&cThe back location is in an unloaded world, '%worldName%'", true, "%worldName%"));
		newMessages.add(new Message(MessageId.general_tp_youTeleportedBack, "&aYou teleported yourself back to the location you were before", true));
		newMessages.add(new Message(MessageId.general_tp_somebodyNoKnownBack, "&cCould not find a location to teleport %player% back to", true, "%player%"));
		newMessages.add(new Message(MessageId.general_tp_somebodyBackWorldUnloaded, "&cThe back location of %player% is in an unloaded world, '%worldName%'", true, "%player%", "%worldName%"));
		newMessages.add(new Message(MessageId.general_tp_somebodyTeleportedYouBack, "&a%teleporter% teleported you back to the location you were before", true, "%teleporter%"));
		newMessages.add(new Message(MessageId.general_tp_youTeleportedSomebodyBack, "&aYou teleported %player% back to the location he was before", true, "%player%"));

		// Commands - HEAL / FEED / HEALTH / FOOD
		newMessages.add(new Message(MessageId.general_heal_autoHeal, "&aYou healed youself", true));
		newMessages.add(new Message(MessageId.general_heal_healedBy, "&aYou have been healed by %name%", true, "%name%"));
		newMessages.add(new Message(MessageId.general_heal_healed, "&aYou healed %name%", true, "%name%"));
		newMessages.add(new Message(MessageId.general_feed_autoFeed, "&aYou fed youself", true));
		newMessages.add(new Message(MessageId.general_feed_fedBy, "&aYou have been fed by %name%", true, "%name%"));
		newMessages.add(new Message(MessageId.general_feed_fed, "&aYou fed %name%", true, "%name%"));
		newMessages.add(new Message(MessageId.general_health_autoSet, "&aYou set your health to %value%", true, "%value%"));
		newMessages.add(new Message(MessageId.general_health_setBy, "&a%name% set your health to %value%", true, "%name%", "%value%"));
		newMessages.add(new Message(MessageId.general_health_set, "&aYou set %name%'s health to %value%", true, "%name%", "%value%"));
		newMessages.add(new Message(MessageId.general_food_autoSet, "&aYou set your food level to %value%", true, "%value%"));
		newMessages.add(new Message(MessageId.general_food_setBy, "&a%name% set your food level to %value%", true, "%name%", "%value%"));
		newMessages.add(new Message(MessageId.general_food_set, "&aYou set %name%'s food level to %value%", true, "%name%", "%value%"));

		// Command - SPY
		newMessages.add(new Message(MessageId.general_spy_disabled, "&aSpy mode disabled", true));
		newMessages.add(new Message(MessageId.general_spy_enabled, "&aSpy mode enabled", true));
		newMessages.add(new Message(MessageId.general_spy_enabledPlayer, "&aSpy mode enabled, spying %playerName%", true, "%playerName%"));

		// Feature - SIGN COLORS
		newMessages.add(new Message(MessageId.general_signcolors_permissionDenied, "&cYou do not have the permission to use colors on signs", true));

		// Feature - PROTECTION SIGNS
		newMessages.add(new Message(MessageId.general_protectionsign_accessDenied, "&cAccess denied by a Private sign.", true));
		newMessages.add(new Message(MessageId.general_protectionsign_breakDenied, "&cThis bloc is protected by a Private sign.", true));

		// Feature - NICKNAME FILTER
		newMessages.add(new Message(MessageId.general_nicknameFilter_invalid, "&cInvalid Nick: '%playerName%'", false, "%playerName%"));

		// Feature - ITEM NETWORK
		newMessages.add(new Message(MessageId.general_itemnetwork_youNeedToBeCreator, "&cYou need to be the creator of the Item Network to do that!", true));
		newMessages.add(new Message(MessageId.general_itemnetwork_alreadyExists, "&cThe Item Network '%networkName%' already exists!", true, "%networkName%"));
		newMessages.add(new Message(MessageId.general_itemnetwork_unknown, "&cUnkown Item Network '%networkName%'", true, "%networkName%"));
		newMessages.add(new Message(MessageId.general_itemnetwork_created, "&aThe Item Network '%networkName%' has been created!", true, "%networkName%"));
		newMessages.add(new Message(MessageId.general_itemnetwork_deleted, "&aThe Item Network '%networkName%' has been deleted!", true, "%networkName%"));

		return newMessages;
	}
}

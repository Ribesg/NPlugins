package fr.ribesg.bukkit.ngeneral.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Messages for NTalk
 *
 * @author Ribesg
 */
public class Messages extends AbstractMessages {

	/**
	 */
	public Messages() {
		super("General");
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
		newMessages.add(new Message(MessageId.missingWorldArg, "&cNon-player user should provide a world name", null, null, true));
		newMessages.add(new Message(MessageId.unknownWorld, "&cUnknown world '%world%'", new String[] {"%world%"}, null, true));

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

		newMessages.add(new Message(MessageId.general_welcome,
		                            textBuilder.toString(),
		                            variablesList.toArray(new String[variablesList.size()]),
		                            null,
		                            false));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null, true));

		// Command - GOD
		newMessages.add(new Message(MessageId.general_god_enabled, "&aGod Mode enabled", null, null, true));
		newMessages.add(new Message(MessageId.general_god_disabled, "&aGod Mode disabled", null, null, true));
		newMessages.add(new Message(MessageId.general_god_enabledFor,
		                            "&aGod Mode enabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_god_disabledFor,
		                            "&aGod Mode disabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_god_enabledBy,
		                            "&aGod Mode enabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_god_disabledBy,
		                            "&aGod Mode disabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));

		// Command - FLY
		newMessages.add(new Message(MessageId.general_fly_enabled, "&aFly Mode enabled", null, null, true));
		newMessages.add(new Message(MessageId.general_fly_disabled, "&aFly Mode disabled", null, null, true));
		newMessages.add(new Message(MessageId.general_fly_enabledFor,
		                            "&aFly Mode enabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_fly_disabledFor,
		                            "&aFly Mode disabled for %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_fly_enabledBy,
		                            "&aFly Mode enabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_fly_disabledBy,
		                            "&aFly Mode disabled by %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));

		// Command - FLYSPEED
		newMessages.add(new Message(MessageId.general_flySpeed_set, "&aFly Speed set to %value%", new String[] {"%value%"}, null, true));
		newMessages.add(new Message(MessageId.general_flySpeed_reset, "&aFly Speed reset", null, null, true));
		newMessages.add(new Message(MessageId.general_flySpeed_setFor,
		                            "&aFly Speed set to %value% for %playerName%",
		                            new String[] {"%value%", "%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_flySpeed_setBy,
		                            "&aFly Speed set to %value% by %playerName%",
		                            new String[] {"%value%", "%playerName%"},
		                            null,
		                            true));

		// Command - WALKSPEED
		newMessages.add(new Message(MessageId.general_walkSpeed_set, "&aWalk Speed set to %value%", new String[] {"%value%"}, null, true));
		newMessages.add(new Message(MessageId.general_walkSpeed_reset, "&aWalk Speed reset", null, null, true));
		newMessages.add(new Message(MessageId.general_walkSpeed_setFor,
		                            "&aWalk Speed set to %value% for %playerName%",
		                            new String[] {"%value%", "%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_walkSpeed_setBy,
		                            "&aWalk Speed set to %value% by %playerName%",
		                            new String[] {"%value%", "%playerName%"},
		                            null,
		                            true));

		// Command - AFK
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcast,
		                            "&e%playerName% is now AFK",
		                            new String[] {"%playerName%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcast,
		                            "&e%playerName% is no longer AFK",
		                            new String[] {"%playerName%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.general_afk_nowAfkBroadcastReason,
		                            "&e%playerName% is now AFK (%reason%)",
		                            new String[] {"%playerName%", "%reason%"},
		                            null,
		                            false));
		newMessages.add(new Message(MessageId.general_afk_noLongerAfkBroadcastReason,
		                            "&e%playerName% is no longer AFK (%reason%)",
		                            new String[] {"%playerName%", "%reason%"},
		                            null,
		                            false));

		// Command - TIME
		newMessages.add(new Message(MessageId.general_timeSet,
		                            "&aTime set to %value% in world %world% by %changer%",
		                            new String[] {"%value%", "%world%", "%changer%"},
		                            null,
		                            true));

		// Command - WEATHER
		newMessages.add(new Message(MessageId.general_weatherSet,
		                            "&aWeather changed to %value% in world %world% by %changer% for %duration% seconds",
		                            new String[] {"%value%", "%world%", "%changer%", "%duration%"},
		                            null,
		                            true));

		// Commands - TP / TPHERE / TPTHERE / TPBACK
		newMessages.add(new Message(MessageId.general_tp_youToTarget,
		                            "&aYou teleported yourself to %target%",
		                            new String[] {"%target%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_somebodyToTarget,
		                            "&a%teleporter% teleported you to %target%",
		                            new String[] {"%teleporter%", "%target%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToTarget,
		                            "&aYou teleported %player% to %target%",
		                            new String[] {"%player%", "%target%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_somebodyToHim,
		                            "&a%teleporter% teleported you to him",
		                            new String[] {"%teleporter%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToYou,
		                            "&aYou teleported %player% to you",
		                            new String[] {"%player%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_noTarget, "&cCould not find a correct location to teleport", null, null, true));
		newMessages.add(new Message(MessageId.general_tp_youToLocation,
		                            "&aYou teleported yourself where you were looking at",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_somebodyToLocation,
		                            "&a%teleporter% teleported you here",
		                            new String[] {"%teleporter%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_youSomebodyToLocation,
		                            "&aYou teleported %player% where you were looking at",
		                            new String[] {"%player%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_youNoKnownBack, "&cCould not find a location to go back to", null, null, true));
		newMessages.add(new Message(MessageId.general_tp_youBackWorldUnloaded,
		                            "&cThe back location is in an unloaded world, '%worldName%'",
		                            new String[] {"%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_youTeleportedBack,
		                            "&aYou teleported yourself back to the location you were before",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_somebodyNoKnownBack,
		                            "&cCould not find a location to teleport %player% back to",
		                            new String[] {"%player%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_somebodyBackWorldUnloaded,
		                            "&cThe back location of %player% is in an unloaded world, '%worldName%'",
		                            new String[] {"%player%", "%worldName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_somebodyTeleportedYouBack,
		                            "&a%teleporter% teleported you back to the location you were before",
		                            new String[] {"%teleporter%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_tp_youTeleportedSomebodyBack,
		                            "&aYou teleported %player% back to the location he was before",
		                            new String[] {"%player%"},
		                            null,
		                            true));

		// Feature - SIGN COLORS
		newMessages.add(new Message(MessageId.general_signcolors_permissionDenied,
		                            "&cYou do not have the permission to use colors on signs",
		                            null,
		                            null,
		                            true));

		// Feature - PROTECTION SIGNS
		newMessages.add(new Message(MessageId.general_protectionsign_accessDenied, "&cAccess denied by a Private sign.", null, null,
		                            true));
		newMessages.add(new Message(MessageId.general_protectionsign_breakDenied,
		                            "&cThis bloc is protected by a Private sign.",
		                            null,
		                            null,
		                            true));

		// Feature - NICKNAME FILTER
		newMessages.add(new Message(MessageId.general_nicknameFilter_invalid,
		                            "&cInvalid Nick: '%playerName%'",
		                            new String[] {"%playerName%"},
		                            null,
		                            false));

		// Feature - ITEM NETWORK
		newMessages.add(new Message(MessageId.general_itemnetwork_youNeedToBeCreator,
		                            "&cYou need to be the creator of the Item Network to do that!",
		                            null,
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_itemnetwork_alreadyExists,
		                            "&cThe Item Network '%networkName%' already exists!",
		                            new String[] {"%networkName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_itemnetwork_unknown,
		                            "&cUnkown Item Network '%networkName%'",
		                            new String[] {"%networkName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_itemnetwork_created,
		                            "&aThe Item Network '%networkName%' has been created!",
		                            new String[] {"%networkName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.general_itemnetwork_deleted,
		                            "&aThe Item Network '%networkName%' has been deleted!",
		                            new String[] {"%networkName%"},
		                            null,
		                            true));

		return newMessages;
	}

}

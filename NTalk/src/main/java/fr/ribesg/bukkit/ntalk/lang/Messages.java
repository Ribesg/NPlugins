/***************************************************************************
 * Project file:    NPlugins - NTalk - Messages.java                       *
 * Full Class name: fr.ribesg.bukkit.ntalk.lang.Messages                   *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.lang;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.Message;
import fr.ribesg.bukkit.ncore.lang.MessageId;

import java.util.HashSet;
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
		super("Talk");
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
		newMessages.add(new Message(MessageId.talk_nobodyToRespond, "&cYou have nobody to respond to", null, null, true));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null, true));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null, true));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null, true));

		// Command - NICK
		newMessages.add(new Message(MessageId.talk_youNickNamed,
		                            "&aYou renamed %realName% %nickName%",
		                            new String[] {"%realName%", "%nickName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.talk_youDeNickNamed,
		                            "&aYou reseted the name of %realName%",
		                            new String[] {"%realName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.talk_youWereNickNamed,
		                            "&aYou were renamed %nickName% by %playerName%",
		                            new String[] {"%nickName%", "%playerName%"},
		                            null,
		                            true));
		newMessages.add(new Message(MessageId.talk_youWereDeNickNamed,
		                            "&aYour name was reseted by %playerName%",
		                            new String[] {"%playerName%"},
		                            null,
		                            true));

		// Chat Filter reasons
		newMessages.add(new Message(MessageId.talk_filterMutedReason, "Use of word '%word%'", new String[] {"%word%"}, null, false));
		newMessages.add(new Message(MessageId.talk_filterMutedReason, "Use of word '%word%'", new String[] {"%word%"}, null, false));
		newMessages.add(new Message(MessageId.talk_filterMutedReason, "Use of word '%word%'", new String[] {"%word%"}, null, false));

		return newMessages;
	}
}

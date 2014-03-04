/***************************************************************************
 * Project file:    NPlugins - NTalk - Messages.java                       *
 * Full Class name: fr.ribesg.bukkit.ntalk.lang.Messages                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
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

	public Messages() {
		super("Talk");
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
		newMessages.add(new Message(MessageId.talk_nobodyToRespond, "&cYou have nobody to respond to", true, null));
		newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", true, null));

		// Command - RELOAD
		newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", true, null));
		newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", true, null));

		// Command - NICK
		newMessages.add(new Message(MessageId.talk_youNickNamed, "&aYou renamed %realName% %nickName%", true, new String[] {
				"%realName%",
				"%nickName%"
		}));
		newMessages.add(new Message(MessageId.talk_youDeNickNamed, "&aYou reseted the name of %realName%", true, new String[] {"%realName%"}));
		newMessages.add(new Message(MessageId.talk_youWereNickNamed, "&aYou were renamed %nickName% by %playerName%", true, new String[] {
				"%nickName%",
				"%playerName%"
		}));
		newMessages.add(new Message(MessageId.talk_youWereDeNickNamed, "&aYour name was reseted by %playerName%", true, new String[] {"%playerName%"}));

		// Chat Filter reasons
		newMessages.add(new Message(MessageId.talk_filterMutedReason, "Use of word '%word%'", false, new String[] {"%word%"}));
		newMessages.add(new Message(MessageId.talk_filterBannedReason, "Use of word '%word%'", false, new String[] {"%word%"}));
		newMessages.add(new Message(MessageId.talk_filterJailedReason, "Use of word '%word%'", false, new String[] {"%word%"}));

		return newMessages;
	}
}

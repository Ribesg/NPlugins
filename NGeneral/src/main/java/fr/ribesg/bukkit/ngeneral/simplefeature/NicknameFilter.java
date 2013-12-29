/***************************************************************************
 * Project file:    NPlugins - NGeneral - NicknameFilter.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.NicknameFilter *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.regex.Pattern;

public class NicknameFilter implements Listener {

	private final NGeneral plugin;
	private final Pattern  pattern;

	public NicknameFilter(final NGeneral plugin) {
		this.plugin = plugin;
		this.pattern = Pattern.compile("^[\\w]{3,16}$");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			final String playerName = event.getPlayer().getName();
			if (!pattern.matcher(playerName).matches()) {
				event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
				event.setKickMessage(plugin.getMessages().get(MessageId.general_nicknameFilter_invalid, playerName)[0]);
			}
		}
	}
}

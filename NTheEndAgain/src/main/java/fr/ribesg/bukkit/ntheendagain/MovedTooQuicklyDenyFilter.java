/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - MovedTooQuicklyDenyFilter.java
 * Full Class name: fr.ribesg.bukkit.ntheendagain.MovedTooQuicklyDenyFilter*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.common.logging.DenyFilter;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MovedTooQuicklyDenyFilter implements DenyFilter {

	private static final String MOVED_TOO_QUICKLY = "moved too quickly!";

	private final NTheEndAgain plugin;

	public MovedTooQuicklyDenyFilter(final NTheEndAgain instance) {
		plugin = instance;
	}

	@Override
	public boolean denies(final String message) {
		if (message != null && message.contains(MOVED_TOO_QUICKLY)) {
			final String beforeMovedTooQuickly = message.substring(0, message.indexOf(MOVED_TOO_QUICKLY));
			final String[] beforeMovedTooQuicklySplit = beforeMovedTooQuickly.split(" ");
			final String playerName = beforeMovedTooQuicklySplit[beforeMovedTooQuicklySplit.length - 1];
			final Player player = Bukkit.getPlayerExact(playerName);
			final EndWorldHandler handler = plugin.getHandler(StringUtil.toLowerCamelCase(player.getWorld().getName()));
			return handler != null && handler.getConfig().getFilterMovedTooQuicklySpam() == 1;
		} else {
			return false;
		}
	}
}

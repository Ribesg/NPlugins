/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - MovedTooQuicklyFilter.java   *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.MovedTooQuicklyFilter    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import org.bukkit.entity.Player;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MovedTooQuicklyFilter implements Filter {

	private final NTheEndAgain plugin;

	public MovedTooQuicklyFilter(final NTheEndAgain instance) {
		plugin = instance;
	}

	@Override
	public boolean isLoggable(final LogRecord record) {
		if (record.getLevel() == Level.WARNING) {
			final String message = record.getMessage();
			final String[] split = message.split(" ");
			final Player p = plugin.getServer().getPlayerExact(split[0]);
			if (p != null) {
				if ("moved".equals(split[1]) && "too".equals(split[2]) && "quickly!".equals(split[3])) {
					final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(p.getWorld().getName()));
					return handler == null || handler.getConfig().getFilterMovedTooQuicklySpam() == 0;
				}
			}
		}
		return true;
	}
}

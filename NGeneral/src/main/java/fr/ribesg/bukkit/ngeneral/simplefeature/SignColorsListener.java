/***************************************************************************
 * Project file:    NPlugins - NGeneral - SignColorsListener.java          *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.SignColorsListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.ColorUtil;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignColorsListener implements Listener {

	private static String colorsRegex;

	private static String getColorsRegex() {
		if (colorsRegex == null) {
			final StringBuilder s = new StringBuilder(3 + ChatColor.values().length);
			s.append("^.*");
			s.append(ColorUtil.ALTERNATE_COLOR_CHAR_STRING);
			s.append('[');
			for (final ChatColor c : ChatColor.values()) {
				s.append(c.getChar());
			}
			s.append("].*$");
			colorsRegex = s.toString();
		}
		return colorsRegex;
	}

	private final NGeneral plugin;

	public SignColorsListener(final NGeneral instance) {
		this.plugin = instance;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(final SignChangeEvent event) {
		boolean containsColors = false;
		for (final String line : event.getLines()) {
			if (line.matches(getColorsRegex())) {
				containsColors = true;
				break;
			}
		}
		if (containsColors) {
			if (Perms.hasSignColors(event.getPlayer())) {
				for (int i = 0; i < 4; i++) {
					event.setLine(i, ColorUtil.colorize(event.getLine(i)));
				}
			} else {
				plugin.sendMessage(event.getPlayer(), MessageId.general_signcolors_permissionDenied);
			}
		}

	}
}

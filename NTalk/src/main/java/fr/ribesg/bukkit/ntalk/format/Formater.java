/***************************************************************************
 * Project file:    NPlugins - NTalk - Formater.java                       *
 * Full Class name: fr.ribesg.bukkit.ntalk.format.Formater                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.format;

import fr.ribesg.bukkit.ncore.utils.AsyncPermAccessor;
import fr.ribesg.bukkit.ntalk.Config;
import fr.ribesg.bukkit.ntalk.NTalk;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Formater {

	private static final String BUKKIT_PLAYERNAME = "%1$s";
	private static final String BUKKIT_MESSAGE    = "%2$s";

	private final Config cfg;

	public Formater(final NTalk instance) {
		cfg = instance.getPluginConfig();
	}

	public String getFormat(final Player player, final boolean async) {
		Format format = cfg.getDefaultFormat();
		if (cfg.getPlayerFormats().containsKey(player.getName())) {
			format = cfg.getPlayerFormats().get(player.getName());
		} else if (async ? AsyncPermAccessor.isOp(player.getName()) : player.isOp()) {
			if (cfg.getGroupFormats().containsKey(cfg.getOpGroup())) {
				format = cfg.getGroupFormats().get(cfg.getOpGroup());
			} else {
				format = cfg.getDefaultFormat();
			}
		} else {
			for (final String groupName : cfg.getGroupFormats().keySet()) {
				if (async ? AsyncPermAccessor.has(player.getName(), "group." + groupName.toLowerCase()) : player.hasPermission("group." + groupName.toLowerCase())) {
					format = cfg.getGroupFormats().get(groupName);
					break;
				}
			}
		}
		String prefixedString = cfg.getTemplate();
		final String playerName = cfg.getPlayerNicknames().containsKey(player.getName()) ? cfg.getPlayerNicknames().get(player.getName()) : BUKKIT_PLAYERNAME;
		prefixedString = prefixedString.replaceAll("\\Q[prefix]\\E", format.getPrefix());
		prefixedString = prefixedString.replace("[name]", playerName);
		prefixedString = prefixedString.replaceAll("\\Q[suffix]\\E", format.getSuffix());
		prefixedString = prefixedString.replace("[message]", BUKKIT_MESSAGE);
		return ChatColor.translateAlternateColorCodes('&', unicoder(prefixedString));
	}

	public String parsePM(final CommandSender from, final CommandSender to, final String message) {
		Format formatFrom = cfg.getDefaultFormat(), formatTo = cfg.getDefaultFormat();
		String prefixedString = cfg.getPmTemplate(); // The final String
		if (!(from instanceof Player)) {
			prefixedString = prefixedString.replaceAll("\\Q[prefixFrom]\\E", "");
			prefixedString = prefixedString.replace("[nameFrom]", "&cCONSOLE");
			prefixedString = prefixedString.replaceAll("\\Q[suffixFrom]\\E", "");
		} else {
			if (cfg.getPlayerFormats().containsKey(from.getName())) {
				formatFrom = cfg.getPlayerFormats().get(from.getName());
			} else if (from.isOp()) {
				if (cfg.getGroupFormats().containsKey(cfg.getOpGroup())) {
					formatFrom = cfg.getGroupFormats().get(cfg.getOpGroup());
				} else {
					formatFrom = cfg.getDefaultFormat();
				}
			} else {
				for (final String groupName : cfg.getGroupFormats().keySet()) {
					if (from.hasPermission("group." + groupName)) {
						formatFrom = cfg.getGroupFormats().get(groupName);
						break;
					}
				}
			}
			final String fromName = cfg.getPlayerNicknames().containsKey(from.getName()) ? cfg.getPlayerNicknames().get(from.getName()) : from.getName();
			prefixedString = prefixedString.replaceAll("\\Q[prefixFrom]\\E", formatFrom.getPrefix());
			prefixedString = prefixedString.replace("[nameFrom]", fromName);
			prefixedString = prefixedString.replaceAll("\\Q[suffixFrom]\\E", formatFrom.getSuffix());
		}
		if (!(to instanceof Player)) {
			prefixedString = prefixedString.replaceAll("\\Q[prefixTo]\\E", "");
			prefixedString = prefixedString.replace("[nameTo]", "&cCONSOLE");
			prefixedString = prefixedString.replaceAll("\\Q[suffixTo]\\E", "");
		} else {
			if (cfg.getPlayerFormats().containsKey(to.getName())) {
				formatTo = cfg.getPlayerFormats().get(to.getName());
			} else if (to.isOp()) {
				if (cfg.getGroupFormats().containsKey(cfg.getOpGroup())) {
					formatTo = cfg.getGroupFormats().get(cfg.getOpGroup());
				} else {
					formatTo = cfg.getDefaultFormat();
				}
			} else {
				for (final String groupName : cfg.getGroupFormats().keySet()) {
					if (to.hasPermission("group." + groupName)) {
						formatTo = cfg.getGroupFormats().get(groupName);
						break;
					}
				}
			}
			final String toName = cfg.getPlayerNicknames().containsKey(to.getName()) ? cfg.getPlayerNicknames().get(to.getName()) : to.getName();
			prefixedString = prefixedString.replaceAll("\\Q[prefixTo]\\E", formatTo.getPrefix());
			prefixedString = prefixedString.replace("[nameTo]", toName);
			prefixedString = prefixedString.replaceAll("\\Q[suffixTo]\\E", formatTo.getSuffix());
		}

		prefixedString = prefixedString.replace("[message]", message);

		return ChatColor.translateAlternateColorCodes('&', unicoder(prefixedString));
	}

	/** Replace {{unicode}} by the actual char with unicode representation "unicode" */
	private String unicoder(String s) {
		if (!s.contains("{{") || !s.contains("}}")) {
			return s;
		} else {
			String id = "";
			while (s.contains("{{") && s.substring(s.indexOf('{')).contains("}}")) {
				try {
					id = s.substring(s.indexOf('{') + 2, s.indexOf('}'));
					final int val = Integer.parseInt(id, 16);
					s = s.replace("{{" + id + "}}", new String(Character.toChars(val)));
				} catch (final Exception e) {
					s = s.replace("{{" + id + "}}", "");
				}
			}
			return s;
		}
	}
}

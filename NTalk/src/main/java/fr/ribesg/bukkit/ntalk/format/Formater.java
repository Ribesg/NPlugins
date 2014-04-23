/***************************************************************************
 * Project file:    NPlugins - NTalk - Formater.java                       *
 * Full Class name: fr.ribesg.bukkit.ntalk.format.Formater                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.format;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;
import fr.ribesg.bukkit.ncore.util.ColorUtil;
import fr.ribesg.bukkit.ntalk.Config;
import fr.ribesg.bukkit.ntalk.NTalk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Formater {

	private static final String BUKKIT_PLAYERNAME = "%1$s";
	private static final String BUKKIT_MESSAGE    = "%2$s";

	private final Config cfg;

	public Formater(final NTalk instance) {
		cfg = instance.getPluginConfig();
	}

	/**
	 * Gets 2 Strings: one containing the format, the other one containing
	 * the format minus the realName part.
	 *
	 * @param player the Player for which we need to get the format
	 * @param async  if this called is made in a different thread than the
	 *               main thread
	 *
	 * @return 2 Strings: one containing the format, the other one containing
	 * the format minus the realName part
	 */
	public String[] getFormat(final Player player, final boolean async) {
		// Get format for this player
		Format format = cfg.getDefaultFormat();
		if (cfg.getPlayerFormats().containsKey(player.getUniqueId())) {
			format = cfg.getPlayerFormats().get(player.getUniqueId());
		} else if (async ? AsyncPermAccessor.isOp(player.getName()) : player.isOp()) {
			if (cfg.getGroupFormats().containsKey(cfg.getOpGroup())) {
				format = cfg.getGroupFormats().get(cfg.getOpGroup());
			} else {
				format = cfg.getDefaultFormat();
			}
		} else {
			for (final String groupName : cfg.getGroupFormats().keySet()) {
				if (async ? AsyncPermAccessor.has(player.getName(), "maingroup." + groupName.toLowerCase()) : player.hasPermission("maingroup." + groupName.toLowerCase())) {
					format = cfg.getGroupFormats().get(groupName);
					break;
				}
			}
		}

		String formatString = cfg.getTemplate();
		final String playerNickname = cfg.getPlayerNicknames().get(player.getUniqueId());
		formatString = formatString.replaceAll("\\Q[prefix]\\E", format.getPrefix());
		formatString = formatString.replaceAll("\\Q[suffix]\\E", format.getSuffix());
		formatString = formatString.replace("[message]", BUKKIT_MESSAGE);
		if (playerNickname == null) {
			formatString = formatString.replace("[name]", BUKKIT_PLAYERNAME);
			formatString = formatString.replaceAll("%%(.*)%%", "");
		} else {
			formatString = formatString.replace("[name]", playerNickname);
			formatString = formatString.replaceAll("%%(.*)%%", "$1");
			formatString = formatString.replace("[realName]", BUKKIT_PLAYERNAME);
		}

		final String[] result = new String[2];
		result[0] = unicoder(formatString);
		result[1] = unicoder(formatString.replaceAll("%%(.*)%%", ""));

		return ColorUtil.colorize(result);
	}

	/**
	 * Gets 2 Strings: one containing the formatted String, the other one
	 * containing the formatted String minus the realName parts.
	 *
	 * @param from    the source Player for which we need to get the format
	 * @param to      the destination Player for which we need to get the format
	 * @param message the message
	 *
	 * @return 2 Strings: one containing the formatted String, the other one
	 * containing the formatted String minus the realName parts
	 */
	public String[] parsePM(final CommandSender from, final CommandSender to, final String message) {
		Format formatFrom = cfg.getDefaultFormat(), formatTo = cfg.getDefaultFormat();
		String formatString = cfg.getPmTemplate(); // The final String
		if (!(from instanceof Player)) {
			formatString = formatString.replaceAll("\\Q[prefixFrom]\\E", "");
			formatString = formatString.replace("[nameFrom]", "&cCONSOLE");
			formatString = formatString.replaceAll("\\Q[suffixFrom]\\E", "");
			formatString = formatString.replaceAll("%1%(.*)%%", "");
		} else {
			if (cfg.getPlayerFormats().containsKey(((Player) from).getUniqueId())) {
				formatFrom = cfg.getPlayerFormats().get(((Player)from).getUniqueId());
			} else if (from.isOp()) {
				if (cfg.getGroupFormats().containsKey(cfg.getOpGroup())) {
					formatFrom = cfg.getGroupFormats().get(cfg.getOpGroup());
				} else {
					formatFrom = cfg.getDefaultFormat();
				}
			} else {
				for (final String groupName : cfg.getGroupFormats().keySet()) {
					if (from.hasPermission("maingroup." + groupName)) {
						formatFrom = cfg.getGroupFormats().get(groupName);
						break;
					}
				}
			}
			final String fromName = from.getName();
			final String fromNickname = cfg.getPlayerNicknames().get(((Player)from).getUniqueId());
			formatString = formatString.replaceAll("\\Q[prefixFrom]\\E", formatFrom.getPrefix());
			formatString = formatString.replaceAll("\\Q[suffixFrom]\\E", formatFrom.getSuffix());
			if (fromNickname == null) {
				formatString = formatString.replace("[nameFrom]", fromName);
				formatString = formatString.replaceAll("%1%(.*)%%", "");
			} else {
				formatString = formatString.replace("[nameFrom]", fromNickname);
				formatString = formatString.replaceAll("%1%(.*)%%", "$1");
				formatString = formatString.replace("[realNameFrom]", fromName);
			}
		}
		if (!(to instanceof Player)) {
			formatString = formatString.replaceAll("\\Q[prefixTo]\\E", "");
			formatString = formatString.replace("[nameTo]", "&cCONSOLE");
			formatString = formatString.replaceAll("\\Q[suffixTo]\\E", "");
			formatString = formatString.replaceAll("%2%(.*)%%", "");
		} else {
			if (cfg.getPlayerFormats().containsKey(((Player)to).getUniqueId())) {
				formatTo = cfg.getPlayerFormats().get(((Player)to).getUniqueId());
			} else if (to.isOp()) {
				if (cfg.getGroupFormats().containsKey(cfg.getOpGroup())) {
					formatTo = cfg.getGroupFormats().get(cfg.getOpGroup());
				} else {
					formatTo = cfg.getDefaultFormat();
				}
			} else {
				for (final String groupName : cfg.getGroupFormats().keySet()) {
					if (to.hasPermission("maingroup." + groupName)) {
						formatTo = cfg.getGroupFormats().get(groupName);
						break;
					}
				}
			}
			final String toName = to.getName();
			final String toNickname = cfg.getPlayerNicknames().get(((Player)to).getUniqueId());
			formatString = formatString.replaceAll("\\Q[prefixTo]\\E", formatTo.getPrefix());
			formatString = formatString.replaceAll("\\Q[suffixTo]\\E", formatTo.getSuffix());
			if (toNickname == null) {
				formatString = formatString.replace("[nameTo]", toName);
				formatString = formatString.replaceAll("%2%(.*)%%", "");
			} else {
				formatString = formatString.replace("[nameTo]", toNickname);
				formatString = formatString.replaceAll("%2%(.*)%%", "$1");
				formatString = formatString.replace("[realNameTo]", toName);
			}
		}

		formatString = formatString.replace("[message]", message);

		final String[] result = new String[2];
		result[0] = unicoder(formatString);
		result[1] = unicoder(formatString.replaceAll("%1%(.*)%%", "").replaceAll("%2%(.*)%%", ""));

		return ColorUtil.colorize(result);
	}

	/**
	 * Replace {{unicode}} with the actual char with unicode representation "unicode"
	 */
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

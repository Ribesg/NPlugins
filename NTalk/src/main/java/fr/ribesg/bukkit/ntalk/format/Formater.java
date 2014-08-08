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

    private final NTalk  plugin;
    private final Config config;

    public Formater(final NTalk instance) {
        this.plugin = instance;
        this.config = instance.getPluginConfig();
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
        this.plugin.entering(this.getClass(), "getFormat", "player=" + player.getName() + '/' + player.getUniqueId() + ";async=" + true);

        Format format = this.config.getDefaultFormat();
        if (this.config.getPlayerFormats().containsKey(player.getUniqueId())) {
            this.plugin.debug("Using player format");
            format = this.config.getPlayerFormats().get(player.getUniqueId());
        } else if (async ? AsyncPermAccessor.isOp(player.getName()) : player.isOp()) {
            this.plugin.debug("Player is Op:");
            if (this.config.getGroupFormats().containsKey(this.config.getOpGroup())) {
                this.plugin.debug(" Using group format of group " + this.config.getOpGroup());
                format = this.config.getGroupFormats().get(this.config.getOpGroup());
            } else {
                this.plugin.debug(" Using default format");
                format = this.config.getDefaultFormat();
            }
        } else {
            this.plugin.debug("Searching group format...");
            for (final String groupName : this.config.getGroupFormats().keySet()) {
                this.plugin.debug(" Checking group " + groupName + "...");
                if (async ? AsyncPermAccessor.has(player.getName(), "maingroup." + groupName.toLowerCase()) : player.hasPermission("maingroup." + groupName.toLowerCase())) {
                    this.plugin.debug("  Using group format of group " + groupName);
                    format = this.config.getGroupFormats().get(groupName);
                    break;
                }
            }
            if (format == this.config.getDefaultFormat()) {
                this.plugin.debug(" No group found for Player " + player.getName() + ", using default format");
            }
        }

        String formatString = this.config.getTemplate();
        final String playerNickname = this.config.getPlayerNicknames().get(player.getUniqueId());
        formatString = formatString.replaceAll("\\Q[prefix]\\E", format.getPrefix());
        formatString = formatString.replaceAll("\\Q[suffix]\\E", format.getSuffix());
        formatString = this.replaceVariables(player, formatString);
        formatString = formatString.replace("[message]", BUKKIT_MESSAGE);
        if (playerNickname == null) {
            formatString = formatString.replace("[name]", BUKKIT_PLAYERNAME);
            formatString = formatString.replaceAll("%%(.*)%%", "");
        } else {
            formatString = formatString.replace("[name]", playerNickname);
            formatString = formatString.replaceAll("%%(.*)%%", "$1");
            formatString = formatString.replace("[realName]", BUKKIT_PLAYERNAME);
        }

        this.plugin.debug("Final format string: " + formatString);

        final String[] result = new String[2];
        result[0] = this.unicoder(formatString);
        result[1] = this.unicoder(formatString.replaceAll("%%(.*)%%", ""));

        this.plugin.exiting(this.getClass(), "getFormat");
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
        this.plugin.entering(this.getClass(), "parsePM", "from=" + from.getName() + ";to=" + to.getName());

        Format formatFrom = this.config.getDefaultFormat(), formatTo = this.config.getDefaultFormat();
        String formatString = this.config.getPmTemplate(); // The final String
        if (!(from instanceof Player)) {
            this.plugin.debug("From is not a Player");
            formatString = formatString.replaceAll("\\Q[prefixFrom]\\E", "");
            formatString = formatString.replace("[nameFrom]", "&cCONSOLE");
            formatString = formatString.replaceAll("\\Q[suffixFrom]\\E", "");
            formatString = formatString.replaceAll("%1%(.*)%%", "");
        } else {
            this.plugin.debug("From is a Player");
            if (this.config.getPlayerFormats().containsKey(((Player)from).getUniqueId())) {
                this.plugin.debug(" Using player format");
                formatFrom = this.config.getPlayerFormats().get(((Player)from).getUniqueId());
            } else if (from.isOp()) {
                this.plugin.debug(" Player is Op:");
                if (this.config.getGroupFormats().containsKey(this.config.getOpGroup())) {
                    this.plugin.debug("  Using group format of group " + this.config.getOpGroup());
                    formatFrom = this.config.getGroupFormats().get(this.config.getOpGroup());
                } else {
                    this.plugin.debug("  Using default format");
                    formatFrom = this.config.getDefaultFormat();
                }
            } else {
                this.plugin.debug(" Searching group format...");
                for (final String groupName : this.config.getGroupFormats().keySet()) {
                    this.plugin.debug(" Checking group " + groupName + "...");
                    if (from.hasPermission("maingroup." + groupName)) {
                        this.plugin.debug("  Using group format of group " + groupName);
                        formatFrom = this.config.getGroupFormats().get(groupName);
                        break;
                    }
                }
                if (formatFrom == this.config.getDefaultFormat()) {
                    this.plugin.debug("  No group found for 'From' Player " + from.getName() + ", using default format");
                }
            }
            final String fromName = from.getName();
            final String fromNickname = this.config.getPlayerNicknames().get(((Player)from).getUniqueId());
            formatString = formatString.replaceAll("\\Q[prefixFrom]\\E", formatFrom.getPrefix());
            formatString = formatString.replaceAll("\\Q[suffixFrom]\\E", formatFrom.getSuffix());
            formatString = this.replaceVariables((Player)from, formatString);
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
            this.plugin.debug("To is not a Player");
            formatString = formatString.replaceAll("\\Q[prefixTo]\\E", "");
            formatString = formatString.replace("[nameTo]", "&cCONSOLE");
            formatString = formatString.replaceAll("\\Q[suffixTo]\\E", "");
            formatString = formatString.replaceAll("%2%(.*)%%", "");
        } else {
            this.plugin.debug("To is a Player");
            if (this.config.getPlayerFormats().containsKey(((Player)to).getUniqueId())) {
                this.plugin.debug(" Using player format");
                formatTo = this.config.getPlayerFormats().get(((Player)to).getUniqueId());
            } else if (to.isOp()) {
                this.plugin.debug(" Player is Op:");
                if (this.config.getGroupFormats().containsKey(this.config.getOpGroup())) {
                    this.plugin.debug("  Using group format of group " + this.config.getOpGroup());
                    formatTo = this.config.getGroupFormats().get(this.config.getOpGroup());
                } else {
                    this.plugin.debug("  Using default format");
                    formatTo = this.config.getDefaultFormat();
                }
            } else {
                this.plugin.debug(" Searching group format...");
                for (final String groupName : this.config.getGroupFormats().keySet()) {
                    this.plugin.debug(" Checking group " + groupName + "...");
                    if (to.hasPermission("maingroup." + groupName)) {
                        this.plugin.debug("  Using group format of group " + groupName);
                        formatTo = this.config.getGroupFormats().get(groupName);
                        break;
                    }
                }
                if (formatTo == this.config.getDefaultFormat()) {
                    this.plugin.debug("  No group found for 'To' Player " + to.getName() + ", using default format");
                }
            }
            final String toName = to.getName();
            final String toNickname = this.config.getPlayerNicknames().get(((Player)to).getUniqueId());
            formatString = formatString.replaceAll("\\Q[prefixTo]\\E", formatTo.getPrefix());
            formatString = formatString.replaceAll("\\Q[suffixTo]\\E", formatTo.getSuffix());
            formatString = this.replaceVariables((Player)to, formatString);
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

        this.plugin.debug("Final format string: " + formatString);

        final String[] result = new String[2];
        result[0] = this.unicoder(formatString);
        result[1] = this.unicoder(formatString.replaceAll("%1%(.*)%%", "").replaceAll("%2%(.*)%%", ""));

        this.plugin.exiting(this.getClass(), "parsePM");
        return ColorUtil.colorize(result);
    }

    /**
     * Replaces various variables in a prefix/suffix
     *
     * @param player the Player involved
     * @param input  the String input
     *
     * @return the String output
     */
    private String replaceVariables(final Player player, final String input) {
        String output = input;
        output = output.replace("[worldName]", player.getWorld().getName());
        // TODO Eventually add other variables
        return output;
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

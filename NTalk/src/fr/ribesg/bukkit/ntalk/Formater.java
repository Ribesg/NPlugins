package fr.ribesg.bukkit.ntalk;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Formater {
    private static final String BUKKIT_PLAYERNAME = "%1$s";
    private static final String BUKKIT_MESSAGE    = "%2$s";

    private final Config        cfg;

    public Formater(final NTalk instance) {
        cfg = instance.getPluginConfig();
    }

    public String getFormat(final Player player) {
        Format format = cfg.getDefaultFormat();
        if (cfg.getPlayerFormats().containsKey(player.getName())) {
            format = cfg.getPlayerFormats().get(player.getName());
        } else if (player.isOp()) {
            if (cfg.getGroupFormats().containsKey(cfg.getOpGroup())) {
                format = cfg.getGroupFormats().get(cfg.getOpGroup());
            } else {
                format = cfg.getDefaultFormat();
            }
        } else {
            for (final String groupName : cfg.getGroupFormats().keySet()) {
                if (player.hasPermission("group." + groupName)) {
                    format = cfg.getGroupFormats().get(groupName);
                    break;
                }
            }
        }
        String prefixedString = new String(cfg.getTemplate());
        prefixedString = prefixedString.replaceAll("\\Q[prefix]\\E", format.getPrefix());
        prefixedString = prefixedString.replace("[name]", BUKKIT_PLAYERNAME);
        prefixedString = prefixedString.replaceAll("\\Q[suffix]\\E", format.getSuffix());
        prefixedString = prefixedString.replace("[message]", BUKKIT_MESSAGE);
        return ChatColor.translateAlternateColorCodes('&', unicoder(prefixedString));
    }

    // Here, a null value for any of the 2 players represents the Console
    public String parsePM(final Player from, final Player to, final String message) {
        Format formatFrom = cfg.getDefaultFormat(), formatTo = cfg.getDefaultFormat();
        String prefixedString = new String(cfg.getPmTemplate()); // The final String
        if (from == null) {
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
            prefixedString = prefixedString.replaceAll("\\Q[prefixFrom]\\E", formatFrom.getPrefix());
            prefixedString = prefixedString.replace("[nameFrom]", from.getName());
            prefixedString = prefixedString.replaceAll("\\Q[suffixFrom]\\E", formatFrom.getSuffix());
        }
        if (to == null) {
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
            prefixedString = prefixedString.replaceAll("\\Q[prefixTo]\\E", formatTo.getPrefix());
            prefixedString = prefixedString.replace("[nameTo]", to.getName());
            prefixedString = prefixedString.replaceAll("\\Q[suffixTo]\\E", formatTo.getSuffix());
        }

        prefixedString = prefixedString.replace("[message]", message);

        return ChatColor.translateAlternateColorCodes('&', unicoder(prefixedString));
    }

    // Replace {{unicode}} by the actual char with unicode representation "unicode"
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

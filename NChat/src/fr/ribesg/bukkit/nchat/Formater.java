package fr.ribesg.bukkit.nchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ribesg.bukkit.nchat.Format.FormatType;

public class Formater {

    private static final String                                                           defaultTemplate   = "&f<[prefix][name][suffix]&f> [message]";
    private static final String                                                           defaultPmTemplate = "&f<[prefixFrom][nameFrom][suffixFrom]&c -> &f[prefixTo][nameTo][suffixTo]&f> [message]";
    private static final Charset                                                          CHARSET           = Charset.defaultCharset();
    private static final String                                                           BUKKIT_PLAYERNAME = "%1$s";
    private static final String                                                           BUKKIT_MESSAGE    = "%2$s";

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private String              template;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private String              pmTemplate;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Format              defaultFormat;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private String              opGroup;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Map<String, Format> playerFormats;                                                                                               // PlayerName;Format
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Map<String, Format> groupFormats;                                                                                                // GroupName;Format

    public Formater() {
        setTemplate(defaultTemplate);
        setPmTemplate(defaultPmTemplate);
        setOpGroup("admin");
        setDefaultFormat(new Format(FormatType.GROUP, "default", "", ""));
        setPlayerFormats(new HashMap<String, Format>());
        setGroupFormats(new HashMap<String, Format>());
    }

    public String getFormat(final Player player) {
        Format format = getDefaultFormat();
        if (getPlayerFormats().containsKey(player.getName())) {
            format = getPlayerFormats().get(player.getName());
        } else if (player.isOp()) {
            if (getGroupFormats().containsKey(getOpGroup())) {
                format = getGroupFormats().get(getOpGroup());
            } else {
                format = getDefaultFormat();
            }
        } else {
            for (final String groupName : getGroupFormats().keySet()) {
                if (player.hasPermission("group." + groupName)) {
                    format = getGroupFormats().get(groupName);
                    break;
                }
            }
        }
        String prefixedString = new String(getTemplate());
        prefixedString = prefixedString.replaceAll("\\Q[prefix]\\E", format.getPrefix());
        prefixedString = prefixedString.replace("[name]", BUKKIT_PLAYERNAME);
        prefixedString = prefixedString.replaceAll("\\Q[suffix]\\E", format.getSuffix());
        prefixedString = prefixedString.replace("[message]", BUKKIT_MESSAGE);
        return ChatColor.translateAlternateColorCodes('&', unicoder(prefixedString));
    }

    // Here, a null value for any of the 2 players represents the Console
    public String parsePM(final Player from, final Player to, final String message) {
        Format formatFrom = getDefaultFormat(), formatTo = getDefaultFormat();
        String prefixedString = new String(getPmTemplate()); // The final String
        if (from == null) {
            prefixedString = prefixedString.replaceAll("\\Q[prefixFrom]\\E", "");
            prefixedString = prefixedString.replace("[nameFrom]", "&cCONSOLE");
            prefixedString = prefixedString.replaceAll("\\Q[suffixFrom]\\E", "");
        } else {
            if (getPlayerFormats().containsKey(from.getName())) {
                formatFrom = getPlayerFormats().get(from.getName());
            } else if (from.isOp()) {
                if (getGroupFormats().containsKey(getOpGroup())) {
                    formatFrom = getGroupFormats().get(getOpGroup());
                } else {
                    formatFrom = getDefaultFormat();
                }
            } else {
                for (final String groupName : getGroupFormats().keySet()) {
                    if (from.hasPermission("group." + groupName)) {
                        formatFrom = getGroupFormats().get(groupName);
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
            if (getPlayerFormats().containsKey(to.getName())) {
                formatTo = getPlayerFormats().get(to.getName());
            } else if (to.isOp()) {
                if (getGroupFormats().containsKey(getOpGroup())) {
                    formatTo = getGroupFormats().get(getOpGroup());
                } else {
                    formatTo = getDefaultFormat();
                }
            } else {
                for (final String groupName : getGroupFormats().keySet()) {
                    if (to.hasPermission("group." + groupName)) {
                        formatTo = getGroupFormats().get(groupName);
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

    public void newConfig(final Path pathConfig) throws IOException {
        getGroupFormats().put("admin", new Format(FormatType.GROUP, "admin", "&c[Admin]&f", ""));
        getGroupFormats().put("user", new Format(FormatType.GROUP, "user", "&c[User]&f", ""));
        getPlayerFormats().put("Ribesg", new Format(FormatType.PLAYER, "Ribesg", "&c[Developer]&f", ""));
        getPlayerFormats().put("Notch", new Format(FormatType.PLAYER, "Notch", "&c[God]&f", ""));
        writeConfig(pathConfig);
    }

    public void writeConfig(final Path pathConfig) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(pathConfig, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            final StringBuilder content = new StringBuilder();

            // Header
            content.append("################################################################################\n");
            content.append("# Config file for NChat plugin. If you don't understand something, please      #\n");
            content.append("# ask on dev.bukkit.org or on forum post.                               Ribesg #\n");
            content.append("################################################################################\n\n");

            // template for chat messages
            content.append("# The template used to parse chat messages\n");
            content.append("# Default : " + defaultTemplate + "\n");
            content.append("template: \"" + getTemplate() + "\"\n\n");

            // template for private messages
            content.append("# The template used to parse private messages\n");
            content.append("# Default : " + defaultPmTemplate + "\n");
            content.append("pmTemplate: \"" + getPmTemplate() + "\"\n\n");

            // the group used for Op players
            content.append("# The group used for Op players\n");
            content.append("opGroup: \"" + getOpGroup() + "\"\n\n");

            // default prefix & suffix for player without any group permission or custom prefix and suffix
            content.append("# Default prefix and suffix used for player without custom prefix/suffix or group\n");
            content.append("# Default : both empty\n");
            content.append("defaultFormat: \n");
            content.append("  prefix: \"" + getDefaultFormat().getPrefix() + "\"\n");
            content.append("  suffix: \"" + getDefaultFormat().getSuffix() + "\"\n\n");

            // group prefixes and suffixes
            content.append("# Group prefixes and suffixes. Use exact group names as written in your permissions files\n");
            content.append("groupFormats:\n");
            for (final Entry<String, Format> e : getGroupFormats().entrySet()) {
                content.append("  " + e.getKey() + ": \n");
                content.append("    prefix: \"" + e.getValue().getPrefix() + "\"\n");
                content.append("    suffix: \"" + e.getValue().getSuffix() + "\"\n\n");
            }

            // group prefixes and suffixes
            content.append("# Player prefixes and suffixes. Use exact player names\n");
            content.append("playerFormats:\n");
            for (final Entry<String, Format> e : getPlayerFormats().entrySet()) {
                content.append("  " + e.getKey() + ": \n");
                content.append("    prefix: \"" + e.getValue().getPrefix() + "\"\n");
                content.append("    suffix: \"" + e.getValue().getSuffix() + "\"\n\n");
            }

            writer.write(content.toString());
        }
    }

    public void load(final Path pathConfig) throws IOException {
        if (!Files.exists(pathConfig)) {
            Files.createFile(pathConfig);
            newConfig(pathConfig);
        } else {
            final YamlConfiguration config = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(pathConfig, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine() + '\n');
                }
                config.loadFromString(s.toString());
            } catch (final Exception e) {
                e.printStackTrace();
            }

            setTemplate(config.getString("template", getTemplate()));
            setPmTemplate(config.getString("pmTemplate", getPmTemplate()));
            setOpGroup(config.getString("opGroup", getOpGroup()));
            if (config.isConfigurationSection("defaultFormat")) {
                final ConfigurationSection defaultFormat = config.getConfigurationSection("defaultFormat");
                final String prefix = defaultFormat.getString("prefix", "");
                final String suffix = defaultFormat.getString("suffix", "");
                setDefaultFormat(new Format(FormatType.GROUP, "default", prefix, suffix));
            }
            if (config.isConfigurationSection("groupFormats")) {
                final ConfigurationSection groupFormats = config.getConfigurationSection("groupFormats");
                for (final String groupName : groupFormats.getKeys(false)) {
                    final ConfigurationSection groupFormat = groupFormats.getConfigurationSection(groupName);
                    final String prefix = groupFormat.getString("prefix", "");
                    final String suffix = groupFormat.getString("suffix", "");
                    getGroupFormats().put(groupName, new Format(FormatType.GROUP, groupName, prefix, suffix));
                }
            }
            if (config.isConfigurationSection("playerFormats")) {
                final ConfigurationSection playerFormats = config.getConfigurationSection("playerFormats");
                for (final String playerName : playerFormats.getKeys(false)) {
                    final ConfigurationSection playerFormat = playerFormats.getConfigurationSection(playerName);
                    final String prefix = playerFormat.getString("prefix", "");
                    final String suffix = playerFormat.getString("suffix", "");
                    getPlayerFormats().put(playerName, new Format(FormatType.PLAYER, playerName, prefix, suffix));
                }
            }

            // Rewrite the config to "clean" it
            writeConfig(pathConfig);
        }
    }
}

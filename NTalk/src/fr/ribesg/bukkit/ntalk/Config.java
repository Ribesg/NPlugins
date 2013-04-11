package fr.ribesg.bukkit.ntalk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntalk.format.Format;
import fr.ribesg.bukkit.ntalk.format.Format.FormatType;

public class Config extends AbstractConfig {

    private final NTalk                                              plugin;

    private static final String                                      defaultTemplate   = "&f<[prefix][name][suffix]&f> [message]";
    private static final String                                      defaultPmTemplate = "&f<[prefixFrom][nameFrom][suffixFrom]&c -> &f[prefixTo][nameTo][suffixTo]&f> [message]";

    @Getter @Setter(AccessLevel.PRIVATE) private String              template;
    @Getter @Setter(AccessLevel.PRIVATE) private String              pmTemplate;
    @Getter @Setter(AccessLevel.PRIVATE) private Format              defaultFormat;
    @Getter @Setter(AccessLevel.PRIVATE) private String              opGroup;

    // PlayerName;Format
    @Getter @Setter(AccessLevel.PRIVATE) private Map<String, Format> playerFormats;
    @Getter @Setter(AccessLevel.PRIVATE) private Map<String, String> playerNicknames;

    // GroupName;Format
    @Getter @Setter(AccessLevel.PRIVATE) private Map<String, Format> groupFormats;

    public Config(final NTalk instance) {
        plugin = instance;

        setTemplate(defaultTemplate);
        setPmTemplate(defaultPmTemplate);
        setDefaultFormat(new Format(FormatType.GROUP, "default", "", ""));
        setOpGroup("admin");

        setPlayerFormats(new HashMap<String, Format>());
        getPlayerFormats().put("Ribesg", new Format(FormatType.PLAYER, "Ribesg", "&c[Dev]&f", ""));
        getPlayerFormats().put("Notch", new Format(FormatType.PLAYER, "Notch", "&c[God]&f", ""));

        setPlayerNicknames(new HashMap<String, String>());
        getPlayerNicknames().put("Notch", "TheNotch");

        setGroupFormats(new HashMap<String, Format>());
        getGroupFormats().put("admin", new Format(FormatType.GROUP, "admin", "&c[Admin]&f", ""));
        getGroupFormats().put("user", new Format(FormatType.GROUP, "user", "&c[User]&f", ""));

    }

    /**
     * @see AbstractConfig#setValues(YamlConfiguration)
     */
    @Override
    protected void setValues(final YamlConfiguration config) {

        setPlayerFormats(new HashMap<String, Format>());
        setPlayerNicknames(new HashMap<String, String>());
        setGroupFormats(new HashMap<String, Format>());

        // template. Default: "&f<[prefix][name][suffix]&f> [message]".
        // Possible values: Any String containing at least "[name]" and "[message]"
        setTemplate(config.getString("template", defaultTemplate));
        if (!getTemplate().contains("[name]") || !getTemplate().contains("[message]")) {
            setTemplate(defaultTemplate);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, "config.yml", "template", defaultTemplate);
        }

        // pmTemplate. Default: "&f<[prefixFrom][nameFrom][suffixFrom]&c -> &f[prefixTo][nameTo][suffixTo]&f> [message]".
        // Possible values: Any String containing at least "[nameFrom]", "[nameTo]" and "[message]"
        setPmTemplate(config.getString("pmTemplate", defaultPmTemplate));
        if (!getPmTemplate().contains("[nameFrom]") || !getPmTemplate().contains("[nameTo]") || !getPmTemplate().contains("[message]")) {
            setPmTemplate(defaultPmTemplate);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, "config.yml", "pmTemplate", defaultPmTemplate);
        }

        // opGroup. Default: "admin".
        // Possible values: Any group defined in the config
        // NOTE: We later check if the value is valid (after loading groups)
        setOpGroup(config.getString("opGroup", getOpGroup()));

        // defaultFormat. Default: empty prefix and suffix.
        // Possible values: any String for prefix and suffix
        if (config.isConfigurationSection("defaultFormat")) {
            final ConfigurationSection defaultFormat = config.getConfigurationSection("defaultFormat");
            final String prefix = defaultFormat.getString("prefix", "");
            final String suffix = defaultFormat.getString("suffix", "");
            setDefaultFormat(new Format(FormatType.GROUP, "default", prefix, suffix));
        }

        // groupFormats.
        if (config.isConfigurationSection("groupFormats")) {
            final ConfigurationSection groupFormats = config.getConfigurationSection("groupFormats");
            for (final String groupName : groupFormats.getKeys(false)) {
                final ConfigurationSection groupFormat = groupFormats.getConfigurationSection(groupName);
                final String prefix = groupFormat.getString("prefix", "");
                final String suffix = groupFormat.getString("suffix", "");
                getGroupFormats().put(groupName, new Format(FormatType.GROUP, groupName, prefix, suffix));
            }
        }

        // playerFormats.
        if (config.isConfigurationSection("playerFormats")) {
            final ConfigurationSection playerFormats = config.getConfigurationSection("playerFormats");
            for (final String playerName : playerFormats.getKeys(false)) {
                final ConfigurationSection playerFormat = playerFormats.getConfigurationSection(playerName);
                final String prefix = playerFormat.getString("prefix", "NOPREFIX");
                final String nickName = playerFormat.getString("nick", "NONICK");
                final String suffix = playerFormat.getString("suffix", "NOSUFFIX");
                if (!prefix.equals("NOPREFIX") || !suffix.equals("NOSUFFIX")) {
                    getPlayerFormats().put(playerName, new Format(FormatType.PLAYER, playerName, prefix.equals("NOPREFIX") ? "" : prefix, suffix.equals("NOSUFFIX") ? "" : suffix));
                }
                if (!nickName.equals("NONICK")) {
                    getPlayerNicknames().put(playerName, nickName);
                }
            }
        }

        // Check for opGroup
        if (getGroupFormats().get(getOpGroup()) == null) {
            // Reset to admin group, and add it if it does not exists
            if (!getGroupFormats().containsKey("admin")) {
                getGroupFormats().put("admin", new Format(FormatType.GROUP, "admin", "&c[Admin]&f", ""));
            }
            setOpGroup("admin");
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, "config.yml", "opGroup", "admin");
        }
    }

    /**
     * @see AbstractConfig#getConfigString()
     */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();

        // Header
        content.append("################################################################################\n");
        content.append("# Config file for NTalk plugin. If you don't understand something, please ask  #\n");
        content.append("# on dev.bukkit.org or on forum post.                                   Ribesg #\n");
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
            content.append("    suffix: \"" + e.getValue().getSuffix() + "\"\n");
        }
        content.append("\n");

        // player prefixes and suffixes
        content.append("# Player prefixes, nicknames and suffixes. Use exact player names\n");
        content.append("playerFormats:\n");
        final Set<String> playerNames = new HashSet<String>();
        playerNames.addAll(getPlayerFormats().keySet());
        playerNames.addAll(getPlayerNicknames().keySet());
        for (final String playerName : playerNames) {
            String prefix = "";
            String nickName = "";
            String suffix = "";
            if (getPlayerFormats().containsKey(playerName)) {
                prefix = getPlayerFormats().get(playerName).getPrefix();
                suffix = getPlayerFormats().get(playerName).getSuffix();
            }
            if (getPlayerNicknames().containsKey(playerName)) {
                nickName = getPlayerNicknames().get(playerName);
            }
            content.append("  " + playerName + ": \n");
            if (prefix.length() > 0) {
                content.append("    prefix: \"" + prefix + "\"\n");
            }
            if (nickName.length() > 0) {
                content.append("    nick: \"" + nickName + "\"\n");
            }
            if (suffix.length() > 0) {
                content.append("    suffix: \"" + suffix + "\"\n");
            }
        }
        content.append("\n");

        return content.toString();
    }
}

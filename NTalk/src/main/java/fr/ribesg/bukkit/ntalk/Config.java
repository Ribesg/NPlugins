/***************************************************************************
 * Project file:    NPlugins - NTalk - Config.java                         *
 * Full Class name: fr.ribesg.bukkit.ntalk.Config                          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntalk.format.Format;
import fr.ribesg.bukkit.ntalk.format.Format.FormatType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Config extends AbstractConfig<NTalk> {

	private static final String defaultTemplate        = "&f<[prefix][name][suffix]&f> [message]";
	private static final String defaultPmTemplate      = "&f<[prefixFrom][nameFrom][suffixFrom]&c -> &f[prefixTo][nameTo][suffixTo]&f> " +
	                                                     "[message]";
	private static final String defaultTempMuteCommand = "/mute %player% %duration%s %reason%";
	private static final String defaultTempBanCommand  = "/ban %player% %duration%s %reason%";
	private static final String defaultTempJailCommand = "/jail %player% %duration% %jailName% %reason%";

	private String template;
	private String pmTemplate;
	private Format defaultFormat;
	private String opGroup;

	// PlayerName;Format
	private Map<String, Format> playerFormats;
	private Map<String, String> playerNicknames;

	// GroupName;Format
	private Map<String, Format> groupFormats;

	// Filters
	private boolean chatFiltersEnabled;
	private String  tempMuteCommand;
	private String  tempBanCommand;
	private String  tempJailCommand;

	public Config(final NTalk instance) {
		super(instance);

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

		setChatFiltersEnabled(true);
		setTempMuteCommand(defaultTempMuteCommand);
		setTempBanCommand(defaultTempBanCommand);
		setTempJailCommand(defaultTempJailCommand);
	}

	/** @see AbstractConfig#handleValues(YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) {

		setPlayerFormats(new HashMap<String, Format>());
		setPlayerNicknames(new HashMap<String, String>());
		setGroupFormats(new HashMap<String, Format>());

		// template. Default: "&f<[prefix][name][suffix]&f> [message]".
		// Possible values: Any String containing at least "[name]" and "[message]"
		setTemplate(config.getString("template", defaultTemplate));
		if (!getTemplate().contains("[name]") || !getTemplate().contains("[message]")) {
			setTemplate(defaultTemplate);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "template",
			                   defaultTemplate);
		}

		// pmTemplate. Default: "&f<[prefixFrom][nameFrom][suffixFrom]&c -> &f[prefixTo][nameTo][suffixTo]&f> [message]".
		// Possible values: Any String containing at least "[nameFrom]", "[nameTo]" and "[message]"
		setPmTemplate(config.getString("pmTemplate", defaultPmTemplate));
		if (!getPmTemplate().contains("[nameFrom]") || !getPmTemplate().contains("[nameTo]") || !getPmTemplate().contains("[message]")) {
			setPmTemplate(defaultPmTemplate);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "pmTemplate",
			                   defaultPmTemplate);
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
					getPlayerFormats().put(playerName,
					                       new Format(FormatType.PLAYER,
					                                  playerName,
					                                  prefix.equals("NOPREFIX") ? "" : prefix,
					                                  suffix.equals("NOSUFFIX") ? "" : suffix));
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
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "opGroup",
			                   "admin");
		}

		// Chat filter
		setChatFiltersEnabled(config.getBoolean("enableChatFilter", true));

		setTempMuteCommand(config.getString("tempMuteCommand", defaultTempMuteCommand));
		if (!getTempMuteCommand().contains("%player%") ||
		    !getTempMuteCommand().contains("%duration%") ||
		    !getTempMuteCommand().contains("%reason%")) {
			setTempMuteCommand(defaultTempMuteCommand);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "tempMuteCommand",
			                   defaultTempMuteCommand);
		}

		setTempBanCommand(config.getString("tempBanCommand", defaultTempBanCommand));
		if (!getTempBanCommand().contains("%player%") ||
		    !getTempBanCommand().contains("%duration%") ||
		    !getTempBanCommand().contains("%reason%")) {
			setTempBanCommand(defaultTempBanCommand);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "tempBanCommand",
			                   defaultTempBanCommand);
		}

		setTempJailCommand(config.getString("tempJailCommand", defaultTempJailCommand));
		if (!getTempJailCommand().contains("%player%") ||
		    !getTempJailCommand().contains("%duration%") ||
		    !getTempJailCommand().contains("%jailName%") ||
		    !getTempJailCommand().contains("%reason%")) {
			setTempJailCommand(defaultTempJailCommand);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "tempJailCommand",
			                   defaultTempJailCommand);
		}
	}

	/** @see AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NTalk plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

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
		final Set<String> playerNames = new HashSet<>();
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

		// Chat filter
		content.append("# Globally toggle the Chat Filter system.\n");
		content.append("enableChatFilter: " + isChatFiltersEnabled() + "\n\n");

		content.append("# The tempmute command that will be used to mute people\n");
		content.append("# It should contain:\n");
		content.append("# - %player% : will be replaced by the player's name\n");
		content.append("# - %duration% : will be replaced by the duration, in seconds\n");
		content.append("# - %reason% : will be replaced by the reason\n");
		content.append("tempMuteCommand: " + getTempMuteCommand() + "\n\n");

		content.append("# The tempban command that will be used to ban people\n");
		content.append("# It should contain:\n");
		content.append("# - %player% : will be replaced by the player's name\n");
		content.append("# - %duration% : will be replaced by the duration, in seconds\n");
		content.append("# - %reason% : will be replaced by the reason\n");
		content.append("tempBanCommand: " + getTempBanCommand() + "\n\n");

		content.append("# The tempjail command that will be used to mute people\n");
		content.append("# It should contain:\n");
		content.append("# - %player% : will be replaced by the player's name\n");
		content.append("# - %duration% : will be replaced by the duration, in seconds\n");
		content.append("# - %jailName% : will be replaced by the jail name\n");
		content.append("# - %reason% : will be replaced by the reason\n");
		content.append("tempJailCommand: " + getTempJailCommand() + "\n\n");

		return content.toString();
	}

	public Map<String, Format> getGroupFormats() {
		return groupFormats;
	}

	public void setGroupFormats(final Map<String, Format> groupFormats) {
		this.groupFormats = groupFormats;
	}

	public String getOpGroup() {
		return opGroup;
	}

	public void setOpGroup(final String opGroup) {
		this.opGroup = opGroup;
	}

	public Map<String, Format> getPlayerFormats() {
		return playerFormats;
	}

	public void setPlayerFormats(final Map<String, Format> playerFormats) {
		this.playerFormats = playerFormats;
	}

	public Map<String, String> getPlayerNicknames() {
		return playerNicknames;
	}

	public void setPlayerNicknames(final Map<String, String> playerNicknames) {
		this.playerNicknames = playerNicknames;
	}

	public String getPmTemplate() {
		return pmTemplate;
	}

	public void setPmTemplate(final String pmTemplate) {
		this.pmTemplate = pmTemplate;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(final String template) {
		this.template = template;
	}

	public Format getDefaultFormat() {
		return defaultFormat;
	}

	public void setDefaultFormat(final Format defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	public boolean isChatFiltersEnabled() {
		return chatFiltersEnabled;
	}

	public void setChatFiltersEnabled(final boolean chatFiltersEnabled) {
		this.chatFiltersEnabled = chatFiltersEnabled;
	}

	public String getTempMuteCommand() {
		return tempMuteCommand;
	}

	public String getTempMuteCommand(final String playerName, final long duration, final String reason) {
		String result = getTempMuteCommand();
		result = result.replace("%player%", playerName);
		result = result.replace("%duration%", Long.toString(duration));
		result = result.replace("%reason%", reason);
		return result.substring(1);
	}

	public void setTempMuteCommand(final String tempMuteCommand) {
		this.tempMuteCommand = tempMuteCommand;
	}

	public String getTempBanCommand() {
		return tempBanCommand;
	}

	public String getTempBanCommand(final String playerName, final long duration, final String reason) {
		String result = getTempBanCommand();
		result = result.replace("%player%", playerName);
		result = result.replace("%duration%", Long.toString(duration));
		result = result.replace("%reason%", reason);
		return result.substring(1);
	}

	public void setTempBanCommand(final String tempBanCommand) {
		this.tempBanCommand = tempBanCommand;
	}

	public String getTempJailCommand() {
		return tempJailCommand;
	}

	public String getTempJailCommand(final String playerName, final long duration, final String jailName, final String reason) {
		String result = getTempJailCommand();
		result = result.replace("%player%", playerName);
		result = result.replace("%duration%", Long.toString(duration));
		result = result.replace("%jailName%", jailName);
		result = result.replace("%reason%", reason);
		return result.substring(1);
	}

	public void setTempJailCommand(final String tempJailCommand) {
		this.tempJailCommand = tempJailCommand;
	}
}

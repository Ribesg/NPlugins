/***************************************************************************
 * Project file:    NPlugins - NCore - AbstractMessages.java               *
 * Full Class name: fr.ribesg.bukkit.ncore.lang.AbstractMessages           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.lang;

import fr.ribesg.bukkit.ncore.util.ColorUtil;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Set;

/**
 * Represents all the messages sent to players
 *
 * @author Ribesg
 */
public abstract class AbstractMessages {

	/**
	 * Separator used in config to define if you want to send multiple messages to player
	 */
	public static final String LINE_SEPARATOR = "##";

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	private final String nodeName;

	/**
	 * Header of each messages sent to player
	 */
	private final String messageHeader;

	/**
	 * Charset used for reading/writing config file
	 */
	private static final Charset CHARSET = StandardCharsets.UTF_8;

	private EnumMap<MessageId, Message> messagesMap;                            // Id ; Message

	/**
	 * Create a new AbstractMessages base for a plugin node Uses the node name for future Message Headers
	 *
	 * @param nodeName The plugin Node name
	 */
	public AbstractMessages(final String nodeName) {
		this.nodeName = nodeName;
		this.messageHeader = "§0[§c§lN§6" + nodeName + "§0] §f";
	}

	/**
	 * Load the config containing messages Creates a new config if it does not exists Fix the config after parsing
	 *
	 * @param plugin The plugin
	 *
	 * @throws IOException If there is an error reading / writing file
	 */
	public void loadMessages(final JavaPlugin plugin) throws IOException {
		final Path path = Paths.get(plugin.getDataFolder().toPath().toAbsolutePath().toString() + File.separator + "messages.yml");
		messagesMap = getDefaultConfig();
		if (!Files.exists(path)) {
			newMessages(path);
		} else {
			final YamlConfiguration messagesConfig = new YamlConfiguration();
			try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
				final StringBuilder s = new StringBuilder();
				while (reader.ready()) {
					s.append(reader.readLine()).append('\n');
				}
				messagesConfig.loadFromString(s.toString());
			} catch (final Exception e) {
				e.printStackTrace();
			}
			for (final String idString : messagesConfig.getKeys(false)) {
				try {
					final MessageId id = MessageId.valueOf(idString);
					final Message def = messagesMap.get(id);
					final ConfigurationSection section = messagesConfig.getConfigurationSection(idString);
					if (section == null) {
						plugin.getLogger().warning(idString + "was not found in messages.yml, maybe it's new? Adding default value!");
					} else if (def == null) {
						plugin.getLogger().warning(idString + " was found in messages.yml but not in the default messages list for this Node. This is due to the fact that it has been removed from this Node, but not from all Nodes. Removing it from this Node's messages.yml!");
					} else {
						final String value = section.getString("value", def.getDefaultMessage());
						final boolean useHeader = section.getBoolean("useHeader", true);
						messagesMap.put(id, new Message(id, def.getDefaultMessage(), value, def.defaultUseHeader(), useHeader, def.getAwaitedArgs()));
					}
				} catch (final IllegalArgumentException e) {
					plugin.getLogger().warning(idString + " is not / no longer used, removing it from messages config file.");
				}
			}
			overwriteConfig(path);
		}
	}

	/**
	 * @return a default AbstractMessages map
	 */
	EnumMap<MessageId, Message> getDefaultConfig() {
		final EnumMap<MessageId, Message> map = new EnumMap<>(MessageId.class);
		for (final Message m : createMessage()) {
			map.put(m.getId(), m);
		}
		return map;
	}

	/**
	 * Here we define the actual messages, for each node
	 *
	 * @return a Set of messages for this plugin
	 */
	protected abstract Set<Message> createMessage();

	private void newMessages(final Path pathMessages) throws IOException {
		writeMessages(pathMessages, false);
	}

	private void overwriteConfig(final Path pathMessages) throws IOException {
		writeMessages(pathMessages, true);
	}

	private void writeMessages(final Path path, final boolean overwrite) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET, overwrite ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
			writer.write(getConfigString());
		}
	}

	/**
	 * @return the String that will be written in config file
	 */
	private String getConfigString() {
		final StringBuilder content = new StringBuilder();

		final FrameBuilder frame = new FrameBuilder();
		frame.addLine("List of N" + nodeName + " messages. You're free to change text/colors/language here.");
		frame.addLine("Supports both '§' and '&' characters for colors.");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line).append('\n');
		}
		content.append('\n');

		for (final Message m : getMessagesMap().values()) {
			content.append("# Default value    : " + m.getDefaultMessage() + '\n');
			content.append("# Default useHeader: " + m.defaultUseHeader() + '\n');
			content.append("# Awaited arguments: " + m.getAwaitedArgsString() + '\n');
			content.append(m.getId().name() + ":\n");
			content.append("  value: \"" + (m.getConfigMessage() != null ? m.getConfigMessage() : m.getDefaultMessage()) + "\"\n");
			content.append("  useHeader: " + m.useHeader() + "\n\n");
		}
		return content.toString();
	}

	/**
	 * @param id   The Id of the message we want
	 * @param args Arguments in the same order as in the result of {@link Message#getAwaitedArgs()}
	 *
	 * @return a String[] containing one String for each line to send to the player
	 */
	public String[] get(final MessageId id, final String... args) {
		try {
			final Message m = getMessagesMap().get(id);
			if (args != null && args.length != m.getAwaitedArgsNb() || args == null && m.getAwaitedArgsNb() > 0) {
				throw new IllegalArgumentException("Call to Messages.get(id,args...) with wrong number of args : " +
				                                   (args == null ? 0 : args.length) +
				                                   " (awaited : " +
				                                   m.getAwaitedArgsNb() +
				                                   ")");
			}
			String res = m.getConfigMessage() == null ? m.getDefaultMessage() : m.getConfigMessage();

			// Handle empty-string as "do not send anything"
			if (res.isEmpty()) {
				return EMPTY_STRING_ARRAY;
			}

			// Replacing args by there values
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					res = res.replace(m.getAwaitedArgs()[i], args[i]);
				}
			}
			// Adding Header, colors
			final String[] resSplit = res.concat(LINE_SEPARATOR).split(LINE_SEPARATOR);
			for (int i = 0; i < resSplit.length; i++) {
				resSplit[i] = (m.useHeader() ? messageHeader : "") + ColorUtil.colorize(resSplit[i]);
			}
			return resSplit;
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
			return new String[] {messageHeader + ChatColor.RED + "Something gone wrong, see console"};
		}
	}

	/**
	 * @param strings The strings to merge
	 *
	 * @return A concatenation of the strings to merge separated by {@link #LINE_SEPARATOR}
	 */
	public static String merge(final String[] strings) {
		if (strings == null || strings.length < 1) {
			return null;
		} else {
			final StringBuilder res = new StringBuilder(strings[0]);
			for (int i = 1; i < strings.length; i++) {
				res.append(LINE_SEPARATOR);
				res.append(strings[i]);
			}
			return res.toString();
		}
	}

	public String getMessageHeader() {
		return messageHeader;
	}

	public EnumMap<MessageId, Message> getMessagesMap() {
		return messagesMap;
	}
}

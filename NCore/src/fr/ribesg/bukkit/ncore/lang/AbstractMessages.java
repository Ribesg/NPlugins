package fr.ribesg.bukkit.ncore.lang;

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

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents all the messages sent to players
 * 
 * @author Ribesg
 */
public abstract class AbstractMessages {

    /**
     * Separator used in config to define if you want to send multiple messages to player
     */
    public static final String                  LINE_SEPARATOR = "%%";

    /**
     * Header of each messages sent to player
     */
    public static String                        MESSAGE_HEADER = "§0§l[§c§lN§6§lCore§0§l] §f";

    /**
     * Charset used for reading/writing config file
     */
    protected static final Charset              CHARSET        = StandardCharsets.UTF_8;

    @Getter private EnumMap<MessageId, Message> messagesMap;                                  // Id ; Message

    /**
     * Create a new AbstractMessages base for a plugin node Uses the node name for future Message Headers
     * 
     * @param nodeName
     *            The plugin Node name
     */
    public AbstractMessages(final String nodeName) {
        MESSAGE_HEADER = "§0§l[§c§lN§6§l" + nodeName + "§0§l] §f";
    }

    /**
     * Load the config containing messages Creates a new config if it does not exists Fix the config after parsing
     * 
     * @param plugin
     *            The plugin
     * @throws IOException
     *             If there is an error reading / writing file
     */
    public void loadMessages(final JavaPlugin plugin) throws IOException {
        final Path path = Paths.get(
                plugin.getDataFolder().toPath().toAbsolutePath().toString()
                        + File.separator
                        + "messages.yml");
        messagesMap = getDefaultConfig();
        if (!Files.exists(path)) {
            newMessages(path);
        } else {
            final YamlConfiguration cMessages = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine() + '\n');
                }
                cMessages.loadFromString(s.toString());
            } catch (final Exception e) {
                e.printStackTrace();
            }
            for (final String idString : cMessages.getKeys(false)) {
                try {
                    final MessageId id = MessageId.valueOf(idString);
                    final Message def = messagesMap.get(id);
                    messagesMap.put(id,
                            new Message(id, def.getDefaultMessage(), def.getAwaitedArgs(), cMessages.getString(
                                    idString, def.getDefaultMessage())));
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            overwriteConfig(path);
        }
    }

    /**
     * @return a default AbstractMessages map
     */
    public EnumMap<MessageId, Message> getDefaultConfig() {
        final EnumMap<MessageId, Message> map = new EnumMap<MessageId, Message>(MessageId.class);
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
        try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET,
                overwrite ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.CREATE_NEW,
                StandardOpenOption.WRITE)) {
            writer.write(getConfigString());
        }
    }

    /**
     * @return the String that will be written in config file
     */
    protected abstract String getConfigString();

    /**
     * @param id
     *            The Id of the message we want
     * @param args
     *            Arguments in the same order as in the result of {@link Message#getAwaitedArgs()}
     * @return a String[] containing one String for each line to send to the player
     */
    public String[] get(final MessageId id, final String... args) {
        try {
            final Message m = getMessagesMap().get(id);
            if (args != null && args.length != m.getAwaitedArgsNb() || args == null && m.getAwaitedArgsNb() > 0) {
                throw new IllegalArgumentException(
                        "Call to AbstractMessages.get(id,args...) with wrong number of args : "
                                + (args == null ? 0 : args.length) + " (awaited : "
                                + m.getAwaitedArgsNb() + ")");
            }
            String res = m.getConfigMessage() == null ? m.getDefaultMessage() : m.getConfigMessage();
            // Replacing args by there values
            for (int i = 0; i < m.getAwaitedArgsNb(); i++) {
                res = res.replace(m.getAwaitedArgs()[i], args[i]);
            }
            // Adding Header, colors
            final String[] resSplit = res.concat(LINE_SEPARATOR).split(LINE_SEPARATOR);
            for (int i = 0; i < resSplit.length; i++) {
                resSplit[i] = MESSAGE_HEADER + ChatColor.translateAlternateColorCodes('&', resSplit[i]);
            }
            return resSplit;
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            return new String[] { MESSAGE_HEADER + ChatColor.RED + "Something gone wrong, see console" };
        }
    }

    /**
     * @param strings
     *            The strings to merge
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
}

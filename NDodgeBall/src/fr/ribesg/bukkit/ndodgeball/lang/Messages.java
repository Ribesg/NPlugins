package fr.ribesg.bukkit.ndodgeball.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {

    public enum MessageId {

        // General plugin messages
        errorWhileLoadingConfiguration,

        // General deny response
        noPermissionForCommand,
        cmdOnlyAvailableForPlayers,

        // Command - RELOAD
        cmdReloadConfig,
        cmdReloadMessages,

    }

    public static final String                         LINE_SEPARATOR = "%%";
    public static final String                         MESSAGE_HEADER = "§0§l[§c§lN§6§lChat§0§l] §f";
    public static final Charset                        CHARSET        = Charset.defaultCharset();

    @Getter private static EnumMap<MessageId, Message> messagesMap;                                  // Id ; Message

    public static void loadConfig(final Path pathMessages) throws IOException {
        messagesMap = getDefaultConfig();
        if (!Files.exists(pathMessages)) {
            newConfig(pathMessages);
        } else {
            final YamlConfiguration cMessages = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(pathMessages, CHARSET)) {
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
                    messagesMap.put(id, new Message(id, def.getDefaultMessage(), def.getAwaitedArgs(), cMessages.getString(idString, def.getDefaultMessage())));
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            overwriteConfig(pathMessages);
        }
    }

    public static EnumMap<MessageId, Message> getDefaultConfig() {
        final Set<Message> newMessages = new HashSet<Message>();

        // General plugin messages
        newMessages.add(new Message(MessageId.errorWhileLoadingConfiguration, "&cError while loading config file %filename%", new String[] { "%filename%" }, null));

        // General deny response
        newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null));
        newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null));

        // Command - RELOAD
        newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null));
        newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null));

        final EnumMap<MessageId, Message> map = new EnumMap<MessageId, Message>(MessageId.class);
        for (final Message m : newMessages) {
            map.put(m.getId(), m);
        }
        return map;
    }

    private static void newConfig(final Path pathMessages) throws IOException {
        writeConfig(pathMessages, false);
    }

    private static void overwriteConfig(final Path pathMessages) throws IOException {
        writeConfig(pathMessages, true);
    }

    private static void writeConfig(final Path pathMessages, final boolean overwrite) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(pathMessages, CHARSET, overwrite ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            final StringBuilder content = new StringBuilder();
            content.append("################################################################################\n");
            content.append("# List of NChat messages. You're free to change text/colors/language here.     #\n");
            content.append("# Supports both '§' and '&' characters for colors.                      Ribesg #\n");
            content.append("################################################################################\n\n");
            for (final Message m : getMessagesMap().values()) {
                content.append("# Default value    : " + m.getDefaultMessage() + '\n');
                content.append("# Awaited arguments: " + m.getAwaitedArgsString() + '\n');
                content.append(m.getId().name() + ": \"" + (m.getConfigMessage() != null ? m.getConfigMessage() : m.getDefaultMessage()) + "\"\n\n");
            }
            writer.write(content.toString());
        }
    }

    // Args have to be in the same order than in Message.getAwaitedArgs()
    public static String[] get(final MessageId id, final String... args) {
        try {
            final Message m = getMessagesMap().get(id);
            if (args != null && args.length != m.getAwaitedArgsNb() || args == null && m.getAwaitedArgsNb() > 0) {
                throw new IllegalArgumentException("Call to AbstractMessages.get(id,args...) with wrong number of args : " + (args == null ? 0 : args.length) + " (awaited : " + m.getAwaitedArgsNb() + ")");
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

package fr.ribesg.bukkit.nchat.lang;

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
        actionCancelledByCuboid,
        noPermissionForCommand,
        cmdOnlyAvailableForPlayers,

        // PlayerStickListener
        blockInSelection,
        blockNotInSelection,
        blockNotProtected,
        blockProtectedMultipleCuboids,
        blockProtectedOneCuboid,
        firstPointSelected,
        noSelection,
        secondPointSelected,
        selectionReset,

        // Command - RELOAD
        cmdReloadConfig,
        cmdReloadCuboids,
        cmdReloadMessages,

        // Command - CREATE
        cmdCreateAlreadyExists,
        cmdCreateCreated,
        cmdCreateNoValidSelection,

        // Command - DELETE
        cmdDeleteDoesNotExist,
        cmdDeleteDeleted,
        cmdDeleteNoPermission,

    }

    public static final String                         LINE_SEPARATOR = "%%";
    public static final String                         MESSAGE_HEADER = "§0§l[§c§lN§6§lCuboid§0§l] §f";
    public static final Charset                        CHARSET        = Charset.defaultCharset();

    @Getter private static EnumMap<MessageId, Message> messagesMap;                                    // Id ; Message

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
        newMessages.add(new Message(MessageId.actionCancelledByCuboid, "&cAction cancelled by the cuboid %cuboid%", new String[] { "%cuboid%" }, null));
        newMessages.add(new Message(MessageId.noPermissionForCommand, "&cYou do not have the permission to use that command", null, null));
        newMessages.add(new Message(MessageId.cmdOnlyAvailableForPlayers, "&cThis command is only available in game", null, null));

        // PlayerStickListener
        newMessages.add(new Message(MessageId.firstPointSelected, "&aFirst point selected : %coords%", new String[] { "%coords%" }, null));
        newMessages.add(new Message(MessageId.secondPointSelected, "&aSecond point selected : %coords%%%&aSelection Size : %size%", new String[] { "%coords%", "%size%" }, null));
        newMessages.add(new Message(MessageId.blockInSelection, "&aThis block is in your selection", null, null));
        newMessages.add(new Message(MessageId.blockNotInSelection, "&cThis block is not in your selection", null, null));
        newMessages.add(new Message(MessageId.blockNotProtected, "&aThis block is not protected", null, null));
        newMessages.add(new Message(MessageId.blockProtectedOneCuboid, "&cThis block is protected by one cuboid:" + LINE_SEPARATOR + "%cuboidInfo%", new String[] { "%cuboidInfo%" }, null));
        newMessages.add(new Message(MessageId.blockProtectedMultipleCuboids, "&cThis block is protected by %nb% cuboids:" + LINE_SEPARATOR + "%cuboidsInfos%", new String[] { "%nb%", "%cuboidsInfos%" }, null));
        newMessages.add(new Message(MessageId.selectionReset, "&aYour selection has been reset", null, null));
        newMessages.add(new Message(MessageId.noSelection, "&cYou have no selection to reset", null, null));

        // Command - RELOAD
        newMessages.add(new Message(MessageId.cmdReloadCuboids, "&aCuboids reloaded !", null, null));
        newMessages.add(new Message(MessageId.cmdReloadConfig, "&aConfig reloaded !", null, null));
        newMessages.add(new Message(MessageId.cmdReloadMessages, "&aMessages reloaded !", null, null));

        // Command - CREATE
        newMessages.add(new Message(MessageId.cmdCreateAlreadyExists, "&cThis cuboid already exists !", null, null));
        newMessages.add(new Message(MessageId.cmdCreateCreated, "&aYou created the cuboid &6%cuboidName% &a!", new String[] { "%cuboidName%" }, null));
        newMessages.add(new Message(MessageId.cmdCreateNoValidSelection, "&cYou need a vallid selection to create a cuboid !", null, null));

        // Command - DELETE
        newMessages.add(new Message(MessageId.cmdDeleteDoesNotExist, "&cThis cuboid does not exist !", null, null));
        newMessages.add(new Message(MessageId.cmdDeleteDeleted, "&aThe cuboid &6%cuboidName% &ahas been deleted !", new String[] { "%cuboidName%" }, null));
        newMessages.add(new Message(MessageId.cmdDeleteNoPermission, "&cYou do not have the permission to delete &6%cuboidName% &c!", new String[] { "%cuboidName%" }, null));

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
            content.append("# List of NCuboid messages. You're free to change text/colors/language here.   #\n");
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
                throw new IllegalArgumentException("Call to Messages.get(id,args...) with wrong number of args : " + (args == null ? 0 : args.length) + " (awaited : " + m.getAwaitedArgsNb() + ")");
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

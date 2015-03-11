/***************************************************************************
 * Project file:    NPlugins - NCore - Chat.java                           *
 * Full Class name: fr.ribesg.bukkit.api.chat.Chat                         *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.api.chat;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

/**
 * Holds static methods to use the Chat API.
 *
 * This class allows plugins to use the Chat API by replicating
 * {@link Player#sendMessage(String)},
 * {@link Server#broadcast(String, String)} and
 * {@link Server#broadcastMessage(String)} while replacing the standard
 * {@link String} argument by a/some {@link Message} type argument.
 *
 * It builds the Mojangson matching the provided Message and send it using
 * the Vanilla {@code /tellraw} command.
 *
 * @author Ribesg
 * @see <a href="http://wiki.vg/Chat">MinecraftCoalition Wiki</a>
 */
public final class Chat {

    private static final Logger LOGGER = Logger.getLogger("Chat API");

    // ##################################################################### //
    // ##                        API Entry points                         ## //
    // ##################################################################### //

    /**
     * Sends the provided Mojangson String(s) to the provided
     * {@link Player}.
     *
     * @param to         the player to whom we will send the message(s)
     * @param mojangsons the message(s) to send
     *
     * @see Player#sendMessage(String)
     */
    public static void sendMessage(final Player to, final String... mojangsons) {
        Validate.notNull(to, "The 'to' argument should not be null");
        Validate.notEmpty(mojangsons, "Please provide at least one Mojangson String");
        Validate.noNullElements(mojangsons, "The 'mojangsons' argument should not contain null values");
        for (final String mojangson : mojangsons) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + to.getName() + ' ' + mojangson);
        }
    }

    /**
     * Broadcasts the provided Mojangson String(s) to
     * {@link Player Players} having the provided permission.
     *
     * @param permission a permission required to receive the message(s)
     * @param mojangsons the message(s) to send
     *
     * @see Server#broadcast(String, String)
     */
    public static void broadcast(final String permission, final String... mojangsons) {
        Validate.notEmpty(permission, "The 'permission' argument should not be null nor empty");
        Validate.notEmpty(mojangsons, "Please provide at least one Mojangson String");
        Validate.noNullElements(mojangsons, "The 'mojangsons' argument should not contain null values");
        for (final String mojangson : mojangsons) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(permission)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + ' ' + mojangson);
                }
            }
        }
    }

    /**
     * Broadcasts the provided Mojangson String(s) to
     * {@link Player Players} having the default
     * {@link Server#BROADCAST_CHANNEL_USERS} permission.
     *
     * @param mojangsons the message(s) to send
     *
     * @see Server#broadcastMessage(String)
     */
    public static void broadcastMessage(final String... mojangsons) {
        Chat.broadcast(Server.BROADCAST_CHANNEL_USERS, mojangsons);
    }

    /**
     * Sends the provided {@link Message Message(s)} to the provided
     * {@link Player}.
     *
     * @param to       the player to whom we will send the message(s)
     * @param messages the message(s) to send
     *
     * @see Player#sendMessage(String)
     */
    public static void sendMessage(final Player to, final Message... messages) {
        Validate.notNull(to, "The 'to' argument should not be null");
        Validate.notEmpty(messages, "Please provide at least one Message");
        Validate.noNullElements(messages, "The 'messages' argument should not contain null values");
        final String[] mojangsons = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            mojangsons[i] = Chat.toMojangson(messages[i]);
        }
        Chat.sendMessage(to, mojangsons);
    }

    /**
     * Broadcasts the provided {@link Message Message(s)} to
     * {@link Player Players} having the provided permission.
     *
     * @param permission a permission required to receive the message(s)
     * @param messages   the message(s) to send
     *
     * @see Server#broadcast(String, String)
     */
    public static void broadcast(final String permission, final Message... messages) {
        Validate.notEmpty(permission, "The 'permission' argument should not be null nor empty");
        Validate.notEmpty(messages, "Please provide at least one Message");
        Validate.noNullElements(messages, "The 'messages' argument should not contain null values");
        final String[] mojangsons = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            mojangsons[i] = Chat.toMojangson(messages[i]);
        }
        Chat.broadcast(permission, mojangsons);
    }

    /**
     * Broadcasts the provided {@link Message Message(s)} to
     * {@link Player Players} having the default
     * {@link Server#BROADCAST_CHANNEL_USERS} permission.
     *
     * @param messages the message(s) to send
     *
     * @see Server#broadcastMessage(String)
     */
    public static void broadcastMessage(final Message... messages) {
        Chat.broadcast(Server.BROADCAST_CHANNEL_USERS, messages);
    }

    // ##################################################################### //
    // ##             Converting Message objects to Mojangson             ## //
    // ##################################################################### //

    private static int    cachedMessageHash  = -1;
    private static String cachedMessageValue = "";

    /**
     * Converts a {@link Message} to a Mojangson Chat String.
     *
     * @param message the message
     *
     * @return a Mojangson String matching the provided Message
     */
    private static String toMojangson(final Message message) {
        final int hash = message.hashCode();
        if (Chat.cachedMessageHash == hash) {
            Chat.LOGGER.info("DEBUG Returning cached Message");
            return Chat.cachedMessageValue;
        } else {
            int extraLevel = 1;
            final StringBuilder result = new StringBuilder();
            result.append("{\"text\":\"\",\"extra\":[");
            for (final Part part : message) {
                extraLevel = Chat.appendPart(result, part, extraLevel);
            }
            for (int i = 0; i < extraLevel; i++) {
                result.append("]}");
            }
            final String resultString = result.toString();
            Chat.LOGGER.info("DEBUG Converted Message to " + resultString);
            Chat.cachedMessageHash = hash;
            Chat.cachedMessageValue = resultString;
            return resultString;
        }
    }

    /**
     * Converts a {@link Part} to a Mojangson Chat 'extra' String and appends
     * it to the provided StringBuilder.
     *
     * @param builder    a StringBuilder
     * @param part       a Part
     * @param extraLevel the amount of extra levels added to the Mojangson
     *
     * @return the updated amount of extra levels
     */
    private static int appendPart(final StringBuilder builder, final Part part, int extraLevel) {
        builder.append('{');
        if (part.isLocalizedText()) {
            builder.append("\"translate\":\"")
                   .append(Chat.escapeString(part.getText()))
                   .append('"');
            final String[] parameters = part.getLocalizedTextParameters();
            if (parameters != null) {
                builder.append(',').append("\"with\":[");
                for (int i = 0; i < parameters.length; i++) {
                    builder.append('"')
                           .append(Chat.escapeString(parameters[i]))
                           .append('"');
                    if (i != parameters.length - 1) {
                        builder.append(',');
                    }
                }
                builder.append(']');
            }
        } else {
            final String text = part.getText();
            Chat.appendClick(builder, part.getClickAction());
            Chat.appendHover(builder, part.getHover(), text == null);
            extraLevel = Chat.appendText(builder, text, extraLevel);
        }
        builder.append('}');
        return extraLevel;
    }

    /**
     * Converts a String representing standard Minecraft formatted text to
     * a Mojangson Chat 'extra' String and append it to the provided
     * StringBuilder.
     *
     * @param builder    a StringBuilder
     * @param text       a text
     * @param extraLevel the amount of extra levels added to the Mojangson
     *
     * @return the updated amount of extra levels
     */
    private static int appendText(final StringBuilder builder, final String text, int extraLevel) {
        if (builder.charAt(builder.length() - 1) != '{') {
            builder.append(',');
        }
        if (text.contains("http") || text.indexOf(ChatColor.COLOR_CHAR) != -1) {
            builder.append("\"text\":\"\",\"extra\":[");
            ++extraLevel;
            int httpIndex, colorIndex, tmp;
            String remainingText = text, url;
            do {
                httpIndex = remainingText.indexOf("http");
                colorIndex = remainingText.indexOf(ChatColor.COLOR_CHAR);
                if (httpIndex != -1 && (colorIndex == -1 || httpIndex < colorIndex)) {
                    // The link is the first thing in the String
                    if (httpIndex != 0) {
                        builder.append("{\"text\":\"")
                               .append(Chat.escapeString(remainingText.substring(0, httpIndex)))
                               .append("\",\"extra\":[");
                        remainingText = remainingText.substring(httpIndex);
                    }
                    tmp = remainingText.indexOf(' ');
                    tmp = tmp == -1 ? remainingText.length() : tmp;
                    url = remainingText.substring(0, tmp);
                    builder.append("{\"text\":\"")
                           .append(Chat.escapeString(url))
                           .append('"');
                    Chat.appendClick(builder, Click.ofOpenUrl(url));
                    builder.append('}');
                    if (httpIndex != 0) {
                        builder.append("]}");
                    }
                    remainingText = remainingText.substring(url.length());
                } else if (colorIndex != -1 && (httpIndex == -1 || colorIndex < httpIndex)) {
                    // The color change is the first thing in the String
                    if (colorIndex != 0) {
                        builder.append("{\"text\":\"")
                               .append(Chat.escapeString(remainingText.substring(0, colorIndex)))
                               .append("\",\"extra\":[");
                        ++extraLevel;
                        remainingText = remainingText.substring(colorIndex);
                    }
                    try {
                        builder.append("{\"text\":\"\",\"color\":\"")
                               .append(Chat.getColorString(remainingText.charAt(1)))
                               .append("\",\"extra\":[");
                    } catch (final IndexOutOfBoundsException e) {
                        throw new IllegalArgumentException("Malformed input: incomplete color code", e);
                    }
                    ++extraLevel;
                    remainingText = remainingText.substring(2);
                } else {
                    // Can't be at the same place: they're both equal to -1. Just append everything left.
                    builder.append('"').append(Chat.escapeString(remainingText)).append('"');
                    remainingText = "";
                }
                if (!remainingText.isEmpty()) {
                    builder.append(',');
                }
            } while (!remainingText.isEmpty());
        } else {
            builder.append("\"text\":\"").append(Chat.escapeString(text)).append('"');
        }
        return extraLevel;
    }

    private static void appendClick(final StringBuilder builder, final Click clickAction) {
        if (clickAction != null) {
            if (builder.charAt(builder.length() - 1) != '{') {
                builder.append(',');
            }
            builder.append("\"clickEvent\":{\"value\":\"")
                   .append(Chat.escapeString(clickAction.getText()))
                   .append("\",\"action\":\"");
            switch (clickAction.getType()) {
                case OPEN_URL:
                    builder.append("open_url");
                    break;
                case SEND_TEXT:
                    builder.append("run_command");
                    break;
                case SET_TEXT:
                    builder.append("suggest_command");
                    break;
            }
            builder.append("\"}");
        }
    }

    private static void appendHover(final StringBuilder builder, final Hover hover, final boolean noText) {
        if (hover != null) {
            if (builder.charAt(builder.length() - 1) != '{') {
                builder.append(',');
            }
            builder.append("\"hoverEvent\":{\"value\":\"");
            switch (hover.getType()) {
                case SHOW_ACHIEVEMENT:
                    builder.append(Chat.getAchievementId(hover.getAchievement()))
                           .append("\",\"action\":\"show_achievement\"}");
                    if (noText) {
                        // FIXME Append achievement name as "translate" (How?)
                        builder.append("\"text\":\"").append(hover.getAchievement().name()).append('"');
                    }
                    break;
                case SHOW_ITEM:
                    Chat.appendItem(builder, hover.getItem());
                    builder.append("\",\"action\":\"show_item\"}");
                    if (noText) {
                        // FIXME Append item name as "translate" (How?)
                        builder.append("\"text\":\"").append(hover.getItem().getType()).append('"');
                    }
                    break;
                case SHOW_TEXT:
                    builder.append(Chat.escapeString(StringUtils.join(hover.getText(), '\n')));
                    builder.append("\",\"action\":\"show_text\"}");
                    break;
            }
        }
    }

    private static void appendItem(final StringBuilder builder, final ItemStack is) {
        builder.append('{')
               .append("Count:").append(is.getAmount()).append(',')
               .append("Damage:").append(is.getDurability()).append(',')
               .append("id:").append(is.getType().getId());
        Chat.appendItemTag(builder, is);
        builder.append('}');
    }

    private static void appendItemTag(final StringBuilder builder, final ItemStack is) {
        boolean hasTag = false;
        final StringBuilder tagBuilder = new StringBuilder();

        // Enchantments
        final Map<Enchantment, Integer> enchantments = is.getEnchantments();
        if (enchantments != null && !enchantments.isEmpty()) {
            tagBuilder.append("ench:[");
            final Iterator<Entry<Enchantment, Integer>> it = enchantments.entrySet().iterator();
            while (it.hasNext()) {
                final Entry<Enchantment, Integer> entry = it.next();
                tagBuilder.append("{id:")
                          .append(entry.getKey().getId())
                          .append(",lvl:")
                          .append(entry.getValue());
                if (it.hasNext()) {
                    tagBuilder.append(',');
                }
            }
            tagBuilder.append("],");
            hasTag = true;
        }

        // Meta
        if (is.hasItemMeta()) {
            final ItemMeta meta = is.getItemMeta();
            if (meta.hasDisplayName() || meta.hasLore() || Chat.isLeatherArmor(is)) {
                Chat.appendItemDisplay(tagBuilder, meta);
            }
            if (is.getType() == Material.POTION) {
                Chat.appendItemPotion(tagBuilder, (PotionMeta)meta);
            }
            if (is.getType() == Material.WRITTEN_BOOK) {
                Chat.appendItemBook(tagBuilder, (BookMeta)meta);
            }
            if (is.getType() == Material.SKULL_ITEM) {
                Chat.appendItemSkull(tagBuilder, (SkullMeta)meta);
            }
            if (is.getType() == Material.FIREWORK) { // Firework Rocket
                Chat.appendItemFirework(tagBuilder, (FireworkMeta)meta);
            }
            if (is.getType() == Material.FIREWORK_CHARGE) { // Firework Star
                Chat.appendItemFireworkEffect(tagBuilder, (FireworkEffectMeta)meta);
            }
        }

        if (hasTag && tagBuilder.charAt(builder.length() - 1) == ',') {
            tagBuilder.deleteCharAt(builder.length() - 1);
        }

        // Append to main builder
        if (hasTag) {
            builder.append(',').append("tag:{").append(tagBuilder).append('}');
        }
    }

    private static void appendItemDisplay(final StringBuilder builder, final ItemMeta meta) {
        builder.append("display{");
        if (meta.hasDisplayName()) {
            builder.append("Name:");
            builder.append(Chat.escapeString(meta.getDisplayName()));
            builder.append(',');
        }
        if (meta.hasLore()) {
            builder.append("Lore:[");
            final Iterator<String> it = meta.getLore().iterator();
            while (it.hasNext()) {
                builder.append(Chat.escapeString(it.next()));
                if (it.hasNext()) {
                    builder.append(',');
                }
            }
            builder.append("],");
        }
        if (meta instanceof LeatherArmorMeta) {
            final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)meta;
            builder.append("color:").append(leatherArmorMeta.getColor().asRGB());
        }
        if (builder.charAt(builder.length() - 1) == ',') {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("},");
    }

    private static void appendItemPotion(final StringBuilder builder, final PotionMeta meta) {
        if (meta.hasCustomEffects()) {
            builder.append("CustomPotionEffects:[");
            final Iterator<PotionEffect> it = meta.getCustomEffects().iterator();
            while (it.hasNext()) {
                final PotionEffect effect = it.next();
                builder.append("{Id:")
                       .append(effect.getType().getId())
                       .append(",Amplifier:")
                       .append(effect.getAmplifier())
                       .append(",Duration:")
                       .append(effect.getDuration())
                       .append(",Ambient:")
                       .append(effect.isAmbient() ? 1 : 0)
                       .append('}');
                if (it.hasNext()) {
                    builder.append(',');
                }
            }
            builder.append("],");
        }
    }

    private static void appendItemBook(final StringBuilder builder, final BookMeta meta) {
        // TODO
    }

    private static void appendItemSkull(final StringBuilder builder, final SkullMeta meta) {
        // TODO
    }

    private static void appendItemFirework(final StringBuilder builder, final FireworkMeta meta) {
        // TODO
    }

    private static void appendItemFireworkEffect(final StringBuilder builder, final FireworkEffectMeta meta) {
        // TODO
    }

    private static void appendItemMap(final StringBuilder builder, final MapMeta meta) {
        // TODO
    }

    /**
     * Gets a Mojangson color String based on a color character.
     *
     * @param colorChar a color char
     *
     * @return a Mojangson color String
     */
    private static String getColorString(final char colorChar) {
        final ChatColor color = ChatColor.getByChar(colorChar);
        if (color == null) {
            throw new IllegalArgumentException("Invalid color char: " + colorChar);
        } else {
            switch (color) {
                case MAGIC:
                    return "obfuscated";
                default:
                    return color.name().toLowerCase();
            }
        }
    }

    /**
     * Gets the String identifier of an {@link Achievement} that the client
     * can understand.
     *
     * @param achievement the achievement
     *
     * @return the achievement's identifier
     */
    private static String getAchievementId(final Achievement achievement) {
        switch (achievement) {
            case BUILD_WORKBENCH:
                return "buildWorkBench";
            case GET_DIAMONDS:
                return "diamonds";
            case NETHER_PORTAL:
                return "portal";
            case GHAST_RETURN:
                return "ghast";
            case GET_BLAZE_ROD:
                return "blazeRod";
            case BREW_POTION:
                return "potion";
            case END_PORTAL:
                return "theEnd";
            case THE_END:
                return "theEnd2";
            default:
                final char[] chars = achievement.name().toLowerCase().toCharArray();
                for (int i = 0; i < chars.length - 1; i++) {
                    if (chars[i] == '_') {
                        i++;
                        chars[i] = Character.toTitleCase(chars[i]);
                    }
                }
                final String result = new String(chars);
                return "achievement." + result.replace("_", "");
        }
    }

    /**
     * Escapes a String for JSON compatibility.
     *
     * @param input a String
     *
     * @return the same String, escaped
     */
    private static String escapeString(final String input) {
        return input.replace("\"", "\\\"");
    }

    /**
     * Checks if the provided {@link ItemStack}'s {@link Material} is one of
     * <ul>
     * <li>{@link Material#LEATHER_BOOTS}
     * <li>{@link Material#LEATHER_CHESTPLATE}
     * <li>{@link Material#LEATHER_HELMET}
     * <li>{@link Material#LEATHER_LEGGINGS}
     * </ul>
     *
     * @param is the ItemStack
     *
     * @return true if the ItemStack represents a Leather Armor part
     */
    private static boolean isLeatherArmor(final ItemStack is) {
        switch (is.getType()) {
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                return true;
            default:
                return false;
        }
    }

    // ##################################################################### //

    /**
     * This class shouldn't be instantiated.
     */
    private Chat() {}
}

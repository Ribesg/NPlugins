/***************************************************************************
 * Project file:    NPlugins - NCore - Message.java                        *
 * Full Class name: fr.ribesg.bukkit.api.chat.Message                      *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.api.chat;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.bukkit.Achievement;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a rich Message which can be sent to Players.
 * This is an API for Minecraft 1.7 "Json" chat messages features.
 */
@SerializableAs("ChatMessage")
public final class Message implements Iterable<Part>, ConfigurationSerializable {

    /**
     * Builds a Message from zero (empty Message), one or more Parts.
     *
     * @param parts parts of the Message, cannot be null and cannot contain null
     *              elements
     *
     * @return the built Message
     */
    public static Message of(final Part... parts) {
        Validate.notNull(parts, "Parts cannot be null!");
        final Message result = new Message();
        for (final Part part : parts) {
            result.append(part);
        }
        return result;
    }

    /**
     * Builds a Message with a text as first Part.
     *
     * @param string the text
     *
     * @return the built Message
     */
    public static Message of(final String string) {
        return Message.of(Part.of(string));
    }

    /**
     * Builds a Message with a localized text as first Part.
     *
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final String id, final String... parameters) {
        return Message.of(Part.of(id, parameters));
    }

    /**
     * Builds a Message with a text as first Part, with one or more lines of
     * hover text.
     *
     * @param text      the text
     * @param hoverText the hover text
     *
     * @return the built Message
     */
    public static Message of(final String text, final String... hoverText) {
        return Message.of(Part.of(text, hoverText));
    }

    /**
     * Builds a Message with an hover text and a localized text as first Part.
     *
     * @param hoverText  the hover text, one or more lines
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final String[] hoverText, final String id, final String... parameters) {
        return Message.of(Part.ofLocalized(hoverText, id, parameters));
    }

    /**
     * Builds a Message with an ItemStack as first Part.
     *
     * @param item the itemstack
     *
     * @return the built Message
     */
    public static Message of(final ItemStack item) {
        return Message.of(Part.of(item));
    }

    /**
     * Builds a Message with a text as first Part, using an ItemStack
     * description as hover text.
     *
     * @param item the itemstack
     * @param text the text
     *
     * @return the built Message
     */
    public static Message of(final ItemStack item, final String text) {
        return Message.of(Part.of(item, text));
    }

    /**
     * Builds a Message with a localized text as first Part, using an
     * ItemStack description as hover text.
     *
     * @param item       the itemstack
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final ItemStack item, final String id, final String... parameters) {
        return Message.of(Part.ofLocalized(item, id, parameters));
    }

    /**
     * Builds a Message with an Achievement as first Part.
     *
     * @param achievement the achievement
     *
     * @return the built Message
     */
    public static Message of(final Achievement achievement) {
        return Message.of(Part.of(achievement));
    }

    /**
     * Builds a Message with a text as first Part, using an Achievement
     * description as hover text.
     *
     * @param achievement the achievement
     * @param text        the text
     *
     * @return the built Message
     */
    public static Message of(final Achievement achievement, final String text) {
        return Message.of(Part.of(achievement, text));
    }

    /**
     * Builds a Message with a localized text as first Part, using an
     * Achievement description as hover text.
     *
     * @param achievement the achievement
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final Achievement achievement, final String id, final String... parameters) {
        return Message.of(Part.ofLocalized(achievement, id, parameters));
    }

    /**
     * Builds a Message with a text as first Part, linking the provided Click
     * action to it.
     *
     * @param clickAction the click action
     * @param text        the text
     *
     * @return the built Message
     */
    public static Message of(final Click clickAction, final String text) {
        return Message.of(Part.of(clickAction, text));
    }

    /**
     * Builds a Message with a localized text as first Part, linking the
     * provided Click action to it.
     *
     * @param clickAction the click action
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final Click clickAction, final String id, final String... parameters) {
        return Message.of(Part.ofLocalized(clickAction, id, parameters));
    }

    /**
     * Builds a Message with a text as first Part, with one or more lines of
     * hover text, linking the provided Click action to it.
     *
     * @param clickAction the click action
     * @param text        the text
     * @param hoverText   the hover text
     *
     * @return the built Message
     */
    public static Message of(final Click clickAction, final String text, final String... hoverText) {
        return Message.of(Part.of(clickAction, text, hoverText));
    }

    /**
     * Builds a Message with a localized text as first Part, with one or more
     * lines of hover text, linking the provided Click action to it.
     *
     * @param clickAction the click action
     * @param hoverText   the hover text, one or more lines
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final Click clickAction, final String[] hoverText, final String id, final String... parameters) {
        return Message.of(Part.ofLocalized(clickAction, hoverText, id, parameters));
    }

    /**
     * Builds a Message with an ItemStack as first Part, linking the provided
     * Click action to it.
     *
     * @param clickAction the click action
     * @param item        the itemstack
     *
     * @return the built Message
     */
    public static Message of(final Click clickAction, final ItemStack item) {
        return Message.of(Part.of(clickAction, item));
    }

    /**
     * Builds a Message with a text as first Part, using an ItemStack as
     * hover text, linking the provided Click action to it.
     *
     * @param clickAction the click action
     * @param item        the itemstack
     * @param text        the text
     *
     * @return the built Message
     */
    public static Message of(final Click clickAction, final ItemStack item, final String text) {
        return Message.of(Part.of(clickAction, item, text));
    }

    /**
     * Builds a Message with a localized text as first Part, using an
     * ItemStack as hover text, linking the provided Click action to it.
     *
     * @param clickAction the click action
     * @param item        the itemstack
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final Click clickAction, final ItemStack item, final String id, final String... parameters) {
        return Message.of(Part.ofLocalized(clickAction, item, id, parameters));
    }

    /**
     * Builds a Message with an Achievement as first Part, linking the
     * provided Click action to it.
     *
     * @param clickAction the click action
     * @param achievement the achievement
     *
     * @return the built Message
     */
    public static Message of(final Click clickAction, final Achievement achievement) {
        return Message.of(Part.of(clickAction, achievement));
    }

    /**
     * Builds a Message with a text as first Part, using an Achievement as
     * hover text, linking the provided Click action to it.
     *
     * @param clickAction the click action
     * @param achievement the achievement
     * @param text        the text
     *
     * @return the built Message
     */
    public static Message of(final Click clickAction, final Achievement achievement, final String text) {
        return Message.of(Part.of(clickAction, achievement, text));
    }

    /**
     * Builds a Message with a localized text as firstPart, using an
     * Achievement as hover text, linking the provided Click action to it.
     *
     * @param clickAction the click action
     * @param achievement the achievement
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return the built Message
     */
    public static Message ofLocalized(final Click clickAction, final Achievement achievement, final String id, final String... parameters) {
        return Message.of(Part.ofLocalized(clickAction, achievement, id, parameters));
    }

    /**
     * The Parts of this Message
     */
    private final List<Part> parts;

    /**
     * Private constructor, Message instances should be built using available
     * static constructors.
     */
    private Message() {
        this.parts = new ArrayList<Part>();
    }

    /**
     * Appends all Parts of the provided Message to this Message.
     *
     * @param message another Message
     *
     * @return this Message for chain calls
     */
    public Message append(final Message message) {
        Validate.notNull(message, "Message cannot be null!");
        this.parts.addAll(message.parts);
        return this;
    }

    /**
     * Appends the provided Part to this Message.
     *
     * @param part the Part
     *
     * @return this Message for chain calls
     */
    public Message append(final Part part) {
        Validate.notNull(part, "Part cannot be null!");
        this.parts.add(part);
        return this;
    }

    /**
     * Appends a Part built from the provided text.
     *
     * @param text the text
     *
     * @return this Message for chain calls
     */
    public Message append(final String text) {
        this.append(Part.of(text));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text.
     *
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final String id, final String... parameters) {
        this.append(Part.ofLocalized(id, parameters));
        return this;
    }

    /**
     * Appends a Part built from the provided text and hover text.
     *
     * @param text      the text
     * @param hoverText the hover text
     *
     * @return this Message for chain calls
     */
    public Message append(final String text, final String... hoverText) {
        this.append(Part.of(text, hoverText));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text and hover text.
     *
     * @param hoverText  the hover text, one or more lines
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final String[] hoverText, final String id, final String... parameters) {
        this.append(Part.ofLocalized(hoverText, id, parameters));
        return this;
    }

    /**
     * Appends a Part built from the provided ItemStack.
     *
     * @param item the itemstack
     *
     * @return this Message for chain calls
     */
    public Message append(final ItemStack item) {
        this.append(Part.of(item));
        return this;
    }

    /**
     * Appends a Part built from the provided text, using the provided
     * ItemStack description as hover text.
     *
     * @param item the itemstack
     * @param text the text
     *
     * @return this Message for chain calls
     */
    public Message append(final ItemStack item, final String text) {
        this.append(Part.of(item, text));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text, using the
     * provided ItemStack description as hover text.
     *
     * @param item       the itemstack
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final ItemStack item, final String id, final String... parameters) {
        this.append(Part.ofLocalized(item, id, parameters));
        return this;
    }

    /**
     * Appends a Part built from the provided Achievement.
     *
     * @param achievement the achievement
     *
     * @return this Message for chain calls
     */
    public Message append(final Achievement achievement) {
        this.append(Part.of(achievement));
        return this;
    }

    /**
     * Appends a Part built from the provided text, using the provided
     * Achievement description as hover text.
     *
     * @param achievement the achievement
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message append(final Achievement achievement, final String text) {
        this.append(Part.of(achievement, text));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text, using the
     * provided Achievement description as hover text.
     *
     * @param achievement the achievement
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final Achievement achievement, final String id, final String... parameters) {
        this.append(Part.ofLocalized(achievement, id, parameters));
        return this;
    }

    /**
     * Appends a Part built from the provided text, linking the provided
     * Click action to it.
     *
     * @param clickAction the click action
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message append(final Click clickAction, final String text) {
        this.append(Part.of(clickAction, text));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text, linking the
     * provided Click action to it.
     *
     * @param clickAction the click action
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final Click clickAction, final String id, final String... parameters) {
        this.append(Part.ofLocalized(clickAction, id, parameters));
        return this;
    }

    /**
     * Appends a Part built from the provided text and hover text, linking the
     * provided Click action to it.
     *
     * @param clickAction the click action
     * @param text        the text
     * @param hoverText   the hover text
     *
     * @return this Message for chain calls
     */
    public Message append(final Click clickAction, final String text, final String... hoverText) {
        this.append(Part.of(clickAction, text, hoverText));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text and hover text,
     * linking the provided Click action to it.
     *
     * @param clickAction the click action
     * @param hoverText   the hover text, one or more lines
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final Click clickAction, final String[] hoverText, final String id, final String... parameters) {
        this.append(Part.ofLocalized(clickAction, hoverText, id, parameters));
        return this;
    }

    /**
     * Appends a Part built from the provided ItemStack, linking the provided
     * Click action to it.
     *
     * @param clickAction the click action
     * @param item        the itemstack
     *
     * @return this Message for chain calls
     */
    public Message append(final Click clickAction, final ItemStack item) {
        this.append(Part.of(clickAction, item));
        return this;
    }

    /**
     * Appends a Part built from the provided text, using the provided
     * ItemStack description as hover text, linking the provided Click action
     * to it.
     *
     * @param clickAction the click action
     * @param item        the itemstack
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message append(final Click clickAction, final ItemStack item, final String text) {
        this.append(Part.of(clickAction, item, text));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text, using the
     * provided ItemStack description as hover text, linking the provided
     * Click action to it.
     *
     * @param clickAction the click action
     * @param item        the itemstack
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final Click clickAction, final ItemStack item, final String id, final String... parameters) {
        this.append(Part.ofLocalized(clickAction, item, id, parameters));
        return this;
    }

    /**
     * Appends a Part built from the provided Achievement, linking the
     * provided Click action to it.
     *
     * @param clickAction the click action
     * @param achievement the achievement
     *
     * @return this Message for chain calls
     */
    public Message append(final Click clickAction, final Achievement achievement) {
        this.append(Part.of(clickAction, achievement));
        return this;
    }

    /**
     * Appends a Part built from the provided text, using the provided
     * Achievement description as hover text, linking the provided Click
     * action to it.
     *
     * @param clickAction the click action
     * @param achievement the achievement
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message append(final Click clickAction, final Achievement achievement, final String text) {
        this.append(Part.of(clickAction, achievement, text));
        return this;
    }

    /**
     * Appends a Part built from the provided localized text, using the
     * provided Achievement description as hover text, linking the provided
     * Click action to it.
     *
     * @param clickAction the click action
     * @param achievement the achievement
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message appendLocalized(final Click clickAction, final Achievement achievement, final String id, final String... parameters) {
        this.append(Part.ofLocalized(clickAction, achievement, id, parameters));
        return this;
    }

    /**
     * Inserts a Part at the provided position in this Message.
     *
     * @param pos  the position
     * @param part the Part
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Part part) {
        Validate.notNull(part, "Part cannot be null!");
        this.parts.add(pos, part);
        return this;
    }

    /**
     * Inserts a Part built from the provided text at the provided position
     * in this Message.
     *
     * @param pos  the position
     * @param text the text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final String text) {
        this.insert(pos, Part.of(text));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text at the provided
     * position in this Message.
     *
     * @param pos        the position
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(id, parameters));
        return this;
    }

    /**
     * Inserts a Part built from the provided text and hover text at the
     * provided position in this Message.
     *
     * @param pos       the position
     * @param text      the text
     * @param hoverText the hover text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final String text, final String... hoverText) {
        this.insert(pos, Part.of(text, hoverText));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text and hover text
     * at the provided position in this Message.
     *
     * @param pos        the position
     * @param hoverText  the hover text, one or more lines
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final String[] hoverText, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(hoverText, id, parameters));
        return this;
    }

    /**
     * Inserts a Part built from the provided ItemStack at the provided
     * position in this Message.
     *
     * @param pos  the position
     * @param item the itemstack
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final ItemStack item) {
        this.insert(pos, Part.of(item));
        return this;
    }

    /**
     * Inserts a Part built from the provided text, using the provided
     * ItemStack description as hover text, at the provided position in this
     * Message.
     *
     * @param pos  the position
     * @param item the itemstack
     * @param text the text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final ItemStack item, final String text) {
        this.insert(pos, Part.of(item, text));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text, using the
     * provided ItemStack description as hover text, at the provided position
     * in this Message.
     *
     * @param pos        the position
     * @param item       the itemstack
     * @param id         the localized text identifier
     * @param parameters the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final ItemStack item, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(item, id, parameters));
        return this;
    }

    /**
     * Inserts a Part built from the provided Achievement at the provided
     * position in this Message.
     *
     * @param pos         the position
     * @param achievement the achievement
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Achievement achievement) {
        this.insert(pos, Part.of(achievement));
        return this;
    }

    /**
     * Inserts a Part built from the provided text, using the provided
     * Achievement description as hover text, at the provided position in
     * this Message.
     *
     * @param pos         the position
     * @param achievement the achievement
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Achievement achievement, final String text) {
        this.insert(pos, Part.of(achievement, text));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text, using the
     * provided Achievement description as hover text, at the provided
     * position in this Message.
     *
     * @param pos         the position
     * @param achievement the achievement
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final Achievement achievement, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(achievement, id, parameters));
        return this;
    }

    /**
     * Inserts a Part built from the provided text, linking the provided
     * Click action to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Click clickAction, final String text) {
        this.insert(pos, Part.of(clickAction, text));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text, linking the
     * provided Click action to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final Click clickAction, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(clickAction, id, parameters));
        return this;
    }

    /**
     * Inserts a Part built from the provided text and hover text, linking
     * the provided Click action to it, at the provided position in this
     * Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param text        the text
     * @param hoverText   the hover text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Click clickAction, final String text, final String... hoverText) {
        this.insert(pos, Part.of(clickAction, text, hoverText));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text and hover text,
     * linking the provided Click action to it, at the provided position in
     * this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param hoverText   the hover text, one or more lines
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final Click clickAction, final String[] hoverText, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(clickAction, hoverText, id, parameters));
        return this;
    }

    /**
     * Inserts a Part built from the provided ItemStack, linking the provided
     * Click action to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param item        the itemstack
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Click clickAction, final ItemStack item) {
        this.insert(pos, Part.of(clickAction, item));
        return this;
    }

    /**
     * Inserts a Part built from the provided text, using the provided
     * ItemStack description as hover text, linking the provided Click action
     * to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param item        the itemstack
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Click clickAction, final ItemStack item, final String text) {
        this.insert(pos, Part.of(clickAction, item, text));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text, using the
     * provided ItemStack description as hover text, linking the provided
     * Click action to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param item        the itemstack
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final Click clickAction, final ItemStack item, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(clickAction, item, id, parameters));
        return this;
    }

    /**
     * Inserts a Part built from the provided Achievement, linking the
     * provided Click action to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param achievement the achievement
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Click clickAction, final Achievement achievement) {
        this.insert(pos, Part.of(clickAction, achievement));
        return this;
    }

    /**
     * Inserts a Part built from the provided text, using the provided
     * Achievement description as hover text, linking the provided Click
     * action to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param achievement the achievement
     * @param text        the text
     *
     * @return this Message for chain calls
     */
    public Message insert(final int pos, final Click clickAction, final Achievement achievement, final String text) {
        this.insert(pos, Part.of(clickAction, achievement, text));
        return this;
    }

    /**
     * Inserts a Part built from the provided localized text, using the
     * provided Achievement description as hover text, linking the provided
     * Click action to it, at the provided position in this Message.
     *
     * @param pos         the position
     * @param clickAction the click action
     * @param achievement the achievement
     * @param id          the localized text identifier
     * @param parameters  the localized text parameters, if any
     *
     * @return this Message for chain calls
     */
    public Message insertLocalized(final int pos, final Click clickAction, final Achievement achievement, final String id, final String... parameters) {
        this.insert(pos, Part.ofLocalized(clickAction, achievement, id, parameters));
        return this;
    }

    /**
     * Gets the Part at the specified position in this Message's Parts list.
     *
     * @param i the index of the wanted Part
     *
     * @return the Part at the specified position
     */
    public Part get(final int i) {
        return this.parts.get(i);
    }

    /**
     * Sets the Part at the specified position in this Message's Parts list.
     *
     * @param i    the index
     * @param part the Part
     *
     * @return this Message for chain calls
     */
    public Message set(final int i, final Part part) {
        Validate.notNull(part, "part can't be null");
        this.parts.set(i, part);
        return this;
    }

    @Override
    public ListIterator<Part> iterator() {
        return this.parts.listIterator();
    }

    /**
     * This implementation of toString is used to send Message to non-Player
     * CommandSender, like ConsoleCommandSender.
     *
     * @return a String representation of this Message
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final Part p : this.parts) {
            builder.append(p);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final Message parts1 = (Message)o;

        if (!this.parts.equals(parts1.parts)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return this.parts.hashCode();
    }

    @Override
    public Message clone() {
        final Message message = new Message();
        for (final Part part : this.parts) {
            message.append(part.clone());
        }
        return message;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>of(
                "parts", this.parts
        );
    }

    /**
     * Converts the given {@link Map} to a chat Message.
     *
     * @param map the map to convert to a chat Message
     *
     * @see ConfigurationSerializable
     */
    public static Message deserialize(final Map<String, Object> map) {
        final Object parts = map.get("parts");
        if (parts == null) {
            throw new IllegalArgumentException("Message parts are missing");
        }
        if (parts instanceof Part) {
            return new Message().append((Part)parts);
        } else if (parts instanceof Collection) {
            final Collection<?> collection = (Collection<?>)parts;
            final Message message = new Message();
            for (final Object part : collection) {
                if (part instanceof Part) {
                    message.append((Part)part);
                } else {
                    throw new IllegalArgumentException(part + " is not a valid Message Part");
                }
            }
            return message;
        } else {
            throw new IllegalArgumentException(parts + " is not a valid Message Part");
        }
    }
}

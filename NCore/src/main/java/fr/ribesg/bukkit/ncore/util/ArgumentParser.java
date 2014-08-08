/***************************************************************************
 * Project file:    NPlugins - NCore - ArgumentParser.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.util.ArgumentParser             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dumptruckman, Ribesg
 */
public class ArgumentParser {

    /**
     * Transform a Bukkit-provided args array, considering quotes in the original String.
     * Example:
     * - Input  = { a | 'b | c | d' | "e | f | g" | h"i | j" | "k | l'm | n" | 'o | p | q }
     * - Output = { a | b c d | e f g | h"i | j" | k l'm n | 'o | p | q }
     *
     * @param args The original Bukkit command args array
     *
     * @return a new command args array
     */
    public static String[] joinArgsWithQuotes(final String[] args) {
        return new ArgumentParser(args).joinArgsWithQuotes();
    }

    /**
     * Different states encountered while iterating over the args array
     */
    private static enum QuoteState {

        /**
         * Not inside a quoted String
         */
        NO_QUOTES,

        /**
         * Inside a single-quoted String
         */
        SINGLE_QUOTES,

        /**
         * Inside a double-quoted String
         */
        DOUBLE_QUOTES
    }

    /**
     * Used to store the input
     */
    private final String[] initialArgs;

    /**
     * Used to store the final result
     */
    private final List<String> parsedArgs;

    /**
     * Used to store the current quote state
     */
    private QuoteState quoteState;

    /**
     * Used to build the content of a quoted String as a single String
     */
    private final StringBuilder quotedArgsBuffer;

    /**
     * This buffer will contain, at any moment, any arg added to the quotedArgsBuffer.
     * This way, in case of non-ended quote String, those would still be inserted in the
     * parsedArgs result by {@link #dealWithAnyUnclosedQuotes()}
     */
    private final List<String> fallBackBuffer;

    /**
     * Builds the ArgumentParser. Initialize tools.
     *
     * @param args The arguments to parse.
     */
    private ArgumentParser(final String[] args) {
        this.initialArgs = args;
        this.parsedArgs = new ArrayList<>(this.initialArgs.length);
        this.fallBackBuffer = new ArrayList<>(this.initialArgs.length);
        this.quotedArgsBuffer = new StringBuilder();
    }

    /**
     * This method actually starts the work
     *
     * @return The final result of this ArgumentParser
     */
    private String[] joinArgsWithQuotes() {
        this.quoteState = QuoteState.NO_QUOTES;

        for (final String arg : this.initialArgs) {
            this.parseSingleArg(arg);
        }

        this.dealWithAnyUnclosedQuotes();

        return this.parsedArgs.toArray(new String[this.parsedArgs.size()]);
    }

    /**
     * Parse an arg differently if we're inside a quoted string or not.
     *
     * @param arg the arg to parse.
     */
    private void parseSingleArg(final String arg) {
        if (arg.isEmpty()) {
            return;
        }

        this.fallBackBuffer.add(arg);
        switch (this.quoteState) {
            case NO_QUOTES:
                this.parseUnquotedArg(arg);
                break;
            default:
                this.quotedArgsBuffer.append(' ');
                this.parseQuotedArg(arg);
                break;
        }
    }

    /**
     * Checks if this arg starts a new quoted string or not.
     *
     * @param arg the arg to parse
     */
    private void parseUnquotedArg(String arg) {
        final char firstChar = arg.charAt(0);
        if (firstChar == '\'') {
            this.quoteState = QuoteState.SINGLE_QUOTES;
            arg = this.removeInitialQuote(arg);
            this.parseQuotedArg(arg);
        } else if (firstChar == '\"') {
            this.quoteState = QuoteState.DOUBLE_QUOTES;
            arg = this.removeInitialQuote(arg);
            this.parseQuotedArg(arg);
        } else {
            this.parsedArgs.add(arg);
        }
    }

    /**
     * Removes the first char quote of the provided String.
     *
     * @param arg the arg to parse.
     *
     * @return the same arg without the first char.
     */
    private String removeInitialQuote(final String arg) {
        if (arg.length() > 1) {
            return arg.substring(1);
        }
        return "";
    }

    /**
     * Checks if this arg closes a quoted String.
     *
     * @param arg the arg to parse.
     */
    private void parseQuotedArg(String arg) {
        if (arg.isEmpty()) {
            return;
        }

        final char lastChar = arg.charAt(arg.length() - 1);
        if (this.charIsQuoteMatchingState(lastChar) && this.endQuoteIsNotEscaped(arg)) {
            arg = this.removeFinalQuote(arg);
            this.quotedArgsBuffer.append(arg);
            this.parsedArgs.add(this.quotedArgsBuffer.toString());
            this.resetQuotedArgTracking();
        } else {
            this.quotedArgsBuffer.append(arg);
        }
    }

    /**
     * Checks if this char closes the current quoted String.
     *
     * @param c the char to check.
     *
     * @return True if this char matches the char that opened the current quoted String, false otherwise.
     */
    private boolean charIsQuoteMatchingState(final char c) {
        return c == '\'' && this.quoteState == QuoteState.SINGLE_QUOTES || c == '\"' && this.quoteState == QuoteState.DOUBLE_QUOTES;
    }

    /**
     * Checks if the quote at the end of this arg is not escaped.
     *
     * @param arg the arg to parse.
     *
     * @return True if we should consider this final quote, false otherwise.
     */
    private boolean endQuoteIsNotEscaped(final String arg) {
        return arg.length() <= 1 || arg.charAt(arg.length() - 2) != '\\';
    }

    /**
     * Removes the final char of the provided String.
     *
     * @param arg the arg to parse.
     *
     * @return the same arg without the last char.
     */
    private String removeFinalQuote(final String arg) {
        if (arg.length() > 1) {
            return arg.substring(0, arg.length() - 1);
        }
        return "";
    }

    /**
     * Resets buffers and state
     */
    private void resetQuotedArgTracking() {
        this.quotedArgsBuffer.delete(0, this.quotedArgsBuffer.length());
        this.fallBackBuffer.clear();
        this.quoteState = QuoteState.NO_QUOTES;
    }

    /**
     * Consider all args that are in an un-closed quoted String as normal args.
     * Called once we checked all args.
     */
    private void dealWithAnyUnclosedQuotes() {
        if (this.quoteState != QuoteState.NO_QUOTES && !this.fallBackBuffer.isEmpty()) {
            this.parsedArgs.addAll(this.fallBackBuffer);
        }
    }
}
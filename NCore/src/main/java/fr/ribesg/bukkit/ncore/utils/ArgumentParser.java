/***************************************************************************
 * Project file:    NPlugins - NCore - ArgumentParser.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.ArgumentParser            *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import java.util.ArrayList;
import java.util.List;

/** @author dumptruckman, Ribesg */
public class ArgumentParser {

	/**
	 * Transform a Bukkit-provided args array, considering quotes in the original String.
	 * Example:
	 * - Input  = { a | 'b | c | d' | "e | f | g" | h"i | j" | "k | l'm | n" | 'o | p | q }
	 * - Output = { a | b c d | e f g | h"i | j" | k l'm n | 'o | p | q }
	 *
	 * @param args The original Bukkit command args array
	 */
	public static String[] joinArgsWithQuotes(String[] args) {
		return new ArgumentParser(args).joinArgsWithQuotes();
	}

	/** Different states encountered while iterating over the args array */
	private static enum QuoteState {

		/** Not inside a quoted String */
		NO_QUOTES,

		/** Inside a single-quoted String */
		SINGLE_QUOTES,

		/** Inside a double-quoted String */
		DOUBLE_QUOTES
	}

	/** Used to store the input */
	private final String[] initialArgs;

	/** Used to store the final result */
	private final List<String> parsedArgs;

	/** Used to store the current quote state */
	private QuoteState quoteState;

	/** Used to build the content of a quoted String as a single String */
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
	private ArgumentParser(String[] args) {
		this.initialArgs = args;
		parsedArgs = new ArrayList<>(initialArgs.length);
		fallBackBuffer = new ArrayList<>(initialArgs.length);
		quotedArgsBuffer = new StringBuilder();
	}

	/**
	 * This method actually starts the work
	 *
	 * @return The final result of this ArgumentParser
	 */
	private String[] joinArgsWithQuotes() {
		quoteState = QuoteState.NO_QUOTES;

		for (String arg : initialArgs) {
			parseSingleArg(arg);
		}

		dealWithAnyUnclosedQuotes();

		return parsedArgs.toArray(new String[parsedArgs.size()]);
	}

	/**
	 * Parse an arg differently if we're inside a quoted string or not.
	 *
	 * @param arg the arg to parse.
	 */
	private void parseSingleArg(String arg) {
		if (arg.isEmpty()) {
			return;
		}

		fallBackBuffer.add(arg);
		switch (quoteState) {
			case NO_QUOTES:
				parseUnquotedArg(arg);
				break;
			default:
				quotedArgsBuffer.append(' ');
				parseQuotedArg(arg);
				break;
		}
	}

	/**
	 * Checks if this arg starts a new quoted string or not.
	 *
	 * @param arg the arg to parse
	 */
	private void parseUnquotedArg(String arg) {
		char firstChar = arg.charAt(0);
		if (firstChar == '\'') {
			quoteState = QuoteState.SINGLE_QUOTES;
			arg = removeInitialQuote(arg);
			parseQuotedArg(arg);
		} else if (firstChar == '\"') {
			quoteState = QuoteState.DOUBLE_QUOTES;
			arg = removeInitialQuote(arg);
			parseQuotedArg(arg);
		} else {
			parsedArgs.add(arg);
		}
	}

	/**
	 * Removes the first char quote of the provided String.
	 *
	 * @param arg the arg to parse.
	 *
	 * @return the same arg without the first char.
	 */
	private String removeInitialQuote(String arg) {
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

		char lastChar = arg.charAt(arg.length() - 1);
		if (charIsQuoteMatchingState(lastChar) && endQuoteIsNotEscaped(arg)) {
			arg = removeFinalQuote(arg);
			quotedArgsBuffer.append(arg);
			parsedArgs.add(quotedArgsBuffer.toString());
			resetQuotedArgTracking();
		} else {
			quotedArgsBuffer.append(arg);
		}
	}

	/**
	 * Checks if this char closes the current quoted String.
	 *
	 * @param c the char to check.
	 *
	 * @return True if this char matches the char that opened the current quoted String, false otherwise.
	 */
	private boolean charIsQuoteMatchingState(char c) {
		return (c == '\'' && quoteState == QuoteState.SINGLE_QUOTES) || (c == '\"' && quoteState == QuoteState.DOUBLE_QUOTES);
	}

	/**
	 * Checks if the quote at the end of this arg is not escaped.
	 *
	 * @param arg the arg to parse.
	 *
	 * @return True if we should consider this final quote, false otherwise.
	 */
	private boolean endQuoteIsNotEscaped(String arg) {
		return arg.length() <= 1 || arg.charAt(arg.length() - 2) != '\\';
	}

	/**
	 * Removes the final char of the provided String.
	 *
	 * @param arg the arg to parse.
	 *
	 * @return the same arg without the last char.
	 */
	private String removeFinalQuote(String arg) {
		if (arg.length() > 1) {
			return arg.substring(0, arg.length() - 1);
		}
		return "";
	}

	/** Resets buffers and state */
	private void resetQuotedArgTracking() {
		quotedArgsBuffer.delete(0, quotedArgsBuffer.length());
		fallBackBuffer.clear();
		quoteState = QuoteState.NO_QUOTES;
	}

	/**
	 * Consider all args that are in an un-closed quoted String as normal args.
	 * Called once we checked all args.
	 */
	private void dealWithAnyUnclosedQuotes() {
		if (quoteState != QuoteState.NO_QUOTES && !fallBackBuffer.isEmpty()) {
			parsedArgs.addAll(fallBackBuffer);
		}
	}
}
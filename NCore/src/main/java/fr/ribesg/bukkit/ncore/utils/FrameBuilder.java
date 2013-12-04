/***************************************************************************
 * Project file:    NPlugins - NCore - FrameBuilder.java                   *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.FrameBuilder              *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is a tool to build Framed text for nice output, in configs
 * or in console.
 *
 * @author Ribesg
 */
public class FrameBuilder {

	/** Enum of options available for each line */
	public enum Option {
		CENTER,
		RIGHT
	}

	/** Represents a line of text with some options */
	private class Line {

		public final String      line;
		public final Set<Option> options;

		public Line(String line, Set<Option> options) {
			this.line = line;
			this.options = options;
		}

		public String parse(int toSize) {
			if (options.contains(Option.CENTER)) {
				return center(line, toSize);
			} else if (options.contains(Option.RIGHT)) {
				return right(line, toSize);
			} else {
				return left(line, toSize);
			}
		}
	}

	private final List<Line> lines;
	private       int        maxLength;
	private       char       frameChar;
	private final String     doubleFrameChar;

	public FrameBuilder() {
		this('#');
	}

	public FrameBuilder(final char frameChar) {
		this.lines = new ArrayList<>();
		this.maxLength = -1;
		this.frameChar = frameChar;
		this.doubleFrameChar = "" + frameChar + frameChar;
	}

	/**
	 * Add a line to the list of lines to be framed
	 *
	 * @param content line to add
	 * @param options options about this line
	 */
	public void addLine(String content, Option... options) {
		final Set<Option> optionSet = new HashSet<>(Arrays.asList(options));
		final Line line = new Line(content, optionSet);
		this.lines.add(line);
		this.maxLength = content.length() > maxLength ? content.length() : maxLength;
	}

	/**
	 * Actually frame the lines contained in this object
	 *
	 * @return framed lines
	 */
	public String[] build() {
		final String[] result = new String[lines.size() + 2];
		result[0] = multipleChars(maxLength + 6, frameChar);
		for (int i = 0; i < lines.size(); i++) {
			result[i + 1] = doubleFrameChar + ' ' + lines.get(i).parse(maxLength) + ' ' + doubleFrameChar;
		}
		result[result.length - 1] = result[0];
		return result;
	}

	/**
	 * Append and prepend spaces to the String to match wanted size
	 *
	 * @param aString the String to resize
	 * @param toSize  the size
	 *
	 * @return the resized String
	 */
	private String center(String aString, int toSize) {
		int stringLength = aString.length();
		int left = (toSize - stringLength) / 2;
		int right = toSize - stringLength - left;
		return spaces(left) + aString + spaces(right);
	}

	/**
	 * Append spaces to the String to match wanted size
	 *
	 * @param aString the String to resize
	 * @param toSize  the size
	 *
	 * @return the resized String
	 */
	private String right(String aString, int toSize) {
		int stringLength = aString.length();
		return spaces(toSize - stringLength) + aString;
	}

	/**
	 * Prepend spaces to the String to match wanted size
	 *
	 * @param aString the String to resize
	 * @param toSize  the size
	 *
	 * @return the resized String
	 */
	private String left(String aString, int toSize) {
		int stringLength = aString.length();
		return aString + spaces(toSize - stringLength);
	}

	/**
	 * Create a String containing spaces
	 *
	 * @param nb the wanted String size
	 *
	 * @return a String of size nb containing spaces
	 */
	private String spaces(final int nb) {
		return multipleChars(nb, ' ');
	}

	/**
	 * Create a String containing provided character
	 *
	 * @param nb returned String length
	 * @param c  character to fill the String with
	 *
	 * @return a String containing nb occurrences of c (and nothing else)
	 */
	private String multipleChars(final int nb, final char c) {
		final StringBuilder s = new StringBuilder(nb);
		for (int i = 0; i < nb; i++) {
			s.append(c);
		}
		return s.toString();
	}

	/**
	 * Frames a text into a box
	 *
	 * @param messages lines of the text
	 *
	 * @return framed lines
	 */
	private String[] frame(final String... messages) {
		final String[] result = new String[messages.length + 2];
		int maxLength = 0;
		for (final String s : messages) {
			maxLength = Math.max(maxLength, s.length());
		}
		final int length = maxLength + 6;
		result[0] = multipleChars(length, frameChar);
		for (int i = 0; i < messages.length; i++) {
			result[i + 1] = doubleFrameChar + ' ' + messages[i] + spaces(maxLength - messages[i].length()) + ' ' + doubleFrameChar;
		}
		result[result.length - 1] = result[0];
		return result;
	}
}

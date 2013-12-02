/***************************************************************************
 * Project file:    NPlugins - NCore - FrameBuilder.java                   *
 * Full Class name: fr.ribesg.bukkit.ncore.common.FrameBuilder             *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author Ribesg */
public class FrameBuilder {

	public enum Option {
		CENTER,
		RIGHT
	}

	private class Line {

		final String      line;
		final Set<Option> options;

		Line(String line, Set<Option> options) {
			this.line = line;
			this.options = options;
		}

		String parse(int toSize) {
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

	public FrameBuilder() {
		this.lines = new ArrayList<Line>();
		maxLength = -1;
	}

	public void addLine(String content, Option... options) {
		Set<Option> optionSet = new HashSet<>(Arrays.asList(options));
		Line line = new Line(content, optionSet);
		lines.add(line);
		maxLength = content.length() > maxLength ? content.length() : maxLength;
	}

	public String[] build() {
		final String[] result = new String[lines.size() + 2];
		result[0] = multipleChars(maxLength + 6, '#');
		for (int i = 0; i < lines.size(); i++) {
			result[i + 1] = "## " + lines.get(i).parse(maxLength) + " ##";
		}
		result[result.length - 1] = result[0];
		return result;
	}

	private String center(String aString, int toSize) {
		int stringLength = aString.length();
		int left = (toSize - stringLength) / 2;
		int right = toSize - stringLength - left;
		return spaces(left) + aString + spaces(right);
	}

	private String right(String aString, int toSize) {
		int stringLength = aString.length();
		return spaces(toSize - stringLength) + aString;
	}

	private String left(String aString, int toSize) {
		int stringLength = aString.length();
		return aString + spaces(toSize - stringLength);
	}

	/**
	 * @param nb The wanted String size
	 *
	 * @return A String of size nb containing spaces
	 */
	private String spaces(final int nb) {
		return multipleChars(nb, ' ');
	}

	/**
	 * @param nb Returned String length
	 * @param c  Character to fill the String with
	 *
	 * @return A String containing nb occurrences of c (and nothing else)
	 */
	private String multipleChars(final int nb, final char c) {
		final StringBuilder s = new StringBuilder(nb);
		for (int i = 0; i < nb; i++) {
			s.append(c);
		}
		return s.toString();
	}

	/**
	 * Frames a text into a ## box
	 *
	 * @param messages Lines of the text
	 *
	 * @return New lines with additional ###
	 */
	private String[] frame(final String... messages) {
		final String[] result = new String[messages.length + 2];
		int maxLength = 0;
		for (final String s : messages) {
			maxLength = Math.max(maxLength, s.length());
		}
		final int length = maxLength + 6;
		result[0] = multipleChars(length, '#');
		for (int i = 0; i < messages.length; i++) {
			result[i + 1] = "## " + messages[i] + spaces(maxLength - messages[i].length()) + " ##";
		}
		result[result.length - 1] = result[0];
		return result;
	}
}

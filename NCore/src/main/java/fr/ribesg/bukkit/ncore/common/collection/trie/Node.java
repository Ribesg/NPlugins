/***************************************************************************
 * Project file:    NPlugins - NCore - Node.java                           *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.trie.Node     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.trie;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Node<T extends TrieElement> {

	private final char                    character;
	private final Map<Character, Node<T>> nextChars;

	/* package */ boolean isFinal;
	/* package */ T       element;

	public Node(final char character) {
		this.character = character;
		this.isFinal = false;
		this.element = null;
		this.nextChars = new TreeMap<>();
	}

	public Node(final char character, final T elem) {
		this.character = character;
		this.isFinal = true;
		this.element = elem;
		this.nextChars = new TreeMap<>();
	}

	public void insert(final char[] chars, final int startIndex, final T elem) {
		char c = chars[startIndex];
		if (c >= 'A' && c <= 'Z') {
			c = Character.toLowerCase(c);
		}
		Node<T> next = this.nextChars.get(c);
		if (next == null) {
			next = new Node<>(c);
			this.nextChars.put(c, next);
		}
		if (chars.length == startIndex + 1) {
			next.isFinal = true;
			next.element = elem;
		} else {
			next.insert(chars, startIndex + 1, elem);
		}
	}

	public T check(final char[] chars, final int startIndex) {
		if (this.isFinal) {
			return this.element;
		} else if (chars.length <= startIndex + 1) {
			return null;
		} else {
			char c = chars[startIndex + 1];
			if (c >= 'A' && c <= 'Z') {
				c = Character.toLowerCase(c);
			}
			final Node<T> next = this.nextChars.get(c);
			if (next == null) {
				return null;
			} else {
				return next.check(chars, startIndex + 1);
			}
		}
	}

	public void addAllTo(final Set<T> result) {
		if (this.element != null) {
			result.add(this.element);
		}
		for (final Node<T> node : this.nextChars.values()) {
			node.addAllTo(result);
		}
	}
}

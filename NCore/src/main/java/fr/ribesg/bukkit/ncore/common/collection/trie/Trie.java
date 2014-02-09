/***************************************************************************
 * Project file:    NPlugins - NCore - Trie.java                           *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.trie.Trie     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.trie;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Represents a Trie that link words to nothing or to an object of type T
 *
 * @param <T> the type of the Object linked by words
 */
public class Trie<T extends TrieElement> {

	private final Map<Character, Node<T>> roots;

	public Trie() {
		this.roots = new TreeMap<>();
	}

	public void insert(final T elem) {
		final char[] chars = elem.getCharSequence();
		Node<T> root = this.roots.get(chars[0]);
		if (root == null) {
			root = new Node<>(chars[0]);
			this.roots.put(chars[0], root);
		}
		if (chars.length == 1) {
			root.isFinal = true;
			root.element = elem;
		} else {
			root.insert(chars, 1, elem);
		}
	}

	public Set<T> checkAll(final String aString) {
		final Set<T> result = new HashSet<>();
		final char[] chars = aString.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			final Node<T> root = this.roots.get(chars[i]);
			if (root != null) {
				final T res = root.check(chars, i);
				if (res != null) {
					result.add(res);
				}
			}
		}
		return result;
	}

	public T check(final String aString) {
		final char[] chars = aString.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			final Node<T> root = this.roots.get(chars[i]);
			if (root != null) {
				final T res = root.check(chars, i);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}

	public Set<T> getAll() {
		final Set<T> result = new HashSet<>();
		for (final Node<T> node : this.roots.values()) {
			node.addAllTo(result);
		}
		return result;
	}

	public void clear() {
		this.roots.clear();
	}
}

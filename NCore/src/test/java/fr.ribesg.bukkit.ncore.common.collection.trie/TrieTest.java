/***************************************************************************
 * Project file:    NPlugins - NCore - TrieTest.java                       *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.trie.TrieTest *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.trie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class TrieTest {

	private class Elem implements TrieElement {

		public final int  a;
		public final char b;

		private Elem(final int a, final char b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public char[] getCharSequence() {
			return (Integer.toString(a) + b).toCharArray();
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			final Elem elem = (Elem) o;

			return a == elem.a && b == elem.b;
		}
	}

	private Elem a, b, c;
	private Trie<Elem> trie;

	@Before
	public void setUp() {
		this.trie = new Trie<>();

		this.a = new Elem(12, 'h');
		this.b = new Elem(-42, 'K');
		this.c = new Elem(Integer.MIN_VALUE, ' ');

		this.trie.insert(a);
		this.trie.insert(b);
		this.trie.insert(c);
	}

	@Test
	public void testTrieCheckNotIn() {
		final String toBeChecked = "Test";
		final Elem z = trie.check(toBeChecked);
		Assert.assertNull(z);
	}

	@Test
	public void testTrieCheckInSimple() {
		final String toBeChecked = "12h";
		final Elem aBis = trie.check(toBeChecked);
		Assert.assertNotNull(aBis);
		Assert.assertEquals(a, aBis);
	}

	@Test
	public void testTrieCheckInLessSimple() {
		final String toBeChecked = "Test-gsgd42gtegzrkeifhze-42k fefsief";
		final Elem bBis = trie.check(toBeChecked);
		Assert.assertNotNull(bBis);
		Assert.assertEquals(b, bBis);
	}

	@Test
	public void testTrieCheckInABitComplex() {
		final String toBeChecked = "Test" + Integer.MIN_VALUE + " jifjzoigzTest--42k";
		final Elem cBis = trie.check(toBeChecked);
		Assert.assertNotNull(cBis);
		Assert.assertEquals(c, cBis);
	}

	@Test
	public void testTrieGetAll() {
		final Set<Elem> content = this.trie.getAll();
		Assert.assertEquals(3, content.size());
		Assert.assertTrue(content.contains(this.a));
		Assert.assertTrue(content.contains(this.b));
		Assert.assertTrue(content.contains(this.c));
	}
}

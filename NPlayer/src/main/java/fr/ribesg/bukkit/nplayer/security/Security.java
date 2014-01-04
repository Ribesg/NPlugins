/***************************************************************************
 * Project file:    NPlugins - NPlayer - Security.java                     *
 * Full Class name: fr.ribesg.bukkit.nplayer.security.Security             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.security;

import fr.ribesg.bukkit.nplayer.user.User;

import java.util.UUID;

public class Security {

	public static boolean isUserPassword(final String enteredPassword, final User user) {
		final String realPasswordHash = user.getPasswordHash();
		final int saltPos = enteredPassword.length() >= realPasswordHash.length()
		                    ? realPasswordHash.length() - 1
		                    : enteredPassword.length();
		final String salt = realPasswordHash.substring(saltPos, saltPos + 12);
		final String newHash = whirlpool(salt + enteredPassword);
		final String enteredPasswordHash = newHash.substring(0, saltPos) + salt + newHash.substring(saltPos);
		return enteredPasswordHash.equals(realPasswordHash);
	}

	public static String hash(final String stringToHash) {
		final String salt = whirlpool(UUID.randomUUID().toString()).substring(0, 12);
		final String hash = whirlpool(salt + stringToHash);
		final int saltPos = stringToHash.length() >= hash.length() ? hash.length() - 1 : stringToHash.length();
		return hash.substring(0, saltPos) + salt + hash.substring(saltPos);
	}

	private static String whirlpool(final String toHash) {
		final Whirlpool w = new Whirlpool();
		final byte[] digest = new byte[Whirlpool.DIGESTBYTES];
		w.NESSIEinit();
		w.NESSIEadd(toHash);
		w.NESSIEfinalize(digest);
		return Whirlpool.display(digest);
	}
}

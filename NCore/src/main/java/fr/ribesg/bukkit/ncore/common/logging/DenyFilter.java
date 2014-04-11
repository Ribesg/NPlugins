/***************************************************************************
 * Project file:    NPlugins - NCore - DenyFilter.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.common.logging.DenyFilter       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.logging;
/**
 * Represents a simple Log filter.
 *
 * @author Ribesg
 */
public interface DenyFilter {

	/**
	 * Check if this DenyFilter denies a message or not.
	 *
	 * @param message the message to check
	 *
	 * @return true if this DenyFilter prevents this message from being logged,
	 * false otherwise
	 */
	public boolean denies(final String message);
}

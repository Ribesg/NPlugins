/***************************************************************************
 * Project file:    NPlugins - NCore - DownloadFailedException.java        *
 * Full Class name: fr.ribesg.bukkit.ncore.updater.DownloadFailedException *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.updater;
public class DownloadFailedException extends Exception {

	public DownloadFailedException() {
	}

	public DownloadFailedException(final String message) {
		super(message);
	}

	public DownloadFailedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DownloadFailedException(final Throwable cause) {
		super(cause);
	}
}

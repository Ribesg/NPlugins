/***************************************************************************
 * Project file:    NPlugins - NCore - WorldUtils.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.WorldUtils                *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class handling worlds.
 *
 * @author Ribesg
 */
public class WorldUtils {

	/**
	 * Check if a given world is loaded, not case sensitive.
	 *
	 * @param worldName The world name given by the sender
	 *
	 * @return The correct name of the world if it is loaded, null otherwise
	 */
	public static String isLoaded(final String worldName) {
		if (Bukkit.getWorld(worldName) != null) {
			return worldName;
		}
		for (final World world : Bukkit.getWorlds()) {
			if (world.getName().equalsIgnoreCase(worldName)) {
				return world.getName();
			}
		}
		return null;
	}

	/**
	 * Check if a given unloaded world exists in world folder, not case sensitive.
	 *
	 * @param worldName The world name given by the sender
	 *
	 * @return The correct name of the world if it exists, null otherwise
	 *
	 * @throws java.io.IOException If it was unable to iterate over the Worlds folder
	 */
	public static String exists(final String worldName) throws IOException {
		try {
			final Path worldFolderPath = Bukkit.getWorldContainer().toPath();
			for (final Path p : Files.newDirectoryStream(worldFolderPath)) {
				if (p.getFileName().toString().equalsIgnoreCase(worldName)) {
					return p.getFileName().toString();
				}
			}
		} catch (final IOException e) {
			Bukkit.getLogger().severe("Unable to iterate over Worlds");
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	/**
	 * Returns the real world name of the given world
	 *
	 * @param worldName The world name given by the sender
	 *
	 * @return The correct name of the world if it is loaded or exists, null otherwise
	 */
	public static String getRealWorldName(final String worldName) {
		try {
			final String res = isLoaded(worldName);
			if (res == null) {
				return exists(worldName);
			}
			return res;
		} catch (final IOException e) {
			// Already outputed a message and printed a Stacktrace
			return null;
		}
	}
}

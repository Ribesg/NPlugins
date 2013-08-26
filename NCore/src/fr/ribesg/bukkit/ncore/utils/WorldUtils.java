package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorldUtils {

	/**
	 * Check if a given world is loaded, not case sensitive.
	 *
	 * @param worldName The world name given by the sender
	 *
	 * @return The correct name of the world if it is loaded, null otherwise
	 */
	public static String isLoaded(final String worldName) {
		final World world = Bukkit.getWorld(worldName);
		if (world != null) {
			return world.getName();
		} else {
			return null;
		}
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
			String res = isLoaded(worldName);
			if (res == null) {
				res = exists(worldName);
			}
			return res;
		} catch (final IOException e) {
			return null;
		}
	}
}

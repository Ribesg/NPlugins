/***************************************************************************
 * Project file:    NPlugins - NCore - YamlConfigUtils.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.YamlConfigUtils           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlConfigUtils {

	/**
	 * Gets a dummy ConfigurationSection that can be used to store things
	 * and then get a String representing the whole ConfigurationSection with
	 * {@link #printConfigurationSection(org.bukkit.configuration.ConfigurationSection)}
	 *
	 * @param key the key for the ConfigurationSection
	 *
	 * @return a dummy ConfigurationSection
	 */
	public static ConfigurationSection createDummyConfigurationSection(final String key) {
		return new YamlConfiguration().createSection(key);
	}

	/**
	 * Takes a YamlConfiguration's ConfigurationSection and gets a String
	 * representing the whole Section.
	 *
	 * @param section a YamlConfiguration's ConfigurationSection
	 *
	 * @return a String representing the provided ConfigurationSection
	 */
	public static String printConfigurationSection(final ConfigurationSection section) {
		return ((YamlConfiguration) section.getRoot()).saveToString();
	}
}

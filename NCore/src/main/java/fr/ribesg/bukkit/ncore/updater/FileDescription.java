/***************************************************************************
 * Project file:    NPlugins - NCore - FileDescription.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.updater.FileDescription         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.updater;

import fr.ribesg.bukkit.ncore.util.VersionUtil;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author Ribesg
 */
public class FileDescription {

	private static final String VERSION_KEY        = "name";
	private static final String FILENAME_KEY       = "fileName";
	private static final String DOWNLOAD_URL_KEY   = "downloadUrl";
	private static final String TYPE_KEY           = "releaseType";
	private static final String BUKKIT_VERSION_KEY = "gameVersion";

	public static SortedMap<String, FileDescription> parse(final String jsonString) {
		final SortedMap<String, FileDescription> result = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(final String a, final String b) {
				return -a.compareTo(b);
			}
		});
		final JSONArray array = (JSONArray)JSONValue.parse(jsonString);
		for (final Object o : array.toArray()) {
			final JSONObject object = (JSONObject)o;
			final String fileName = (String)object.get(FILENAME_KEY);
			final String version = VersionUtil.getVersion((String)object.get(VERSION_KEY));
			final String bukkitVersion = (String)object.get(BUKKIT_VERSION_KEY);
			final String type = (String)object.get(TYPE_KEY);
			final String link = (String)object.get(DOWNLOAD_URL_KEY);
			final FileDescription fileDescription = new FileDescription(fileName, version, link, type, bukkitVersion);
			if (VersionUtil.isRelease(version)) {
				result.put(version, fileDescription);
			}
		}
		return result;
	}

	private final String fileName;
	private final String version;
	private final String downloadUrl;
	private final String type;
	private final String bukkitVersion;

	public FileDescription(final String fileName, final String version, final String downloadUrl, final String type, final String bukkitVersion) {
		this.fileName = fileName;
		this.version = version;
		this.downloadUrl = downloadUrl;
		this.type = type;
		this.bukkitVersion = bukkitVersion;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getVersion() {
		return this.version;
	}

	public String getDownloadUrl() {
		return this.downloadUrl;
	}

	public String getType() {
		return this.type;
	}

	public String getBukkitVersion() {
		return this.bukkitVersion;
	}

	@Override
	public String toString() {
		return "FileDescription{" +
		       "fileName='" + this.fileName + '\'' +
		       ", version='" + this.version + '\'' +
		       ", downloadUrl='" + this.downloadUrl + '\'' +
		       ", type='" + this.type + '\'' +
		       ", bukkitVersion='" + this.bukkitVersion + '\'' +
		       '}';
	}
}

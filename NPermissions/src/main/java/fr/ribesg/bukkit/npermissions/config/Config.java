/***************************************************************************
 * Project file:    NPlugins - NPermissions - Config.java                  *
 * Full Class name: fr.ribesg.bukkit.npermissions.config.Config            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.config;

import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.npermissions.NPermissions;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends AbstractConfig<NPermissions> {

	private String  defaultGroup;
	private boolean perWorldPermissions;

	/**
	 * @param instance the plugin instance
	 */
	public Config(final NPermissions instance) {
		super(instance);
		this.defaultGroup = "user";
		this.perWorldPermissions = false;
	}

	/**
	 * @see AbstractConfig#handleValues(YamlConfiguration)
	 */
	@Override
	protected void handleValues(final YamlConfiguration config) {
		this.defaultGroup = config.getString("defaultGroup", "user");
		this.perWorldPermissions = config.getBoolean("perWorldPermissions", false);
	}

	/**
	 * @see AbstractConfig#getConfigString()
	 */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NPermissions plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line).append('\n');
		}
		content.append('\n');

		// TODO add some doc comment before this
		content.append("# First-join users are affected to this group.\n");
		content.append("defaultGroup: ").append(this.defaultGroup).append("\n\n");

		content.append("# Enable per-world permissions.\n");
		content.append("# Start and stop the server once with this enabled to generate appropriate files.\n");
		content.append("perWorldPermissions: ").append(this.perWorldPermissions).append("\n\n");

		return content.toString();
	}

	public String getDefaultGroup() {
		return this.defaultGroup;
	}

	public boolean hasPerWorldPermissions() {
		return perWorldPermissions;
	}
}

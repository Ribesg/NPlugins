/***************************************************************************
 * Project file:    NPlugins - NCore - Config.java                         *
 * Full Class name: fr.ribesg.bukkit.ncore.config.Config                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.config;
import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/** @author Ribesg */
public class Config extends AbstractConfig<NCore> {

	private boolean updateCheck;

	/**
	 * Constructor
	 *
	 * @param instance Linked plugin instance
	 */
	public Config(final NCore instance) {
		super(instance);
		this.updateCheck = true;
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// updateCheck. Default: true
		// Possible values: boolean
		setUpdateCheck(config.getBoolean("updateCheck", true));
	}

	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NCore plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		// updateCheck. Default: true
		content.append("# Enables update check at startup. Default : true\n");
		content.append("updateCheck: " + isUpdateCheck() + "\n\n");

		return content.toString();
	}

	public boolean isUpdateCheck() {
		return updateCheck;
	}

	public void setUpdateCheck(final boolean updateCheck) {
		this.updateCheck = updateCheck;
	}
}

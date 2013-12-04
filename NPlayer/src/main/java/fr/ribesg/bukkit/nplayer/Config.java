/***************************************************************************
 * Project file:    NPlugins - NPlayer - Config.java                       *
 * Full Class name: fr.ribesg.bukkit.nplayer.Config                        *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends AbstractConfig<NPlayer> {

	private int maximumLoginAttempts;
	private int tooManyAttemptsPunishment;
	private int tooManyAttemptsPunishmentDuration;

	public Config(final NPlayer instance) {
		super(instance);
		setMaximumLoginAttempts(3);
		setTooManyAttemptsPunishment(1);
		setTooManyAttemptsPunishmentDuration(300);
	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#handleValues(org.bukkit.configuration.file.YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) {

		// maximumLoginAttempts. Default: 3.
		// Possible values: positive integers
		setMaximumLoginAttempts(config.getInt("maximumLoginAttempts", 3));
		if (getMaximumLoginAttempts() < 1) {
			setMaximumLoginAttempts(1);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "maximumLoginAttempts",
			                   "1");
		}

		// tooManyAttemptsPunishment. Default: 1.
		// Possible values: 0, 1, 2
		setTooManyAttemptsPunishment(config.getInt("tooManyAttemptsPunishment", 1));
		if (getTooManyAttemptsPunishment() < 0 || getTooManyAttemptsPunishment() > 2) {
			setTooManyAttemptsPunishment(1);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "tooManyAttemptsPunishment",
			                   "1");
		}

		// tooManyAttemptsPunishmentDuration. Default: 300.
		// Possible values: positive integers
		setTooManyAttemptsPunishmentDuration(config.getInt("tooManyAttemptsPunishmentDuration", 300));
		if (getTooManyAttemptsPunishmentDuration() < 1) {
			setTooManyAttemptsPunishmentDuration(300);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "tooManyAttemptsPunishmentDuration",
			                   "300");
		}
	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NPlayer plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		// Maximum Login attempts
		content.append("# Maximum login attempts before punishment. Possible values: Positive integers\n");
		content.append("# Default : 3\n");
		content.append("maximumLoginAttempts: " + getMaximumLoginAttempts() + "\n\n");

		// Too Many Attempts Punishment
		content.append("# How do we punish people after too many attempts? Possible values: 0, 1, 2\n");
		content.append("# Default : 1\n");
		content.append("#\n");
		content.append("#   Value | Action\n");
		content.append("#       0 : Kick\n");
		content.append("#       1 : Tempban\n");
		content.append("#       2 : Ban\n");
		content.append("#\n");
		content.append("tooManyAttemptsPunishment: " + getTooManyAttemptsPunishment() + "\n\n");

		// Too Many Attempts Punishment Duration
		content.append("# The duration of the punishment, if applicable. Possible values: Positive integers\n");
		content.append("# Default : 300\n");
		content.append("#\n");
		content.append("# Here are some example values:\n");
		content.append("#      Value | Description\n");
		content.append("#         30 : 30 seconds\n");
		content.append("#         60 : 1 minute\n");
		content.append("#        300 : 5 minutes\n");
		content.append("#        600 : 10 minutes\n");
		content.append("#       1800 : 30 minutes\n");
		content.append("#       1800 : 30 minutes\n");
		content.append("#       1800 : 30 minutes\n");
		content.append("#       3600 : 1 hour\n");
		content.append("#       7200 : 2 hours\n");
		content.append("#      10800 : 3 hours\n");
		content.append("#      14400 : 4 hours\n");
		content.append("#      21600 : 6 hours\n");
		content.append("#      28800 : 8 hours\n");
		content.append("#      43200 : 12 hours\n");
		content.append("#      86400 : 24 hours - 1 day\n");
		content.append("#     172800 : 48 hours - 2 days\n");
		content.append("#     604800 : 7 days\n");
		content.append("#\n");
		content.append("# You can use *any* strictly positive value you want, just be sure to convert it to seconds.\n");
		content.append("# Note: only applies to TempBan punishment for now.\n");
		content.append("#\n");
		content.append("tooManyAttemptsPunishmentDuration: " + getTooManyAttemptsPunishmentDuration() + "\n\n");

		return content.toString();
	}

	public int getMaximumLoginAttempts() {
		return maximumLoginAttempts;
	}

	public void setMaximumLoginAttempts(int maximumLoginAttempts) {
		this.maximumLoginAttempts = maximumLoginAttempts;
	}

	public int getTooManyAttemptsPunishment() {
		return tooManyAttemptsPunishment;
	}

	public void setTooManyAttemptsPunishment(int tooManyAttemptsPunishment) {
		this.tooManyAttemptsPunishment = tooManyAttemptsPunishment;
	}

	public int getTooManyAttemptsPunishmentDuration() {
		return tooManyAttemptsPunishmentDuration;
	}

	public void setTooManyAttemptsPunishmentDuration(int tooManyAttemptsPunishmentDuration) {
		this.tooManyAttemptsPunishmentDuration = tooManyAttemptsPunishmentDuration;
	}
}

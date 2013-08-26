package fr.ribesg.bukkit.ngeneral.config;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.logging.Logger;

public class DbConfig extends AbstractConfig<NGeneral> {

	private final Logger log;

	public DbConfig(NGeneral instance) {
		super(instance);
		log = instance.getLogger();
	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#handleValues(org.bukkit.configuration.file.YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// #############
		// ## GodMode ##
		// #############

		if (plugin.getPluginConfig().hasGodModeFeature() && config.isList("godModePlayers")) {
			List<String> godModePlayers = config.getStringList("godModePlayers");
			plugin.getGodMode().getGodPlayers().addAll(godModePlayers);
		}

		if (plugin.getPluginConfig().hasFlyModeFeature() && config.isList("flyModePlayers")) {
			List<String> flyModePlayers = config.getStringList("flyModePlayers");
			plugin.getFlyMode().getFlyPlayers().addAll(flyModePlayers);
		}

	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("DbConfig file for NGeneral plugin", FrameBuilder.Option.CENTER);
		frame.addLine("This file is used to store objects, to make features persistent");
		frame.addLine("It may be a good idea not to touch anything");
		frame.addLine("unless you know what you are doing");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// #############
		// ## GodMode ##
		// #############

		if (plugin.getPluginConfig().hasGodModeFeature()) {
			content.append("godModePlayers:\n");
			for (String playerName : plugin.getGodMode().getGodPlayers()) {
				content.append("- " + playerName + "\n");
			}
			content.append('\n');
		}

		if (plugin.getPluginConfig().hasFlyModeFeature()) {
			content.append("flyModePlayers:\n");
			for (String playerName : plugin.getFlyMode().getFlyPlayers()) {
				content.append("- " + playerName + "\n");
			}
			content.append('\n');
		}

		return content.toString();
	}
}

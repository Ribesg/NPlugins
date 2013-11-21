package fr.ribesg.bukkit.ngeneral.config;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.common.FrameBuilder;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetwork;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ReceiverSign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class DbConfig extends AbstractConfig<NGeneral> {

	public DbConfig(NGeneral instance) {
		super(instance);
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

		// #############
		// ## FlyMode ##
		// #############

		if (plugin.getPluginConfig().hasFlyModeFeature() && config.isList("flyModePlayers")) {
			List<String> flyModePlayers = config.getStringList("flyModePlayers");
			plugin.getFlyMode().getFlyPlayers().addAll(flyModePlayers);
		}

		// #################
		// ## ItemNetwork ##
		// #################

		if (plugin.getPluginConfig().hasItemNetworkFeature()) {
			final ConfigurationSection inetSection = config.getConfigurationSection("itemnetworks");
			for (String networkName : inetSection.getKeys(false)) {
				final ConfigurationSection networkSection = inetSection.getConfigurationSection(networkName);
				final String networkCreator = networkSection.getString("creator");
				final ItemNetwork network = new ItemNetwork(plugin.getItemNetwork(), networkName, networkCreator);
				final ConfigurationSection receiversSection = networkSection.getConfigurationSection("receivers");
				for (String key : receiversSection.getKeys(false)) {
					final ConfigurationSection receiverSection = receiversSection.getConfigurationSection(key);
					final NLocation location = NLocation.toNLocation(receiverSection.getString("location"));
					final String acceptsString = receiverSection.getString("accepts");
					final ReceiverSign receiverSign = new ReceiverSign(location, acceptsString);
					network.getReceivers().add(receiverSign);
				}
			}
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

		// #############
		// ## FlyMode ##
		// #############

		if (plugin.getPluginConfig().hasFlyModeFeature()) {
			content.append("flyModePlayers:\n");
			for (String playerName : plugin.getFlyMode().getFlyPlayers()) {
				content.append("- " + playerName + "\n");
			}
			content.append('\n');
		}

		// #################
		// ## ItemNetwork ##
		// #################

		if (plugin.getPluginConfig().hasItemNetworkFeature()) {
			content.append("itemnetworks:\n");
			for (ItemNetwork network : plugin.getItemNetwork().getNetworks().values()) {
				content.append("  " + network.getName() + ":\n");
				content.append("    creator: " + network.getCreator() + "\n");
				content.append("    receivers:\n");
				int i = 1;
				for (ReceiverSign receiver : network.getReceivers()) {
					content.append("      receiver" + i++ + ":\n");
					content.append("        location: " + receiver.getLocation().toString() + "\n");
					content.append("        accepts: " + receiver.getAcceptsString() + "\n");
				}
			}
			content.append('\n');
		}
		// TODO Limitation de distance

		return content.toString();
	}
}

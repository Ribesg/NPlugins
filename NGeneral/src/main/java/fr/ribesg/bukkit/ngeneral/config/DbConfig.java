/***************************************************************************
 * Project file:    NPlugins - NGeneral - DbConfig.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.config.DbConfig              *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.config;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.ItemNetworkFeature;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetwork;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ReceiverSign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class DbConfig extends AbstractConfig<NGeneral> {

	public DbConfig(final NGeneral instance) {
		super(instance);
	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#handleValues(org.bukkit.configuration.file.YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// #############
		// ## FlyMode ##
		// #############

		if (config.isList("flyModePlayers")) {
			final List<String> flyModePlayers = config.getStringList("flyModePlayers");
			plugin.getFeatures().get(FlyModeFeature.class).getFlyPlayers().addAll(flyModePlayers);
		}

		// #############
		// ## GodMode ##
		// #############

		if (config.isList("godModePlayers")) {
			final List<String> godModePlayers = config.getStringList("godModePlayers");
			plugin.getFeatures().get(GodModeFeature.class).getGodPlayers().addAll(godModePlayers);
		}

		// #################
		// ## ItemNetwork ##
		// #################

		if (config.isConfigurationSection("itemnetworks")) {
			final ItemNetworkFeature feature = plugin.getFeatures().get(ItemNetworkFeature.class);
			final ConfigurationSection inetSection = config.getConfigurationSection("itemnetworks");
			for (final String networkName : inetSection.getKeys(false)) {
				final ConfigurationSection networkSection = inetSection.getConfigurationSection(networkName);
				final String networkCreator = networkSection.getString("creator");
				final ItemNetwork network = new ItemNetwork(feature, networkName, networkCreator);
				if (networkSection.isConfigurationSection("receivers")) {
					final ConfigurationSection receiversSection = networkSection.getConfigurationSection("receivers");
					for (final String key : receiversSection.getKeys(false)) {
						final ConfigurationSection receiverSection = receiversSection.getConfigurationSection(key);
						final NLocation location = NLocation.toNLocation(receiverSection.getString("location"));
						final String acceptsString = receiverSection.getString("accepts");
						final ReceiverSign receiverSign = new ReceiverSign(location, acceptsString);
						network.getReceivers().add(receiverSign);
					}
				}
				feature.getNetworks().put(networkName.toLowerCase(), network);
			}
		}
	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

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
		// ## FlyMode ##
		// #############

		if (plugin.getPluginConfig().hasFlyModeFeature()) {
			content.append("flyModePlayers:\n");
			for (final String playerName : plugin.getFeatures().get(FlyModeFeature.class).getFlyPlayers()) {
				content.append("- " + playerName + "\n");
			}
			content.append('\n');
		}

		// #############
		// ## GodMode ##
		// #############

		if (plugin.getPluginConfig().hasGodModeFeature()) {
			content.append("godModePlayers:\n");
			for (final String playerName : plugin.getFeatures().get(GodModeFeature.class).getGodPlayers()) {
				content.append("- " + playerName + "\n");
			}
			content.append('\n');
		}

		// #################
		// ## ItemNetwork ##
		// #################

		if (plugin.getPluginConfig().hasItemNetworkFeature()) {
			content.append("itemnetworks:\n");
			for (final ItemNetwork network : plugin.getFeatures().get(ItemNetworkFeature.class).getNetworks().values()) {
				content.append("  " + network.getName() + ":\n");
				content.append("    creator: " + network.getCreator() + "\n");
				content.append("    receivers:\n");
				int i = 1;
				for (final ReceiverSign receiver : network.getReceivers()) {
					content.append("      receiver" + i++ + ":\n");
					content.append("        location: " + receiver.getLocation().toString() + "\n");
					content.append("        accepts: \"" + receiver.getAcceptsString() + "\"\n");
				}
			}
			content.append('\n');
		}

		return content.toString();
	}
}

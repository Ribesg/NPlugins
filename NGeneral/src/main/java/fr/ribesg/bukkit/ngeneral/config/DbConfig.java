/***************************************************************************
 * Project file:    NPlugins - NGeneral - DbConfig.java                    *
 * Full Class name: fr.ribesg.bukkit.ngeneral.config.DbConfig              *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.config;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeFeature;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.ItemNetworkFeature;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetwork;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ReceiverSign;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class DbConfig extends AbstractConfig<NGeneral> {

	public DbConfig(final NGeneral instance) {
		super(instance);
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// #############
		// ## FlyMode ##
		// #############

		if (config.isList("flyModePlayers")) {
			final List<String> flyModePlayersStrings = config.getStringList("flyModePlayers");
			final List<UUID> flyModePlayers = new LinkedList<>();
			for (final String idString : flyModePlayersStrings) {
				UUID id = null;
				if (PlayerIdsUtil.isValidUuid(idString)) {
					id = UUID.fromString(idString);
				} else if (PlayerIdsUtil.isValidMinecraftUserName(idString)) {
					id = UuidDb.getId(Node.GENERAL, idString, true);
				}
				if (id == null) {
					throw new InvalidConfigurationException("Unknown playerId '" + idString + "' found in db.yml under section 'flyModePlayers'");
				} else {
					flyModePlayers.add(id);
				}
			}
			this.plugin.getFeatures().get(FlyModeFeature.class).getFlyPlayers().addAll(flyModePlayers);
		}

		// #############
		// ## GodMode ##
		// #############

		if (config.isList("godModePlayers")) {
			final List<String> godModePlayersStrings = config.getStringList("godModePlayers");
			final List<UUID> godModePlayers = new LinkedList<>();
			for (final String idString : godModePlayersStrings) {
				UUID id = null;
				if (PlayerIdsUtil.isValidUuid(idString)) {
					id = UUID.fromString(idString);
				} else if (PlayerIdsUtil.isValidMinecraftUserName(idString)) {
					id = UuidDb.getId(Node.GENERAL, idString, true);
				}
				if (id == null) {
					throw new InvalidConfigurationException("Unknown playerId '" + idString + "' found in db.yml under section 'godModePlayers'");
				} else {
					godModePlayers.add(id);
				}
			}
			this.plugin.getFeatures().get(GodModeFeature.class).getGodPlayers().addAll(godModePlayers);
		}

		// #################
		// ## ItemNetwork ##
		// #################

		if (config.isConfigurationSection("itemnetworks")) {
			final ItemNetworkFeature feature = this.plugin.getFeatures().get(ItemNetworkFeature.class);
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
						final ReceiverSign receiverSign = new ReceiverSign(this.plugin, location, acceptsString);
						network.getReceivers().add(receiverSign);
					}
				}
				feature.getNetworks().put(networkName.toLowerCase(), network);
			}
		}
	}

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

		if (this.plugin.getPluginConfig().hasFlyModeFeature()) {
			content.append("flyModePlayers:\n");
			for (final UUID playerId : this.plugin.getFeatures().get(FlyModeFeature.class).getFlyPlayers()) {
				content.append("- " + playerId + " # " + UuidDb.getName(playerId) + '\n');
			}
			content.append('\n');
		}

		// #############
		// ## GodMode ##
		// #############

		if (this.plugin.getPluginConfig().hasGodModeFeature()) {
			content.append("godModePlayers:\n");
			for (final UUID playerId : this.plugin.getFeatures().get(GodModeFeature.class).getGodPlayers()) {
				content.append("- " + playerId + " # " + UuidDb.getName(playerId) + '\n');
			}
			content.append('\n');
		}

		// #################
		// ## ItemNetwork ##
		// #################

		if (this.plugin.getPluginConfig().hasItemNetworkFeature()) {
			content.append("itemnetworks:\n");
			for (final ItemNetwork network : this.plugin.getFeatures().get(ItemNetworkFeature.class).getNetworks().values()) {
				content.append("  " + network.getName() + ":\n");
				content.append("    creator: " + network.getCreator() + '\n');
				content.append("    receivers:\n");
				int i = 1;
				for (final ReceiverSign receiver : network.getReceivers()) {
					content.append("      receiver" + i++ + ":\n");
					content.append("        location: " + receiver.getLocation() + '\n');
					content.append("        accepts: \"" + receiver.getAcceptsString() + "\"\n");
				}
			}
			content.append('\n');
		}

		return content.toString();
	}
}

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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/** @author Ribesg */
public class Config extends AbstractConfig<NCore> {

	private boolean           updateCheck;
	private List<String>      checkFor;
	private String            apiKey;
	private InetSocketAddress proxyAddress;

	private List<String> debugEnabled;

	/**
	 * Constructor
	 *
	 * @param instance Linked plugin instance
	 */
	public Config(final NCore instance) {
		super(instance);
		this.updateCheck = true;
		this.checkFor = new ArrayList<>();
		this.checkFor.add("NCore");
		this.checkFor.add("NCuboid");
		this.checkFor.add("NEnchantingEgg");
		this.checkFor.add("NGeneral");
		this.checkFor.add("NPlayer");
		this.checkFor.add("NTalk");
		this.checkFor.add("NTheEndAgain");
		this.checkFor.add("NWorld");
		this.apiKey = "";
		this.proxyAddress = null;

		this.debugEnabled = new ArrayList<>();
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// updateCheck. Default: true
		// Possible values: boolean
		setUpdateCheck(config.getBoolean("updateCheck", true));

		// checkFor. Default: NCore, NCuboid, NEnchantingEgg, NGeneral, NPlayer, NTalk, NTheEndAgain, NWorld
		// Possible values: any subset of the default value
		setCheckFor(config.getStringList("checkFor"));

		// apiKey. Default: empty
		setApiKey(config.getString("apiKey", ""));

		// proxyAddress
		final String proxyHost = config.getString("proxyHost", null);
		final int proxyPort = config.getInt("proxyPort", -1);
		if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != -1) {
			setProxyAddress(new InetSocketAddress(proxyHost, proxyPort));
		}

		// debugEnabled. Default: empty
		setDebugEnabled(config.getStringList("debugEnabled"));
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
		content.append("# Enables update check globally at startup. Default : true\n");
		content.append("updateCheck: " + isUpdateCheck() + "\n\n");

		// checkFor. Default: NCore, NCuboid, NEnchantingEgg, NGeneral, NPlayer, NTalk, NTheEndAgain, NWorld
		content.append("# Enable update check for each specific node. Default: all nodes\n");
		content.append("# Note: Will not consider unknown plugins nor unused nodes.\n");
		content.append("checkFor:\n");
		for (final String pluginName : this.checkFor) {
			content.append("- " + pluginName + "\n");
		}

		// apiKey. Default: empty
		content.append("# An API key is not required for the Updater to work,\n");
		content.append("# but you can define one here. Default: empty\n");
		content.append("#\n");
		content.append("# For now, the API key has no use, but there may be an\n");
		content.append("# anonymous request limitation in the future. If the Updater\n");
		content.append("# starts to fail a lot for no reason, populate this.\n");
		content.append("#\n");
		content.append("# Note: Generate a key from https://dev.bukkit.org/home/servermods-apikey/\n");
		content.append("#       You must be logged in.\n");
		content.append("apiKey: " + this.apiKey + "\n");

		// proxyAddress. Default: empty
		content.append("# Proxy informations for the Updater. Default: empty\n");
		if (this.proxyAddress == null) {
			content.append("proxyHost: \"\"\n");
			content.append("proxyPort: \"\"\n");
		} else {
			content.append("proxyHost: \"" + this.proxyAddress.getHostName() + "\"\n");
			content.append("proxyPort: " + this.proxyAddress.getPort() + "\n");
		}

		// debugEnabled. Default: empty
		content.append("# Enables debug mode for each specific node. Default: empty\n");
		content.append("debugEnabled:\n");
		for (final String debugged : this.debugEnabled) {
			content.append("- " + debugged + "\n");
		}

		return content.toString();
	}

	public boolean isUpdateCheck() {
		return updateCheck;
	}

	public void setUpdateCheck(final boolean updateCheck) {
		this.updateCheck = updateCheck;
	}

	public List<String> getCheckFor() {
		return checkFor;
	}

	public void setCheckFor(final List<String> checkFor) {
		this.checkFor = checkFor;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	public InetSocketAddress getProxyAddress() {
		return proxyAddress;
	}

	public void setProxyAddress(final InetSocketAddress proxyAddress) {
		this.proxyAddress = proxyAddress;
	}

	public Proxy getProxy() {
		if (this.proxyAddress != null) {
			return new Proxy(Proxy.Type.HTTP, this.proxyAddress);
		} else {
			return null;
		}
	}

	public List<String> getDebugEnabled() {
		return this.debugEnabled;
	}

	public boolean isDebugEnabled(final String nodeName) {
		return this.debugEnabled.contains(nodeName);
	}

	public void setDebugEnabled(final List<String> debugEnabled) {
		this.debugEnabled = debugEnabled;
	}
}

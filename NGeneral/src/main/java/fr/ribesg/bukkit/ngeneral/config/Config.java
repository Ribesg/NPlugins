package fr.ribesg.bukkit.ngeneral.config;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.common.FrameBuilder;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.logging.Logger;

public class Config extends AbstractConfig<NGeneral> {

	private final Logger log;

	// Features control
	private boolean godModeFeature;
	private boolean flyModeFeature;
	private boolean protectionSignFeature;

	private static final String DEFAULT_protectionSignNoPermMsgLine1 = "&4You don't";
	private static final String DEFAULT_protectionSignNoPermMsgLine2 = "&4have the";
	private static final String DEFAULT_protectionSignNoPermMsgLine3 = "&4permission!";
	private String protectionSignNoPermMsgLine1;
	private String protectionSignNoPermMsgLine2;
	private String protectionSignNoPermMsgLine3;

	private static final String DEFAULT_protectionSignNothingToProtectMsgLine1 = "&4Nothing to";
	private static final String DEFAULT_protectionSignNothingToProtectMsgLine2 = "&4protect!";
	private static final String DEFAULT_protectionSignNothingToProtectMsgLine3 = "";
	private String protectionSignNothingToProtectMsgLine1;
	private String protectionSignNothingToProtectMsgLine2;
	private String protectionSignNothingToProtectMsgLine3;

	private static final String DEFAULT_protectionSignAlreadyProtectedMsgLine1 = "&4Already";
	private static final String DEFAULT_protectionSignAlreadyProtectedMsgLine2 = "&4protected!";
	private static final String DEFAULT_protectionSignAlreadyProtectedMsgLine3 = "";
	private String protectionSignAlreadyProtectedMsgLine1;
	private String protectionSignAlreadyProtectedMsgLine2;
	private String protectionSignAlreadyProtectedMsgLine3;

	// Misc options
	private boolean broadCastOnAfk;

	public Config(NGeneral instance) {
		super(instance);
		log = instance.getLogger();

		godModeFeature = true;
		flyModeFeature = true;
		protectionSignFeature = true;

		broadCastOnAfk = true;

		protectionSignNoPermMsgLine1 = DEFAULT_protectionSignNoPermMsgLine1;
		protectionSignNoPermMsgLine2 = DEFAULT_protectionSignNoPermMsgLine2;
		protectionSignNoPermMsgLine3 = DEFAULT_protectionSignNoPermMsgLine3;

		protectionSignNothingToProtectMsgLine1 = DEFAULT_protectionSignNothingToProtectMsgLine1;
		protectionSignNothingToProtectMsgLine2 = DEFAULT_protectionSignNothingToProtectMsgLine2;
		protectionSignNothingToProtectMsgLine3 = DEFAULT_protectionSignNothingToProtectMsgLine3;

		protectionSignAlreadyProtectedMsgLine1 = DEFAULT_protectionSignAlreadyProtectedMsgLine1;
		protectionSignAlreadyProtectedMsgLine2 = DEFAULT_protectionSignAlreadyProtectedMsgLine2;
		protectionSignAlreadyProtectedMsgLine3 = DEFAULT_protectionSignAlreadyProtectedMsgLine3;
	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#handleValues(org.bukkit.configuration.file.YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// ######################
		// ## Features control ##
		// ######################

		// godModeFeature. Default: true.
		// Possible values: true, false
		setGodModeFeature(config.getBoolean("godModeFeature", true));

		// flyModeFeature. Default: true.
		// Possible values: true, false
		setFlyModeFeature(config.getBoolean("flyModeFeature", true));

		// protectionSignFeature. Default: true.
		// Possible values: true, false
		setProtectionSignFeature(config.getBoolean("protectionSignFeature", true));

		// protectionSignNoPermMsgLines.
		setProtectionSignNoPermMsgLine1(config.getString("protectionSignNoPermMsgLine1", DEFAULT_protectionSignNoPermMsgLine1));
		if (getProtectionSignNoPermMsgLine1().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignNoPermMsgLine1",
			           getProtectionSignNoPermMsgLine1(),
			           DEFAULT_protectionSignNoPermMsgLine1);
			setProtectionSignNoPermMsgLine1(DEFAULT_protectionSignNoPermMsgLine1);
		}
		setProtectionSignNoPermMsgLine2(config.getString("protectionSignNoPermMsgLine2", DEFAULT_protectionSignNoPermMsgLine2));
		if (getProtectionSignNoPermMsgLine2().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignNoPermMsgLine2",
			           getProtectionSignNoPermMsgLine2(),
			           DEFAULT_protectionSignNoPermMsgLine2);
			setProtectionSignNoPermMsgLine2(DEFAULT_protectionSignNoPermMsgLine2);
		}
		setProtectionSignNoPermMsgLine3(config.getString("protectionSignNoPermMsgLine3", DEFAULT_protectionSignNoPermMsgLine3));
		if (getProtectionSignNoPermMsgLine3().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignNoPermMsgLine3",
			           getProtectionSignNoPermMsgLine3(),
			           DEFAULT_protectionSignNoPermMsgLine3);
			setProtectionSignNoPermMsgLine3(DEFAULT_protectionSignNoPermMsgLine3);
		}

		// protectionSignNothingToProtectMsgLines.
		setProtectionSignNothingToProtectMsgLine1(config.getString("protectionSignNothingToProtectMsgLine1",
		                                                           DEFAULT_protectionSignNothingToProtectMsgLine1));
		if (getProtectionSignNothingToProtectMsgLine1().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignNothingToProtectMsgLine1",
			           getProtectionSignNothingToProtectMsgLine1(),
			           DEFAULT_protectionSignNothingToProtectMsgLine1);
			setProtectionSignNothingToProtectMsgLine1(DEFAULT_protectionSignNothingToProtectMsgLine1);
		}
		setProtectionSignNothingToProtectMsgLine2(config.getString("protectionSignNothingToProtectMsgLine2",
		                                                           DEFAULT_protectionSignNothingToProtectMsgLine2));
		if (getProtectionSignNothingToProtectMsgLine2().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignNothingToProtectMsgLine2",
			           getProtectionSignNothingToProtectMsgLine2(),
			           DEFAULT_protectionSignNothingToProtectMsgLine2);
			setProtectionSignNothingToProtectMsgLine2(DEFAULT_protectionSignNothingToProtectMsgLine2);
		}
		setProtectionSignNothingToProtectMsgLine3(config.getString("protectionSignNothingToProtectMsgLine3",
		                                                           DEFAULT_protectionSignNothingToProtectMsgLine3));
		if (getProtectionSignNothingToProtectMsgLine3().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignNothingToProtectMsgLine3",
			           getProtectionSignNothingToProtectMsgLine3(),
			           DEFAULT_protectionSignNothingToProtectMsgLine3);
			setProtectionSignNothingToProtectMsgLine3(DEFAULT_protectionSignNothingToProtectMsgLine3);
		}

		// protectionSignAlreadyProtectedMsgLines.
		setProtectionSignAlreadyProtectedMsgLine1(config.getString("protectionSignAlreadyProtectedMsgLine1",
		                                                           DEFAULT_protectionSignAlreadyProtectedMsgLine1));
		if (getProtectionSignAlreadyProtectedMsgLine1().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignAlreadyProtectedMsgLine1",
			           getProtectionSignAlreadyProtectedMsgLine1(),
			           DEFAULT_protectionSignAlreadyProtectedMsgLine1);
			setProtectionSignAlreadyProtectedMsgLine1(DEFAULT_protectionSignAlreadyProtectedMsgLine1);
		}
		setProtectionSignAlreadyProtectedMsgLine2(config.getString("protectionSignAlreadyProtectedMsgLine2",
		                                                           DEFAULT_protectionSignAlreadyProtectedMsgLine2));
		if (getProtectionSignAlreadyProtectedMsgLine2().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignAlreadyProtectedMsgLine2",
			           getProtectionSignAlreadyProtectedMsgLine2(),
			           DEFAULT_protectionSignAlreadyProtectedMsgLine2);
			setProtectionSignAlreadyProtectedMsgLine2(DEFAULT_protectionSignAlreadyProtectedMsgLine2);
		}
		setProtectionSignAlreadyProtectedMsgLine3(config.getString("protectionSignAlreadyProtectedMsgLine3",
		                                                           DEFAULT_protectionSignAlreadyProtectedMsgLine3));
		if (getProtectionSignAlreadyProtectedMsgLine3().length() > 15) {
			wrongValue("config.yml",
			           "protectionSignAlreadyProtectedMsgLine3",
			           getProtectionSignAlreadyProtectedMsgLine3(),
			           DEFAULT_protectionSignAlreadyProtectedMsgLine3);
			setProtectionSignAlreadyProtectedMsgLine3(DEFAULT_protectionSignAlreadyProtectedMsgLine3);
		}

		// ##################
		// ## Misc options ##
		// ##################

		// broadCastOnAfk. Default: true.
		// Possible values: true, false
		setBroadCastOnAfk(config.getBoolean("broadCastOnAfk", true));

	}

	/** @see fr.ribesg.bukkit.ncore.AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NGeneral plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// ######################
		// ## Features control ##
		// ######################

		frame = new FrameBuilder();
		frame.addLine("Features control", FrameBuilder.Option.CENTER);
		content.append('\n');
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// God Mode Feature
		content.append("# Defines if the GodMode feature is enabled or not\n");
		content.append("godModeFeature: " + hasGodModeFeature() + "\n\n");

		// Fly Mode Feature
		content.append("# Defines if the FlyMode feature is enabled or not\n");
		content.append("flyModeFeature: " + hasFlyModeFeature() + "\n\n");

		// Protection Sign Feature
		content.append("# Defines if the ProtectionSign feature is enabled or not\n");
		content.append("protectionSignFeature: " + hasProtectionSignFeature() + "\n\n");

		// No Permission for Protection Signs message
		content.append("# Message written on Error signs when player is not allowed to\n");
		content.append("# place a Protection sign. Maximum length 15 characters!\n");
		content.append("# Default values:\n");
		content.append("#   \"" + DEFAULT_protectionSignNoPermMsgLine1 + "\"\n");
		content.append("#   \"" + DEFAULT_protectionSignNoPermMsgLine2 + "\"\n");
		content.append("#   \"" + DEFAULT_protectionSignNoPermMsgLine3 + "\"\n");
		content.append("protectionSignNoPermMsgLine1: \"" + getProtectionSignNoPermMsgLine1() + "\"\n");
		content.append("protectionSignNoPermMsgLine2: \"" + getProtectionSignNoPermMsgLine2() + "\"\n");
		content.append("protectionSignNoPermMsgLine3: \"" + getProtectionSignNoPermMsgLine3() + "\"\n\n");

		// Nothing to protect Protection Signs message
		content.append("# Message written on Error signs when there is not valid 'protectable'\n");
		content.append("# block around the sign. Maximum length 15 characters!\n");
		content.append("# Default values:\n");
		content.append("#   \"" + DEFAULT_protectionSignNothingToProtectMsgLine1 + "\"\n");
		content.append("#   \"" + DEFAULT_protectionSignNothingToProtectMsgLine1 + "\"\n");
		content.append("#   \"" + DEFAULT_protectionSignNothingToProtectMsgLine1 + "\"\n");
		content.append("protectionSignNothingToProtectMsgLine1: \"" + getProtectionSignNothingToProtectMsgLine1() + "\"\n");
		content.append("protectionSignNothingToProtectMsgLine2: \"" + getProtectionSignNothingToProtectMsgLine2() + "\"\n");
		content.append("protectionSignNothingToProtectMsgLine3: \"" + getProtectionSignNothingToProtectMsgLine3() + "\"\n\n");

		// Nothing to protect Protection Signs message
		content.append("# Message written on Error signs when a block that would be protected by the\n");
		content.append("# placed sign is already protected by another sign. Maximum length 15 characters!\n");
		content.append("# Default values:\n");
		content.append("#   \"" + DEFAULT_protectionSignAlreadyProtectedMsgLine1 + "\"\n");
		content.append("#   \"" + DEFAULT_protectionSignAlreadyProtectedMsgLine1 + "\"\n");
		content.append("#   \"" + DEFAULT_protectionSignAlreadyProtectedMsgLine1 + "\"\n");
		content.append("protectionSignAlreadyProtectedMsgLine1: \"" + getProtectionSignAlreadyProtectedMsgLine1() + "\"\n");
		content.append("protectionSignAlreadyProtectedMsgLine2: \"" + getProtectionSignAlreadyProtectedMsgLine2() + "\"\n");
		content.append("protectionSignAlreadyProtectedMsgLine3: \"" + getProtectionSignAlreadyProtectedMsgLine3() + "\"\n\n");

		// ##################
		// ## Misc options ##
		// ##################

		frame = new FrameBuilder();
		frame.addLine("Misc options", FrameBuilder.Option.CENTER);
		content.append('\n');
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// Broadcast on AFK
		content.append("# Defines if we broadcast a message when a player changes his AFK state\n");
		content.append("broadCastOnAfk: " + hasBroadCastOnAfk() + "\n\n");

		return content.toString();
	}

	// Getters and Setters for config values

	public boolean hasFlyModeFeature() {
		return flyModeFeature;
	}

	public void setFlyModeFeature(boolean flyModeFeature) {
		this.flyModeFeature = flyModeFeature;
	}

	public boolean hasGodModeFeature() {
		return godModeFeature;
	}

	public void setGodModeFeature(boolean godModeFeature) {
		this.godModeFeature = godModeFeature;
	}

	public boolean hasProtectionSignFeature() {
		return protectionSignFeature;
	}

	public void setProtectionSignFeature(boolean protectionSignFeature) {
		this.protectionSignFeature = protectionSignFeature;
	}

	public boolean hasBroadCastOnAfk() {
		return broadCastOnAfk;
	}

	public void setBroadCastOnAfk(boolean broadCastOnAfk) {
		this.broadCastOnAfk = broadCastOnAfk;
	}

	public String getProtectionSignNoPermMsgLine1() {
		return protectionSignNoPermMsgLine1;
	}

	public void setProtectionSignNoPermMsgLine1(String protectionSignNoPermMsgLine1) {
		this.protectionSignNoPermMsgLine1 = protectionSignNoPermMsgLine1;
	}

	public String getProtectionSignNoPermMsgLine2() {
		return protectionSignNoPermMsgLine2;
	}

	public void setProtectionSignNoPermMsgLine2(String protectionSignNoPermMsgLine2) {
		this.protectionSignNoPermMsgLine2 = protectionSignNoPermMsgLine2;
	}

	public String getProtectionSignNoPermMsgLine3() {
		return protectionSignNoPermMsgLine3;
	}

	public void setProtectionSignNoPermMsgLine3(String protectionSignNoPermMsgLine3) {
		this.protectionSignNoPermMsgLine3 = protectionSignNoPermMsgLine3;
	}

	public String getProtectionSignNothingToProtectMsgLine1() {
		return protectionSignNothingToProtectMsgLine1;
	}

	public void setProtectionSignNothingToProtectMsgLine1(String protectionSignNothingToProtectMsgLine1) {
		this.protectionSignNothingToProtectMsgLine1 = protectionSignNothingToProtectMsgLine1;
	}

	public String getProtectionSignNothingToProtectMsgLine2() {
		return protectionSignNothingToProtectMsgLine2;
	}

	public void setProtectionSignNothingToProtectMsgLine2(String protectionSignNothingToProtectMsgLine2) {
		this.protectionSignNothingToProtectMsgLine2 = protectionSignNothingToProtectMsgLine2;
	}

	public String getProtectionSignNothingToProtectMsgLine3() {
		return protectionSignNothingToProtectMsgLine3;
	}

	public void setProtectionSignNothingToProtectMsgLine3(String protectionSignNothingToProtectMsgLine3) {
		this.protectionSignNothingToProtectMsgLine3 = protectionSignNothingToProtectMsgLine3;
	}

	public String getProtectionSignAlreadyProtectedMsgLine1() {
		return protectionSignAlreadyProtectedMsgLine1;
	}

	public void setProtectionSignAlreadyProtectedMsgLine1(String protectionSignAlreadyProtectedMsgLine1) {
		this.protectionSignAlreadyProtectedMsgLine1 = protectionSignAlreadyProtectedMsgLine1;
	}

	public String getProtectionSignAlreadyProtectedMsgLine2() {
		return protectionSignAlreadyProtectedMsgLine2;
	}

	public void setProtectionSignAlreadyProtectedMsgLine2(String protectionSignAlreadyProtectedMsgLine2) {
		this.protectionSignAlreadyProtectedMsgLine2 = protectionSignAlreadyProtectedMsgLine2;
	}

	public String getProtectionSignAlreadyProtectedMsgLine3() {
		return protectionSignAlreadyProtectedMsgLine3;
	}

	public void setProtectionSignAlreadyProtectedMsgLine3(String protectionSignAlreadyProtectedMsgLine3) {
		this.protectionSignAlreadyProtectedMsgLine3 = protectionSignAlreadyProtectedMsgLine3;
	}
}

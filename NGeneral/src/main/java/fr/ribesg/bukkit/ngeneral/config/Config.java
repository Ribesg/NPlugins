/***************************************************************************
 * Project file:    NPlugins - NGeneral - Config.java                      *
 * Full Class name: fr.ribesg.bukkit.ngeneral.config.Config                *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.config;

import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ngeneral.NGeneral;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends AbstractConfig<NGeneral> {

    // Features control
    private String initialMessage;

    private boolean autoAfkFeature;
    private boolean flyModeFeature;
    private boolean godModeFeature;
    private boolean itemNetworkFeature;
    private boolean protectionSignFeature;
    private boolean spyModeFeature;

    private int autoAfkDelay;
    private int itemNetworkMaxDistance;

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

    private static final String DEFAULT_itemNetworkSignUnknownNetworkMsgLine1 = "&4Unknown";
    private static final String DEFAULT_itemNetworkSignUnknownNetworkMsgLine2 = "&4network!";
    private static final String DEFAULT_itemNetworkSignUnknownNetworkMsgLine3 = "";
    private String itemNetworkSignUnknownNetworkMsgLine1;
    private String itemNetworkSignUnknownNetworkMsgLine2;
    private String itemNetworkSignUnknownNetworkMsgLine3;

    private static final String DEFAULT_itemNetworkSignNotAllowedMsgLine1 = "&4Not your";
    private static final String DEFAULT_itemNetworkSignNotAllowedMsgLine2 = "&4network!";
    private static final String DEFAULT_itemNetworkSignNotAllowedMsgLine3 = "";
    private String itemNetworkSignNotAllowedMsgLine1;
    private String itemNetworkSignNotAllowedMsgLine2;
    private String itemNetworkSignNotAllowedMsgLine3;

    private static final String DEFAULT_itemNetworkSignInvalidMaterialsMsgLine1 = "&4Invalid";
    private static final String DEFAULT_itemNetworkSignInvalidMaterialsMsgLine2 = "&4accepted";
    private static final String DEFAULT_itemNetworkSignInvalidMaterialsMsgLine3 = "&4materials!";
    private String itemNetworkSignInvalidMaterialsMsgLine1;
    private String itemNetworkSignInvalidMaterialsMsgLine2;
    private String itemNetworkSignInvalidMaterialsMsgLine3;

    private static final String DEFAULT_itemNetworkSignTooFarMsgLine1 = "&4Too";
    private static final String DEFAULT_itemNetworkSignTooFarMsgLine2 = "&4far!";
    private static final String DEFAULT_itemNetworkSignTooFarMsgLine3 = "";
    private String itemNetworkSignTooFarMsgLine1;
    private String itemNetworkSignTooFarMsgLine2;
    private String itemNetworkSignTooFarMsgLine3;

    // Misc options
    private boolean broadCastOnAfk;
    private boolean broadCastOnBusy;

    public Config(final NGeneral instance) {
        super(instance);

        this.initialMessage = "";

        this.autoAfkFeature = true;
        this.flyModeFeature = true;
        this.godModeFeature = true;
        this.itemNetworkFeature = true;
        this.protectionSignFeature = true;
        this.spyModeFeature = true;

        this.autoAfkDelay = 120;
        this.itemNetworkMaxDistance = 100;

        this.broadCastOnAfk = true;
        this.broadCastOnBusy = true;

        this.protectionSignNoPermMsgLine1 = DEFAULT_protectionSignNoPermMsgLine1;
        this.protectionSignNoPermMsgLine2 = DEFAULT_protectionSignNoPermMsgLine2;
        this.protectionSignNoPermMsgLine3 = DEFAULT_protectionSignNoPermMsgLine3;

        this.protectionSignNothingToProtectMsgLine1 = DEFAULT_protectionSignNothingToProtectMsgLine1;
        this.protectionSignNothingToProtectMsgLine2 = DEFAULT_protectionSignNothingToProtectMsgLine2;
        this.protectionSignNothingToProtectMsgLine3 = DEFAULT_protectionSignNothingToProtectMsgLine3;

        this.protectionSignAlreadyProtectedMsgLine1 = DEFAULT_protectionSignAlreadyProtectedMsgLine1;
        this.protectionSignAlreadyProtectedMsgLine2 = DEFAULT_protectionSignAlreadyProtectedMsgLine2;
        this.protectionSignAlreadyProtectedMsgLine3 = DEFAULT_protectionSignAlreadyProtectedMsgLine3;

        this.itemNetworkSignUnknownNetworkMsgLine1 = DEFAULT_itemNetworkSignUnknownNetworkMsgLine1;
        this.itemNetworkSignUnknownNetworkMsgLine2 = DEFAULT_itemNetworkSignUnknownNetworkMsgLine2;
        this.itemNetworkSignUnknownNetworkMsgLine3 = DEFAULT_itemNetworkSignUnknownNetworkMsgLine3;

        this.itemNetworkSignNotAllowedMsgLine1 = DEFAULT_itemNetworkSignNotAllowedMsgLine1;
        this.itemNetworkSignNotAllowedMsgLine2 = DEFAULT_itemNetworkSignNotAllowedMsgLine2;
        this.itemNetworkSignNotAllowedMsgLine3 = DEFAULT_itemNetworkSignNotAllowedMsgLine3;

        this.itemNetworkSignInvalidMaterialsMsgLine1 = DEFAULT_itemNetworkSignInvalidMaterialsMsgLine1;
        this.itemNetworkSignInvalidMaterialsMsgLine2 = DEFAULT_itemNetworkSignInvalidMaterialsMsgLine2;
        this.itemNetworkSignInvalidMaterialsMsgLine3 = DEFAULT_itemNetworkSignInvalidMaterialsMsgLine3;

        this.itemNetworkSignTooFarMsgLine1 = DEFAULT_itemNetworkSignTooFarMsgLine1;
        this.itemNetworkSignTooFarMsgLine2 = DEFAULT_itemNetworkSignTooFarMsgLine2;
        this.itemNetworkSignTooFarMsgLine3 = DEFAULT_itemNetworkSignTooFarMsgLine3;
    }

    @Override
    protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

        // ######################
        // ## Features control ##
        // ######################

        // initialMessage. Default: ""
        // Possible values: anything, multi-line with ##
        this.setInitialMessage(config.getString("initialMessage", ""));

        // autoAfkFeature. Default: true.
        // Possible values: true, false
        this.setAutoAfkFeature(config.getBoolean("autoAfkFeature", true));

        // flyModeFeature. Default: true.
        // Possible values: true, false
        this.setFlyModeFeature(config.getBoolean("flyModeFeature", true));

        // godModeFeature. Default: true.
        // Possible values: true, false
        this.setGodModeFeature(config.getBoolean("godModeFeature", true));

        // itemNetworkFeature. Default: true.
        // Possible values: true, false
        this.setItemNetworkFeature(config.getBoolean("itemNetworkFeature", true));

        // protectionSignFeature. Default: true.
        // Possible values: true, false
        this.setProtectionSignFeature(config.getBoolean("protectionSignFeature", true));

        // spyModeFeature. Default: true.
        // Possible values: true, false
        this.setSpyModeFeature(config.getBoolean("spyModeFeature", true));

        // autoAfkDelay. Default: 120
        // Possible values: any positive int
        this.setAutoAfkDelay(config.getInt("autoAfkDelay", 120));

        // itemNetworkMaxDistance. Default: 100
        // Possible values: any positive int
        this.setItemNetworkMaxDistance(config.getInt("itemNetworkMaxDistance", 100));

        // protectionSignNoPermMsgLines.
        this.setProtectionSignNoPermMsgLine1(config.getString("protectionSignNoPermMsgLine1", DEFAULT_protectionSignNoPermMsgLine1));
        if (this.protectionSignNoPermMsgLine1.length() > 15) {
            this.wrongValue("config.yml", "protectionSignNoPermMsgLine1", this.protectionSignNoPermMsgLine1, DEFAULT_protectionSignNoPermMsgLine1);
            this.setProtectionSignNoPermMsgLine1(DEFAULT_protectionSignNoPermMsgLine1);
        }

        this.setProtectionSignNoPermMsgLine2(config.getString("protectionSignNoPermMsgLine2", DEFAULT_protectionSignNoPermMsgLine2));
        if (this.protectionSignNoPermMsgLine2.length() > 15) {
            this.wrongValue("config.yml", "protectionSignNoPermMsgLine2", this.protectionSignNoPermMsgLine2, DEFAULT_protectionSignNoPermMsgLine2);
            this.setProtectionSignNoPermMsgLine2(DEFAULT_protectionSignNoPermMsgLine2);
        }

        this.setProtectionSignNoPermMsgLine3(config.getString("protectionSignNoPermMsgLine3", DEFAULT_protectionSignNoPermMsgLine3));
        if (this.protectionSignNoPermMsgLine3.length() > 15) {
            this.wrongValue("config.yml", "protectionSignNoPermMsgLine3", this.protectionSignNoPermMsgLine3, DEFAULT_protectionSignNoPermMsgLine3);
            this.setProtectionSignNoPermMsgLine3(DEFAULT_protectionSignNoPermMsgLine3);
        }

        // protectionSignNothingToProtectMsgLines.
        this.setProtectionSignNothingToProtectMsgLine1(config.getString("protectionSignNothingToProtectMsgLine1", DEFAULT_protectionSignNothingToProtectMsgLine1));
        if (this.protectionSignNothingToProtectMsgLine1.length() > 15) {
            this.wrongValue("config.yml", "protectionSignNothingToProtectMsgLine1", this.protectionSignNothingToProtectMsgLine1, DEFAULT_protectionSignNothingToProtectMsgLine1);
            this.setProtectionSignNothingToProtectMsgLine1(DEFAULT_protectionSignNothingToProtectMsgLine1);
        }

        this.setProtectionSignNothingToProtectMsgLine2(config.getString("protectionSignNothingToProtectMsgLine2", DEFAULT_protectionSignNothingToProtectMsgLine2));
        if (this.protectionSignNothingToProtectMsgLine2.length() > 15) {
            this.wrongValue("config.yml", "protectionSignNothingToProtectMsgLine2", this.protectionSignNothingToProtectMsgLine2, DEFAULT_protectionSignNothingToProtectMsgLine2);
            this.setProtectionSignNothingToProtectMsgLine2(DEFAULT_protectionSignNothingToProtectMsgLine2);
        }

        this.setProtectionSignNothingToProtectMsgLine3(config.getString("protectionSignNothingToProtectMsgLine3", DEFAULT_protectionSignNothingToProtectMsgLine3));
        if (this.protectionSignNothingToProtectMsgLine3.length() > 15) {
            this.wrongValue("config.yml", "protectionSignNothingToProtectMsgLine3", this.protectionSignNothingToProtectMsgLine3, DEFAULT_protectionSignNothingToProtectMsgLine3);
            this.setProtectionSignNothingToProtectMsgLine3(DEFAULT_protectionSignNothingToProtectMsgLine3);
        }

        // protectionSignAlreadyProtectedMsgLines.
        this.setProtectionSignAlreadyProtectedMsgLine1(config.getString("protectionSignAlreadyProtectedMsgLine1", DEFAULT_protectionSignAlreadyProtectedMsgLine1));
        if (this.protectionSignAlreadyProtectedMsgLine1.length() > 15) {
            this.wrongValue("config.yml", "protectionSignAlreadyProtectedMsgLine1", this.protectionSignAlreadyProtectedMsgLine1, DEFAULT_protectionSignAlreadyProtectedMsgLine1);
            this.setProtectionSignAlreadyProtectedMsgLine1(DEFAULT_protectionSignAlreadyProtectedMsgLine1);
        }

        this.setProtectionSignAlreadyProtectedMsgLine2(config.getString("protectionSignAlreadyProtectedMsgLine2", DEFAULT_protectionSignAlreadyProtectedMsgLine2));
        if (this.protectionSignAlreadyProtectedMsgLine2.length() > 15) {
            this.wrongValue("config.yml", "protectionSignAlreadyProtectedMsgLine2", this.protectionSignAlreadyProtectedMsgLine2, DEFAULT_protectionSignAlreadyProtectedMsgLine2);
            this.setProtectionSignAlreadyProtectedMsgLine2(DEFAULT_protectionSignAlreadyProtectedMsgLine2);
        }

        this.setProtectionSignAlreadyProtectedMsgLine3(config.getString("protectionSignAlreadyProtectedMsgLine3", DEFAULT_protectionSignAlreadyProtectedMsgLine3));
        if (this.protectionSignAlreadyProtectedMsgLine3.length() > 15) {
            this.wrongValue("config.yml", "protectionSignAlreadyProtectedMsgLine3", this.protectionSignAlreadyProtectedMsgLine3, DEFAULT_protectionSignAlreadyProtectedMsgLine3);
            this.setProtectionSignAlreadyProtectedMsgLine3(DEFAULT_protectionSignAlreadyProtectedMsgLine3);
        }

        // itemNetworkSignUnknownNetworkMsgLines.
        this.setItemNetworkSignUnknownNetworkMsgLine1(config.getString("itemNetworkSignUnknownNetworkMsgLine1", DEFAULT_itemNetworkSignUnknownNetworkMsgLine1));
        if (this.itemNetworkSignUnknownNetworkMsgLine1.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignUnknownNetworkMsgLine1", this.itemNetworkSignUnknownNetworkMsgLine1, DEFAULT_itemNetworkSignUnknownNetworkMsgLine1);
            this.setItemNetworkSignUnknownNetworkMsgLine1(DEFAULT_itemNetworkSignUnknownNetworkMsgLine1);
        }

        this.setItemNetworkSignUnknownNetworkMsgLine2(config.getString("itemNetworkSignUnknownNetworkMsgLine2", DEFAULT_itemNetworkSignUnknownNetworkMsgLine2));
        if (this.itemNetworkSignUnknownNetworkMsgLine2.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignUnknownNetworkMsgLine2", this.itemNetworkSignUnknownNetworkMsgLine2, DEFAULT_itemNetworkSignUnknownNetworkMsgLine2);
            this.setItemNetworkSignUnknownNetworkMsgLine2(DEFAULT_itemNetworkSignUnknownNetworkMsgLine2);
        }

        this.setItemNetworkSignUnknownNetworkMsgLine3(config.getString("itemNetworkSignUnknownNetworkMsgLine3", DEFAULT_itemNetworkSignUnknownNetworkMsgLine3));
        if (this.itemNetworkSignUnknownNetworkMsgLine3.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignUnknownNetworkMsgLine3", this.itemNetworkSignUnknownNetworkMsgLine3, DEFAULT_itemNetworkSignUnknownNetworkMsgLine3);
            this.setItemNetworkSignUnknownNetworkMsgLine3(DEFAULT_itemNetworkSignUnknownNetworkMsgLine3);
        }

        // itemNetworkSignNotAllowedMsgLines.
        this.setItemNetworkSignNotAllowedMsgLine1(config.getString("itemNetworkSignNotAllowedMsgLine1", DEFAULT_itemNetworkSignNotAllowedMsgLine1));
        if (this.itemNetworkSignNotAllowedMsgLine1.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignNotAllowedMsgLine1", this.itemNetworkSignNotAllowedMsgLine1, DEFAULT_itemNetworkSignNotAllowedMsgLine1);
            this.setItemNetworkSignNotAllowedMsgLine1(DEFAULT_itemNetworkSignNotAllowedMsgLine1);
        }

        this.setItemNetworkSignNotAllowedMsgLine2(config.getString("itemNetworkSignNotAllowedMsgLine2", DEFAULT_itemNetworkSignNotAllowedMsgLine2));
        if (this.itemNetworkSignNotAllowedMsgLine2.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignNotAllowedMsgLine2", this.itemNetworkSignNotAllowedMsgLine2, DEFAULT_itemNetworkSignNotAllowedMsgLine2);
            this.setItemNetworkSignNotAllowedMsgLine2(DEFAULT_itemNetworkSignNotAllowedMsgLine2);
        }

        this.setItemNetworkSignNotAllowedMsgLine3(config.getString("itemNetworkSignNotAllowedMsgLine3", DEFAULT_itemNetworkSignNotAllowedMsgLine3));
        if (this.itemNetworkSignNotAllowedMsgLine3.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignNotAllowedMsgLine3", this.itemNetworkSignNotAllowedMsgLine3, DEFAULT_itemNetworkSignNotAllowedMsgLine3);
            this.setItemNetworkSignNotAllowedMsgLine3(DEFAULT_itemNetworkSignNotAllowedMsgLine3);
        }

        // itemNetworkSignInvalidMaterialsMsgLines.
        this.setItemNetworkSignInvalidMaterialsMsgLine1(config.getString("itemNetworkSignInvalidMaterialsMsgLine1", DEFAULT_itemNetworkSignInvalidMaterialsMsgLine1));
        if (this.itemNetworkSignInvalidMaterialsMsgLine1.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignInvalidMaterialsMsgLine1", this.itemNetworkSignInvalidMaterialsMsgLine1, DEFAULT_itemNetworkSignInvalidMaterialsMsgLine1);
            this.setItemNetworkSignInvalidMaterialsMsgLine1(DEFAULT_itemNetworkSignInvalidMaterialsMsgLine1);
        }

        this.setItemNetworkSignInvalidMaterialsMsgLine2(config.getString("itemNetworkSignInvalidMaterialsMsgLine2", DEFAULT_itemNetworkSignInvalidMaterialsMsgLine2));
        if (this.itemNetworkSignInvalidMaterialsMsgLine2.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignInvalidMaterialsMsgLine2", this.itemNetworkSignInvalidMaterialsMsgLine2, DEFAULT_itemNetworkSignInvalidMaterialsMsgLine2);
            this.setItemNetworkSignInvalidMaterialsMsgLine2(DEFAULT_itemNetworkSignInvalidMaterialsMsgLine2);
        }

        this.setItemNetworkSignInvalidMaterialsMsgLine3(config.getString("itemNetworkSignInvalidMaterialsMsgLine3", DEFAULT_itemNetworkSignInvalidMaterialsMsgLine3));
        if (this.itemNetworkSignInvalidMaterialsMsgLine3.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignInvalidMaterialsMsgLine3", this.itemNetworkSignInvalidMaterialsMsgLine3, DEFAULT_itemNetworkSignInvalidMaterialsMsgLine3);
            this.setItemNetworkSignInvalidMaterialsMsgLine3(DEFAULT_itemNetworkSignInvalidMaterialsMsgLine3);
        }

        // itemNetworkSignTooFarMsgLines.
        this.setItemNetworkSignTooFarMsgLine1(config.getString("itemNetworkSignTooFarMsgLine1", DEFAULT_itemNetworkSignTooFarMsgLine1));
        if (this.itemNetworkSignTooFarMsgLine1.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignTooFarMsgLine1", this.itemNetworkSignTooFarMsgLine1, DEFAULT_itemNetworkSignTooFarMsgLine1);
            this.setItemNetworkSignTooFarMsgLine1(DEFAULT_itemNetworkSignTooFarMsgLine1);
        }

        this.setItemNetworkSignTooFarMsgLine2(config.getString("itemNetworkSignTooFarMsgLine2", DEFAULT_itemNetworkSignTooFarMsgLine2));
        if (this.itemNetworkSignTooFarMsgLine2.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignTooFarMsgLine2", this.itemNetworkSignTooFarMsgLine2, DEFAULT_itemNetworkSignTooFarMsgLine2);
            this.setItemNetworkSignTooFarMsgLine2(DEFAULT_itemNetworkSignTooFarMsgLine2);
        }

        this.setItemNetworkSignTooFarMsgLine3(config.getString("itemNetworkSignTooFarMsgLine3", DEFAULT_itemNetworkSignTooFarMsgLine3));
        if (this.itemNetworkSignTooFarMsgLine3.length() > 15) {
            this.wrongValue("config.yml", "itemNetworkSignTooFarMsgLine3", this.itemNetworkSignTooFarMsgLine3, DEFAULT_itemNetworkSignTooFarMsgLine3);
            this.setItemNetworkSignTooFarMsgLine3(DEFAULT_itemNetworkSignTooFarMsgLine3);
        }

        // ##################
        // ## Misc options ##
        // ##################

        // broadCastOnAfk. Default: true.
        // Possible values: true, false
        this.setBroadCastOnAfk(config.getBoolean("broadCastOnAfk", true));

        // broadCastOnBusy. Default: true.
        // Possible values: true, false
        this.setBroadCastOnBusy(config.getBoolean("broadCastOnBusy", true));
    }

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

        // Initial Message
        content.append("# Initial Message. This will be sent to any connecting Player,\n");
        content.append("# followed by a bunch of spaces to hide it.\n");
        content.append("# You can specify different lines using the ## separator.\n");
        content.append("# For example, this is the perfect place to use Rei's Minimap codes.\n");
        content.append("initialMessage: " + this.initialMessage + "\n\n");

        // Auto AFK Feature
        content.append("# Defines if the AutoAfk feature is enabled or not\n");
        content.append("autoAfkFeature: " + this.hasAutoAfkFeature() + "\n\n");

        // Fly Mode Feature
        content.append("# Defines if the FlyMode feature is enabled or not\n");
        content.append("flyModeFeature: " + this.hasFlyModeFeature() + "\n\n");

        // God Mode Feature
        content.append("# Defines if the GodMode feature is enabled or not\n");
        content.append("godModeFeature: " + this.hasGodModeFeature() + "\n\n");

        // Item Network Feature
        content.append("# Defines if the ItemNetwork feature is enabled or not\n");
        content.append("itemNetworkFeature: " + this.hasItemNetworkFeature() + "\n\n");

        // Protection Sign Feature
        content.append("# Defines if the ProtectionSign feature is enabled or not\n");
        content.append("protectionSignFeature: " + this.hasProtectionSignFeature() + "\n\n");

        // Protection Sign Feature
        content.append("# Defines if the Spy Mode feature is enabled or not\n");
        content.append("spyModeFeature: " + this.hasSpyModeFeature() + "\n\n");

        // Auto AFK Delay
        content.append("# Time before a player is set to auto-afk\n");
        content.append("autoAfkDelay: " + this.autoAfkDelay + "\n\n");

        // Item Network Max Distance
        content.append("# Maximum allowed distance between an Emitter sign and any\n");
        content.append("# Receiver sign of the same network\n");
        content.append("itemNetworkMaxDistance: " + this.itemNetworkMaxDistance + "\n\n");

        // No Permission for Protection Signs message
        content.append("# Message written on Error signs when player is not allowed to\n");
        content.append("# place a Protection sign. Maximum length 15 characters!\n");
        content.append("# Default values:\n");
        content.append("#   \"" + DEFAULT_protectionSignNoPermMsgLine1 + "\"\n");
        content.append("#   \"" + DEFAULT_protectionSignNoPermMsgLine2 + "\"\n");
        content.append("#   \"" + DEFAULT_protectionSignNoPermMsgLine3 + "\"\n");
        content.append("protectionSignNoPermMsgLine1: \"" + this.protectionSignNoPermMsgLine1 + "\"\n");
        content.append("protectionSignNoPermMsgLine2: \"" + this.protectionSignNoPermMsgLine2 + "\"\n");
        content.append("protectionSignNoPermMsgLine3: \"" + this.protectionSignNoPermMsgLine3 + "\"\n\n");

        // Nothing to protect Protection Signs message
        content.append("# Message written on Error signs when there is not valid 'protectable'\n");
        content.append("# block around the sign. Maximum length 15 characters!\n");
        content.append("# Default values:\n");
        content.append("#   \"" + DEFAULT_protectionSignNothingToProtectMsgLine1 + "\"\n");
        content.append("#   \"" + DEFAULT_protectionSignNothingToProtectMsgLine2 + "\"\n");
        content.append("#   \"" + DEFAULT_protectionSignNothingToProtectMsgLine3 + "\"\n");
        content.append("protectionSignNothingToProtectMsgLine1: \"" + this.protectionSignNothingToProtectMsgLine1 + "\"\n");
        content.append("protectionSignNothingToProtectMsgLine2: \"" + this.protectionSignNothingToProtectMsgLine2 + "\"\n");
        content.append("protectionSignNothingToProtectMsgLine3: \"" + this.protectionSignNothingToProtectMsgLine3 + "\"\n\n");

        // Already protected Protection Signs message
        content.append("# Message written on Error signs when a block that would be protected by the\n");
        content.append("# placed sign is already protected by another sign. Maximum length 15 characters!\n");
        content.append("# Default values:\n");
        content.append("#   \"" + DEFAULT_protectionSignAlreadyProtectedMsgLine1 + "\"\n");
        content.append("#   \"" + DEFAULT_protectionSignAlreadyProtectedMsgLine2 + "\"\n");
        content.append("#   \"" + DEFAULT_protectionSignAlreadyProtectedMsgLine3 + "\"\n");
        content.append("protectionSignAlreadyProtectedMsgLine1: \"" + this.protectionSignAlreadyProtectedMsgLine1 + "\"\n");
        content.append("protectionSignAlreadyProtectedMsgLine2: \"" + this.protectionSignAlreadyProtectedMsgLine2 + "\"\n");
        content.append("protectionSignAlreadyProtectedMsgLine3: \"" + this.protectionSignAlreadyProtectedMsgLine3 + "\"\n\n");

        // Unknown Network Item Network Signs message
        content.append("# Message written on Error signs when the Network Name provided on the sign does not\n");
        content.append("# match any known valid Item Network. Maximum length 15 characters!\n");
        content.append("# Default values:\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignUnknownNetworkMsgLine1 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignUnknownNetworkMsgLine2 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignUnknownNetworkMsgLine3 + "\"\n");
        content.append("itemNetworkSignUnknownNetworkMsgLine1: \"" + this.itemNetworkSignUnknownNetworkMsgLine1 + "\"\n");
        content.append("itemNetworkSignUnknownNetworkMsgLine2: \"" + this.itemNetworkSignUnknownNetworkMsgLine2 + "\"\n");
        content.append("itemNetworkSignUnknownNetworkMsgLine3: \"" + this.itemNetworkSignUnknownNetworkMsgLine3 + "\"\n\n");

        // Not Allowed Item Network Signs message
        content.append("# Message written on Error signs when the User is not owner of the provided\n");
        content.append("# Item Network. Maximum length 15 characters!\n");
        content.append("# Default values:\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignNotAllowedMsgLine1 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignNotAllowedMsgLine2 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignNotAllowedMsgLine3 + "\"\n");
        content.append("itemNetworkSignNotAllowedMsgLine1: \"" + this.itemNetworkSignNotAllowedMsgLine1 + "\"\n");
        content.append("itemNetworkSignNotAllowedMsgLine2: \"" + this.itemNetworkSignNotAllowedMsgLine2 + "\"\n");
        content.append("itemNetworkSignNotAllowedMsgLine3: \"" + this.itemNetworkSignNotAllowedMsgLine3 + "\"\n\n");

        // Invalid Materials Item Network Signs message
        content.append("# Message written on Error signs when the 3rd line defining\n");
        content.append("# accepted materials is wrong. Maximum length 15 characters!\n");
        content.append("# Default values:\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignInvalidMaterialsMsgLine1 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignInvalidMaterialsMsgLine2 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignInvalidMaterialsMsgLine3 + "\"\n");
        content.append("itemNetworkSignInvalidMaterialsMsgLine1: \"" + this.itemNetworkSignInvalidMaterialsMsgLine1 + "\"\n");
        content.append("itemNetworkSignInvalidMaterialsMsgLine2: \"" + this.itemNetworkSignInvalidMaterialsMsgLine2 + "\"\n");
        content.append("itemNetworkSignInvalidMaterialsMsgLine3: \"" + this.itemNetworkSignInvalidMaterialsMsgLine3 + "\"\n\n");

        // Too Far Item Network Signs message
        content.append("# Message written on Error signs when the User tries to create a\n");
        content.append("# sign too far from other signs of the network. Maximum length 15 characters!\n");
        content.append("# Default values:\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignTooFarMsgLine1 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignTooFarMsgLine2 + "\"\n");
        content.append("#   \"" + DEFAULT_itemNetworkSignTooFarMsgLine3 + "\"\n");
        content.append("itemNetworkSignTooFarMsgLine1: \"" + this.itemNetworkSignTooFarMsgLine1 + "\"\n");
        content.append("itemNetworkSignTooFarMsgLine2: \"" + this.itemNetworkSignTooFarMsgLine2 + "\"\n");
        content.append("itemNetworkSignTooFarMsgLine3: \"" + this.itemNetworkSignTooFarMsgLine3 + "\"\n\n");

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
        content.append("broadCastOnAfk: " + this.hasBroadCastOnAfk() + "\n\n");

        // Broadcast on BUSY
        content.append("# Defines if we broadcast a message when a player changes his BUSY state\n");
        content.append("broadCastOnBusy: " + this.hasBroadCastOnBusy() + "\n\n");

        return content.toString();
    }

    // Getters and Setters for config values

    public boolean hasAutoAfkFeature() {
        return this.autoAfkFeature;
    }

    public void setAutoAfkFeature(final boolean autoAfkFeature) {
        this.autoAfkFeature = autoAfkFeature;
    }

    public boolean hasFlyModeFeature() {
        return this.flyModeFeature;
    }

    public void setFlyModeFeature(final boolean flyModeFeature) {
        this.flyModeFeature = flyModeFeature;
    }

    public boolean hasGodModeFeature() {
        return this.godModeFeature;
    }

    public void setGodModeFeature(final boolean godModeFeature) {
        this.godModeFeature = godModeFeature;
    }

    public boolean hasItemNetworkFeature() {
        return this.itemNetworkFeature;
    }

    public void setItemNetworkFeature(final boolean itemNetworkFeature) {
        this.itemNetworkFeature = itemNetworkFeature;
    }

    public boolean hasProtectionSignFeature() {
        return this.protectionSignFeature;
    }

    public void setProtectionSignFeature(final boolean protectionSignFeature) {
        this.protectionSignFeature = protectionSignFeature;
    }

    public boolean hasSpyModeFeature() {
        return this.spyModeFeature;
    }

    public void setSpyModeFeature(final boolean spyModeFeature) {
        this.spyModeFeature = spyModeFeature;
    }

    public int getAutoAfkDelay() {
        return this.autoAfkDelay;
    }

    public void setAutoAfkDelay(final int autoAfkDelay) {
        this.autoAfkDelay = autoAfkDelay;
    }

    public int getItemNetworkMaxDistance() {
        return this.itemNetworkMaxDistance;
    }

    public void setItemNetworkMaxDistance(final int itemNetworkMaxDistance) {
        this.itemNetworkMaxDistance = itemNetworkMaxDistance;
    }

    public boolean hasBroadCastOnAfk() {
        return this.broadCastOnAfk;
    }

    public void setBroadCastOnAfk(final boolean broadCastOnAfk) {
        this.broadCastOnAfk = broadCastOnAfk;
    }

    public boolean hasBroadCastOnBusy() {
        return this.broadCastOnBusy;
    }

    public void setBroadCastOnBusy(final boolean broadCastOnBusy) {
        this.broadCastOnBusy = broadCastOnBusy;
    }

    public String getProtectionSignNoPermMsgLine1() {
        return this.protectionSignNoPermMsgLine1;
    }

    public void setProtectionSignNoPermMsgLine1(final String protectionSignNoPermMsgLine1) {
        this.protectionSignNoPermMsgLine1 = protectionSignNoPermMsgLine1;
    }

    public String getProtectionSignNoPermMsgLine2() {
        return this.protectionSignNoPermMsgLine2;
    }

    public void setProtectionSignNoPermMsgLine2(final String protectionSignNoPermMsgLine2) {
        this.protectionSignNoPermMsgLine2 = protectionSignNoPermMsgLine2;
    }

    public String getProtectionSignNoPermMsgLine3() {
        return this.protectionSignNoPermMsgLine3;
    }

    public void setProtectionSignNoPermMsgLine3(final String protectionSignNoPermMsgLine3) {
        this.protectionSignNoPermMsgLine3 = protectionSignNoPermMsgLine3;
    }

    public String getProtectionSignNothingToProtectMsgLine1() {
        return this.protectionSignNothingToProtectMsgLine1;
    }

    public void setProtectionSignNothingToProtectMsgLine1(final String protectionSignNothingToProtectMsgLine1) {
        this.protectionSignNothingToProtectMsgLine1 = protectionSignNothingToProtectMsgLine1;
    }

    public String getProtectionSignNothingToProtectMsgLine2() {
        return this.protectionSignNothingToProtectMsgLine2;
    }

    public void setProtectionSignNothingToProtectMsgLine2(final String protectionSignNothingToProtectMsgLine2) {
        this.protectionSignNothingToProtectMsgLine2 = protectionSignNothingToProtectMsgLine2;
    }

    public String getProtectionSignNothingToProtectMsgLine3() {
        return this.protectionSignNothingToProtectMsgLine3;
    }

    public void setProtectionSignNothingToProtectMsgLine3(final String protectionSignNothingToProtectMsgLine3) {
        this.protectionSignNothingToProtectMsgLine3 = protectionSignNothingToProtectMsgLine3;
    }

    public String getProtectionSignAlreadyProtectedMsgLine1() {
        return this.protectionSignAlreadyProtectedMsgLine1;
    }

    public void setProtectionSignAlreadyProtectedMsgLine1(final String protectionSignAlreadyProtectedMsgLine1) {
        this.protectionSignAlreadyProtectedMsgLine1 = protectionSignAlreadyProtectedMsgLine1;
    }

    public String getProtectionSignAlreadyProtectedMsgLine2() {
        return this.protectionSignAlreadyProtectedMsgLine2;
    }

    public void setProtectionSignAlreadyProtectedMsgLine2(final String protectionSignAlreadyProtectedMsgLine2) {
        this.protectionSignAlreadyProtectedMsgLine2 = protectionSignAlreadyProtectedMsgLine2;
    }

    public String getProtectionSignAlreadyProtectedMsgLine3() {
        return this.protectionSignAlreadyProtectedMsgLine3;
    }

    public void setProtectionSignAlreadyProtectedMsgLine3(final String protectionSignAlreadyProtectedMsgLine3) {
        this.protectionSignAlreadyProtectedMsgLine3 = protectionSignAlreadyProtectedMsgLine3;
    }

    public String getInitialMessage() {
        return this.initialMessage;
    }

    public void setInitialMessage(final String initialMessage) {
        this.initialMessage = initialMessage;
    }

    public String getItemNetworkSignInvalidMaterialsMsgLine1() {
        return this.itemNetworkSignInvalidMaterialsMsgLine1;
    }

    public void setItemNetworkSignInvalidMaterialsMsgLine1(final String itemNetworkSignInvalidMaterialsMsgLine1) {
        this.itemNetworkSignInvalidMaterialsMsgLine1 = itemNetworkSignInvalidMaterialsMsgLine1;
    }

    public String getItemNetworkSignInvalidMaterialsMsgLine2() {
        return this.itemNetworkSignInvalidMaterialsMsgLine2;
    }

    public void setItemNetworkSignInvalidMaterialsMsgLine2(final String itemNetworkSignInvalidMaterialsMsgLine2) {
        this.itemNetworkSignInvalidMaterialsMsgLine2 = itemNetworkSignInvalidMaterialsMsgLine2;
    }

    public String getItemNetworkSignInvalidMaterialsMsgLine3() {
        return this.itemNetworkSignInvalidMaterialsMsgLine3;
    }

    public void setItemNetworkSignInvalidMaterialsMsgLine3(final String itemNetworkSignInvalidMaterialsMsgLine3) {
        this.itemNetworkSignInvalidMaterialsMsgLine3 = itemNetworkSignInvalidMaterialsMsgLine3;
    }

    public String getItemNetworkSignTooFarMsgLine1() {
        return this.itemNetworkSignTooFarMsgLine1;
    }

    public void setItemNetworkSignTooFarMsgLine1(final String itemNetworkSignTooFarMsgLine1) {
        this.itemNetworkSignTooFarMsgLine1 = itemNetworkSignTooFarMsgLine1;
    }

    public String getItemNetworkSignTooFarMsgLine2() {
        return this.itemNetworkSignTooFarMsgLine2;
    }

    public void setItemNetworkSignTooFarMsgLine2(final String itemNetworkSignTooFarMsgLine2) {
        this.itemNetworkSignTooFarMsgLine2 = itemNetworkSignTooFarMsgLine2;
    }

    public String getItemNetworkSignTooFarMsgLine3() {
        return this.itemNetworkSignTooFarMsgLine3;
    }

    public void setItemNetworkSignTooFarMsgLine3(final String itemNetworkSignTooFarMsgLine3) {
        this.itemNetworkSignTooFarMsgLine3 = itemNetworkSignTooFarMsgLine3;
    }

    public String getItemNetworkSignNotAllowedMsgLine1() {
        return this.itemNetworkSignNotAllowedMsgLine1;
    }

    public void setItemNetworkSignNotAllowedMsgLine1(final String itemNetworkSignNotAllowedMsgLine1) {
        this.itemNetworkSignNotAllowedMsgLine1 = itemNetworkSignNotAllowedMsgLine1;
    }

    public String getItemNetworkSignNotAllowedMsgLine2() {
        return this.itemNetworkSignNotAllowedMsgLine2;
    }

    public void setItemNetworkSignNotAllowedMsgLine2(final String itemNetworkSignNotAllowedMsgLine2) {
        this.itemNetworkSignNotAllowedMsgLine2 = itemNetworkSignNotAllowedMsgLine2;
    }

    public String getItemNetworkSignNotAllowedMsgLine3() {
        return this.itemNetworkSignNotAllowedMsgLine3;
    }

    public void setItemNetworkSignNotAllowedMsgLine3(final String itemNetworkSignNotAllowedMsgLine3) {
        this.itemNetworkSignNotAllowedMsgLine3 = itemNetworkSignNotAllowedMsgLine3;
    }

    public String getItemNetworkSignUnknownNetworkMsgLine1() {
        return this.itemNetworkSignUnknownNetworkMsgLine1;
    }

    public void setItemNetworkSignUnknownNetworkMsgLine1(final String itemNetworkSignUnknownNetworkMsgLine1) {
        this.itemNetworkSignUnknownNetworkMsgLine1 = itemNetworkSignUnknownNetworkMsgLine1;
    }

    public String getItemNetworkSignUnknownNetworkMsgLine2() {
        return this.itemNetworkSignUnknownNetworkMsgLine2;
    }

    public void setItemNetworkSignUnknownNetworkMsgLine2(final String itemNetworkSignUnknownNetworkMsgLine2) {
        this.itemNetworkSignUnknownNetworkMsgLine2 = itemNetworkSignUnknownNetworkMsgLine2;
    }

    public String getItemNetworkSignUnknownNetworkMsgLine3() {
        return this.itemNetworkSignUnknownNetworkMsgLine3;
    }

    public void setItemNetworkSignUnknownNetworkMsgLine3(final String itemNetworkSignUnknownNetworkMsgLine3) {
        this.itemNetworkSignUnknownNetworkMsgLine3 = itemNetworkSignUnknownNetworkMsgLine3;
    }
}

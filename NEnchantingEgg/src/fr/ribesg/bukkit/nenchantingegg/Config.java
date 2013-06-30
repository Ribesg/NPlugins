package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends AbstractConfig<NEnchantingEgg> {

    // @Getter @Setter(AccessLevel.PRIVATE) private int broadcastOnWorldCreate;

    public Config(final NEnchantingEgg instance) {
        super(instance);

    }

    /** @see AbstractConfig#setValues(YamlConfiguration) */
    @Override
    protected void setValues(final YamlConfiguration config) {

        // // broadcastOnWorldCreate. Default: 0.
        // // Possible values: 0,1
        // setBroadcastOnWorldCreate(config.getInt("broadcastOnWorldCreate", 0));
        // if (getBroadcastOnWorldCreate() < 0 || getBroadcastOnWorldCreate() > 1) {
        //     setBroadcastOnWorldCreate(0);
        //     plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, "config.yml", "broadcastOnWorldCreate", "0");
        // }
    }

    /** @see AbstractConfig#getConfigString() */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();

        // Header
        content.append("################################################################################\n");
        content.append("# Config file for NEnchantingEgg plugin. If you don't understand something,    #\n");
        content.append("# please ask on dev.bukkit.org or on forum post.                        Ribesg #\n");
        content.append("################################################################################\n\n");

        // // Broadcast on world creation
        // content.append("# Do we broadcast a message on World creation. Possible values: 0,1\n");
        // content.append("# Default : 0\n");
        // content.append("broadcastOnWorldCreate: " + getBroadcastOnWorldCreate() + "\n\n");

        return content.toString();
    }
}

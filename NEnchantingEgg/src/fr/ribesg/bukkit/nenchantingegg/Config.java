package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ncore.utils.NLocation;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.Altars;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class Config extends AbstractConfig<NEnchantingEgg> {

    private int    minimumDistanceBetweenTwoAltars;
    private double repairBoostMultiplier;

    private Altars altars;

    public Config(final NEnchantingEgg instance) {
        super(instance);

        altars = instance.getAltars();

        setMinimumDistanceBetweenTwoAltars(500);
        setRepairBoostMultiplier(2.5);
    }

    /** @see AbstractConfig#handleValues(YamlConfiguration) */
    @Override
    protected void handleValues(final YamlConfiguration config) {

        // minimumDistanceBetweenTwoAltars. Default: 500.
        // Possible values: Positive integer >= 35
        setMinimumDistanceBetweenTwoAltars(config.getInt("minimumDistanceBetweenTwoAltars", 500));
        if (getMinimumDistanceBetweenTwoAltars() < 35) {
            setMinimumDistanceBetweenTwoAltars(500);
            plugin.sendMessage(plugin.getServer().getConsoleSender(),
                               MessageId.incorrectValueInConfiguration,
                               "config.yml",
                               "minimumDistanceBetweenTwoAltars",
                               "500");
        }

        // boostMultiplier. Default: 2.5.
        // Possible values: Positive double
        setRepairBoostMultiplier(config.getDouble("repairBoostMultiplier", 2.5));
        if (getRepairBoostMultiplier() <= 0.0) {
            setRepairBoostMultiplier(2.5);
            plugin.sendMessage(plugin.getServer().getConsoleSender(),
                               MessageId.incorrectValueInConfiguration,
                               "config.yml",
                               "repairBoostMultiplier",
                               "2.5");
        }

        if (config.isList("altars")) {
            List<String> list = config.getStringList("altars");
            for (String s : list) {
                NLocation loc = NLocation.toNLocation(s);
                if (loc == null) {
                    plugin.getLogger().severe("Incorrect altar location (Malformed): \"" + s + "\"");
                    break;
                }
                Altar a = new Altar(plugin, loc);
                if (!altars.canAdd(a, getMinimumDistanceBetweenTwoAltars())) {
                    plugin.getLogger().severe("Incorrect altar location (Too close): \"" + s + "\"");
                    break;
                }
                altars.add(a);
            }
        }
    }

    /** @see AbstractConfig#getConfigString() */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        FrameBuilder frame;

        // Header
        frame = new FrameBuilder();
        frame.addLine("Config file for NEnchantingEgg plugin", FrameBuilder.Option.CENTER);
        frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
        frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
        for (final String line : frame.build()) {
            content.append(line + '\n');
        }

        // Minimum distance between 2 altars
        content.append("# The minimum distance between 2 altars. Default: 500\n");
        content.append("# Note: You can't use a value under 35.\n");
        content.append("minimumDistanceBetweenTwoAltars: " + getMinimumDistanceBetweenTwoAltars() + "\n\n");

        // Repair boost multiplier
        content.append("# The coefficient applied to durability boost on repair. Default: 2.5\n");
        content.append("# Note: You can't use a value equals to or under 0.0\n");
        content.append("repairBoostMultiplier: " + getRepairBoostMultiplier() + "\n\n");

        // Altars
        content.append("# This stores created altars\n");
        content.append("altars:\n");
        for (Altar a : altars.getAltars()) {
            content.append("- " + a.getCenterLocation().toString() + '\n');
        }

        return content.toString();
    }

    public int getMinimumDistanceBetweenTwoAltars() {
        return minimumDistanceBetweenTwoAltars;
    }

    public void setMinimumDistanceBetweenTwoAltars(int minimumDistanceBetweenTwoAltars) {
        this.minimumDistanceBetweenTwoAltars = minimumDistanceBetweenTwoAltars;
    }

    public double getRepairBoostMultiplier() {
        return repairBoostMultiplier;
    }

    public void setRepairBoostMultiplier(double repairBoostMultiplier) {
        this.repairBoostMultiplier = repairBoostMultiplier;
    }
}

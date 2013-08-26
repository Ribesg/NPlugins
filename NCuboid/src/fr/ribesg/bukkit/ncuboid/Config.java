package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The config for the NCuboid node
 *
 * @author Ribesg
 */
public class Config extends AbstractConfig<NCuboid> {

	private static Material selectionItemMaterial;

	/**
	 * Constructor
	 *
	 * @param instance Linked plugin instance
	 */
	public Config(NCuboid instance) {
		super(instance);
		setSelectionItemMaterial(Material.STICK);
	}

	/** @see AbstractConfig#handleValues(YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) {

		// selectionItemMaterial. Default : Stick/280
		final Material m = Material.getMaterial(config.getInt("selectionItemMaterial", 280));
		setSelectionItemMaterial(m == null ? Material.STICK : m);
	}

	/** @see AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NCuboid plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		// selectionItemMaterial. Default : Stick/280
		content.append("# The tool used to select points and get informations about blocks protection. Default : 180 (Stick)\n");
		content.append("selectionItemMaterial: " + getSelectionItemMaterial().getId() + "\n\n");

		return content.toString();
	}

	public static Material getSelectionItemMaterial() {
		return selectionItemMaterial;
	}

	private static void setSelectionItemMaterial(Material selectionItemMaterial) {
		Config.selectionItemMaterial = selectionItemMaterial;
	}
}

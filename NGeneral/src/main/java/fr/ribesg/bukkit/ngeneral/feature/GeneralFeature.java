package fr.ribesg.bukkit.ngeneral.feature;
import fr.ribesg.bukkit.ngeneral.NGeneral;

public abstract class GeneralFeature {

	protected final NGeneral plugin;

	protected GeneralFeature(final NGeneral instance) {
		this.plugin = instance;
	}

	public NGeneral getPlugin() {
		return this.plugin;
	}

	public abstract void init();

	public void disable() {
		// Should be overriden by Features that have to do something onDisable
	}
}

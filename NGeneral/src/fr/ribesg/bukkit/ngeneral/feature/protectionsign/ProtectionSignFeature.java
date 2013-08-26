package fr.ribesg.bukkit.ngeneral.feature.protectionsign;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.GeneralFeature;
import org.bukkit.Bukkit;

public class ProtectionSignFeature extends GeneralFeature {

	private final ProtectionSignListener listener;

	public ProtectionSignFeature(NGeneral instance) {
		super(instance);
		listener = new ProtectionSignListener(this);
	}

	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
	}
}

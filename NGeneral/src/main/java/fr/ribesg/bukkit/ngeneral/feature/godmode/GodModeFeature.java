package fr.ribesg.bukkit.ngeneral.feature.godmode;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.GeneralFeature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GodModeFeature extends GeneralFeature {

	private final GodModeListener        listener;
	private final GodModeCommandExecutor executor;

	private Set<String> godPlayers;

	public GodModeFeature(final NGeneral instance) {
		super(instance);
		listener = new GodModeListener(this);
		executor = new GodModeCommandExecutor(this);
		godPlayers = new HashSet<>();
	}

	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
		getPlugin().getCommand("god").setExecutor(executor);
	}

	public Set<String> getGodPlayers() {
		return godPlayers;
	}

	public boolean hasGodMode(String playerName) {
		return godPlayers.contains(playerName);
	}

	public void setGodMode(Player player, boolean value) {
		if (value) {
			godPlayers.add(player.getName());
			player.setHealth(player.getMaxHealth());
			player.setFoodLevel(20);
			player.setSaturation(20.0f);
		} else {
			godPlayers.remove(player.getName());
		}
	}
}

package fr.ribesg.bukkit.ngeneral.feature.flymode;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.GeneralFeature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class FlyModeFeature extends GeneralFeature {

	private final FlyModeListener        listener;
	private final FlyModeCommandExecutor executor;

	private Set<String> flyPlayers;

	public FlyModeFeature(NGeneral instance) {
		super(instance);
		listener = new FlyModeListener(this);
		executor = new FlyModeCommandExecutor(this);
		flyPlayers = new HashSet<>();
	}

	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
		getPlugin().getCommand("fly").setExecutor(executor);
	}

	@Override
	public void disable() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (!Perms.hasFly(player) && this.hasFlyMode(player.getName())) {
				player.setAllowFlight(false);
				this.setFlyMode(player, false);
				player.setFallDistance(-100f);
			}
		}
	}

	public Set<String> getFlyPlayers() {
		return flyPlayers;
	}

	public boolean hasFlyMode(String playerName) {
		return flyPlayers.contains(playerName);
	}

	public void setFlyMode(Player player, boolean value) {
		if (value) {
			flyPlayers.add(player.getName());
			player.setAllowFlight(true);
		} else {
			flyPlayers.remove(player.getName());
			player.setAllowFlight(false);
		}
	}
}

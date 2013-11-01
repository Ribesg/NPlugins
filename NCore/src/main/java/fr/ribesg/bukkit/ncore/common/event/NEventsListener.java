package fr.ribesg.bukkit.ncore.common.event;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NEventsListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Bukkit.getPluginManager().callEvent(new PlayerJoinedEvent(event.getPlayer()));
	}
}

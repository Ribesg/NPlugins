package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFlagListener extends AbstractListener {

	public ChatFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		final GeneralCuboid c = getPlugin().getDb().getPriorByLocation(event.getPlayer().getLocation());
		if (c.getFlag(Flag.CHAT) && !c.isAllowedPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}

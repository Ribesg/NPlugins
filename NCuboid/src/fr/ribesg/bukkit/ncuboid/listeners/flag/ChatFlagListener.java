package fr.ribesg.bukkit.ncuboid.listeners.flag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

public class ChatFlagListener extends AbstractListener {

    public ChatFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        final PlayerCuboid c = getPlugin().getDb().getPriorByLoc(event.getPlayer().getLocation());
        if (c.getFlag(Flag.CHAT) && !c.isAllowedPlayer(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

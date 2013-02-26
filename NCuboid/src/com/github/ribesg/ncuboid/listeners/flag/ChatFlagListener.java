package com.github.ribesg.ncuboid.listeners.flag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.Flag;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

public class ChatFlagListener extends AbstractListener {

    public ChatFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        final PlayerCuboid c = CuboidDB.getInstance().getPriorByLoc(event.getPlayer().getLocation());
        if (c.getFlag(Flag.CHAT) && !c.isAllowedPlayer(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

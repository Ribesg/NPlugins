package fr.ribesg.bukkit.ntalk;

import lombok.Getter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class NListener implements Listener {

    @Getter private final NTalk plugin;

    public NListener(final NTalk instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChatFirst(final AsyncPlayerChatEvent event) {
        if (event.getMessage().startsWith("@")) {
            event.setCancelled(true);
            // TODO Launch command /pm
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChatLast(final AsyncPlayerChatEvent event) {
        event.setFormat(plugin.getFormater().getFormat(event.getPlayer()));
        event.setMessage(event.getMessage()); // Reformat the message
    }
}

package fr.ribesg.bukkit.ntalk.listeners;

import lombok.Getter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.ribesg.bukkit.ntalk.NTalk;

public class PlayerChatListener implements Listener {

    @Getter private final NTalk plugin;

    public PlayerChatListener(final NTalk instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        event.setFormat(plugin.getFormater().getFormat(event.getPlayer()));
        event.setMessage(event.getMessage()); // Reformat the message
    }
}

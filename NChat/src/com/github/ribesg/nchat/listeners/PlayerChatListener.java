package com.github.ribesg.nchat.listeners;

import lombok.Getter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.ribesg.nchat.NChat;

public class PlayerChatListener implements Listener {

    @Getter private final NChat plugin;

    public PlayerChatListener(final NChat instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        event.setFormat(plugin.getFormater().getFormat(event.getPlayer()));
        event.setMessage(event.getMessage()); // Reformat the message
    }
}

package fr.ribesg.bukkit.nworld;

import lombok.Getter;

import org.bukkit.event.Listener;

public class NListener implements Listener {

    @Getter private final NWorld plugin;

    public NListener(final NWorld instance) {
        plugin = instance;
    }

}

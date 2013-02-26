package fr.ribesg.bukkit.ncuboid.listeners;

import lombok.Getter;

import org.bukkit.event.Listener;

import fr.ribesg.bukkit.ncuboid.NCuboid;

public abstract class AbstractListener implements Listener {

    @Getter private final NCuboid plugin;

    public AbstractListener(final NCuboid instance) {
        plugin = instance;
    }
}

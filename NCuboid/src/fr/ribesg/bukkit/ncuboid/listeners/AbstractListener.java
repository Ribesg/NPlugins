package fr.ribesg.bukkit.ncuboid.listeners;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.event.Listener;

public abstract class AbstractListener implements Listener {

    private final NCuboid plugin;

    public AbstractListener(final NCuboid instance) {
        plugin = instance;
    }

    public NCuboid getPlugin() {
        return this.plugin;
    }
}

package fr.ribesg.bukkit.ntheendagain.listener;
import fr.ribesg.bukkit.ncore.utils.Utils;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.io.IOException;

/**
 * Handles World Load and Unload events
 *
 * @author Ribesg
 */
public class WorldListener implements Listener {

    private final NTheEndAgain plugin;

    public WorldListener(final NTheEndAgain instance) {
        plugin = instance;
    }

    /**
     * Creates an EndWorldHandler if the loaded world is an End world
     *
     * @param event a World Load Event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(final WorldLoadEvent event) {
        if (event.getWorld().getEnvironment() == World.Environment.THE_END) {
            plugin.getLogger().info("Additional End world detected: handling " + event.getWorld().getName());
            final EndWorldHandler handler = new EndWorldHandler(plugin, event.getWorld());
            try {
                handler.loadConfig();
                handler.loadChunks();
                plugin.getWorldHandlers().put(Utils.toLowerCamelCase(event.getWorld().getName()), handler);
                handler.init();
            } catch (final IOException e) {
                plugin.getLogger().severe("An error occured, stacktrace follows:");
                e.printStackTrace();
                plugin.getLogger().severe("This error occured when NTheEndAgain tried to load " + e.getMessage() + ".yml");
            }
        }
    }

    /**
     * Creates an EndWorldHandler if the loaded world is an End world
     *
     * @param event a World Unload Event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onWorldUnload(final WorldUnloadEvent event) {
        if (event.getWorld().getEnvironment() == World.Environment.THE_END) {
            plugin.getLogger().info("Handling " + event.getWorld().getName() + " unload");
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(event.getWorld().getName()));
            if (handler != null) {
                handler.unload();
            }
        }
    }
}

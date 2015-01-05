/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - WorldListener.java           *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.listener.WorldListener   *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.listener;

import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;

import java.io.IOException;

import org.bukkit.World.Environment;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * Handles World Load and Unload events
 *
 * @author Ribesg
 */
public class WorldListener implements Listener {

    private final NTheEndAgain plugin;

    public WorldListener(final NTheEndAgain instance) {
        this.plugin = instance;
    }

    /**
     * Creates an EndWorldHandler if the loaded world is an End world
     *
     * @param event a World Load Event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(final WorldLoadEvent event) {
        if (event.getWorld().getEnvironment() == Environment.THE_END) {
            this.plugin.getLogger().info("Additional End world detected: handling " + event.getWorld().getName());
            final EndWorldHandler handler = new EndWorldHandler(this.plugin, event.getWorld());
            try {
                handler.loadConfig();
                handler.loadChunks();
                this.plugin.getWorldHandlers().put(handler.getCamelCaseWorldName(), handler);
                handler.init();
            } catch (final IOException | InvalidConfigurationException e) {
                this.plugin.getLogger().severe("An error occured, stacktrace follows:");
                e.printStackTrace();
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
        if (event.getWorld().getEnvironment() == Environment.THE_END) {
            this.plugin.getLogger().info("Handling " + event.getWorld().getName() + " unload");
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(event.getWorld().getName()));
            if (handler != null) {
                try {
                    handler.unload(false);
                } catch (final InvalidConfigurationException e) {
                    this.plugin.getLogger().severe("An error occured, stacktrace follows:");
                    e.printStackTrace();
                }
            }
        }
    }
}

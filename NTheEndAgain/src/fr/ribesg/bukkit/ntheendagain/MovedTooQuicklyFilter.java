package fr.ribesg.bukkit.ntheendagain;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncore.utils.Utils;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

public class MovedTooQuicklyFilter implements Filter {

    private final NTheEndAgain plugin;

    public MovedTooQuicklyFilter(final NTheEndAgain instance) {
        plugin = instance;
    }

    @Override
    public boolean isLoggable(final LogRecord record) {
        if (record.getLevel() == Level.WARNING) {
            final String message = record.getMessage();
            final String[] split = message.split(" ");
            final Player p = plugin.getServer().getPlayerExact(split[0]);
            if (p != null) {
                if ("moved".equals(split[1]) && "too".equals(split[2]) && "quickly!".equals(split[3])) {
                    final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(p.getWorld().getName()));
                    return handler == null || handler.getConfig().getFilterMovedTooQuicklySpam() == 0;
                }
            }
        }
        return true;
    }
}

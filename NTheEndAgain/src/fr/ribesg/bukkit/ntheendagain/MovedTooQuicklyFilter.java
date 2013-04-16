package fr.ribesg.bukkit.ntheendagain;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

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
            for (final String s : split) {
                System.out.println(s);
            }
            if (plugin.getServer().getPlayerExact(split[0]) != null) {
                if (split[1].equals("moved") && split[2].equals("too") && split[3].equals("quickly!")) {
                    plugin.getLogger().severe("Log cancelled");
                    return false;
                }
            }
        }
        return true;
    }
}

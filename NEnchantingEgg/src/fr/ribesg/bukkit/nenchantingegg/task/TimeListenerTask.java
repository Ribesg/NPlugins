package fr.ribesg.bukkit.nenchantingegg.task;

import fr.ribesg.bukkit.ncore.utils.Time;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * This task listen to time changes and notices Altars
 * Should be run Sync with a low delay
 */
public class TimeListenerTask extends BukkitRunnable {

    private final NEnchantingEgg    plugin;
    private final Map<String, Long> previousTimeMap;

    public TimeListenerTask(final NEnchantingEgg instance) {
        plugin = instance;
        previousTimeMap = new HashMap<String, Long>();
        for (final World w : Bukkit.getWorlds()) {
            previousTimeMap.put(w.getName(), w.getTime());
        }
    }

    @Override
    public void run() {
        for (final World w : Bukkit.getWorlds()) {
            final long actualTime = w.getTime();
            final long previousTime = previousTimeMap.get(w.getName());
            if (Time.isDayTime(previousTime) && Time.isNightTime(actualTime)) {
                plugin.getAltars().timeChange(w.getName(), Time.DAY, Time.NIGHT);
            } else if (Time.isNightTime(previousTime) && Time.isDayTime(actualTime)) {
                plugin.getAltars().timeChange(w.getName(), Time.NIGHT, Time.DAY);
            }
            previousTimeMap.put(w.getName(), actualTime);
        }
    }
}

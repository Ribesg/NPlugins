package fr.ribesg.bukkit.ncore.emergency;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import org.bukkit.event.Listener;

/** @author Ribesg */
public class EmergencyListener implements Listener {

    private NPlugin plugin;

    public EmergencyListener(NPlugin instance) {
        this.plugin = instance;
    }
}

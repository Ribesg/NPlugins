package fr.ribesg.bukkit.nenchantingegg;

import lombok.Getter;

import org.bukkit.event.Listener;

/**
 * TODO
 * 
 * @author Ribesg
 */
public class NListener implements Listener {

    @Getter private final NEnchantingEgg plugin;

    public NListener(final NEnchantingEgg instance) {
        plugin = instance;
    }

}

package fr.ribesg.bukkit.nplayer.api;

import fr.ribesg.bukkit.ncore.nodes.player.PunisherNode;
import fr.ribesg.bukkit.nplayer.NPlayer;

public class NPlayerAPI extends PunisherNode {

    private final NPlayer plugin;

    public NPlayerAPI(final NPlayer instance) {
        plugin = instance;
    }

}

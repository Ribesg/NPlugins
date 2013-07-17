package fr.ribesg.bukkit.ndodgeball.api;

import fr.ribesg.bukkit.ncore.nodes.dodgeball.DodgeBallNode;
import fr.ribesg.bukkit.ndodgeball.NDodgeBall;

public class NDodgeBallAPI extends DodgeBallNode {

    private final NDodgeBall plugin;

    public NDodgeBallAPI(final NDodgeBall instance) {
        plugin = instance;
    }
}

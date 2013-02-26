package fr.ribesg.bukkit.nchat.api;

import fr.ribesg.bukkit.nchat.NChat;
import fr.ribesg.bukkit.ncore.nodes.chat.ChatNode;

public class NChatAPI extends ChatNode {
    private final NChat plugin;

    public NChatAPI(final NChat instance) {
        plugin = instance;
    }
}

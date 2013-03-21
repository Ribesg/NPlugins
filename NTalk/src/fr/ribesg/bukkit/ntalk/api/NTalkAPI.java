package fr.ribesg.bukkit.ntalk.api;

import fr.ribesg.bukkit.ncore.nodes.chat.TalkNode;
import fr.ribesg.bukkit.ntalk.NTalk;

public class NTalkAPI extends TalkNode {
    private final NTalk plugin;

    public NTalkAPI(final NTalk instance) {
        plugin = instance;
    }
}

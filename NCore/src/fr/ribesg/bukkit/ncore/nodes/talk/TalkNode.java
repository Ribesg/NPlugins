package fr.ribesg.bukkit.ncore.nodes.talk;

import fr.ribesg.bukkit.ncore.nodes.NPlugin;

/**
 * Represents the NTalk plugin
 * 
 * @author Ribesg
 */
public abstract class TalkNode extends NPlugin {

    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#linkCore()
     */
    @Override
    protected void linkCore() {
        getCore().setTalkNode(this);
    }
}

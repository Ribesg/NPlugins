package fr.ribesg.bukkit.ncore.node.talk;

import fr.ribesg.bukkit.ncore.node.NPlugin;

/**
 * Represents the NTalk plugin
 *
 * @author Ribesg
 */
public abstract class TalkNode extends NPlugin {

    /** @see fr.ribesg.bukkit.ncore.node.NPlugin#linkCore() */
    @Override
    protected void linkCore() {
        getCore().setTalkNode(this);
    }
}

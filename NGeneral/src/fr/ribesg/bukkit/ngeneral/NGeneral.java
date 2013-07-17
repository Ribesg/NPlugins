package fr.ribesg.bukkit.ngeneral;

import fr.ribesg.bukkit.ncore.nodes.general.GeneralNode;

public class NGeneral extends GeneralNode {

    @Override
    protected String getMinCoreVersion() {
        return "0.2.1";
    }

    @Override
    protected boolean onNodeEnable() {
        return false;  // TODO Implement method
    }

    @Override
    protected void onNodeDisable() {
        // TODO Implement method
    }

    @Override
    protected void handleOtherNodes() {
        // NOP
    }

    @Override
    protected void linkCore() {
        getCore().setGeneralNode(this);
    }
}

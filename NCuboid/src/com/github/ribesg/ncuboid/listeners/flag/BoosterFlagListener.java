package com.github.ribesg.ncuboid.listeners.flag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.Flag;
import com.github.ribesg.ncuboid.beans.FlagAtt;
import com.github.ribesg.ncuboid.events.extensions.ExtendedPlayerMoveEvent;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

public class BoosterFlagListener extends AbstractListener {

    public BoosterFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMoveBlock(final ExtendedPlayerMoveEvent ext) {
        final PlayerMoveEvent event = (PlayerMoveEvent) ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            if (ext.getToCuboid() != null && ext.getToCuboid().getFlag(Flag.BOOSTER)) {
                event.getPlayer().setVelocity(ext.getToCuboid().getVectFlagAtt(FlagAtt.BOOSTER_VECTOR));
            }
        }
    }
}

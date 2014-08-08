/***************************************************************************
 * Project file:    NPlugins - NCuboid - MessageListener.java              *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.attribute.MessageListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.attribute;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncore.util.ColorUtil;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class MessageListener extends AbstractListener {

    public MessageListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
        final PlayerGridMoveEvent event = (PlayerGridMoveEvent)ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            final Player player = event.getPlayer();
            if (ext.getFromRegion() != null && !ext.getToRegions().contains(ext.getFromRegion())) {
                final String farewellMessage = ext.getFromRegion().getStringAttribute(Attribute.FAREWELL_MESSAGE);
                if (farewellMessage != null) {
                    player.sendMessage(ColorUtil.colorize(farewellMessage));
                }
            }
            if (ext.getToRegion() != null && !ext.getFromRegions().contains(ext.getToRegion())) {
                final String welcomeMessage = ext.getToRegion().getStringAttribute(Attribute.WELCOME_MESSAGE);
                if (welcomeMessage != null) {
                    player.sendMessage(ColorUtil.colorize(welcomeMessage));
                }
            }
        }
    }
}

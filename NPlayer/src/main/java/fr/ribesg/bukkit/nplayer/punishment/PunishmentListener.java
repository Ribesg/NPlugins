/***************************************************************************
 * Project file:    NPlugins - NPlayer - PunishmentListener.java           *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.PunishmentListener *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.TimeUtil;
import fr.ribesg.bukkit.nplayer.NPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

/**
 * @author Ribesg
 */
public class PunishmentListener implements Listener {

	private final NPlayer      plugin;
	private final PunishmentDb punishmentDb;

	public PunishmentListener(final NPlayer instance) {
		this.plugin = instance;
		this.punishmentDb = plugin.getPunishmentDb();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			final UUID id = event.getPlayer().getUniqueId();
			final String playerIp = event.getAddress().getHostAddress();
			if (punishmentDb.isIdBanned(id)) {
				final Punishment ban = punishmentDb.get(id.toString(), PunishmentType.BAN);
				final String playerBannedMessage;
				if (ban.isPermanent()) {
					playerBannedMessage = plugin.getMessages().get(MessageId.player_deniedPermBanned, ban.getReason())[0];
				} else {
					playerBannedMessage = plugin.getMessages().get(MessageId.player_deniedTempBanned, ban.getReason(), TimeUtil.toString((ban.getEndDate() - System.currentTimeMillis()) / 1000))[0];
				}
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED, playerBannedMessage);
			} else if (punishmentDb.isIpBanned(playerIp)) {
				final Punishment ipBan = punishmentDb.get(playerIp, PunishmentType.IPBAN);
				final String ipBannedMessage;
				if (ipBan.isPermanent()) {
					ipBannedMessage = plugin.getMessages().get(MessageId.player_deniedPermIpBanned, ipBan.getReason())[0];
				} else {
					ipBannedMessage = plugin.getMessages().get(MessageId.player_deniedTempIpBanned, ipBan.getReason(), TimeUtil.toString((ipBan.getEndDate() - System.currentTimeMillis()) / 1000))[0];
				}
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ipBannedMessage);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		plugin.entering(getClass(), "onPlayerChat");

		final UUID id = event.getPlayer().getUniqueId();
		final Punishment mute;
		synchronized (this.punishmentDb) {
			mute = punishmentDb.get(id.toString(), PunishmentType.MUTE);
		}
		if (mute != null) {
			if (mute.isPermanent()) {
				plugin.debug("Player is muted permanently");
				plugin.sendMessage(event.getPlayer(), MessageId.player_deniedPermMuted, mute.getReason());
			} else {
				plugin.debug("Player is muted temporarily");
				plugin.sendMessage(event.getPlayer(), MessageId.player_deniedTempMuted, mute.getReason(), TimeUtil.toString((mute.getEndDate() - System.currentTimeMillis()) / 1000));
			}
			event.setCancelled(true);
		}

		plugin.exiting(getClass(), "onPlayerChat");
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerKick(final PlayerKickEvent event) {
		plugin.entering(getClass(), "onPlayerKick");

		String msg = punishmentDb.getLeaveMessages().remove(event.getPlayer().getUniqueId());
		if (msg != null) {
			if (plugin.isDebugEnabled()) {
				plugin.debug("Message not null: '" + msg + "'");
			}
		} else {
			msg = plugin.getMessages().get(MessageId.player_standardKickMessage, event.getPlayer().getName())[0];
			if (plugin.isDebugEnabled()) {
				plugin.debug("Message is null, setting default: '" + msg + "'");
			}
		}
		event.setLeaveMessage(msg);

		plugin.exiting(getClass(), "onPlayerKick");
	}
}

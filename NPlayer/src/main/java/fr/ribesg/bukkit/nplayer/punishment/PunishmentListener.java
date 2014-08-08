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

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

/**
 * @author Ribesg
 */
public class PunishmentListener implements Listener {

	private final NPlayer      plugin;
	private final PunishmentDb punishmentDb;

	public PunishmentListener(final NPlayer instance) {
		this.plugin = instance;
		this.punishmentDb = this.plugin.getPunishmentDb();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		if (event.getResult() == Result.ALLOWED) {
			final UUID id = event.getPlayer().getUniqueId();
			final String playerIp = event.getAddress().getHostAddress();
			if (this.punishmentDb.isIdBanned(id)) {
				final Punishment ban = this.punishmentDb.get(id.toString(), PunishmentType.BAN);
				final String playerBannedMessage;
				if (ban.isPermanent()) {
					playerBannedMessage = this.plugin.getMessages().get(MessageId.player_deniedPermBanned, ban.getReason())[0];
				} else {
					playerBannedMessage = this.plugin.getMessages().get(MessageId.player_deniedTempBanned, ban.getReason(), TimeUtil.toString((ban.getEndDate() - System.currentTimeMillis()) / 1000))[0];
				}
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED, playerBannedMessage);
			} else if (this.punishmentDb.isIpBanned(playerIp)) {
				final Punishment ipBan = this.punishmentDb.get(playerIp, PunishmentType.IPBAN);
				final String ipBannedMessage;
				if (ipBan.isPermanent()) {
					ipBannedMessage = this.plugin.getMessages().get(MessageId.player_deniedPermIpBanned, ipBan.getReason())[0];
				} else {
					ipBannedMessage = this.plugin.getMessages().get(MessageId.player_deniedTempIpBanned, ipBan.getReason(), TimeUtil.toString((ipBan.getEndDate() - System.currentTimeMillis()) / 1000))[0];
				}
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ipBannedMessage);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		this.plugin.entering(this.getClass(), "onPlayerChat");

		final UUID id = event.getPlayer().getUniqueId();
		final Punishment mute;
		synchronized (this.punishmentDb) {
			mute = this.punishmentDb.get(id.toString(), PunishmentType.MUTE);
		}
		if (mute != null) {
			if (mute.isPermanent()) {
				this.plugin.debug("Player is muted permanently");
				this.plugin.sendMessage(event.getPlayer(), MessageId.player_deniedPermMuted, mute.getReason());
			} else {
				this.plugin.debug("Player is muted temporarily");
				this.plugin.sendMessage(event.getPlayer(), MessageId.player_deniedTempMuted, mute.getReason(), TimeUtil.toString((mute.getEndDate() - System.currentTimeMillis()) / 1000));
			}
			event.setCancelled(true);
		}

		this.plugin.exiting(this.getClass(), "onPlayerChat");
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerKick(final PlayerKickEvent event) {
		this.plugin.entering(this.getClass(), "onPlayerKick");

		String msg = this.punishmentDb.getLeaveMessages().remove(event.getPlayer().getUniqueId());
		if (msg != null) {
			if (this.plugin.isDebugEnabled()) {
				this.plugin.debug("Message not null: '" + msg + '\'');
			}
		} else {
			msg = this.plugin.getMessages().get(MessageId.player_standardKickMessage, event.getPlayer().getName())[0];
			if (this.plugin.isDebugEnabled()) {
				this.plugin.debug("Message is null, setting default: '" + msg + '\'');
			}
		}
		event.setLeaveMessage(msg);

		this.plugin.exiting(this.getClass(), "onPlayerKick");
	}
}

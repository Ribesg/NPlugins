package fr.ribesg.bukkit.nplayer.punishment;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.TimeUtils;
import fr.ribesg.bukkit.nplayer.NPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/** @author Ribesg */
public class PunishmentListener implements Listener {

	private final NPlayer      plugin;
	private final PunishmentDb punishmentDb;

	public PunishmentListener(NPlayer instance) {
		this.plugin = instance;
		this.punishmentDb = plugin.getPunishmentDb();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		final String playerName = event.getPlayer().getName();
		final String playerIp = event.getAddress().getHostAddress();
		if (punishmentDb.isNickBanned(playerName)) {
			final Punishment ban = punishmentDb.get(playerName, PunishmentType.BAN);
			String playerBannedMessage;
			if (ban.isPermanent()) {
				playerBannedMessage = plugin.getMessages().get(MessageId.player_deniedPermBanned, ban.getReason())[0];
			} else {
				playerBannedMessage = plugin.getMessages()
				                            .get(MessageId.player_deniedTempBanned,
				                                 ban.getReason(),
				                                 TimeUtils.toString((ban.getEndDate() - System.currentTimeMillis()) / 1000))[0];
			}
			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, playerBannedMessage);
		} else if (punishmentDb.isIpBanned(playerIp)) {
			final Punishment ipBan = punishmentDb.get(playerName, PunishmentType.IPBAN);
			String ipBannedMessage;
			if (ipBan.isPermanent()) {
				ipBannedMessage = plugin.getMessages().get(MessageId.player_deniedPermIpBanned, ipBan.getReason())[0];
			} else {
				ipBannedMessage = plugin.getMessages()
				                        .get(MessageId.player_deniedTempIpBanned,
				                             ipBan.getReason(),
				                             TimeUtils.toString((ipBan.getEndDate() - System.currentTimeMillis()) / 1000))[0];
			}
			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ipBannedMessage);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		final String playerName = event.getPlayer().getName();
		Punishment mute;
		synchronized (this.punishmentDb) {
			mute = punishmentDb.get(playerName, PunishmentType.MUTE);
		}
		if (mute != null) {
			if (mute.isPermanent()) {
				plugin.sendMessage(event.getPlayer(), MessageId.player_deniedPermMuted, mute.getReason());
			} else {
				plugin.sendMessage(event.getPlayer(),
				                   MessageId.player_deniedTempMuted,
				                   mute.getReason(),
				                   TimeUtils.toString((mute.getEndDate() - System.currentTimeMillis()) / 1000));
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerKick(final PlayerKickEvent event) {
		final String msg = punishmentDb.getLeaveMessages().get(event.getPlayer().getName());
		if (msg != null) {
			event.setLeaveMessage(msg);
		} else {
			event.setLeaveMessage(plugin.getMessages().get(MessageId.player_standardKickMessage, event.getPlayer().getName())[0]);
		}
	}
}

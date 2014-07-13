/***************************************************************************
 * Project file:    NPlugins - NPlayer - TemporaryPunishmentCleanerTask.java
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.TemporaryPunishmentCleanerTask
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.IPValidator;
import fr.ribesg.bukkit.nplayer.NPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TemporaryPunishmentCleanerTask extends BukkitRunnable {

	private final NPlayer plugin;

	public TemporaryPunishmentCleanerTask(final NPlayer instance) {
		this.plugin = instance;
	}

	@Override
	public void run() {
		final Set<Punishment> toBeRemoved = new HashSet<>();
		for (final Punishment p : plugin.getPunishmentDb().getTempPunishmentEndDateMap().headMap(System.currentTimeMillis()).values()) {
			toBeRemoved.add(p);
		}
		for (final Punishment p : toBeRemoved) {
			plugin.getPunishmentDb().remove(p);
			if (p.getType() == PunishmentType.JAIL) {
				plugin.getCuboidNode().unJail(UUID.fromString(p.getPunished()));
			}
			String punished = p.getPunished();
			if (!IPValidator.isValidIp(punished)) {
				punished = UuidDb.getName(UUID.fromString(p.getPunished()));
			}
			switch (p.getType()) {
				case BAN:
					plugin.broadcastMessage(MessageId.player_unBannedBroadcast, punished);
					break;
				case IPBAN:
					plugin.broadcastMessage(MessageId.player_unBannedIpBroadcast, punished);
					break;
				case JAIL:
					plugin.broadcastMessage(MessageId.player_unJailedBroadcast, punished);
					break;
				case MUTE:
					plugin.broadcastMessage(MessageId.player_unMutedBroadcast, punished);
					break;
				default:
					break;
			}
		}
	}
}

/***************************************************************************
 * Project file:    NPlugins - NGeneral - AutoAfkFeature.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.autoafk.AutoAfkFeature
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.autoafk;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.Feature;
import fr.ribesg.bukkit.ngeneral.feature.FeatureType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoAfkFeature extends Feature {

	/* package*/ static final String AFK_PREFIX = "" + ChatColor.GRAY + ChatColor.ITALIC;

	private Map<String, Long> lastUpdateMap;
	private BukkitTask        task;

	/** Must not interact with Bukkit in any way */
	public AutoAfkFeature(final NGeneral instance) {
		super(instance, FeatureType.AUTO_AFK, instance.getPluginConfig().hasAutoAfkFeature());
		this.lastUpdateMap = new ConcurrentHashMap<>();
	}

	@Override
	public void initialize() {
		final AutoAfkListener listener = new AutoAfkListener(this);
		final AutoAfkCommandExecutor executor = new AutoAfkCommandExecutor(this);

		Bukkit.getPluginManager().registerEvents(listener, plugin);
		plugin.setCommandExecutor("afk", executor);

		// The lack of *20 in the period is not a bug
		// Example: for a daly of 120s, it's nice to check every 120 ticks (~6s)
		final long taskPeriod = plugin.getPluginConfig().getAutoAfkDelay();
		task = new AutoAfkTask(this).runTaskTimerAsynchronously(plugin, taskPeriod, taskPeriod);
	}

	@Override
	public void terminate() {
		task.cancel();
	}

	public void update(final String playerName) {
		this.lastUpdateMap.put(playerName, System.currentTimeMillis());
	}

	public Map<String, Long> getLastUpdateMap() {
		return lastUpdateMap;
	}

	public boolean isAfk(final Player player) {
		return player.getPlayerListName().startsWith(AFK_PREFIX);
	}

	public void setAfk(final String playerName, final boolean value, @Nullable final String reason) {
		final Player player = Bukkit.getPlayerExact(playerName);
		if (player != null) {
			if (!value) {
				player.setPlayerListName(player.getName());
				if (plugin.getPluginConfig().hasBroadCastOnAfk()) {
					if (reason != null && reason.length() > 0) {
						plugin.broadcastMessage(MessageId.general_afk_noLongerAfkBroadcastReason, player.getName(), reason);
					} else {
						plugin.broadcastMessage(MessageId.general_afk_noLongerAfkBroadcast, player.getName());
					}
				}
			} else {
				String newPlayerListName = AFK_PREFIX + player.getName();
				if (newPlayerListName.length() > 16) {
					newPlayerListName = newPlayerListName.substring(0, 16);
				}
				player.setPlayerListName(newPlayerListName);
				if (plugin.getPluginConfig().hasBroadCastOnAfk()) {
					if (reason != null && reason.length() > 0) {
						plugin.broadcastMessage(MessageId.general_afk_nowAfkBroadcastReason, player.getName(), reason);
					} else {
						plugin.broadcastMessage(MessageId.general_afk_nowAfkBroadcast, player.getName());
					}
				}
			}
		}
	}
}

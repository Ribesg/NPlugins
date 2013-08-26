package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfkCommand implements CommandExecutor {

	private static final String AFK_PREFIX = "" + ChatColor.GRAY + ChatColor.ITALIC;

	private final NGeneral plugin;

	public AfkCommand(NGeneral instance) {
		this.plugin = instance;
		plugin.getCommand("afk").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equals("afk")) {
			if (!Perms.hasAfk(sender)) {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			} else if (!(sender instanceof Player)) {
				plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			} else {
				final Player player = (Player) sender;
				String reason = "";
				for (String arg : args) {
					reason += arg + ' ';
				}
				if (reason.length() > 0) {
					reason = reason.substring(0, reason.length() - 1);
				}
				if (player.getPlayerListName().startsWith(AFK_PREFIX)) {
					player.setPlayerListName(player.getName());
					plugin.sendMessage(player, MessageId.general_afk_noLongerAfk);
					if (plugin.getPluginConfig().hasBroadCastOnAfk()) {
						if (reason.length() > 0) {
							sendMessageToAllBut(player, MessageId.general_afk_noLongerAfkBroadcastReason, player.getName(), reason);
						} else {
							sendMessageToAllBut(player, MessageId.general_afk_noLongerAfkBroadcast, player.getName());
						}
					}
				} else {
					String newPlayerListName = AFK_PREFIX + player.getName();
					if (newPlayerListName.length() > 16) {
						newPlayerListName = newPlayerListName.substring(0, 16);
					}
					player.setPlayerListName(newPlayerListName);
					plugin.sendMessage(player, MessageId.general_afk_nowAfk);
					if (plugin.getPluginConfig().hasBroadCastOnAfk()) {
						if (reason.length() > 0) {
							sendMessageToAllBut(player, MessageId.general_afk_nowAfkBroadcastReason, player.getName(), reason);
						} else {
							sendMessageToAllBut(player, MessageId.general_afk_nowAfkBroadcast, player.getName());
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Broadcasts a message to all players but the one passed as argument
	 *
	 * @param player The player that should not receive the message
	 * @param id     The message ID
	 * @param args   The arguments to the message
	 */
	private void sendMessageToAllBut(Player player, MessageId id, String... args) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.equals(player)) {
				plugin.sendMessage(p, id, args);
			}
		}
	}

}

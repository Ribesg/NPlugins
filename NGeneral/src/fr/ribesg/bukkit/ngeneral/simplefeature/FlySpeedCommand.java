package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class FlySpeedCommand implements CommandExecutor, Listener {

	private static final float DEFAULT_BUKKIT_FLYSPEED = 0.1f;

	private final NGeneral plugin;

	public FlySpeedCommand(NGeneral instance) {
		this.plugin = instance;
		plugin.getCommand("flyspeed").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.getPlayer().setFlySpeed(DEFAULT_BUKKIT_FLYSPEED);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equals("flyspeed")) {
			if (!Perms.hasFlySpeed(sender)) {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			} else if (args.length == 1) {
				if (!(sender instanceof Player)) {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
				} else {
					Player player = (Player) sender;
					try {
						float value = Float.parseFloat(args[0]);
						if (value < -1 || value > 1) {
							return false;
						}
						player.setFlySpeed(value);
						plugin.sendMessage(player, MessageId.general_flyspeed_set, Float.toString(value));
					} catch (NumberFormatException e) {
						player.setFlySpeed(DEFAULT_BUKKIT_FLYSPEED);
						plugin.sendMessage(player, MessageId.general_flyspeed_reset);
					}
				}
			} else if (args.length == 2) {
				if (!Perms.hasFlySpeedOthers(sender)) {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				} else {
					String arg0 = args[0].toLowerCase();
					float value = DEFAULT_BUKKIT_FLYSPEED;
					try {
						value = Float.parseFloat(arg0);
					} catch (NumberFormatException ignored) {
					}
					String arg1 = args[1].toLowerCase();
					String[] names = arg1.split(",");
					setAll(sender, names, value);
				}
				return true;
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Change FlyMode state of all players provided.
	 *
	 * @param playerNames Names of players
	 * @param value       Null for toggle, actual value for a set
	 */
	private void setAll(CommandSender sender, String[] playerNames, float value) {
		for (String name : playerNames) {
			Player p = Bukkit.getPlayer(name);
			if (p == null) {
				plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, name);
			} else {
				p.setFlySpeed(value);
				plugin.sendMessage(sender, MessageId.general_flyspeed_setFor, p.getName(), Float.toString(value));
				plugin.sendMessage(p, MessageId.general_flyspeed_setBy, sender.getName(), Float.toString(value));
			}
		}
	}
}

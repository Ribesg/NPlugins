package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class WeatherCommand implements CommandExecutor {

	private static final String COMMAND = "weather";

	private final NGeneral plugin;

	public WeatherCommand(NGeneral instance) {
		this.plugin = instance;
		plugin.getCommand(COMMAND).setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equals(COMMAND)) {
			if (!Perms.hasWeather(sender)) {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			} else {
				World world = null;
				String type;
				int duration = new Random().nextInt(1000) + 500;
				if (args.length == 1) {
					type = args[0];
					if (!(sender instanceof Player)) {
						plugin.sendMessage(sender, MessageId.missingWorldArg);
						return true;
					}
					world = ((Player) sender).getWorld();
				} else if (args.length == 2) {
					type = args[0];
					try {
						duration = Integer.parseInt(args[1]);
						if (!(sender instanceof Player)) {
							plugin.sendMessage(sender, MessageId.missingWorldArg);
							return true;
						}
						world = ((Player) sender).getWorld();
					} catch (NumberFormatException e) {
						world = Bukkit.getWorld(args[1]);
						if (world == null) {
							plugin.sendMessage(sender, MessageId.unknownWorld, args[1]);
							return true;
						}
					}
				} else if (args.length == 3) {
					type = args[0];
					try {
						duration = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						return false;
					}
					world = Bukkit.getWorld(args[2]);
					if (world == null) {
						plugin.sendMessage(sender, MessageId.unknownWorld, args[2]);
						return true;
					}
				} else {
					return false;
				}
				switch (type) {
					case "clear":
						world.setStorm(false);
						world.setThundering(false);
						world.setWeatherDuration(duration * 20);
						world.setThunderDuration(0);
						break;
					case "rain":
						world.setStorm(true);
						world.setThundering(false);
						world.setWeatherDuration(duration * 20);
						world.setThunderDuration(0);
						break;
					case "thunder":
						world.setStorm(true);
						world.setThundering(true);
						world.setWeatherDuration(duration * 20);
						world.setThunderDuration(duration * 20);
						break;
					default:
						return false;
				}
				plugin.broadcastMessage(MessageId.general_weatherSet, type, world.getName(), sender.getName(), Integer.toString(duration));
				return true;
			}
		} else {
			return false;
		}
	}

}

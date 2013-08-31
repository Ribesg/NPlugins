package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.ArgumentParser;
import fr.ribesg.bukkit.ncore.utils.WorldUtils;
import fr.ribesg.bukkit.nworld.warp.Warp;
import fr.ribesg.bukkit.nworld.world.AdditionalSubWorld;
import fr.ribesg.bukkit.nworld.world.AdditionalWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.ChatPaginator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** @author Ribesg */
public class WorldCommandExecutor implements CommandExecutor {

	private final NWorld plugin;

	public WorldCommandExecutor(final NWorld instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equals("nworld")) {
			if (Perms.hasWorld(sender)) {
				return cmdWorld(sender, args);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else if (command.getName().equals("spawn")) {
			if (Perms.hasSpawn(sender)) {
				return cmdSpawn(sender);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else if (command.getName().equals("setspawn")) {
			if (Perms.hasSetSpawn(sender)) {
				return cmdSetSpawn(sender);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else if (command.getName().equals("warp")) {
			if (Perms.hasWarp(sender)) {
				return cmdWarp(sender, args);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else if (command.getName().equals("setwarp")) {
			if (Perms.hasSetWarp(sender)) {
				return cmdSetWarp(sender, args);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cmdWorld(final CommandSender sender, String[] args) {
		String[] parsedArgs = ArgumentParser.joinArgsWithQuotes(args);
		if (parsedArgs.length == 0) {
			// Lists available worlds
			plugin.sendMessage(sender, MessageId.world_availableWorlds);
			for (final GeneralWorld world : plugin.getWorlds()) {
				boolean hasPermission = Perms.hasRequiredPermission(sender, world.getRequiredPermission());
				if (world.isEnabled()) {
					if (hasPermission) {
						sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + world.getWorldName());
					} else if (!world.isHidden()) {
						sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + world.getWorldName());
					}
				} else if (Perms.hasAdmin(sender)) {
					sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.BLACK + world.getWorldName());
				}
			}
			return true;
		} else if (parsedArgs.length == 1) {
			// Warp to world
			return subCmdWorldWarp(sender, parsedArgs[0]);
		} else { // Handle subcommands
			final String subCmd = parsedArgs[0].toLowerCase();
			parsedArgs = Arrays.copyOfRange(parsedArgs, 1, parsedArgs.length);
			switch (subCmd) {
				case "create":
					return subCmdWorldCreate(sender, parsedArgs);
				case "load":
					return subCmdWorldLoad(sender, parsedArgs);
				case "unload":
					return subCmdWorldUnload(sender, parsedArgs);
				case "hidden":
				case "sethidden":
					return subCmdWorldSetHidden(sender, parsedArgs);
				case "perm":
				case "setperm":
					return subCmdWorldSetPerm(sender, parsedArgs);
				case "nether":
				case "setnether":
					return subCmdWorldSetNether(sender, parsedArgs);
				case "end":
				case "setend":
					return subCmdWorldSetEnd(sender, parsedArgs);
				default:
					return false;
			}
		}
	}

	private boolean subCmdWorldWarp(final CommandSender sender, final String givenWorldName) {
		if (!(sender instanceof Player)) {
			plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		}
		final Player player = (Player) sender;
		final String worldName = givenWorldName.toLowerCase();
		final GeneralWorld world = plugin.getWorlds().get(worldName);
		if (world != null && world.isEnabled()) {
			if (Perms.hasRequiredPermission(player, world.getRequiredPermission()) || Perms.hasWorldWarpAll(player)) {
				final Location loc = world.getSpawnLocation().toBukkitLocation();
				loc.getChunk().load();
				Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

					@Override
					public void run() {
						player.teleport(loc);
					}
				}, 1L);
				plugin.sendMessage(sender, MessageId.world_teleportedToWorld, world.getWorldName());
				return true;
			} else if (!world.isHidden()) {
				plugin.sendMessage(player, MessageId.world_warpToThisWorldDisallowed, plugin.getServer().getWorld(worldName).getName());
				return true;
			} else {
				plugin.sendMessage(player, MessageId.world_unknownWorld, givenWorldName);
				return true;
			}
		} else {
			plugin.sendMessage(player, MessageId.world_unknownWorld, givenWorldName);
			return true;
		}
	}

	private boolean subCmdWorldCreate(final CommandSender sender, final String[] args) {
		if (!Perms.hasWorldCreate(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		final String realWorldName = WorldUtils.getRealWorldName(args[0]);
		if (realWorldName != null) {
			plugin.sendMessage(sender, MessageId.world_alreadyExists, realWorldName);
			return true;
		} else {
			final String worldName = args[0];
			long seed = new Random().nextLong();
			if (args.length > 1) {
				try {
					seed = Long.parseLong(args[1]);
				} catch (final NumberFormatException e) {
					seed = args[1].hashCode();
				}
			}

			String requiredPermission = plugin.getPluginConfig().getDefaultRequiredPermission();
			if (args.length > 2) {
				requiredPermission = args[2];
			}

			boolean hidden = plugin.getPluginConfig().isDefaultHidden();
			WorldType type = null;

			if (args.length > 3) {
				final Set<String> additionalArguments = new HashSet<>();
				for (int i = 3; i < args.length; i++) {
					additionalArguments.add(args[i].toLowerCase());
				}

				if (additionalArguments.contains("public")) {
					hidden = true;
				} else if (additionalArguments.contains("private")) {
					hidden = false;
				}

				if (additionalArguments.contains("flat")) {
					type = WorldType.FLAT;
				} else if (additionalArguments.contains("large") || additionalArguments.contains("large_biome")) {
					type = WorldType.LARGE_BIOMES;
				}
			}

			AdditionalWorld nWorld = new AdditionalWorld(plugin, worldName, seed, null, requiredPermission, true, hidden, false, false);

			if (plugin.getPluginConfig().getBroadcastOnWorldCreate() == 1) {
				plugin.broadcastMessage(MessageId.world_creatingWorldMayBeLaggy);
			} else {
				plugin.sendMessage(sender, MessageId.world_creatingWorldMayBeLaggy);
			}

			nWorld.create(type);

			if (plugin.getPluginConfig().getBroadcastOnWorldCreate() == 1) {
				plugin.broadcastMessage(MessageId.world_created);
			} else {
				plugin.sendMessage(sender, MessageId.world_created);
			}

			plugin.getWorlds().put(nWorld.getWorldName(), nWorld);

			return true;
		}
	}

	private boolean subCmdWorldLoad(final CommandSender sender, final String[] args) {
		if (!Perms.hasWorldLoad(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		final String realWorldName = WorldUtils.getRealWorldName(args[0]);
		if (realWorldName == null) {
			plugin.sendMessage(sender, MessageId.world_unknownWorld, args[0]);
			return true;
		} else if (plugin.getServer().getWorld(realWorldName) != null) {
			plugin.sendMessage(sender, MessageId.world_alreadyLoaded, realWorldName);
			return true;
		} else {
			boolean wasKnown = true;
			AdditionalWorld world = plugin.getWorlds().getAdditional().get(realWorldName);

			// Main world & messages
			if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
				plugin.broadcastMessage(MessageId.world_loadingWorldMayBeLaggy);
			} else {
				plugin.sendMessage(sender, MessageId.world_loadingWorldMayBeLaggy);
			}

			if (world == null) { // Load a never-loaded world
				wasKnown = false;
				final World newLoaded = new WorldCreator(realWorldName).createWorld();
				long seed = newLoaded.getSeed();
				final NLocation spawn = new NLocation(newLoaded.getSpawnLocation());
				final String requiredPermission = plugin.getPluginConfig().getDefaultRequiredPermission();
				final boolean hidden = plugin.getPluginConfig().isDefaultHidden();
				world = new AdditionalWorld(plugin, realWorldName, seed, spawn, requiredPermission, true, hidden, false, false);
				plugin.getWorlds().put(realWorldName, world);
			} else {
				world.load();
			}
			plugin.getWarps().worldEnabled(world.getWorldName());
			if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
				plugin.broadcastMessage(MessageId.world_loaded);
			} else {
				plugin.sendMessage(sender, MessageId.world_loaded);
			}
			if (!wasKnown) {
				// TODO What if it's a single player adventure world with Nether etc
				return true;
			}

			// Nether world & messages
			if (world.hasNether()) {
				if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
					plugin.broadcastMessage(MessageId.world_loadingWorldMayBeLaggy);
				} else {
					plugin.sendMessage(sender, MessageId.world_loadingWorldMayBeLaggy);
				}

				world.getNetherWorld().load();
				plugin.getWarps().worldEnabled(world.getNetherWorld().getWorldName());

				if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
					plugin.broadcastMessage(MessageId.world_loaded);
				} else {
					plugin.sendMessage(sender, MessageId.world_loaded);
				}
			}

			// End world & messages
			if (world.hasEnd()) {
				if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
					plugin.broadcastMessage(MessageId.world_loadingWorldMayBeLaggy);
				} else {
					plugin.sendMessage(sender, MessageId.world_loadingWorldMayBeLaggy);
				}

				world.getEndWorld().load();
				plugin.getWarps().worldEnabled(world.getEndWorld().getWorldName());

				if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
					plugin.broadcastMessage(MessageId.world_loaded);
				} else {
					plugin.sendMessage(sender, MessageId.world_loaded);
				}
			}

			world.setEnabled(true);
			return true;
		}
	}

	private boolean subCmdWorldUnload(final CommandSender sender, final String[] args) {
		if (!Perms.hasWorldUnload(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		final String realWorldName = WorldUtils.getRealWorldName(args[0]);
		if (realWorldName == null) {
			plugin.sendMessage(sender, MessageId.world_unknownWorld, args[0]);
			return true;
		} else if (plugin.getServer().getWorld(realWorldName) == null) {
			plugin.sendMessage(sender, MessageId.world_notLoaded, realWorldName);
			return true;
		} else {
			final AdditionalWorld world = plugin.getWorlds().getAdditional().get(realWorldName);
			if (world == null) {
				plugin.sendMessage(sender, MessageId.world_unknownWorld, args[0]);
				return true;
			}

			if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
				plugin.broadcastMessage(MessageId.world_unloadingWorldMayBeLaggy);
			} else {
				plugin.sendMessage(sender, MessageId.world_unloadingWorldMayBeLaggy);
			}

			world.unload();
			plugin.getWarps().worldDisabled(world.getWorldName());

			if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
				plugin.broadcastMessage(MessageId.world_unloaded);
			} else {
				plugin.sendMessage(sender, MessageId.world_unloaded);
			}

			if (world.hasNether()) {
				if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
					plugin.broadcastMessage(MessageId.world_unloadingWorldMayBeLaggy);
				} else {
					plugin.sendMessage(sender, MessageId.world_unloadingWorldMayBeLaggy);
				}

				world.getNetherWorld().unload();
				plugin.getWarps().worldDisabled(world.getNetherWorld().getWorldName());

				if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
					plugin.broadcastMessage(MessageId.world_unloaded);
				} else {
					plugin.sendMessage(sender, MessageId.world_unloaded);
				}
			}

			if (world.hasEnd()) {
				if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
					plugin.broadcastMessage(MessageId.world_unloadingWorldMayBeLaggy);
				} else {
					plugin.sendMessage(sender, MessageId.world_unloadingWorldMayBeLaggy);
				}

				world.getEndWorld().unload();
				plugin.getWarps().worldDisabled(world.getEndWorld().getWorldName());

				if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
					plugin.broadcastMessage(MessageId.world_unloaded);
				} else {
					plugin.sendMessage(sender, MessageId.world_unloaded);
				}
			}

			world.setEnabled(false);
			return true;
		}
	}

	private boolean subCmdWorldSetHidden(final CommandSender sender, final String[] args) {
		if (!Perms.hasWorldSetHidden(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		if (args.length != 2) {
			return false;
		}
		String worldName = args[0];
		boolean hidden = Boolean.parseBoolean(args[1]);
		final Player player = (Player) sender;
		GeneralWorld world = plugin.getWorlds().get(worldName);
		if (world != null) {
			world.setHidden(hidden);
			if (hidden) {
				plugin.sendMessage(sender, MessageId.world_worldHiddenTrue, worldName);
			} else {
				plugin.sendMessage(sender, MessageId.world_worldHiddenFalse, worldName);
			}
			return true;
		} else {
			plugin.sendMessage(player, MessageId.world_unknownWorld, worldName);
			return true;
		}
	}

	private boolean subCmdWorldSetPerm(final CommandSender sender, final String[] args) {
		if (!Perms.hasWorldSetPerm(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		if (args.length != 2) {
			return false;
		}
		String worldName = args[0];
		String permission = args[1];
		if (plugin.getPluginConfig().getPermissionShortcuts().containsKey(permission.toLowerCase())) {
			permission = plugin.getPluginConfig().getPermissionShortcuts().get(permission.toLowerCase());
		}
		final Player player = (Player) sender;
		GeneralWorld world = plugin.getWorlds().get(worldName);
		if (world != null) {
			world.setRequiredPermission(permission);
			plugin.sendMessage(sender, MessageId.world_changedWorldRequiredPermission, worldName, permission);
			return true;
		} else {
			plugin.sendMessage(player, MessageId.world_unknownWorld, worldName);
			return true;
		}
	}

	private boolean subCmdWorldSetNether(final CommandSender sender, final String[] args) {
		if (!Perms.hasWorldSetNether(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		if (args.length != 2) {
			return false;
		}
		String worldName = args[0];
		boolean value = Boolean.parseBoolean(args[1]);
		AdditionalWorld world = plugin.getWorlds().getAdditional().get(worldName);
		if (world != null) {
			if (world.hasNether()) {
				if (value) {
					plugin.sendMessage(sender, MessageId.world_worldNetherAlreadyEnabled, worldName);
				} else {
					plugin.sendMessage(sender, MessageId.world_worldNetherDisabled, worldName);
					world.setNether(false);
					Bukkit.unloadWorld(worldName, true);
				}
			} else {
				if (value) {
					plugin.sendMessage(sender, MessageId.world_worldNetherEnabled, worldName);
					world.setNether(true);
					AdditionalSubWorld nether = world.getNetherWorld();
					if (nether == null) {
						nether = new AdditionalSubWorld(plugin,
						                                world,
						                                null /* Will be affected by load() */,
						                                plugin.getPluginConfig().getDefaultRequiredPermission(),
						                                true,
						                                plugin.getPluginConfig().isDefaultHidden(),
						                                World.Environment.NETHER);
					}
					nether.load();
				} else {
					plugin.sendMessage(sender, MessageId.world_worldNetherAlreadyDisabled, worldName);
				}
			}
			return true;
		} else {
			plugin.sendMessage(sender, MessageId.world_unknownWorld, worldName);
			return true;
		}
	}

	private boolean subCmdWorldSetEnd(final CommandSender sender, final String[] args) {
		if (!Perms.hasWorldSetEnd(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		if (args.length != 2) {
			return false;
		}
		String worldName = args[0];
		boolean value = Boolean.parseBoolean(args[1]);
		AdditionalWorld world = plugin.getWorlds().getAdditional().get(worldName);
		if (world != null) {
			if (world.hasEnd()) {
				if (value) {
					plugin.sendMessage(sender, MessageId.world_worldEndAlreadyEnabled, worldName);
				} else {
					plugin.sendMessage(sender, MessageId.world_worldEndDisabled, worldName);
					world.setEnd(false);
				}
			} else {
				if (value) {
					plugin.sendMessage(sender, MessageId.world_worldEndEnabled, worldName);
					world.setEnd(true);
					AdditionalSubWorld end = world.getEndWorld();
					if (end == null) {
						end = new AdditionalSubWorld(plugin,
						                             world,
						                             null /* Will be affected by load() */,
						                             plugin.getPluginConfig().getDefaultRequiredPermission(),
						                             true,
						                             plugin.getPluginConfig().isDefaultHidden(),
						                             World.Environment.THE_END);
					}
					end.load();
				} else {
					plugin.sendMessage(sender, MessageId.world_worldEndAlreadyDisabled, worldName);
				}
			}
			return true;
		} else {
			plugin.sendMessage(sender, MessageId.world_unknownWorld, worldName);
			return true;
		}
	}

	private boolean cmdSpawn(final CommandSender sender) {
		if (!(sender instanceof Player)) {
			plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		}
		final Player player = (Player) sender;
		int spawnBehaviour = plugin.getPluginConfig().getSpawnCommandBehaviour();
		String worldName = null;
		if (spawnBehaviour == 0) {
			worldName = player.getWorld().getName();
		} else {
			worldName = Bukkit.getWorlds().get(0).getName();
		}
		GeneralWorld world = plugin.getWorlds().get(worldName);
		player.teleport(world.getSpawnLocation().toBukkitLocation());
		plugin.sendMessage(player, MessageId.world_teleportingToSpawn);
		return true;
	}

	private boolean cmdSetSpawn(final CommandSender sender) {
		if (!(sender instanceof Player)) {
			plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		}
		final Player player = (Player) sender;
		GeneralWorld world = plugin.getWorlds().get(player.getWorld().getName());
		world.setSpawnLocation(player.getLocation());
		plugin.sendMessage(player, MessageId.world_settingSpawnPoint, player.getWorld().getName());
		return true;
	}

	private boolean cmdWarp(final CommandSender sender, String[] args) {
		if (args.length == 0) {
			// Lists available warps
			plugin.sendMessage(sender, MessageId.world_availableWarps);
			StringBuilder builder = new StringBuilder();
			for (final Warp warp : plugin.getWarps()) {
				if (warp.isEnabled()) {
					boolean hasPermission = Perms.hasRequiredPermission(sender, warp.getRequiredPermission()) || Perms.hasWarpAll(sender);
					if (hasPermission) {
						builder.append(ChatColor.GRAY + ", " + ChatColor.GREEN + warp.getName());
					} else if (!warp.isHidden()) {
						builder.append(ChatColor.GRAY + ", " + ChatColor.RED + warp.getName());
					}
				}
			}
			if (builder.length() == 0) {
				sender.sendMessage(ChatColor.RED + "No warps");
			} else {
				String warps = builder.toString().substring((ChatColor.GRAY + ", ").length());
				String[] messages = ChatPaginator.wordWrap(warps, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
				for (String message : messages) {
					sender.sendMessage(message);
				}
			}
			return true;
		} else if (args.length == 1) {
			// Warp to warp location
			return subCmdWarp(sender, args[0]);
		} else { // Handle subcommands
			final String subCmd = args[0].toLowerCase();
			args = Arrays.copyOfRange(args, 1, args.length);
			switch (subCmd) {
				case "hidden":
				case "sethidden":
					return subCmdWarpSetHidden(sender, args);
				case "perm":
				case "setperm":
					return subCmdWarpSetPerm(sender, args);
				default:
					return false;
			}
		}
	}

	private boolean subCmdWarp(final CommandSender sender, final String givenWarpName) {
		if (!(sender instanceof Player)) {
			plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		}
		final Player player = (Player) sender;
		final String warpName = givenWarpName.toLowerCase();
		Warp warp = plugin.getWarps().get(warpName);
		if (warp != null) {
			if (Perms.hasRequiredPermission(player, warp.getRequiredPermission()) || Perms.hasWarpAll(player)) {
				final Location loc = warp.getLocation().toBukkitLocation();
				loc.getChunk().load();
				Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

					@Override
					public void run() {
						player.teleport(loc);
					}
				}, 1L);
				plugin.sendMessage(sender, MessageId.world_teleportedToWarp, warp.getName());
				return true;
			} else if (!warp.isHidden()) {
				plugin.sendMessage(player, MessageId.world_warpToThisWarpDisallowed, warp.getName());
				return true;
			} else {
				plugin.sendMessage(player, MessageId.world_unknownWarp, givenWarpName);
				return true;
			}
		} else {
			plugin.sendMessage(player, MessageId.world_unknownWarp, givenWarpName);
			return true;
		}
	}

	private boolean subCmdWarpSetHidden(final CommandSender sender, final String[] args) {
		if (!Perms.hasWarpSetHidden(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		if (args.length != 2) {
			return false;
		}
		String warpName = args[0];
		boolean hidden = Boolean.parseBoolean(args[1]);
		final Player player = (Player) sender;
		Warp warp = plugin.getWarps().get(warpName);
		if (warp != null) {
			warp.setHidden(hidden);
			if (hidden) {
				plugin.sendMessage(sender, MessageId.world_warpHiddenTrue, warpName);
			} else {
				plugin.sendMessage(sender, MessageId.world_warpHiddenFalse, warpName);
			}
			return true;
		} else {
			plugin.sendMessage(player, MessageId.world_unknownWarp, warpName);
			return true;
		}
	}

	private boolean subCmdWarpSetPerm(final CommandSender sender, final String[] args) {
		if (!Perms.hasWarpSetPerm(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		if (args.length != 2) {
			return false;
		}
		String warpName = args[0];
		String permission = args[1];
		if (plugin.getPluginConfig().getPermissionShortcuts().containsKey(permission.toLowerCase())) {
			permission = plugin.getPluginConfig().getPermissionShortcuts().get(permission.toLowerCase());
		}
		final Player player = (Player) sender;
		Warp warp = plugin.getWarps().get(warpName);
		if (warp != null) {
			warp.setRequiredPermission(permission);
			plugin.sendMessage(sender, MessageId.world_changedWarpRequiredPermission, warpName, permission);
			return true;
		} else {
			plugin.sendMessage(player, MessageId.world_unknownWarp, warpName);
			return true;
		}
	}

	private boolean cmdSetWarp(final CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		final Player player = (Player) sender;
		String warpName = args[0];
		boolean requiredPermissionProvided = false;
		String requiredPermission = plugin.getPluginConfig().getDefaultRequiredPermission();
		boolean hiddenProvided = false;
		boolean hidden = plugin.getPluginConfig().isDefaultHidden();
		if (args.length > 1) {
			requiredPermission = args[1];
			requiredPermissionProvided = true;
		}
		if (args.length > 2) {
			hidden = Boolean.parseBoolean(args[2]);
			hiddenProvided = true;
		}

		if (plugin.getWarps().containsKey(warpName)) {
			Warp warp = plugin.getWarps().get(warpName);
			warp.setLocation(player.getLocation());
			if (requiredPermissionProvided) {
				warp.setRequiredPermission(requiredPermission);
			}
			if (hiddenProvided) {
				warp.setHidden(hiddenProvided);
			}
		} else {
			Warp warp = new Warp(warpName, new NLocation(player.getLocation()), true, requiredPermission, hidden);
			plugin.getWarps().put(warpName, warp);
		}
		plugin.sendMessage(player, MessageId.world_settingWarpPoint, warpName);
		return true;
	}

	private boolean cmdDelWarp(CommandSender sender, String[] args) {
		if (!Perms.hasDelWarp(sender)) {
			plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		String warpName = args[0];
		Warp warp = plugin.getWarps().get(warpName);
		if (warp == null) {
			plugin.sendMessage(sender, MessageId.world_unknownWarp, warpName);
		} else {
			plugin.getWarps().remove(warp.getName());
			plugin.sendMessage(sender, MessageId.world_warpRemoved, warpName);
		}
		return true;
	}
}

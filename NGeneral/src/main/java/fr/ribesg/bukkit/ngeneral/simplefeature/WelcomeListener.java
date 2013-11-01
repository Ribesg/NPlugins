package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.common.event.PlayerJoinedEvent;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.lang.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class WelcomeListener implements Listener {

	private final NGeneral plugin;

	public WelcomeListener(NGeneral instance) {
		this.plugin = instance;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinedInitial(final PlayerJoinedEvent event) {
		event.getPlayer().sendMessage(plugin.getPluginConfig().getInitialMessage().split(Messages.LINE_SEPARATOR));
		for (int i = 0; i < 150; i++) {
			event.getPlayer().sendMessage("");
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinedWelcome(final PlayerJoinedEvent event) {
		final Server server = Bukkit.getServer();

		final String bukkitVersion = server.getBukkitVersion();
		final String ip = server.getIp();
		final String maxPlayers = Integer.toString(server.getMaxPlayers());
		final String motd = server.getMotd();
		final String name = server.getName();
		final String onlineMode = server.getOnlineMode() ? "§aOnline" : "§cOffline";
		final String onlinePlayersCount = Integer.toString(server.getOnlinePlayers().length);
		final String port = Integer.toString(server.getPort());
		final String serverId = server.getServerId();
		final String serverName = server.getServerName();
		final String version = server.getVersion();
		final String viewDistance = Integer.toString(server.getViewDistance());

		plugin.sendMessage(event.getPlayer(),
		                   MessageId.general_welcome,
		                   bukkitVersion,
		                   ip,
		                   maxPlayers,
		                   motd,
		                   name,
		                   onlineMode,
		                   onlinePlayersCount,
		                   port,
		                   serverId,
		                   serverName,
		                   version,
		                   viewDistance);
	}
}

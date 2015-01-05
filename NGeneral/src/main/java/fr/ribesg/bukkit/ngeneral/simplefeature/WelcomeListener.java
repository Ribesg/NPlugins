/***************************************************************************
 * Project file:    NPlugins - NGeneral - WelcomeListener.java             *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.WelcomeListener*
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;

import fr.ribesg.bukkit.ncore.event.PlayerJoinedEvent;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.lang.Messages;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class WelcomeListener implements Listener {

    private final NGeneral plugin;

    public WelcomeListener(final NGeneral instance) {
        this.plugin = instance;
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoinedInitial(final PlayerJoinedEvent event) {
        event.getPlayer().sendMessage(this.plugin.getPluginConfig().getInitialMessage().split(Messages.LINE_SEPARATOR));
        for (int i = 0; i < 25; i++) {
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

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        final String playerIp = player.getAddress().getAddress().getHostAddress();
        final String playerGamemode;
        switch (player.getGameMode()) {
            case SURVIVAL:
                playerGamemode = this.plugin.getMessages().get(MessageId.general_welcome_gameMode_survival)[0];
                break;
            case CREATIVE:
                playerGamemode = this.plugin.getMessages().get(MessageId.general_welcome_gameMode_creative)[0];
                break;
            case ADVENTURE:
                playerGamemode = this.plugin.getMessages().get(MessageId.general_welcome_gameMode_adventure)[0];
                break;
            default:
                throw new RuntimeException("wat.");
        }
        final String playerWorld = player.getWorld().getName();
        final String playerWorldType;
        switch (event.getPlayer().getWorld().getEnvironment()) {
            case NORMAL:
                playerWorldType = this.plugin.getMessages().get(MessageId.general_welcome_worldType_normal)[0];
                break;
            case NETHER:
                playerWorldType = this.plugin.getMessages().get(MessageId.general_welcome_worldType_nether)[0];
                break;
            case THE_END:
                playerWorldType = this.plugin.getMessages().get(MessageId.general_welcome_worldType_end)[0];
                break;
            default:
                throw new RuntimeException("wat.");
        }
        final String playerWorldDifficulty;
        switch (player.getWorld().getDifficulty()) {
            case PEACEFUL:
                playerWorldDifficulty = this.plugin.getMessages().get(MessageId.general_welcome_difficulty_peaceful)[0];
                break;
            case EASY:
                playerWorldDifficulty = this.plugin.getMessages().get(MessageId.general_welcome_difficulty_easy)[0];
                break;
            case NORMAL:
                playerWorldDifficulty = this.plugin.getMessages().get(MessageId.general_welcome_difficulty_normal)[0];
                break;
            case HARD:
                playerWorldDifficulty = this.plugin.getMessages().get(MessageId.general_welcome_difficulty_hard)[0];
                break;
            default:
                throw new RuntimeException("wat.");
        }

        final StringBuilder pluginList = new StringBuilder();
        final Plugin[] plugins = server.getPluginManager().getPlugins();
        final String pluginCount = Integer.toString(plugins.length);

        for (int i = 1; i <= plugins.length; i++) {
            pluginList.append(plugins[i - 1].getName());
            if (i < plugins.length) {
                pluginList.append(", ");
            }
        }

        this.plugin.sendMessage(event.getPlayer(), MessageId.general_welcome,
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
                                viewDistance,
                                pluginList.toString(),
                                pluginCount,
                                playerName,
                                playerIp,
                                playerGamemode,
                                playerWorld,
                                playerWorldType,
                                playerWorldDifficulty
        );
    }
}

package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.regex.Pattern;

public class NicknameFilter implements Listener {

	private final NGeneral plugin;
	private final Pattern  pattern;

	public NicknameFilter(NGeneral plugin) {
		this.plugin = plugin;
		this.pattern = Pattern.compile("^[\\w]{3,16}$");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			final String playerName = event.getPlayer().getName();
			if (!pattern.matcher(playerName).matches()) {
				event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
				event.setKickMessage(plugin.getMessages().get(MessageId.general_nicknameFilter_invalid, playerName)[0]);
			}
		}
	}
}

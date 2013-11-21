package fr.ribesg.bukkit.ngeneral.feature.itemnetwork;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetwork;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ItemNetworkCommandExecutor implements CommandExecutor {

	private final ItemNetworkFeature feature;

	public ItemNetworkCommandExecutor(final ItemNetworkFeature feature) {
		this.feature = feature;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (command.getName().equals("itemnetwork")) {
			if (!Perms.hasItemNetwork(sender)) {
				feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			} else {
				if (args.length < 2) {
					return false;
				} else {
					final String networkName = args[1];
					final ItemNetwork network = feature.getNetworks().get(networkName.toLowerCase());
					switch (args[0].toLowerCase()) {
						case "c":
						case "create":
							if (network != null) {
								feature.getPlugin().sendMessage(sender, MessageId.general_itemnetwork_alreadyExists, networkName);
								return true;
							} else {
								feature.getNetworks()
								       .put(networkName.toLowerCase(), new ItemNetwork(feature, networkName, sender.getName()));
								feature.getPlugin().sendMessage(sender, MessageId.general_itemnetwork_created, networkName);
								return true;
							}
						case "d":
						case "delete":
							if (network == null) {
								feature.getPlugin().sendMessage(sender, MessageId.general_itemnetwork_unknown, networkName);
								return true;
							} else if (network.getCreator().equals(sender.getName()) || Perms.isAdmin(sender)) {
								network.destroy();
								feature.getNetworks().remove(networkName.toLowerCase());
								feature.getPlugin().sendMessage(sender, MessageId.general_itemnetwork_deleted, networkName);
								return true;
							} else {
								feature.getPlugin().sendMessage(sender, MessageId.general_itemnetwork_youNeedToBeCreator);
								return true;
							}
						default:
							return false;
					}
				}
			}
		} else {
			return false;
		}
	}
}

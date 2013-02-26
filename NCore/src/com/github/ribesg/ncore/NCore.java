package com.github.ribesg.ncore;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.ribesg.ncore.nodes.chat.ChatNode;
import com.github.ribesg.ncore.nodes.cuboid.CuboidNode;
import com.github.ribesg.ncore.nodes.dodgeball.DodgeBallNode;
import com.github.ribesg.ncore.nodes.enchantingegg.EnchantingEggNode;
import com.github.ribesg.ncore.nodes.general.GeneralNode;
import com.github.ribesg.ncore.nodes.punisher.PunisherNode;
import com.github.ribesg.ncore.nodes.theendagain.TheEndAgainNode;

public class NCore extends JavaPlugin {

	@Getter @Setter private ChatNode			chatNode;
	@Getter @Setter private CuboidNode			cuboidNode;
	@Getter @Setter private DodgeBallNode		dodgeBallNode;
	@Getter @Setter private EnchantingEggNode	enchantingEggNode;
	@Getter @Setter private GeneralNode			generalNode;
	@Getter @Setter private PunisherNode		punisherNode;
	@Getter @Setter private TheEndAgainNode		theEndAgainNode;

	@Override
	public void onEnable() {
		// Nothing yet 
	}

	@Override
	public void onDisable() {
		// Nothing yet
	}
}

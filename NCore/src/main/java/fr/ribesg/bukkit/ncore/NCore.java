package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.node.dodgeball.DodgeBallNode;
import fr.ribesg.bukkit.ncore.node.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ncore.node.player.PlayerNode;
import fr.ribesg.bukkit.ncore.node.talk.TalkNode;
import fr.ribesg.bukkit.ncore.node.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Core of the N Plugin Suite
 *
 * @author Ribesg
 */
public class NCore extends JavaPlugin {

	private CuboidNode        cuboidNode;
	private DodgeBallNode     dodgeBallNode;
	private EnchantingEggNode enchantingEggNode;
	private GeneralNode       generalNode;
	private PlayerNode        playerNode;
	private TalkNode          talkNode;
	private TheEndAgainNode   theEndAgainNode;
	private WorldNode         worldNode;

	@Override
	public void onEnable() {
		// Nothing yet
	}

	@Override
	public void onDisable() {
		// Nothing yet
	}

	public TalkNode getTalkNode() {
		return talkNode;
	}

	public void setTalkNode(TalkNode talkNode) {
		this.talkNode = talkNode;
	}

	public CuboidNode getCuboidNode() {
		return cuboidNode;
	}

	public void setCuboidNode(CuboidNode cuboidNode) {
		this.cuboidNode = cuboidNode;
	}

	public DodgeBallNode getDodgeBallNode() {
		return dodgeBallNode;
	}

	public void setDodgeBallNode(DodgeBallNode dodgeBallNode) {
		this.dodgeBallNode = dodgeBallNode;
	}

	public EnchantingEggNode getEnchantingEggNode() {
		return enchantingEggNode;
	}

	public void setEnchantingEggNode(EnchantingEggNode enchantingEggNode) {
		this.enchantingEggNode = enchantingEggNode;
	}

	public GeneralNode getGeneralNode() {
		return generalNode;
	}

	public void setGeneralNode(GeneralNode generalNode) {
		this.generalNode = generalNode;
	}

	public PlayerNode getPlayerNode() {
		return playerNode;
	}

	public void setPlayerNode(PlayerNode playerNode) {
		this.playerNode = playerNode;
	}

	public TheEndAgainNode getTheEndAgainNode() {
		return theEndAgainNode;
	}

	public void setTheEndAgainNode(TheEndAgainNode theEndAgainNode) {
		this.theEndAgainNode = theEndAgainNode;
	}

	public WorldNode getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(WorldNode worldNode) {
		this.worldNode = worldNode;
	}
}

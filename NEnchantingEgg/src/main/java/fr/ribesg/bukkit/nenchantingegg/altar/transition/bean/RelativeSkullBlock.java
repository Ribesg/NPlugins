/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - RelativeSkullBlock.java    *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSkullBlock
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;

public class RelativeSkullBlock extends RelativeBlock {

	private final SkullType type;
	private final BlockFace rotation;

	public RelativeSkullBlock(final double x, final double y, final double z, final SkullType type, final BlockFace rotation) {
		super(x, y, z, Material.SKULL, BlockData.SKULL_FLOOR);
		this.type = type;
		this.rotation = rotation;
	}

	@Override
	public boolean needAdditionalData() {
		return true;
	}

	@Override
	public void setAdditionalData(final Block block) {
		final Skull skullState = (Skull) block.getState();
		skullState.setSkullType(type);
		skullState.setRotation(rotation);
		skullState.update();
	}

}

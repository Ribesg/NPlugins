package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class RelativeBlock extends RelativeLocation {

	private final Material blockMaterial;
	private final byte     blockData;

	public RelativeBlock(final double x, final double y, final double z, final Material blockMaterial) {
		this(x, y, z, blockMaterial, (byte) 0);
	}

	public RelativeBlock(final double x, final double y, final double z, final Material blockMaterial, final byte blockData) {
		super(x, y, z);
		this.blockMaterial = blockMaterial;
		this.blockData = blockData;
	}

	public boolean needAdditionalData() {
		return false;
	}

	public void setAdditionalData(final Block theBlock) {
		throw new UnsupportedOperationException();
	}

	public byte getBlockData() {
		return blockData;
	}

	public Material getBlockMaterial() {
		return blockMaterial;
	}
}

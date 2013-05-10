package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class RelativeBlock extends Relative {

    @Getter private final Material blockMaterial;
    @Getter private final byte     blockData;

    public RelativeBlock(final int x, final int y, final int z, final Material blockMaterial) {
        this(x, y, z, blockMaterial, (byte) 0);
    }

    public RelativeBlock(final int x, final int y, final int z, final Material blockMaterial, final byte blockData) {
        super(x, y, z);
        this.blockMaterial = blockMaterial;
        this.blockData = blockData;
    }

    public boolean needSpecialWork() {
        return false;
    }

    public void doSpecialWork(final Block theBlock) {};
}

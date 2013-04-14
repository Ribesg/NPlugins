package fr.ribesg.bukkit.nenchantingegg.altar.transitions.beans;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class RelativeBlock {

    @Getter private final Vector   relativeLocation;
    @Getter private final Material blockMaterial;
    @Getter private final byte     blockData;

    public RelativeBlock(final int x, final int y, final int z, final Material blockMaterial, final byte blockData) {
        final Vector relativeLocation = new Vector(x, y, z);
        this.relativeLocation = relativeLocation;
        this.blockMaterial = blockMaterial;
        this.blockData = blockData;
    }

    public RelativeBlock(final int x, final int y, final int z, final Material blockMaterial) {
        this(x, y, z, blockMaterial, (byte) 0);
    }

    public boolean needSpecialWork() {
        return false;
    }

    public void doSpecialWork(final Block theBlock) {};
}

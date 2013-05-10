package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;

public class RelativeSkullBlock extends RelativeBlock {

    private final SkullType type;
    private final BlockFace rotation;

    public RelativeSkullBlock(final int x, final int y, final int z, final SkullType type, final BlockFace rotation) {
        super(x, y, z, Material.SKULL, (byte) 1);
        this.type = type;
        this.rotation = rotation;
    }

    @Override
    public boolean needSpecialWork() {
        return true;
    }

    @Override
    public void doSpecialWork(final Block block) {
        final Skull skullState = (Skull) block.getState();
        skullState.setSkullType(type);
        skullState.setRotation(rotation);
        skullState.update();
    };

}

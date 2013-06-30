package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ItemBuilder {

    private static Random       rand              = new Random();
    private static Set<Integer> possibleMainItems = null;

    private static Set<Integer> getPossibleMainItems() {
        if (possibleMainItems == null) {
            possibleMainItems = new HashSet<Integer>();
            for (int i = 256; i <= 258; i++) {
                possibleMainItems.add(i);
            }
            possibleMainItems.add(261);
            for (int i = 267; i <= 279; i++) {
                possibleMainItems.add(i);
            }
            for (int i = 283; i <= 286; i++) {
                possibleMainItems.add(i);
            }
            for (int i = 290; i <= 294; i++) {
                possibleMainItems.add(i);
            }
            for (int i = 298; i <= 317; i++) {
                possibleMainItems.add(i);
            }
        }
        return possibleMainItems;
    }

    // Boost multiplied by COEF%
    private static final float COEF = 150;

    private final NEnchantingEgg plugin;

    private       ItemStack       mainItem;
    private final List<ItemStack> items;
    private final Altar           altar;

    public ItemBuilder(final Altar altar, final String playerName) {
        this.altar = altar;
        this.plugin = altar.getPlugin();
        items = new ArrayList<ItemStack>();
    }

    public void addItem(final ItemStack is) {
        if (getPossibleMainItems().contains(is.getTypeId()) && is.getEnchantments().size() != 0) {
            System.out.println("Main item");
            mainItem = is;
            plugin.getEggProvidedToItemProvidedTransition().doTransition(altar);
        } else {
            items.add(is);
            System.out.println("Not main item");
        }
    }

    public void computeItem() {
        if (items != null && mainItem != null) {
            // Step 1: repair
            repair();

            // TODO: Other steps

            // Output the item
            altar.buildItem(mainItem);
        } else {
            plugin.getItemProvidedToLockedTransition().doTransition(altar);
        }
    }

    private void repair() {
        final int id = mainItem.getTypeId();
        final short maxDurability = Material.getMaterial(id).getMaxDurability();
        System.out.println("MaxDurability=" + maxDurability);

        // Get the total durability points sacrificed, in %
        double repairCount = 0;
        final Iterator<ItemStack> it = items.iterator();
        ItemStack is;
        while (it.hasNext()) {
            is = it.next();
            if (is.getTypeId() == id) {
                repairCount += is.getAmount() * ((maxDurability - is.getDurability()) / (double) maxDurability);
                it.remove();
            }
        }
        System.out.println("RepairCount=" + repairCount);

        // Get the number of enchantment levels
        int totalEnchantmentLevel = 0;
        for (final Integer i : mainItem.getEnchantments().values()) {
            totalEnchantmentLevel += i;
        }
        System.out.println("TotalEnchantmentLevel=" + totalEnchantmentLevel);

        // Compute base durability boost
        double boost = COEF / 100.0 * repairCount / totalEnchantmentLevel;
        System.out.println("Boost=" + boost);

        // Add some randomness: boost = 80%*boost + [0-40%]*boost; => boost = [80-120%]*boost;
        boost = boost - 0.2 * boost + rand.nextFloat() * 0.4 * boost;
        System.out.println("Boost=" + boost + " (Random)");

        // Apply durability
        double finalDurability = mainItem.getDurability() - boost * maxDurability;
        if (finalDurability < 0) {
            finalDurability = 0;
        }
        System.out.println("FinalDurability=" + finalDurability);
        mainItem.setDurability((short) finalDurability);
    }
}

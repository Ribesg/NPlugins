/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - ItemBuilder.java           *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.item.ItemBuilder       *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.item;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Based on a main item and some ingredients, builds a new boosted item.
 */
public class ItemBuilder {

    private static final Random RANDOM = new Random();
    private static Set<Material> possibleMainItems;

    private static final double[][] BOOST_VALUES = {
            {
                    1.3d,
                    -0.05d
            },
            {
                    1.1d,
                    -0.15d
            },
            {
                    1.2d,
                    -0.37d
            },
            {
                    1.4d,
                    -0.68d
            },
            {
                    1.7d,
                    -1.09d
            },
            {
                    1.9d,
                    -1.42d
            }
    };
    private static final double     ENCH_REDUCE  = 0.1f;

    /**
     * List of items that can be boosted
     */
    private static Set<Material> getPossibleMainItems() {
        if (possibleMainItems == null) {
            possibleMainItems = EnumSet.of(
                    // Axes
                    Material.WOOD_AXE,
                    Material.STONE_AXE,
                    Material.IRON_AXE,
                    Material.GOLD_AXE,
                    Material.DIAMOND_AXE,

                    // Pickaxes
                    Material.WOOD_PICKAXE,
                    Material.IRON_PICKAXE,
                    Material.GOLD_PICKAXE,
                    Material.DIAMOND_PICKAXE,
                    Material.STONE_PICKAXE,

                    // Shovels
                    Material.WOOD_SPADE,
                    Material.STONE_SPADE,
                    Material.IRON_SPADE,
                    Material.GOLD_SPADE,
                    Material.DIAMOND_SPADE,

                    // Hoes
                    Material.WOOD_HOE,
                    Material.STONE_HOE,
                    Material.IRON_HOE,
                    Material.GOLD_HOE,
                    Material.DIAMOND_HOE,

                    // Weapons
                    Material.BOW,
                    Material.WOOD_SWORD,
                    Material.STONE_SWORD,
                    Material.IRON_SWORD,
                    Material.GOLD_SWORD,
                    Material.DIAMOND_SWORD,

                    // Leather Armor
                    Material.LEATHER_HELMET,
                    Material.LEATHER_CHESTPLATE,
                    Material.LEATHER_LEGGINGS,
                    Material.LEATHER_BOOTS,

                    // Iron Armor
                    Material.IRON_HELMET,
                    Material.IRON_CHESTPLATE,
                    Material.IRON_LEGGINGS,
                    Material.IRON_BOOTS,

                    // Gold Armor
                    Material.GOLD_HELMET,
                    Material.GOLD_CHESTPLATE,
                    Material.GOLD_LEGGINGS,
                    Material.GOLD_BOOTS,

                    // Diamond Armor
                    Material.DIAMOND_HELMET,
                    Material.DIAMOND_CHESTPLATE,
                    Material.DIAMOND_LEGGINGS,
                    Material.DIAMOND_BOOTS,

                    // Chainmail Armor
                    Material.CHAINMAIL_HELMET,
                    Material.CHAINMAIL_CHESTPLATE,
                    Material.CHAINMAIL_LEGGINGS,
                    Material.CHAINMAIL_BOOTS,

                    // Misc
                    Material.CARROT_STICK,
                    Material.FISHING_ROD,
                    Material.FLINT_AND_STEEL,
                    Material.SHEARS
            );
        }
        return possibleMainItems;
    }

    /**
     * The amount of base material needed based on the type of item
     */
    private int getBaseRessourceAmount(final Material material) {
        switch (material) {
            case LEATHER_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
            case IRON_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLD_CHESTPLATE:
                return 8;
            case LEATHER_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case IRON_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLD_LEGGINGS:
                return 7;
            case LEATHER_HELMET:
            case CHAINMAIL_HELMET:
            case IRON_HELMET:
            case DIAMOND_HELMET:
            case GOLD_HELMET:
                return 5;
            case LEATHER_BOOTS:
            case CHAINMAIL_BOOTS:
            case IRON_BOOTS:
            case DIAMOND_BOOTS:
            case GOLD_BOOTS:
                return 4;
            case IRON_PICKAXE:
            case IRON_AXE:
            case WOOD_PICKAXE:
            case WOOD_AXE:
            case STONE_PICKAXE:
            case STONE_AXE:
            case DIAMOND_PICKAXE:
            case DIAMOND_AXE:
            case GOLD_PICKAXE:
            case GOLD_AXE:
            case FISHING_ROD:
            case CARROT_STICK:
                return 3;
            case IRON_SWORD:
            case WOOD_SWORD:
            case STONE_SWORD:
            case DIAMOND_SWORD:
            case GOLD_SWORD:
            case WOOD_HOE:
            case STONE_HOE:
            case IRON_HOE:
            case DIAMOND_HOE:
            case GOLD_HOE:
            case SHEARS:
                return 2;
            default:
                return 1;
        }
    }

    private final NEnchantingEgg plugin;

    private       ItemStack       mainItem;
    private final List<ItemStack> items;
    private final Altar           altar;

    public ItemBuilder(final Altar altar) {
        this.altar = altar;
        this.plugin = altar.getPlugin();
        this.items = new ArrayList<>();
    }

    /**
     * Handles the reception of an item
     */
    public void addItem(final ItemStack is) {
        this.plugin.entering(this.getClass(), "addItem");

        if (getPossibleMainItems().contains(is.getType()) && !is.getEnchantments().isEmpty()) {
            this.plugin.debug("Main item detected");
            this.mainItem = is;
            if (this.altar.getEggLocation().getBlock().getType() != Material.DRAGON_EGG) {
                this.plugin.debug("The Dragon Egg is no longer here, cancel everything");
                this.items.clear();
                this.altar.hardResetToInactive(true);
            } else {
                this.plugin.debug("The Dragon Egg is still here, proceed to next step");
                this.plugin.getEggProvidedToItemProvidedTransition().doTransition(this.altar);
            }
        } else {
            this.plugin.debug("Secondary item detected");
            this.items.add(is);
        }

        this.plugin.exiting(this.getClass(), "addItem");
    }

    /**
     * Spawn back stored items.
     */
    public void popItems() {
        final Location loc = this.altar.getCenterLocation().toBukkitLocation().clone().add(0.5, 3, 0.5);
        for (final ItemStack is : this.items) {
            loc.getWorld().dropItem(loc, is);
        }
        this.items.clear();
    }

    public void computeItem() {
        this.plugin.entering(this.getClass(), "computeItem");

        if (!this.items.isEmpty() && this.mainItem != null) {
            this.plugin.debug("Starting item computation process");

            // Step 1: repair
            this.repair();

            // Step 2: boost
            this.boost();

            // Step 3: enchant
            this.enchant();

            // TODO: Other steps

            // Output the item
            this.altar.buildItem(this.mainItem, this.items);
        } else {
            this.plugin.debug("Something is missing, just give back all items");
            this.altar.buildItem(this.mainItem, this.items);
            this.plugin.getItemProvidedToLockedTransition().doTransition(this.altar);
        }

        this.plugin.exiting(this.getClass(), "computeItem");
    }

    private void repair() {
        this.plugin.entering(this.getClass(), "repair");

        final Material mat = this.mainItem.getType();
        final short maxDurability = mat.getMaxDurability();

        if (this.plugin.isDebugEnabled()) {
            this.plugin.debug("Repairing item " + mat.name() + " with max durability " + maxDurability);
        }

        // Get the total durability points sacrificed, in %
        double repairCount = 0;
        final Iterator<ItemStack> it = this.items.iterator();
        ItemStack is;
        while (it.hasNext()) {
            is = it.next();
            if (is.getType() == mat) {
                repairCount += is.getAmount() * ((maxDurability - is.getDurability()) / (double)maxDurability);
                it.remove();
            }
        }

        final int baseRessourceAmount = this.getBaseRessourceAmount(mat);

        this.plugin.debug("repairCount=" + repairCount);
        this.plugin.debug("baseRessourceAmount=" + baseRessourceAmount);

        repairCount *= baseRessourceAmount;

        this.plugin.debug("repairCount*baseRessourceAmount=" + repairCount);

        // Get the number of enchantment levels
        int totalEnchantmentLevel = 0;
        for (final Integer i : this.mainItem.getEnchantments().values()) {
            totalEnchantmentLevel += i;
        }

        this.plugin.debug("totalEnchantmentLevel=" + totalEnchantmentLevel);

        // Compute base durability boost
        final double configurableCoef = this.plugin.getPluginConfig().getRepairBoostMultiplier();
        double boost = configurableCoef * repairCount / totalEnchantmentLevel;

        this.plugin.debug("configurableCoef=" + configurableCoef);
        this.plugin.debug("boost=" + boost);

        // Add some randomness: boost = 80%*boost + [0-40%]*boost; => boost = [80-120%]*boost;
        boost = boost - 0.2 * boost + RANDOM.nextFloat() * 0.4 * boost;

        this.plugin.debug("randomizedBoost=" + boost);

        // Apply durability
        double finalDurability = this.mainItem.getDurability() - boost * maxDurability;
        if (finalDurability < 0) {
            finalDurability = 0;
        }

        this.plugin.debug("finalDurability=" + finalDurability);

        this.mainItem.setDurability((short)finalDurability);

        this.plugin.exiting(this.getClass(), "repair");
    }

    private void boost() {
        this.plugin.entering(this.getClass(), "boost");

        if (this.plugin.isDebugEnabled()) {
            this.plugin.debug("Original list of Enchantments:");
            for (final Map.Entry<Enchantment, Integer> e : this.mainItem.getEnchantments().entrySet()) {
                this.plugin.debug('\t' + e.getKey().getName() + ", level " + e.getValue());
            }
        }

        // Count the amount of Magma Cream and Eye of Ender sacrificed
        int magmaCream = 0;
        int eyeOfEnder = 0;
        final Iterator<ItemStack> it = this.items.iterator();
        ItemStack is;
        while (it.hasNext()) {
            is = it.next();
            if (is.getType() == Material.MAGMA_CREAM && magmaCream < 64) {
                magmaCream += is.getAmount();
                it.remove();
            } else if (is.getType() == Material.EYE_OF_ENDER && eyeOfEnder < 64) {
                eyeOfEnder += is.getAmount();
                it.remove();
            }
            if (magmaCream == 64 && eyeOfEnder == 64) {
                break;
            }
        }
        this.plugin.debug("Found " + magmaCream + " Magma Cream(s)");
        this.plugin.debug("Found " + eyeOfEnder + " Eye(s) of Ender");

        // We do nothing if there's none
        if (magmaCream != 0 || eyeOfEnder != 0) {
            // Get the amount of enchantment levels
            double enchantments = -1f;
            for (final Integer i : this.mainItem.getEnchantments().values()) {
                enchantments += 0.75f + 0.25f * i;
            }

            this.plugin.debug("enchantments=" + enchantments);

            // Get the total amount of ingredients
            final int total = magmaCream + eyeOfEnder;

            this.plugin.debug("total=" + total);

            // Get a ratio between 0 and 1 of the total of ingredients
            double ratio = magmaCream == 0 ? 0 : eyeOfEnder / (float)magmaCream;
            if (ratio > 1) {
                ratio = 1 / ratio;
            }

            this.plugin.debug("ratio=" + ratio);

            // Weight the total with the ratio
            final double weightedTotal = total / 2f + ratio * (total / 2f);

            this.plugin.debug("weightedTotal=" + weightedTotal);

            // Get a coef between 0 and 1 from the weightedTotal (Math.exp(something between -1 and 0))
            final double coef = Math.exp(-(1f - weightedTotal / 128f));

            this.plugin.debug("coef=" + coef);

            // Compute probabilities
            final double[] probabilities = {
                    coef * BOOST_VALUES[0][0] + BOOST_VALUES[0][1] - ENCH_REDUCE * enchantments,
                    coef * BOOST_VALUES[1][0] + BOOST_VALUES[1][1] - ENCH_REDUCE * enchantments,
                    coef * BOOST_VALUES[2][0] + BOOST_VALUES[2][1] - ENCH_REDUCE * enchantments,
                    coef * BOOST_VALUES[3][0] + BOOST_VALUES[3][1] - ENCH_REDUCE * enchantments,
                    coef * BOOST_VALUES[4][0] + BOOST_VALUES[4][1] - ENCH_REDUCE * enchantments,
                    coef * BOOST_VALUES[5][0] + BOOST_VALUES[5][1] - ENCH_REDUCE * enchantments
            };

            this.plugin.debug("probabilities=" + Arrays.toString(probabilities));

            // Apply configurable coef and fix out-of-scope values
            final double configurableCoef = this.plugin.getPluginConfig().getEnchantmentBoostMultiplier();
            for (int i = 0; i < probabilities.length; i++) {
                probabilities[i] *= configurableCoef;
                if (probabilities[i] > 1) {
                    probabilities[i] = 1;
                } else if (probabilities[i] < 0) {
                    probabilities[i] = 0;
                }
            }

            this.plugin.debug("configurableCoef=" + configurableCoef);
            this.plugin.debug("fixedProbabilities=" + Arrays.toString(probabilities));

            // Roll dice
            final Map<Enchantment, Integer> newEnchantmentsMap = new HashMap<>();
            for (final Map.Entry<Enchantment, Integer> e : this.mainItem.getEnchantments().entrySet()) {
                int result = 0;
                for (int i = 6; i > 0; i--) {
                    if (RANDOM.nextFloat() <= probabilities[i - 1]) {
                        result = i;
                        break;
                    }
                }
                newEnchantmentsMap.put(e.getKey(), Math.min(this.plugin.getPluginConfig().getEnchantmentMaxLevel(e.getKey()), e.getValue() + result));
            }

            if (this.plugin.isDebugEnabled()) {
                this.plugin.debug("Final list of Enchantments:");
                for (final Map.Entry<Enchantment, Integer> e : newEnchantmentsMap.entrySet()) {
                    this.plugin.debug('\t' + e.getKey().getName() + ", level " + e.getValue());
                }
            }

            // Clear enchantments
            for (final Enchantment e : Enchantment.values()) {
                this.mainItem.removeEnchantment(e);
            }

            // Apply enchantments
            this.mainItem.addUnsafeEnchantments(newEnchantmentsMap);
        }

        this.plugin.exiting(this.getClass(), "boost");
    }

    private void enchant() {
        this.plugin.entering(this.getClass(), "enchant");

        if (this.plugin.isDebugEnabled()) {
            this.plugin.debug("Original list of Enchantments:");
            for (final Map.Entry<Enchantment, Integer> e : this.mainItem.getEnchantments().entrySet()) {
                this.plugin.debug('\t' + e.getKey().getName() + ", level " + e.getValue());
            }
        }

        // Count the amount of Ghast Tears sacrificed
        int ghastTear = 0;
        final Iterator<ItemStack> it = this.items.iterator();
        ItemStack is;
        while (it.hasNext()) {
            is = it.next();
            if (is.getType() == Material.GHAST_TEAR) {
                ghastTear += is.getAmount();
                it.remove();
                if (ghastTear == 16) {
                    break;
                }
            }
        }
        this.plugin.debug("Found " + ghastTear + " Ghast Tear(s)");

        if (ghastTear != 0) {

            // Arboricide
            if (this.plugin.getArboricide().canEnchant(this.mainItem)) {
                this.plugin.debug("Try to apply Arboricide");
                if (RANDOM.nextFloat() < (ghastTear / 16f) * (2f / 3f) * this.plugin.getPluginConfig().getEnchantmentBoostMultiplier()) {
                    this.mainItem = this.plugin.getArboricide().enchant(this.mainItem);
                    this.plugin.debug("Applied Arboricide!");
                }
            }

            // TODO Other enchantments here
        }

        this.plugin.exiting(this.getClass(), "enchant");
    }
}

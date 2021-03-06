package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.registry.SoulItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 11/01/2021
 */
public enum SoulItemTier implements IItemTier
{
    SOULIUM(3, 1251, 7f, 2.75f, 10, () -> {
        return Ingredient.of(SoulItems.SOULIUM_INGOT.get());
    }),
    POWERED_SOULIUM(4, 1561, 8.5f, 3.5f, 12, () -> {
        return Ingredient.of(SoulItems.SOULIUM_INGOT.get());
    });

    int harvestLevel;
    int maxUses;
    float efficiency;
    float attackDamage;
    int enchantability;
    LazyValue<Ingredient> repairMaterial;

    SoulItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial)
    {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    public int getUses()
    {
        return this.maxUses;
    }

    public float getSpeed()
    {
        return this.efficiency;
    }

    public float getAttackDamageBonus()
    {
        return this.attackDamage;
    }

    public int getLevel()
    {
        return this.harvestLevel;
    }

    public int getEnchantmentValue()
    {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient()
    {
        return this.repairMaterial.get();
    }
}

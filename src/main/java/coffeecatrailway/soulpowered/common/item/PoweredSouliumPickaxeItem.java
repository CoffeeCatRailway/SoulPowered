package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.item.PoweredToolItem;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;

/**
 * @author CoffeeCatRailway
 * Created: 11/01/2021
 */
public class PoweredSouliumPickaxeItem extends PoweredToolItem
{
    public static final int CAPACITY = 12_000;
    public static final int TRANSFER = 5000;

    public PoweredSouliumPickaxeItem(Properties properties)
    {
        super(1, -2.8f, SoulItemTier.POWERED_SOULIUM, PickaxeItem.DIGGABLES, properties.stacksTo(1).durability(0), ToolType.PICKAXE);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.getAttackDamage() / 2f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.8f, AttributeModifier.Operation.ADDITION));
        this.unpoweredAttributeModifiers = builder.build();
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state)
    {
        if (this.hasEnergy(stack))
        {
            int i = this.getTier().getLevel();
            if (state.getHarvestTool() == net.minecraftforge.common.ToolType.PICKAXE) {
                return i >= state.getHarvestLevel();
            }
            Material material = state.getMaterial();
            return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL;
        }
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        Material material = state.getMaterial();
        float speed = material != Material.METAL && material != Material.HEAVY_METAL && material != Material.STONE ? super.getDestroySpeed(stack, state) : this.speed;
        return speed / (this.isFoil(stack) ? 1f : 2f);
    }

    @Override
    public ForgeConfigSpec.IntValue getEnergyCost()
    {
        return SoulPoweredMod.SERVER_CONFIG.poweredSouliumPickaxeEnergyCost;
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return super.hasEnergy(stack, SoulPoweredMod.SERVER_CONFIG.poweredSouliumPickaxeEffectEnergyAmount.get());
    }

    @Override
    public int getMaxEnergy()
    {
        return CAPACITY;
    }

    @Override
    public int getMaxReceive()
    {
        return TRANSFER;
    }

    @Override
    public int getMaxExtract()
    {
        return TRANSFER;
    }
}

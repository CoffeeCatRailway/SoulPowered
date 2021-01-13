package coffeecatrailway.soulpowered.api.item;

import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * @author CoffeeCatRailway
 * Created: 12/01/2021
 */
public abstract class PoweredToolItem extends ToolItem implements IVanishable, IEnergyItem
{
    protected Multimap<Attribute, AttributeModifier> unpoweredAttributeModifiers;

    public PoweredToolItem(float attackDamage, float attackSpeed, IItemTier tier, Set<Block> effectiveBlocks, Properties properties, ToolType toolType)
    {
        super(attackDamage, attackSpeed, tier, effectiveBlocks, properties.addToolType(toolType, tier.getHarvestLevel()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        return super.getDestroySpeed(stack, state) / (this.hasEffect(stack) ? 1f : 2f);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        this.consumeEnergy(stack, false);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        this.consumeEnergy(stack, state.getBlockHardness(world, pos) != 0f);
        return true;
    }

    public abstract ForgeConfigSpec.IntValue getEnergyCost();

    protected void consumeEnergy(ItemStack stack, boolean doubleCost)
    {
        EnergyUtils.ifPresent(stack, energy -> energy.extractEnergy(this.getEnergyCost().get() * (doubleCost ? 2 : 1), false));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
        if (stack.isEmpty() || slot != EquipmentSlotType.MAINHAND)
            return super.getAttributeModifiers(slot, stack);
        return this.hasEffect(stack) ? super.getAttributeModifiers(slot) : this.unpoweredAttributeModifiers;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        super.fillItemGroup(group, items);
        if (this.isInGroup(group))
            this.addItemVarients(items);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        IEnergyItem.super.addInformation(stack, world, tooltip, flag);
    }
}

package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.item.PoweredModifierToolItem;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * @author CoffeeCatRailway
 * Created: 11/01/2021
 */
public class PoweredSouliumHoeItem extends PoweredModifierToolItem
{
    public static final int CAPACITY = 12_000;
    public static final int TRANSFER = 5000;

    public PoweredSouliumHoeItem(Properties properties)
    {
        super(-3, 0f, SoulItemTier.POWERED_SOULIUM, HoeItem.EFFECTIVE_ON_BLOCKS, properties.maxStackSize(1).defaultMaxDamage(0), ToolType.HOE);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.getAttackDamage() / 2f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", 0f, AttributeModifier.Operation.ADDITION));
        this.unpoweredAttributeModifiers = builder.build();
    }

    @Override
    protected ActionResultType modifyAtPosition(ItemUseContext context, BlockPos pos)
    {
        World world = context.getWorld();
        int hook = ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        if (context.getFace() != Direction.DOWN && world.isAirBlock(pos.up()))
        {
            BlockState modifiedState = world.getBlockState(pos).getToolModifiedState(world, pos, context.getPlayer(), context.getItem(), net.minecraftforge.common.ToolType.HOE);
            if (modifiedState != null)
            {
                PlayerEntity playerentity = context.getPlayer();
                world.playSound(playerentity, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote)
                {
                    world.setBlockState(pos, modifiedState, 11);
                    this.consumeEnergy(context.getItem(), true);
                }

                return ActionResultType.func_233537_a_(world.isRemote);
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ForgeConfigSpec.IntValue getEnergyCost()
    {
        return SoulPoweredMod.SERVER_CONFIG.poweredSouliumHoeEnergyCost;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return super.hasEnergy(stack, SoulPoweredMod.SERVER_CONFIG.poweredSouliumHoeEffectEnergyAmount.get());
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

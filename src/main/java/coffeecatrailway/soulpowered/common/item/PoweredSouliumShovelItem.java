package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.item.PoweredModifierToolItem;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;

/**
 * @author CoffeeCatRailway
 * Created: 11/01/2021
 */
public class PoweredSouliumShovelItem extends PoweredModifierToolItem
{
    public static final int CAPACITY = 12_000;
    public static final int TRANSFER = 5000;

    public PoweredSouliumShovelItem(Properties properties)
    {
        super(1.5f, -3f, SoulItemTier.POWERED_SOULIUM, ShovelItem.EFFECTIVE_ON, properties.maxStackSize(1).defaultMaxDamage(0), ToolType.SHOVEL);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.getAttackDamage() / 2f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -3f, AttributeModifier.Operation.ADDITION));
        this.unpoweredAttributeModifiers = builder.build();
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state)
    {
        return this.hasEnergy(stack) && (state.isIn(Blocks.SNOW) || state.isIn(Blocks.SNOW_BLOCK));
    }

    @Override
    protected ActionResultType modifyAtPosition(ItemUseContext context, BlockPos pos)
    {
        World world = context.getWorld();
        BlockState blockstate = world.getBlockState(pos);
        if (context.getFace() == Direction.DOWN)
            return ActionResultType.PASS;
        else
        {
            PlayerEntity player = context.getPlayer();
            BlockState modifiedState = blockstate.getToolModifiedState(world, pos, player, context.getItem(), net.minecraftforge.common.ToolType.SHOVEL);
            BlockState newState = null;
            if (modifiedState != null && world.isAirBlock(pos.up()))
            {
                world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1f, 1f);
                newState = modifiedState;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.get(CampfireBlock.LIT))
            {
                if (!world.isRemote())
                    world.playEvent(null, 1009, pos, 0);

                CampfireBlock.extinguish(world, pos, blockstate);
                newState = blockstate.with(CampfireBlock.LIT, false);
            }

            if (newState != null)
            {
                if (!world.isRemote)
                {
                    world.setBlockState(pos, newState, 11);
                    this.consumeEnergy(context.getItem(), true);
                }

                return ActionResultType.func_233537_a_(world.isRemote);
            } else
                return ActionResultType.PASS;
        }
    }

    @Override
    public ForgeConfigSpec.IntValue getEnergyCost()
    {
        return SoulPoweredMod.SERVER_CONFIG.poweredSouliumShovelEnergyCost;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return super.hasEnergy(stack, SoulPoweredMod.SERVER_CONFIG.poweredSouliumShovelEffectEnergyAmount.get());
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

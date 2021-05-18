package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulMod;
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
        super(1.5f, -3f, SoulItemTier.POWERED_SOULIUM, ShovelItem.DIGGABLES, properties.stacksTo(1).durability(0), ToolType.SHOVEL);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.getAttackDamage() / 2f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3f, AttributeModifier.Operation.ADDITION));
        this.unpoweredAttributeModifiers = builder.build();
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state)
    {
        return this.hasEnergy(stack) && (state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK));
    }

    @Override
    protected ActionResultType modifyAtPosition(ItemUseContext context, BlockPos pos)
    {
        World world = context.getLevel();
        BlockState blockstate = world.getBlockState(pos);
        if (context.getClickedFace() == Direction.DOWN)
            return ActionResultType.PASS;
        else
        {
            PlayerEntity player = context.getPlayer();
            BlockState modifiedState = blockstate.getToolModifiedState(world, pos, player, context.getItemInHand(), net.minecraftforge.common.ToolType.SHOVEL);
            BlockState newState = null;
            if (modifiedState != null && world.isEmptyBlock(pos.above()))
            {
                world.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1f, 1f);
                newState = modifiedState;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT))
            {
                if (!world.isClientSide())
                    world.levelEvent(null, 1009, pos, 0);

                CampfireBlock.dowse(world, pos, blockstate);
                newState = blockstate.setValue(CampfireBlock.LIT, false);
            }

            if (newState != null)
            {
                if (!world.isClientSide())
                {
                    world.setBlock(pos, newState, 11);
                    this.consumeEnergy(context.getItemInHand(), true);
                }

                return ActionResultType.sidedSuccess(world.isClientSide());
            } else
                return ActionResultType.PASS;
        }
    }

    @Override
    public ForgeConfigSpec.IntValue getEnergyCost()
    {
        return SoulMod.SERVER_CONFIG.poweredSouliumShovelEnergyCost;
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return super.hasEnergy(stack, SoulMod.SERVER_CONFIG.poweredSouliumShovelEffectEnergyAmount.get());
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

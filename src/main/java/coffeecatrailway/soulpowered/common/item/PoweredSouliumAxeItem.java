package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.item.PoweredModifierToolItem;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;

/**
 * @author CoffeeCatRailway
 * Created: 11/01/2021
 */
public class PoweredSouliumAxeItem extends PoweredModifierToolItem
{
    public static final int CAPACITY = 13_000;
    public static final int TRANSFER = 5000;

    public PoweredSouliumAxeItem(Properties properties)
    {
        super(5f, -3f, SoulItemTier.POWERED_SOULIUM, AxeItem.OTHER_DIGGABLE_BLOCKS, properties.stacksTo(1).durability(0), ToolType.AXE);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.getAttackDamage() / 2f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3f, AttributeModifier.Operation.ADDITION));
        this.unpoweredAttributeModifiers = builder.build();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        Material material = state.getMaterial();
        float speed = AxeItem.OTHER_DIGGABLE_BLOCKS.contains(material) ? this.speed : super.getDestroySpeed(stack, state);
        return speed / (this.isFoil(stack) ? 1f : 2f);
    }

    @Override
    protected ActionResultType modifyAtPosition(ItemUseContext context, BlockPos blockpos)
    {
        World world = context.getLevel();
        BlockState blockstate = world.getBlockState(blockpos);
        BlockState modifiedState = blockstate.getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.AXE);
        if (modifiedState != null)
        {
            PlayerEntity player = context.getPlayer();
            world.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1f, 1f);
            if (!world.isClientSide())
            {
                world.setBlock(blockpos, modifiedState, Constants.BlockFlags.DEFAULT_AND_RERENDER);
                this.consumeEnergy(context.getItemInHand(), true);
            }

            return ActionResultType.sidedSuccess(world.isClientSide());
        }
        return ActionResultType.PASS;
    }

    @Override
    public ForgeConfigSpec.IntValue getEnergyCost()
    {
        return SoulPoweredMod.SERVER_CONFIG.poweredSouliumAxeEnergyCost;
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return super.hasEnergy(stack, SoulPoweredMod.SERVER_CONFIG.poweredSouliumAxeEffectEnergyAmount.get());
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

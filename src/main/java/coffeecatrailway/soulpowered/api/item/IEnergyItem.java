package coffeecatrailway.soulpowered.api.item;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageImpl;
import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageItemImpl;
import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 22/12/2020
 */
public interface IEnergyItem extends IItemProvider, IForgeItem
{
    @Nonnull
    default <T> LazyOptional<T> getCapability(ItemStack stack, @Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() ->  new SoulEnergyStorageItemImpl(stack, IEnergyItem.this.getMaxEnergy(), IEnergyItem.this.getMaxReceive(), IEnergyItem.this.getMaxExtract())).cast();
        return LazyOptional.empty();
    }

    @Nullable
    @Override
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new ICapabilityProvider()
        {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
            {
                return IEnergyItem.this.getCapability(stack, cap, side);
            }
        };
    }

    default void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        // Apparently, addInformation can be called before caps are initialized
        if (CapabilityEnergy.ENERGY == null) return;
        EnergyUtils.ifPresent(stack, storage -> tooltip.add(SoulData.Lang.energyWithMax(storage.getEnergyStored(), storage.getMaxEnergyStored())));
    }

    default void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (this.isInGroupPrivate(group))
        {
            items.add(new ItemStack(this.getItem()));

            ItemStack full = new ItemStack(this.getItem());
            full.getOrCreateTag().putInt("Energy", this.getMaxEnergy());
            items.add(full);
        }
    }

    /**
     * Copied from: {@link net.minecraft.item.Item} <br/>
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    default boolean isInGroupPrivate(ItemGroup group)
    {
        if (getCreativeTabs().stream().anyMatch(tab -> tab == group)) return true;
        ItemGroup itemgroup = this.getItem().getGroup();
        return itemgroup != null && (group == ItemGroup.SEARCH || group == itemgroup);
    }

    @Override
    default boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    default double getDurabilityForDisplay(ItemStack stack)
    {
        return 1 - this.getChargeRatio(stack);
    }

    @Override
    default int getRGBDurabilityForDisplay(ItemStack stack)
    {
        return MathHelper.hsvToRGB((1 + this.getChargeRatio(stack)) / 3f, 1f, 1f);
    }

    default float getChargeRatio(ItemStack stack)
    {
        if (EnergyUtils.isPresent(stack))
        {
            IEnergyStorage energyStorage = EnergyUtils.get(stack).orElse(EnergyUtils.EMPTY);
            return (float) energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
        }
        return 0;
    }

    int getMaxEnergy();

    int getMaxReceive();

    int getMaxExtract();

    interface PickableBlock extends IForgeBlock
    {
        @Override
        default ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
        {
            ItemStack stack = new ItemStack(this.getBlock());
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof AbstractMachineTileEntity && tile.getCapability(CapabilityEnergy.ENERGY).isPresent() && EnergyUtils.isPresent(stack))
            {
                IEnergyStorage stackCap = EnergyUtils.getIfPresent(stack).orElse(EnergyUtils.EMPTY);
                if (stackCap instanceof SoulEnergyStorageImpl)
                {
                    IEnergyStorage tileCap = tile.getCapability(CapabilityEnergy.ENERGY).orElse(EnergyUtils.EMPTY);
                    ((SoulEnergyStorageImpl) stackCap).setEnergyExact(tileCap.getEnergyStored());
                }
            }

            return stack;
        }

        default void onBlockPlaceBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof AbstractMachineTileEntity && tile.getCapability(CapabilityEnergy.ENERGY).isPresent() && EnergyUtils.isPresent(stack))
            {
                IEnergyStorage tileCap = tile.getCapability(CapabilityEnergy.ENERGY).orElse(EnergyUtils.EMPTY);
                if (tileCap instanceof SoulEnergyStorageImpl)
                {
                    IEnergyStorage stackCap = EnergyUtils.getIfPresent(stack).orElse(EnergyUtils.EMPTY);
                    ((SoulEnergyStorageImpl) tileCap).setEnergyExact(stackCap.getEnergyStored());
                }
            }
        }
    }
}

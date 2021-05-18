package coffeecatrailway.soulpowered.api.item;

import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageItemImpl;
import coffeecatrailway.soulpowered.data.gen.SoulLanguage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

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
            return LazyOptional.of(() -> new SoulEnergyStorageItemImpl(stack, IEnergyItem.this.getMaxEnergy(), IEnergyItem.this.getMaxReceive(), IEnergyItem.this.getMaxExtract())).cast();
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
        if (CapabilityEnergy.ENERGY != null)
            EnergyUtils.ifPresent(stack, storage -> tooltip.add(SoulLanguage.energyWithMax(storage.getEnergyStored(), storage.getMaxEnergyStored())));
    }

    default void addItemVarients(NonNullList<ItemStack> items)
    {
        this.addItemVarients(items, nbt -> {});
    }

    default void addItemVarients(NonNullList<ItemStack> items, Consumer<CompoundNBT> extra)
    {
        ItemStack full = new ItemStack(this.getItem());
        CompoundNBT nbt = full.getOrCreateTag();
        nbt.putInt("Energy", this.getMaxEnergy());
        extra.accept(nbt);
        items.add(full);
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
        return MathHelper.hsvToRgb((1 + this.getChargeRatio(stack)) / 3f, 1f, 1f);
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

    default boolean hasEnergy(ItemStack stack, int amount)
    {
        return EnergyUtils.getIfPresent(stack).orElse(EnergyUtils.EMPTY).getEnergyStored() > amount;
    }

    default boolean hasEnergy(ItemStack stack)
    {
        return this.hasEnergy(stack, 0);
    }

    int getMaxEnergy();

    int getMaxReceive();

    int getMaxExtract();
}

package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageItemImpl;
import coffeecatrailway.soulpowered.utils.EnergyUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 18/11/2020
 */
public class EnergyItem extends Item
{
    private final int maxEnergy;
    private final int maxReceive;
    private final int maxExtract;

    public EnergyItem(Properties properties, int maxEnergy, int maxTransfer)
    {
        this(properties, maxEnergy, maxTransfer, maxTransfer);
    }

    public EnergyItem(Properties properties, int maxEnergy, int maxReceive, int maxExtract)
    {
        super(properties);
        this.maxEnergy = maxEnergy;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new ICapabilityProvider()
        {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
            {
                if (cap == CapabilityEnergy.ENERGY)
                    return LazyOptional.of(() -> new SoulEnergyStorageItemImpl(stack, EnergyItem.this.maxEnergy, EnergyItem.this.maxReceive, EnergyItem.this.maxExtract)).cast();
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        // Apparently, addInformation can be called before caps are initialized
        if (CapabilityEnergy.ENERGY == null) return;
        EnergyUtils.ifPresent(stack, storage -> tooltip.add(SoulData.Lang.energyWithMax(storage.getEnergyStored(), storage.getMaxEnergyStored())));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            items.add(new ItemStack(this));

            ItemStack full = new ItemStack(this);
            full.getOrCreateTag().putInt("Energy", this.maxEnergy);
            items.add(full);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1 - this.getChargeRatio(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        return MathHelper.hsvToRGB((1 + this.getChargeRatio(stack)) / 3f, 1f, 1f);
    }

    private float getChargeRatio(ItemStack stack)
    {
        if (EnergyUtils.isPresent(stack))
        {
            IEnergyStorage energyStorage = EnergyUtils.get(stack).orElse(EnergyUtils.EMPTY);
            return (float) energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
        }
        return 0;
    }

    public int getMaxEnergy()
    {
        return maxEnergy;
    }

    public int getMaxReceive()
    {
        return maxReceive;
    }

    public int getMaxExtract()
    {
        return maxExtract;
    }
}

package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageImpl;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 */
public interface IEnergyHandler
{
    SoulEnergyStorageImpl getEnergyImpl();

    default LazyOptional<IEnergyStorage> getEnergy(@Nullable Direction side)
    {
        return this.getEnergyImpl().getCapability(CapabilityEnergy.ENERGY, side);
    }

    default int getEnergyStored()
    {
        IEnergyStorage energy = this.getEnergy(null).orElse(new EnergyStorage(100_000));
        return energy.getEnergyStored();
    }

    default int getMaxEnergyStored()
    {
        IEnergyStorage energy = getEnergy(null).orElse(new EnergyStorage(100_000));
        return energy.getMaxEnergyStored();
    }

    default void setEnergyStoredExact(int value)
    {
        getEnergy(null).ifPresent(energy -> {
            if (energy instanceof SoulEnergyStorageImpl)
                ((SoulEnergyStorageImpl) energy).setEnergyExact(value);
        });
    }

    default void readEnergy(CompoundNBT nbt)
    {
        this.setEnergyStoredExact(nbt.getInt("Energy"));
    }

    default void writeEnergy(CompoundNBT nbt)
    {
        nbt.putInt("Energy", this.getEnergyStored());
    }
}

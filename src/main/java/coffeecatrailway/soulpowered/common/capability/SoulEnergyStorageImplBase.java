package coffeecatrailway.soulpowered.common.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 * Based on: https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/capability/EnergyStorageImplBase.java
 */
public class SoulEnergyStorageImplBase extends EnergyStorage implements ICapabilityProvider
{
    private final LazyOptional<SoulEnergyStorageImplBase> optional;

    public SoulEnergyStorageImplBase(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract, 0);
        this.optional = LazyOptional.of(() -> this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return CapabilityEnergy.ENERGY.orEmpty(cap, optional.cast());
    }

    public void invalidate()
    {
        this.optional.invalidate();
    }
}

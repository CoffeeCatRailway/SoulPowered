package coffeecatrailway.soulpowered.common.capability;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 * Base on: https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/capability/EnergyStorageImpl.java
 */
public class SoulEnergyStorageImpl extends SoulEnergyStorageImplBase
{
    private final EnumMap<Direction, LazyOptional<Connection>> connections = new EnumMap<>(Direction.class);
    private final TileEntity tileEntity;

    public SoulEnergyStorageImpl(int capacity, int maxReceive, int maxExtract, TileEntity tileEntity)
    {
        super(capacity, maxReceive, maxExtract);
        this.tileEntity = tileEntity;
        Arrays.stream(Direction.values()).forEach(d -> this.connections.put(d, LazyOptional.of(Connection::new)));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (side == null) return super.getCapability(cap, null);
        return CapabilityEnergy.ENERGY.orEmpty(cap, this.connections.get(side).cast());
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        this.connections.values().forEach(LazyOptional::invalidate);
    }

    public void createEnergy(int amount)
    {
        this.energy = Math.min(this.energy + amount, this.getMaxEnergyStored());
    }

    public void consumeEnergy(int amount)
    {
        this.energy = Math.max(this.energy - amount, 0);
    }

    public void setEnergyExact(int amount)
    {
        this.energy = amount;
    }

    public class Connection implements IEnergyStorage
    {
        private long lastReceiveTick;

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            World world = SoulEnergyStorageImpl.this.tileEntity.getWorld();
            if (world == null) return 0;

            int received = SoulEnergyStorageImpl.this.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate)
                this.lastReceiveTick = world.getGameTime();
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            World world = SoulEnergyStorageImpl.this.tileEntity.getWorld();
            if (world == null) return 0;

            long time = world.getGameTime();
            if (time != this.lastReceiveTick)
                return SoulEnergyStorageImpl.this.extractEnergy(maxExtract, simulate);
            return 0;
        }

        @Override
        public int getEnergyStored()
        {
            return SoulEnergyStorageImpl.this.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored()
        {
            return SoulEnergyStorageImpl.this.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract()
        {
            return SoulEnergyStorageImpl.this.canExtract();
        }

        @Override
        public boolean canReceive()
        {
            return SoulEnergyStorageImpl.this.canReceive();
        }
    }
}

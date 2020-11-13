package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageImpl;
import coffeecatrailway.soulpowered.utils.RedstoneMode;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.util.EnergyUtils;
import net.silentchaos512.utils.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 * Base on:
 * - https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/block/AbstractEnergyInventoryTileEntity.java
 * - https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/block/AbstractMachineBaseTileEntity.java
 */
public abstract class AbstractMachineTileEntity extends LockableSidedInventoryTileEntity implements IEnergyHandler, ITickableTileEntity
{
    public static final int FIELDS_COUNT = 5;

    protected RedstoneMode redstoneMode = RedstoneMode.IGNORED;
    protected final SoulEnergyStorageImpl energy;
    private final int maxExtract;

    private final IIntArray fields = new IIntArray()
    {
        @Override
        public int get(int index)
        {
            switch (index)
            {
                case 0: // Energy lower bytes
                    return AbstractMachineTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1: // Energy upper bytes
                    return (AbstractMachineTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2: // Max energy lower bytes
                    return AbstractMachineTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3: // Max energy upper bytes
                    return (AbstractMachineTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return AbstractMachineTileEntity.this.redstoneMode.ordinal();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value)
        {
            switch (index)
            {
                case 4:
                    AbstractMachineTileEntity.this.redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
                default:
                    AbstractMachineTileEntity.this.getEnergyImpl().setEnergyExact(value);
            }
        }

        @Override
        public int size()
        {
            return FIELDS_COUNT;
        }
    };

    public AbstractMachineTileEntity(TileEntityType<?> type, int inventorySize, int maxEnergy, int maxReceive, int maxExtract)
    {
        super(type, inventorySize);
        this.energy = new SoulEnergyStorageImpl(maxEnergy, maxReceive, maxExtract, this);
        this.maxExtract = maxExtract;
    }

    public RedstoneMode getRedstoneMode()
    {
        return this.redstoneMode;
    }

    public void setRedstoneMode(RedstoneMode redstoneMode)
    {
        this.redstoneMode = redstoneMode;
    }

    @Override
    public SoulEnergyStorageImpl getEnergyImpl()
    {
        return this.energy;
    }

    public IIntArray getFields()
    {
        return this.fields;
    }

    @Override
    public void tick()
    {
        if (this.world == null || this.world.isRemote) return;
        if (this.maxExtract > 0)
            EnergyUtils.trySendToNeighbors(this.world, this.pos, this, this.maxExtract);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        SyncVariable.Helper.readSyncVars(this, nbt);
        this.readEnergy(nbt);
        this.redstoneMode = EnumUtils.byOrdinal(nbt.getByte("RedstoneMode"), RedstoneMode.IGNORED);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        SyncVariable.Helper.writeSyncVars(this, nbt, SyncVariable.Type.WRITE);
        this.writeEnergy(nbt);
        nbt.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        super.onDataPacket(net, packet);
        CompoundNBT nbt = packet.getNbtCompound();
        SyncVariable.Helper.readSyncVars(this, nbt);
        this.readEnergy(nbt);
        this.redstoneMode = EnumUtils.byOrdinal(nbt.getByte("RedstoneMode"), RedstoneMode.IGNORED);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = super.getUpdateTag();
        SyncVariable.Helper.writeSyncVars(this, nbt, SyncVariable.Type.PACKET);
        nbt.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
        return nbt;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (!this.removed && cap == CapabilityEnergy.ENERGY)
            return this.getEnergy(side).cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove()
    {
        super.remove();
        this.energy.invalidate();
    }
}

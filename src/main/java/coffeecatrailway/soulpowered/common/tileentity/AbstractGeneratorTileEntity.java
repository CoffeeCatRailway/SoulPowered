package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.api.RedstoneMode;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 * Based on: https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/block/generator/AbstractGeneratorTileEntity.java
 */
public abstract class AbstractGeneratorTileEntity extends AbstractMachineTileEntity
{
    public static final int FIELDS_COUNT = 7;

    protected int burnTime;
    protected int totalBurnTime;

    protected final IIntArray fields = new IIntArray()
    {
        @Override
        public int get(int index)
        {
            switch (index)
            {
                case 0:
                    // Energy lower bytes
                    return AbstractGeneratorTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (AbstractGeneratorTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return AbstractGeneratorTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (AbstractGeneratorTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return AbstractGeneratorTileEntity.this.redstoneMode.ordinal();
                case 5:
                    return AbstractGeneratorTileEntity.this.burnTime;
                case 6:
                    return AbstractGeneratorTileEntity.this.totalBurnTime;
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
                    AbstractGeneratorTileEntity.this.redstoneMode = RedstoneMode.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
                case 5:
                    AbstractGeneratorTileEntity.this.burnTime = value;
                    break;
                case 6:
                    AbstractGeneratorTileEntity.this.totalBurnTime = value;
                    break;
            }
        }

        @Override
        public int getCount()
        {
            return FIELDS_COUNT;
        }
    };

    public AbstractGeneratorTileEntity(TileEntityType<?> type, int inventorySize, int maxEnergy, int maxReceive, int maxExtract)
    {
        super(type, inventorySize, maxEnergy, maxReceive, maxExtract);
    }

    protected abstract boolean hasFuel();

    protected abstract void consumeFuel();

    protected abstract int getEnergyCreatedPerTick();

    @Override
    protected BlockState getActiveState()
    {
        return super.getActiveState().setValue(AbstractFurnaceBlock.LIT, true);
    }

    @Override
    protected BlockState getInactiveState()
    {
        return super.getInactiveState().setValue(AbstractFurnaceBlock.LIT, false);
    }

    @Override
    public void tick()
    {
        if (this.canRun())
        {
            if (this.burnTime <= 0 && this.hasFuel())
            {
                this.consumeFuel();
                this.sendUpdate(this.getActiveState(), true);
            }

            if (this.burnTime > 0)
                this.energy.createEnergy(this.getEnergyCreatedPerTick());
        }

        if (this.burnTime > 0)
            this.burnTime--;
        else
            this.sendUpdate(this.getInactiveState(), false);
        super.tick();
    }

    @Override
    protected boolean canRun()
    {
        return super.canRun() && this.getEnergyStored() + this.getEnergyCreatedPerTick() <= this.getMaxEnergyStored();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.totalBurnTime = nbt.getInt("TotalBurnTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt)
    {
        nbt.putInt("BurnTime", this.burnTime);
        nbt.putInt("TotalBurnTime", this.totalBurnTime);
        return super.save(nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        super.onDataPacket(net, packet);
        CompoundNBT nbt = packet.getTag();
        this.burnTime = nbt.getInt("BurnTime");
        this.totalBurnTime = nbt.getInt("TotalBurnTime");
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putInt("BurnTime", this.burnTime);
        nbt.putInt("TotalBurnTime", this.totalBurnTime);
        return nbt;
    }
}

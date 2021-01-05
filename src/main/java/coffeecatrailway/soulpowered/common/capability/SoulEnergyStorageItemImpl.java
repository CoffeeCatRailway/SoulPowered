package coffeecatrailway.soulpowered.common.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

/**
 * @author CoffeeCatRailway
 * Created: 18/11/2020
 */
public class SoulEnergyStorageItemImpl extends EnergyStorage
{
    private final ItemStack stack;

    public SoulEnergyStorageItemImpl(ItemStack stack, int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract);
        this.stack = stack;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!this.canReceive())
            return 0;

        int energyStored = this.getEnergyStored();
        int energyReceived = Math.min(capacity - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            this.setEnergyStored(energyStored + energyReceived);
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!this.canExtract())
            return 0;

        int energyStored = this.getEnergyStored();
        int energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            this.setEnergyStored(energyStored - energyExtracted);
        return energyExtracted;
    }

    @Override
    public int getEnergyStored()
    {
        return this.stack.getOrCreateTag().getInt("Energy");
    }

    public void setEnergyStored(int amount)
    {
        this.stack.getOrCreateTag().putInt("Energy", amount);
    }
}

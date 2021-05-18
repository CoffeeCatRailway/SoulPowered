package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.inventory.container.CoalGeneratorContainer;
import coffeecatrailway.soulpowered.registry.SoulTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 6/01/2021
 */
public class CoalGeneratorTileEntity extends AbstractGeneratorTileEntity
{
    public static final int MAX_ENERGY = 20_000;
    public static final int MAX_SEND = 1000;
    public static final int ENERGY_CREATED_PER_TICK = 30;

    private final Tier tier;

    public CoalGeneratorTileEntity(Tier tier)
    {
        super(SoulTileEntities.COAL_GENERATOR.get(tier).get(), 1, tier.calculateEnergyCapacity(MAX_ENERGY), 0, tier.calculateEnergyTransfer(MAX_SEND));
        this.tier = tier;
    }

    public static boolean isFuel(ItemStack stack)
    {
        return AbstractFurnaceTileEntity.isFuel(stack);
    }

    @Override
    protected boolean hasFuel()
    {
        return isFuel(this.getItem(0));
    }

    @Override
    protected void consumeFuel()
    {
        ItemStack fuel = this.getItem(0);
        this.burnTime = ForgeHooks.getBurnTime(fuel);
        if (this.burnTime > 0)
        {
            this.totalBurnTime = this.burnTime;

            if (fuel.hasContainerItem())
                this.setItem(0, fuel.getContainerItem());
            else if (!fuel.isEmpty())
            {
                fuel.shrink(1);
                if (fuel.isEmpty())
                    this.setItem(0, fuel.getContainerItem());
            }
        }
    }

    @Override
    protected int getEnergyCreatedPerTick()
    {
        return this.tier.calculatePowerGenerated(ENERGY_CREATED_PER_TICK);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return isFuel(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return !isFuel(stack);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return new CoalGeneratorContainer(id, inventory, this, this.fields, this.tier);
    }
}

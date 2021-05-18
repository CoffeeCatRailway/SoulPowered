package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.api.item.ISoulFuel;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import coffeecatrailway.soulpowered.data.gen.SoulItemTags;
import coffeecatrailway.soulpowered.registry.SoulTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 * Based on: https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/block/generator/coal/CoalGeneratorTileEntity.java
 */
public class SoulGeneratorTileEntity extends AbstractGeneratorTileEntity
{
    public static final int MAX_ENERGY = 30_000;
    public static final int MAX_SEND = 1500;
    public static final int ENERGY_CREATED_PER_TICK = 60;

    public SoulGeneratorTileEntity()
    {
        super(SoulTileEntities.SOUL_GENERATOR.get(), 1, MAX_ENERGY, 0, MAX_SEND);
    }

    public static boolean isFuel(ItemStack stack)
    {
        return stack.getItem().is(SoulItemTags.SOUL_GENERATOR_FUEL) && stack.getItem() instanceof ISoulFuel;
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
        this.burnTime = ((ISoulFuel) fuel.getItem()).getBurnTime();
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
        return ENERGY_CREATED_PER_TICK;
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
        return new SoulGeneratorContainer(id, inventory, this, this.fields);
    }
}

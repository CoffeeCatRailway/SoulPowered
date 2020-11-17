package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import coffeecatrailway.soulpowered.registry.SoulTileEntities;
import coffeecatrailway.soulpowered.utils.BurnTimeUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 * Based on: https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/block/generator/coal/CoalGeneratorTileEntity.java
 */
public class SoulGeneratorTileEntity extends AbstractGeneratorTileEntity
{
    public static final int MAX_ENERGY = 10_000;
    public static final int MAX_SEND = 500;
    public static final int ENERGY_CREATED_PER_TICK = 60;

    public SoulGeneratorTileEntity()
    {
        this(SoulTileEntities.SOUL_GENERATOR.get());
    }

    public SoulGeneratorTileEntity(TileEntityType<? extends SoulGeneratorTileEntity> type)
    {
        super(type, 1, MAX_ENERGY, 0, MAX_SEND);
    }

    public static boolean isFuel(ItemStack stack)
    {
        return stack.getItem().isIn(SoulData.TagItems.SOUL_GENERATOR_FUEL);
    }

    @Override
    protected boolean hasFuel()
    {
        return isFuel(this.getStackInSlot(0));
    }

    @Override
    protected void consumeFuel()
    {
        ItemStack fuel = this.getStackInSlot(0);
        this.burnTime = BurnTimeUtils.getBurnTime(fuel);
        if (this.burnTime > 0)
        {
            this.totalBurnTime = this.burnTime;

            if (fuel.hasContainerItem())
                this.setInventorySlotContents(0, fuel.getContainerItem());
            else if (!fuel.isEmpty())
            {
                fuel.shrink(1);
                if (fuel.isEmpty())
                    this.setInventorySlotContents(0, fuel.getContainerItem());
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
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction)
    {
        return isFuel(stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return !isFuel(stack);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory)
    {
        return new SoulGeneratorContainer(id, playerInventory, this, this.fields);
    }
}

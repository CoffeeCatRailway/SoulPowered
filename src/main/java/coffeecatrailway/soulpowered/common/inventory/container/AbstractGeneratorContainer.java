package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;

/**
 * @author CoffeeCatRailway
 * Created: 8/01/2021
 */
public abstract class AbstractGeneratorContainer<T extends AbstractMachineTileEntity> extends AbstractEnergyStorageContainer<T>
{
    public AbstractGeneratorContainer(ContainerType<?> type, int id, T tile, IIntArray fields)
    {
        super(type, id, tile, fields);
    }

    public int getBurnTime()
    {
        return this.fields.get(5);
    }

    public int getTotalBurnTime()
    {
        return this.fields.get(6);
    }

    public boolean isBurning()
    {
        return getBurnTime() > 0;
    }

    protected abstract boolean isFuel(ItemStack stack);

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int inventorySize = 1;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index != 0)
            {
                if (this.isFuel(itemstack1))
                {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false))
                        return ItemStack.EMPTY;
                } else if (index < playerInventoryEnd)
                {
                    if (!this.moveItemStackTo(itemstack1, playerInventoryEnd, playerHotbarEnd, false))
                        return ItemStack.EMPTY;
                } else if (index < playerHotbarEnd && !this.moveItemStackTo(itemstack1, inventorySize, playerInventoryEnd, false))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, false))
                return ItemStack.EMPTY;

            if (itemstack1.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}

package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.common.tileentity.AbstractGeneratorTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
import coffeecatrailway.soulpowered.registry.SoulContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public class SoulGeneratorContainer extends AbstractEnergyStorageContainer<SoulGeneratorTileEntity>
{
    public SoulGeneratorContainer(ContainerType<? extends SoulGeneratorContainer> type, int id, PlayerInventory playerInventory)
    {
        this(type, id, playerInventory, new SoulGeneratorTileEntity(), new IntArray(AbstractGeneratorTileEntity.FIELDS_COUNT));
    }

    public SoulGeneratorContainer(int id, PlayerInventory playerInventory, SoulGeneratorTileEntity tileEntity, IIntArray fields)
    {
        this(SoulContainers.SOUL_GENERATOR.get(), id, playerInventory, tileEntity, fields);
    }

    public SoulGeneratorContainer(ContainerType<? extends SoulGeneratorContainer> type, int id, PlayerInventory playerInventory, SoulGeneratorTileEntity tileEntity, IIntArray fields)
    {
        super(type, id, tileEntity, fields);
        this.addSlot(new Slot(this.tileEntity, 0, 80, 33));
        this.addPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
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

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = 1;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index != 0)
            {
                if (this.isFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        return ItemStack.EMPTY;
                } else if (index < playerInventoryEnd)
                {
                    if (!this.mergeItemStack(itemstack1, playerInventoryEnd, playerHotbarEnd, false))
                        return ItemStack.EMPTY;
                } else if (index < playerHotbarEnd && !this.mergeItemStack(itemstack1, inventorySize, playerInventoryEnd, false))
                    return ItemStack.EMPTY;
            } else if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, false))
                return ItemStack.EMPTY;

            if (itemstack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    private boolean isFuel(ItemStack stack)
    {
        return SoulGeneratorTileEntity.isFuel(stack);
    }
}

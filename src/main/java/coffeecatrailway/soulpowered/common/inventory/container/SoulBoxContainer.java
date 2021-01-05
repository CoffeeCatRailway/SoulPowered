package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.SoulBoxTileEntity;
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
 * Created: 14/11/2020
 */
public class SoulBoxContainer extends AbstractEnergyStorageContainer<SoulBoxTileEntity>
{
    public SoulBoxContainer(ContainerType<? extends SoulBoxContainer> type, int id, PlayerInventory playerInventory, Tier tier)
    {
        this(type, id, playerInventory, new SoulBoxTileEntity(tier), new IntArray(AbstractMachineTileEntity.FIELDS_COUNT));
    }

    public SoulBoxContainer(int id, PlayerInventory playerInventory, SoulBoxTileEntity tileEntity, IIntArray fields, Tier tier)
    {
        this(SoulContainers.SOUL_BOX.get(tier).get(), id, playerInventory, tileEntity, fields);
    }

    public SoulBoxContainer(ContainerType<? extends SoulBoxContainer> type, int id, PlayerInventory playerInventory, SoulBoxTileEntity tileEntity, IIntArray fields)
    {
        super(type, id, tileEntity, fields);

        this.addSlot(new Slot(this.tileEntity, 0, 44, 34));
        this.addSlot(new Slot(this.tileEntity, 1, 116, 34));

        this.addPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = SoulBoxTileEntity.INVENTORY_SIZE;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index >= inventorySize)
            {
                if (this.isPoweredItem(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 0, inventorySize, false))
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

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean isPoweredItem(ItemStack stack)
    {
        return EnergyUtils.isPresent(stack);
    }
}

package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.tileentity.AlloySmelterTileEntity;
import coffeecatrailway.soulpowered.registry.SoulContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

/**
 * @author CoffeeCatRailway
 * Created: 15/12/2020
 */
public class AlloySmelterContainer extends AbstractEnergyStorageContainer<AlloySmelterTileEntity>
{
    public AlloySmelterContainer(int id, PlayerInventory inventory, Tier tier)
    {
        this(SoulContainers.ALLOY_SMELTER.get(tier).get(), id, inventory, new AlloySmelterTileEntity(tier), new IntArray(AlloySmelterTileEntity.FIELDS_COUNT));
    }

    public AlloySmelterContainer(int id, PlayerInventory inventory, AlloySmelterTileEntity tile, IIntArray fields, Tier tier)
    {
        this(SoulContainers.ALLOY_SMELTER.get(tier).get(), id, inventory, tile, fields);
    }

    public AlloySmelterContainer(ContainerType<? extends AlloySmelterContainer> type, int id, PlayerInventory inventory, AlloySmelterTileEntity tile, IIntArray fields)
    {
        super(type, id, tile, fields);

        this.addSlot(new Slot(this.tileEntity, 0, 59, 15));
        this.addSlot(new Slot(this.tileEntity, 1, 80, 15));
        this.addSlot(new Slot(this.tileEntity, 2, 101, 15));
        this.addSlot(new FurnaceResultSlot(inventory.player, this.tileEntity, 3, 80, 51));
        this.addPlayerSlots(inventory, 8, 84).forEach(this::addSlot);
    }

    public int getProgressTime()
    {
        return this.fields.get(5);
    }

    public int getTotalProgressTime()
    {
        return this.fields.get(6);
    }

    public boolean isCrafting()
    {
        return this.getProgressTime() < this.getTotalProgressTime();
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int inventorySize = 4;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index != 0)
            {
                if (this.hasRecipe(itemstack1))
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

    private boolean hasRecipe(ItemStack stack)
    {
        return true;
//        return this.world.getRecipeManager().getRecipe(SoulRecipes.ALLOY_SMELTING_TYPE, new Inventory(stack), this.world).isPresent();
    }
}

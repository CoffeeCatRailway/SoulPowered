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
import net.minecraft.world.World;

/**
 * @author CoffeeCatRailway
 * Created: 15/12/2020
 */
public class AlloySmelterContainer extends AbstractEnergyStorageContainer<AlloySmelterTileEntity>
{
    protected final World world;

    public AlloySmelterContainer(ContainerType<? extends AlloySmelterContainer> type, int id, PlayerInventory playerInventory, Tier tier)
    {
        this(type, id, playerInventory, new AlloySmelterTileEntity(tier), new IntArray(AlloySmelterTileEntity.FIELDS_COUNT));
    }

    public AlloySmelterContainer(int id, PlayerInventory playerInventory, AlloySmelterTileEntity tileEntity, IIntArray fields, Tier tier)
    {
        this(SoulContainers.ALLOY_SMELTER.get(tier).get(), id, playerInventory, tileEntity, fields);
    }

    public AlloySmelterContainer(ContainerType<? extends AlloySmelterContainer> type, int id, PlayerInventory playerInventory, AlloySmelterTileEntity tileEntity, IIntArray fields)
    {
        super(type, id, tileEntity, fields);
        this.world = playerInventory.player.world;

        this.addSlot(new Slot(this.tileEntity, 0, 59, 15));
        this.addSlot(new Slot(this.tileEntity, 1, 80, 15));
        this.addSlot(new Slot(this.tileEntity, 2, 101, 15));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, this.tileEntity, 3, 80, 51));
        this.addPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
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
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = 4;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index != 0)
            {
                if (this.hasRecipe(itemstack1))
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

    private boolean hasRecipe(ItemStack stack)
    {
        return true;
//        return this.world.getRecipeManager().getRecipe(SoulRecipes.ALLOY_SMELTING_TYPE, new Inventory(stack), this.world).isPresent();
    }
}

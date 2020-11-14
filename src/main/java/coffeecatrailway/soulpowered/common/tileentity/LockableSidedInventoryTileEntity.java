package coffeecatrailway.soulpowered.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 15/11/2020
 * Based on: https://github.com/SilentChaos512/SilentLib/blob/1.16.x/src/main/java/net/silentchaos512/lib/tile/LockableSidedInventoryTileEntity.java
 */
public abstract class LockableSidedInventoryTileEntity extends LockableTileEntity implements ISidedInventory
{
    protected NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    public LockableSidedInventoryTileEntity(TileEntityType<?> type, int inventorySize)
    {
        super(type);
        this.items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : this.items)
            if (!stack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        if (index < 0 || index >= this.items.size())
            return ItemStack.EMPTY;
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.items.set(index, stack);
        if (stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return this.world != null && this.world.getTileEntity(this.pos) == this && player.getPosition().distanceSq(this.pos) <= 64;
    }

    @Override
    public void clear()
    {
        this.items.clear();
    }

    @Override
    public void read(BlockState stateIn, CompoundNBT nbt)
    {
        super.read(stateIn, nbt);
        this.items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        ItemStackHelper.saveAllItems(nbt, this.items);
        return nbt;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT tags = getUpdateTag();
        ItemStackHelper.saveAllItems(tags, this.items);
        return new SUpdateTileEntityPacket(this.pos, 1, tags);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        super.onDataPacket(net, packet);
        ItemStackHelper.loadAllItems(packet.getNbtCompound(), this.items);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (side == Direction.UP)
                return this.handlers[0].cast();
            if (side == Direction.DOWN)
                return this.handlers[1].cast();
            return this.handlers[2].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove()
    {
        super.remove();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers)
            handler.invalidate();
    }
}

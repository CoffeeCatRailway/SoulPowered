package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.api.RedstoneMode;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author CoffeeCatRailway
 * Created: 15/12/2020
 */
public abstract class AbstractProcessMachineTileEntity<R extends IRecipe<?>> extends AbstractMachineTileEntity
{
    public static final int FIELDS_COUNT = 7;

    protected float progress;
    protected int processTime;

    protected final IIntArray fields = new IIntArray()
    {
        @Override
        public int get(int index)
        {
            switch (index)
            {
                case 0:
                    // Energy lower bytes
                    return AbstractProcessMachineTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (AbstractProcessMachineTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return AbstractProcessMachineTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (AbstractProcessMachineTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return AbstractProcessMachineTileEntity.this.redstoneMode.ordinal();
                case 5:
                    return (int) AbstractProcessMachineTileEntity.this.progress;
                case 6:
                    return AbstractProcessMachineTileEntity.this.processTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value)
        {
            switch (index)
            {
                case 4:
                    AbstractProcessMachineTileEntity.this.redstoneMode = RedstoneMode.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
                case 5:
                    AbstractProcessMachineTileEntity.this.progress = value;
                    break;
                case 6:
                    AbstractProcessMachineTileEntity.this.processTime = value;
                    break;
            }
        }

        @Override
        public int size()
        {
            return FIELDS_COUNT;
        }
    };

    public AbstractProcessMachineTileEntity(TileEntityType<?> type, int inventorySize, int maxEnergy, int maxReceive, int maxExtract)
    {
        super(type, inventorySize, maxEnergy, maxReceive, maxExtract);
    }

    protected abstract int getEnergyConsumedPerTick();

    @Override
    protected BlockState getActiveState()
    {
        return super.getActiveState().with(AbstractFurnaceBlock.LIT, true);
    }

    @Override
    protected BlockState getInactiveState()
    {
        return super.getInactiveState().with(AbstractFurnaceBlock.LIT, false);
    }

    protected abstract int[] outputSlots();

    @Nullable
    protected abstract R getRecipe();

    protected abstract int getProcessTime(R recipe);

    protected abstract Collection<ItemStack> getProcessedResults(R recipe);

    protected Collection<ItemStack> getPossibleProcessedResult(R recipe)
    {
        return this.getProcessedResults(recipe);
    }

    public abstract int getInputSlotCount();

    @Override
    public void tick()
    {
        super.tick();

        R recipe = this.getRecipe();
        if (recipe != null && this.canMachineRun(recipe))
        {
            this.processTime = this.getProcessTime(recipe);
            this.progress++; // TODO: Change for upgrades
            this.energy.consumeEnergy(this.getEnergyConsumedPerTick()); // TODO: Change for upgrades

            if (this.progress >= this.processTime)
            {
                this.getProcessedResults(recipe).forEach(this::outputResultItem);
                this.consumeIngredients(recipe);
                this.progress = 0;

                if (this.getRecipe() == null)
                    this.sendUpdate(this.getInactiveState(), false);
            } else
                this.sendUpdate(this.getActiveState(), false);
        } else
        {
            if (recipe == null)
                this.progress = 0;

            this.sendUpdate(this.getInactiveState(), false);
        }
    }

    private boolean canMachineRun(R recipe)
    {
        return this.canRun() && this.getEnergyStored() >= this.getEnergyConsumedPerTick() && this.canOutput(this.getPossibleProcessedResult(recipe));
    }

    protected boolean canOutput(Iterable<ItemStack> stacks)
    {
        for (ItemStack stack : stacks)
            if (!this.canOutputItemStack(stack))
                return false;
        return true;
    }

    private boolean canOutputItemStack(ItemStack stack)
    {
        for (int i : this.outputSlots())
        {
            ItemStack output = this.getStackInSlot(i);
            if (this.canStacksStack(stack, output))
                return true;
        }
        return false;
    }

    private boolean canStacksStack(ItemStack stackA, ItemStack stackB)
    {
        return (stackA.isEmpty() || stackB.isEmpty()) || (ItemHandlerHelper.canItemStacksStack(stackA, stackB) && stackA.getCount() + stackB.getCount() <= stackA.getMaxStackSize());
    }

    protected void outputResultItem(ItemStack stack)
    {
        for (int i : this.outputSlots())
        {
            ItemStack output = this.getStackInSlot(i);
            if (this.canStacksStack(stack, output))
            {
                if (output.isEmpty())
                    this.setInventorySlotContents(i, stack);
                else
                    output.grow(stack.getCount());
                return;
            }
        }
    }

    protected void consumeIngredients(R recipe)
    {
        this.decrStackSize(0, 1);
    }

    @Override
    public IIntArray getFields()
    {
        return this.fields;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.progress = nbt.getInt("Progress");
        this.processTime = nbt.getInt("ProcessTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("Progress", (int) this.progress);
        nbt.putInt("ProcessTime", this.processTime);
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        super.onDataPacket(net, packet);
        CompoundNBT nbt = packet.getNbtCompound();
        this.progress = nbt.getInt("Progress");
        this.processTime = nbt.getInt("ProcessTime");
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putInt("Progress", (int) this.progress);
        nbt.putInt("ProcessTime", this.processTime);
        return nbt;
    }
}

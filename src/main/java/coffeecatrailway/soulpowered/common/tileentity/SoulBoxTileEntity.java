package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.common.block.SoulBoxBlock;
import coffeecatrailway.soulpowered.common.inventory.container.SoulBoxContainer;
import coffeecatrailway.soulpowered.registry.SoulTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

/**
 * @author CoffeeCatRailway
 * Created: 14/11/2020
 */
public class SoulBoxTileEntity extends AbstractMachineTileEntity
{
    public static final int MAX_ENERGY = 500_000;
    public static final int MAX_RECEIVE = 500;
    public static final int MAX_SEND = 500;

    public static final int INVENTORY_SIZE = 2;
    private static final int[] SLOTS = IntStream.range(0, INVENTORY_SIZE).toArray();

    public SoulBoxTileEntity()
    {
        this(SoulTileEntities.SOUL_BOX.get());
    }

    public SoulBoxTileEntity(TileEntityType<? extends SoulBoxTileEntity> type)
    {
        super(type, INVENTORY_SIZE, MAX_ENERGY, MAX_RECEIVE, MAX_SEND);
    }

    @Override
    public void tick()
    {
        super.tick();
        if (this.world == null || this.world.isRemote) return;

        boolean hasEnergy = this.energy.getEnergyStored() > 0;
        if (this.world.getGameTime() % 30 == 0)
        {
            BlockState currentState = this.getBlockState();
            BlockState newState = this.getBlockState().with(SoulBoxBlock.ON, hasEnergy);
            if (currentState != newState)
                this.world.setBlockState(this.pos, newState, Constants.BlockFlags.DEFAULT);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction)
    {
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent(); // TODO: Add powered items
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent(); // TODO: Add powered items
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory)
    {
        return new SoulBoxContainer(id, playerInventory, this, this.getFields());
    }
}

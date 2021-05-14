package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.common.block.AbstractMachineBlock;
import coffeecatrailway.soulpowered.common.inventory.container.SoulBoxContainer;
import coffeecatrailway.soulpowered.registry.SoulTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * @author CoffeeCatRailway
 * Created: 14/11/2020
 */
public class SoulBoxTileEntity extends AbstractMachineTileEntity
{
    public static final int MAX_ENERGY = 600_000;
    public static final Function<Tier, Integer> MAX_TRANSFER = tier -> tier.calculateEnergyTransfer(1000);

    public static final int INVENTORY_SIZE = 2;
    private static final int[] SLOTS = IntStream.range(0, INVENTORY_SIZE).toArray();

    private final Tier tier;

    public SoulBoxTileEntity(Tier tier)
    {
        this(SoulTileEntities.SOUL_BOX.get(tier).get(), tier);
    }

    public SoulBoxTileEntity(TileEntityType<? extends SoulBoxTileEntity> type, Tier tier)
    {
        super(type, INVENTORY_SIZE, (int) tier.calculateEnergyCapacity(MAX_ENERGY), MAX_TRANSFER.apply(tier), MAX_TRANSFER.apply(tier));
        this.tier = tier;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (this.level == null || this.level.isClientSide()) return;

        if (this.energy.canReceive())
        {
            ItemStack inStack = this.getItem(0);
            if (EnergyUtils.isPresent(inStack))
            {
                IEnergyStorage energyIn = EnergyUtils.get(inStack).orElse(EnergyUtils.EMPTY);
                if (energyIn.getEnergyStored() > 0)
                {
                    int toSend = energyIn.extractEnergy(MAX_TRANSFER.apply(this.tier), true);
                    int sent = this.energy.receiveEnergy(toSend, false);
                    if (sent > 0)
                        energyIn.extractEnergy(sent, false);
                }
            }
        }

        if (this.energy.canExtract())
        {
            ItemStack outStack = this.getItem(1);
            if (EnergyUtils.isPresent(outStack))
            {
                if (this.energy.getEnergyStored() > 0)
                {
                    int toSend = this.energy.extractEnergy(MAX_TRANSFER.apply(this.tier), true);
                    int sent = EnergyUtils.get(outStack).orElse(EnergyUtils.EMPTY).receiveEnergy(toSend, false);
                    if (sent > 0)
                        this.energy.extractEnergy(sent, false);
                }
            }
        }

        if (this.level.getGameTime() % 30 == 0)
        {
            BlockState currentState = this.getBlockState();
            BlockState newState = this.getBlockState().setValue(AbstractMachineBlock.LIT, this.energy.getEnergyStored() > 0);
            if (currentState != newState)
                this.level.setBlock(this.getBlockPos(), newState, Constants.BlockFlags.DEFAULT);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return EnergyUtils.isPresent(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return EnergyUtils.isPresent(stack);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory)
    {
        return new SoulBoxContainer(id, playerInventory, this, this.getFields(), this.tier);
    }
}

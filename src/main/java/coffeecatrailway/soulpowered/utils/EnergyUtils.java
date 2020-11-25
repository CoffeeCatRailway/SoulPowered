package coffeecatrailway.soulpowered.utils;

import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageImplBase;
import coffeecatrailway.soulpowered.common.tileentity.IEnergyHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public final class EnergyUtils
{
    public static final SoulEnergyStorageImplBase EMPTY = new SoulEnergyStorageImplBase(0);

    private EnergyUtils()
    {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Original: https://github.com/SilentChaos512/Silents-Mechanisms
     */
    public static void trySendToNeighbors(IBlockReader world, BlockPos pos, IEnergyHandler energyHandler, int maxSend)
    {
        for (Direction side : Direction.values())
        {
            if (energyHandler.getEnergyStored() == 0) return;
            TileEntity tileEntity = world.getTileEntity(pos.offset(side));
            if (tileEntity != null)
            {
                IEnergyStorage energy = energyHandler.getEnergy(side).orElse(new EnergyStorage(0));
                tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).ifPresent(other -> {
                    if (other.canReceive())
                    {
                        int toSend = energy.extractEnergy(maxSend, true);
                        int sent = other.receiveEnergy(toSend, false);
                        if (sent > 0)
                            energy.extractEnergy(sent, false);
                    }
                });
            }
        }
    }

    public static LazyOptional<IEnergyStorage> get(ItemStack stack)
    {
        return !stack.isEmpty() ? stack.getCapability(CapabilityEnergy.ENERGY, null) : LazyOptional.empty();
    }

    public static boolean isPresent(ItemStack stack)
    {
        return get(stack).isPresent();
    }

    public static void ifPresent(ItemStack stack, NonNullConsumer<? super IEnergyStorage> consumer)
    {
        get(stack).ifPresent(consumer);
    }
}

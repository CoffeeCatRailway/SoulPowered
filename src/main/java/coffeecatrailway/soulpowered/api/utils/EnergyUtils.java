package coffeecatrailway.soulpowered.api.utils;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageImplBase;
import coffeecatrailway.soulpowered.common.tileentity.IEnergyHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public final class EnergyUtils
{
    public static final SoulEnergyStorageImplBase EMPTY = new SoulEnergyStorageImplBase(0);

    public static final ResourceLocation ENERGY_BAR_TEXTURE = SoulPoweredMod.getLocation("textures/gui/container/energy_bar.png");

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

    public static LazyOptional<IEnergyStorage> getIfPresent(ItemStack stack)
    {
        return isPresent(stack) ? get(stack) : LazyOptional.empty();
    }

    public static boolean isPresent(ItemStack stack)
    {
        return get(stack).isPresent();
    }

    public static void ifPresent(ItemStack stack, NonNullConsumer<? super IEnergyStorage> consumer)
    {
        get(stack).ifPresent(consumer);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderThinEnergyBar(MatrixStack matrixStack, int x, int y, int energyBarHeight)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(ENERGY_BAR_TEXTURE);
        AbstractGui.blit(matrixStack, x, y - 50, 0, 0, 14, 52, 84, 52);
        if (energyBarHeight > 0)
            AbstractGui.blit(matrixStack, x , y + 2 - energyBarHeight, 14, 52 - energyBarHeight, 14, energyBarHeight, 84, 52);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderWideEnergyBar(MatrixStack matrixStack, int x, int y, int energyBarHeight)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(ENERGY_BAR_TEXTURE);
        AbstractGui.blit(matrixStack, x, y - 50, 28, 0, 28, 52, 84, 52);
        if (energyBarHeight > 0)
            AbstractGui.blit(matrixStack, x, y + 2 - energyBarHeight, 56, 52 - energyBarHeight, 28, energyBarHeight, 84, 52);
    }
}

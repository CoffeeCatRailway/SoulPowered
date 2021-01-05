package coffeecatrailway.soulpowered.api.item;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.api.RedstoneMode;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.registry.SoulTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 22/12/2020
 */
public class MachineBlockItem extends BlockItem implements IEnergyItem
{
    public static final int TRANSFER = 1000;

    public MachineBlockItem(Block block, Properties properties)
    {
        super(block, properties);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        super.fillItemGroup(group, items);
        if (this.isInGroup(group))
            this.addItemVarients(items);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        IEnergyItem.super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public boolean updateItemStackNBT(CompoundNBT nbt)
    {
        super.updateItemStackNBT(nbt);
        if (nbt.contains("Energy", Constants.NBT.TAG_ANY_NUMERIC))
        {
        }
        return false;
    }

    @Override
    public int getMaxEnergy()
    {
        if (CapabilityEnergy.ENERGY != null && this.getTileEntity() != null && this.getTileEntity().getCapability(CapabilityEnergy.ENERGY).isPresent())
        {
            IEnergyStorage storage = this.getTileEntity().getCapability(CapabilityEnergy.ENERGY).orElse(EnergyUtils.EMPTY);
            return storage.getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    public int getMaxReceive()
    {
        return TRANSFER;
    }

    @Override
    public int getMaxExtract()
    {
        return TRANSFER;
    }

    @Nullable
    private TileEntity getTileEntity()
    {
        if (!this.getBlock().hasTileEntity(this.getBlock().getDefaultState()))
            return null;
        return this.getBlock().createTileEntity(this.getBlock().getDefaultState(), null);
    }
}

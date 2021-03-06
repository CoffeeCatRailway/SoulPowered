package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.api.item.IEnergyItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 25/11/2020
 */
public class SoulAmuletPoweredItem extends SoulAmuletItem implements IEnergyItem
{
    public static final ResourceLocation TEXTURE = SoulMod.getLocation("textures/models/powered_soulium_soul_amulet.png");
    public static final int CAPACITY = 10_000;
    public static final int TRANSFER = 5000;

    public SoulAmuletPoweredItem(Properties properties, float range, float soulGatheringChance)
    {
        super(properties, 0, range, soulGatheringChance, TEXTURE);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new ICapabilityProvider()
        {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
            {
                if (cap == CapabilityEnergy.ENERGY)
                    return SoulAmuletPoweredItem.this.getCapability(stack, cap, side);
                return SoulAmuletPoweredItem.super.initCapabilities(stack, nbt).getCapability(cap, side);
            }
        };
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items)
    {
        super.fillItemCategory(group, items);
        if (this.allowdedIn(group))
            this.addItemVarients(items);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.appendHoverText(stack, world, tooltip, flag);
        IEnergyItem.super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return IEnergyItem.super.hasEnergy(stack, 0);
    }

    @Override
    public int getMaxEnergy()
    {
        return CAPACITY;
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
}

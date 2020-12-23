package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.item.IEnergyItem;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.utils.EnergyUtils;
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
import top.theillusivec4.curios.api.CuriosCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 25/11/2020
 */
public class SoulAmuletPoweredItem extends SoulAmuletItem implements IEnergyItem
{
    public static final ResourceLocation TEXTURE = SoulPoweredMod.getLocation("textures/models/powered_soulium_soul_amulet.png");
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
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        IEnergyItem.super.fillItemGroup(group, items);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        IEnergyItem.super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return EnergyUtils.isPresent(stack) && EnergyUtils.get(stack).orElse(EnergyUtils.EMPTY).getEnergyStored() > 0;
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

package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
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
public class SoulAmuletPoweredItem extends EnergyItem implements ISoulCurios, ISoulAmulet
{
    private final float range;
    private final float soulGatheringChance;

    public SoulAmuletPoweredItem(Properties properties, float range, float soulGatheringChance)
    {
        super(properties, 10_000, 5000);
        this.range = range;
        this.soulGatheringChance = soulGatheringChance;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity)
    {
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        ISoulAmulet.super.initCapabilities(stack, nbt);
        return new ICapabilityProvider()
        {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
            {
                if (cap == CapabilityEnergy.ENERGY)
                    return SoulAmuletPoweredItem.super.initCapabilities(stack, nbt).getCapability(cap, side);
                if (cap == CuriosCapability.ITEM)
                    return CuriosIntegration.getCapability(stack).getCapability(cap, side);
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> info, ITooltipFlag flag)
    {
        super.addInformation(stack, world, info, flag);
        ISoulAmulet.super.addInformation(stack, world, info, flag);
    }

    @Override
    public float getRange()
    {
        return this.range;
    }

    @Override
    public float getSoulGatheringChance()
    {
        return this.soulGatheringChance;
    }
}

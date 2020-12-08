package coffeecatrailway.soulpowered.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 8/11/2020
 */
public class SoulAmuletItem extends Item implements ISoulAmulet
{
    private final float range;
    private final float soulGatheringChance;

    public SoulAmuletItem(Properties properties, float range, float soulGatheringChance)
    {
        super(properties);
        this.range = range;
        this.soulGatheringChance = soulGatheringChance;
    }

    @Override
    public void curioTick(ItemStack stack, String identifier, int index, LivingEntity livingEntity)
    {
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return ISoulAmulet.super.initCapabilities(stack, nbt);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> info, ITooltipFlag flag)
    {
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

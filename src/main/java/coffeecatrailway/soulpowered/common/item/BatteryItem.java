package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.api.item.IEnergyItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 21/12/2020
 */
public class BatteryItem extends Item implements IEnergyItem
{
    public static final int CAPACITY = 200_000;
    public static final int TRANSFER = 10_000;

    private final Tier tier;

    public BatteryItem(Properties properties, Tier tier)
    {
        super(properties.maxStackSize(1).rarity(Rarity.UNCOMMON));
        this.tier = tier;
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
    public int getMaxEnergy()
    {
        return (int) (CAPACITY * this.tier.getEnergyCapacity());
    }

    @Override
    public int getMaxReceive()
    {
        return (int) (TRANSFER * this.tier.getEnergyTransfer());
    }

    @Override
    public int getMaxExtract()
    {
        return (int) (TRANSFER * this.tier.getEnergyTransfer());
    }
}

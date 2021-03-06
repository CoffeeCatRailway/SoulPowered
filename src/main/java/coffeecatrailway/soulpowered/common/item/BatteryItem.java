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
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
        this.tier = tier;
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
    public int getMaxEnergy()
    {
        return this.tier.calculateEnergyCapacity(CAPACITY);
    }

    @Override
    public int getMaxReceive()
    {
        return this.tier.calculateEnergyTransfer(TRANSFER);
    }

    @Override
    public int getMaxExtract()
    {
        return this.tier.calculateEnergyTransfer(TRANSFER);
    }
}

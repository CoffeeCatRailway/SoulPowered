package coffeecatrailway.soulpowered.utils;

import coffeecatrailway.soulpowered.common.item.ISoulFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public final class BurnTimeUtils
{
    private BurnTimeUtils() {throw new IllegalAccessError("Utility class");}

    public static int getBurnTime(ItemStack stack)
    {
        if (stack.getItem() instanceof ISoulFuel)
            return ((ISoulFuel) stack.getItem()).getBurnTime();
        return ForgeHooks.getBurnTime(stack);
    }
}

package coffeecatrailway.soulpowered.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public interface ISoulFuel
{
    static int getBurnTime(ItemStack stack)
    {
        if (stack.getItem() instanceof ISoulFuel)
            return ((ISoulFuel) stack.getItem()).getBurnTime();
        return ForgeHooks.getBurnTime(stack);
    }

    int getBurnTime();
}

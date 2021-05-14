package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.api.item.ISoulFuel;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * @author CoffeeCatRailway
 * Created: 10/11/2020
 */
public class SoulBottleItem extends Item implements ISoulFuel
{
    public SoulBottleItem(Properties properties)
    {
        super(properties.stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (!CuriosIntegration.hasCurio(player) && !SoulsCapability.isPresent(player))
            return ActionResult.fail(stack);

        if (SoulsCapability.get(player).orElse(SoulsCapability.EMPTY).addSouls(2, false) && !player.isCreative())
        {
            player.addItem(new ItemStack(Items.GLASS_BOTTLE));
            stack.shrink(1);
        }
        return ActionResult.consume(stack);
    }

    @Override
    public int getBurnTime()
    {
        return 900;
    }
}

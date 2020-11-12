package coffeecatrailway.soulpowered.common.item;

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
public class SoulBottleItem extends Item
{
    public SoulBottleItem(Properties properties)
    {
        super(properties.maxStackSize(16));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        boolean creativeFlag = !player.isCreative();

        if (CuriosIntegration.hasCurio(player, "necklace").isEmpty() && creativeFlag)
            return ActionResult.resultFail(stack);

        player.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler -> {
            if (handler.addSouls(2, false) && creativeFlag)
            {
                stack.shrink(1);
                player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        });
        return ActionResult.resultConsume(stack);
    }
}

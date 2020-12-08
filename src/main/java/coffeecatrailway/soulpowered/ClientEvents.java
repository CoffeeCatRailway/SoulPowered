package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.ActivateCurioMessage;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.Optional;

/**
 * @author CoffeeCatRailway
 * Created: 3/12/2020
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = SoulPoweredMod.MOD_ID)
public class ClientEvents
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void keyPressed(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && SoulPoweredMod.ACTIVATE_CURIO.isPressed())
        {
            ClientPlayerEntity player = minecraft.player;
            if (!CuriosIntegration.hasCurio(player))
                return;

            if (CuriosApi.getSlotHelper().getSlotType("charm").isPresent())
            {
                Optional<ISlotType> optional = CuriosApi.getSlotHelper().getSlotType("charm");
                optional.ifPresent(handler -> {
                    if (handler.getSize() == 0)
                        return;
                    for (int slotIndex = 0; slotIndex < handler.getSize(); slotIndex++)
                    {
                        ItemStack artifact = CuriosIntegration.getCurioStack(player, "charm", slotIndex);
                        if (!artifact.isEmpty())
                            SoulMessageHandler.PLAY.sendToServer(new ActivateCurioMessage(slotIndex));
                    }
                });
            }
        }
    }
}

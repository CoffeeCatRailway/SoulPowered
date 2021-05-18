package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.client.entity.SoulShieldRenderer;
import coffeecatrailway.soulpowered.client.gui.SoulHUDOverlayHandler;
import coffeecatrailway.soulpowered.client.gui.screen.SoulGeneratorScreen;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.ActivateCurioMessage;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.registry.SoulContainers;
import coffeecatrailway.soulpowered.registry.SoulEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 3/12/2020
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = SoulMod.MOD_ID)
public class ClientEvents
{
    public static final ResourceLocation POWERED_ITEM_PROPERTY = SoulMod.getLocation("powered");
    public static final Set<Supplier<Item>> POWERED_ITEM_PROPERTY_SET = new HashSet<>();

    @SuppressWarnings("unchecked")
    public static void init(FMLClientSetupEvent event)
    {
        POWERED_ITEM_PROPERTY_SET.stream().map(Supplier::get).forEach(item -> ItemModelsProperties.register(item, POWERED_ITEM_PROPERTY, (stack, world, entity) ->
                EnergyUtils.getIfPresent(stack).orElse(EnergyUtils.EMPTY).getEnergyStored() > 0 ? 1f : 0f));
        SoulHUDOverlayHandler.init();
        ClientRegistry.registerKeyBinding(SoulMod.ACTIVATE_CURIO);
        RenderingRegistry.registerEntityRenderingHandler(SoulEntities.SOUL_SHIELD.get(), SoulShieldRenderer::new);
        ScreenManager.register(SoulContainers.SOUL_GENERATOR.get(), SoulGeneratorScreen::new);
        SoulContainers.SCREENS.forEach((typeSupplier, screenFactory) -> ScreenManager.register(typeSupplier.get(), screenFactory));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void keyPressed(InputEvent.KeyInputEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && SoulMod.ACTIVATE_CURIO.consumeClick())
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

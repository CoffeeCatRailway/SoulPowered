package coffeecatrailway.soulpowered.client;

import coffeecatrailway.soulpowered.SoulMod;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SoulMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoulPoweredAtlases
{
    public static final ResourceLocation EMPTY_SLOT_PLUS = SoulMod.getLocation("item/empty_power_slot_plus");
    public static final ResourceLocation EMPTY_SLOT_MINUS = SoulMod.getLocation("item/empty_power_slot_minus");

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event)
    {
        if (!event.getMap().location().equals(PlayerContainer.BLOCK_ATLAS))
            return;
        event.addSprite(EMPTY_SLOT_PLUS);
        event.addSprite(EMPTY_SLOT_MINUS);
    }
}

package coffeecatrailway.soulpowered.client;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.inventory.container.SoulBoxContainer;
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
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SoulPoweredMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoulPoweredAtlases
{
    private static final Set<RenderMaterial> MATERIALS = new HashSet<>();

    public static final RenderMaterial EMPTY_SLOT_PLUS_MATERIAL = register(PlayerContainer.LOCATION_BLOCKS_TEXTURE, SoulPoweredMod.getLocation("item/empty_power_slot_plus"));
    public static final RenderMaterial EMPTY_SLOT_MINUS_MATERIAL = register(PlayerContainer.LOCATION_BLOCKS_TEXTURE, SoulPoweredMod.getLocation("item/empty_power_slot_minus"));

    private static RenderMaterial register(ResourceLocation atlasLocation, ResourceLocation textureLocation)
    {
        RenderMaterial material = new RenderMaterial(atlasLocation, textureLocation);
        MATERIALS.add(material);
        return material;
    }

    @SubscribeEvent
    public static void onEvent(TextureStitchEvent.Pre event)
    {
        AtlasTexture texture = event.getMap();
        for (RenderMaterial material : MATERIALS)
        {
            if (texture.getTextureLocation().equals(material.getAtlasLocation()))
                event.addSprite(material.getTextureLocation());
        }
    }
}

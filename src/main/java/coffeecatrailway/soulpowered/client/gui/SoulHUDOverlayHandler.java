package coffeecatrailway.soulpowered.client.gui;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
@OnlyIn(Dist.CLIENT)
public class SoulHUDOverlayHandler
{
    private int soulsIconsOffset;
    private int playerSouls;

    private static final ResourceLocation ICONS_TEX = SoulPoweredMod.getLocation("textures/gui/icons.png");

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new SoulHUDOverlayHandler());
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreOverlayRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD)
            return;

        this.soulsIconsOffset = ForgeIngameGui.right_height;

        if (event.isCanceled())
            return;

        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;

        if (!CuriosIntegration.hasCurio(player))
            return;

        player.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler -> {
            this.playerSouls = handler.getSouls();

            int left = mc.getMainWindow().getScaledWidth() / 2 + 91;
            int top = mc.getMainWindow().getScaledHeight() - this.soulsIconsOffset;

            mc.getTextureManager().bindTexture(ICONS_TEX);
            RenderSystem.enableBlend();

            for (int i = 0; i < 10; i++) {
                int x = left - i * 8 - 9;
                int y = top;
                if (player.getAir() < player.getMaxAir())
                    y -= 9;

                AbstractGui.blit(matrixStack, x, y, 18, 0, 9, 9, 32, 32);

                if (this.playerSouls > 0) {
                    int idx = i * 2 + 1;

                    if (idx < this.playerSouls)
                        AbstractGui.blit(matrixStack, x, y, 0, 0, 9, 9, 32, 32);
                    else if (idx == this.playerSouls)
                        AbstractGui.blit(matrixStack, x, y, 9, 0, 9, 9, 32, 32);
                }
            }
            RenderSystem.disableBlend();
            mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        });
    }
}

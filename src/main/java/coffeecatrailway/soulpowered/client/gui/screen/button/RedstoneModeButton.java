package coffeecatrailway.soulpowered.client.gui.screen.button;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.RedstoneMode;
import coffeecatrailway.soulpowered.common.inventory.container.AbstractEnergyStorageContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public class RedstoneModeButton extends Button
{
    public static final ResourceLocation REDSTONE_MODE = SoulPoweredMod.getLocation("textures/gui/container/redstone_mode.png");

    private final AbstractEnergyStorageContainer<?> container;

    public RedstoneModeButton(AbstractEnergyStorageContainer<?> container, int x, int y, int width, int height, IPressable onPress)
    {
        super(x, y, width, height, new StringTextComponent(""), button -> {
            ((RedstoneModeButton) button).cycleMode();
            onPress.onPress(button);
        });
        this.container = container;
    }

    public RedstoneMode getMode()
    {
        return this.container.getRedstoneMode();
    }

    private void cycleMode()
    {
        int ordinal = this.container.getRedstoneMode().ordinal() + 1;
        if (ordinal >= RedstoneMode.values().length)
            ordinal = 0;
        this.container.setRedstoneMode(RedstoneMode.byOrdinal(ordinal, RedstoneMode.IGNORED));
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().getTexture(REDSTONE_MODE);
        GlStateManager._disableDepthTest();

        blit(matrixStack, this.x, this.y, this.width, this.height, 0, 0, 26, 26, 48, 48);
        int offsetX = 6;
        int offsetY = 4;
        switch (this.container.getRedstoneMode())
        {
            case IGNORED:
                blit(matrixStack, this.x + offsetX, this.y + offsetY, 16, 16, 32, 0, 16, 16, 48, 48);
                break;
            case ON:
                blit(matrixStack, this.x + offsetX, this.y + offsetY, 16, 16, 32, 32, 16, 16, 48, 48);
                break;
            case OFF:
                blit(matrixStack, this.x + offsetX, this.y + offsetY, 16, 16, 32, 16, 16, 16, 48, 48);
                break;
        }

        GlStateManager._enableDepthTest();
    }
}

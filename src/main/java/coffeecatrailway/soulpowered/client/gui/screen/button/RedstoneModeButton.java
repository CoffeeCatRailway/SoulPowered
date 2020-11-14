package coffeecatrailway.soulpowered.client.gui.screen.button;

import coffeecatrailway.soulpowered.common.inventory.container.AbstractEnergyStorageContainer;
import coffeecatrailway.soulpowered.utils.RedstoneMode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public class RedstoneModeButton extends Button
{
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
        minecraft.getTextureManager().bindTexture(this.container.getRedstoneMode().getTexture());
        GlStateManager.disableDepthTest();

        blit(matrixStack, this.x, this.y, 0, 0, this.width, this.height, 16, 16);
        GlStateManager.enableDepthTest();
    }
}

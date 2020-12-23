package coffeecatrailway.soulpowered.client.gui.screen;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.api.RedstoneMode;
import coffeecatrailway.soulpowered.client.gui.screen.button.RedstoneModeButton;
import coffeecatrailway.soulpowered.common.inventory.container.AbstractEnergyStorageContainer;
import coffeecatrailway.soulpowered.network.SetRedstoneModeMessage;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public abstract class AbstractMachineScreen<C extends AbstractEnergyStorageContainer<?>> extends ContainerScreen<C>
{
    public AbstractMachineScreen(C container, PlayerInventory inv, ITextComponent title)
    {
        super(container, inv, title);
    }

    public abstract ResourceLocation getGuiTexture();

    @Override
    protected void init()
    {
        super.init();
        this.addButton(new RedstoneModeButton(this.container, this.guiLeft - 22, this.guiTop, 26, 26, button -> {
            RedstoneMode mode = ((RedstoneModeButton) button).getMode();
            SoulMessageHandler.PLAY.sendToServer(new SetRedstoneModeMessage(mode));
        }));
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y)
    {
        if (this.isPointInRegion(153, 17, 13, 51, x, y))
        {
            IFormattableTextComponent text = SoulData.Lang.energyWithMax(this.container.getEnergyStored(), this.container.getMaxEnergyStored());
            this.renderTooltip(matrixStack, text, x, y);
        }
        super.renderHoveredTooltip(matrixStack, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        if (this.minecraft == null) return;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(getGuiTexture());
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        blit(matrixStack, xPos, yPos, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y)
    {
        this.font.drawString(matrixStack, this.title.getString(), 8.0F, 6.0F, 4210752);
//        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);

        for (Widget widget : this.buttons)
        {
            if (widget.isHovered() && widget instanceof RedstoneModeButton)
            {
                RedstoneMode mode = ((RedstoneModeButton) widget).getMode();
                renderTooltip(matrixStack, SoulData.Lang.redstoneMode(mode), x - this.guiLeft, y - this.guiTop);
            }
        }
    }
}

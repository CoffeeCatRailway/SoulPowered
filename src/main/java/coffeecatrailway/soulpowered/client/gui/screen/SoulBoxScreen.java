package coffeecatrailway.soulpowered.client.gui.screen;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.inventory.container.SoulBoxContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.util.TextUtil;

/**
 * @author CoffeeCatRailway
 * Created: 14/11/2020
 */
public class SoulBoxScreen extends AbstractMachineScreen<SoulBoxContainer>
{
    public static final ResourceLocation TEXTURE = SoulPoweredMod.getLocation("textures/gui/container/soul_box.png");

    public SoulBoxScreen(SoulBoxContainer container, PlayerInventory inv, ITextComponent title)
    {
        super(container, inv, title);
    }

    @Override
    public ResourceLocation getGuiTexture()
    {
        return TEXTURE;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y)
    {
        if (isPointInRegion(74, 17, 27, 51, x, y))
        {
            ITextComponent text = TextUtil.energyWithMax(this.container.getEnergyStored(), this.container.getTileEntity().getMaxEnergyStored());
            renderTooltip(matrixStack, text, x, y);
        }
        super.renderHoveredTooltip(matrixStack, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);

        if (this.minecraft == null) return;
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;

        // Energy meter
        int energyBarHeight = this.container.getEnergyBarHeight();
        if (energyBarHeight > 0)
            blit(matrixStack, xPos + 75, yPos + 68 - energyBarHeight, 176, 50 - energyBarHeight, 26, energyBarHeight);
    }
}
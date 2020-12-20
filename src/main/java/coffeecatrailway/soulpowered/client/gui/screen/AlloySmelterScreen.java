package coffeecatrailway.soulpowered.client.gui.screen;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.inventory.container.AlloySmelterContainer;
import coffeecatrailway.soulpowered.utils.EnergyUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author CoffeeCatRailway
 * Created: 15/12/2020
 */
public class AlloySmelterScreen extends AbstractMachineScreen<AlloySmelterContainer>
{
    public static final ResourceLocation TEXTURE = SoulPoweredMod.getLocation("textures/gui/container/alloy_smelter.png");

    public AlloySmelterScreen(AlloySmelterContainer container, PlayerInventory inv, ITextComponent title)
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
        if (isPointInRegion(153, 17, 13, 51, x, y))
        {
            ITextComponent text = SoulData.Lang.energyWithMax(this.container.getEnergyStored(), this.container.getTileEntity().getMaxEnergyStored());
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

        // Progress remaining
        if (this.container.isCrafting())
        {
            int height = getProgressBarIconHeight();
            blit(matrixStack, xPos + 65, yPos + 31 + 20 - height, 176, 20 - height, 46, height + 1);
        }

        // Energy meter
        EnergyUtils.renderThinEnergyBar(matrixStack, xPos + 153, yPos + 67, this.container.getEnergyBarHeight());
    }

    private int getProgressBarIconHeight()
    {
        if (this.container.getTotalProgressTime() == 0 || this.container.getProgressTime() == 0) return 0;
        return this.container.getProgressTime() * 20 / this.container.getTotalProgressTime();
    }
}

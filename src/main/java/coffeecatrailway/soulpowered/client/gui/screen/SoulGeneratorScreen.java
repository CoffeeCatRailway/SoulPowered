package coffeecatrailway.soulpowered.client.gui.screen;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.util.TextUtil;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public class SoulGeneratorScreen extends AbstractMachineScreen<SoulGeneratorContainer>
{
    public static final ResourceLocation TEXTURE = SoulPoweredMod.getLocation("textures/gui/container/soul_generator.png");

    public SoulGeneratorScreen(SoulGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent title)
    {
        super(screenContainer, inv, title);
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

        // Fuel remaining
        if (this.container.isBurning())
        {
            int height = getFlameIconHeight();
            blit(matrixStack, xPos + 81, yPos + 53 + 12 - height, 176, 12 - height, 14, height + 1);
        }

        // Energy meter
        int energyBarHeight = this.container.getEnergyBarHeight();
        if (energyBarHeight > 0)
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 81 - energyBarHeight, 12, energyBarHeight);
    }

    private int getFlameIconHeight()
    {
        int total = this.container.getTotalBurnTime();
        if (total == 0) total = 200;
        return this.container.getBurnTime() * 13 / total;
    }
}

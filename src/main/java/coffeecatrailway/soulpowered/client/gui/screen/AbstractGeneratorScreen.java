package coffeecatrailway.soulpowered.client.gui.screen;

import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.common.inventory.container.AbstractGeneratorContainer;
import coffeecatrailway.soulpowered.data.gen.SoulLanguage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/**
 * @author CoffeeCatRailway
 * Created: 8/01/2021
 */
public abstract class AbstractGeneratorScreen<C extends AbstractGeneratorContainer<?>> extends AbstractMachineScreen<C>
{
    public AbstractGeneratorScreen(C container, PlayerInventory inv, ITextComponent title)
    {
        super(container, inv, title);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int x, int y)
    {
        if (this.isHovering(153, 17, 13, 51, x, y))
            renderTooltip(matrixStack, SoulLanguage.energyWithMax(this.menu.getEnergyStored(), this.menu.getBlockEntity().getMaxEnergyStored()), x, y);
        super.renderTooltip(matrixStack, x, y);
    }



    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        super.renderBg(matrixStack, partialTicks, x, y);

        if (this.minecraft == null) return;
        int xPos = (this.width - this.getXSize()) / 2;
        int yPos = (this.height - this.getYSize()) / 2;

        // Fuel remaining
        if (this.menu.isBurning())
        {
            int height = getFlameIconHeight();
            blit(matrixStack, xPos + 81, yPos + 53 + 12 - height, 176, 12 - height, 14, height + 1);
        }

        // Energy meter
        EnergyUtils.renderThinEnergyBar(matrixStack, xPos + 153, yPos + 67, this.menu.getEnergyStored(), this.menu.getMaxEnergyStored());
    }

    private int getFlameIconHeight()
    {
        int total = this.menu.getTotalBurnTime();
        if (total == 0) total = 200;
        return this.menu.getBurnTime() * 13 / total;
    }
}

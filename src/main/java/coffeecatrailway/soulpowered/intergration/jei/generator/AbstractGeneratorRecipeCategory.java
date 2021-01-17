package coffeecatrailway.soulpowered.intergration.jei.generator;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 17/01/2021
 */
public abstract class AbstractGeneratorRecipeCategory<R extends AbstractGeneratorRecipe> implements IRecipeCategory<R>
{
    public static final ResourceLocation TEXTURE = SoulPoweredMod.getLocation("textures/gui/jei_generator.png");

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final String localizedName;
    private final int maxEnergy;

    public AbstractGeneratorRecipeCategory(IGuiHelper guiHelper, ItemStack icon, String unlocalizedName, int maxEnergy)
    {
        this.background = guiHelper.createDrawable(AbstractGeneratorRecipeCategory.TEXTURE, 0, 0, 92, 56); // TODO: Fix background not rendering
        this.icon = guiHelper.createDrawableIngredient(icon);
        this.localizedName = I18n.format(unlocalizedName);
        this.maxEnergy = maxEnergy;
    }

    protected abstract Tier getTier();

    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public IDrawable getIcon()
    {
        return this.icon;
    }

    @Override
    public void draw(R recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        IDrawableAnimated flame = recipe.getFlame();
        flame.draw(matrixStack, 3, 37);
        Minecraft.getInstance().fontRenderer.func_243248_b(matrixStack, recipe.getBurnTime(), 24f, 13f, -8355712);

        EnergyUtils.renderThinEnergyBar(matrixStack, 76, 52, recipe.getRfPerTick(this.getTier()), this.maxEnergy); // TODO: Add tiered energy tile configs
    }

    @Override
    public List<ITextComponent> getTooltipStrings(R recipe, double mouseX, double mouseY)
    {
        List<ITextComponent> toolTips = new ArrayList<>();
        if (this.isPointInRegion(76, 2, 14, 52, mouseX, mouseY))
            toolTips.add(recipe.getRfPerTickLang(this.getTier()));
        return toolTips;
    }

    protected boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY)
    {
        return mouseX >= (double) (x - 1) && mouseX < (double) (x + width + 1) && mouseY >= (double) (y - 1) && mouseY < (double) (y + height + 1);
    }
}

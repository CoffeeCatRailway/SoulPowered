package coffeecatrailway.soulpowered.intergration.jei.generator;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.tileentity.CoalGeneratorTileEntity;
import coffeecatrailway.soulpowered.data.gen.SoulLanguage;
import coffeecatrailway.soulpowered.intergration.jei.JeiSoulPlugin;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author CoffeeCatRailway
 * Created: 14/01/2021
 */
public class CoalGeneratorRecipeCategory extends AbstractGeneratorRecipeCategory<CoalGeneratorRecipe>
{
    public CoalGeneratorRecipeCategory(IGuiHelper guiHelper)
    {
        super(guiHelper, new ItemStack(SoulBlocks.SIMPLE_COAL_GENERATOR.get()), SoulLanguage.JEI_CATEGORY_COAL_GENERATOR, CoalGeneratorTileEntity.MAX_ENERGY);
//        ITextComponent rfPerTick = SoulData.Lang.energyPerTick(1_000_000);
//        int stringWidth = Minecraft.getInstance().fontRenderer.getStringWidth(rfPerTick.getString());
    }

    @Override
    protected Tier getTier()
    {
        return Tier.NORMAL;
    }

    @Override
    public ResourceLocation getUid()
    {
        return JeiSoulPlugin.COAL_GENERATOR_ID;
    }

    @Override
    public Class<? extends CoalGeneratorRecipe> getRecipeClass()
    {
        return CoalGeneratorRecipe.class;
    }

    @Override
    public void setIngredients(CoalGeneratorRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputs());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, CoalGeneratorRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup group = layout.getItemStacks();
        group.init(0, true, 2, 17);
        group.set(ingredients);
    }
}

package coffeecatrailway.soulpowered.intergration.jei.generator;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
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
 * Created: 17/01/2021
 */
public class SoulGeneratorRecipeCategory extends AbstractGeneratorRecipeCategory<SoulGeneratorRecipe>
{
    public SoulGeneratorRecipeCategory(IGuiHelper guiHelper)
    {
        super(guiHelper, new ItemStack(SoulBlocks.SOUL_GENERATOR.get()), SoulLanguage.JEI_CATEGORY_SOUL_GENERATOR, SoulGeneratorTileEntity.MAX_ENERGY);
    }

    @Override
    protected Tier getTier()
    {
        return Tier.NORMAL;
    }

    @Override
    public ResourceLocation getUid()
    {
        return JeiSoulPlugin.SOUL_GENERATOR_ID;
    }

    @Override
    public Class<? extends SoulGeneratorRecipe> getRecipeClass()
    {
        return SoulGeneratorRecipe.class;
    }

    @Override
    public void setIngredients(SoulGeneratorRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputs());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, SoulGeneratorRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup group = layout.getItemStacks();
        group.init(0, true, 2, 17);
        group.set(ingredients);
    }
}

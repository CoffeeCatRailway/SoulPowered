package coffeecatrailway.soulpowered.intergration.jei;

import coffeecatrailway.soulpowered.client.gui.screen.AlloySmelterScreen;
import coffeecatrailway.soulpowered.common.item.crafting.AlloySmelterRecipe;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author CoffeeCatRailway
 * Created: 18/12/2020
 */
public class AlloySmelterRecipeCategory implements IRecipeCategory<AlloySmelterRecipe>
{
    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated progress;

    private final String localizedName;

    public AlloySmelterRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(AlloySmelterScreen.TEXTURE, 57, 13, 62, 56);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(SoulBlocks.SOULIUM_ALLOY_SMELTER.get()));

        this.progress = guiHelper.drawableBuilder(AlloySmelterScreen.TEXTURE, 176, 0, 46, 20)
                .buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);

        this.localizedName = SoulBlocks.SOULIUM_ALLOY_SMELTER.get().getTranslatedName().getString();
    }

    @Override
    public ResourceLocation getUid()
    {
        return JeiSoulPlugin.ALLOY_SMELTER_ID;
    }

    @Override
    public Class<? extends AlloySmelterRecipe> getRecipeClass()
    {
        return AlloySmelterRecipe.class;
    }

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
    public void setIngredients(AlloySmelterRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredientsMap().keySet().stream().map(ingredient -> Arrays.asList(ingredient.getMatchingStacks())).collect(Collectors.toList()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, AlloySmelterRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup group = layout.getItemStacks();
        group.init(0, true, 1, 1);
        group.init(1, true, 22, 1);
        group.init(2, true, 43, 1);
        group.init(3, false, 22, 37);

        int i = 0;
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredientsMap().entrySet())
        {
            group.set(i++, Arrays.stream(entry.getKey().getMatchingStacks()).map(s -> {
                        ItemStack stack = s.copy();
                        stack.setCount(entry.getValue());
                        return stack;
                    }).collect(Collectors.toList())
            );
        }
        group.set(3, recipe.getRecipeOutput());
    }

    @Override
    public void draw(AlloySmelterRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        this.progress.draw(matrixStack, 8, 18);
    }
}

package coffeecatrailway.soulpowered.intergration.jei.generator;

import coffeecatrailway.soulpowered.common.tileentity.CoalGeneratorTileEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import java.util.*;

/**
 * @author CoffeeCatRailway
 * Created: 15/01/2021
 */
public class CoalGeneratorRecipeMaker
{
    public static List<CoalGeneratorRecipe> getGeneratorRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers)
    {
        IGuiHelper guiHelper = helpers.getGuiHelper();
        Collection<ItemStack> allStacks = ingredientManager.getAllIngredients(VanillaTypes.ITEM);
        List<CoalGeneratorRecipe> generatorRecipes = new ArrayList<>();

        for (ItemStack stack : allStacks)
        {
            int burnTime = ForgeHooks.getBurnTime(stack);
            if (burnTime > 0)
                generatorRecipes.add(new CoalGeneratorRecipe(guiHelper, Collections.singleton(stack), CoalGeneratorTileEntity.ENERGY_CREATED_PER_TICK, burnTime));
        }

        return generatorRecipes;
    }
}

package coffeecatrailway.soulpowered.intergration.jei.generator;

import coffeecatrailway.soulpowered.api.item.ISoulFuel;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 15/01/2021
 */
public class SoulGeneratorRecipeMaker
{
    public static List<SoulGeneratorRecipe> getGeneratorRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers)
    {
        IGuiHelper guiHelper = helpers.getGuiHelper();
        Collection<ItemStack> allStacks = ingredientManager.getAllIngredients(VanillaTypes.ITEM);
        List<SoulGeneratorRecipe> generatorRecipes = new ArrayList<>();

        for (ItemStack stack : allStacks)
            if (stack.getItem() instanceof ISoulFuel)
                generatorRecipes.add(new SoulGeneratorRecipe(guiHelper, Collections.singleton(stack), SoulGeneratorTileEntity.ENERGY_CREATED_PER_TICK, ((ISoulFuel) stack.getItem()).getBurnTime()));

        return generatorRecipes;
    }
}

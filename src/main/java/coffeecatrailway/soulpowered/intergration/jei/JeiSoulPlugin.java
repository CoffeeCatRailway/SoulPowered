package coffeecatrailway.soulpowered.intergration.jei;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.client.gui.screen.AlloySmelterScreen;
import coffeecatrailway.soulpowered.client.gui.screen.CoalGeneratorScreen;
import coffeecatrailway.soulpowered.client.gui.screen.SoulGeneratorScreen;
import coffeecatrailway.soulpowered.common.inventory.container.AlloySmelterContainer;
import coffeecatrailway.soulpowered.common.inventory.container.CoalGeneratorContainer;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import coffeecatrailway.soulpowered.intergration.jei.generator.CoalGeneratorRecipeCategory;
import coffeecatrailway.soulpowered.intergration.jei.generator.CoalGeneratorRecipeMaker;
import coffeecatrailway.soulpowered.intergration.jei.generator.SoulGeneratorRecipeCategory;
import coffeecatrailway.soulpowered.intergration.jei.generator.SoulGeneratorRecipeMaker;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import coffeecatrailway.soulpowered.registry.SoulRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author CoffeeCatRailway
 * Created: 18/12/2020
 */
@JeiPlugin
public class JeiSoulPlugin implements IModPlugin
{
    private static final ResourceLocation UID = SoulPoweredMod.getLocation("plugin/main");
    public static final Set<Supplier<Item>> POWERED_ITEM_SET = new HashSet<>();

    public static final ResourceLocation ALLOY_SMELTER_ID = SoulPoweredMod.getLocation("category/alloy_smelter");
    public static final ResourceLocation COAL_GENERATOR_ID = SoulPoweredMod.getLocation("category/coal_generator");
    public static final ResourceLocation SOUL_GENERATOR_ID = SoulPoweredMod.getLocation("category/soul_generator");

    @Override
    public ResourceLocation getPluginUid()
    {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration)
    {
        ISubtypeInterpreter interpreter = stack -> {
            if (!EnergyUtils.isPresent(stack))
                return ISubtypeInterpreter.NONE;
            return String.valueOf(EnergyUtils.getIfPresent(stack).orElse(EnergyUtils.EMPTY).getEnergyStored());
        };
        POWERED_ITEM_SET.stream().map(Supplier::get).forEach(item -> registration.registerSubtypeInterpreter(item, interpreter));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new AlloySmelterRecipeCategory(guiHelper),
                new CoalGeneratorRecipeCategory(guiHelper),
                new SoulGeneratorRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        registration.addRecipes(getRecipesOfType(SoulRecipes.ALLOY_SMELTING_TYPE), ALLOY_SMELTER_ID);
        registration.addRecipes(CoalGeneratorRecipeMaker.getGeneratorRecipes(registration.getIngredientManager(), registration.getJeiHelpers()), COAL_GENERATOR_ID);
        registration.addRecipes(SoulGeneratorRecipeMaker.getGeneratorRecipes(registration.getIngredientManager(), registration.getJeiHelpers()), SOUL_GENERATOR_ID);
    }

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> type)
    {
        if (Minecraft.getInstance().world == null) return new ArrayList<>();
        return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toList());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        registration.addRecipeTransferHandler(AlloySmelterContainer.class, ALLOY_SMELTER_ID, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(CoalGeneratorContainer.class, COAL_GENERATOR_ID, 0, 1, 1, 36);
        registration.addRecipeTransferHandler(SoulGeneratorContainer.class, SOUL_GENERATOR_ID, 0, 1, 1, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.SIMPLE_ALLOY_SMELTER.get()), ALLOY_SMELTER_ID);
        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.NORMAL_ALLOY_SMELTER.get()), ALLOY_SMELTER_ID);
        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.SOULIUM_ALLOY_SMELTER.get()), ALLOY_SMELTER_ID);

        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.SIMPLE_COAL_GENERATOR.get()), COAL_GENERATOR_ID);
        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.NORMAL_COAL_GENERATOR.get()), COAL_GENERATOR_ID);

        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.SOUL_GENERATOR.get()), SOUL_GENERATOR_ID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(AlloySmelterScreen.class, 65, 31, 46, 20, ALLOY_SMELTER_ID);
        registration.addRecipeClickArea(CoalGeneratorScreen.class, 80, 52, 14, 14, COAL_GENERATOR_ID);
        registration.addRecipeClickArea(SoulGeneratorScreen.class, 80, 52, 14, 14, SOUL_GENERATOR_ID);
    }
}

package coffeecatrailway.soulpowered.intergration.jei;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.client.gui.screen.AlloySmelterScreen;
import coffeecatrailway.soulpowered.common.inventory.container.AlloySmelterContainer;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import coffeecatrailway.soulpowered.registry.SoulItems;
import coffeecatrailway.soulpowered.registry.SoulRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CoffeeCatRailway
 * Created: 18/12/2020
 */
@JeiPlugin
public class JeiSoulPlugin implements IModPlugin
{
    private static final ResourceLocation UID = SoulPoweredMod.getLocation("plugin/main");

    static final ResourceLocation ALLOY_SMELTER_ID = SoulPoweredMod.getLocation("category/alloy_smelter");

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
        registration.registerSubtypeInterpreter(SoulItems.SIMPLE_BATTERY.get(), interpreter);
        registration.registerSubtypeInterpreter(SoulItems.NORMAL_BATTERY.get(), interpreter);
        registration.registerSubtypeInterpreter(SoulItems.SOULIUM_BATTERY.get(), interpreter);

        registration.registerSubtypeInterpreter(SoulItems.POWERED_SOULIUM_SOUL_AMULET.get(), interpreter);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new AlloySmelterRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        registration.addRecipes(getRecipesOfType(SoulRecipes.ALLOY_SMELTING_TYPE), ALLOY_SMELTER_ID);
    }

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> type)
    {
        if (Minecraft.getInstance().world == null) return new ArrayList<>();
        return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toList());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        registration.addRecipeTransferHandler(AlloySmelterContainer.class, ALLOY_SMELTER_ID, 0, 3, 4, 35);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.SIMPLE_ALLOY_SMELTER.get()), ALLOY_SMELTER_ID);
        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.NORMAL_ALLOY_SMELTER.get()), ALLOY_SMELTER_ID);
        registration.addRecipeCatalyst(new ItemStack(SoulBlocks.SOULIUM_ALLOY_SMELTER.get()), ALLOY_SMELTER_ID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(AlloySmelterScreen.class, 65, 31, 46, 20, ALLOY_SMELTER_ID);
    }
}

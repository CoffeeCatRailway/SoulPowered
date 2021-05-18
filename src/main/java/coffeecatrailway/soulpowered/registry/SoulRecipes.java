package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.item.crafting.AlloySmelterRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 17/12/2020
 */
public class SoulRecipes
{
    private static final Logger LOGGER = SoulMod.getLogger("Recipes");
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SoulMod.MOD_ID);

    public static final IRecipeType<AlloySmelterRecipe> ALLOY_SMELTING_TYPE = registerType("alloy_smelting");
    public static final RegistryObject<IRecipeSerializer<?>> ALLOY_SMELTING = registerSerializer("alloy_smelting", AlloySmelterRecipe.Serializer::new);

    public static void load(IEventBus bus)
    {
        RECIPE_SERIALIZERS.register(bus);
        LOGGER.debug("Loaded");
    }

    private static <T extends IRecipe<?>> IRecipeType<T> registerType(String name)
    {
        return Registry.register(Registry.RECIPE_TYPE, SoulMod.getLocation(name), new IRecipeType<T>()
        {
            @Override
            public String toString()
            {
                return name;
            }
        });
    }

    private static RegistryObject<IRecipeSerializer<?>> registerSerializer(String name, Supplier<IRecipeSerializer<?>> serializer)
    {
        return RECIPE_SERIALIZERS.register(name, serializer);
    }
}

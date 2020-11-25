package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.item.*;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Logger;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 7/11/2020
 */
public class SoulItems
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Items");

    public static final RegistryEntry<Item> SOUL_METAL_INGOT = REGISTRATE.item("soul_metal_ingot", Item::new).defaultLang().defaultModel().tag(Tags.Items.INGOTS, ItemTags.BEACON_PAYMENT_ITEMS)
            .recipe((ctx, provider) -> provider.singleItem(DataIngredient.items(SoulBlocks.SOUL_METAL_BLOCK.get()), ctx::getEntry, 1, 9)).register();

    public static final RegistryEntry<Item> COPPER_INGOT = REGISTRATE.item("copper_ingot", Item::new).defaultLang().defaultModel()
            .tag(Tags.Items.INGOTS, ItemTags.BEACON_PAYMENT_ITEMS, SoulData.TagItems.INGOTS_COPPER)
            .recipe((ctx, provider) -> {
                provider.singleItem(DataIngredient.items(SoulBlocks.COPPER_BLOCK.get()), ctx::getEntry, 1, 9);
                provider.smelting(DataIngredient.items(SoulBlocks.COPPER_ORE), ctx::getEntry, .7f, 200);
                provider.blasting(DataIngredient.items(SoulBlocks.COPPER_ORE), ctx::getEntry, .7f, 100);
            }).register();

    // Misc
    public static final RegistryEntry<SoulBottleItem> SOUL_BOTTLE = REGISTRATE.item("soul_bottle", SoulBottleItem::new).defaultLang().defaultModel()
            .tag(SoulData.TagItems.SOUL_GENERATOR_FUEL).register();

    public static final RegistryEntry<SoulAmuletItem> SOUL_AMULET = registerSoulCurioItem("soul_amulet", "Allows you to gather souls from your kills", SoulAmuletItem::new)
            .defaultLang().defaultModel().properties(prop -> prop.maxStackSize(1)).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .key('b', SoulData.TagItems.SOUL_BLOCKS).key('s', SOUL_BOTTLE.get())
                    .patternLine(" b ").patternLine("b b").patternLine(" s ").addCriterion("has_soul_sand", RegistrateRecipeProvider.hasItem(SoulData.TagItems.SOUL_BLOCKS))
                    .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SOUL_BOTTLE.get())).build(provider)).register();

    private static <T extends SoulCurioItem> ItemBuilder<T, Registrate> registerSoulCurioItem(String id, String description, NonNullFunction<Item.Properties, T> factory)
    {
        SoulData.Lang.EXTRA_LANGS.put("item." + SoulPoweredMod.MOD_ID + "." + id + ".description", description);
        return REGISTRATE.item(id, factory).tag(SoulData.TagItems.CURIOS_NECKLACE);
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}

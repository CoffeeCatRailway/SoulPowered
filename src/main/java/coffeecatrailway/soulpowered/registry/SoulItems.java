package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.item.*;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Supplier;

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

    public static final RegistryEntry<SoulAmuletItem> SOUL_AMULET_IRON = registerSoulAmulet("soul_amulet_iron", prop -> new SoulAmuletItem(prop, 1.5f, .25f),
            "Iron Soul Amulet", () -> Items.IRON_INGOT);
    public static final RegistryEntry<SoulAmuletItem> SOUL_AMULET = registerSoulAmulet("soul_amulet_soul_metal", prop -> new SoulAmuletItem(prop, 2f, .5f),
            "Soul Metal Soul Amulet", SOUL_METAL_INGOT::get);
    public static final RegistryEntry<SoulAmuletPoweredItem> SOUL_AMULET_POWERED = registerSoulAmulet("soul_amulet_powered", prop -> new SoulAmuletPoweredItem(prop, 2.5f, 1f),
            "Powered Soul Amulet", SOUL_METAL_INGOT::get, BATTERY::get, NonNullBiConsumer.noop());

    private static <T extends Item> RegistryEntry<T> registerSoulAmulet(String id, NonNullFunction<Item.Properties, T> item, String name, Supplier<IItemProvider> ingot)
    {
        return registerSoulAmulet(id, item, name, ingot, () -> null, (ctx, provider) -> provider.handheld(ctx::getEntry, SoulPoweredMod.getLocation("item/soul_amulet_gem"))
                .texture("layer1", SoulPoweredMod.getLocation("item/" + ctx.getName())));
    }

    private static <T extends Item> RegistryEntry<T> registerSoulAmulet(String id, NonNullFunction<Item.Properties, T> item, String name, Supplier<IItemProvider> ingot, Supplier<IItemProvider> top, NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> model)
    {
        return REGISTRATE.item(id, item).tag(SoulData.TagItems.CURIOS_NECKLACE).lang(name).model(model)
                .recipe((ctx, provider) -> {
                    boolean flag = top.get() != null;
                    ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', ingot.get()).key('s', SOUL_BOTTLE.get());

                    if (flag)
                        builder.key('t', top.get()).patternLine(" t ");
                    else
                        builder.patternLine(" i ");

                    builder.patternLine("i i").patternLine(" s ")
                            .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SOUL_BOTTLE.get()))
                            .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(ingot.get()));
                    if (flag)
                        builder.addCriterion("has_top", RegistrateRecipeProvider.hasItem(top.get()));
                    builder.build(provider);
                }).register();
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}

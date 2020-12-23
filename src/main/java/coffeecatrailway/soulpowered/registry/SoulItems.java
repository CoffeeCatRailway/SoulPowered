package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.item.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 7/11/2020
 */
public class SoulItems
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Items");

    // Tech
    public static final RegistryEntry<BatteryItem> SIMPLE_BATTERY = REGISTRATE.item("simple_battery", prop -> new BatteryItem(prop, Tier.SIMPLE))
            .defaultModel().defaultLang().recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .key('i', Tags.Items.INGOTS_IRON).key('c', SoulData.TagItems.INGOTS_COPPER).key('r', Tags.Items.DUSTS_REDSTONE).key('s', Tags.Items.STONE)
                    .patternLine("ici").patternLine("srs").patternLine("ici")
                    .addCriterion("has_iron_ingot", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                    .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                    .addCriterion("has_stone", RegistrateRecipeProvider.hasItem(Tags.Items.STONE))
                    .addCriterion("has_redstone", RegistrateRecipeProvider.hasItem(Tags.Items.DUSTS_REDSTONE)).build(provider)).register();

    public static final RegistryEntry<BatteryItem> BATTERY = REGISTRATE.item("battery", prop -> new BatteryItem(prop, Tier.SOULIUM))
            .defaultModel().defaultLang().recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .key('s', SoulItems.SOULIUM_INGOT.get()).key('c', SoulData.TagItems.INGOTS_COPPER).key('b', SoulItems.SOUL_BOTTLE.get())
                    .patternLine("scs").patternLine("sbs").patternLine("scs")
                    .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                    .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                    .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SoulItems.SOUL_BOTTLE.get())).build(provider)).register();

    // Ingots
    public static final RegistryEntry<Item> SOULIUM_INGOT = REGISTRATE.item("soulium_ingot", Item::new).defaultLang().defaultModel().tag(Tags.Items.INGOTS, ItemTags.BEACON_PAYMENT_ITEMS)
            .recipe((ctx, provider) -> provider.singleItem(DataIngredient.items(SoulBlocks.SOULIUM_BLOCK.get()), ctx::getEntry, 1, 9)).register();

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

    public static final RegistryEntry<SoulAmuletItem> GOLD_SOUL_AMULET = registerSoulAmulet("gold_soul_amulet", prop -> new SoulAmuletItem(prop, ItemTier.GOLD, 1.5f, .1f,
            SoulPoweredMod.getLocation("textures/models/gold_soul_amulet.png")), "Golden Soul Amulet", () -> Items.GOLD_INGOT)
            .tag(ItemTags.PIGLIN_LOVED).register();

    public static final RegistryEntry<SoulAmuletItem> COPPER_SOUL_AMULET = registerSoulAmulet("copper_soul_amulet", prop -> new SoulAmuletItem(prop, 100, 1.5f, .15f,
            SoulPoweredMod.getLocation("textures/models/copper_soul_amulet.png")), "Copper Soul Amulet", COPPER_INGOT::get).register();

    public static final RegistryEntry<SoulAmuletItem> IRON_SOUL_AMULET = registerSoulAmulet("iron_soul_amulet", prop -> new SoulAmuletItem(prop, ItemTier.IRON, 1.75f, .25f,
            SoulPoweredMod.getLocation("textures/models/iron_soul_amulet.png")), "Iron Soul Amulet", () -> Items.IRON_INGOT).register();

    public static final RegistryEntry<SoulAmuletItem> DIAMOND_SOUL_AMULET = registerSoulAmulet("diamond_soul_amulet", prop -> new SoulAmuletItem(prop, ItemTier.DIAMOND, 2f, .5f,
            SoulPoweredMod.getLocation("textures/models/diamond_soul_amulet.png")), "Diamond Soul Amulet", () -> Items.DIAMOND).register();

    public static final RegistryEntry<SoulAmuletItem> SOULIUM_SOUL_AMULET = registerSoulAmulet("soulium_soul_amulet", prop -> new SoulAmuletItem(prop, 1750, 2.5f, .75f,
            SoulPoweredMod.getLocation("textures/models/soulium_soul_amulet.png")), "Soulium Soul Amulet", SOULIUM_INGOT::get).register();

    public static final RegistryEntry<SoulAmuletPoweredItem> POWERED_SOULIUM_SOUL_AMULET = registerSoulAmulet("powered_soulium_soul_amulet", prop -> new SoulAmuletPoweredItem(prop, 3f, 1f),
            "Powered Soulium Soul Amulet", SOULIUM_INGOT::get, BATTERY::get, NonNullBiConsumer.noop(), true)
            .recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(SOULIUM_SOUL_AMULET.get()), Ingredient.fromItems(BATTERY.get()), ctx.getEntry())
                    .addCriterion("has_soulium_soul_amulet", RegistrateRecipeProvider.hasItem(SOULIUM_SOUL_AMULET.get()))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(BATTERY.get())).build(provider, ctx.getId())).register();

    public static final RegistryEntry<SoulAmuletItem> NETHERITE_SOUL_AMULET = registerSoulAmulet("netherite_soul_amulet", prop -> new SoulAmuletItem(prop, ItemTier.NETHERITE, 3.5f, 1f,
            SoulPoweredMod.getLocation("textures/models/netherite_soul_amulet.png")), "Netherite Soul Amulet", () -> Items.NETHERITE_INGOT, true)
            .recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(DIAMOND_SOUL_AMULET.get()), Ingredient.fromTag(Tags.Items.INGOTS_NETHERITE), ctx.getEntry())
                    .addCriterion("has_diamond_soul_amulet", RegistrateRecipeProvider.hasItem(DIAMOND_SOUL_AMULET.get()))
                    .addCriterion("has_netherite", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_NETHERITE)).build(provider, ctx.getId())).register();

    public static final RegistryEntry<SoulShieldItem> SOUL_SHIELD = registerCurio(REGISTRATE.item("soul_shield", prop -> new SoulShieldItem(prop, 4f, 10f, 25f))
            .tag(SoulData.TagItems.CURIOS_CHARM).defaultLang().model(NonNullBiConsumer.noop()).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .key('s', Items.SHIELD).key('b', SOUL_BOTTLE.get()).key('i', SOULIUM_INGOT.get()).patternLine("s").patternLine("b").patternLine("i")
                    .addCriterion("has_shield", RegistrateRecipeProvider.hasItem(Items.SHIELD))
                    .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SOUL_BOTTLE.get()))
                    .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(SOULIUM_INGOT.get())).build(provider)), "Uses souls to shield you").register();

    private static <T extends Item> ItemBuilder<T, Registrate> registerCurio(ItemBuilder<T, Registrate> entry, String description)
    {
        SoulData.Lang.EXTRA_LANGS.put("item." + SoulPoweredMod.MOD_ID + "." + entry.getName() + ".description", description);
        return entry;
    }

    private static <T extends Item> ItemBuilder<T, Registrate> registerSoulAmulet(String id, NonNullFunction<Item.Properties, T> item, String name, Supplier<IItemProvider> ingot)
    {
        return registerSoulAmulet(id, item, name, ingot, () -> null, (ctx, provider) -> provider.generated(ctx::getEntry, SoulPoweredMod.getLocation("item/soul_amulet_gem"))
                .texture("layer1", SoulPoweredMod.getLocation("item/" + ctx.getName())), false);
    }

    private static <T extends Item> ItemBuilder<T, Registrate> registerSoulAmulet(String id, NonNullFunction<Item.Properties, T> item, String name, Supplier<IItemProvider> ingot, boolean customRecipe)
    {
        return registerSoulAmulet(id, item, name, ingot, () -> null, (ctx, provider) -> provider.generated(ctx::getEntry, SoulPoweredMod.getLocation("item/soul_amulet_gem"))
                .texture("layer1", SoulPoweredMod.getLocation("item/" + ctx.getName())), customRecipe);
    }

    private static <T extends Item> ItemBuilder<T, Registrate> registerSoulAmulet(String id, NonNullFunction<Item.Properties, T> item, String name, Supplier<IItemProvider> ingot, Supplier<IItemProvider> top, NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> model, boolean customRecipe)
    {
        ItemBuilder<T, Registrate> builder = REGISTRATE.item(id, item).tag(SoulData.TagItems.CURIOS_NECKLACE).lang(name).model(model);

        if (!customRecipe)
            builder = builder.recipe((ctx, provider) -> {
                boolean flag = top.get() != null;
                ShapedRecipeBuilder recipeBuilder = ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', ingot.get()).key('s', SOUL_BOTTLE.get());

                if (flag)
                    recipeBuilder.key('t', top.get()).patternLine(" t ");
                else
                    recipeBuilder.patternLine(" i ");

                recipeBuilder.patternLine("i i").patternLine(" s ")
                        .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SOUL_BOTTLE.get()))
                        .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(ingot.get()));
                if (flag)
                    recipeBuilder.addCriterion("has_top", RegistrateRecipeProvider.hasItem(top.get()));
                recipeBuilder.build(provider);
            });

        return registerCurio(builder, "Allows you to gather souls from your kills");
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}

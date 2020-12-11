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
    public static final RegistryEntry<EnergyItem> BATTERY = REGISTRATE.item("battery", prop -> new EnergyItem(prop, 200_000, 10_000))
            .properties(prop -> prop.maxStackSize(1).rarity(Rarity.UNCOMMON)).defaultModel().defaultLang().recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
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

    public static final RegistryEntry<SoulAmuletItem> GOLD_SOUL_AMULET = registerSoulAmulet("gold_soul_amulet", prop -> new SoulAmuletItem(prop, 1.25f, .1f,
                    SoulPoweredMod.getLocation("textures/models/gold_soul_amulet.png")), "Golden Soul Amulet", () -> Items.GOLD_INGOT);

    public static final RegistryEntry<SoulAmuletItem> COPPER_SOUL_AMULET = registerSoulAmulet("copper_soul_amulet", prop -> new SoulAmuletItem(prop, 1.5f, .15f,
                    SoulPoweredMod.getLocation("textures/models/copper_soul_amulet.png")), "Copper Soul Amulet", COPPER_INGOT::get);

    public static final RegistryEntry<SoulAmuletItem> IRON_SOUL_AMULET = registerSoulAmulet("iron_soul_amulet", prop -> new SoulAmuletItem(prop, 1.5f, .25f,
                    SoulPoweredMod.getLocation("textures/models/iron_soul_amulet.png")), "Iron Soul Amulet", () -> Items.IRON_INGOT);

    public static final RegistryEntry<SoulAmuletItem> DIAMOND_SOUL_AMULET = registerSoulAmulet("diamond_soul_amulet", prop -> new SoulAmuletItem(prop, 1.75f, .5f,
                    SoulPoweredMod.getLocation("textures/models/diamond_soul_amulet.png")), "Diamond Soul Amulet", () -> Items.IRON_INGOT);

    public static final RegistryEntry<SoulAmuletItem> SOULIUM_SOUL_AMULET = registerSoulAmulet("soulium_soul_amulet", prop -> new SoulAmuletItem(prop, 2f, .75f,
                    SoulPoweredMod.getLocation("textures/models/soulium_soul_amulet.png")), "Soulium Soul Amulet", SOULIUM_INGOT::get);

    public static final RegistryEntry<SoulAmuletPoweredItem> POWERED_SOULIUM_SOUL_AMULET = registerSoulAmulet("powered_soul_amulet", prop -> new SoulAmuletPoweredItem(prop, 2.5f, 1f),
            "Powered Soulium Soul Amulet", SOULIUM_INGOT::get, BATTERY::get, NonNullBiConsumer.noop());

    public static final RegistryEntry<SoulShieldItem> SOUL_SHIELD = registerCurio(REGISTRATE.item("soul_shield", prop -> new SoulShieldItem(prop, 4f, 10f, 25f))
            .tag(SoulData.TagItems.CURIOS_CHARM).defaultLang().model(NonNullBiConsumer.noop()).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .key('s', Items.SHIELD).key('b', SOUL_BOTTLE.get()).key('i', SOULIUM_INGOT.get()).patternLine("s").patternLine("b").patternLine("i")
                    .addCriterion("has_shield", RegistrateRecipeProvider.hasItem(Items.SHIELD))
                    .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SOUL_BOTTLE.get()))
                    .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(SOULIUM_INGOT.get())).build(provider)).register(), "Uses souls to shield you");

    private static <T extends Item> RegistryEntry<T> registerCurio(RegistryEntry<T> entry, String description)
    {
        SoulData.Lang.EXTRA_LANGS.put("item." + SoulPoweredMod.MOD_ID + "." + entry.getId().getPath() +  ".description", description);
        return entry;
    }

    private static <T extends Item> RegistryEntry<T> registerSoulAmulet(String id, NonNullFunction<Item.Properties, T> item, String name, Supplier<IItemProvider> ingot)
    {
        return registerSoulAmulet(id, item, name, ingot, () -> null, (ctx, provider) -> provider.handheld(ctx::getEntry, SoulPoweredMod.getLocation("item/soul_amulet_gem"))
                .texture("layer1", SoulPoweredMod.getLocation("item/" + ctx.getName())));
    }

    private static <T extends Item> RegistryEntry<T> registerSoulAmulet(String id, NonNullFunction<Item.Properties, T> item, String name, Supplier<IItemProvider> ingot, Supplier<IItemProvider> top, NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> model)
    {
        return registerCurio(REGISTRATE.item(id, item).tag(SoulData.TagItems.CURIOS_NECKLACE).lang(name).model(model)
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
                }).register(), "Allows you to gather souls from your kills");
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}

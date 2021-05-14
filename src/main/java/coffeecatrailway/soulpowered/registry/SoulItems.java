package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.item.*;
import coffeecatrailway.soulpowered.intergration.jei.JeiSoulPlugin;
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
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
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
    public static final RegistryEntry<BatteryItem> SIMPLE_BATTERY = registerPoweredItem(REGISTRATE.item("simple_battery", prop -> new BatteryItem(prop, Tier.SIMPLE))
            .defaultModel().defaultLang().recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .key('p', ItemTags.PLANKS).key('c', SoulData.TagItems.INGOTS_COPPER).key('r', Tags.Items.DUSTS_REDSTONE).key('s', Tags.Items.RODS_WOODEN)
                    .patternLine("scs").patternLine("prp").patternLine("scs")
                    .addCriterion("has_iron_ingot", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                    .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                    .addCriterion("has_stone", RegistrateRecipeProvider.hasItem(Tags.Items.STONE))
                    .addCriterion("has_redstone", RegistrateRecipeProvider.hasItem(Tags.Items.DUSTS_REDSTONE)).build(provider)), false).register();

    public static final RegistryEntry<BatteryItem> NORMAL_BATTERY = registerPoweredItem(REGISTRATE.item("normal_battery", prop -> new BatteryItem(prop, Tier.NORMAL))
            .defaultModel().lang("Battery").recipe((ctx, provider) -> {
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                        .key('i', Tags.Items.INGOTS_IRON).key('c', SoulData.TagItems.INGOTS_COPPER).key('r', Tags.Items.DUSTS_REDSTONE).key('s', Tags.Items.STONE)
                        .patternLine("ici").patternLine("srs").patternLine("ici")
                        .addCriterion("has_iron_ingot", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                        .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                        .addCriterion("has_stone", RegistrateRecipeProvider.hasItem(Tags.Items.STONE))
                        .addCriterion("has_redstone", RegistrateRecipeProvider.hasItem(Tags.Items.DUSTS_REDSTONE)).build(provider);
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                        .key('s', Tags.Items.STONE).key('o', SIMPLE_BATTERY.get()).key('r', Tags.Items.DUSTS_REDSTONE)
                        .patternLine(" o ").patternLine("srs").patternLine(" o ")
                        .addCriterion("has_stone", RegistrateRecipeProvider.hasItem(Tags.Items.STONE))
                        .addCriterion("has_simple_battery", RegistrateRecipeProvider.hasItem(SIMPLE_BATTERY.get()))
                        .addCriterion("has_redstone", RegistrateRecipeProvider.hasItem(Tags.Items.DUSTS_REDSTONE)).build(provider, SoulPoweredMod.getLocation("normal_battery_from_simple_battery"));
            }), false).register();

    public static final RegistryEntry<BatteryItem> SOULIUM_BATTERY = registerPoweredItem(REGISTRATE.item("soulium_battery", prop -> new BatteryItem(prop, Tier.SOULIUM))
            .defaultModel().defaultLang().recipe((ctx, provider) -> {
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                        .key('s', SoulItems.SOULIUM_INGOT.get()).key('c', SoulData.TagItems.INGOTS_COPPER).key('b', SoulItems.SOUL_BOTTLE.get())
                        .patternLine("scs").patternLine("sbs").patternLine("scs")
                        .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                        .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                        .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SoulItems.SOUL_BOTTLE.get())).build(provider);
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                        .key('s', SoulItems.SOULIUM_INGOT.get()).key('o', NORMAL_BATTERY.get()).key('b', SoulItems.SOUL_BOTTLE.get())
                        .patternLine(" o ").patternLine("sbs").patternLine(" o ")
                        .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                        .addCriterion("has_normal_battery", RegistrateRecipeProvider.hasItem(NORMAL_BATTERY.get()))
                        .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SoulItems.SOUL_BOTTLE.get())).build(provider, SoulPoweredMod.getLocation("soulium_battery_from_normal_battery"));
            }), false).register();

    // Ingots
    public static final RegistryEntry<Item> SOULIUM_INGOT = REGISTRATE.item("soulium_ingot", Item::new).defaultLang().defaultModel().tag(Tags.Items.INGOTS, ItemTags.BEACON_PAYMENT_ITEMS)
            .recipe((ctx, provider) -> provider.singleItem(DataIngredient.items(SoulBlocks.SOULIUM_BLOCK.get()), ctx::getEntry, 1, 9)).register();

    public static final RegistryEntry<Item> COPPER_INGOT = REGISTRATE.item("copper_ingot", Item::new).defaultLang().defaultModel()
            .tag(Tags.Items.INGOTS, ItemTags.BEACON_PAYMENT_ITEMS, SoulData.TagItems.INGOTS_COPPER).register();

    // Weapons & Tools
    public static final RegistryEntry<SwordItem> SOULIUM_SWORD = REGISTRATE.item("soulium_sword", prop -> new SwordItem(SoulItemTier.SOULIUM, 3, -2.4f, prop))
            .defaultLang().model((ctx, provider) -> provider.handheld(ctx::getEntry)).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .patternLine("i").patternLine("i").patternLine("s").key('i', SOULIUM_INGOT.get()).key('s', Tags.Items.RODS_WOODEN)
                    .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(SOULIUM_INGOT.get()))
                    .addCriterion("has_stick", RegistrateRecipeProvider.hasItem(Tags.Items.RODS_WOODEN)).build(provider)).register();

    public static final RegistryEntry<AxeItem> SOULIUM_AXE = REGISTRATE.item("soulium_axe", prop -> new AxeItem(SoulItemTier.SOULIUM, 5f, -3f, prop))
            .defaultLang().model((ctx, provider) -> provider.handheld(ctx::getEntry)).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .patternLine("ii").patternLine("is").patternLine(" s").key('i', SOULIUM_INGOT.get()).key('s', Tags.Items.RODS_WOODEN)
                    .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(SOULIUM_INGOT.get()))
                    .addCriterion("has_stick", RegistrateRecipeProvider.hasItem(Tags.Items.RODS_WOODEN)).build(provider)).register();

    public static final RegistryEntry<PickaxeItem> SOULIUM_PICKAXE = REGISTRATE.item("soulium_pickaxe", prop -> new PickaxeItem(SoulItemTier.SOULIUM, 1, -2.8f, prop))
            .defaultLang().model((ctx, provider) -> provider.handheld(ctx::getEntry)).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .patternLine("iii").patternLine(" s ").patternLine(" s ").key('i', SOULIUM_INGOT.get()).key('s', Tags.Items.RODS_WOODEN)
                    .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(SOULIUM_INGOT.get()))
                    .addCriterion("has_stick", RegistrateRecipeProvider.hasItem(Tags.Items.RODS_WOODEN)).build(provider)).register();

    public static final RegistryEntry<ShovelItem> SOULIUM_SHOVEL = REGISTRATE.item("soulium_shovel", prop -> new ShovelItem(SoulItemTier.SOULIUM, 1.5f, -3f, prop))
            .defaultLang().model((ctx, provider) -> provider.handheld(ctx::getEntry)).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .patternLine("i").patternLine("s").patternLine("s").key('i', SOULIUM_INGOT.get()).key('s', Tags.Items.RODS_WOODEN)
                    .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(SOULIUM_INGOT.get()))
                    .addCriterion("has_stick", RegistrateRecipeProvider.hasItem(Tags.Items.RODS_WOODEN)).build(provider)).register();

    public static final RegistryEntry<HoeItem> SOULIUM_HOE = REGISTRATE.item("soulium_hoe", prop -> new HoeItem(SoulItemTier.SOULIUM, -3, 0f, prop))
            .defaultLang().model((ctx, provider) -> provider.handheld(ctx::getEntry)).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .patternLine("ii").patternLine(" s").patternLine(" s").key('i', SOULIUM_INGOT.get()).key('s', Tags.Items.RODS_WOODEN)
                    .addCriterion("has_ingot", RegistrateRecipeProvider.hasItem(SOULIUM_INGOT.get()))
                    .addCriterion("has_stick", RegistrateRecipeProvider.hasItem(Tags.Items.RODS_WOODEN)).build(provider)).register();

    public static final RegistryEntry<PoweredSouliumSwordItem> POWERED_SOULIUM_SWORD = registerPoweredItem(REGISTRATE.item("powered_soulium_sword", PoweredSouliumSwordItem::new).defaultLang()
            .model(poweredItemModel()).recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(SOULIUM_SWORD.get()), Ingredient.fromItems(SOULIUM_BATTERY.get()), ctx.getEntry())
                    .addCriterion("has_sword", RegistrateRecipeProvider.hasItem(SOULIUM_SWORD.get()))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SOULIUM_BATTERY.get())).build(provider, ctx.getId())), true).register();

    public static final RegistryEntry<PoweredSouliumAxeItem> POWERED_SOULIUM_AXE = registerPoweredItem(REGISTRATE.item("powered_soulium_axe", PoweredSouliumAxeItem::new).defaultLang()
            .model(poweredItemModel()).recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(SOULIUM_AXE.get()), Ingredient.fromItems(SOULIUM_BATTERY.get()), ctx.getEntry())
                    .addCriterion("has_axe", RegistrateRecipeProvider.hasItem(SOULIUM_AXE.get()))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SOULIUM_BATTERY.get())).build(provider, ctx.getId())), true).register();

    public static final RegistryEntry<PoweredSouliumPickaxeItem> POWERED_SOULIUM_PICKAXE = registerPoweredItem(REGISTRATE.item("powered_soulium_pickaxe", PoweredSouliumPickaxeItem::new).defaultLang()
            .model(poweredItemModel()).recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(SOULIUM_PICKAXE.get()), Ingredient.fromItems(SOULIUM_BATTERY.get()), ctx.getEntry())
                    .addCriterion("has_pickaxe", RegistrateRecipeProvider.hasItem(SOULIUM_PICKAXE.get()))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SOULIUM_BATTERY.get())).build(provider, ctx.getId())), true).register();

    public static final RegistryEntry<PoweredSouliumShovelItem> POWERED_SOULIUM_SHOVEL = registerPoweredItem(REGISTRATE.item("powered_soulium_shovel", PoweredSouliumShovelItem::new).defaultLang()
            .model(poweredItemModel()).recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(SOULIUM_SHOVEL.get()), Ingredient.fromItems(SOULIUM_BATTERY.get()), ctx.getEntry())
                    .addCriterion("has_shovel", RegistrateRecipeProvider.hasItem(SOULIUM_SHOVEL.get()))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SOULIUM_BATTERY.get())).build(provider, ctx.getId())), true).register();

    public static final RegistryEntry<PoweredSouliumHoeItem> POWERED_SOULIUM_HOE = registerPoweredItem(REGISTRATE.item("powered_soulium_hoe", PoweredSouliumHoeItem::new).defaultLang()
            .model(poweredItemModel()).recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(SOULIUM_HOE.get()), Ingredient.fromItems(SOULIUM_BATTERY.get()), ctx.getEntry())
                    .addCriterion("has_hoe", RegistrateRecipeProvider.hasItem(SOULIUM_HOE.get()))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SOULIUM_BATTERY.get())).build(provider, ctx.getId())), true).register();

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

    public static final RegistryEntry<SoulAmuletPoweredItem> POWERED_SOULIUM_SOUL_AMULET = registerPoweredItem(registerSoulAmulet("powered_soulium_soul_amulet", prop -> new SoulAmuletPoweredItem(prop, 3f, 1f),
            "Powered Soulium Soul Amulet", SOULIUM_INGOT::get, SOULIUM_BATTERY::get, NonNullBiConsumer.noop(), true)
            .recipe((ctx, provider) -> SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(SOULIUM_SOUL_AMULET.get()), Ingredient.fromItems(SOULIUM_BATTERY.get()), ctx.getEntry())
                    .addCriterion("has_soulium_soul_amulet", RegistrateRecipeProvider.hasItem(SOULIUM_SOUL_AMULET.get()))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SOULIUM_BATTERY.get())).build(provider, ctx.getId())), true).register();

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

    private static <T extends Item> ItemBuilder<T, Registrate> registerPoweredItem(ItemBuilder<T, Registrate> item, boolean hasModelProperty)
    {
        if (hasModelProperty)
            SoulPoweredMod.POWERED_ITEM_PROPERTY_SET.add(() -> item.get().get());
        JeiSoulPlugin.POWERED_ITEM_SET.add(() -> item.get().get());
        return item;
    }

    private static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> poweredItemModel()
    {
        return (ctx, provider) -> {
            ItemModelBuilder model = provider.handheld(ctx::getEntry);

            ModelFile off = provider.withExistingParent(ctx.getName() + "_off", SoulPoweredMod.getLocation("item/" + ctx.getName()))
                    .texture("layer0", SoulPoweredMod.getLocation("item/" + ctx.getName()));
            ModelFile on = provider.withExistingParent(ctx.getName() + "_on", SoulPoweredMod.getLocation("item/" + ctx.getName()))
                    .texture("layer0", SoulPoweredMod.getLocation("item/" + ctx.getName() + "_on"));

            model.override().predicate(SoulPoweredMod.POWERED_ITEM_PROPERTY, 0f).model(off).end()
                    .override().predicate(SoulPoweredMod.POWERED_ITEM_PROPERTY, 1f).model(on).end();
        };
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}

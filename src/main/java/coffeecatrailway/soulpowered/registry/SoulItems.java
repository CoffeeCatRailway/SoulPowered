package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.item.SoulAmuletItem;
import coffeecatrailway.soulpowered.common.item.SoulBottleItem;
import coffeecatrailway.soulpowered.common.item.SoulCurioItem;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Logger;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 7/11/2020
 */
public class SoulItems
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Items");

    public static final RegistryEntry<SoulBottleItem> SOUL_BOTTLE = REGISTRATE.item("soul_bottle", SoulBottleItem::new).defaultLang().defaultModel()
            .tag(SoulData.TagItems.SOUL_GENERATOR_FUEL).register();

    public static final RegistryEntry<SoulAmuletItem> SOUL_AMULET = registerSoulCurioItem("soul_amulet", "Allows you to gather souls from your kills", SoulAmuletItem::new)
            .defaultLang().defaultModel().properties(prop -> prop.maxStackSize(1)).recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                    .key('b', SoulData.TagItems.SOUL_BLOCKS).key('s', SOUL_BOTTLE.get())
                    .patternLine(" b ").patternLine("b b").patternLine(" s ").addCriterion("has_soul_sand", RegistrateRecipeProvider.hasItem(SoulData.TagItems.SOUL_BLOCKS))
                    .addCriterion("has_soul_bottle", RegistrateRecipeProvider.hasItem(SOUL_BOTTLE.get())).build(provider)).register();

    private static <T extends SoulCurioItem> ItemBuilder<T, Registrate> registerSoulCurioItem(String id, String description, NonNullFunction<Item.Properties, T> factory)
    {
        SoulData.SoulLang.EXTRA_LANGS.put("item." + SoulPoweredMod.MOD_ID + "." + id + ".description", description);
        return REGISTRATE.item(id, factory).tag(SoulData.TagItems.CURIOS_NECKLACE);
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}

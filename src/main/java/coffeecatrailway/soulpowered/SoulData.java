package coffeecatrailway.soulpowered;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulData
{
    public static class SoulLang implements NonNullConsumer<RegistrateLangProvider>
    {
        public static final Map<String, String> EXTRA_LANGS = new HashMap<>();

        @Override
        public void accept(RegistrateLangProvider provider)
        {
            EXTRA_LANGS.forEach(provider::add);

            provider.add("commands.souls.get", "%1$s has %2$s soul(s)");
            provider.add("commands.souls.modify.add", "Gave %2$s soul(s) to %1$s");
            provider.add("commands.souls.modify.remove", "Removed %2$s soul(s) to %1$s");
            provider.add("commands.souls.modify.set", "%1$s now has %2$s soul(s)");

            provider.add("item.soul_curio.description.range", TextFormatting.GOLD + "Range: " + TextFormatting.YELLOW + "%s Blocks");
            provider.add("item.soul_curio.description.soul_gathering", TextFormatting.GOLD + "Soul Gathering: " + TextFormatting.YELLOW + "x%s");
        }
    }

    public static class TagItems implements NonNullConsumer<RegistrateTagsProvider<Item>>
    {
        public static final ITag.INamedTag<Item> SOUL_ITEMS = ItemTags.createOptional(SoulPoweredMod.getLocation("soul_items"));
        public static final ITag.INamedTag<Item> CURIOS_NECKLACE = ItemTags.createOptional(new ResourceLocation("curios", "necklace"));

        @Override
        public void accept(RegistrateTagsProvider<Item> provider)
        {
            provider.getOrCreateBuilder(SOUL_ITEMS).add(Items.SOUL_SAND, Items.SOUL_SOIL);
        }
    }
}

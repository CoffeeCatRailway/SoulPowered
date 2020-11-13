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

            provider.add("commands.soulpowered.souls.get", "%1$s has %2$s soul(s)");
            provider.add("commands.soulpowered.souls.modify.add", "Gave %2$s soul(s) to %1$s");
            provider.add("commands.soulpowered.souls.modify.remove", "Removed %2$s soul(s) to %1$s");
            provider.add("commands.soulpowered.souls.modify.set", "%1$s now has %2$s soul(s)");

            provider.add("item.soulpowered.soul_curio.description.range", TextFormatting.GOLD + "Range: " + TextFormatting.YELLOW + "%s Blocks");
            provider.add("item.soulpowered.soul_curio.description.soul_gathering", TextFormatting.GOLD + "Soul Gathering: " + TextFormatting.YELLOW + "x%s");

            provider.add("misc.soulpowered.energy", "%s SE");
            provider.add("misc.soulpowered.energy_per_tick", "%s SE/t");
            provider.add("misc.soulpowered.energy_with_max", "%s / %s SE");
            provider.add("misc.soulpowered.fluid_with_max", "%s / %s mB");
            provider.add("misc.soulpowered.redstone_mode", "Redstone Mode: %s");

            provider.add("container.soulpowered.soul_generator", "Soul Generator");
        }
    }

    public static class TagItems implements NonNullConsumer<RegistrateTagsProvider<Item>>
    {
        public static final ITag.INamedTag<Item> SOUL_BLOCKS = ItemTags.createOptional(SoulPoweredMod.getLocation("soul_blocks"));
        public static final ITag.INamedTag<Item> SOUL_GENERATOR_FUEL = ItemTags.createOptional(SoulPoweredMod.getLocation("soul_generator_fuel"));

        public static final ITag.INamedTag<Item> CURIOS_NECKLACE = ItemTags.createOptional(new ResourceLocation("curios", "necklace"));

        @Override
        public void accept(RegistrateTagsProvider<Item> provider)
        {
            provider.getOrCreateBuilder(SOUL_BLOCKS).add(Items.SOUL_SAND, Items.SOUL_SOIL);
        }
    }
}

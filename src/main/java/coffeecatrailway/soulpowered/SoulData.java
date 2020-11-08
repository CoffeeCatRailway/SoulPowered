package coffeecatrailway.soulpowered;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulData
{
    public static class SoulLang implements NonNullConsumer<RegistrateLangProvider>
    {
        @Override
        public void accept(RegistrateLangProvider provider)
        {
            provider.add("commands.souls.get", "%1$s has %2$s soul(s)");
            provider.add("commands.souls.modify.set", "%1$s now has %2$s soul(s)");
        }
    }

    public static class TagItems implements NonNullConsumer<RegistrateTagsProvider<Item>>
    {
        public static final ITag.INamedTag<Item> CURIOS_NECKLACE = ItemTags.createOptional(new ResourceLocation("curios", "necklace"));

        @Override
        public void accept(RegistrateTagsProvider<Item> provider)
        {
            provider.getOrCreateBuilder(SOUL_ITEMS).add(Items.SOUL_SAND, Items.SOUL_SOIL);
        }
    }
}
